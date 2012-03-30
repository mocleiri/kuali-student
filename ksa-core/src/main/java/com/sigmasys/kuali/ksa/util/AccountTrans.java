package com.sigmasys.kuali.ksa.util;

/** 
 * User: dmulderink
 * Date: 3/29/12
 * Time: 1:19 PM
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Date;

public class AccountTrans  implements Serializable {
   private static final long serialVersionUID = -7525378097732916411L;

   private Date ledgerDate;
   private String statementText;
   private String externalID;
   private BigDecimal amount;
   private BigDecimal totalAmount;

   private Map<String, Object> remoteFieldValuesMap;

   private List<AccountTrans> subList = new ArrayList<AccountTrans>();

   public AccountTrans() {
      remoteFieldValuesMap = new HashMap<String, Object>();
      remoteFieldValuesMap.put("remoteLedgerDate", "03/25/2012");
      remoteFieldValuesMap.put("remoteStatementText", "Statement");
      remoteFieldValuesMap.put("remoteExternalID", 7889);
      remoteFieldValuesMap.put("remoteAmount", 348.29);
   }

   public AccountTrans(Date ledgerDate, String statementText, String externalID, BigDecimal amount) {
      this.ledgerDate = ledgerDate;
      this.statementText = statementText;
      this.externalID = externalID;
      this.amount = amount;

      remoteFieldValuesMap = new HashMap<String, Object>();
      remoteFieldValuesMap.put("remoteLedgerDate", new Date());
      remoteFieldValuesMap.put("remoteStatementText", "Statement");
      remoteFieldValuesMap.put("remoteExternalID", "2718");
      remoteFieldValuesMap.put("remoteAmount", 46.5);

   }

   /**
    * @return the ledgerDate
    */
   public Date getLedgerDate() {
      return this.ledgerDate;
   }

   /**
    * @param ledgerDate the ledgerDate to set
    */
   public void setLedgerDate(Date ledgerDate) {
      this.ledgerDate = ledgerDate;
   }

   /**
    * @return the statementText
    */
   public String getStatementText() {
      return this.statementText;
   }

   /**
    * @param statementText the statementText to set
    */
   public void setStatementText(String statementText) {
      this.statementText = statementText;
   }

   /**
    * @return the externalID
    */
   public String getExternalID() {
      return this.externalID;
   }

   /**
    * @param externalID the externalID to set
    */
   public void setExternalID(String externalID) {
      this.externalID = externalID;
   }

   /**
    * @return the amount
    */
   public BigDecimal getAmount() {
      if (this.amount != null)
      {
         return this.amount.setScale(5, BigDecimal.ROUND_CEILING);
      }
      return BigDecimal.ZERO;
   }

   /**
    * Calculate the total amount and return the same
    * @return the total amount
    */
   public BigDecimal getTotalAmount() {
      if (this.totalAmount != null)
      {
         return this.totalAmount.setScale(5, BigDecimal.ROUND_CEILING);
      }
      return BigDecimal.ZERO;
   }

   /**
    * Set the calculated total amount
    * @param totalAmount
    */
   public void setTotalAmount(BigDecimal totalAmount) {
      this.totalAmount = totalAmount;
   }

   /**
    * @param amount the amount to set
    */
   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   /**
    * @param subList the subList to set
    */
   public void setSubList(List<AccountTrans> subList) {
      this.subList = subList;
   }

   /**
    * @return the subList
    */
   public List<AccountTrans> getSubList() {
      return subList;
   }

   public Map<String, Object> getRemoteFieldValuesMap() {
      return remoteFieldValuesMap;
   }

   public void setRemoteFieldValuesMap(Map<String, Object> remoteFieldValuesMap) {
      this.remoteFieldValuesMap = remoteFieldValuesMap;
   }


}