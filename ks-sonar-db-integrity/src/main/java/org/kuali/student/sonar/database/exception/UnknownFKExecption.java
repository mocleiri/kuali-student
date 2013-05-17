package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Used for yet unhandled FK Relationship exceptions
 */
public class UnknownFKExecption extends Throwable {
    ForeignKeyConstraint fkConstraint;

    public UnknownFKExecption(ForeignKeyConstraint fkConstraint) {
        super("Uncategorized Exception when adding constraint: " +
                fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
