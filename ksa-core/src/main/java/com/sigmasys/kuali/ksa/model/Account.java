package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * KSA Account model
 * <p/>
 * <p/>
 *
 * @author Michael Ivanov
 *         Date: 3/13/12
 *         Time: 3:56 PM
 */
@Entity
@Table(name = "KSSA_ACNT")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account implements Identifiable {

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
    protected Boolean isKimAccount;

    /**
     * True if the user can authenticate
     */
    protected Boolean ableToAuthenticate;

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
    protected Set<PersonName> personNames;

    /**
     * Collection of associated electronic contacts
     */
    protected Set<ElectronicContact> electronicContacts;

    /**
     * Collection of associated postal addresses
     */
    protected Set<PostalAddress> postalAddresses;

    /**
     * Account Status Type
     */
    protected AccountStatusType statusType;

    /**
     * lastName, firstName of the default PersonName Set record
     */
    private String compositeDefaultPersonName;

    /**
     * Address line 1, state, postalCode, and country of the default PostalAddress Set record
     */
    private String compositeDefaultPostalAddress;


    protected Account() {
    }


    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 45)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ENTITY_ID", length = 45)
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

    @org.hibernate.annotations.Type(type = "yes_no")
    @Column(name = "IS_KIM_ACNT")
    public Boolean isKimAccount() {
        return isKimAccount;
    }

    public void setKimAccount(Boolean kimAccount) {
        isKimAccount = kimAccount;
    }

    @org.hibernate.annotations.Type(type = "yes_no")
    @Column(name = "CAN_AUTHENTICATE")
    public Boolean isAbleToAuthenticate() {
        return ableToAuthenticate;
    }

    public void setAbleToAuthenticate(Boolean ableToAuthenticate) {
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
    public Set<PersonName> getPersonNames() {
        return personNames;
    }

    public void setPersonNames(Set<PersonName> personNames) {
        this.personNames = personNames;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    public Set<ElectronicContact> getElectronicContacts() {
        return electronicContacts;
    }

    public void setElectronicContacts(Set<ElectronicContact> electronicContacts) {
        this.electronicContacts = electronicContacts;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    public Set<PostalAddress> getPostalAddresses() {
        return postalAddresses;
    }

    public void setPostalAddresses(Set<PostalAddress> postalAddresses) {
        this.postalAddresses = postalAddresses;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACNT_STATUS_TYPE_ID_FK")
    public AccountStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(AccountStatusType statusType) {
        this.statusType = statusType;
    }

    @Transient
    public PersonName getDefaultPersonName() {
        Set<PersonName> personNames = getPersonNames();
        if (personNames != null) {
            for (PersonName personName : personNames) {
                if (personName.isDefault()) {
                    return personName;
                }
            }
        }
        return null;
    }

    @Transient
    public PostalAddress getDefaultPostalAddress() {
        Set<PostalAddress> postalAddresses = getPostalAddresses();
        if (postalAddresses != null) {
            for (PostalAddress postalAddress : postalAddresses) {
                if (postalAddress.isDefault()) {
                    return postalAddress;
                }
            }
        }
        return null;
    }

    @Transient
    public ElectronicContact getDefaultElectronicContact() {
        Set<ElectronicContact> electronicContacts = getElectronicContacts();
        if (electronicContacts != null) {
            for (ElectronicContact electronicContact : electronicContacts) {
                if (electronicContact.isDefault()) {
                    return electronicContact;
                }
            }
        }
        return null;
    }

    /**
     * Get lastName, firstName of the default PersonName Set record
     */
    @Transient
    public String getCompositeDefaultPersonName() {
        return compositeDefaultPersonName;
    }

    /**
     * Set lastName, firstName of the default PersonName Set record
     *
     * @param compositeDefaultPersonName
     */
    public void setCompositeDefaultPersonName(String compositeDefaultPersonName) {
        this.compositeDefaultPersonName = compositeDefaultPersonName;
    }

    /**
     * Get Address line 1, state, postalCode, and country of the default PostalAddress Set record
     */
    @Transient
    public String getCompositeDefaultPostalAddress() {
        return compositeDefaultPostalAddress;
    }

    /**
     * Set Address line 1, state, postalCode, and country of the default PostalAddress Set record
     *
     * @param compositeDefaultPostalAddress
     */
    public void setCompositeDefaultPostalAddress(String compositeDefaultPostalAddress) {
        this.compositeDefaultPostalAddress = compositeDefaultPostalAddress;
    }


    @Transient
    public Account getCopy() {
        Account account = new Account();
        account.setId(getId());
        account.setEntityId(getEntityId());
        account.setStatusType(getStatusType());
        account.setCreationDate(getCreationDate());
        account.setLastKimUpdate(getLastKimUpdate());
        account.setKimAccount(isKimAccount());
        account.setLatePeriod(getLatePeriod());
        account.setAbleToAuthenticate(isAbleToAuthenticate());
        account.setCreditLimit(getCreditLimit());
        account.setPersonNames(new HashSet<PersonName>(getPersonNames()));
        account.setElectronicContacts(new HashSet<ElectronicContact>(getElectronicContacts()));
        account.setPostalAddresses(new HashSet<PostalAddress>(getPostalAddresses()));
        return account;
    }

}
