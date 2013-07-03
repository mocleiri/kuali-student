package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 9:08 PM
 *
 * Indicates that Orphaned data is preventing a FK Constraint from being created
 */
public class ParentKeysMissingException extends FKConstraintException {

    public ParentKeysMissingException(ForeignKeyConstraint fkConstraint) {
        super("Orphaned data exists for unconstrained relationship\n" +
                "Constraint: " + fkConstraint.toString() + "\n" +
                "Run the following SQL to identify which keys are missing: " +
                "select " + fkConstraint.getLocalColumn() +
                        " from " + fkConstraint.localTable +
                        " left join " + fkConstraint.foreignTable +
                            " on " + fkConstraint.getForeignColumn() + "=" + fkConstraint.getLocalColumn() +
                        " where " + fkConstraint.getForeignColumn() + " is null\n");
        this.fkConstraint = fkConstraint;
    }
}
