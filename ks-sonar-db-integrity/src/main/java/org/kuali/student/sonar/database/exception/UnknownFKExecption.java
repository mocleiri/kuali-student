package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Used for yet unhandled FK Relationship exceptions
 */
public class UnknownFKExecption extends FKConstraintException {
    private String sqlMessage;

    public UnknownFKExecption(ForeignKeyConstraint fkConstraint, String sqlMessage) {
        super("Uncategorized Exception when adding constraint: " +
                fkConstraint.toString() + "\n" + sqlMessage + "\n" +
                "Constraint: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
        this.sqlMessage = sqlMessage;
    }
}
