package com.sigmasys.kuali.ksa.model;

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
    protected Deferment deferment;


    public Debit(DebitType debitType) {
        super(debitType);
    }


    /**
     * If a transaction is deferred, then it will return true here.
     * Deferred transactions also bear the identifier of the deferment transaction that offsets them in deferment
     */
    public boolean isDeferred() {
        return deferment != null;
    }


    public Deferment getDeferment() {
        return deferment;
    }

    public void setDeferment(Deferment deferment) {
        this.deferment = deferment;
    }

    /**
     * using the transactionType, return a list of the general ledger accounts that this debit will feed. This will require the
     * effectiveDate of the transaction, as some GL codes will change after certain periods of time.
     */
    public void getGlAccounts() {

    }

    /**
     * As getGlAccounts(), but also returns the breakout of the amounts to the general ledger accounts. For example, if this transaction is
     * for $1000, and sends 30% to account 111 and 70% to 222 then the system will return the breakout amounts as well as the gl accounts.
     */
    public void getGlAccountsWithBreakdown() {

    }

    /**
     * automatically generates a deferment transaction, and allocates and locks the two transactions together.
     */

    public void defer() {

    }

    /**
     * Gets the priority of the debit from the transaction code. The priority of a transaction defines when it is paid off in the payment allocation system.
     * The priority of a debit may change, and is reference against the effective date of the transaction to ensure the correct priority.
     */
    public void getPriority() {

    }

}

