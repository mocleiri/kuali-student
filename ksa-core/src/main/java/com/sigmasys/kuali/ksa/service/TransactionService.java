package com.sigmasys.kuali.ksa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.*;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Transaction service declares business operations on Transaction and related
 * objects
 * <p/>
 *
 * @author Michael Ivanov
 */
@Url(TransactionService.SERVICE_URL)
@WebService(serviceName = TransactionService.SERVICE_NAME, portName = TransactionService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface TransactionService {

    String SERVICE_URL = "transaction.webservice";
    String SERVICE_NAME = "TransactionService";
    String PORT_NAME = SERVICE_NAME + "Port";


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
    @WebMethod(exclude = true)
    Transaction createTransaction(String transactionTypeId, String userId, Date effectiveDate, BigDecimal amount);

    /**
     * Creates a new transaction based on the given parameters
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param externalId        Transaction external ID
     * @param userId            Account ID
     * @param effectiveDate     Transaction effective Date
     * @param expirationDate    used for deferments only
     * @param amount            Transaction amount
     * @return new Transaction instance
     */
    Transaction createTransaction(String transactionTypeId, String externalId, String userId,
                                  Date effectiveDate, Date expirationDate, BigDecimal amount);

    /**
     * Creates a new transaction based on the given parameters
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param externalId        Transaction External ID
     * @param userId            Account ID
     * @param effectiveDate     Transaction effective Date
     * @param expirationDate    used for deferments only
     * @param amount            Transaction amount
     * @param overrideBlocks    indicates whether the account blocks must be overridden
     * @return new Transaction instance
     */
    @WebMethod(exclude = true)
    Transaction createTransaction(String transactionTypeId, String externalId, String userId,
                                  Date effectiveDate, Date expirationDate,
                                  BigDecimal amount, boolean overrideBlocks);

    /**
     * Returns the transaction type instance for the given transaction type ID and effective date
     *
     * @param transactionTypeId The first part of TransactionTypeId PK, the second part (sub-code) will be calculated
     *                          based on the effective date
     * @param effectiveDate     Transaction effective Date
     * @return TransactionType instance
     */
    TransactionType getTransactionType(String transactionTypeId, Date effectiveDate);

    /**
     * Returns the transaction type instance for the given transaction type ID and effective date
     *
     * @param transactionTypeId TransactionTypeId instance
     * @return TransactionType instance
     */
    @WebMethod(exclude = true)
    TransactionType getTransactionType(TransactionTypeId transactionTypeId);


    /**
     * Returns Transaction by ID
     *
     * @param id Transaction ID
     * @return Transaction instance
     */
    Transaction getTransaction(Long id);

    /**
     * Returns Charge by ID
     *
     * @param id Charge ID
     * @return Charge instance
     */
    Charge getCharge(Long id);

    /**
     * Returns Payment by ID
     *
     * @param id Payment ID
     * @return Payment instance
     */
    Payment getPayment(Long id);

    /**
     * Returns Deferment by ID
     *
     * @param id Deferment ID
     * @return Deferment instance
     */
    Deferment getDeferment(Long id);

    /**
     * Returns all transactions sorted by ID
     *
     * @return List of transactions
     */
    @WebMethod(exclude = true)
    List<Transaction> getTransactions();

    /**
     * Returns all transactions sorted by ID
     *
     * @return List of transactions
     */
    List<Transaction> getTransactions(String userId);

    /**
     * Returns all charges by account ID
     *
     * @param userId Account ID
     * @return List of all charges by account ID
     */
    List<Charge> getCharges(String userId);

    /**
     * Returns all charges sorted by ID
     *
     * @return List of all charges
     */
    @WebMethod(exclude = true)
    List<Charge> getCharges();

    /**
     * Returns all payments sorted by ID
     *
     * @return List of all payments
     */
    @WebMethod(exclude = true)
    List<Payment> getPayments();

    /**
     * Returns all payments by account ID
     *
     * @param userId Account ID
     * @return List of all payments by account ID
     */
    List<Payment> getPayments(String userId);

    /**
     * Returns all deferments sorted by ID
     *
     * @return List of all deferments
     */
    @WebMethod(exclude = true)
    List<Deferment> getDeferments();

    /**
     * Returns all deferments by account ID
     *
     * @param userId Account ID
     * @return List of all deferments by account ID
     */
    List<Deferment> getDeferments(String userId);

    /**
     * Persists the transaction in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param transaction Transaction instance
     * @return Transaction ID
     */
    Long persistTransaction(Transaction transaction);

    /**
     * Removes the transaction from the database.
     *
     * @param id Transaction ID
     * @return true if the Transaction entity has been deleted
     */
    boolean deleteTransaction(Long id);


    /**
     * Checks if the first transaction can pay the second transaction.
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @return true if transactionId1 can pay transactionId2, false - otherwise
     */
    @WebMethod(exclude = true)
    boolean canPay(Long transactionId1, Long transactionId2);


    /**
     * Checks if the first transaction can pay the second transaction.
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param priority       priority
     * @return true if transactionId1 can pay transactionId2, false - otherwise
     */
    @WebMethod(exclude = true)
    boolean canPay(Long transactionId1, Long transactionId2, int priority);

    /**
     * Checks if the first transaction can pay the second transaction.
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param priorityFrom   lower priority boundary
     * @param priorityTo     upper priority boundary
     * @return true if transactionId1 can pay transactionId2, false - otherwise
     */
    boolean canPay(Long transactionId1, Long transactionId2, int priorityFrom, int priorityTo);

    /**
     * This will allocate the value of amount on the transaction. A check will
     * be made to ensure that the allocated amount is equal to or less than the
     * localAmount, less any lockedAllocationAmount. The expectation is that
     * this method will only be called by the payment application module.
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param amount         amount of money to be allocated
     * @return a new CompositeAllocation instance that has references to Allocation and GL transactions
     */
    @WebMethod(exclude = true)
    CompositeAllocation createAllocation(Long transactionId1, Long transactionId2, BigDecimal amount);

    /**
     * This will allocate the value of amount on the transaction. A check will
     * be made to ensure that the allocated amount is equal to or less than the
     * localAmount, less any lockedAllocationAmount. The expectation is that
     * this method will only be called by the payment application module.
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param amount         amount of money to be allocated
     * @param isQueued       indicates whether the GL transaction should be in Q or W status
     * @return a new allocation
     */
    CompositeAllocation createAllocation(Long transactionId1, Long transactionId2, BigDecimal amount, boolean isQueued);

    /**
     * This will allocate a locked amount on the transaction. A check will be
     * made to ensure that the lockedAmount and the allocateAmount don't exceed
     * the ledgerAmount of the transaction. Setting an amount as locked prevents
     * the payment application system from reallocating the balance elsewhere.
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param amount         amount of money to be allocated
     * @return a new CompositeAllocation instance that has references to Allocation and GL transactions
     */
    @WebMethod(exclude = true)
    CompositeAllocation createLockedAllocation(Long transactionId1, Long transactionId2, BigDecimal amount);

    /**
     * This will allocate a locked amount on the transaction. A check will be
     * made to ensure that the lockedAmount and the allocateAmount don't exceed
     * the ledgerAmount of the transaction. Setting an amount as locked prevents
     * the payment application system from reallocating the balance elsewhere.
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param amount         amount of money to be allocated
     * @param isQueued       indicates whether the GL transaction should be in Q or W status
     * @return a new CompositeAllocation instance that has references to Allocation and GL transactions
     */
    CompositeAllocation createLockedAllocation(Long transactionId1, Long transactionId2, BigDecimal amount, boolean isQueued);

    /**
     * Removes all allocations associated with the given transactions
     * <p/>
     *
     * @param transactionId transaction1 ID
     * @return list of generated GL transactions
     */
    List<GlTransaction> removeAllocations(Long transactionId);

    /**
     * Removes allocation between two given transactions
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @return list of generated GL transactions
     */
    @WebMethod(exclude = true)
    List<GlTransaction> removeAllocation(Long transactionId1, Long transactionId2);

    /**
     * Removes allocation between two given transactions
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param isQueued       indicates whether the GL transaction should be in Q or W status
     * @return list of generated GL transactions
     */
    List<GlTransaction> removeAllocation(Long transactionId1, Long transactionId2, boolean isQueued);

    /**
     * Removes locked allocation between two given transactions
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @return list of generated GL transactions
     */
    @WebMethod(exclude = true)
    List<GlTransaction> removeLockedAllocation(Long transactionId1, Long transactionId2);

    /**
     * Removes locked allocation between two given transactions
     * <p/>
     *
     * @param transactionId1 transaction1 ID
     * @param transactionId2 transaction2 ID
     * @param isQueued       indicates whether the GL transaction should be in Q or W status
     * @return list of generated GL transactions
     */
    List<GlTransaction> removeLockedAllocation(Long transactionId1, Long transactionId2, boolean isQueued);


    /**
     * Moves a transaction from a pre-effective state to an effective state. Once a transaction is effective, its
     * general ledger entries are created. In certain cases, a transaction might be moved to an effective state
     * before its effective date, in which case, forceEffective is passed as true.
     *
     * @param transactionId  transaction ID
     * @param forceEffective indicates whether it has to be forced
     */
    void makeEffective(Long transactionId, boolean forceEffective);


    /**
     * If the reverse method is called, the system will generate a negative
     * transaction for the type of the original transaction. A memo transaction
     * will be generated, and the transactions will be locked together. Subject
     * to user customization, the transactions may be marked as hidden. (likely
     * that credits will not be hidden, debits will.) A charge to an account may
     * be reversed when a mistake is made, or a refund is issued. A payment may
     * be reversed when a payment bounces, or for some other reason is entered
     * on to the account and is not payable.
     *
     * @param transactionId   Transaction ID
     * @param memoText        Text of the memo to be created
     * @param partialAmount   Partial amount
     * @param statementPrefix Statement prefix that will be added to the existing Transaction statement
     */
    void reverseTransaction(Long transactionId, String memoText, BigDecimal partialAmount,
                            String statementPrefix);

    /**
     * A deferment may be expired automatically (when the date of the deferment
     * passes) or be expired manually but the system, either through user
     * intervention, or by a payment being received on the account that removes
     * the need for the deferment. If, for example, an account is paid in full,
     * the deferment would have to be expired, otherwise a credit balance would
     * technically occur on the account.
     */
    void expireDeferment(Long defermentId);

    /**
     * A deferment may be reduced or set to zero after expiration. Often, the
     * value of a deferment may not exceed the debit balance on the account to
     * prevent a credit balance being available for refund on the strength of a
     * deferment. A deferment may not be increased. Should such a situation
     * arise, the deferment would need to be expired, and a new deferment
     * issued.
     */
    void reduceDeferment(Long defermentId, BigDecimal newAmount);

    /**
     * Returns the transaction type for the given transaction type ID
     *
     * @param transactionTypeId The first part of TransactionTypeId PK
     * @return a subclass of TransactionType
     */
    @WebMethod(exclude = true)
    <T extends TransactionType> Class<T> getTransactionTypeClass(String transactionTypeId);

    /**
     * Determine if the transaction is allowed for the given account ID, transaction type and effective date
     *
     * @param accountId       Account ID
     * @param transactionType Transaction Type
     * @param effectiveDate   Effective Date
     * @return true/false
     */
    boolean isTransactionAllowed(String accountId, String transactionType, Date effectiveDate);

    /**
     * Returns the list of matching transactions for the given name pattern.
     *
     * @param pattern Statement text pattern
     * @return List of Transaction instances
     */
    List<Transaction> findTransactionsByStatementPattern(String pattern);

    /**
        * The logic of this is very similar to reverseTransaction(), except a partial write off is allowed, and only
        * credits can be written off. Also, the institution can choose to write off charges to a different general
        * ledger account, instead of the original, permitting the writing off to a general “bad debt” account, if they
        * so choose.
        *
        * @param transactionId Transaction ID
        * @param transactionTypeId TransactionType ID
        * @param memoText Memo test
        * @param statementPrefix Transaction statement prefix
        * @return a write-off transaction instance
        */
    Transaction writeOffTransaction(Long transactionId, TransactionTypeId transactionTypeId,
                                       String memoText, String statementPrefix);

}
