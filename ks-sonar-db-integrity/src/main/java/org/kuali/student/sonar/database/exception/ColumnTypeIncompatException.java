package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Indicates that the local field and foreign field of a FK Relationship are incompatible
 */
public class ColumnTypeIncompatException extends Throwable {
    ForeignKeyConstraint fkConstraint;

    public ColumnTypeIncompatException(ForeignKeyConstraint fkConstraint) {
        super("Incompatible Column types in constraint: " +
                fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
