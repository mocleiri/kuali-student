package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/31/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NonPKMappingException extends FKConstraintException {

    public NonPKMappingException(ForeignKeyConstraint fkConstraint) {
        super("Foreign Column is not a primary key or is part of a multi-field primary key: " + fkConstraint.getForeignColumn() +
                "Constraint: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
