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
public class ParentKeysMissingException extends Exception {
    ForeignKeyConstraint fkConstraint;

    public ParentKeysMissingException(ForeignKeyConstraint fkConstraint) {
        super("Orphaned data exists for unconstrained relationship: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
