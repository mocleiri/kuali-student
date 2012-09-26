package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Allocation;
import com.sigmasys.kuali.ksa.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 8/29/12 at 12:58 PM
 */
public class KsaStudentAccountsForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   // use this object as a query argument for matching transactions by student name
   private String bioSearchByAccount;

   // this is the composite person name first + middle and last name
   private String selectedAccountCompositePersonName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   // account ID and Bio information
   private String selectedId;

   /*
      Summary information and Biographic
    */
   private String kimNameType;

   private String title;

   private String firstName;

   private String middleName;

   private String lastName;

   private String suffix;

   private String isDefault;

   // PersonName fields concatenated
   private String compositePersonName;

   // PostalAddress fields concatenated
   private String compositePostalAddress;



   /*
     Account Overview
   */
   private List<Transaction> rollUpList;

   private List<Transaction> unGroupedList;

   private List<Transaction> byRollUpList;

   private List<Allocation> allocationList;

   private String id;

   private String accountId;

   private String allocationId;

   private String allocationAccountId;

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

   private boolean isInternal;

   private boolean isOverridden;

   private boolean isPaymentBilling;

   private boolean isRefundable;

   private String refundRule;

   private String generalLedgerType;

   private boolean isEntryGenerated;

   private boolean isOverRidden;

   private boolean isAllocationLocked;

   private Date ledgerDate;

   private Date effectiveDate;

   private Date originationDate;

   private Date recognitionDate;

   private Date expirationDate;

   private Date clearDate;

   private String nativeAmountCurr;

   private String ResponsibleEntity;


   private String type;

   private String typeId;

   private String typeSubCode;

   private String statementText;

   private BigDecimal unGroupedBalance;

   private BigDecimal unGroupedDebit;

   private BigDecimal unGroupedCredit;

   private BigDecimal unGroupedTotalCredit;


   /*
      Get/Set methods
    */

   /*
      Form values
    */


   public String getBioSearchByAccount() {
      return bioSearchByAccount;
   }

   public void setBioSearchByAccount(String bioSearchByAccount) {
      this.bioSearchByAccount = bioSearchByAccount;
   }

   public String getSelectedAccountCompositePersonName() {
      return selectedAccountCompositePersonName;
   }

   public void setSelectedAccountCompositePersonName(String selectedAccountCompositePersonName) {
      this.selectedAccountCompositePersonName = selectedAccountCompositePersonName;
   }

   public List<Account> getAccountBrowseList() {
      return accountBrowseList;
   }

   public void setAccountBrowseList(List<Account> accountBrowseList) {
      this.accountBrowseList = accountBrowseList;
   }

   public String getSelectedId() {
      return selectedId;
   }

   public void setSelectedId(String selectedId) {
      this.selectedId = selectedId;
   }

   public String getKimNameType() {
      return kimNameType;
   }

   public void setKimNameType(String kimNameType) {
      this.kimNameType = kimNameType;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getMiddleName() {
      return middleName;
   }

   public void setMiddleName(String middleName) {
      this.middleName = middleName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getSuffix() {
      return suffix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public String getDefault() {
      return isDefault;
   }

   public void setDefault(String aDefault) {
      isDefault = aDefault;
   }

   public String getCompositePersonName() {
      return compositePersonName;
   }

   public void setCompositePersonName(String compositePersonName) {
      this.compositePersonName = compositePersonName;
   }

   public String getCompositePostalAddress() {
      return compositePostalAddress;
   }

   public void setCompositePostalAddress(String compositePostalAddress) {
      this.compositePostalAddress = compositePostalAddress;
   }

   public List<Transaction> getRollUpList() {
      return rollUpList;
   }

   public void setRollUpList(List<Transaction> rollUpList) {
      this.rollUpList = rollUpList;
   }

   public List<Transaction> getUnGroupedList() {
      return unGroupedList;
   }

   public void setUnGroupedList(List<Transaction> unGroupedList) {
      this.unGroupedList = unGroupedList;
   }

   public List<Transaction> getByRollUpList() {
      return byRollUpList;
   }

   public void setByRollUpList(List<Transaction> byRollUpList) {
      this.byRollUpList = byRollUpList;
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

   public boolean isInternal() {
      return isInternal;
   }

   public void setInternal(boolean internal) {
      isInternal = internal;
   }

   public boolean isOverridden() {
      return isOverridden;
   }

   public void setOverridden(boolean overridden) {
      isOverridden = overridden;
   }

   public boolean isPaymentBilling() {
      return isPaymentBilling;
   }

   public void setPaymentBilling(boolean paymentBilling) {
      isPaymentBilling = paymentBilling;
   }

   public boolean isRefundable() {
      return isRefundable;
   }

   public void setRefundable(boolean refundable) {
      isRefundable = refundable;
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

   public boolean isEntryGenerated() {
      return isEntryGenerated;
   }

   public void setEntryGenerated(boolean entryGenerated) {
      isEntryGenerated = entryGenerated;
   }

   public boolean isOverRidden() {
      return isOverRidden;
   }

   public void setOverRidden(boolean overRidden) {
      isOverRidden = overRidden;
   }

   public boolean isAllocationLocked() {
      return isAllocationLocked;
   }

   public void setAllocationLocked(boolean allocationLocked) {
      isAllocationLocked = allocationLocked;
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

   public String getResponsibleEntity() {
      return ResponsibleEntity;
   }

   public void setResponsibleEntity(String responsibleEntity) {
      ResponsibleEntity = responsibleEntity;
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
}
