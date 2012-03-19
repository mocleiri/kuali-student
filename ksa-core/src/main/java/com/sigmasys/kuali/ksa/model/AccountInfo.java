package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Information main class
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
@Entity
@Table(name = "KSSA_INFORMATION")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AccountInfo implements Identifiable {

    /**
     * AccountInfo ID
     */
    private String id;

    /**
     * Tax reference
     */
    private String taxReference;

    /**
     * Bank details
     */
    private String bankDetails;

    /**
     * Tax type
     */
    private TaxType taxType;

    /**
     * Bank type
     */
    private BankType bankType;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "TAX_REFERENCE")
    public String getTaxReference() {
        return taxReference;
    }

    public void setTaxReference(String taxReference) {
        this.taxReference = taxReference;
    }

    @Column(name = "BANK_DETAILS")
    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAX_TYPE_ID_FK")
    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANK_TYPE_ID_FK")
    public BankType getBankType() {
        return bankType;
    }

    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }
}
