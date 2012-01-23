package com.sigmasys.kuali.ksa.dao;

import java.math.BigDecimal;

/**
 * Transaction service declares persistence operations on Transaction.
 * <p/>
 * User: mivanov
 * Date: 1/22/12
 * Time: 12:23 PM
 *
 * @author Michael Ivanov
 */
public interface TransactionDao {

    // TODO

    /**
     * This will allocate the value of amount on the transaction. A check will be made to ensure that the allocated amount
     * is equal to or less than the localAmount, less any lockedAllocationAmount. The expectation is that this method will only
     * be called by the payment application module.
     * <p/>
     *
     * @param transactionId transaction ID
     * @param amount        amount of money to be allocated
     *                      TODO -
     * @throws MaxAmountExceededException
     */
    public void allocateAmount(String transactionId, BigDecimal amount);


    /**
     * This will allocate a locked amount on the transaction. A check will be made to ensure that the lockedAmount and the allocateAmount
     * don't exceed the ledgerAmount
     * of the transaction. Setting an amount as locked prevents the payment application system from reallocating the balance elsewhere.
     *
     * @param transactionId transaction ID
     * @param amount        amount of money to be allocated
     *                      TODO -
     * @throws MaxAmountExceededException
     */
    public void allocateLockedAmount(String transactionId, BigDecimal amount);

    /**
     * If a memos can be generated in a number of ways. If a memo is generated against a transaction, it is placed in the main memo account, and also the memoReference is
     * set to point to that memo. This allows the CSR to see the most recent memo associated with a certain transaction.
     * Certain transaction instantiations will generate a memo
     * as soon as they are created.
     *
     * @param transactionId transaction ID
     * @param memoText      memo text
     */
    public void generateTransactionMemo(String transactionId, String memoText);


}
