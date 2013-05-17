package org.kuali.student.sonar.database.utility;

import org.kuali.student.sonar.database.ForeignKeyConstraint;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 3:36 PM
 *
 * This Class Provides SQL statments and helper methods for doing Database Integrity Checks
 */
public class FKGenerationUtil {
    private static int fKseq = 0;
    private static final String fkPrefix = "DB_INGRTY_CHK_FK_";

    /*
     * Loads the SQL from a resource file.  Assumes a single statement
     *
     * @param filename the path in the classpath to get the file
     * @return the SQL statement from the file
     */
    public static String getSqlFromFile(String filename) {
        InputStream is;
        String sql;
        byte[] b;

        is = ClassLoader.getSystemResourceAsStream(filename);
        if (is == null) {
            throw new RuntimeException("error reading file " + filename);
        }

        try {
            b = new byte[is.available()];
            is.read(b);
            sql = new String(b);
        } catch (IOException e) {
            throw new RuntimeException("error reading file " + filename, e);
        }
        return sql;
    }

    /*
     * Returns a String to add a FK Constraint base on fkConstraint values.
     * This will generate a FK name based on a static seq and a prefix.
     * Generally you want Constraint names to be meaningful but in this case
     * the purpose is for testing and these constraints should be cleaned
     * up after the tests or reports have finished.
     *
     * @param fkConstraint contains the table mapping needed to generate the constraint
     * @return the ALTER statment
     */
    public static String getGeneratedAlterStmt(ForeignKeyConstraint fkConstraint) {
        fKseq++;

        return "ALTER TABLE " + fkConstraint.localTable +
                " ADD CONSTRAINT " + fkPrefix + fKseq +
                " FOREIGN KEY (" + fkConstraint.localColumn + ") " +
                "REFERENCES " + fkConstraint.foreignTable + " (" + fkConstraint.foreignColumn + " )";
    }

    /*
     * Returns a list of constraints that were generated to test database integrity in general for deletion
     *
     * @param conn an active oracle connection
     * @throws SQLException if cursor fails to close
     */
    public static List<ForeignKeyConstraint> getGeneratedFKConstraints(Connection conn) throws SQLException{
        String findConstraintsSQL =
                "SELECT \n" +
                "    l.table_name AS local_table, \n" +
                "    l.column_name AS local_column, \n" +
                "    f.table_name AS foreign_table, \n" +
                "    f.column_name AS foreign_column, \n" +
                "    l.constraint_name AS constraint_name \n" +
                "FROM all_cons_columns l\n" +
                "    JOIN all_constraints c ON l.constraint_name=c.constraint_name\n" +
                "    JOIN all_cons_columns f ON f.constraint_name = c.r_constraint_name\n" +
                "WHERE l.constraint_name LIKE '" + fkPrefix + "%'";

        Statement stmt = null;
        ArrayList<ForeignKeyConstraint> constraintList = new ArrayList<>();

        try {
            stmt= conn.createStatement();
            ResultSet result = stmt.executeQuery(findConstraintsSQL);
            while(result.next()){
                constraintList.add(new ForeignKeyConstraint(result));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to select generated FK Constraints with: " + findConstraintsSQL, e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return constraintList;
    }


}
