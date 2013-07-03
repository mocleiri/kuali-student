package org.kuali.student.sonar.database.utility;

import org.kuali.student.sonar.database.exception.ColumnTypeIncompatException;
import org.kuali.student.sonar.database.exception.FKConstraintException;
import org.kuali.student.sonar.database.exception.FieldMappingException;
import org.kuali.student.sonar.database.exception.NonPKMappingException;
import org.kuali.student.sonar.database.exception.ParentKeysMissingException;
import org.kuali.student.sonar.database.exception.TableMappingException;
import org.kuali.student.sonar.database.exception.UnknownFKExecption;
import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/28/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class FKConstraintReport {
    // field mapping issues
    private ArrayList<FKConstraintException> fmeFKViolationList;
    // table mapping issues
    private ArrayList<FKConstraintException> tmeFKViolationList;
    // column type incompatibility issues
    private ArrayList<FKConstraintException> cteFKViolationList;
    // orphaned relationships
    private ArrayList<FKConstraintException> orphFKViolationList;
    // fk mapped to a non pk issues
    private ArrayList<FKConstraintException> nonPKViolationList;
    // other issues
    private ArrayList<FKConstraintException> otherFKViolationList;

    public FKConstraintReport() {
        fmeFKViolationList = new ArrayList<FKConstraintException>();
        tmeFKViolationList = new ArrayList<FKConstraintException>();
        cteFKViolationList = new ArrayList<FKConstraintException>();
        orphFKViolationList = new ArrayList<FKConstraintException>();
        nonPKViolationList = new ArrayList<FKConstraintException>();
        otherFKViolationList = new ArrayList<FKConstraintException>();
    }

    public void addFieldMappingIssue(FKConstraintException fkce) {
        fmeFKViolationList.add(fkce);
    }

    public void addTableMappingIssue(FKConstraintException fkce) {
        tmeFKViolationList.add(fkce);
    }

    public void addColumnTypeIncompatabilityIssue(FKConstraintException fkce) {
        cteFKViolationList.add(fkce);
    }

    public void addOrphanedDataIssue(FKConstraintException fkce) {
        orphFKViolationList.add(fkce);
    }

    public void addNonPKMappingIssue(FKConstraintException fkce) {
        nonPKViolationList.add(fkce);
    }

    public void addOtherIssue(FKConstraintException fkce) {
        otherFKViolationList.add(fkce);
    }

    public List<FKConstraintException> getFieldMappingIssues(){
        return fmeFKViolationList;
    }

    public List<FKConstraintException> getTableMappingIssues() {
        return tmeFKViolationList;
    }

    public List<FKConstraintException> getColumnTypeIncompatabilityIssues() {
        return cteFKViolationList;
    }

    public List<FKConstraintException> getOrphanedDataIssues() {
        return orphFKViolationList;
    }

    public List<FKConstraintException> getNonPKViolationList() {
        return nonPKViolationList;
    }

    public List<FKConstraintException> getOtherIssues() {
        return otherFKViolationList;
    }

    public void addException(FKConstraintException fkce) {
        if (fkce instanceof ColumnTypeIncompatException) {
            addColumnTypeIncompatabilityIssue(fkce);
        } else if (fkce instanceof ParentKeysMissingException) {
            addOrphanedDataIssue(fkce);
        } else if (fkce instanceof NonPKMappingException) {
            addNonPKMappingIssue(fkce);
        } else if (fkce instanceof FieldMappingException) {
            addFieldMappingIssue(fkce);
        } else if (fkce instanceof TableMappingException) {
            addTableMappingIssue(fkce);
        } else if (fkce instanceof UnknownFKExecption) {
            addOtherIssue(fkce);
        }
    }
}
