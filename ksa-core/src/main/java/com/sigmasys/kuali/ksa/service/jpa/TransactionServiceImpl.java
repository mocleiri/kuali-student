package com.sigmasys.kuali.ksa.service.jpa;

import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.TransactionService;

import java.math.BigDecimal;

/**
 * Transaction service JPA implementation.
 * 
 * User: mivanov Date: 1/22/12 Time: 12:23 PM
 * 
 * @author Michael Ivanov
 * 
 */
public class TransactionServiceImpl implements TransactionService {

	// TODO
	// Inject Entity Manager here
	
	
	/**
	 * Creates a new transaction and persists it the data store
	 * @return Transaction instance
	 */
	@Override
	public Transaction createTransaction() {
		// TODO
		return null;
	}

	/**
	 * Removes the transaction object from the persistence store by the given
	 * transaction ID
	 * 
	 * @param transactionId
	 *            transaction ID
	 */
	@Override
	public void removeTransaction(String transactionId) {
		 // TODO
	}

	
	@Override
	public void allocateAmount(String transactionId, BigDecimal amount) {
		// TODO
	}

	@Override
	public void allocateLockedAmount(String transactionId, BigDecimal amount) {
		// TODO
	}

	@Override
	public void generateTransactionMemo(String transactionId, String memoText) {
		// TODO
	}

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
	@Override
	public void reverseTransaction(String transactionId) {
		// TODO
	}
}
