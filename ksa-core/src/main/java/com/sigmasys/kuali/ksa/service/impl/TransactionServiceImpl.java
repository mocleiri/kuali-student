package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.CalendarService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Transaction service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("transactionService")
@Transactional(readOnly = true)
@WebService(serviceName = TransactionService.SERVICE_NAME, portName = TransactionService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class TransactionServiceImpl extends GenericPersistenceService implements TransactionService {

    private static final Log logger = LogFactory.getLog(TransactionServiceImpl.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private UserSessionManager userSessionManager;


    private <T extends Transaction> List<T> getTransactions(Class<T> entityType, String... userIds) {
        Query query = em.createQuery("select t from " + entityType.getName() + " t " +
                " left outer join fetch t.transactionType tt " +
                " left outer join fetch t.account a " +
                " left outer join fetch t.currency c " +
                " left outer join fetch t.rollup r " +
                " left outer join fetch t.document d " +
                ((userIds != null && userIds.length > 0) ? " where t.account.id in (:userIds) " : "") +
                " order by t.id desc");
        if (userIds != null && userIds.length > 0) {
            query.setParameter("userIds", Arrays.asList(userIds));
        }
        return (List<T>) query.getResultList();
    }

    private <T extends Transaction> T getTransaction(Long id, Class<T> entityType) {
        Query query = em.createQuery("select t from " + entityType.getName() + " t " +
                " left outer join fetch t.transactionType tt " +
                " left outer join fetch t.account a " +
                " left outer join fetch t.currency c " +
                " left outer join fetch t.rollup r " +
                " left outer join fetch t.document d " +
                " where t.id = :id ");
        query.setParameter("id", id);
        List<T> transactions = query.getResultList();
        return (transactions != null && !transactions.isEmpty()) ? transactions.get(0) : null;
    }

    /**
     * Creates a new transaction based on the given parameters
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param userId            Account ID
     * @param effectiveDate     Transaction effective Date
     * @param amount            Transaction amount
     * @return new Transaction instance
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public Transaction createTransaction(String transactionTypeId, String userId, Date effectiveDate, BigDecimal amount) {
        return createTransaction(transactionTypeId, null, userId, effectiveDate, amount);
    }

    /**
     * Creates a new transaction based on the given parameters
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param externalId            Transaction External ID
     * @param userId            Account ID
     * @param effectiveDate     Transaction effective Date
     * @param amount            Transaction amount
     * @return new Transaction instance
     */
    @Override
    @Transactional(readOnly = false)
    public Transaction createTransaction(String transactionTypeId, String externalId, String userId, Date effectiveDate,
                                         BigDecimal amount) {
        TransactionTypeId id = getTransactionType(transactionTypeId, effectiveDate).getId();
        return createTransaction(id, externalId, userId, effectiveDate, amount);
    }

    /**
     * Returns the transaction type instance for the given transaction type ID and effective date
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param effectiveDate     Transaction effective Date
     * @return TransactionType instance
     */
    @Override
    public TransactionType getTransactionType(String transactionTypeId, Date effectiveDate) {
        Query query = em.createQuery("select t from TransactionType t " +
                " where t.id.id = :transactionTypeId and :effectiveDate >= t.startDate and " +
                " (t.endDate is null or t.endDate > :effectiveDate)");
        query.setParameter("transactionTypeId", transactionTypeId);
        query.setParameter("effectiveDate", effectiveDate);
        List<TransactionType> transactionTypes = query.getResultList();
        if (transactionTypes != null && !transactionTypes.isEmpty()) {
            return transactionTypes.get(0);
        }
        String errMsg = "Cannot find TransactionType for ID = " + transactionTypeId + " and date = " + effectiveDate;
        logger.error(errMsg);
        throw new IllegalStateException(errMsg);
    }

    /**
     * Creates a new transaction based on the given parameters
     *
     * @param id            Transaction type ID
     * @param userId        Account ID
     * @param effectiveDate Transaction effective Date
     * @param amount        Transaction amount
     * @return new Transaction instance
     */
    @Transactional(readOnly = false)
    protected Transaction createTransaction(TransactionTypeId id, String externalId, String userId, Date effectiveDate,
                                            BigDecimal amount) {

        TransactionType transactionType = em.find(TransactionType.class, id);
        if (transactionType == null) {
            String errMsg = "Transaction type does not exist for the given ID = " + id;
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Account account = em.find(Account.class, userId);
        if (account == null) {
            String errMsg = "Account does not exist for the given ID = " + userId;
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        String currencyCode = java.util.Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        Currency currency = currencyService.getCurrency(currencyCode);
        if (currency == null) {
            String errMsg = "Currency does not exist for the given ISO code = " + currencyCode;
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Transaction transaction = (transactionType instanceof CreditType) ? new Payment() : new Charge();

        transaction.setTransactionType(transactionType);
        transaction.setAccount(account);
        transaction.setCurrency(currency);

        transaction.setExternalId(externalId);
        transaction.setEffectiveDate(effectiveDate);
        transaction.setNativeAmount(amount);
        transaction.setAmount(amount);
        transaction.setAllocatedAmount(BigDecimal.ZERO);
        transaction.setLockedAllocatedAmount(BigDecimal.ZERO);

        transaction.setRollup(transactionType.getRollup());
        transaction.setStatementText(transactionType.getDescription());
        transaction.setGlEntryGenerated(false);
        transaction.setInternal(false);

        transaction.setResponsibleEntity(userSessionManager.getUserId(RequestUtils.getThreadRequest()));

        if (transaction instanceof Payment) {
            CreditType creditType = (CreditType) transactionType;
            Payment payment = (Payment) transaction;
            int clearPeriod = (creditType.getClearPeriod() != null) ? creditType.getClearPeriod() : 0;
            payment.setClearDate(calendarService.addCalendarDays(effectiveDate, clearPeriod));
            payment.setRefundRule(creditType.getRefundRule());
            payment.setRefundable(creditType.getRefundRule() != null);
        } else {
            Charge charge = (Charge) transaction;
            charge.setDeferred(false);
            charge.setGlOverriden(false);
        }

        persistTransaction(transaction);

        return transaction;
    }


    /**
     * Returns Transaction by ID
     *
     * @param id Transaction ID
     * @return Transaction instance
     */
    @Override
    public Transaction getTransaction(Long id) {
        return getTransaction(id, Transaction.class);
    }

    /**
     * Returns Charge by ID
     *
     * @param id Charge ID
     * @return Charge instance
     */
    @Override
    public Charge getCharge(Long id) {
        return getTransaction(id, Charge.class);
    }

    /**
     * Returns Payment by ID
     *
     * @param id Payment ID
     * @return Payment instance
     */
    @Override
    public Payment getPayment(Long id) {
        return getTransaction(id, Payment.class);
    }

    /**
     * Returns Deferment by ID
     *
     * @param id Deferment ID
     * @return Deferment instance
     */
    @Override
    public Deferment getDeferment(Long id) {
        return getTransaction(id, Deferment.class);
    }


    /**
     * Returns all transactions sorted by ID
     *
     * @return List of transactions
     */
    @Override
    @WebMethod(exclude = true)
    public List<Transaction> getTransactions() {
        return getTransactions(Transaction.class);
    }

    /**
     * Returns all charges sorted by ID
     *
     * @return List of all charges
     */
    @Override
    @WebMethod(exclude = true)
    public List<Charge> getCharges() {
        return getTransactions(Charge.class);
    }

    /**
     * Returns all charges by account ID
     *
     * @param userId Account ID
     * @return List of all charges by account ID
     */
    @Override
    public List<Charge> getCharges(String userId) {
        return getTransactions(Charge.class, userId);
    }

    /**
     * Returns all payments sorted by ID
     *
     * @return List of all payments
     */
    @Override
    @WebMethod(exclude = true)
    public List<Payment> getPayments() {
        return getTransactions(Payment.class);
    }

    /**
     * Returns all payments by account ID
     *
     * @param userId Account ID
     * @return List of all payments by account ID
     */
    @Override
    public List<Payment> getPayments(String userId) {
        return getTransactions(Payment.class, userId);
    }


    /**
     * Returns all deferments sorted by ID
     *
     * @return List of all deferments
     */
    @Override
    @WebMethod(exclude = true)
    public List<Deferment> getDeferments() {
        return getTransactions(Deferment.class);
    }

    /**
     * Returns all deferments by account ID
     *
     * @param userId Account ID
     * @return List of all deferments by account ID
     */
    @Override
    public List<Deferment> getDeferments(String userId) {
        return getTransactions(Deferment.class, userId);
    }

    /**
     * Returns all transactions sorted by ID
     *
     * @param userId Account ID
     * @return List of transactions
     */
    @Override
    public List<Transaction> getTransactions(String userId) {
        return getTransactions(Transaction.class, userId);
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
    public void createAllocation(Long transactionId1, Long transactionId2, BigDecimal amount) {
        createAllocation(transactionId1, transactionId2, amount, false);
    }

    protected void createAllocation(Long transactionId1, Long transactionId2, BigDecimal newAmount, boolean locked) {

        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            String errMsg = "The allocation amount should be a positive number";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Transaction transaction1 = getTransaction(transactionId1);
        if (transaction1 == null) {
            String errMsg = "Transaction with ID = " + transactionId1 + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Transaction transaction2 = getTransaction(transactionId2);
        if (transaction2 == null) {
            String errMsg = "Transaction with ID = " + transactionId2 + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        if (transaction1.getAccount() == null || transaction2.getAccount() == null) {
            String errMsg = "Transaction must be associated with Account";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        Account account = transaction1.getAccount();
        String userId = account.getId();
        if (!userId.equals(transaction2.getAccount().getId())) {
            String errMsg = "Both transactions must be associated with the same account";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        Query query = em.createQuery("select a from Allocation a " +
                " join fetch a.firstTransaction t1 " +
                " join fetch a.secondTransaction t2 " +
                " where a.account.id = :userId");
        query.setParameter("userId", userId);

        List<Allocation> allocations = query.getResultList();
        for (Allocation allocation : allocations) {
            Long id1 = allocation.getFirstTransaction().getId();
            Long id2 = allocation.getSecondTransaction().getId();
            if ((id1.equals(transactionId1) && id2.equals(transactionId2)) ||
                    (id1.equals(transactionId2) && id2.equals(transactionId1))) {
                BigDecimal allocatedAmount1 = transaction1.getAllocatedAmount() != null ?
                        transaction1.getAllocatedAmount() : BigDecimal.ZERO;
                BigDecimal allocatedAmount2 = transaction2.getAllocatedAmount() != null ?
                        transaction2.getAllocatedAmount() : BigDecimal.ZERO;
                BigDecimal newAllocatedAmount1 = (allocatedAmount1.compareTo(BigDecimal.ZERO) >= 0) ?
                        allocatedAmount1.subtract(allocation.getAmount()) :
                        allocatedAmount1.add(allocation.getAmount());
                BigDecimal newAllocatedAmount2 = (allocatedAmount2.compareTo(BigDecimal.ZERO) >= 0) ?
                        allocatedAmount2.subtract(allocation.getAmount()) :
                        allocatedAmount2.add(allocation.getAmount());
                transaction1.setAllocatedAmount(newAllocatedAmount1);
                transaction2.setAllocatedAmount(newAllocatedAmount2);
                deleteEntity(allocation.getId(), Allocation.class);
            }
        }

        BigDecimal unallocatedAmount1 = getUnallocatedAmount(transaction1);
        BigDecimal unallocatedAmount2 = getUnallocatedAmount(transaction2);

        if (unallocatedAmount1.abs().compareTo(newAmount) < 0 || unallocatedAmount2.abs().compareTo(newAmount) < 0) {
            String errMsg = "Not enough balance to cover the allocation amount " + newAmount;
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        TransactionType transactionType1 = transaction1.getTransactionType();
        TransactionType transactionType2 = transaction2.getTransactionType();

        boolean canAllocate = false;
        if ((transactionType1 instanceof DebitType && transactionType2 instanceof CreditType) ||
                (transactionType1 instanceof CreditType && transactionType2 instanceof DebitType)) {
            canAllocate = (unallocatedAmount1.compareTo(BigDecimal.ZERO) > 0 &&
                    unallocatedAmount2.compareTo(BigDecimal.ZERO) > 0);
        } else if ((transactionType1 instanceof DebitType && transactionType2 instanceof DebitType) ||
                (transactionType1 instanceof CreditType && transactionType2 instanceof CreditType)) {
            canAllocate = (unallocatedAmount1.compareTo(BigDecimal.ZERO) > 0 &&
                    unallocatedAmount2.compareTo(BigDecimal.ZERO) < 0) ||
                    (unallocatedAmount1.compareTo(BigDecimal.ZERO) < 0 &&
                            unallocatedAmount2.compareTo(BigDecimal.ZERO) > 0);
        }

        if (canAllocate) {
            Allocation allocation = new Allocation();
            allocation.setAccount(account);
            allocation.setFirstTransaction(transaction1);
            allocation.setSecondTransaction(transaction2);
            allocation.setAmount(newAmount);
            allocation.setLocked(locked);
            if (locked) {
                transaction1.setLockedAllocatedAmount(newAmount);
                transaction2.setLockedAllocatedAmount(newAmount);
            } else {
                transaction1.setAllocatedAmount(newAmount);
                transaction2.setAllocatedAmount(newAmount);
            }
        } else {
            String errMsg = "Illegal allocation. Transaction IDs: " + transactionId1 + ", " + transactionId2 +
                    " Amount: " + newAmount;
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
    }

    protected BigDecimal getUnallocatedAmount(Transaction transaction) {

        BigDecimal amount = transaction.getAmount() != null ?
                transaction.getAmount() : BigDecimal.ZERO;

        BigDecimal allocatedAmount = transaction.getAllocatedAmount() != null ?
                transaction.getAllocatedAmount() : BigDecimal.ZERO;

        BigDecimal lockedAllocatedAmount = transaction.getLockedAllocatedAmount() != null ?
                transaction.getLockedAllocatedAmount() : BigDecimal.ZERO;

        return amount.subtract(allocatedAmount.add(lockedAllocatedAmount));
    }

    @Override
    @Transactional(readOnly = false)
    public void createLockedAllocation(Long transactionId1, Long transactionId2, BigDecimal amount) {
        createAllocation(transactionId1, transactionId2, amount, true);
    }

    @Override
    @Transactional(readOnly = false)
    public void createTransactionMemo(Long transactionId, String memoText) {
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

}