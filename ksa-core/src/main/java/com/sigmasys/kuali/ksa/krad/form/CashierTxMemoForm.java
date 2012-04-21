package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User: dmulderink
 * Date: 4/16/12
 * Time: 2:49 PM
 */
public class CashierTxMemoForm extends UifFormBase {
   private static final long serialVersionUID = -7525378097732916420L;

   // use this object as a query argument for matching transactions by student name
   private String studentLookupByName;

   // result set of matching persons and address postal information
   private List<Account> accountBrowseList;

   // result set of charges
   private List<Charge> chargeList;

   // result set of charges
   private List<Payment> paymentList;

   private List<Deferment> defermentList;

   // Account Status values
   private BigDecimal pastDue;

   // the account balance
   private BigDecimal balance;

   // unallocated balance
   private BigDecimal future;

   // deferment sum
   private BigDecimal defermentTotal;

   // ID and Bio information
   private String selectedId;

   // PersonName fields concatenated
   private String compositePersonName;

   // PostalAddress fields concatenated
   private String compositePostalAddress;

   // Alerts
   private List<Alert> alertList;

   // Flags
   private List<Flag> flagList;

   // the last aging date
   private Date lastAgeDate;

   // sum of aged values
   private BigDecimal agedTotal;

   // amountLate1
   private BigDecimal aged30;

   // amountLate2
   private BigDecimal aged60;

   // amountLate3
   private BigDecimal aged90;

   // Aged debit flag
   private boolean ignoreDeferment;

   // a list of Memos
   private List<Memo> memoList;

   // the Information Text field
   private String infoText;

   // the type of information
   private String infoType;

   // deprecated in this class
   private InformationTypeValue informationTypeValue;

   private Date infoEffectiveDate;

   /**
    * Get the student name
    * Possible uses is a query match for transactions
    * The value can be a partial matching name
    * @return
    */
   public String getStudentLookupByName() {
      return studentLookupByName;
   }

   /**
    * Set the student lookup name
    * Possible uses is a query match for transactions
    * The value can be a partial matching name
    * @param studentLookupByName
    */
   public void setStudentLookupByName(String studentLookupByName) {
      this.studentLookupByName = studentLookupByName;
   }

   /**
    * Get the accountBrowseList
    * Encapsulates Person and Address model
    * @return
    */
   public List<Account> getAccountBrowseList() {
      return accountBrowseList;
   }

   /**
    * Set the accountBrowseList
    * Encapsulates Person and Address model
    * @param accountBrowseList
    */
   public void setAccountBrowseList(List<Account> accountBrowseList) {
      this.accountBrowseList = accountBrowseList;
   }

   /**
    * Get the list of Charges found via a selected Account
    * @return
    */
   public List<Charge> getChargeList() {
      return chargeList;
   }

   /**
    * Set the list of Charges found via a selected Account
    * @param chargeList
    */
   public void setChargeList(List<Charge> chargeList) {
      this.chargeList = chargeList;
   }

   /**
    * Get the list of Payments found via a selected Account
    * @return
    */
   public List<Payment> getPaymentList() {
      return paymentList;
   }

   /**
    * Set the list of Payments found via a selected Account
    * @param paymentList
    */
   public void setPaymentList(List<Payment> paymentList) {
      this.paymentList = paymentList;
   }

   /**
    * Get the deferment list
    * @return
    */
   public List<Deferment> getDefermentList() {
      return defermentList;
   }

   /**
    * Set the deferment list
    * @param defermentList
    */
   public void setDefermentList(List<Deferment> defermentList) {
      this.defermentList = defermentList;
   }

   /**
    * Get the past due value
    * @return
    */
   public BigDecimal getPastDue() {
      return pastDue;
   }

   /**
    * Set the past due value
    * @param pastDue
    */
   public void setPastDue(BigDecimal pastDue) {
      this.pastDue = pastDue;
   }

   /**
    * Get the due balance
    * @return
    */
   public BigDecimal getBalance() {
      return balance;
   }

   /**
    * Set the due balance
    * @param balance
    */
   public void setBalance(BigDecimal balance) {
      this.balance = balance;
   }

   /**
    * Get the future value
    * @return
    */
   public BigDecimal getFuture() {
      return future;
   }

   /**
    * Set the future value
    * @param future
    */
   public void setFuture(BigDecimal future) {
      this.future = future;
   }

   /**
    * Get the deferment total
    * @return
    */
   public BigDecimal getDefermentTotal() {
      return defermentTotal;
   }

   /**
    * Set the deferment total
    * @param defermentTotal
    */
   public void setDefermentTotal(BigDecimal defermentTotal) {
      this.defermentTotal = defermentTotal;
   }

   /**
    * Get the selected table line item id
    * @return
    */
   public String getSelectedId() {
      return selectedId;
   }

   /**
    * Set the selected table line item id
    * @param selectedId
    */
   public void setSelectedId(String selectedId) {
      this.selectedId = selectedId;
   }

   /**
    * Get the composite person name
    * @return
    */
   public String getCompositePersonName() {
      return compositePersonName;
   }

   /**
    * Set the composite person name
    * @param compositePersonName
    */
   public void setCompositePersonName(String compositePersonName) {
      this.compositePersonName = compositePersonName;
   }

   /**
    * Get the composite postal address
    * @return
    */
   public String getCompositePostalAddress() {
      return compositePostalAddress;
   }

   /**
    * Set the composite postal address
    * @param compositePostalAddress
    */
   public void setCompositePostalAddress(String compositePostalAddress) {
      this.compositePostalAddress = compositePostalAddress;
   }

   /**
    * Get the alert list
    * @return
    */
   public List<Alert> getAlertList() {
      return alertList;
   }

   /**
    * Set the alert list
    * @param alertList
    */
   public void setAlertList(List<Alert> alertList) {
      this.alertList = alertList;
   }

   /**
    * Get the flag list
    * @return
    */
   public List<Flag> getFlagList() {
      return flagList;
   }

   /**
    * Set the flag list
    * @param flagList
    */
   public void setFlagList(List<Flag> flagList) {
      this.flagList = flagList;
   }

   /**
    * Get the last aging date
    * @return
    */
   public Date getLastAgeDate() {
      return lastAgeDate;
   }

   /**
    * Set the last aging date
    * @param lastAgeDate
    */
   public void setLastAgeDate(Date lastAgeDate) {
      this.lastAgeDate = lastAgeDate;
   }

   /**
    * Get the age total sum
    * @return
    */
   public BigDecimal getAgedTotal() {
      return getFormattedAmount(agedTotal);
   }

   /**
    * Set the age total sum
    * @param agedTotal
    */
   public void setAgedTotal(BigDecimal agedTotal) {
      this.agedTotal = agedTotal;
   }

   /**
    * Get the 30 day age value
    * @return
    */
   public BigDecimal getAged30() {
      return getFormattedAmount(aged30);
   }

   /**
    * Set the 30 day age value
    * @param aged30
    */
   public void setAged30(BigDecimal aged30) {
      this.aged30 = aged30;
   }

   /**
    * Get the 60 day age value
    * @return
    */
   public BigDecimal getAged60() {
      return getFormattedAmount(aged60);
   }

   /**
    * Set the 60 day age value
    * @param aged60
    */
   public void setAged60(BigDecimal aged60) {
      this.aged60 = aged60;
   }

   /**
    * Get the 90 day age value
    * @return
    */
   public BigDecimal getAged90() {
      return getFormattedAmount(aged90);
   }

   /**
    * Set the 90 day age value
    * @param aged90
    */
   public void setAged90(BigDecimal aged90) {
      this.aged90 = aged90;
   }

   /**
    * Get the memo list
    * @return
    */
   public List<Memo> getMemoList() {
      return memoList;
   }

   /**
    * Set the memo list
    * @param memoList
    */
   public void setMemoList(List<Memo> memoList) {
      this.memoList = memoList;
   }

   /**
    * Get the formatted BigDecimal value
    * @param value
    * @return
    */
   public BigDecimal getFormattedAmount(BigDecimal value) {
      if (value != null) {
         return value.setScale(5, BigDecimal.ROUND_CEILING);
      }
      return BigDecimal.ZERO;
   }

   /**
    * Get the infomation text
    * @return
    */
   public String getInfoText() {
      return infoText;
   }

   /**
    * Set the infomation text
    * @param infoText
    */
   public void setInfoText(String infoText) {
      this.infoText = infoText;
   }

   /**
    *
    * @return
    */
   public String getInfoType() {
      return infoType;
   }

   /**
    * Set the memo type
    * @param infoType
    */
   public void setInfoType(String infoType) {
      this.infoType = infoType;
   }

   /**
    * Not used
    * @return
    */
   public InformationTypeValue getInformationTypeValue() {
      return informationTypeValue;
   }

   /**
    * Not used
    * @param informationTypeValue
    */
   public void setInformationTypeValue(InformationTypeValue informationTypeValue) {
      this.informationTypeValue = informationTypeValue;
   }

   /**
    * Get ignoreDeferment flag
    * @return
    */
   public boolean getIgnoreDeferment() {
      return ignoreDeferment;
   }

   /**
    * Set ignoreDeferment flag
    * @param ignoreDeferment
    */
   public void setIgnoreDeferment(boolean ignoreDeferment) {
      this.ignoreDeferment = ignoreDeferment;
   }

   /**
    * Get the info effective date
    * @return
    */
   public Date getInfoEffectiveDate() {
      return infoEffectiveDate;
   }

   /**
    * Set the info effective date
    * @param infoEffectiveDate
    */
   public void setInfoEffectiveDate(Date infoEffectiveDate) {
      this.infoEffectiveDate = infoEffectiveDate;
   }
}
