package com.sigmasys.kuali.ksa.krad.model;

import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.GeneralLedgerType;
import com.sigmasys.kuali.ksa.model.Rollup;

import java.util.Date;

/**
 * User: timb
 * Date: 11/29/12
 * Time: 11:43 AM
 */
public class AuditableEntityModel extends AuditableEntity {
    private AuditableEntity parentEntity;

    public AuditableEntityModel(AuditableEntity entity){
        setAuditableEntity(entity);
    }

    public void setAuditableEntity(AuditableEntity entity){
        parentEntity = entity;
    }

    public AuditableEntity getParentEntity(){
        return parentEntity;
    }

    public Currency getCurrency(){
        if(parentEntity instanceof Currency){
            return (Currency)parentEntity;
        }
        return null;
    }

    public GeneralLedgerType getGeneralLedgerType(){
        if(parentEntity instanceof GeneralLedgerType){
            return (GeneralLedgerType)parentEntity;
        }
        return null;
    }

    public Rollup getRollup(){
        if(parentEntity instanceof Rollup){
            return (Rollup)parentEntity;
        }
        return null;
    }

    @Override
    public Long getId() {
        return parentEntity.getId();
    }

    @Override
    public void setId(Long id) {
        parentEntity.setId(id);
    }

    @Override
    public String getCode() {
        return parentEntity.getCode();
    }

    @Override
    public void setCode(String code) {
        parentEntity.setCode(code);
    }

    @Override
    public String getName() {
        return parentEntity.getName();
    }

    @Override
    public void setName(String name) {
        parentEntity.setName(name);
    }

    @Override
    public String getCreatorId() {
        return parentEntity.getCreatorId();
    }

    @Override
    public void setCreatorId(String creatorId) {
        parentEntity.setCreatorId(creatorId);
    }

    @Override
    public String getEditorId() {
        return parentEntity.getEditorId();
    }

    @Override
    public void setEditorId(String editorId) {
        parentEntity.setEditorId(editorId);
    }

    @Override
    public Date getLastUpdate() {
        return parentEntity.getLastUpdate();
    }

    @Override
    public void setLastUpdate(Date lastUpdate) {
        parentEntity.setLastUpdate(lastUpdate);
    }

    @Override
    public String getDescription() {
        return parentEntity.getDescription();
    }

    @Override
    public void setDescription(String description) {
        parentEntity.setDescription(description);
    }

    @Override
    public Date getCreationDate() {
        return parentEntity.getCreationDate();
    }

    @Override
    public void setCreationDate(Date creationDate) {
        parentEntity.setCreationDate(creationDate);
    }

    @Override
    public String toString() {
        return parentEntity.toString();
    }
}
