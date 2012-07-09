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

   private static final String IMPORT_SCHEMA_LOCATION = "classpath*:ConsolidatedSchemata.xsd";
   private static final String XML_SCHEMA_LOCATION = "classpath*:xml.xsd";

   private final static XmlSchemaValidator schemaValidator =
   new XmlSchemaValidator(XML_SCHEMA_LOCATION, IMPORT_SCHEMA_LOCATION);

   private XMLGregorianCalendar xmlGCLedgerDate;

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

   private Boolean singleBatchFailure = true;

   private Boolean isBatch = true;

   BigDecimal totalValue = BigDecimal.ZERO;
   BigDecimal acceptedValue = BigDecimal.ZERO;
   BigDecimal failedValue = BigDecimal.ZERO;
   BigDecimal totalValueCredit = BigDecimal.ZERO;
   BigDecimal totalValueDebits = BigDecimal.ZERO;

   int numberOfAccepted = 0;
   int numberOfFailed = 0;

   private KsaBatchTransaction ksaBatchTransaction = null;

   private ObjectFactory objectFactory;
   private KsaBatchTransactionResponse.Accepted accepted;
   private KsaBatchTransactionResponse.Failed failed;
   private KsaBatchTransactionResponse.BatchSummary batchSummary;
   private KsaBatchTransactionResponse ksaBatchTransactionResponse;

   private BatchReceipt batchReceipt;
   private List<KsaTransaction> ksaTransactionList;
   private com.sigmasys.kuali.ksa.model.Account firstAccount = null;

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
   @WebMethod
   public String processTransactions(String xml) {
      try {
         // validate against schema
         if (schemaValidator.validateXml(xml)) {
             return parseTransactions(xml);
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
   * @param xml String
   * @return XML content as String
   */
   private String parseTransactions(String xml) {

      String batchStatus = "incomplete";
      String uuidBatchResponseIdentifier = null;
      String uuidBatchIdentifier = null;
      int batchSize = 0;
      boolean firstTime = true;

      objectFactory = new ObjectFactory();
      accepted = objectFactory.createKsaBatchTransactionResponseAccepted();
      failed = objectFactory.createKsaBatchTransactionResponseFailed();
      batchSummary = objectFactory.createKsaBatchTransactionResponseBatchSummary();
      ksaBatchTransactionResponse = objectFactory.createKsaBatchTransactionResponse();
      ksaTransactionList = new ArrayList<KsaTransaction>();

      ksaBatchTransactionResponse.setBatchSummary(batchSummary);
      ksaBatchTransactionResponse.setAccepted(accepted);
      ksaBatchTransactionResponse.setFailed(failed);

      singleBatchFailure = Boolean.valueOf(configService.getInitialParameter(Constants.IMPORT_SINGLE_BATCH_FALURE));

      if (xml != null) {

         // determine if batch or single transaction
         StringReader batchReader = new StringReader(xml);
         isBatch = extractBatchXml(batchReader);

         if (!isBatch) {
            StringReader singleReader = new StringReader(xml);
            isBatch = extractSingleXml(singleReader);
         }

         try {
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
                     qualifyTransaction(trans);

                     // the KsaTransaction might not have an account which will be created later
                     if (firstAccount == null) {
                        uuidBatchResponseIdentifier = createBatchReceipt(trans.getAccountIdentifier());
                     }
                  }

                  batchIsQualified = failed.getKsaTransactionAndReason().size() == 0 ? true : false;

                  // process the accepted list
                  // no failed rejects and singleBatchFailure is false
                  if (batchIsQualified && !singleBatchFailure) {
                     failureNoted = processTransactions();
                  } else if (!batchIsQualified && !singleBatchFailure) {
                     failureNoted = processTransactions();
                  } else {
                     for (KsaTransaction trans : ksaTransactionList) {
                        accepted.getKsaTransactionAndTransactionDetails().add(trans);
                     }
                  }

               }
            }

            if (!failureNoted) {
               batchStatus = "complete";
            }

             ksaBatchTransactionResponse.setBatchStatus(batchStatus);
         } catch (Exception e) {
            // the batch status should be default incomplete
            logger.error(e.getLocalizedMessage(), e);
         }
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
      } catch (JAXBException jxbExp) {
         writer.write(jxbExp.getLocalizedMessage());
         jxbExp.printStackTrace();
         logger.error(jxbExp.getLocalizedMessage());
      } finally {
         // TODO see commented set methods
         // fill in the BatchReceipt and persist
/*
         batchReceipt = new BatchReceipt();
         batchReceipt.setAccount(firstAccount);
         batchReceipt.setBatchDate(new Date());
         batchReceipt.setCreditOfAcceptedTransactions(totalValueCredit);
         batchReceipt.setDebitOfAcceptedTransactions(totalValueDebits);
         batchReceipt.setExternalId(ksaBatchTransactionResponse.getResponseIdentifier());
         batchReceipt.setNumberOfAcceptedTransactions(numberOfAccepted);
         batchReceipt.setNumberOfRejectedTransactions(numberOfFailed);
         batchReceipt.setNumberOfTransactions(batchSize);
         batchReceipt.setReceiptDate(new Date());
         //batchReceipt.setStatus(new BatchReceiptStatus("E"));
         batchReceipt.setTotalVolume(totalValue);
         batchReceipt.setVolumeOfRejectedTransactions(failedValue);
         //batchReceipt.setIncomingXMLLocation(xml);
         //batchReceipt.setoutgoingXMLLocation(writer);
         persistBatchReceipt(batchReceipt);
*/
      }

     // return the XML marshaled result or exception
     return writer.toString();
   }

   /**
    * Use the accepted list to produce transactions
    * @return
    */
   private boolean processTransactions() {
      boolean failureNoted = false;

      for (KsaTransaction trans : ksaTransactionList) {
         // Perform TransactionService insert to persist object
         // use the return value to further distinguish success or failure
         // the return value would be the transaction id
         // Use the amount to update the totalValue
         // Do we really know the actual transaction without from the TransactionService

         totalValue = totalValue.add(trans.getAmount());

         // returns a KsaTransaction oon success, otherwise null on failure
         // add the accepted or failed transactions to the appropriate response list
         // add one for each accepted or failed transactions to the appropriate number scalar

         KsaTransaction ksaTransaction = persistTransaction(trans);

         if (ksaTransaction != null) {
            // add the KsaTransaction object to the accepted's KsaTransactionAndTransactionDetails
            accepted.getKsaTransactionAndTransactionDetails().add(ksaTransaction);

            numberOfAccepted++;
            acceptedValue = acceptedValue.add(trans.getAmount());

            // add the ID and LedgerDate to the TransactionDetails and then to the KsaTransactionAndTransactionDetails
            KsaBatchTransactionResponse.Accepted.TransactionDetails transactionDetails =
                  new KsaBatchTransactionResponse.Accepted.TransactionDetails();

            // The return value from TransactionService persistTransaction returns
            // the Transaction which is morphed into a KsaTransaction
            transactionDetails.setTransactionId(ksaTransaction.getAccountIdentifier());
            transactionDetails.setAcceptedDate(xmlGCLedgerDate);
            accepted.getKsaTransactionAndTransactionDetails().add(transactionDetails);

         } else {
            failureNoted = true;
            // add the KsaTransactionAndReason object to the failed's KsaTransactionAndReason
            failed.getKsaTransactionAndReason().add(trans);

            numberOfFailed++;
            failedValue = failedValue.add(trans.getAmount());

            // add the reason to the KsaTransactionAndReason
            failed.getKsaTransactionAndReason().add("Unable to create or persist transaction id " + trans.getAccountIdentifier());
         }
      }

      return failureNoted;
   }

   /**
    * Attempt to unmarshal a batch input XML file
    * return true if a batch XML input
    * @param reader
    */
   private boolean extractBatchXml(StringReader reader) {

      boolean exitState = false;

      try {

         JAXBContext jaxbContext = JAXBContext.newInstance(KsaBatchTransaction.class);
         Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

         if (unMarshaller != null) {

            // multiple unmarshaled transaction objects
            ksaBatchTransaction = (KsaBatchTransaction) unMarshaller.unmarshal(reader);

            exitState = true;
         }

      } catch (JAXBException jxbExp) {
         jxbExp.printStackTrace();
         // log the exception
         logger.error(jxbExp.getLocalizedMessage());
      } catch (Exception e) {
         logger.error(e.getLocalizedMessage(), e);
      }

      return exitState;
   }

   /**
    * Need to wrap a single transaction as a batch
    * Attempt to unmarshal a single input XML file
    * @param reader
    * @return
    */
   private boolean extractSingleXml(StringReader reader) {

      boolean exitState = false;

      try {

         JAXBContext jaxbContext = JAXBContext.newInstance(KsaTransaction.class);
         Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

         if (unMarshaller != null) {

            // multiple unmarshaled transaction objects
            KsaTransaction ksaTransaction = (KsaTransaction) unMarshaller.unmarshal(reader);

            ksaBatchTransaction = new KsaBatchTransaction();
            ksaBatchTransaction.getKsaTransaction().add(ksaTransaction);

            exitState = true;
         }

      } catch (JAXBException jxbExp) {
         jxbExp.printStackTrace();
         // log the exception
         logger.error(jxbExp.getLocalizedMessage());
      } catch (Exception e) {
         logger.error(e.getLocalizedMessage(), e);
      }

      return exitState;
   }

   /**
    * check if the Batch response to indentifier is unique
    * Return false if the batch identifier has not been used, true otherwise
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
    * Create a batch receipt UUID for this run
    * @param accountId
    * @return
    */
   private String createBatchReceipt(String accountId) {
      String uuidValue = null;
      // TODO Create a unique BatchReceipt UUID

      com.sigmasys.kuali.ksa.model.Account fullAccount = accountService.getFullAccount(accountId);

      if (fullAccount != null) {
         firstAccount = fullAccount;
         uuidValue = UUID.randomUUID().toString();
         ksaBatchTransactionResponse.setResponseIdentifier(uuidValue);
      }
      return uuidValue;
   }

   /**
    * Determine if a KSA account exists and or create a KSA account if none found
    * using KIM details to supplement KSA account. Return true if the account exists
    * otherwise false if no account from getOrCreateAccount method combined with KIM
    * @param accountId
    */
   private boolean doesImportAccountExist(String accountId) {

      com.sigmasys.kuali.ksa.model.Account account = null;
      if (accountId != null) {
         try {
         account = accountService.getOrCreateAccount(accountId);
         } catch (IllegalStateException iseExp) {
            logger.error(iseExp.getLocalizedMessage());
         }
      }
      return account != null ? true : false;
   }

   /**
   * Validate inputs are provided.
   *
   * @param ksaTransaction KsaTransaction instance
   * @return trye/false
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
    * @param accountId
    * @param transactionType
    * @param effectiveDate
    * @return
    */
   private boolean isTransactionAllowed(String accountId, String transactionType, Date effectiveDate) {
      boolean exitState = false;

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
    * @param accountId
    * @return
    */
   private boolean isWithinCreditLimit(String accountId) {
      boolean exitState = false;

      // TODO need to define a method that checks account is not over credit limit
      //exitState = transactionService.isWithinCreditLimit(accountId);
      // TODO remove when isWithinCreditLimit is defined
      exitState = true;
      return exitState;
   }

   /**
    * check i fthe given transationType ID is defined
    * @param transactionType
    * @param effectiveDate
    * @return
    */
   private boolean isTransactionCodeValid(String transactionType, Date effectiveDate) {

      boolean exitState = false;
      try {
         TransactionType transactionTypeResult =  transactionService.getTransactionType(transactionType, effectiveDate);

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
    * @param refundRule
    * @return
    */
   private boolean isRefundRuleValid(String refundRule) {
      boolean exitState = false;

      // TODO page 50 of Process Diagram and TransactionType model
      // TODO find the given Refund Rule and work over similar to below
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
                     exitState = account != null ? true : false;
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
    * Predetermine the other data qualifications for this transaction
    * before attempting to produce the transaction. Any failure in the
    * sequence sends the transaction to a fail state list with a known message
    * @param ksaTransaction
    * @return
    */
   private boolean qualifyTransaction(KsaTransaction ksaTransaction) {
      String errMsg = "";
      boolean qualified = false;
      Date effectiveDate = CalendarUtils.asDate(ksaTransaction.getEffectiveDate());

      if (!verifyRequiredValues(ksaTransaction)) {
         errMsg = "Required values are missing from transaction ";
      } else if (!doesImportAccountExist(ksaTransaction.getAccountIdentifier())) {
         errMsg = "Account does not exist";
      } else if (!isTransactionAllowed(ksaTransaction.getAccountIdentifier(), ksaTransaction.getTransactionType(), effectiveDate)) {
         errMsg = "Transaction not allowed";
      } else if (!isWithinCreditLimit(ksaTransaction.getAccountIdentifier())) {
         errMsg = "Account has insufficient credit";
      } if (!isTransactionCodeValid(ksaTransaction.getTransactionType(), effectiveDate)) {
         errMsg = "No such transaction code";
      } if (!isRefundRuleValid(ksaTransaction.getRefundRule())) {
         // applies to Payments
         errMsg = "Invalid refund rule";
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
         qualified = true;
         ksaTransactionList.add(ksaTransaction);
         //accepted.getKsaTransactionAndTransactionDetails().add(ksaTransaction);
      }

      return qualified;
   }

   /**
    * Create a persisted database transaction, otherwise an error
    * return a KsaTransaction if persisted, otherwise a null value
    * Store other types like document and override
    * @param ksaTransaction
    * @return
    */
   private KsaTransaction persistTransaction(KsaTransaction ksaTransaction) {
      xmlGCLedgerDate = null;

      KsaTransaction retKsaTrans = null;
      Transaction transaction = null;
      try {
         Date effectiveDate = CalendarUtils.asDate(ksaTransaction.getEffectiveDate());
         // create a basic transaction
         transaction =
                      transactionService.createTransaction(ksaTransaction.getTransactionType(),
                            ksaTransaction.getAccountIdentifier(), effectiveDate, ksaTransaction.getAmount());

         if (transaction != null) {
            Date originationDate = CalendarUtils.asDate(ksaTransaction.getOriginationDate());
            Currency currency = currencyService.getCurrency(ksaTransaction.getCurrency());
            transaction.setNativeAmount(ksaTransaction.getNativeAmount());
            transaction.setOriginationDate(originationDate);
            transaction.setCurrency(currency);
            transaction.setExternalId(ksaTransaction.getIncomingIdentifier());

            if (transaction instanceof Payment) {
               TransactionType transactionType = em.find(TransactionType.class, ksaTransaction.getTransactionType().toString());
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

               retKsaTrans = new KsaTransaction();
               retKsaTrans.setAccountIdentifier(transaction.getId().toString());
               retKsaTrans.setIncomingIdentifier(transaction.getExternalId());
               retKsaTrans.setEffectiveDate(CalendarUtils.asXmlGregorianCalendar(transaction.getEffectiveDate()));
               retKsaTrans.setOriginationDate(CalendarUtils.asXmlGregorianCalendar(transaction.getOriginationDate()));
               retKsaTrans.setAmount(transaction.getAmount());
               retKsaTrans.setNativeAmount(transaction.getNativeAmount());
               retKsaTrans.setCurrency(transaction.getCurrency().getIso());
               retKsaTrans.setTransactionType(transaction.getTransactionType().getId().getId());
               // document
               // TODO set the return ksaTransaction document info
               retKsaTrans.setDocument(ksaTransaction.getDocument());
               // override
               // TODO set the return ksaTransaction override info

               // refund applies to credit type only
               if (transaction instanceof Payment) {
                  totalValueCredit = totalValueCredit.add(transaction.getAmount());
                  Payment payment = (Payment) transaction;
                  retKsaTrans.setRefundRule(payment.getRefundRule());
                  retKsaTrans.setIsRefundable(payment.isRefundable());
               } else {
                  totalValueDebits = totalValueDebits.add(transaction.getAmount());
               }
            }
         }

      } catch (Exception exp) {
         logger.error(exp.getLocalizedMessage());
         logger.error("The transaction failed to create trans type " + ksaTransaction.getTransactionType() +
               " for account " + ksaTransaction.getAccountIdentifier());
      } finally {
         xmlGCLedgerDate = CalendarUtils.asXmlGregorianCalendar(transaction.getLedgerDate());
      }

      return retKsaTrans;
    }
}
