package com.sigmasys.kuali.ksa.model;

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
@DiscriminatorValue(TransactionType.CREDIT_TYPE)
public class CreditType extends TransactionType {

    private Integer clearPeriod;

    private String refundRule;

    private String authorizationText;


    @Column(name = "REFUND_RULE", length = 2000)
    public String getRefundRule() {
        return refundRule;
    }

    public void setRefundRule(String refundRule) {
        this.refundRule = refundRule;
    }

    @Column(name = "AUTH_TXT", length = 1000)
    public String getAuthorizationText() {
        return authorizationText;
    }

    public void setAuthorizationText(String authorizationText) {
        this.authorizationText = authorizationText;
    }

    @Column(name = "CLEAR_PERIOD")
    public Integer getClearPeriod() {
        return clearPeriod;
    }

    public void setClearPeriod(Integer clearPeriod) {
        this.clearPeriod = clearPeriod;
    }
}
