package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/31/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NonPKMappingException extends Throwable {
    ForeignKeyConstraint fkConstraint;

    public NonPKMappingException(ForeignKeyConstraint fkConstraint) {
        super("Uncategorized Exception when adding constraint: " +
                fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
