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
     * @param maxAmount    Maximum amount
     * @param isQueued     Indicates whether the generated GL transactions should be put in a queue or not
     */
    @Override
    public void applyPayments(List<Transaction> transactions, BigDecimal maxAmount, boolean isQueued) {
        List<GlTransaction> glTransactions = new LinkedList<GlTransaction>();
        for (Transaction transaction : transactions) {
            if (transaction instanceof Credit) {
                TransactionTypeId creditTypeId = transaction.getTransactionType().getId();
                List<CreditPermission> creditPermissions = transactionService.getCreditPermissions(creditTypeId);
                // Assuming that credit permissions are sorted by priorities in descending order
                for (CreditPermission creditPermission : creditPermissions) {
                    Integer priority = creditPermission.getPriority();
                    for (Transaction transaction1 : transactions) {
                        if (transactionService.canPay(transaction.getId(), transaction1.getId(), priority)) {
                            BigDecimal amount = (transaction.getAmount() != null) ? transaction.getAmount() : BigDecimal.ZERO;
                            BigDecimal amount1 = (transaction1.getAmount() != null) ? transaction1.getAmount() : BigDecimal.ZERO;
                            BigDecimal minAmount = (amount.compareTo(amount1) <= 0) ? amount : amount1;
                            boolean lessThanMaxAmount = (maxAmount == null) || minAmount.compareTo(maxAmount) < 0;
                            if (amount.compareTo(minAmount) > 0 && lessThanMaxAmount) {
                                CompositeAllocation allocation =
                                        transactionService.createAllocation(transaction.getId(), transaction1.getId(),
                                                minAmount, isQueued);
                                glTransactions.add(allocation.getCreditGlTransaction());
                                glTransactions.add(allocation.getDebitGlTransaction());
                                if (maxAmount != null) {
                                    maxAmount = maxAmount.subtract(minAmount);
                                }
                                // TODO: provide business logic
                            }
                        }
                    }
                }
            }
        }


    }

}