package org.kuali.student.sonar.database.utility;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.kuali.common.util.LocationUtils;
import org.kuali.student.sonar.database.exception.ColumnTypeIncompatException;
import org.kuali.student.sonar.database.exception.MissingFieldException;
import org.kuali.student.sonar.database.exception.NonPKMappingException;
import org.kuali.student.sonar.database.exception.ParentKeysMissingException;
import org.kuali.student.sonar.database.exception.TableMappingException;
import org.kuali.student.sonar.database.exception.UnknownFKExecption;
import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.scan.filesystem.PathResolver;

public class FKConstraintValidator {
    private static final Logger LOG = LoggerFactory.getLogger(FKConstraintValidator.class);

    protected static final String OWNER_KEY = "owner";

    protected static final String CONSTRAINT_NAME_KEY = "constraint_name";

    protected ForeignKeyValidationContext context;

    public ForeignKeyValidationContext getContext() {
        return context;
    }

    public void setContext(ForeignKeyValidationContext context) {
        this.context = context;
    }

    public FKConstraintReport runFKSQL(ClassLoader classLoader) throws SQLException {

        String sql = LocationUtils.toString(new PathResolver().relativeFile(new File(context.getQueryFilePath()), context.getQueryFileName()));

        Statement stmt = null;
        ResultSet result = null;
        // unconstrained foreign key relationships
        ArrayList<ForeignKeyConstraint> newConstraintList = new ArrayList<ForeignKeyConstraint>();
        FKConstraintReport report = new FKConstraintReport();


        try {
            //creating PreparedStatement object to execute query
            stmt = context.getConnection().createStatement();
            result = stmt.executeQuery(sql);

            while (result.next()) {
                ForeignKeyConstraint constraint = new ForeignKeyConstraint(result);
                constraint.constraintName = FKGenerationUtil.getNextConstraintName();
                if (result.getString(OWNER_KEY) == null) {
                    constraint.setErrorMessage("Field does not exists (" +
                            constraint.foreignTable + "." +
                            constraint.foreignColumn + ")");
                    report.addFieldMappingIssue(constraint);
                } else if (result.getString(CONSTRAINT_NAME_KEY) == null) {
                    newConstraintList.add(constraint);
                }
            }

            LOG.debug("****    Done Compiling Field Mappings    ****");
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

        LOG.debug("Adding constraints and detecting issues");
        for (ForeignKeyConstraint constraint : newConstraintList) {
            try {
                constraint.addFKConstraint(context.getConnection());
            } catch (TableMappingException tme) {
                constraint.setErrorMessage("Table not found (" + constraint.foreignTable + ")");
                report.addTableMappingIssue(constraint);
            } catch (ColumnTypeIncompatException cte) {
                constraint.setErrorMessage("Column types do not match");
                report.addColumnTypeIncompatabilityIssue(constraint);
            } catch (ParentKeysMissingException pke) {
                constraint.setErrorMessage("Records in " +
                        constraint.localTable + "." + constraint.localColumn +
                        " referece IDs that don't exist in " +
                        constraint.foreignTable + "." + constraint.foreignColumn);
                report.addOrphanedDataIssue(constraint);
            } catch (NonPKMappingException npke) {
                constraint.setErrorMessage("Foreign Column is not a Primary Key (" +
                        constraint.foreignTable + "." + constraint.foreignColumn + ")");
                report.addNonPKMappingIssue(constraint);
            } catch (UnknownFKExecption uFKe) {
                report.addOtherIssue(constraint);
            }
        }

        return report;
    }

    public void revert() throws MissingFieldException, SQLException {
        List<ForeignKeyConstraint> constraintList = null;
        try {
            constraintList = FKGenerationUtil.getGeneratedFKConstraints(context.getConnection());
        } catch (SQLException e) {
            LOG.error("error retrieving generated constraints: " + e.getErrorCode() + " " + e.getMessage(), e);
        }

        LOG.debug("\nDeleting " + constraintList.size() + " FK Constraints");

        for (ForeignKeyConstraint constraint : constraintList)  {
            constraint.deleteFKConstraint(context.getConnection());
        }
    }

    public void closeConn() throws SQLException {
        if (context.getConnection() != null) {
            context.getConnection().close();
        }
    }

}
