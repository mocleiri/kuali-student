package com.sigmasys.kuali.ksa.model;

import com.sigmasys.kuali.ksa.annotation.Auditable;

import javax.persistence.*;

/**
 * User Preference
 * <p/>
 * <p/>
 *
 * @author Michael Ivanov
 */
@Auditable
@Entity
@IdClass(UserPreferenceId.class)
@Table(name = "KSSA_USER_PREF")
public class UserPreference extends AccountIdAware implements Identifiable {


    /**
     * The Account reference
     */
    private Account account;

    /**
     * The preference name
     */
    private String name;

    /**
     * The preference value
     */
    private String value;


    @Transient
    public UserPreferenceId getId() {
        return new UserPreferenceId(getAccountId(), getName());
    }

    @Id
    @Column(name = "ACNT_ID_FK", insertable = false, updatable = false, length = 45)
    public String getAccountId() {
        return accountId;
    }

    @Id
    @Column(name = "NAME", length = 256)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "VALUE", length = 1024)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACNT_ID_FK")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
