package com.sigmasys.kuali.ksa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.sigmasys.kuali.ksa.model.support.FeeBase;

/**
 * A super class for chargeable accounts
 *
 * @author Michael Ivanov
 */
@MappedSuperclass
public abstract class ChargeableAccount extends Account {

    /**
     * Outstanding balance
     */
    protected BigDecimal outstandingBalance;

    /**
     * Balance due
     */
    protected BigDecimal balanceDue;

    /**
     * Amount for DAYS_LATE1 period
     */
    protected BigDecimal amountLate1;

    /**
     * Amount for DAYS_LATE2 period
     */
    protected BigDecimal amountLate2;

    /**
     * Amount for DAYS_LATE3 period
     */
    protected BigDecimal amountLate3;

    /**
     * Amount for DAYS_LATE3 period
     */
    protected Date lateLastUpdate;
    
    /**
     * Student data in form of a Set of KeyPair objects.
     */
    protected Set<KeyPair> studentData;
    
    /**
     * Period data in form of a Set of PeriodKeyPair objects.
     */
    protected Set<PeriodKeyPair> periodData;
    
    /**
     * All courses that have been taken on this account.
     */
    protected Set<LearningUnit> study;
    
    

    @Column(name = "OUTSTANDING")
    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    @Column(name = "DUE")
    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal balanceDue) {
        this.balanceDue = balanceDue;
    }

    @Column(name = "LATE1")
    public BigDecimal getAmountLate1() {
        return amountLate1;
    }

    public void setAmountLate1(BigDecimal amountLate1) {
        this.amountLate1 = amountLate1;
    }

    @Column(name = "LATE2")
    public BigDecimal getAmountLate2() {
        return amountLate2;
    }

    public void setAmountLate2(BigDecimal amountLate2) {
        this.amountLate2 = amountLate2;
    }

    @Column(name = "LATE3")
    public BigDecimal getAmountLate3() {
        return amountLate3;
    }

    public void setAmountLate3(BigDecimal amountLate3) {
        this.amountLate3 = amountLate3;
    }

    @Column(name = "LATE_LAST_UPDATE")
    public Date getLateLastUpdate() {
        return lateLastUpdate;
    }

    public void setLateLastUpdate(Date lateLastUpdate) {
        this.lateLastUpdate = lateLastUpdate;
    }
    
    /* ****************************************
     * 
     * Fee Assessment support.
     * You should normally call "getFeeBase" to get a support
     * FeeBase object and use the data it contains.
     * 
     * ****************************************/
    
    @Transient
    public FeeBase getFeeBase() {
    	// Create a new FeeBase instance:
    	FeeBase feeBase = new FeeBase();
    	
    	feeBase.setAccount(this);
    	feeBase.setStudentData(studentData);
    	feeBase.setPeriodData(periodData);
    	feeBase.setStudy(study);
    	
    	return feeBase;
    }

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "KSSA_ACNT_KYPR",
	    	joinColumns = {
	            	@JoinColumn(name = "ACNT_ID_FK")
	    	},
	    	inverseJoinColumns = {
	            	@JoinColumn(name = "KYPR_ID_FK")
	    	}
	)
	public Set<KeyPair> getStudentData() {
		return studentData;
	}

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "KSSA_ACNT_PERIOD_KYPR",
	    	joinColumns = {
	            	@JoinColumn(name = "ACNT_ID_FK")
	    	},
	    	inverseJoinColumns = {
	            	@JoinColumn(name = "PERDIOD_KYPR_ID_FK")
	    	}
	)
	public Set<PeriodKeyPair> getPeriodData() {
		return periodData;
	}

    @OneToMany(cascade=CascadeType.ALL, mappedBy="account")
	public Set<LearningUnit> getStudy() {
		return study;
	}

	public void setStudentData(Set<KeyPair> studentData) {
		this.studentData = studentData;
	}

	public void setPeriodData(Set<PeriodKeyPair> periodData) {
		this.periodData = periodData;
	}

	public void setStudy(Set<LearningUnit> study) {
		this.study = study;
	}
}

