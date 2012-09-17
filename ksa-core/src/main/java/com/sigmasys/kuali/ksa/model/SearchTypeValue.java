package com.sigmasys.kuali.ksa.model;

/**
 * Created by: dmulderink on 9/11/12 at 12:39 PM
 */
public enum SearchTypeValue {

   // short cut for the search type key which is the key_code
   ACCOUNTS(SearchTypeValue.ACCOUNTS_CODE),
   ALERT_FLAG(SearchTypeValue.ALERT_FLAG_CODE),
   MEMO(SearchTypeValue.MEMO_CODE);

   public static final String ACCOUNTS_CODE = "AO";
   public static final String ALERT_FLAG_CODE = "AF";
   public static final String MEMO_CODE = "MO";

   private String code;

   private SearchTypeValue(String code) {
      this.code = code;
   }

   public String getCode() {
      return code;
   }

   /*
      These are the values
    */
   @Override
   public String toString() {
      switch (this) {
         case ACCOUNTS:
            return "Accounts";
         case ALERT_FLAG:
            return "Alert/Flag";
         case MEMO:
            return "Memo";
      }
      throw new IllegalStateException("No search type description found for " + name() + " value");
   }

}
