package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.*;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.List;

/**
 * Payment service provides KSA payment functionality which is partially rule-based.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Url(PaymentService.SERVICE_URL)
@WebService(serviceName = PaymentService.SERVICE_NAME, portName = PaymentService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface PaymentService {

    String SERVICE_URL = "payment.webservice";
    String SERVICE_NAME = "PaymentService";
    String PORT_NAME = SERVICE_NAME + "Port";


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
    void applyPayments(List<Transaction> transactions, BigDecimal maxAmount, boolean isQueued);


}
