package org.kuali.student.sonar.database;

import org.junit.Before;
import org.junit.Test;
import org.kuali.student.sonar.database.utility.FKProcessesor;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import static org.junit.Assert.fail;


public class TestDatabaseIntegrityScript {




    private Connection conn;
    private String missing_FK_query_sql_filename;
    FKProcessesor fkProcessor;

    @Before
    public void init() {
        getConn();
    }

    public Connection getConn() {
        if (conn == null) {
            //URL of Oracle database server
            String url = "jdbc:oracle:thin:@localhost:1521:xe";

            //properties for creating connection to Oracle database
            Properties props = new Properties();
            props.setProperty("user", "KSBUNDLED");
            props.setProperty("password", "KSBUNDLED");


            try {
                //creating connection to Oracle database using JDBC
                conn = DriverManager.getConnection(url,props);

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

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setMissing_FK_query_sql_filename(String missing_FK_query_sql_filename) {
        this.missing_FK_query_sql_filename = missing_FK_query_sql_filename;
    }



    @Test
    public void testConn() {

        String sql ="select sysdate as current_day from dual";

        try {
            //creating PreparedStatement object to execute query
            PreparedStatement preStatement = conn.prepareStatement(sql);


            ResultSet result = preStatement.executeQuery();

            while(result.next()){
                System.out.println("Current Date from Oracle : " +         result.getString("current_day"));
            }
            System.out.println("done");
        } catch (SQLException e) {
            fail("error testing connection: " + e.getErrorCode() + " " + e.getMessage());
        }
    }

    @Test
    public void testFKSQL() {

        String sql = fkProcessor.getFKSQL(getMissing_FK_query_sql_filename());
        try {
            //creating PreparedStatement object to execute query
            Statement stmt = conn.createStatement();


            ResultSet result = stmt.executeQuery(sql);

            while(result.next()){
                if (result.getString("owner") == null) {
                    System.out.println("FIELD MAPPING ISSUE: Field does not exists (" +
                            result.getString("foreign_table")+ "." +
                            result.getString("foreign_column") + ")");
                } else if (result.getString("constraint_name") == null) {
                    System.out.println("MISSING FK CONSTRAINT: " +
                            result.getString("local_table") + "." +
                            result.getString("local_column") + " -> " +
                            result.getString("foreign_table")+ "." +
                            result.getString("foreign_column"));
                }
            }
            System.out.println("done");
        } catch (SQLException e) {
            fail("error running missing FK sql: " + e.getErrorCode() + " " + e.getMessage());
        }
    }

    @Test
    public void testAddFKs() {

    }


}