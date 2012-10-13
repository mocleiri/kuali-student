package com.sigmasys.kuali.ksa.krad.model;

import com.sigmasys.kuali.ksa.model.*;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by: dmulderink on 9/27/12 at 7:46 AM
 */
public class TransactionModel extends Transaction {

   private TransactionTypeValue transactionTypeValue;

   private String transactionAmount;

   private String transactionNativeAmount;

   private String transactionAmountAllocated;

   private String transactionAmountAllocatedLocked;

   private String transactionTypeId;

   private String transactionTypeDesc;

   private String currencyCode;
   //
   private String rollupDesc;

   // a comma delimited collection of rollup tag values for the transactionType
   private String rollupTag;

   // transaction type subcode
   private String transactionTypeSubCode;

    // Integer equivalent for a Debit, otherwise null
   private String debitPriority;

   private String deferredId;

   // Date for a deferment, otherwise null
   private Date defermentExpirationDate;

   // Date for a Payment, otherwise null
   private Date paymentClearDate;

   private String refundRule;

   // Boolean get method does not work well except as a string
   private String internal;

   // Document id as a string
   private String documentId;

   // General Ledger id;
   private String generalLedgerTypeId;

   // Boolean get method does not work well except as a string
   private String glOverRidden;

   // aggregated values
   private String rollUpBalance;

   private String rollUpDebit;

   private String rollUpCredit;

   private String unGroupedBalance;

   private String unGroupedDebit;

   private String unGroupedCredit;

   private String unGroupedTotalCredit;


   // checkboxes
   private String paymentBilling;

   private String refundable;

   private String entryGenerated;

   private String allocationLocked;


    // Constructor
    public TransactionModel() {
    }

    public TransactionModel(Transaction transaction) {
        transactionTypeValue = transaction.getTransactionTypeValue();
        // populate TransactionModel's properties from Transaction instance
        setId(transaction.getId());
        setAccountId(transaction.getAccountId());
        setTransactionType(transaction.getTransactionType());
        setRollup(transaction.getRollup());
        setExternalId(transaction.getExternalId());
        setCreationDate(transaction.getCreationDate());
        setEffectiveDate(transaction.getEffectiveDate());
        setOriginationDate(transaction.getOriginationDate());
        setRecognitionDate(transaction.getRecognitionDate());
        setAmount(transaction.getAmount());
        setNativeAmount(transaction.getNativeAmount());
        setCurrency(transaction.getCurrency());
        setInternal(transaction.isInternal());
        setAllocatedAmount(transaction.getAllocatedAmount());
        setLockedAllocatedAmount(transaction.getLockedAllocatedAmount());
        setStatementText(transaction.getStatementText());
        setDocument(transaction.getDocument());
        setAccount(transaction.getAccount());
        setGlEntryGenerated(transaction.isGlEntryGenerated());
        setGeneralLedgerType(transaction.getGeneralLedgerType());
        setGlOverridden(transaction.isGlOverridden());

        // charge, payment or deferment specific data members
    }

    @Override
    public TransactionTypeValue getTransactionTypeValue() {
        return transactionTypeValue;
    }

   @Transient
   public String getFormattedUSDAmount(String value) {

      String formattedNumber = "";

      if (value != null) {
         NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
         double doubleValue = Double.parseDouble(value);
         String usdCurrency = numberFormat.format(doubleValue);
         formattedNumber = usdCurrency.substring(1);
      }
      return formattedNumber;
   }

   @Transient
   public String getFormattedUSDAmount(BigDecimal value) {

      String formattedNumber = "";

      if (value != null) {
         NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
         String usdCurrency = numberFormat.format(value);
         formattedNumber = usdCurrency.substring(1);
      }
      return formattedNumber;
   }

   @Transient
   public String getFormattedAmount(BigDecimal value) {

      String formattedNumber = "";

      if (value != null) {
         NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
         //double doubleValue = Double.parseDouble(value);
         String usdCurrency = numberFormat.format(value);
         formattedNumber = usdCurrency.substring(1);
      }
      return formattedNumber;
   }


   public String getTransactionTypeId() {
      return transactionTypeId;
   }

   public void setTransactionTypeId(String transactionTypeId) {
      this.transactionTypeId = transactionTypeId;
   }

   public String getTransactionTypeDesc() {
      return transactionTypeDesc;
   }

   public void setTransactionTypeDesc(String transactionTypeDesc) {
      this.transactionTypeDesc = transactionTypeDesc;
   }

   public String getCurrencyCode() {
      if (this.getCurrency() != null) {
         return this.getCurrency().getCode();
      }

      return "";
   }

   public void setCurrencyCode(String currencyCode) {
      if (this.getCurrency() != null) {
         this.getCurrency().setCode(currencyCode);
         this.currencyCode = currencyCode;
      }
   }

   public String getTransactionAmount() {
      return transactionAmount = getFormattedUSDAmount(this.getAmount());
   }

   public void setTransactionAmount(String transactionAmount) {
      double value = Double.parseDouble(transactionAmount);
      this.setAmount(BigDecimal.valueOf(value));
      this.transactionAmount = transactionAmount;
   }

   public String getTransactionNativeAmount() {
      return transactionNativeAmount = getFormattedAmount(this.getNativeAmount());
   }

   public void setTransactionNativeAmount(String transactionNativeAmount) {
      double value = Double.parseDouble(transactionNativeAmount);
      this.setNativeAmount(BigDecimal.valueOf(value));
      this.transactionNativeAmount = transactionNativeAmount;
   }

   public String getTransactionAmountAllocated() {
      return transactionAmountAllocated = getFormattedUSDAmount(this.getAllocatedAmount());
   }

   public void setTransactionAmountAllocated(String transactionAmountAllocated) {
      double value = Double.parseDouble(transactionAmountAllocated);
      this.setAllocatedAmount(BigDecimal.valueOf(value));
      this.transactionAmountAllocated = transactionAmountAllocated;
   }

   public String getTransactionAmountAllocatedLocked() {
      return transactionAmountAllocatedLocked = getFormattedUSDAmount(this.getLockedAllocatedAmount());
   }

   public void setTransactionAmountAllocatedLocked(String transactionAmountAllocatedLocked) {
      double value = Double.parseDouble(transactionAmountAllocatedLocked);
      this.setLockedAllocatedAmount(BigDecimal.valueOf(value));
      this.transactionAmountAllocatedLocked = transactionAmountAllocatedLocked;
   }

   public String getRollupDesc() {
      return rollupDesc;
   }

   public void setRollupDesc(String rollupDesc) {
      this.rollupDesc = rollupDesc;
   }

   public String getTransactionTypeSubCode() {
      return transactionTypeSubCode;
   }

   public String getRollupTag() {
      return rollupTag;
   }

   public void setRollupTag(String rollupTag) {
      this.rollupTag = rollupTag;
   }

   public void setTransactionTypeSubCode(String transactionTypeSubCode) {
      this.transactionTypeSubCode = transactionTypeSubCode;
   }

   public String getDebitPriority() {
      return debitPriority;
   }

   public void setDebitPriority(String debitPriority) {
      this.debitPriority = debitPriority;
   }

   public String getDeferredId() {
      return deferredId;
   }

   public void setDeferredId(String deferredId) {
      this.deferredId = deferredId;
   }

   public Date getDefermentExpirationDate() {
      return defermentExpirationDate;
   }

   public void setDefermentExpirationDate(Date defermentExpirationDate) {
      this.defermentExpirationDate = defermentExpirationDate;
   }

   public Date getPaymentClearDate() {
      return paymentClearDate;
   }

   public void setPaymentClearDate(Date paymentClearDate) {
      this.paymentClearDate = paymentClearDate;
   }

   public String getRefundRule() {
      return refundRule;
   }

   public void setRefundRule(String refundRule) {
      this.refundRule = refundRule;
   }

   public String getInternal() {
      return internal;
   }

   public void setInternal(String internal) {
      this.internal = internal;
   }

   public String getDocumentId() {
      return documentId;
   }

   public void setDocumentId(String documentId) {
      this.documentId = documentId;
   }

   public String getGeneralLedgerTypeId() {
      return generalLedgerTypeId;
   }

   public void setGeneralLedgerTypeId(String generalLedgerTypeId) {
      this.generalLedgerTypeId = generalLedgerTypeId;
   }

   public String getGlOverRidden() {
      return glOverRidden;
   }

   public void setGlOverRidden(String glOverRidden) {
      this.glOverRidden = glOverRidden;
   }

   public String getPaymentBilling() {
      return paymentBilling;
   }

   public void setPaymentBilling(String paymentBilling) {
      this.paymentBilling = paymentBilling;
   }

   public String getRefundable() {
      return refundable;
   }

   public void setRefundable(String refundable) {
      this.refundable = refundable;
   }

   public String getEntryGenerated() {
      return entryGenerated;
   }

   public void setEntryGenerated(String entryGenerated) {
      this.entryGenerated = entryGenerated;
   }

   public String getAllocationLocked() {
      return allocationLocked;
   }

   public void setAllocationLocked(String allocationLocked) {
      this.allocationLocked = allocationLocked;
   }


   // aggregates
   public String getRollUpBalance() {
      return getFormattedUSDAmount(rollUpBalance);
   }

   public void setRollUpBalance(String rollUpBalance) {
      this.rollUpBalance = rollUpBalance;
   }

   public String getRollUpDebit() {
      return getFormattedUSDAmount(rollUpDebit);
   }

   public void setRollUpDebit(String rollUpDebit) {
      this.rollUpDebit = rollUpDebit;
   }

   public String getRollUpCredit() {
      return getFormattedUSDAmount(rollUpCredit);
   }

   public void setRollUpCredit(String rollUpCredit) {
      this.rollUpCredit = rollUpCredit;
   }

   public String getUnGroupedBalance() {
      return getFormattedUSDAmount(unGroupedBalance);
   }

   public void setUnGroupedBalance(String unGroupedBalance) {
      this.unGroupedBalance = unGroupedBalance;
   }

   public String getUnGroupedDebit() {
      return getFormattedUSDAmount(unGroupedDebit);
   }

   public void setUnGroupedDebit(String unGroupedDebit) {
      this.unGroupedDebit = unGroupedDebit;
   }

   public String getUnGroupedCredit() {
      return getFormattedUSDAmount(unGroupedCredit);
   }

   public void setUnGroupedCredit(String unGroupedCredit) {
      this.unGroupedCredit = unGroupedCredit;
   }

   public String getUnGroupedTotalCredit() {
      return getFormattedUSDAmount(unGroupedTotalCredit);
   }

   public void setUnGroupedTotalCredit(String unGroupedTotalCredit) {
      this.unGroupedTotalCredit = unGroupedTotalCredit;
   }
}
