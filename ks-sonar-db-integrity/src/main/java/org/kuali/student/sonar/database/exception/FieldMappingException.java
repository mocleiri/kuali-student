package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.kuali.student.sonar.database.utility.FKConstraintReport;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 10:57 PM
 *
 * This classes is needed because we're creating alter scripts based on fields that may not be set.
 * Bad things could happen if we don't check for missing fields.
 */
public class FieldMappingException extends FKConstraintException {

    public FieldMappingException(ForeignKeyConstraint fkConstraint) {
        super("Improperly mapped field (does not exist) " + fkConstraint.getForeignColumn() + "\n" +
                "see project file referenced by mvn properties kuali.student.sonar.fkvalidation.query.path " +
                "kuali.student.sonar.fkvalidation.query.filename\n" +
                "Constraint: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }

}
