package org.kuali.student.sonar.database.utility;

import org.kuali.common.util.LocationUtils;
import org.kuali.student.sonar.database.exception.*;
import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.sonar.api.scan.filesystem.PathResolver;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/28/13
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class FKConstraintValidator {
    private Connection conn;
    private String missing_FK_query_sql_filename;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbDriver;

    public static final String FK_QUERY_PATH = "sql/";

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }


    public Connection getConn() throws SQLException{
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName(this.dbDriver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find DB Driver Class", e);
            }
            //properties for creating connection to Oracle database
            Properties props = new Properties();
            props.setProperty("user", this.getDbUser());
            props.setProperty("password", this.getDbPassword());


            //creating connection to Oracle database using JDBC
            conn = DriverManager.getConnection(this.dbUrl, props);

        }
        return conn;
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

    public String getMissing_FK_query_sql_filename() {
        if (missing_FK_query_sql_filename == null) {
            missing_FK_query_sql_filename = "missing_FK_query.sql";
        }
        return missing_FK_query_sql_filename;
    }


    public FKConstraintReport runFKSQL(ClassLoader classLoader) throws SQLException {

        String sql = LocationUtils.toString(new PathResolver().relativeFile(new File(FK_QUERY_PATH), getMissing_FK_query_sql_filename()));

        Statement stmt = null;
        ResultSet result = null;
        // TODO: KSENROLL-6924 create a report structure and move out of test
        // unconstrained foreign key relationships
        ArrayList<ForeignKeyConstraint> newConstraintList = new ArrayList<ForeignKeyConstraint>();
        FKConstraintReport report = new FKConstraintReport();


        try {
            //creating PreparedStatement object to execute query
            stmt = this.getConn().createStatement();


            result = stmt.executeQuery(sql);

            while (result.next()) {
                ForeignKeyConstraint constraint = new ForeignKeyConstraint(result);
                if (result.getString("owner") == null) {
                    report.addFieldMappingIssue(constraint);
                } else if (result.getString("constraint_name") == null) {
                    newConstraintList.add(constraint);
                }
            }
            System.out.println("\n****    Done Compiling Field Mappings    ****\n");
        } catch (SQLException e) {
            throw new SQLException("error running missing FK sql. ", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (result != null) {
                result.close();
            }
        }

        System.out.println("Adding constraints and detecting issues");
        for (ForeignKeyConstraint constraint : newConstraintList) {
            try {
                constraint.addFKConstraint(this.getConn());
            } catch (TableMappingException tme) {
                report.addTableMappingIssue(constraint);
            } catch (ColumnTypeIncompatException cte) {
                report.addColumnTypeIncompatabilityIssue(constraint);
            } catch (ParentKeysMissingException pke) {
                report.addOrphanedDataIssue(constraint);
            } catch (UnknownFKExecption unknownFKExecption) {
                report.addOtherIssue(constraint);
            }
        }

        return report;
    }

    public void revert() throws MissingFieldException, SQLException {
        List<ForeignKeyConstraint> constraintList = null;
        try {
            constraintList = FKGenerationUtil.getGeneratedFKConstraints(this.getConn());
        } catch (SQLException e) {
            System.out.println("error retrieving generated constraints: " + e.getErrorCode() + " " + e.getMessage());
        }


        System.out.println("\nDeleting " + constraintList.size() + " FK Constraints");

        for (ForeignKeyConstraint constraint : constraintList)  {
            constraint.deleteFKConstraint(this.getConn());
        }
    }

    public void closeConn() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }
}
