package com.sigmasys.kuali.ksa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * CreditType
 * Credit types define the clearing period for a transaction, refund preferences,
 * charges that can be paid by the credit, etc.
 * <p/>
 * <p/>
 * <p/>
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue("C")
public class CreditType extends TransactionType {

    private Date clearDate;

    private Integer clearPeriod;

    private String refundRule;

    private String authorizationText;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "KSSA_CREDIT_TAG",
            joinColumns = {
                    @JoinColumn(name = "CREDIT_TYPE_ID_FK"),
                    @JoinColumn(name = "CREDIT_SUB_CODE_FK")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "TAG_ID_FK")
            }
    )
    @Override
    public List<Tag> getTags() {
        return tags;
    }

    @Column(name = "CLEAR_DATE")
    public Date getClearDate() {
        return clearDate;
    }

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }

    @Column(name = "REFUND_RULE")
    public String getRefundRule() {
        return refundRule;
    }

    public void setRefundRule(String refundRule) {
        this.refundRule = refundRule;
    }

    @Column(name = "AUTH_TEXT")
    public String getAuthorizationText() {
        return authorizationText;
    }

    public void setAuthorizationText(String authorizationText) {
        this.authorizationText = authorizationText;
    }

    @Column(name = "DEF_CLEAR_PERIOD")
    public Integer getClearPeriod() {
        return clearPeriod;
    }

    public void setClearPeriod(Integer clearPeriod) {
        this.clearPeriod = clearPeriod;
    }
}
