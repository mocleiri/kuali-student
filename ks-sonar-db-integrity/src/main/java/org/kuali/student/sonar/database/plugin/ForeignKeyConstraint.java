package org.kuali.student.sonar.database.plugin;

import org.apache.commons.lang.StringUtils;
import org.kuali.student.sonar.database.exception.*;
import org.kuali.student.sonar.database.utility.FKGenerationUtil;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 9:11 PM
 *
 * Entity class for managing FK Constraints
 */
public class ForeignKeyConstraint extends Resource{
    public String localTable;
    public String localColumn;
    public String foreignTable;
    public String foreignColumn;
    public String constraintName;

    public ForeignKeyConstraint(String localTable, String localColumn, String foreignTable, String foreignColumn, String constraintName) {
        this.localTable = localTable;
        this.localColumn = localColumn;
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
        this.constraintName = constraintName;

        this.setKey("foreign.key.constraint.resource");
    }

    /** Assumes the resultset contains the field names
     * "local_table", "local_column", "foreign_table", and "foreign_column"
     *
     * @param resultSet		an open cursor - careful not to do anything inefficient here
     * @throws SQLException when something really bad happens to the cursor
     */
    public ForeignKeyConstraint(ResultSet resultSet) throws SQLException {
        localTable = resultSet.getString("local_table");
        localColumn = resultSet.getString("local_column");
        foreignTable = resultSet.getString("foreign_table");
        foreignColumn  = resultSet.getString("foreign_column");
        constraintName = resultSet.getString("constraint_name");
        if (constraintName == null) {
            constraintName = "";
        }

        this.setKey("foreign.key.constraint.resource");
    }

    public String toString() {
        return  "ConstraintName (" + constraintName + ") " +
                localTable + "." + localColumn + " -> " +
                foreignTable + "." + foreignColumn;
    }

    /** Adds the constraint to the local table
     *
     * @param conn		An active DB connection
     * @throws ParentKeysMissingException if there is orphaned data
     * @throws TableMappingException if a table name doesn't exist
     * @throws ColumnTypeIncompatException if field type from local table doesn't match primary key from foreign table
     * @throws UnknownFKExecption if an unknown error occurs
     * @throws SQLException if cursor fails to close
     */
    public void addFKConstraint(Connection conn) throws ParentKeysMissingException, TableMappingException, ColumnTypeIncompatException, UnknownFKExecption, SQLException {

        Statement stmt = null;
        String alterSQL = FKGenerationUtil.getGeneratedAlterStmt(this);

        try {
            stmt = conn.createStatement();

            stmt.executeUpdate(alterSQL);
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 2298:
                    throw new ParentKeysMissingException(this);
                case 942:
                    throw new TableMappingException(this);
                case 2267:
                    throw new ColumnTypeIncompatException(this);
                default:
                    System.out.println("ERROR CREATING FK CONSTRAINT " +
                            this.toString() +
                            "\n   ERROR CODE: " + e.getErrorCode() + " ERROR MESSAGE: " + e.getMessage() +
                            "   ALTER STMT: " + alterSQL);
                    throw new UnknownFKExecption(this);
            }
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }


    /** Drops the constraint from the local table
     *
     * @param conn		An active DB connection
     * @throws MissingFieldException if the constraintName or localTable fields are empty
     * @throws SQLException if cursor fails to close
     */
    public void deleteFKConstraint(Connection conn) throws MissingFieldException, SQLException {

        if (StringUtils.isEmpty(this.constraintName)) {
            throw new MissingFieldException("constraintName");
        }

        if (StringUtils.isEmpty(this.localTable)) {
            throw new MissingFieldException("localTable");
        }

        String alterSQL = "ALTER TABLE " + this.localTable + " DROP CONSTRAINT " + this.constraintName;
        Statement stmt  = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(alterSQL);
        } catch (SQLException e) {
            throw new SQLException("Failed to delete a FK Constraint with: " + alterSQL, e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }


    }

    @Override
    public String getName() {
        return constraintName;
    }

    @Override
    public String getLongName() {
        return constraintName;
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public Language getLanguage() {
        return null;  //TODO: implement this
    }

    @Override
    public String getScope() {
        // scope is impex dir so it doesn't try to impex it
        return Scopes.DIRECTORY;
    }

    @Override
    public String getQualifier() {
        //TODO: figure out what this does and implement it properly
        // required field for DB
        return "qualifier";
    }

    @Override
    public Resource getParent() {
        return null;  //TODO: implement this
    }

    @Override
    public boolean matchFilePattern(String s) {
        return false;  //TODO: implement this
    }
}
