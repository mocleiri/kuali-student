package com.sigmasys.kuali.ksa.transform;

import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.util.XMLGregorianCalendarConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.lang.Long;

/**
 * Created by: dmulderink on 6/24/12 at 10:56 AM
 */
public class XmlToObj {

   private Long retIdValue = 0L;
   private XMLGregorianCalendar xmlGCLedgerDate;

   @Autowired
   private AccountService accountService;

   @Autowired
   private TransactionService transactionService;

   /*
     Unmarshal is the process of converting an XML file content into a class object
     Marshall is a class object to XML
   */
   public String convertXmlToTransaction(InputStream is, boolean isBatch) {
      String batchStatus = "incomplete";
      ObjectFactory of = new ObjectFactory();

      KsaBatchTransactionResponse.Accepted accepted = of.createKsaBatchTransactionResponseAccepted();
      KsaBatchTransactionResponse.Failed failed = of.createKsaBatchTransactionResponseFailed();
      KsaBatchTransactionResponse.BatchSummary batchSummary = of.createKsaBatchTransactionResponseBatchSummary();
      KsaBatchTransactionResponse ksaBatchTransactionResponse = of.createKsaBatchTransactionResponse();
      ksaBatchTransactionResponse.setBatchSummary(batchSummary);
      ksaBatchTransactionResponse.setAccepted(accepted);
      ksaBatchTransactionResponse.setFailed(failed);

      // TODO Should this come from a config file?
      Class modelClass = isBatch == true ? KsaBatchTransaction.class : KsaTransaction.class;

      if (is != null) {
         try {

            JAXBContext jaxbContext = JAXBContext.newInstance(modelClass);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            boolean meetsRequiredInputs = false;

            if (unMarshaller != null) {
               BigDecimal totalValue = BigDecimal.ZERO;
               BigDecimal acceptedValue = BigDecimal.ZERO;
               BigDecimal failedValue = BigDecimal.ZERO;

               boolean failureNoted = false;

               int numberOfAccepted = 0;
               int numberOfFailed = 0;

               if (isBatch) {
                  // multiple unmarshaled transaction objects
                  KsaBatchTransaction ksaBatchTransaction = (KsaBatchTransaction)unMarshaller.unmarshal(is);
                  // the unmarshaled list
                  List<KsaTransaction> ksaTransactionList = ksaBatchTransaction.getKsaTransaction();
                  System.out.println(ksaBatchTransaction.getKsaTransaction().toArray().length);

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

                  if (!failureNoted) {
                     batchStatus = "complete";
                  }

                  // fill in the response
                  ksaBatchTransactionResponse.setResponseIdentifier(UUID.randomUUID().toString());
                  ksaBatchTransactionResponse.setResponseToBatchIdentifier(ksaBatchTransaction.getBatchIdentifier());
                  ksaBatchTransactionResponse.setBatchStatus(batchStatus);
                  batchSummary.setTransactionsInBatch(ksaTransactionList.size());
                  batchSummary.setValueOfBatch(totalValue);
                  batchSummary.setTransactionsAccepted(numberOfAccepted);
                  batchSummary.setTransactionsFailed(numberOfFailed);
                  batchSummary.setValueAccepted(acceptedValue);
                  batchSummary.setValueFailed(failedValue);

               } else {
                  // a single unmarshaled KsaTransaction object
                  KsaTransaction trans = ((KsaTransaction)unMarshaller.unmarshal(is));
                  System.out.println(trans);

                  meetsRequiredInputs = verifyRequiredValues(trans);
                  if (meetsRequiredInputs) {

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
                  } else {
                     failureNoted = true;
                  }

                  if (!failureNoted) {
                     batchStatus = "complete";
                  }

                  // TODO this should come from the BatchReceipt class for a first single transaction
                  // or from an unmarshaled Xml batch identifier tag entry
                  ksaBatchTransactionResponse.setResponseIdentifier(UUID.randomUUID().toString());
                  ksaBatchTransactionResponse.setResponseToBatchIdentifier(UUID.randomUUID().toString());
                  ksaBatchTransactionResponse.setBatchStatus(batchStatus);
                  batchSummary.setTransactionsInBatch(1);
                  batchSummary.setValueOfBatch(trans.getAmount());
                  batchSummary.setTransactionsAccepted(numberOfAccepted);
                  batchSummary.setTransactionsFailed(numberOfFailed);
                  batchSummary.setValueAccepted(acceptedValue);
                  batchSummary.setValueFailed(failedValue);

               }
            }
         } catch(JAXBException jxbExp) {
            ksaBatchTransactionResponse.setBatchStatus(jxbExp.getLocalizedMessage());
            jxbExp.printStackTrace();
         }
      }

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
      }

      // return the XML marshaled result or exception
      return writer.toString();
   }

   /**
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
         com.sigmasys.kuali.ksa.model.Account account = accountService.getFullAccount(userId);

         if (account != null) {
            Transaction transaction =
                  transactionService.createTransaction(transactionTypeId, userId, effectiveDate, amount);

            if (transaction != null) {
               retIdValue = transaction.getId();
               xmlGCLedgerDate = XMLGregorianCalendarConversionUtil.asXMLGregorianCalendar(transaction.getLedgerDate());
            }
         }
      }
   }

   /**
    *
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
      }
      return exitState;
   }
}
