package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Account type.
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
@Entity
@Table(name = "KSSA_LATE_PERIODS")
public class LatePeriods extends AuditableEntity {

    private boolean isDefault;

    private Integer late1;
    private Integer late2;
    private Integer late3;


    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @TableGenerator(name = "TABLE_GEN_ACNT_TYPE",
            table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "ACCOUNT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_ACNT_TYPE")
    @Override
    public Long getId() {
        return id;
    }

    @Column(name = "IS_DEFAULT")
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Column(name = "LATE1")
    public Integer getLate1() {
        return late1;
    }

    public void setLate1(Integer late1) {
        this.late1 = late1;
    }

    @Column(name = "LATE2")
    public Integer getLate2() {
        return late2;
    }

    public void setLate2(Integer late2) {
        this.late2 = late2;
    }

    @Column(name = "LATE3")
    public Integer getLate3() {
        return late3;
    }

    public void setLate3(Integer late3) {
        this.late3 = late3;
    }
}
