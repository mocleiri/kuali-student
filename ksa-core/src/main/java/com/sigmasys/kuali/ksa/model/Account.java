package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * KSA Account model
 * <p/>
 * <p/>
 * User: ivanovm
 * Date: 3/13/12
 * Time: 3:56 PM
 */
@Entity
@Table(name = "KSSA_ACNT")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Account implements Identifiable {

    /**
     * Account ID (User ID)
     */
    protected String id;

    /**
     * Entity ID
     */
    protected String entityId;

    /**
     * Creation date
     */
    protected Date creationDate;

    /**
     * Last KIM update date
     */
    protected Date lastKimUpdate;

    /**
     * Last KIM update date
     */
    protected boolean isKimAccount;

    /**
     * True if the user can authenticate
     */
    protected boolean ableToAuthenticate;

    /**
     * Credit limit
     */
    protected BigDecimal creditLimit;

    /**
     * Late period
     */
    protected LatePeriod latePeriod;

    /**
     * Collection of associated person names
     */
    protected List<PersonName> personNames;

    /**
     * Collection of associated electronic contacts
     */
    protected List<ElectronicContact> electronicContacts;

    /**
     * Collection of associated postal addresses
     */
    protected List<PostalAddress> postalAddresses;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ENTITY_ID")
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Column(name = "CREATION_DATE")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "LAST_KIM_UPDATE")
    public Date getLastKimUpdate() {
        return lastKimUpdate;
    }

    public void setLastKimUpdate(Date lastKimUpdate) {
        this.lastKimUpdate = lastKimUpdate;
    }

    @Column(name = "IS_KIM_ACNT")
    public boolean isKimAccount() {
        return isKimAccount;
    }

    public void setKimAccount(boolean kimAccount) {
        isKimAccount = kimAccount;
    }

    @Column(name = "CAN_AUTHENTICATE")
    public boolean isAbleToAuthenticate() {
        return ableToAuthenticate;
    }

    public void setAbleToAuthenticate(boolean ableToAuthenticate) {
        this.ableToAuthenticate = ableToAuthenticate;
    }

    @Column(name = "CREDIT_LIMIT")
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LATE_PERIOD_ID_FK")
    public LatePeriod getLatePeriod() {
        return latePeriod;
    }

    public void setLatePeriod(LatePeriod latePeriod) {
        this.latePeriod = latePeriod;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    public List<PersonName> getPersonNames() {
        return personNames;
    }

    public void setPersonNames(List<PersonName> personNames) {
        this.personNames = personNames;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    public List<ElectronicContact> getElectronicContacts() {
        return electronicContacts;
    }

    public void setElectronicContacts(List<ElectronicContact> electronicContacts) {
        this.electronicContacts = electronicContacts;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    public List<PostalAddress> getPostalAddresses() {
        return postalAddresses;
    }

    public void setPostalAddresses(List<PostalAddress> postalAddresses) {
        this.postalAddresses = postalAddresses;
    }
}
