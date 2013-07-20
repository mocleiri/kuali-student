package com.sigmasys.kuali.ksa.service.pb;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.pb.PaymentBillingPlan;
import com.sigmasys.kuali.ksa.model.pb.PaymentBillingSchedule;
import com.sigmasys.kuali.ksa.model.pb.PaymentBillingTransaction;
import com.sigmasys.kuali.ksa.model.pb.PaymentBillingTransferDetail;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Payment Billing Service
 * <p/>
 *
 * @author Michael Ivanov
 */
@Url(PaymentBillingService.SERVICE_URL)
@WebService(serviceName = PaymentBillingService.SERVICE_NAME, portName = PaymentBillingService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface PaymentBillingService {

    String SERVICE_URL = "paymentBilling.webservice";
    String SERVICE_NAME = "PaymentBillingService";
    String PORT_NAME = SERVICE_NAME + "Port";

    /**
     * Creates and persists a payment billing transfer detail object for the given parameters.
     *
     * @param paymentBillingPlanId PaymentBillingPlan ID
     * @param accountId            DirectChargeAccount ID
     * @param maxAmount            Maximum amount to finance
     * @param initiationDate       Initiation Date
     * @return PaymentBillingTransferDetail instance
     */
    PaymentBillingTransferDetail generatePaymentBillingTransfer(Long paymentBillingPlanId,
                                                                String accountId,
                                                                BigDecimal maxAmount,
                                                                Date initiationDate);

    /**
     * Retrieves PaymentBillingPlan instance by ID from the persistent store.
     *
     * @param paymentBillingPlanId PaymentBillingPlan ID
     * @return PaymentBillingPlan instance
     */
    PaymentBillingPlan getPaymentBillingPlan(Long paymentBillingPlanId);

    /**
     * Retrieves PaymentBillingTransferDetail with ACTIVE status by ID from the persistent store.
     *
     * @param transferDetailId PaymentBillingTransferDetail ID
     * @return PaymentBillingTransferDetail instance
     */
    PaymentBillingTransferDetail getPaymentBillingTransferDetail(Long transferDetailId);


    /**
     * Reverses a payment billing transfer specified by PaymentBillingTransferDetail ID.
     *
     * @param transferDetailId PaymentBillingTransferDetail ID
     * @param removeFees       if true, then the fee charges will also be reversed
     * @param memoText         Memo text
     * @return PaymentBillingTransferDetail instance
     */
    PaymentBillingTransferDetail reversePaymentBillingTransfer(Long transferDetailId, boolean removeFees, String memoText);


    /**
     * Persists PaymentBillingTransferDetail instance in the persistent store.
     *
     * @param transferDetail PaymentBillingTransferDetail instance
     * @return PaymentBillingTransferDetail ID
     */
    Long persistPaymentBillingTransferDetail(PaymentBillingTransferDetail transferDetail);

    /**
     * Returns a list of payment billing transactions for the plan specified by ID.
     *
     * @param paymentBillingPlanId PaymentBillingPlan ID
     * @return list of PaymentBillingTransaction instances
     */
    List<PaymentBillingTransaction> getPaymentBillingTransactions(Long paymentBillingPlanId);

    /**
     * Generates a list of PaymentBillingSchedule objects for the given PaymentBillingPlan ID
     *
     * @param paymentBillingPlanId PaymentBillingPlan ID
     * @return list of PaymentBillingSchedule instances
     */
    List<PaymentBillingSchedule> generatePaymentBillingSchedules(Long paymentBillingPlanId);

    /**
     * This method creates the transactions that make up the payment billing charges and
     * nets off the transactions that are financed.
     *
     * @param paymentBillingPlanId PaymentBillingPlan ID
     * @return PaymentBillingTransferDetail instance
     */
    PaymentBillingTransferDetail generatePaymentBillingTransactions(Long paymentBillingPlanId);


    // TODO


}
