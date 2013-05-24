package org.kuali.student.sonar.database;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.kuali.student.sonar.database.exception.*;
import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.kuali.student.sonar.database.utility.FKGenerationUtil;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Runs Database integrity checks by searching for unconstrained FK Relationships.
 * Finds Problems with the Mappings that are returned by the search.  It then attempts
 * to create the FK Constraint.  The addConstraint method will throw excetpions if
 * there are any issues which get handled here by adding to lists of Constraint
 * Issues.  At the end of the test the report is sent to System.out
 */
public class TestDatabaseIntegrityScript {

    private Connection conn;
    private String missing_FK_query_sql_filename;

    @Before
    public void init() {
        getConn();
    }

    public Connection getConn() {
        if (conn == null) {
            //URL of Oracle database server
            String url = "jdbc:oracle:thin:@lsymms-dev.no-ip.org:1521:xe";

            //properties for creating connection to Oracle database
            Properties props = new Properties();
            props.setProperty("user", "KSBUNDLED");
            props.setProperty("password", "KSBUNDLED");


            try {
                //creating connection to Oracle database using JDBC
                conn = DriverManager.getConnection(url, props);

            } catch (SQLException e) {
                fail("unable to establish connection: " + e.getErrorCode() + " " + e.getMessage());
            }
        }
        return conn;
    }

    public String getMissing_FK_query_sql_filename() {
        if (missing_FK_query_sql_filename == null) {
            missing_FK_query_sql_filename = "sql/missing_FK_query.sql";
        }
        return missing_FK_query_sql_filename;
    }

    /*
     * Spring method
     *
     * @conn active connection to an oracle datasource
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /*
     * Spring method
     */
    public void setMissing_FK_query_sql_filename(String missing_FK_query_sql_filename) {
        this.missing_FK_query_sql_filename = missing_FK_query_sql_filename;
    }



    @Test
    public void testConn() throws SQLException {

        String findFKsSQL ="select sysdate as current_day from dual";
        PreparedStatement stmt = null;

        try {
            //creating PreparedStatement object to execute query
            stmt = conn.prepareStatement(findFKsSQL);


            ResultSet result = stmt.executeQuery();

            while(result.next()){
                System.out.println("Current Date from Oracle : " +         result.getString("current_day"));
            }
            System.out.println("done");
        } catch (SQLException e) {
            fail("error testing connection: " + e.getErrorCode() + " " + e.getMessage());
        }  finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    @Test
    public void testFKSQL() throws SQLException {

        String sql = FKGenerationUtil.getSqlFromFile(getMissing_FK_query_sql_filename());
        Statement stmt = null;
        // TODO: KSENROLL-6924 create a report structure and move out of test
        // unconstrained foreign key relationships
        ArrayList<ForeignKeyConstraint> newConstraintList = new ArrayList<ForeignKeyConstraint>();
        // field mapping issues
        ArrayList<ForeignKeyConstraint> fmeConstraintList = new ArrayList<ForeignKeyConstraint>();
        // table mapping issues
        ArrayList<ForeignKeyConstraint> tmeConstraintList = new ArrayList<ForeignKeyConstraint>();
        // column type incompatibility issues
        ArrayList<ForeignKeyConstraint> cteConstraintList = new ArrayList<ForeignKeyConstraint>();
        // orphaned relationships
        ArrayList<ForeignKeyConstraint> orphConstraintList = new ArrayList<ForeignKeyConstraint>();
        // other issues
        ArrayList<ForeignKeyConstraint> othConstraintList = new ArrayList<ForeignKeyConstraint>();


        try {
            //creating PreparedStatement object to execute query
            stmt = conn.createStatement();


            ResultSet result = stmt.executeQuery(sql);

            while(result.next()){
                ForeignKeyConstraint constraint = new ForeignKeyConstraint(result);
                if (result.getString("owner") == null) {
                    fmeConstraintList.add(constraint);
                } else if (result.getString("constraint_name") == null) {
                    newConstraintList.add(constraint);
                }
            }
            System.out.println("\n****    Done Compiling Field Mappings    ****\n");
        } catch (SQLException e) {
            fail("error running missing FK sql: " + e.getErrorCode() + " " + e.getMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }

        System.out.println("Adding constraints and detecting issues");
        for (ForeignKeyConstraint constraint : newConstraintList) {
            try {
                constraint.addFKConstraint(conn);
            } catch (TableMappingException tme) {
                tmeConstraintList.add(constraint);
            } catch (ColumnTypeIncompatException cte) {
                cteConstraintList.add(constraint);
            } catch (ParentKeysMissingException pke) {
                orphConstraintList.add(constraint);
            } catch (UnknownFKExecption unknownFKExecption) {
                othConstraintList.add(constraint);
            }
        }

        System.out.println("\n****    Done Adding constraints and Detecting Orphaned Data    *****\n");

        System.out.println("\nReporting Issues\n");
        for (ForeignKeyConstraint constraint : fmeConstraintList) {
            System.out.println("FIELD MAPPING ISSUE: Field does not exists (" +
                    constraint.foreignTable + "." +
                    constraint.foreignColumn + ")");
        }

        for (ForeignKeyConstraint constraint : tmeConstraintList) {
            System.out.println("TABLE MAPPING ISSUE: table " +
                    constraint.foreignTable +
                    " not found in constraint: " + constraint.toString());
        }

        for (ForeignKeyConstraint constraint : cteConstraintList) {
            System.out.println("COLUMN TYPE INCOMPATIBILITY ISSUE: " +
                    constraint.toString());
        }

        for (ForeignKeyConstraint constraint : orphConstraintList) {
            System.out.println("ORPHANED DATA FOR RELATIONSHIP: " +
                    constraint.toString());
        }

        for (ForeignKeyConstraint constraint : othConstraintList) {
            System.out.println("UNHANDLED CONSTRAINT MAPPING ISSUE: " +
                    constraint.toString() + " (see log for more details)");
        }

    }

    @After
    public void cleanup() throws MissingFieldException, SQLException {
        List<ForeignKeyConstraint> constraintList = null;
        try {
            constraintList = FKGenerationUtil.getGeneratedFKConstraints(conn);
        } catch (SQLException e) {
            System.out.println("error retrieving generated constraints: " + e.getErrorCode() + " " + e.getMessage());
        }


        System.out.println("\nDeleting " + constraintList.size() + " FK Constraints");

        for (ForeignKeyConstraint constraint : constraintList)  {
            constraint.deleteFKConstraint(conn);
        }
    }

}