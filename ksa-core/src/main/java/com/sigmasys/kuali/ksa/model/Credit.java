package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;

/*
*  * The Credit class is entirely abstract and exists only to differentiate the differential between credits and debits.
*   */
public abstract class Credit extends Transaction {
	
	/** if set to true, the amount may be refunded to the student subject to any other clearing rules, for example, a check 10-day waiting period, etc. 
	 * This will be set to false in the case of tuition deposits, etc, which may not be refunded to a student, but may be allocated to charges on the account.
	 * */
	protected boolean isRefundable;


    @Column(name = "REFUND_STAT")
    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }
}

