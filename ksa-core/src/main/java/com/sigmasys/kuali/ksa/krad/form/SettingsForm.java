package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.AuditableEntityModel;
import com.sigmasys.kuali.ksa.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: dmulderink on 10/10/12 at 8:07 PM
 */
public class SettingsForm extends AbstractViewModel {

   // Currency stuff

   private Account account;

   private List<AuditableEntityModel> entities;

   private AuditableEntityModel entity;

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

    public List<AuditableEntityModel> getAuditableEntities() {
        return entities;
    }

    public <T extends AuditableEntity> void setAuditableEntities(List<T> entities) {
        List<AuditableEntityModel> list = new ArrayList<AuditableEntityModel>(entities.size());

        for(AuditableEntity entity : entities){
            AuditableEntityModel m;
            if(entity instanceof AuditableEntityModel){
                m = (AuditableEntityModel)entity;
            } else {
                m = new AuditableEntityModel(entity);
            }
            list.add(m);
        }

        this.entities = list;
    }

    public AuditableEntityModel getAuditableEntity() {
        return entity;
    }

    public void setAuditableEntity(AuditableEntityModel entity) {
        if(this.entity == null){
            this.entity = entity;
        }
    }


    public <T extends AuditableEntity> void setAuditableEntity(T entity) {
        if(entity instanceof AuditableEntityModel){
            this.entity = (AuditableEntityModel)entity;
        } else {
            this.entity = new AuditableEntityModel(entity);
        }

    }

    public String getType(){
        if(entity == null){ return "null"; }
        return entity.getParentEntity().getClass().getName();
    }

    public String getSimpleType(){
        if(entity == null){ return "null"; }
        return entity.getParentEntity().getClass().getSimpleName();
    }

    public String getHumanType(){
        AuditableEntity parent = entity.getParentEntity();
        if(parent instanceof AccountStatusType) { return "Account Status Type"; }
        else if (parent instanceof ActivityType) { return "Activity Type"; }
        else if(parent instanceof BankType) { return "Bank Type"; }
        else if(parent instanceof Currency) { return "Currency Type"; }
        else if(parent instanceof FlagType) { return "Flag Type"; }
        else if(parent instanceof LatePeriod) { return "Late Period Type"; }
        else if(parent instanceof Rollup){ return "Rollup Type"; }
        else if(parent instanceof TaxType) { return "Tax Type"; }

        return this.getSimpleType();
    }

    public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
