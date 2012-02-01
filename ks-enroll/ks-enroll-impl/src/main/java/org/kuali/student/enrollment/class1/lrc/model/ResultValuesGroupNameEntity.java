package org.kuali.student.enrollment.class1.lrc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LRC_RES_VAL_GRP_NAME")
public class ResultValuesGroupNameEntity extends NameEntity<ResultValuesGroupEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private ResultValuesGroupEntity owner;
    
    public ResultValuesGroupNameEntity() {
    }
    
    public ResultValuesGroupNameEntity(Name name) {
        super(name);
    }
    
    public ResultValuesGroupNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public ResultValuesGroupNameEntity(Name name, ResultValuesGroupEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(ResultValuesGroupEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public ResultValuesGroupEntity getOwner() {
        return owner;
    }
}