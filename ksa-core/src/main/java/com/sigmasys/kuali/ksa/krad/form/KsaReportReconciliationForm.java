package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;

/**
 * Created by: dmulderink on 10/6/12 at 2:23 PM
 */
public class KsaReportReconciliationForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private Account account;

   /*
     Get/Set methods
   */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }
}
