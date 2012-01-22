package com.sigmasys.kuali.ksa.model;

import java.util.Date;

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
public class CreditType extends TransactionType {

    private Date clearDate;
    
    private RefundRule refundRule;
    
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
