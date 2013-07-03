package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 7/3/13
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class FKConstraintException extends Exception {
    protected ForeignKeyConstraint fkConstraint;

    public FKConstraintException(String s) {
        super(s);
    }

    public ForeignKeyConstraint getFkConstraint() {
        return fkConstraint;
    }
}
