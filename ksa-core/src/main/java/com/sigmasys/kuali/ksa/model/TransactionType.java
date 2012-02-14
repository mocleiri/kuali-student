package com.sigmasys.kuali.ksa.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 * TransactionType defines different transaction types existing in KSA.
 *
 * User: mivanov
 * Date: 1/22/12
 * Time: 3:46 PM
 * @author Michael Ivanov
 */
@Entity
@IdClass(TransactionTypeId.class)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class TransactionType {
    
	@Id
	@Column(name = "ID")
    protected Long id;
    
	@Id
	@Column(name = "SUBCODE")
    protected String subCode;
    
	@Column(name = "START_DATE")
    protected Date startDate;
    
	@Column(name = "END_DATE")
    protected Date endDate;
    
	@Column(name = "DEF_TRN_TXT")
    protected String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
