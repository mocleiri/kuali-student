package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.TransactionTypeModel;
import com.sigmasys.kuali.ksa.model.*;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionTypeForm extends AbstractViewModel {

   // Currency stuff

   private Account account;

   private List<TransactionTypeModel> entities;

   private TransactionTypeModel transactionType;

   private String statusMessage;


    // Fields used for creating a new transaction type
    private String type;
    private String code;
    private String description;
    private Date startDate;
    private Integer priority;

    // Credit types only
    private Integer clearPeriod;
    private String authorizationText;
    private String unallocatedGLAccount;
    private String unallocatedGLOperation;

    // Debit types only

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

    public List<KeyValue> getTransactionTypeCodes() {
        List<KeyValue> labels = new ArrayList<KeyValue>();

        labels.add(new ConcreteKeyValue("", ""));
        labels.add(new ConcreteKeyValue("C", "Credit"));
        return labels;
    }

    public void reset(){

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getClearPeriod() {
        return clearPeriod;
    }

    public void setClearPeriod(Integer clearPeriod) {
        this.clearPeriod = clearPeriod;
    }

    public String getAuthorizationText() {
        return authorizationText;
    }

    public void setAuthorizationText(String authorizationText) {
        this.authorizationText = authorizationText;
    }

    public String getUnallocatedGLAccount() {
        return unallocatedGLAccount;
    }

    public void setUnallocatedGLAccount(String unallocatedGLAccount) {
        this.unallocatedGLAccount = unallocatedGLAccount;
    }

    public String getUnallocatedGLOperation() {
        return unallocatedGLOperation;
    }

    public void setUnallocatedGLOperation(String unallocatedGLOperation) {
        this.unallocatedGLOperation = unallocatedGLOperation;
    }
}
