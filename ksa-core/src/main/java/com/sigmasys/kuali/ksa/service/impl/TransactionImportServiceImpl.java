package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.Account;
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
import org.xml.sax.SAXException;

import javax.jws.WebMethod;
import javax.jws.WebService;
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
public class TransactionImportServiceImpl implements TransactionImportService {
   private static final Log logger = LogFactory.getLog(TransactionServiceImpl.class);
   private static final String IMPORT_SCHEMA_LOCATION = "classpath*:ConsolidatedSchemata.xsd";
   private static final String XML_SCHEMA_LOCATION = "classpath*:xml.xsd";

   private final XmlSchemaValidator schemaValidator =
         new XmlSchemaValidator(XML_SCHEMA_LOCATION, IMPORT_SCHEMA_LOCATION);

   private Long retIdValue = 0L;
   private XMLGregorianCalendar xmlGCLedgerDate;
   KsaBatchTransactionResponse ksaBatchTransactionResponse;

   @Autowired
   private AccountService accountService;

   @Autowired
   private TransactionService transactionService;

   /**
    * This is the service method exposed to up load and process an XML string
    * Use of base 64 string can be optional
    * @param base64Xml
    * @return
    */
   @WebMethod
   public String xmlUpload(String base64Xml) {

      String xmlToPersistResponse = "";
      boolean validSchema = false;

      System.out.println(IMPORT_SCHEMA_LOCATION);
      System.out.println(XML_SCHEMA_LOCATION);

      if (!base64Xml.isEmpty()) {

         byte[] decoded = DatatypeConverter.parseBase64Binary(base64Xml);
         String xmlDataStream = new String(decoded);
         InputStream is = new ByteArrayInputStream(decoded);

         try {
            // validate against schema
            validSchema = schemaValidator.validateXml(xmlDataStream);
         } catch(IOException ioExp){
            xmlToPersistResponse = ioExp.getLocalizedMessage();
         } catch(SAXException saxExp) {
            xmlToPersistResponse = saxExp.getLocalizedMessage();
         } finally {
            // if schema is valid
            if (validSchema) {
               // convert to an object model of 1 or more

               // if there is an InputStream created
               if (is != null) {
                  // TODO need configuration information single, batch...

                  xmlToPersistResponse = convertXmlToTransaction(is);
                  System.out.println(xmlToPersistResponse);
               }
            } else {
               // send the failed schema message as the response
               xmlToPersistResponse = "Transaction content is invalid.\n\n" + xmlDataStream;
               //logger.error(errMsg);
               throw new IllegalStateException(xmlToPersistResponse);
            }
         }
      }

      // write the XML result to base64 string
      byte[] encoded = xmlToPersistResponse.getBytes();
      String base64Value = DatatypeConverter.printBase64Binary(encoded);

      return base64Value;
   }

   /**
    * Convert an XML input transaction representation to a Transaction with persistence
    * @param is
    * @return
    */
   private String convertXmlToTransaction(InputStream is) {

      String batchStatus = "incomplete";
      String uuidBatchIdentifier = null;
      BigDecimal totalValue = BigDecimal.ZERO;
      BigDecimal acceptedValue = BigDecimal.ZERO;
      BigDecimal failedValue = BigDecimal.ZERO;
      int batchSize = 0;
      int numberOfAccepted = 0;
      int numberOfFailed = 0;

      ObjectFactory of = new ObjectFactory();

      KsaBatchTransactionResponse.Accepted accepted = of.createKsaBatchTransactionResponseAccepted();
      KsaBatchTransactionResponse.Failed failed = of.createKsaBatchTransactionResponseFailed();
      KsaBatchTransactionResponse.BatchSummary batchSummary = of.createKsaBatchTransactionResponseBatchSummary();
      ksaBatchTransactionResponse = of.createKsaBatchTransactionResponse();
      ksaBatchTransactionResponse.setBatchSummary(batchSummary);
      ksaBatchTransactionResponse.setAccepted(accepted);
      ksaBatchTransactionResponse.setFailed(failed);

      // TODO Should this come from a config file?
      Class modelClass = KsaBatchTransaction.class;

      if (is != null) {
         try {

            JAXBContext jaxbContext = JAXBContext.newInstance(modelClass);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();

            boolean meetsRequiredInputs = false;
            boolean failureNoted = false;

            if (unMarshaller != null) {

               // multiple unmarshaled transaction objects
               KsaBatchTransaction ksaBatchTransaction = (KsaBatchTransaction)unMarshaller.unmarshal(is);

               // the unmarshaled list
               List<KsaTransaction> ksaTransactionList = ksaBatchTransaction.getKsaTransaction();
               if (ksaTransactionList != null) {

                  uuidBatchIdentifier = ksaBatchTransaction.getBatchIdentifier();
                  batchSize = ksaTransactionList.size();
                  //DWMSystem.out.println(ksaBatchTransaction.getKsaTransaction().toArray().length);

                  int transIndx = 0;

                  for (KsaTransaction trans : ksaTransactionList) {
                     meetsRequiredInputs = verifyRequiredValues(trans);
                     if (meetsRequiredInputs) {
                        System.out.println(trans);

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
                        persistTransaction(trans.getTransactionType().toString(),
                              trans.getAccount().toString(), effectiveDate, trans.getAmount());

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

         } catch(JAXBException jxbExp) {
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
    * @param ksaTransaction
    * @return
    */
   private boolean verifyRequiredValues(KsaTransaction ksaTransaction) {
      boolean exitState = false;

      if (ksaTransaction.getAccount() != null &&
            ksaTransaction.getIncomingIdentifier() != null &&
            ksaTransaction.getEffectiveDate() != null &&
            ksaTransaction.getAmount() != null &&
            ksaTransaction.getCurrency() != null &&
            ksaTransaction.getTransactionType() != null) {

         exitState = true;
      } else {
         logger.error("Verification of inputs failed");
      }
      return exitState;
   }

   /**
    * Check if an account exists, otherwise an error.
    * Create a persisted database transaction, otherwise an error
    * Failure is noted as an incomplete batch or single transaction processing
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
               logger.error(exp.getLocalizedMessage());
            } finally {
               if (transaction != null) {
                  retIdValue = transaction.getId();
                  xmlGCLedgerDate = XMLGregorianCalendarConversionUtil.asXMLGregorianCalendar(transaction.getLedgerDate());
               } else {
                  // transaction create failed
                  // log the reason
                  logger.error("The transaction failed to create trans type " + transactionTypeId.toString() + " for account " + userId);
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
