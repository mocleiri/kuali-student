package org.kuali.student.enrollment.class1.lrc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.enrollment.class1.hold.model.HoldEntity;
import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LRC_RES_VALUE_NAME")
public class ResultValueNameEntity extends NameEntity<ResultValueEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private ResultValueEntity owner;
    
    public ResultValueNameEntity() {

    }
    
    public ResultValueNameEntity(Name name) {
        super(name);
    }
    
    public ResultValueNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public ResultValueNameEntity(Name name, ResultValueEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(ResultValueEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public ResultValueEntity getOwner() {
        return owner;
    }

}
