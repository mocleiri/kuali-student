package com.sigmasys.kuali.ksa.service;

import java.math.BigDecimal;

import com.sigmasys.kuali.ksa.model.Transaction;

/**
 * Transaction service declares business operations on Transaction and related
 * objects
 * <p/>
 * User: mivanov Date: 1/22/12 Time: 12:23 PM
 * 
 * @author Michael Ivanov
 */
public interface TransactionService {

	// TODO: figure out what parameters are needed for creating a new
	// transaction and persisting it
	// in the database

	/**
	 * Creates a new transaction and persists it the data store
	 * @return Transaction instance
	 */
	Transaction createTransaction();

	/**
	 * Removes the transaction object from the persistence store by the given
	 * transaction ID
	 * 
	 * @param transactionId
	 *            transaction ID
	 */
	void removeTransaction(String transactionId);

	/**
	 * This will allocate the value of amount on the transaction. A check will
	 * be made to ensure that the allocated amount is equal to or less than the
	 * localAmount, less any lockedAllocationAmount. The expectation is that
	 * this method will only be called by the payment application module.
	 * <p/>
	 * 
	 * @param transactionId
	 *            transaction ID
	 * @param amount
	 *            amount of money to be allocated TODO -
	 * @throws MaxAmountExceededException
	 */
	void allocateAmount(String transactionId, BigDecimal amount);

	/**
	 * This will allocate a locked amount on the transaction. A check will be
	 * made to ensure that the lockedAmount and the allocateAmount don't exceed
	 * the ledgerAmount of the transaction. Setting an amount as locked prevents
	 * the payment application system from reallocating the balance elsewhere.
	 * 
	 * @param transactionId
	 *            transaction ID
	 * @param amount
	 *            amount of money to be allocated TODO -
	 * @throws MaxAmountExceededException
	 */
	void allocateLockedAmount(String transactionId, BigDecimal amount);

	/**
	 * If a memos can be generated in a number of ways. If a memo is generated
	 * against a transaction, it is placed in the main memo account, and also
	 * the memoReference is set to point to that memo. This allows the CSR to
	 * see the most recent memo associated with a certain transaction. Certain
	 * transaction instantiations will generate a memo as soon as they are
	 * created.
	 * 
	 * @param transactionId
	 *            transaction ID
	 * @param memoText
	 *            memo text
	 */
	void generateTransactionMemo(String transactionId, String memoText);

	/**
	 * If the reverse method is called, the system will generate a negative
	 * transaction for the type of the original transaction. A memo transaction
	 * will be generated, and the transactions will be locked together. Subject
	 * to user customization, the transactions may be marked as hidden. (likely
	 * that credits will not be hidden, debits will.) A charge to an account may
	 * be reversed when a mistake is made, or a refund is issued. A payment may
	 * be reversed when a payment bounces, or for some other reason is entered
	 * on to the account and is not payable.
	 */
	void reverseTransaction(String transactionId);

}
