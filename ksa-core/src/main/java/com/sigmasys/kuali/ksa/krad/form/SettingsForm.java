package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Currency;

import java.util.List;

/**
 * Created by: dmulderink on 10/10/12 at 8:07 PM
 */
public class SettingsForm extends AbstractViewModel {

   // Currency stuff

   private Account account;

   private List<Currency> currencies;

   private Currency currency;

   private String statusMessage;

   /*
     Get/Set methods
   */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public List<Currency> getCurrencies() {
      return currencies;
   }

   public void setCurrencies(List<Currency> currencies) {
      this.currencies = currencies;
   }

   public Currency getCurrency() {
      return currency;
   }

   public void setCurrency(Currency currency) {
      this.currency = currency;
   }

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
