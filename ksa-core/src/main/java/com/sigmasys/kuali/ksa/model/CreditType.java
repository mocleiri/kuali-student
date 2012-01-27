package com.sigmasys.kuali.ksa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CreditType
 * Credit types define the clearing period for a transaction, refund preferences,
 * charges that can be paid by the credit, etc.
 * <p/>
 * <p/>
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 4:13 PM
 */
@Entity
@Table(name = "KSSA_CREDIT_TYPE")
public class CreditType extends TransactionType {

	@Column(name = "CLEAR_DATE")
    private Date clearDate;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REFUND_RULE_ID")
    private RefundRule refundRule;
    
	@Column(name = "AUTH_TEXT")
    private String authorizationText;

    public Date getClearDate() {
        return clearDate;
    }

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }

    public RefundRule getRefundRule() {
        return refundRule;
    }

    public void setRefundRule(RefundRule refundRule) {
        this.refundRule = refundRule;
    }

    public String getAuthorizationText() {
        return authorizationText;
    }

    public void setAuthorizationText(String authorizationText) {
        this.authorizationText = authorizationText;
    }
}
