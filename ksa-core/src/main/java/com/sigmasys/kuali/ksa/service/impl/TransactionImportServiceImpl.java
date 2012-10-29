package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.transform.*;

import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import javax.persistence.Query;
import javax.xml.bind.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by: dmulderink on 6/29/12 at 11:07 AM
 */
@Service("transactionImportService")
@Transactional(readOnly = true)
@WebService(serviceName = TransactionImportService.SERVICE_NAME, portName = TransactionImportService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class TransactionImportServiceImpl extends GenericPersistenceService implements TransactionImportService {

    private static final Log logger = LogFactory.getLog(TransactionImportServiceImpl.class);

    private static enum Status {
        COMPLETE,
        INCOMPLETE
    }

    private static final String IMPORT_SCHEMA_LOCATION = "classpath*:/xsd/transaction-import.xsd";
    private static final String XML_SCHEMA_LOCATION = "classpath*:/xsd/xml.xsd";

    private final static XmlSchemaValidator schemaValidator =
            new XmlSchemaValidator(XML_SCHEMA_LOCATION, IMPORT_SCHEMA_LOCATION);


    @Autowired
    private AccountService accountService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GeneralLedgerService glService;


    /**
     * Persist the given batch receipt in the database
     *
     * @param batchReceipt BatchReceipt instance
     * @return BatchReceipt ID
     */
    protected Long persistBatchReceipt(BatchReceipt batchReceipt) {
        return persistEntity(batchReceipt);
    }

    /**
     * Returns BatchReceipt by the given ID
     *
     * @param id batchReceipt ID
     * @return BatchReceipt instance
     */
    protected BatchReceipt getBatchReceipt(Long id) {
        Query query = em.createQuery("select br from BatchReceipt br " +
                " left outer join fetch br.account a " +
                " where br.id = :id ");
        query.setParameter("id", id);
        List<BatchReceipt> batchReceipts = query.getResultList();
        return (batchReceipts != null && !batchReceipts.isEmpty()) ? batchReceipts.get(0) : null;
    }

    /**
     * This is the service method exposed to up load and process transactions
     *
     * @param xml XML content
     * @return XML response
     */
    @Override
    @Transactional(readOnly = false)
    public String processTransactions(String xml) {
        // validate against schema
        if (schemaValidator.validateXml(xml)) {
            return parseTransactions(xml);
        } else {
            String errMsg = "XML content is invalid:\n" + xml;
            logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Convert an XML input transaction representation to a Transaction and persist
     * the transaction. Separate filed reject KsaTransactions from valid acceptable
     * KsaTransactions for further processing
     *
     * @param xml String
     * @return XML content as String
     */
    private String parseTransactions(String xml) {

        Status batchStatus = Status.INCOMPLETE;
        String batchIdentifier = null;
        int batchSize = 0;

        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal acceptedValue = BigDecimal.ZERO;
        BigDecimal failedValue = BigDecimal.ZERO;
        BigDecimal totalValueCredit = BigDecimal.ZERO;
        BigDecimal totalValueDebits = BigDecimal.ZERO;
        int numberOfAccepted = 0;
        int numberOfFailed = 0;
        XMLGregorianCalendar creationDate;
        com.sigmasys.kuali.ksa.model.Account firstAccount = null;

        ObjectFactory objectFactory = new ObjectFactory();
        KsaBatchTransactionResponse.Accepted accepted = objectFactory.createKsaBatchTransactionResponseAccepted();
        KsaBatchTransactionResponse.Failed failed = objectFactory.createKsaBatchTransactionResponseFailed();
        KsaBatchTransactionResponse.BatchSummary batchSummary = objectFactory.createKsaBatchTransactionResponseBatchSummary();
        KsaBatchTransactionResponse ksaBatchTransactionResponse = objectFactory.createKsaBatchTransactionResponse();
        List<KsaTransaction> acceptedKsaTransactionList = new ArrayList<KsaTransaction>();

        ksaBatchTransactionResponse.setBatchSummary(batchSummary);
        ksaBatchTransactionResponse.setAccepted(accepted);
        ksaBatchTransactionResponse.setFailed(failed);

        String failureValue = configService.getInitialParameter(Constants.IMPORT_SINGLE_BATCH_FAILURE_PARAM_NAME);
        Boolean singleBatchFailure = Boolean.valueOf(failureValue);

        // determine if batch or single transaction
        Object object = parseXml(new StringReader(xml));

        KsaBatchTransaction ksaBatchTransaction;
        if (object instanceof KsaTransaction) {
            ksaBatchTransaction = new KsaBatchTransaction();
            ksaBatchTransaction.getKsaTransaction().add((KsaTransaction) object);
        } else {
            ksaBatchTransaction = (KsaBatchTransaction) object;
        }


        boolean failureNoted = false;

        // the unmarshaled list
        List<KsaTransaction> ksaTransactions = ksaBatchTransaction.getKsaTransaction();

        if (ksaTransactions != null) {

            // Get the external batch ID and generate one if it is null
            batchIdentifier = ksaBatchTransaction.getBatchIdentifier();
            if (batchIdentifier == null) {
                batchIdentifier = UUID.randomUUID().toString();
            } else {
                // If the batch ID already exists add a failure reason and set the batch ID to null
                if (batchIdExists(batchIdentifier)) {
                    failed.getKsaTransactionAndReason().add(ksaBatchTransaction);
                    failed.getKsaTransactionAndReason().add("Batch ID = '" + batchIdentifier + "' already exists");
                    batchIdentifier = null;
                }
            }

            ksaBatchTransactionResponse.setResponseToBatchIdentifier(batchIdentifier);
            batchSize = ksaTransactions.size();

            String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());

            for (KsaTransaction ksaTransaction : ksaTransactions) {

                // pre determine, qualify the transaction create accepted and rejected lists of transactions
                Date effectiveDate = CalendarUtils.toDate(ksaTransaction.getEffectiveDate());
                String errMsg = "";
                if (!verifyRequiredValues(ksaTransaction)) {
                    errMsg = "Required values are missing from transaction";
                } else if (!accountService.accountExists(ksaTransaction.getAccountIdentifier())) {
                    errMsg = "Account '" + ksaTransaction.getAccountIdentifier() + "' does not exist";
                } else if (!transactionService.isTransactionAllowed(currentUserId,
                        ksaTransaction.getTransactionType(), effectiveDate)) {
                    errMsg = "Transaction is not allowed. Transaction Type = '" +
                            ksaTransaction.getTransactionType() + "', Effective Date = '" + effectiveDate +
                            "', Account ID = '" + currentUserId + "'";
                } else if (!isWithinCreditLimit(ksaTransaction.getAccountIdentifier())) {
                    errMsg = "Account '" + ksaTransaction.getAccountIdentifier() + "' has insufficient credit";
                }
                if (!isTransactionTypeValid(ksaTransaction.getTransactionType(), effectiveDate)) {
                    errMsg = "No such Transaction Type = '" + ksaTransaction.getTransactionType() +
                            "' with Effective Date = '" + effectiveDate + "'";
                }
                if (ksaTransaction.getOverride() != null &&
                        !isRefundRuleValid(ksaTransaction.getOverride().getRefundRule())) {
                    // applies to Payments
                    errMsg = "Invalid refund rule '" + ksaTransaction.getOverride().getRefundRule() + "'";
                }

                if (errMsg.length() > 0) {
                    // place the transaction in the failed list
                    failed.getKsaTransactionAndReason().add(ksaTransaction);
                    failed.getKsaTransactionAndReason().add(errMsg);
                    numberOfFailed++;
                    failedValue = failedValue.add(ksaTransaction.getAmount());
                    totalValue = totalValue.add(ksaTransaction.getAmount());
                } else {
                    // place the transaction in the accepted list
                    acceptedKsaTransactionList.add(ksaTransaction);
                }

                // the KsaTransaction might not have an account which will be created later
                if (firstAccount == null) {
                    firstAccount = accountService.getOrCreateAccount(ksaTransaction.getAccountIdentifier());
                    ksaBatchTransactionResponse.setResponseIdentifier(UUID.randomUUID().toString());
                }
            }

            boolean batchIsQualified = failed.getKsaTransactionAndReason().isEmpty();

            // process the accepted list
            // no failed rejects and singleBatchFailure is false
            if ((batchIsQualified && !singleBatchFailure) || (!batchIsQualified && !singleBatchFailure)) {

                for (KsaTransaction ksaTransaction : acceptedKsaTransactionList) {


                    try {

                        Transaction transaction = persistTransaction(ksaTransaction);

                        totalValue = totalValue.add(ksaTransaction.getAmount());

                        // refund applies to credit type only
                        if (transaction instanceof Payment) {
                            totalValueCredit = totalValueCredit.add(transaction.getAmount());
                        } else {
                            totalValueDebits = totalValueDebits.add(transaction.getAmount());
                        }

                        creationDate = CalendarUtils.toXmlGregorianCalendar(transaction.getCreationDate());

                        // add the KsaTransaction object to the accepted KsaTransactionAndTransactionDetails
                        accepted.getKsaTransactionAndTransactionDetails().add(ksaTransaction);

                        numberOfAccepted++;
                        acceptedValue = acceptedValue.add(transaction.getAmount());

                        // add the ID and Creation Date to the TransactionDetails and then to the KsaTransactionAndTransactionDetails
                        KsaBatchTransactionResponse.Accepted.TransactionDetails transactionDetails =
                                new KsaBatchTransactionResponse.Accepted.TransactionDetails();

                        transactionDetails.setTransactionId(transaction.getId().toString());
                        transactionDetails.setAcceptedDate(creationDate);
                        accepted.getKsaTransactionAndTransactionDetails().add(transactionDetails);

                    } catch (Exception e) {

                        logger.error(e.getMessage(), e);

                        failureNoted = true;

                        // add the KsaTransactionAndReason object to the failed KsaTransactionAndReason
                        failed.getKsaTransactionAndReason().add(ksaTransaction);

                        numberOfFailed++;
                        failedValue = failedValue.add(ksaTransaction.getAmount());

                        // add the reason to the KsaTransactionAndReason
                        failed.getKsaTransactionAndReason().add("Unable to create or persist transaction id " +
                                ksaTransaction.getAccountIdentifier());
                    }
                }
            } else {
                for (KsaTransaction acceptedTrans : acceptedKsaTransactionList) {
                    accepted.getKsaTransactionAndTransactionDetails().add(acceptedTrans);
                }
            }
        }

        if (!failureNoted) {
            batchStatus = Status.COMPLETE;
        }

        ksaBatchTransactionResponse.setBatchStatus(batchStatus.toString());

        // fill in the response

        batchSummary.setTransactionsInBatch(batchSize);
        batchSummary.setValueOfBatch(totalValue);
        batchSummary.setTransactionsAccepted(numberOfAccepted);
        batchSummary.setTransactionsFailed(numberOfFailed);
        batchSummary.setValueAccepted(acceptedValue);
        batchSummary.setValueFailed(failedValue);

        // log the batch summary statics and values

        // Convert the batch response object to XML or return the exception
        StringWriter writer = new StringWriter();
        try {

            JAXBContext context = JAXBContext.newInstance(KsaBatchTransactionResponse.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(ksaBatchTransactionResponse, writer);

            // fill in the BatchReceipt and persist

            BatchReceipt batchReceipt = new BatchReceipt();
            batchReceipt.setAccount(firstAccount);
            batchReceipt.setBatchDate(new Date());
            batchReceipt.setCreditOfAcceptedTransactions(totalValueCredit);
            batchReceipt.setDebitOfAcceptedTransactions(totalValueDebits);
            batchReceipt.setExternalId(batchIdentifier);
            batchReceipt.setNumberOfAcceptedTransactions(numberOfAccepted);
            batchReceipt.setNumberOfRejectedTransactions(numberOfFailed);
            batchReceipt.setNumberOfTransactions(batchSize);
            batchReceipt.setReceiptDate(new Date());
            batchReceipt.setStatus((Status.COMPLETE.equals(batchStatus)) ?
                    BatchReceiptStatus.ACCEPTED : BatchReceiptStatus.FAILED);
            batchReceipt.setTotalVolume(totalValue);
            batchReceipt.setVolumeOfRejectedTransactions(failedValue);

            String responseXml = writer.toString();

            batchReceipt.setIncomingXml(createXmlDocument(xml));

            batchReceipt.setOutgoingXml(createXmlDocument(responseXml));

            persistBatchReceipt(batchReceipt);

            return responseXml;


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private XmlDocument createXmlDocument(String xml) {
        XmlDocument xmlDocument = new XmlDocument();
        xmlDocument.setXml(xml);
        xmlDocument.setCreationDate(new Date());
        xmlDocument.setCreatorId(userSessionManager.getUserId(RequestUtils.getThreadRequest()));
        persistEntity(xmlDocument);
        return xmlDocument;
    }

    /**
     * Attempt to unmarshal a batch input XML file
     * return true if a batch XML input
     *
     * @param reader StringReader
     */
    private Object parseXml(StringReader reader) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KsaTransaction.class, KsaBatchTransaction.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    /**
     * Validate inputs are provided.
     *
     * @param ksaTransaction KsaTransaction instance
     * @return true/false
     */
    private boolean verifyRequiredValues(KsaTransaction ksaTransaction) {
        return (ksaTransaction.getAccountIdentifier() != null &&
                ksaTransaction.getIncomingIdentifier() != null &&
                ksaTransaction.getEffectiveDate() != null &&
                ksaTransaction.getAmount() != null &&
                ksaTransaction.getCurrency() != null &&
                ksaTransaction.getTransactionType() != null);
    }

    /**
     * Checks if the given batch ID already exists in the database
     *
     * @param batchId Batch ID
     * @return true if the given batch ID already exists, false - otherwise
     */
    private boolean batchIdExists(String batchId) {
        Query query = em.createQuery("select br.externalId from BatchReceipt br where br.externalId = :batchId");
        query.setParameter("batchId", batchId);
        query.setMaxResults(1);
        List<String> results = query.getResultList();
        return (results != null && !results.isEmpty());
    }


    /**
     * Determine the credit limit of the account. do not exceed
     *
     * @param accountId Account ID to be checked
     * @return true if the given account is within credit limit, false - otherwise
     */
    private boolean isWithinCreditLimit(String accountId) {
        boolean exitState;

        // TODO need to define a method that checks account is not over credit limit
        //exitState = transactionService.isWithinCreditLimit(accountId);
        // TODO remove when isWithinCreditLimit is defined
        exitState = true;
        return exitState;
    }

    /**
     * Check if the given TransactionType ID is defined
     *
     * @param transactionType Transaction Type
     * @param effectiveDate   Effective Date
     * @return boolean value
     */
    private boolean isTransactionTypeValid(String transactionType, Date effectiveDate) {
        return transactionService.getTransactionType(transactionType, effectiveDate) != null;
    }

    /**
     * Payments may have a refund rule applied. Check for A and S types
     *
     * @param refundRule Refund rule
     * @return boolean value
     */
    private boolean isRefundRuleValid(String refundRule) {

        if (refundRule == null) {
            return true;
        }

        // example A(100)(pheald) or S(45)
        // transactionService.isRefundRule(refundRule);
        // applies to credit types
        // drill down on the refund rule provided
        if (refundRule.startsWith("A") || refundRule.startsWith("S")) {
            // does the rule have a number 0 - 65535 after/in parenthesis
            int openRuleIndex = refundRule.indexOf("(");
            int closeRuleIndex = refundRule.indexOf(")");
            if (openRuleIndex > 0 && closeRuleIndex > 0 && openRuleIndex < closeRuleIndex) {
                try {
                    int value = Integer.valueOf(refundRule.substring(openRuleIndex + 1, closeRuleIndex - 1));
                    if (value < 0 || value > Short.MAX_VALUE * 2) {
                        return false;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return false;
                }
                if (refundRule.startsWith("S")) {
                    return true;
                } else {
                    // There should be a second parameter in parenthesis for "A" which appears to be the accountId
                    // which must exist in KSA
                    int openAccountIndex = refundRule.lastIndexOf("(");
                    int closeAccountIndex = refundRule.lastIndexOf(")");
                    if (openAccountIndex > 0 && closeAccountIndex > 0 && openAccountIndex < closeAccountIndex) {
                        try {
                            String accountId = refundRule.substring(openAccountIndex + 1, closeAccountIndex - 1);
                            return accountService.getOrCreateAccount(accountId) != null;
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Persists the Transaction instance created from the incoming KsaTransaction
     *
     * @param ksaTransaction KsaTransaction instance
     * @return Transaction instance
     */
    private Transaction persistTransaction(KsaTransaction ksaTransaction) {

        Date effectiveDate = CalendarUtils.toDate(ksaTransaction.getEffectiveDate());

        Transaction transaction = transactionService.createTransaction(ksaTransaction.getTransactionType(),
                ksaTransaction.getAccountIdentifier(), effectiveDate, ksaTransaction.getAmount());

        transaction.setNativeAmount(ksaTransaction.getNativeAmount());
        transaction.setOriginationDate(CalendarUtils.toDate(ksaTransaction.getOriginationDate()));
        transaction.setCurrency(currencyService.getCurrency(ksaTransaction.getCurrency()));
        transaction.setExternalId(ksaTransaction.getIncomingIdentifier());

        KsaTransaction.Override override = ksaTransaction.getOverride();

        // Setting general ledger type
        String glTypeCode = (override != null && override.getGeneralLedgerType() != null) ?
                override.getGeneralLedgerType() :
                configService.getInitialParameter(Constants.DEFAULT_GL_TYPE_PARAM_NAME);

        if (glTypeCode != null) {
            transaction.setGeneralLedgerType(glService.getGeneralLedgerType(glTypeCode));
        }

        if (transaction instanceof Payment) {

            TransactionType transactionType =
                    transactionService.getTransactionType(ksaTransaction.getTransactionType(), effectiveDate);

            if (transactionType == null) {
                String errMsg = "Cannot find Transaction Type for ID = " + ksaTransaction.getTransactionType() +
                        " and Effective Date = " + effectiveDate;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            CreditType creditType = (CreditType) transactionType;
            Payment payment = (Payment) transaction;

            // Setting payment clear date
            Integer clearPeriod = null;
            if (override != null) {
                if (override.getOverrideClearPeriod() != null) {
                    clearPeriod = override.getOverrideClearPeriod();
                }
                payment.setRefundable(override.isRefundable());
                String refundRule = override.getOverrideRefundRule();
                if (StringUtils.isBlank(refundRule)) {
                    refundRule = override.getRefundRule();
                }
                payment.setRefundRule(refundRule);
            }

            if (clearPeriod == null) {
                clearPeriod = (creditType.getClearPeriod() != null) ? creditType.getClearPeriod() : 0;
            }

            payment.setClearDate(calendarService.addCalendarDays(effectiveDate, clearPeriod));

        }

        // TODO What to do persist where and what document and override plus other types
        // Save additional information concerning the transaction above and beyond the basic transaction created
        transactionService.persistTransaction(transaction);
        return transaction;
    }
}
