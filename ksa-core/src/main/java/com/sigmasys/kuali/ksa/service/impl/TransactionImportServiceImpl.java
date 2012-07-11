package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.CalendarService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.TransactionImportService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.transform.*;

import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
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
    private ConfigService configService;

    /**
     * Persist the given batch receipt in the database
     *
     * @param batchReceipt BatchReceipt instance
     * @return BatchReceipt ID
     */
    @Override
    @WebMethod(exclude = true)
    public Long persistBatchReceipt(BatchReceipt batchReceipt) {
        return persistEntity(batchReceipt);
    }

    /**
     * Returns BatchReceipt by the given ID
     *
     * @param id batchReceipt ID
     * @return BatchReceipt instance
     */
    @Override
    @WebMethod(exclude = true)
    public BatchReceipt getBatchReceipt(Long id) {
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

        String batchStatus = "incomplete";
        String uuidBatchResponseIdentifier;
        String uuidBatchIdentifier;
        int batchSize = 0;

        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal acceptedValue = BigDecimal.ZERO;
        BigDecimal failedValue = BigDecimal.ZERO;
        BigDecimal totalValueCredit = BigDecimal.ZERO;
        BigDecimal totalValueDebits = BigDecimal.ZERO;
        int numberOfAccepted = 0;
        int numberOfFailed = 0;
        XMLGregorianCalendar xmlGCLedgerDate = null;
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

        Boolean singleBatchFailure = Boolean.valueOf(configService.getInitialParameter(Constants.IMPORT_SINGLE_BATCH_FAILURE));

        if (xml != null) {

            // determine if batch or single transaction
            StringReader batchReader = new StringReader(xml);
            KsaBatchTransaction ksaBatchTransaction = extractBatchXml(batchReader);

            if (ksaBatchTransaction == null) {
                StringReader singleReader = new StringReader(xml);
                ksaBatchTransaction = extractSingleXml(singleReader);
            }

            boolean batchIsQualified = false;
            boolean failureNoted = false;

            // the unmarshaled list
            List<KsaTransaction> ksaTransactionList = ksaBatchTransaction.getKsaTransaction();

            if (ksaTransactionList != null) {

                // A true batch stream will have a UUID. Otherwise a single will not have a value or be null
                // set the incoming batch id as a response to batch identifier
                uuidBatchIdentifier = ksaBatchTransaction.getBatchIdentifier();
                ksaBatchTransactionResponse.setResponseToBatchIdentifier(uuidBatchIdentifier);
                batchSize = ksaTransactionList.size();

                if (!checkBatchIDUsed(uuidBatchIdentifier)) {

                    for (KsaTransaction trans : ksaTransactionList) {

                        // pre determine, qualify the transaction create accepted and rejected lists of transactions
                        Date effectiveDate = CalendarUtils.asDate(trans.getEffectiveDate());
                        String errMsg = "";
                        if (!verifyRequiredValues(trans)) {
                            errMsg = "Required values are missing from transaction ";
                        } else if (!doesImportAccountExist(trans.getAccountIdentifier())) {
                            errMsg = "Account does not exist";
                        } else if (!isTransactionAllowed(trans.getAccountIdentifier(), trans.getTransactionType(), effectiveDate)) {
                            errMsg = "Transaction not allowed";
                        } else if (!isWithinCreditLimit(trans.getAccountIdentifier())) {
                            errMsg = "Account has insufficient credit";
                        }
                        if (!isTransactionCodeValid(trans.getTransactionType(), effectiveDate)) {
                            errMsg = "No such transaction code";
                        }
                        if (!isRefundRuleValid(trans.getRefundRule())) {
                            // applies to Payments
                            errMsg = "Invalid refund rule";
                        }

                        if (errMsg.length() > 0) {
                            // place the transaction in the failed list
                            failed.getKsaTransactionAndReason().add(trans);
                            failed.getKsaTransactionAndReason().add(errMsg);
                            numberOfFailed++;
                            failedValue = failedValue.add(trans.getAmount());
                            totalValue = totalValue.add(trans.getAmount());
                        } else {
                            // place the transaction in the accepted list
                            acceptedKsaTransactionList.add(trans);
                        }

                        // the KsaTransaction might not have an account which will be created later
                        if (firstAccount == null) {
                            com.sigmasys.kuali.ksa.model.Account fullAccount =
                                    accountService.getFullAccount(trans.getAccountIdentifier());

                            if (fullAccount != null) {
                                firstAccount = fullAccount;
                                uuidBatchResponseIdentifier = UUID.randomUUID().toString();
                                ksaBatchTransactionResponse.setResponseIdentifier(uuidBatchResponseIdentifier);
                            }
                        }
                    }

                    batchIsQualified = failed.getKsaTransactionAndReason().isEmpty();

                    // process the accepted list
                    // no failed rejects and singleBatchFailure is false
                    if ((batchIsQualified && !singleBatchFailure) ||
                            (!batchIsQualified && !singleBatchFailure)) {
                        for (KsaTransaction tmpTransaction : acceptedKsaTransactionList) {
                            // Perform TransactionService insert to persist object
                            // use the return value to further distinguish success or failure
                            // the return value would be a Transaction
                            // Use the amount to update the totalValue

                            totalValue = totalValue.add(tmpTransaction.getAmount());

                            // returns a KsaTransaction oon success, otherwise null on failure
                            // add the accepted or failed transactions to the appropriate response list
                            // add one for each accepted or failed transactions to the appropriate number scalar

                            Transaction transaction = createTransaction(tmpTransaction);

                            if (transaction != null) {
                                // The return value from TransactionService persistTransaction returns
                                // the Transaction which is morphed into a KsaTransaction

                                KsaTransaction persistedKsaTransaction = persistTransaction(transaction, tmpTransaction);
                                // refund applies to credit type only
                                if (transaction instanceof Payment) {
                                    totalValueCredit = totalValueCredit.add(transaction.getAmount());
                                    Payment payment = (Payment) transaction;
                                    persistedKsaTransaction.setRefundRule(payment.getRefundRule());
                                    persistedKsaTransaction.setIsRefundable(payment.isRefundable());
                                } else {
                                    totalValueDebits = totalValueDebits.add(transaction.getAmount());
                                }

                                xmlGCLedgerDate = CalendarUtils.asXmlGregorianCalendar(transaction.getLedgerDate());

                                // add the KsaTransaction object to the accepted KsaTransactionAndTransactionDetails
                                accepted.getKsaTransactionAndTransactionDetails().add(persistedKsaTransaction);

                                numberOfAccepted++;
                                acceptedValue = acceptedValue.add(persistedKsaTransaction.getAmount());

                                // add the ID and LedgerDate to the TransactionDetails and then to the KsaTransactionAndTransactionDetails
                                KsaBatchTransactionResponse.Accepted.TransactionDetails transactionDetails =
                                        new KsaBatchTransactionResponse.Accepted.TransactionDetails();

                                transactionDetails.setTransactionId(transaction.getId().toString());
                                transactionDetails.setAcceptedDate(xmlGCLedgerDate);
                                accepted.getKsaTransactionAndTransactionDetails().add(transactionDetails);

                            } else {
                                failureNoted = true;
                                // add the KsaTransactionAndReason object to the failed's KsaTransactionAndReason
                                failed.getKsaTransactionAndReason().add(tmpTransaction);

                                numberOfFailed++;
                                failedValue = failedValue.add(tmpTransaction.getAmount());

                                // add the reason to the KsaTransactionAndReason
                                failed.getKsaTransactionAndReason().add("Unable to create or persist transaction id " +
                                        tmpTransaction.getAccountIdentifier());
                            }
                        }
                    } else {
                        for (KsaTransaction acceptedTrans : acceptedKsaTransactionList) {
                            accepted.getKsaTransactionAndTransactionDetails().add(acceptedTrans);
                        }
                    }
                }
            }

            if (!failureNoted) {
                batchStatus = "complete";
            }

            ksaBatchTransactionResponse.setBatchStatus(batchStatus);

        }

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
            batchReceipt.setExternalId(ksaBatchTransactionResponse.getResponseIdentifier());
            batchReceipt.setNumberOfAcceptedTransactions(numberOfAccepted);
            batchReceipt.setNumberOfRejectedTransactions(numberOfFailed);
            batchReceipt.setNumberOfTransactions(batchSize);
            batchReceipt.setReceiptDate(new Date());
            if (batchStatus.compareTo("complete") == 0) {
                batchReceipt.setStatus(BatchReceiptStatus.ACCEPTED);
            } else {
                batchReceipt.setStatus(BatchReceiptStatus.FAILED);
            }
            batchReceipt.setTotalVolume(totalValue);
            batchReceipt.setVolumeOfRejectedTransactions(failedValue);
            XmlDocument xmlInDocument = new XmlDocument();
            xmlInDocument.setXml(xml);
            batchReceipt.setIncomingXml(xmlInDocument);
            XmlDocument xmlOutDocument = new XmlDocument();
            xmlOutDocument.setXml(writer.toString());
            batchReceipt.setOutgoingXml(xmlOutDocument);

            persistBatchReceipt(batchReceipt);


        } catch (JAXBException jxbExp) {
            writer.write(jxbExp.getLocalizedMessage());
            jxbExp.printStackTrace();
            logger.error(jxbExp.getLocalizedMessage());
        }
        // return the XML marshaled result or exception
        return writer.toString();
    }

    /**
     * Attempt to unmarshal a batch input XML file
     * return true if a batch XML input
     *
     * @param reader
     */
    private KsaBatchTransaction extractBatchXml(StringReader reader) {
        KsaBatchTransaction ksaBatchTransaction = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(KsaBatchTransaction.class);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

            if (unMarshaller != null) {
                // multiple unmarshaled transaction objects
                ksaBatchTransaction = (KsaBatchTransaction) unMarshaller.unmarshal(reader);
            }
        } catch (UnmarshalException ume) {
            ume.printStackTrace();
            logger.error(ume.getLocalizedMessage());
        } catch (JAXBException jxbExp) {
            jxbExp.printStackTrace();
            // log the exception
            logger.error(jxbExp.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return ksaBatchTransaction;
    }

    /**
     * Need to wrap a single transaction as a batch
     * Attempt to unmarshal a single input XML file
     *
     * @param reader
     * @return
     */
    private KsaBatchTransaction extractSingleXml(StringReader reader) {

        KsaBatchTransaction ksaBatchTransaction = null;

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(KsaTransaction.class);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

            if (unMarshaller != null) {

                // multiple unmarshaled transaction objects
                KsaTransaction ksaTransaction = (KsaTransaction) unMarshaller.unmarshal(reader);

                ksaBatchTransaction = new KsaBatchTransaction();
                ksaBatchTransaction.getKsaTransaction().add(ksaTransaction);
            }
        } catch (UnmarshalException ume) {
            ume.printStackTrace();
            logger.error(ume.getLocalizedMessage());
        } catch (JAXBException jxbExp) {
            jxbExp.printStackTrace();
            // log the exception
            logger.error(jxbExp.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return ksaBatchTransaction;
    }

    /**
     * check if the Batch response to indentifier is unique
     * Return false if the batch identifier has not been used, true otherwise
     *
     * @param uuidBatchIdentifier
     * @return
     */
    private boolean checkBatchIDUsed(String uuidBatchIdentifier) {
        boolean exitState = false;
        // TODO check batch identifier has not been previously used with BatchReceipt
        //exitState = BatchReceipt.checkUsage(uuidBatchIdentifier);
        return exitState;
    }

    /**
     * Determine if a KSA account exists and or create a KSA account if none found
     * using KIM details to supplement KSA account. Return true if the account exists
     * otherwise false if no account from getOrCreateAccount method combined with KIM
     *
     * @param accountId Account ID
     */
    private boolean doesImportAccountExist(String accountId) {
        return accountService.getOrCreateAccount(accountId) != null;
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
     * Determine if the account is blocking all transactions or certain types of transactions
     *
     * @param accountId
     * @param transactionType
     * @param effectiveDate
     * @return
     */
    private boolean isTransactionAllowed(String accountId, String transactionType, Date effectiveDate) {
        boolean exitState;

        // TODO getAccountBlockedStatus
        if (doesImportAccountExist(accountId) && isTransactionCodeValid(transactionType, effectiveDate)) {
            // this method invokes isTransactionAllowed which appears circular
            //exitState = accountService.getAccountBlockedStatus(accountId);
        }
        // TODO remove when getAccountBlockStatus or isTransactionAllowed is defined
        exitState = true;
        return exitState;
    }

    /**
     * Determine the credit limit of the account. do not exceed
     *
     * @param accountId
     * @return
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
     * Check if the given TransationType ID is defined
     *
     * @param transactionType
     * @param effectiveDate
     * @return
     */
    private boolean isTransactionCodeValid(String transactionType, Date effectiveDate) {

        boolean exitState = false;
        try {
            TransactionType transactionTypeResult = transactionService.getTransactionType(transactionType, effectiveDate);

            if (transactionTypeResult != null) {
                exitState = true;
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return exitState;
    }

    /**
     * Payments may have a refund rule applied. Check for A and S types
     *
     * @param refundRule
     * @return
     */
    private boolean isRefundRuleValid(String refundRule) {
        boolean exitState = false;

        // example A(100)(pheald) or S(45)
        // transactionService.isRefundRule(refundRule);

        // applies to credit types
        if (refundRule != null) {
            // drill down on the refund rule provided
            if (refundRule.startsWith("A") || refundRule.startsWith("S")) {
                // does the rule have a number 0 - 65535 after/in parenthesis
                int openRuleParenthesis = refundRule.indexOf("(");
                int closeRuleParenthesis = refundRule.indexOf(")");

                if (openRuleParenthesis > 0 && closeRuleParenthesis > 0) {
                    String ruleNumber = refundRule.substring(openRuleParenthesis + 1, closeRuleParenthesis - 1);
                    if (refundRule.startsWith("S")) {
                        exitState = true;
                    } else {
                        // there may be a second parameter in parenthesis which appears to be the accountId
                        // if getOrCreateAccount then exitState = true, otherwise false
                        int openAccountParenthesis = refundRule.lastIndexOf("(");
                        int closeAccountParenthesis = refundRule.lastIndexOf(")");
                        if (openAccountParenthesis > 0 && closeAccountParenthesis > 0) {
                            String accountId = refundRule.substring(openAccountParenthesis + 1, closeAccountParenthesis - 1);
                            com.sigmasys.kuali.ksa.model.Account account = accountService.getOrCreateAccount(accountId);
                            exitState = (account != null);
                        }
                    }
                }
            }
        } else {
            exitState = true;
        }

        return exitState;
    }

    /**
     * Create a basic transaction
     * return a Transaction if persisted, otherwise a null value
     *
     * @param ksaTransaction
     * @return
     */
    private Transaction createTransaction(KsaTransaction ksaTransaction) {
        Transaction transaction = null;
        try {
            Date effectiveDate = CalendarUtils.asDate(ksaTransaction.getEffectiveDate());
            // create a basic transaction
            transaction =
                    transactionService.createTransaction(ksaTransaction.getTransactionType(),
                            ksaTransaction.getAccountIdentifier(), effectiveDate, ksaTransaction.getAmount());
        } catch (Exception exp) {
            logger.error(exp.getLocalizedMessage());
            logger.error("The transaction failed to create trans type " + ksaTransaction.getTransactionType() +
                    " for account " + ksaTransaction.getAccountIdentifier());
        }

        return transaction;
    }

    /**
     * persist the extra Transaction field information from the incoming KsaTransaction
     * return a revised KsaTransaction
     *
     * @param transaction
     * @param ksaTransaction
     * @return
     */
    private KsaTransaction persistTransaction(Transaction transaction, KsaTransaction ksaTransaction) {
        KsaTransaction retKsaTrans = null;

        if (transaction != null) {
            Date effectiveDate = CalendarUtils.asDate(ksaTransaction.getEffectiveDate());
            Date originationDate = CalendarUtils.asDate(ksaTransaction.getOriginationDate());
            Currency currency = currencyService.getCurrency(ksaTransaction.getCurrency());
            transaction.setNativeAmount(ksaTransaction.getNativeAmount());
            transaction.setOriginationDate(originationDate);
            transaction.setCurrency(currency);
            transaction.setExternalId(ksaTransaction.getIncomingIdentifier());

            if (transaction instanceof Payment) {
                TransactionType transactionType = em.find(TransactionType.class, ksaTransaction.getTransactionType());
                CreditType creditType = (CreditType) transactionType;
                Payment payment = (Payment) transaction;
                int clearPeriod = (creditType.getClearPeriod() != null) ? creditType.getClearPeriod() : 0;
                payment.setClearDate(calendarService.addCalendarDays(effectiveDate, clearPeriod));
                payment.setRefundable(ksaTransaction.isIsRefundable());
                payment.setRefundRule(ksaTransaction.getRefundRule());
            }

            // TODO What to do persist where and what document and override plus other types
            // Save additional information concerning the transaction above and beyond the basic transction created
            if (transactionService.persistTransaction(transaction) > 0) {

                // persist incoming ksaTransaction document

                // persist incoming ksaTransaction override

                // setup the return KsaTransaction
                retKsaTrans = new KsaTransaction();
                retKsaTrans.setAccountIdentifier(ksaTransaction.getAccountIdentifier());
                retKsaTrans.setIncomingIdentifier(transaction.getExternalId());
                retKsaTrans.setEffectiveDate(CalendarUtils.asXmlGregorianCalendar(transaction.getEffectiveDate()));
                retKsaTrans.setOriginationDate(CalendarUtils.asXmlGregorianCalendar(transaction.getOriginationDate()));
                retKsaTrans.setAmount(transaction.getAmount());
                retKsaTrans.setNativeAmount(transaction.getNativeAmount());
                retKsaTrans.setCurrency(transaction.getCurrency().getIso());
                retKsaTrans.setTransactionType(transaction.getTransactionType().getId().getId());
                // document
                retKsaTrans.setDocument(ksaTransaction.getDocument());
                // override
                retKsaTrans.setOverride(ksaTransaction.getOverride());
            }
        }
        return retKsaTrans;
    }
}
