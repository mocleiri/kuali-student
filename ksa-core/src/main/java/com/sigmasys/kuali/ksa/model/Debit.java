package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * an abstract debit class. The abstraction exists in case other types of debits are to be added at a later time. The concrete Charge class should be used for instantiation of
 * actual debits.
 *
 * @author Paul Heald
 */
public abstract class Debit extends Transaction {


    /**
     * The reference to Deferment that offsets this transaction.
     * If this is null, isDeferred will also be set to false.
     */
    protected boolean deferred;

    
    /**
     * If a transaction is deferred, then it will return true here.
     * Deferred transactions also bear the identifier of the deferment transaction that offsets them in deferment
     */
    @Column(name = "DEFER_STAT")
    public Boolean isDeferred() {
        return deferred;
    }

    public void setDeferred(boolean deferred) {
        this.deferred = deferred;
    }

    /**
     * Gets the priority of the debit from the transaction code. The priority of a transaction defines when it is paid off in the payment allocation system.
     * The priority of a debit may change, and is reference against the effective date of the transaction to ensure the correct priority.
     */
    @Transient
    public void getPriority() {
         // TODO
    }

}

