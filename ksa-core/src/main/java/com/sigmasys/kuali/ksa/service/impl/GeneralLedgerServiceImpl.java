package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.exception.InvalidGeneralLedgerTypeException;
import com.sigmasys.kuali.ksa.exception.TransactionNotFoundException;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * General Ledger service implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("generalLedgerService")
@Transactional(readOnly = true)
@WebService(serviceName = GeneralLedgerService.SERVICE_NAME, portName = GeneralLedgerService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class GeneralLedgerServiceImpl extends GenericPersistenceService implements GeneralLedgerService {

    private static final Log logger = LogFactory.getLog(GeneralLedgerServiceImpl.class);


    @Autowired
    private ConfigService configService;


    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param description   Transaction description
     * @param isQueued      Set status to Q unless isQueued is passed and is false, in which case, set status to W
     * @return new GL Transaction instance
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount,
                                             String description, boolean isQueued) {

        Transaction transaction = em.find(Transaction.class, transactionId);
        if (transaction == null) {
            String errMsg = "Transaction with ID = " + transactionId + " does not exist";
            logger.error(errMsg);
            throw new TransactionNotFoundException(errMsg);
        }

        GlTransaction glTransaction = new GlTransaction();
        glTransaction.setDate(new Date());
        glTransaction.setAmount(amount);
        glTransaction.setGlAccountId(userId);
        glTransaction.setDescription(description);
        glTransaction.setTransactions(new HashSet<Transaction>(Arrays.asList(transaction)));
        glTransaction.setStatus(isQueued ? GlTransactionStatus.QUEUED : GlTransactionStatus.WAITING);

        persistEntity(glTransaction);

        return glTransaction;

    }

    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param description   Transaction description
     * @return new GL Transaction instance
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount,
                                             String description) {
        return createGlTransaction(transactionId, userId, amount, description, true);

    }

    /**
     * Summarizes the general ledger transactions for the given GL transaction list
     *
     * @param glTransactions List of general ledger transactions
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public void summarizeGlTransactions(List<GlTransaction> glTransactions) {
        // Map of GL Account ID and set of transactions for this GL account
        Map<String, Set<Transaction>> transactionMap = new HashMap<String, Set<Transaction>>();
        // Map of GL Account ID and total transaction amount for this GL account
        Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
        // Map of GL Account ID and the first GL transaction for this GL account from the given list
        Map<String, List<GlTransaction>> glTransactionMap = new HashMap<String, List<GlTransaction>>();

        for (GlTransaction glTransaction : glTransactions) {
            if (glTransaction.getStatus() != null && GlTransactionStatus.WAITING == glTransaction.getStatus()) {
                String glAccountId = glTransaction.getGlAccountId();
                if (glAccountId != null) {

                    Set<Transaction> transactionsForAccount = transactionMap.get(glAccountId);
                    if (transactionsForAccount == null) {
                        transactionsForAccount = new HashSet<Transaction>();
                        transactionMap.put(glAccountId, transactionsForAccount);
                    }
                    // Adding all transactions to the list of transactions for the current GL account
                    transactionsForAccount.addAll(glTransaction.getTransactions());

                    List<GlTransaction> glTransactionsForAccount = glTransactionMap.get(glAccountId);
                    if (glTransactionsForAccount == null) {
                        glTransactionsForAccount = new LinkedList<GlTransaction>();
                        glTransactionMap.put(glAccountId, glTransactionsForAccount);
                    }
                    // Adding GL transaction to the list of GL transactions for the current GL account
                    glTransactionsForAccount.add(glTransaction);

                    BigDecimal amount = amountMap.get(glAccountId);
                    if (amount == null) {
                        amount = BigDecimal.ZERO;
                    }

                    for (Transaction trans : glTransaction.getTransactions()) {
                        amount = amount.add(trans.getAmount() != null ? trans.getAmount() : BigDecimal.ZERO);
                    }
                    // Sum up the transaction amount for the current GL account
                    amountMap.put(glAccountId, amount.add(amount));
                }
            }
        }

        Set<Long> glTransactionIdsToDelete = new HashSet<Long>();
        for (String glAccountId : glTransactionMap.keySet()) {
            List<GlTransaction> glTransactionsForAccount = glTransactionMap.get(glAccountId);
            if (glTransactionsForAccount != null && !glTransactionsForAccount.isEmpty()) {
                BigDecimal totalAmount = amountMap.get(glAccountId);
                if (!BigDecimal.ZERO.equals(totalAmount)) {
                    // Persisting the first GL transaction with the total amount and all
                    // transactions for the current GL account
                    GlTransaction firstGlTransaction = glTransactionsForAccount.get(0);
                    firstGlTransaction.setTransactions(transactionMap.get(glAccountId));
                    firstGlTransaction.setAmount(totalAmount);
                    persistEntity(firstGlTransaction);
                    // We have to remove the rest of GL transactions
                    for (int i = 1; i < glTransactionsForAccount.size(); i++) {
                        glTransactionIdsToDelete.add(glTransactionsForAccount.get(i).getId());
                    }

                } else {
                    for (GlTransaction glTransaction : glTransactionsForAccount) {
                        glTransactionIdsToDelete.add(glTransaction.getId());
                    }
                }
            }
        }
        // Deleting GL transactions that must be eliminated from the list and database
        for (Iterator<GlTransaction> iterator = glTransactions.iterator(); iterator.hasNext(); ) {
            GlTransaction glTransaction = iterator.next();
            if (glTransactionIdsToDelete.contains(glTransaction.getId())) {
                deleteEntity(glTransaction.getId(), GlTransaction.class);
                iterator.remove();
            } else {
                glTransaction.setStatus(GlTransactionStatus.QUEUED);
                persistEntity(glTransaction);
            }
        }
    }

    /**
     * Returns the general ledger type instance for the given code.
     *
     * @param glTypeCode General Ledger type code
     * @return GeneralLedgerType instance
     */
    @Override
    public GeneralLedgerType getGeneralLedgerType(String glTypeCode) {
        Query query = em.createQuery("select t from GeneralLedgerType t where t.code = :code");
        query.setParameter("code", glTypeCode);
        List<GeneralLedgerType> glTypes = query.getResultList();
        if (glTypes != null && !glTypes.isEmpty()) {
            return glTypes.get(0);
        }
        String errMsg = "Cannot find GeneralLedgerType for the code = " + glTypeCode;
        logger.error(errMsg);
        throw new InvalidGeneralLedgerTypeException(errMsg);
    }

    /**
     * Returns the default general ledger type instance for the given code.
     *
     * @return GeneralLedgerType instance
     */
    @Override
    public GeneralLedgerType getDefaultGeneralLedgerType() {
        return getGeneralLedgerType(configService.getInitialParameter(Constants.DEFAULT_GL_TYPE_PARAM_NAME));
    }

    /**
     * Gets all queued or in session general ledger transactions within the date range specified and
     * adds the recognition period to the transmission
     *
     * @param recognitionPeriod Recognition period
     * @param fromDate          Start date
     * @param toDate            End date
     * @return true if one or more records have been updated, false - otherwise
     */
    public boolean setRecognitionPeriod(String recognitionPeriod, Date fromDate, Date toDate) {
        Query query = em.createQuery("update GlTransmission " +
                " set recognitionPeriod = :recognitionPeriod " +
                " where id in " +
                " (select t.transmission.id " +
                "  from GlTransaction t " +
                "  where t.transmission <> null and t.date between :fromDate and :toDate)");
        query.setParameter("recognitionPeriod", recognitionPeriod);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.executeUpdate() > 0;
    }


}