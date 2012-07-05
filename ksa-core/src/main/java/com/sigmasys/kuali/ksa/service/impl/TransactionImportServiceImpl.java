package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.BatchReceipt;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.TransactionImportService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.transform.*;
import com.sigmasys.kuali.ksa.util.XMLGregorianCalendarConversionUtil;
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

    private static final String IMPORT_SCHEMA_LOCATION = "classpath*:ConsolidatedSchemata.xsd";
    private static final String XML_SCHEMA_LOCATION = "classpath*:xml.xsd";

    private final static XmlSchemaValidator schemaValidator =
            new XmlSchemaValidator(XML_SCHEMA_LOCATION, IMPORT_SCHEMA_LOCATION);

    private Long retIdValue = 0L;
    private XMLGregorianCalendar xmlGCLedgerDate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


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
    public String importTransactions(String xml) {
        try {
            // validate against schema
            if (schemaValidator.validateXml(xml)) {
                // TODO: Single/batch processing logic
                return importTransactions(new StringReader(xml));
            } else {
                String errMsg = "XML content is invalid:\n" + xml;
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Convert an XML input transaction representation to a Transaction with persistence
     *
     * @param reader StringReader
     * @return XML content as String
     */
    private String importTransactions(StringReader reader) {

        String batchStatus = "incomplete";
        String uuidBatchIdentifier = null;
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal acceptedValue = BigDecimal.ZERO;
        BigDecimal failedValue = BigDecimal.ZERO;
        int batchSize = 0;
        int numberOfAccepted = 0;
        int numberOfFailed = 0;

        ObjectFactory objectFactory = new ObjectFactory();

        KsaBatchTransactionResponse.Accepted accepted = objectFactory.createKsaBatchTransactionResponseAccepted();
        KsaBatchTransactionResponse.Failed failed = objectFactory.createKsaBatchTransactionResponseFailed();
        KsaBatchTransactionResponse.BatchSummary batchSummary =
                objectFactory.createKsaBatchTransactionResponseBatchSummary();

        KsaBatchTransactionResponse ksaBatchTransactionResponse = objectFactory.createKsaBatchTransactionResponse();
        ksaBatchTransactionResponse.setBatchSummary(batchSummary);
        ksaBatchTransactionResponse.setAccepted(accepted);
        ksaBatchTransactionResponse.setFailed(failed);

        if (reader != null) {
            try {

                JAXBContext jaxbContext = JAXBContext.newInstance(KsaBatchTransaction.class);
                Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

                boolean failureNoted = false;

                if (unMarshaller != null) {

                    // multiple unmarshaled transaction objects
                    KsaBatchTransaction ksaBatchTransaction = (KsaBatchTransaction) unMarshaller.unmarshal(reader);

                    // the unmarshaled list
                    List<KsaTransaction> ksaTransactionList = ksaBatchTransaction.getKsaTransaction();

                    if (ksaTransactionList != null) {

                        uuidBatchIdentifier = ksaBatchTransaction.getBatchIdentifier();
                        batchSize = ksaTransactionList.size();

                        int transIndx = 0;

                        for (KsaTransaction trans : ksaTransactionList) {
                            boolean meetsRequiredInputs = verifyRequiredValues(trans);
                            if (meetsRequiredInputs) {

                                // Perform TransactionService insert to persist object
                                // use the return value to further distinguish success or failure
                                // the return value would be the transaction id
                                // Use the amount to update the totalValue
                                // Do we really know the actual transaction without from the TransactionService

                                totalValue = totalValue.add(trans.getAmount());

                                // set the transaction id from the return value, otherwise an empty value or -1
                                // add the accepted or failed transactions to the appropriate response list
                                // add one for each accepted or failed transactions to the appropriate number scalar

                                Date effectiveDate = XMLGregorianCalendarConversionUtil.asDate(trans.getEffectiveDate());

                                persistTransaction(trans.getTransactionType(),
                                        trans.getAccount(), effectiveDate, trans.getAmount());

                                if (retIdValue > 0) {
                                    // add the KsaTransaction object to the accepted's KsaTransactionAndTransactionDetails
                                    accepted.getKsaTransactionAndTransactionDetails().add(trans);

                                    numberOfAccepted++;
                                    acceptedValue = acceptedValue.add(trans.getAmount());

                                    // add the ID and LedgerDate to the TransactionDetails and then to the KsaTransactionAndTransactionDetails
                                    KsaBatchTransactionResponse.Accepted.TransactionDetails transactionDetails =
                                            new KsaBatchTransactionResponse.Accepted.TransactionDetails();

                                    // The return value from TransactionService persistTransaction returns the ID
                                    transactionDetails.setTransactionId(retIdValue);
                                    transactionDetails.setAcceptedDate(xmlGCLedgerDate);
                                    accepted.getKsaTransactionAndTransactionDetails().add(transactionDetails);

                                } else {
                                    failureNoted = true;
                                    // add the KsaTransactionAndReason object to the failed's KsaTransactionAndReason
                                    failed.getKsaTransactionAndReason().add(trans);

                                    numberOfFailed++;
                                    failedValue = failedValue.add(trans.getAmount());

                                    // add the reason to the KsaTransactionAndReason
                                    failed.getKsaTransactionAndReason().add(retIdValue);
                                }

                                // having updated the transaction ID set the list trans
                                ksaTransactionList.set(transIndx, trans);
                                transIndx++;
                            } else {
                                failureNoted = true;
                            }
                        }
                    }

                    if (!failureNoted) {
                        batchStatus = "complete";
                    }

                } else {
                    logger.error("Conversion of XML input failed");
                }

                ksaBatchTransactionResponse.setBatchStatus(batchStatus);

            } catch (JAXBException jxbExp) {
                ksaBatchTransactionResponse.setBatchStatus("incomplete");
                jxbExp.printStackTrace();
                // log the exception
                logger.error(jxbExp.getLocalizedMessage());
            }
        }

        // fill in the response
        ksaBatchTransactionResponse.setResponseIdentifier(UUID.randomUUID().toString());
        ksaBatchTransactionResponse.setResponseToBatchIdentifier(uuidBatchIdentifier);

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
        } catch (JAXBException jxbExp) {
            writer.write(jxbExp.getLocalizedMessage());
            jxbExp.printStackTrace();
            logger.error(jxbExp.getLocalizedMessage());
        }

        // return the XML marshaled result or exception
        return writer.toString();
    }

    /**
     * Validate inputs are provided. Validate amount is positive number?
     *
     * @param ksaTransaction KsaTransaction instance
     * @return trye/false
     */
    private boolean verifyRequiredValues(KsaTransaction ksaTransaction) {
        return (ksaTransaction.getAccount() != null &&
                ksaTransaction.getIncomingIdentifier() != null &&
                ksaTransaction.getEffectiveDate() != null &&
                ksaTransaction.getAmount() != null &&
                ksaTransaction.getCurrency() != null &&
                ksaTransaction.getTransactionType() != null);
    }

    /**
     * Check if an account exists, otherwise an error.
     * Create a persisted database transaction, otherwise an error
     * Failure is noted as an incomplete batch or single transaction processing
     *
     * @param transactionTypeId
     * @param userId
     * @param effectiveDate
     * @param amount
     */
    private void persistTransaction(String transactionTypeId, String userId,
                                    Date effectiveDate, BigDecimal amount) {
        retIdValue = 0L;
        xmlGCLedgerDate = null;

        if (accountService != null && transactionService != null) {
            boolean accountExists = accountService.doesKsaAccountExist(userId); //userId or user1 or admin

            if (accountExists) {
                Transaction transaction = null;
                try {
                    transaction =
                            transactionService.createTransaction(transactionTypeId, userId, effectiveDate, amount);
                } catch (Exception exp) {
                    logger.error(exp.getMessage(), exp);
                } finally {
                    if (transaction != null) {
                        retIdValue = transaction.getId();
                        xmlGCLedgerDate = XMLGregorianCalendarConversionUtil.asXMLGregorianCalendar(transaction.getLedgerDate());
                    } else {
                        // transaction create failed
                        // log the reason
                        logger.error("The transaction failed to create trans type " + transactionTypeId +
                                " for account " + userId);
                    }
                }

            } else {
                // no account - the return ID value will signal a failure
                // log the reason
                logger.error("The account does not exist " + userId);
            }

        }
    }

}
