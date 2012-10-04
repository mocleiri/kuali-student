package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.*;

/**
 * Payment service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("paymentService")
@Transactional(readOnly = true)
@WebService(serviceName = PaymentService.SERVICE_NAME, portName = PaymentService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class PaymentServiceImpl extends GenericPersistenceService implements PaymentService {

    private static final Log logger = LogFactory.getLog(PaymentServiceImpl.class);

    @Autowired
    private TransactionService transactionService;


    /**
     * This method takes the list that has been manipulated by the other payment application filters.
     * The system will then iterate through the list and apply payments, following simple payment logic.
     * Note that when a payment is encountered, the priority in the permissibleDebitArray will be used.
     * Where a charge is encountered, the next available payment will be applied (that is allowed to pay the charge).
     * Under normal circumstances, payments will be first in the list, unless the user wants to override this behavior.
     * If maximumAmount is passed, then the payment application will not allocate more than that amount.
     * If isQueued is set as false, the general ledger transactions that are created will be put into status of
     * Waiting, so they will not be transmitted to the general ledger until this status is set to Queued.
     * This will usually be done by passing the list of general ledger transactions to the
     * summarizeGeneralLedgerTransactions() method.
     *
     * @param transactions List of transactions
     * @param maxAmount    Maximum amount allowed
     * @param isQueued     Indicates whether the generated GL transactions should be put in a queue or not
     * @return List of generated GL transactions
     */
    @Override
    public List<GlTransaction> applyPayments(List<Transaction> transactions, BigDecimal maxAmount, boolean isQueued) {
        BigDecimal remainingAmount = maxAmount;
        List<GlTransaction> glTransactions = new LinkedList<GlTransaction>();
        List<CreditPermission> creditPermissions = Collections.emptyList();
        for (Transaction transaction : transactions) {
            Long transactionId = transaction.getId();
            TransactionTypeId transactionTypeId = transaction.getTransactionType().getId();
            // Check if it is Credit
            boolean isCredit = (transaction instanceof Credit);
            if (isCredit) {
                // Assuming that credit permissions are sorted by priorities in descending order
                creditPermissions = transactionService.getCreditPermissions(transactionTypeId);
                if (creditPermissions == null) {
                    creditPermissions = Collections.emptyList();
                }
            }
            int i = 0;
            do {
                Integer currentPriority = null;
                if (isCredit && i < creditPermissions.size()) {
                    // Getting the next priority from the sorted credit permissions
                    currentPriority = creditPermissions.get(i++).getPriority();
                }
                for (Transaction transaction1 : transactions) {
                    Long transactionId1 = transaction1.getId();
                    // We have to exclude the same transaction
                    if (!transactionId.equals(transactionId1)) {
                        // Check if the transactions can pay one another
                        boolean canPay = false;
                        if (isCredit && currentPriority != null) {
                            // Credit
                            canPay = transactionService.canPay(transactionId, transactionId1, currentPriority);
                        } else if (!isCredit) {
                            // Debit
                            canPay = transactionService.canPay(transactionId, transactionId1);
                        }
                        if (canPay) {
                            BigDecimal amount = transactionService.getUnallocatedAmount(transaction);
                            logger.info("First transaction ID = " + transactionId + ", unallocated amount = "
                                    + amount.toPlainString());
                            BigDecimal amount1 = transactionService.getUnallocatedAmount(transaction1);
                            logger.info("Second transaction ID = " + transactionId1 + ", unallocated amount = " +
                                    amount1.toPlainString());
                            if (amount.compareTo(BigDecimal.ZERO) > 0 && amount1.compareTo(BigDecimal.ZERO) > 0) {
                                BigDecimal minAmount = (amount.compareTo(amount1) <= 0) ? amount : amount1;
                                logger.info("Amount to allocate = " + minAmount.toPlainString());
                                if (remainingAmount == null || minAmount.compareTo(remainingAmount) <= 0) {
                                    // If the smallest amount is less than or equal to the remaining amount
                                    // create a new allocation
                                    CompositeAllocation allocation = transactionService.createAllocation(transaction,
                                            transaction1, minAmount, isQueued, false);
                                    logger.info("Created allocation between " +
                                            transaction.getTransactionTypeValue() + "(" +
                                            transactionId + ") and " + transaction1.getTransactionTypeValue() +
                                            "(" + transactionId1 + ") transactions. Allocated amount = " +
                                            allocation.getAllocation().getAmount());
                                    // Adding new GL transactions to the list
                                    glTransactions.addAll(allocation.getGlTransactions());
                                    if (remainingAmount != null) {
                                        remainingAmount = remainingAmount.subtract(minAmount);
                                    }
                                }
                                if (remainingAmount != null && remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                                    // We have exceeded the limit and just need to return the result
                                    return glTransactions;
                                }
                            }
                        }
                    }
                }
            } while (isCredit && i < creditPermissions.size());
        }
        return glTransactions;
    }

}