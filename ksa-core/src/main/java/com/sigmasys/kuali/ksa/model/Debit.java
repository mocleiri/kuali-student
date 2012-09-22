package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * An abstract debit class. The abstraction exists in case other types of debits are to be added at a later time. The concrete Charge class should be used for instantiation of
 * actual debits.
 *
 * @author Michael Ivanov
 */
@MappedSuperclass
public abstract class Debit extends Transaction {


    /**
     * The reference to Deferment that offsets this transaction.
     * If this is null, isDeferred will also be set to false.
     */
    protected Boolean deferred;


    /**
     * Indicates if the payment is billing
     */
    protected Boolean paymentBilling;


    /**
     * If a transaction is deferred, then it will return true here.
     * Deferred transactions also bear the identifier of the deferment transaction that offsets them in deferment
     *
     * @return boolean value
     */
    @org.hibernate.annotations.Type(type = "yes_no")
    @Column(name = "IS_DEFERRED")
    public Boolean isDeferred() {
        return deferred;
    }

    public void setDeferred(Boolean deferred) {
        this.deferred = deferred;
    }

    @org.hibernate.annotations.Type(type = "yes_no")
    @Column(name = "IS_PAYMENT_BILLING")
    public Boolean isPaymentBilling() {
        return paymentBilling;
    }

    public void setPaymentBilling(Boolean paymentBilling) {
        this.paymentBilling = paymentBilling;
    }

}

