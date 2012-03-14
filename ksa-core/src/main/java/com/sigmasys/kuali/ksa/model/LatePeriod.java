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
public class LatePeriod extends AuditableEntity {

    private boolean isDefault;

    private Integer daysLate1;
    private Integer daysLate2;
    private Integer daysLate3;


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

    @Column(name = "DAYS_LATE1")
    public Integer getDaysLate1() {
        return daysLate1;
    }

    public void setDaysLate1(Integer daysLate1) {
        this.daysLate1 = daysLate1;
    }

    @Column(name = "DAYS_LATE2")
    public Integer getDaysLate2() {
        return daysLate2;
    }

    public void setDaysLate2(Integer daysLate2) {
        this.daysLate2 = daysLate2;
    }

    @Column(name = "DAYS_LATE3")
    public Integer getDaysLate3() {
        return daysLate3;
    }

    public void setDaysLate3(Integer daysLate3) {
        this.daysLate3 = daysLate3;
    }
}
