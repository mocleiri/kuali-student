package org.kuali.student.sonar.database.exception;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 7/3/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidConstraintException extends Exception {
    public InvalidConstraintException(String missingValueField) {
        super("Constraint missing value for " + missingValueField);
    }
}
