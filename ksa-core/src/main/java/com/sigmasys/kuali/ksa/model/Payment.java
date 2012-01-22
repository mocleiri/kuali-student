package com.sigmasys.kuali.ksa.model;

import java.util.Date;

/*
 * Concrete payment class. 
 * 
 */
public class Payment extends Credit {

    /**
     * the clearDate is the date after which a payment is considered refundable after bank processing.
     * It is generally inherited at the time of instantiation from the type of payment
     * as looked up in the TransactionType. For example, an institution may set the clearDate for a check as 10 days
     * after the receipt of the check, whereas cash is cleared
     * instantly. The refundability of a transaction based on this date does NOT affect the refund flag isRefundable,
     * which is set for transactions which are inherently non refundable,
     * such as tuition deposits, etc.
     */
    private Date clearDate;


    public Payment(CreditType creditType) {
        super(creditType);
    }


    public Date getClearDate() {
        return clearDate;
    }

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }

    /*
    * Using the payment type and effective date, look up the priority of the payment for the payment application system.
    */
    public void getPriority() {
        // TODO
    }


    /*
      * Using the payment type and effective date, look up the types of debits that can be paid with this payment.
      * This will be used by the payment application system.
      */
    public void getAllowableCharges() {
        // TODO
    }


}

