package com.sigmasys.kuali.ksa.model;

import java.util.Date;

/**
 * TransactionType enum defines different transaction types existing in KSA.
 *
 * User: mivanov
 * Date: 1/22/12
 * Time: 3:46 PM
 * @author Michael Ivanov
 */
public abstract class TransactionType {
    
    private String id;
    
    private String subCode;
    
    private Date startDate;
    
    private Date endDate;
    
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
