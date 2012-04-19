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

   private BigDecimal balance;

   private BigDecimal future;

   private BigDecimal defermentTotal;

   // ID and Bio information
   private String selectedId;

   private String compositePersonName;

   private String compositePostalAddress;

   // Alerts and Flags
   private List<Memo> alertFlagList;

   private Date lastAgeDate;

   private BigDecimal agedTotal;

   private BigDecimal aged30;

   private BigDecimal aged60;

   private BigDecimal aged90;

   private List<Memo> memoList;


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

   public List<Deferment> getDefermentList() {
      return defermentList;
   }

   public void setDefermentList(List<Deferment> defermentList) {
      this.defermentList = defermentList;
   }

   public BigDecimal getPastDue() {
      return pastDue;
   }

   public void setPastDue(BigDecimal pastDue) {
      this.pastDue = pastDue;
   }

   public BigDecimal getBalance() {
      return balance;
   }

   public void setBalance(BigDecimal balance) {
      this.balance = balance;
   }

   public BigDecimal getFuture() {
      return future;
   }

   public void setFuture(BigDecimal future) {
      this.future = future;
   }

   public BigDecimal getDefermentTotal() {
      return defermentTotal;
   }

   public void setDefermentTotal(BigDecimal defermentTotal) {
      this.defermentTotal = defermentTotal;
   }

   public String getSelectedId() {
      return selectedId;
   }

   public void setSelectedId(String selectedId) {
      this.selectedId = selectedId;
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

   public List<Memo> getAlertFlagList() {
      return alertFlagList;
   }

   public void setAlertFlagList(List<Memo> alertFlagList) {
      this.alertFlagList = alertFlagList;
   }

   public Date getLastAgeDate() {
      return lastAgeDate;
   }

   public void setLastAgeDate(Date lastAgeDate) {
      this.lastAgeDate = lastAgeDate;
   }

   public BigDecimal getAgedTotal() {
      return getFormattedAmount(agedTotal);
   }

   public void setAgedTotal(BigDecimal agedTotal) {
      this.agedTotal = agedTotal;
   }

   public BigDecimal getAged30() {
      return getFormattedAmount(aged30);
   }

   public void setAged30(BigDecimal aged30) {
      this.aged30 = aged30;
   }

   public BigDecimal getAged60() {
      return getFormattedAmount(aged60);
   }

   public void setAged60(BigDecimal aged60) {
      this.aged60 = aged60;
   }

   public BigDecimal getAged90() {
      return getFormattedAmount(aged90);
   }

   public void setAged90(BigDecimal aged90) {
      this.aged90 = aged90;
   }

   public List<Memo> getMemoList() {
      return memoList;
   }

   public void setMemoList(List<Memo> memoList) {
      this.memoList = memoList;
   }

   public BigDecimal getFormattedAmount(BigDecimal value) {
      if (value != null) {
         return value.setScale(5, BigDecimal.ROUND_CEILING);
      }
      return BigDecimal.ZERO;
   }
}
