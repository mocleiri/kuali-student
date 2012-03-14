package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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
     * KIM Entity ID
     */
    protected String kimEntityId;

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


    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "KIM_ENTITY")
    public String getKimEntityId() {
        return kimEntityId;
    }

    public void setKimEntityId(String kimEntityId) {
        this.kimEntityId = kimEntityId;
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
}
