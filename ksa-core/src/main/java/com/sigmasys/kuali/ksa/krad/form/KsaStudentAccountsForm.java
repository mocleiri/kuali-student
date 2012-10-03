package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.TransactionModel;
import com.sigmasys.kuali.ksa.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 8/29/12 at 12:58 PM
 */
public class KsaStudentAccountsForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   /*
     Account Overview
   */

   private List<TransactionModel> rollUpTransactionModelList;

   private List<TransactionModel> unGroupedTransactionModelList;

   private List<TransactionModel> byRollUpTransactionModelList;

   private List<Allocation> allocationList;

   private TransactionModel transactionModel;

   private String id;

   private String accountId;

   private Account account;

   private String allocationId;

   private String allocationAccountId;

   private String extensionId;

   private String payerTransactionId;

   private String paidTransactionId;

   private String deferredId;

   private String documentId;

   private String previousMemoId;

   private String nextMemoId;

   private String derivativeTransactionId;

   private String memo;

   private String rollUpDesc;

   private String rollUpPriority;

   private String rollUpTag;

   private String rollUpType;

   private BigDecimal rollUpBalance;

   private BigDecimal rollUpDebit;

   private BigDecimal rollUpCredit;

   private BigDecimal amount;

   private BigDecimal amountAllocated;

   private BigDecimal amountAllocatedLocked;

   private BigDecimal allocationAmount;

   private String internal;

   private String overRidden;

   private String paymentBilling;

   private String refundable;

   private String refundRule;

   private String generalLedgerType;

   private String generalLedgerTypeId;

   private String entryGenerated;

   private String allocationLocked;

   private Date ledgerDate;

   private Date effectiveDate;

   private Date originationDate;

   private Date recognitionDate;

   private Date expirationDate;

   private Date clearDate;

   private String nativeAmountCurr;

   private String creatorId;


   private String type;

   private String typeId;

   private String typeSubCode;

   private String statementText;

   private BigDecimal unGroupedBalance;

   private BigDecimal unGroupedDebit;

   private BigDecimal unGroupedCredit;

   private BigDecimal unGroupedTotalCredit;

   private String selectedRollupType;

   private String selectedTransactionType;

   /*
      Get/Set methods
    */

   /*
      Form values
    */

   public List<TransactionModel> getRollUpTransactionModelList() {
      return rollUpTransactionModelList;
   }

   public void setRollUpTransactionModelList(List<TransactionModel> rollUpTransactionModelList) {
      this.rollUpTransactionModelList = rollUpTransactionModelList;
   }

   public List<TransactionModel> getUnGroupedTransactionModelList() {
      return unGroupedTransactionModelList;
   }

   public void setUnGroupedTransactionModelList(List<TransactionModel> unGroupedTransactionModelList) {
      this.unGroupedTransactionModelList = unGroupedTransactionModelList;
   }

   public List<TransactionModel> getByRollUpTransactionModelList() {
      return byRollUpTransactionModelList;
   }

   public void setByRollUpTransactionModelList(List<TransactionModel> byRollUpTransactionModelList) {
      this.byRollUpTransactionModelList = byRollUpTransactionModelList;
   }

   public List<Allocation> getAllocationList() {
      return allocationList;
   }

   public void setAllocationList(List<Allocation> allocationList) {
      this.allocationList = allocationList;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getAccountId() {
      return accountId;
   }

   public void setAccountId(String accountId) {
      this.accountId = accountId;
   }

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public String getAllocationId() {
      return allocationId;
   }

   public void setAllocationId(String allocationId) {
      this.allocationId = allocationId;
   }

   public String getAllocationAccountId() {
      return allocationAccountId;
   }

   public void setAllocationAccountId(String allocationAccountId) {
      this.allocationAccountId = allocationAccountId;
   }

   public String getExtensionId() {
      return extensionId;
   }

   public void setExtensionId(String extensionId) {
      this.extensionId = extensionId;
   }

   public String getPayerTransactionId() {
      return payerTransactionId;
   }

   public void setPayerTransactionId(String payerTransactionId) {
      this.payerTransactionId = payerTransactionId;
   }

   public String getPaidTransactionId() {
      return paidTransactionId;
   }

   public void setPaidTransactionId(String paidTransactionId) {
      this.paidTransactionId = paidTransactionId;
   }

   public String getDeferredId() {
      return deferredId;
   }

   public void setDeferredId(String deferredId) {
      this.deferredId = deferredId;
   }

   public String getDocumentId() {
      return documentId;
   }

   public void setDocumentId(String documentId) {
      this.documentId = documentId;
   }

   public String getPreviousMemoId() {
      return previousMemoId;
   }

   public void setPreviousMemoId(String previousMemoId) {
      this.previousMemoId = previousMemoId;
   }

   public String getNextMemoId() {
      return nextMemoId;
   }

   public void setNextMemoId(String nextMemoId) {
      this.nextMemoId = nextMemoId;
   }

   public String getDerivativeTransactionId() {
      return derivativeTransactionId;
   }

   public void setDerivativeTransactionId(String derivativeTransactionId) {
      this.derivativeTransactionId = derivativeTransactionId;
   }

   public String getMemo() {
      return memo;
   }

   public void setMemo(String memo) {
      this.memo = memo;
   }

   public String getRollUpDesc() {
      return rollUpDesc;
   }

   public void setRollUpDesc(String rollUpDesc) {
      this.rollUpDesc = rollUpDesc;
   }

   public String getRollUpPriority() {
      return rollUpPriority;
   }

   public void setRollUpPriority(String rollUpPriority) {
      this.rollUpPriority = rollUpPriority;
   }

   public String getRollUpTag() {
      return rollUpTag;
   }

   public void setRollUpTag(String rollUpTag) {
      this.rollUpTag = rollUpTag;
   }

   public String getRollUpType() {
      return rollUpType;
   }

   public void setRollUpType(String rollUpType) {
      this.rollUpType = rollUpType;
   }

   public BigDecimal getRollUpBalance() {
      return rollUpBalance;
   }

   public void setRollUpBalance(BigDecimal rollUpBalance) {
      this.rollUpBalance = rollUpBalance;
   }

   public BigDecimal getRollUpDebit() {
      return rollUpDebit;
   }

   public void setRollUpDebit(BigDecimal rollUpDebit) {
      this.rollUpDebit = rollUpDebit;
   }

   public BigDecimal getRollUpCredit() {
      return rollUpCredit;
   }

   public void setRollUpCredit(BigDecimal rollUpCredit) {
      this.rollUpCredit = rollUpCredit;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public BigDecimal getAmountAllocated() {
      return amountAllocated;
   }

   public void setAmountAllocated(BigDecimal amountAllocated) {
      this.amountAllocated = amountAllocated;
   }

   public BigDecimal getAmountAllocatedLocked() {
      return amountAllocatedLocked;
   }

   public void setAmountAllocatedLocked(BigDecimal amountAllocatedLocked) {
      this.amountAllocatedLocked = amountAllocatedLocked;
   }

   public BigDecimal getAllocationAmount() {
      return allocationAmount;
   }

   public void setAllocationAmount(BigDecimal allocationAmount) {
      this.allocationAmount = allocationAmount;
   }

   public String getRefundRule() {
      return refundRule;
   }

   public void setRefundRule(String refundRule) {
      this.refundRule = refundRule;
   }

   public String getGeneralLedgerType() {
      return generalLedgerType;
   }

   public void setGeneralLedgerType(String generalLedgerType) {
      this.generalLedgerType = generalLedgerType;
   }

   public Date getLedgerDate() {
      return ledgerDate;
   }

   public void setLedgerDate(Date ledgerDate) {
      this.ledgerDate = ledgerDate;
   }

   public Date getEffectiveDate() {
      return effectiveDate;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public Date getOriginationDate() {
      return originationDate;
   }

   public void setOriginationDate(Date originationDate) {
      this.originationDate = originationDate;
   }

   public Date getRecognitionDate() {
      return recognitionDate;
   }

   public void setRecognitionDate(Date recognitionDate) {
      this.recognitionDate = recognitionDate;
   }

   public Date getExpirationDate() {
      return expirationDate;
   }

   public void setExpirationDate(Date expirationDate) {
      this.expirationDate = expirationDate;
   }

   public Date getClearDate() {
      return clearDate;
   }

   public void setClearDate(Date clearDate) {
      this.clearDate = clearDate;
   }

   public String getNativeAmountCurr() {
      return nativeAmountCurr;
   }

   public void setNativeAmountCurr(String nativeAmountCurr) {
      this.nativeAmountCurr = nativeAmountCurr;
   }

   public String getCreatorId() {
      return creatorId;
   }

   public void setCreatorId(String creatorId) {
      this.creatorId = creatorId;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getTypeId() {
      return typeId;
   }

   public void setTypeId(String typeId) {
      this.typeId = typeId;
   }

   public String getTypeSubCode() {
      return typeSubCode;
   }

   public void setTypeSubCode(String typeSubCode) {
      this.typeSubCode = typeSubCode;
   }

   public String getStatementText() {
      return statementText;
   }

   public void setStatementText(String statementText) {
      this.statementText = statementText;
   }

   public BigDecimal getUnGroupedBalance() {
      return unGroupedBalance;
   }

   public void setUnGroupedBalance(BigDecimal unGroupedBalance) {
      this.unGroupedBalance = unGroupedBalance;
   }

   public BigDecimal getUnGroupedDebit() {
      return unGroupedDebit;
   }

   public void setUnGroupedDebit(BigDecimal unGroupedDebit) {
      this.unGroupedDebit = unGroupedDebit;
   }

   public BigDecimal getUnGroupedCredit() {
      return unGroupedCredit;
   }

   public void setUnGroupedCredit(BigDecimal unGroupedCredit) {
      this.unGroupedCredit = unGroupedCredit;
   }

   public BigDecimal getUnGroupedTotalCredit() {
      return unGroupedTotalCredit;
   }

   public void setUnGroupedTotalCredit(BigDecimal unGroupedTotalCredit) {
      this.unGroupedTotalCredit = unGroupedTotalCredit;
   }

   public String getSelectedRollupType() {
      return selectedRollupType;
   }

   public void setSelectedRollupType(String selectedRollupType) {
      this.selectedRollupType = selectedRollupType;
   }

   public String getSelectedTransactionType() {
      return selectedTransactionType;
   }

   public void setSelectedTransactionType(String selectedTransactionType) {
      this.selectedTransactionType = selectedTransactionType;
   }

   public String getInternal() {
      return internal;
   }

   public void setInternal(String internal) {
      this.internal = internal;
   }

   public String getOverRidden() {
      return overRidden;
   }

   public void setOverRidden(String overRidden) {
      this.overRidden = overRidden;
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

   public String getGeneralLedgerTypeId() {
      return generalLedgerTypeId;
   }

   public void setGeneralLedgerTypeId(String generalLedgerTypeId) {
      this.generalLedgerTypeId = generalLedgerTypeId;
   }

   public TransactionModel getTransactionModel() {
      return transactionModel;
   }

   public void setTransactionModel(TransactionModel transactionModel) {
      this.transactionModel = transactionModel;
   }
}
