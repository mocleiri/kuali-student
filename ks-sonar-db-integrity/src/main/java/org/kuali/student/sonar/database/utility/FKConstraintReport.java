package org.kuali.student.sonar.database.utility;

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
    private ArrayList<ForeignKeyConstraint> fmeFKViolationList;
    // table mapping issues
    private ArrayList<ForeignKeyConstraint> tmeFKViolationList;
    // column type incompatibility issues
    private ArrayList<ForeignKeyConstraint> cteFKViolationList;
    // orphaned relationships
    private ArrayList<ForeignKeyConstraint> orphFKViolationList;
    // fk mapped to a non pk issues
    private ArrayList<ForeignKeyConstraint> nonPKViolationList;
    // other issues
    private ArrayList<ForeignKeyConstraint> otherFKViolationList;

    public FKConstraintReport() {
        fmeFKViolationList = new ArrayList<ForeignKeyConstraint>();
        tmeFKViolationList = new ArrayList<ForeignKeyConstraint>();
        cteFKViolationList = new ArrayList<ForeignKeyConstraint>();
        orphFKViolationList = new ArrayList<ForeignKeyConstraint>();
        nonPKViolationList = new ArrayList<ForeignKeyConstraint>();
        otherFKViolationList = new ArrayList<ForeignKeyConstraint>();
    }

    public void addFieldMappingIssue(ForeignKeyConstraint constraint) {
        fmeFKViolationList.add(constraint);
    }

    public void addTableMappingIssue(ForeignKeyConstraint constraint) {
        tmeFKViolationList.add(constraint);
    }

    public void addColumnTypeIncompatabilityIssue(ForeignKeyConstraint constraint) {
        cteFKViolationList.add(constraint);
    }

    public void addOrphanedDataIssue(ForeignKeyConstraint constraint) {
        orphFKViolationList.add(constraint);
    }

    public void addNonPKMappingIssue(ForeignKeyConstraint constraint) {
        nonPKViolationList.add(constraint);
    }

    public void addOtherIssue(ForeignKeyConstraint constraint) {
        otherFKViolationList.add(constraint);
    }

    public List<ForeignKeyConstraint> getFieldMappingIssues(){
        return fmeFKViolationList;
    }

    public List<ForeignKeyConstraint> getTableMappingIssues() {
        return tmeFKViolationList;
    }

    public List<ForeignKeyConstraint> getColumnTypeIncompatabilityIssues() {
        return cteFKViolationList;
    }

    public List<ForeignKeyConstraint> getOrphanedDataIssues() {
        return orphFKViolationList;
    }

    public ArrayList<ForeignKeyConstraint> getNonPKViolationList() {
        return nonPKViolationList;
    }

    public List<ForeignKeyConstraint> getOtherIssues() {
        return otherFKViolationList;
    }


}
