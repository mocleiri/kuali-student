package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Transaction service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("transactionService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class TransactionServiceImpl extends GenericPersistenceService implements TransactionService {


    /**
     * Returns Transaction by ID
     *
     * @param id Transaction ID
     * @return Transaction instance
     */
    @Override
    public Transaction getTransaction(Long id) {
        return getEntity(id, Transaction.class);
    }

    /**
     * Returns Charge by ID
     *
     * @param id Charge ID
     * @return Charge instance
     */
    @Override
    public Charge getCharge(Long id) {
        return getEntity(id, Charge.class);
    }

    /**
     * Returns Payment by ID
     *
     * @param id Payment ID
     * @return Payment instance
     */
    @Override
    public Payment getPayment(Long id) {
        return getEntity(id, Payment.class);
    }

    /**
     * Returns Deferment by ID
     *
     * @param id Deferment ID
     * @return Deferment instance
     */
    @Override
    public Deferment getDeferment(Long id) {
        return getEntity(id, Deferment.class);
    }


    /**
     * Returns all transactions sorted by ID
     *
     * @return List of transactions
     */
    @Override
    public List<Transaction> getTransactions() {
        return getEntities(Transaction.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all charges sorted by ID
     *
     * @return List of all charges
     */
    @Override
    public List<Charge> getCharges() {
        return getEntities(Charge.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all transactions sorted by ID
     *
     * @return List of transactions
     */
    @Override
    public List<Transaction> getTransactions(String userId) {
        Query query = em.createQuery("select t from Transaction t where t.account.id = :userId order by t.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Persists the transaction in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param transaction Transaction instance
     * @return Transaction ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistTransaction(Transaction transaction) {
        return persistEntity(transaction);
    }

    /**
     * Removes the transaction from the database.
     *
     * @param id Transaction ID
     * @return true if the Transaction entity has been deleted
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteTransaction(Long id) {
        return deleteEntity(id, Transaction.class);
    }

    @Override
    @Transactional(readOnly = false)
    public void allocateAmount(Long transactionId, BigDecimal amount) {
        // TODO
    }

    @Override
    @Transactional(readOnly = false)
    public void allocateLockedAmount(Long transactionId, BigDecimal amount) {
        // TODO
    }

    @Override
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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