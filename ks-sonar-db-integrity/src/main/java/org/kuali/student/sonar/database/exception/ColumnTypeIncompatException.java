package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.kuali.student.sonar.database.utility.FKConstraintReport;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Indicates that the local field and foreign field of a FK Relationship are incompatible
 */
public class ColumnTypeIncompatException extends FKConstraintException {

    public ColumnTypeIncompatException(ForeignKeyConstraint fkConstraint) {
        super("Incompatible Column types in constraint: " +
                fkConstraint.toString() + "\n" +
                "Run the following SQL to see the types: " +
                "SELECT c.table_name, c.column_name, c.data_type, c.data_length FROM user_tab_cols c" +
                " LEFT JOIN all_tables t" +
                " ON c.table_name = t.table_name" +
                " WHERE (t.table_name = '" + fkConstraint.localTable + "' AND c.column_name = '" + fkConstraint.localColumn + "') " +
                " OR (t.table_name = '" + fkConstraint.foreignTable + "' AND c.column_name = '" + fkConstraint.foreignColumn + "')");
        this.fkConstraint = fkConstraint;
    }

}
