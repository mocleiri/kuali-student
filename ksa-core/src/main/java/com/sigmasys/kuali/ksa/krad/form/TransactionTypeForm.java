package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.AuditableEntityModel;
import com.sigmasys.kuali.ksa.krad.model.TransactionTypeModel;
import com.sigmasys.kuali.ksa.model.*;

import java.util.ArrayList;
import java.util.List;

public class TransactionTypeForm extends AbstractViewModel {

   // Currency stuff

   private Account account;

   private List<TransactionTypeModel> entities;

   private TransactionTypeModel transactionType;

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

    public List<TransactionTypeModel> getTransactionTypes() {
        return entities;
    }

    public <T extends AuditableEntity> void setTransactionTypes(List<T> entities){
        List<TransactionTypeModel> list = new ArrayList<TransactionTypeModel>(entities.size());

        for(AuditableEntity entity : entities){
            TransactionTypeModel m;
            if(entity instanceof TransactionType) {
                m = new TransactionTypeModel((TransactionType)entity);
            } else {
                m = (TransactionTypeModel)entity;
            }
            list.add(m);
        }

        this.entities = list;
    }

    public TransactionTypeModel getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeModel transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = new TransactionTypeModel(transactionType);
    }


    public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
