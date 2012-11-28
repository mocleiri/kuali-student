package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.model.Currency;

import java.util.List;

/**
 * Created by: dmulderink on 10/10/12 at 8:07 PM
 */
public class SettingsForm extends AbstractViewModel {

   // Currency stuff

   private Account account;

   private List<AuditableEntity> entities;

   private AuditableEntity entity;

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

    public List<AuditableEntity> getAuditableEntities() {
        return entities;
    }

    public void setAuditableEntities(List<AuditableEntity> entities) {
        this.entities = entities;
    }

    public AuditableEntity getAuditableEntity() {
        return entity;
    }

    public void setAuditableEntity(AuditableEntity entity) {
        this.entity = entity;
    }

    public List<Currency> getCurrencies() {
      return (List<Currency>)(List<?>)entities;
   }

   public void setCurrencies(List<Currency> currencies) {
      this.entities = (List<AuditableEntity>)(List<?>)currencies;
   }

   public Currency getCurrency() {
      return (Currency)entity;
   }

   public void setCurrency(Currency currency) {
      this.entity = currency;
   }

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
