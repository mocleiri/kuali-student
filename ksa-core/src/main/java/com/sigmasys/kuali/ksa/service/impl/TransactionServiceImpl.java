package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Deferment;
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
	 * 
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
	public void removeTransaction(Long transactionId) {
		// TODO
	}

	@Override
	public void allocateAmount(Long transactionId, BigDecimal amount) {
		// TODO
	}

	@Override
	public void allocateLockedAmount(Long transactionId, BigDecimal amount) {
		// TODO
	}

	@Override
	public void generateTransactionMemo(Long transactionId, String memoText) {
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
	public void reverseTransaction(Long transactionId) {
		// TODO
	}

	/**
	 * A deferment may be expired automatically (when the date of the deferment
	 * passes) or be expired manually but the system, either through user
	 * intervention, or by a payment being received on the account that removes
	 * the need for the deferment. If, for example, an account is paid in full,
	 * the deferment would have to be expired, otherwise a credit balance would
	 * technically occur on the account.
	 */
	@Override
	public void expireDeferment(Long defermentId) {
		// TODO
	}

	/**
	 * A deferment may be reduced or set to zero after expiration. Often, the
	 * value of a deferment may not exceed the debit balance on the account to
	 * prevent a credit balance being available for refund on the strength of a
	 * deferment. A deferment may not be increased. Should such a situation
	 * arise, the deferment would need to be expired, and a new deferment
	 * issued.
	 */
	@Override
	public void reduceDeferment(Long defermentId, BigDecimal newAmount) {
		// TODO
	}
	
	/**
     * Automatically generates a deferment transaction, 
     * allocates and locks the two transactions together.
     */
    @Override
    public Deferment deferTransaction(Long transactionId) {
        // TODO
    	return null;
    }
    

    /**
     * using the transactionType, return a list of the general ledger accounts that this debit will feed. This will require the
     * effectiveDate of the transaction, as some GL codes will change after certain periods of time.
     */
    public void getGlAccounts() {
         // TODO
    }

    /**
     * As getGlAccounts(), but also returns the breakout of the amounts to the general ledger accounts. For example, if this transaction is
     * for $1000, and sends 30% to account 111 and 70% to 222 then the system will return the breakout amounts as well as the gl accounts.
     */
    public void getGlAccountsWithBreakdown() {
        // TODO
    }

}
