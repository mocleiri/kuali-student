package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * General Ledger service declares business operations on GL Transactions and related objects
 * <p/>
 *
 * @author Michael Ivanov
 */
@Url(GeneralLedgerService.SERVICE_URL)
@WebService(serviceName = GeneralLedgerService.SERVICE_NAME, portName = GeneralLedgerService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface GeneralLedgerService {

    String SERVICE_URL = "generalLedger.webservice";
    String SERVICE_NAME = "GeneralLedgerService";
    String PORT_NAME = SERVICE_NAME + "Port";


    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param operationType GL operation type
     * @param isQueued      Set status to Q unless isQueued is passed and is false, in which case, set status to W
     * @return new GL Transaction instance
     */
    @WebMethod(exclude = true)
    GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount, GlOperationType operationType,
                                      boolean isQueued);

    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param operationType GL operation type
     * @return new GL Transaction instance
     */
    @WebMethod(exclude = true)
    GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount, GlOperationType operationType);


    /**
     * Summarizes the general ledger transactions for the given GL transaction list
     *
     * @param transactions List of general ledger transactions
     * @return the modified list of GL transactions
     */
    @WebMethod(exclude = true)
    List<GlTransaction> summarizeGlTransactions(List<GlTransaction> transactions);

    /**
     * Returns the general ledger type instance for the given code.
     *
     * @param glTypeCode General Ledger type code
     * @return GeneralLedgerType instance
     */
    GeneralLedgerType getGeneralLedgerType(String glTypeCode);

    /**
     * Returns the default general ledger mode.
     *
     * @return GeneralLedgerMode instance
     */
    GeneralLedgerMode getDefaultGeneralLedgerMode();

    /**
     * Returns the default general ledger type instance for the given code.
     *
     * @return GeneralLedgerType instance
     */
    GeneralLedgerType getDefaultGeneralLedgerType();

    /**
     * Prepares a transmission to the general ledger for the given range of effective dates.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     *
     * @param fromDate Start effective date
     * @param toDate   End effective date
     */
    @WebMethod(exclude = true)
    void prepareGlTransmissionForEffectiveDates(Date fromDate, Date toDate);

    /**
     * Prepares a transmission to the general ledger for the given range of recognition dates.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     *
     * @param fromDate Start recognition date
     * @param toDate   End recognition date
     */
    @WebMethod(exclude = true)
    void prepareGlTransmissionForRecognitionDates(Date fromDate, Date toDate);

    /**
     * Prepares a transmission to the general ledger for all GL transactions in status Q.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     */
    @WebMethod(exclude = true)
    void prepareGlTransmission();

}
