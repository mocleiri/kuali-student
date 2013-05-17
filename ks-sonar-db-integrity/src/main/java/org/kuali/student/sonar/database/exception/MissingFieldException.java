package org.kuali.student.sonar.database.exception;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 10:57 PM
 *
 * This classes is needed because we're creating alter scripts based on fields that may not be set.
 * Bad things could happen if we don't check for missing fields.
 */
public class MissingFieldException extends Exception {
    String fieldName;

    public MissingFieldException(String fieldName) {
        super ("Required field missing: " + fieldName);
        this.fieldName = fieldName;
    }

}
