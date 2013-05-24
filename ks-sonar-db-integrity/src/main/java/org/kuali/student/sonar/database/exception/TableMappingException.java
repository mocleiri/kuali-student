package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 11:39 PM
 *
 * Usually indicates that the foreign table does not exists.
 * It may also indicate that the local table is not a table but a view
 */
public class TableMappingException extends Exception {
    ForeignKeyConstraint fkConstraint;

    public TableMappingException(ForeignKeyConstraint fkConstraint) {
        super("Improperly mapped tabel: " + fkConstraint.foreignTable + "\n" +
                "Constraint: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
