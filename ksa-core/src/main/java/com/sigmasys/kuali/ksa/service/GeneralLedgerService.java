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
     * @param description   Transaction description
     * @param isQueued      Set status to Q unless isQueued is passed and is false, in which case, set status to W
     * @return new GL Transaction instance
     */
    @WebMethod(exclude = true)
    GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount, String description,
                                      boolean isQueued);

    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param description   Transaction description
     * @return new GL Transaction instance
     */
    @WebMethod(exclude = true)
    GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount, String description);


    /**
     * Summarizes the general ledger transactions for the given GL transaction list
     *
     * @param glTransactions List of general ledger transactions
     */
    @WebMethod(exclude = true)
    void summarizeGlTransactions(List<GlTransaction> glTransactions);

    /**
     * Returns the general ledger type instance for the given code.
     *
     * @param glTypeCode General Ledger type code
     * @return GeneralLedgerType instance
     */
    GeneralLedgerType getGeneralLedgerType(String glTypeCode);

    /**
     * Returns the default general ledger type instance for the given code.
     *
     * @return GeneralLedgerType instance
     */
    GeneralLedgerType getDefaultGeneralLedgerType();

    /**
     * Gets all queued or in session general ledger transactions within the date range specified and
     * adds the recognition period to the transmission
     *
     * @param recognitionPeriod Recognition period
     * @param fromDate          Start date
     * @param toDate            End date
     * @return true if one or more records have been updated, false - otherwise
     */
    boolean setRecognitionPeriod(String recognitionPeriod, Date fromDate, Date toDate);

}
