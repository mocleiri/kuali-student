package com.sigmasys.kuali.ksa.model;

/**
 * Created by: dmulderink on 9/11/12 at 12:39 PM
 */
public enum SearchTypeValue {

   // short cut for the search type key which is the key_code
   ACCOUNTS,
   ALERT_FLAG,
   MEMO;

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
