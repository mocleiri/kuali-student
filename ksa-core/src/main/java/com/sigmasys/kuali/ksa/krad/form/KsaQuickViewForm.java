package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Alert;
import com.sigmasys.kuali.ksa.model.Flag;
import com.sigmasys.kuali.ksa.model.Memo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

/**
 * Created by: dmulderink on 9/28/12 at 2:25 PM
 */
public class KsaQuickViewForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private Account account;

   private String userId;

   // Account Overview

   private BigDecimal pastDueAmount;

   private BigDecimal balanceAmount;

   private BigDecimal futureAmount;

   private BigDecimal defermentAmount;

   // Biographic Information

   private String compositeDefaultPersonName;

   private String compositeDefaultPostalAddress;

   // Alert

   private List<Alert> alerts;

   // Flag

   private List<Flag> flags;

   // Aging

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
   private String ignoreDeferment;

   // Memos

   private List<Memo> memos;

   private Date memoEffectiveDate;

   private Date memoExpirationDate;

   private String memoAccessLevel;

   private String previousMemo;

   private String nextMemo;

   private String memoText;
   /*
      Get / Set methods
    */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public BigDecimal getPastDueAmount() {
      return pastDueAmount;
   }

   public void setPastDueAmount(BigDecimal pastDueAmount) {
      this.pastDueAmount = pastDueAmount;
   }

   public BigDecimal getBalanceAmount() {
      return balanceAmount;
   }

   public void setBalanceAmount(BigDecimal balanceAmount) {
      this.balanceAmount = balanceAmount;
   }

   public BigDecimal getFutureAmount() {
      return futureAmount;
   }

   public void setFutureAmount(BigDecimal futureAmount) {
      this.futureAmount = futureAmount;
   }

   public BigDecimal getDefermentAmount() {
      return defermentAmount;
   }

   public void setDefermentAmount(BigDecimal defermentAmount) {
      this.defermentAmount = defermentAmount;
   }


   public String getCompositeDefaultPersonName() {
      return compositeDefaultPersonName;
   }

   public void setCompositeDefaultPersonName(String compositeDefaultPersonName) {
      this.compositeDefaultPersonName = compositeDefaultPersonName;
   }

   public String getCompositeDefaultPostalAddress() {
      return compositeDefaultPostalAddress;
   }

   public void setCompositeDefaultPostalAddress(String compositeDefaultPostalAddress) {
      this.compositeDefaultPostalAddress = compositeDefaultPostalAddress;
   }


   public List<Alert> getAlerts() {
      return alerts;
   }

   public void setAlerts(List<Alert> alerts) {
      this.alerts = alerts;
   }


   public List<Flag> getFlags() {
      return flags;
   }

   public void setFlags(List<Flag> flags) {
      this.flags = flags;
   }


   public Date getLastAgeDate() {
      return lastAgeDate;
   }

   public void setLastAgeDate(Date lastAgeDate) {
      this.lastAgeDate = lastAgeDate;
   }

   public BigDecimal getAgedTotal() {
      return agedTotal;
   }

   public void setAgedTotal(BigDecimal agedTotal) {
      this.agedTotal = agedTotal;
   }

   public BigDecimal getAged30() {
      return aged30;
   }

   public void setAged30(BigDecimal aged30) {
      this.aged30 = aged30;
   }

   public BigDecimal getAged60() {
      return aged60;
   }

   public void setAged60(BigDecimal aged60) {
      this.aged60 = aged60;
   }

   public BigDecimal getAged90() {
      return aged90;
   }

   public void setAged90(BigDecimal aged90) {
      this.aged90 = aged90;
   }


   public String getIgnoreDeferment() {
      return ignoreDeferment;
   }

   public void setIgnoreDeferment(String ignoreDeferment) {
      this.ignoreDeferment = ignoreDeferment;
   }

   public List<Memo> getMemos() {
      return memos;
   }

   public void setMemos(List<Memo> memos) {
      this.memos = memos;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public Date getMemoEffectiveDate() {
      return memoEffectiveDate;
   }

   public void setMemoEffectiveDate(Date memoEffectiveDate) {
      this.memoEffectiveDate = memoEffectiveDate;
   }

   public Date getMemoExpirationDate() {
      return memoExpirationDate;
   }

   public void setMemoExpirationDate(Date memoExpirationDate) {
      this.memoExpirationDate = memoExpirationDate;
   }

   public String getPreviousMemo() {
      return previousMemo;
   }

   public void setPreviousMemo(String previousMemo) {
      this.previousMemo = previousMemo;
   }

   public String getNextMemo() {
      return nextMemo;
   }

   public void setNextMemo(String nextMemo) {
      this.nextMemo = nextMemo;
   }

   public String getMemoText() {
      return memoText;
   }

   public void setMemoText(String memoText) {
      this.memoText = memoText;
   }

   public String getMemoAccessLevel() {
      return memoAccessLevel;
   }

   public void setMemoAccessLevel(String memoAccessLevel) {
      this.memoAccessLevel = memoAccessLevel;
   }
}
