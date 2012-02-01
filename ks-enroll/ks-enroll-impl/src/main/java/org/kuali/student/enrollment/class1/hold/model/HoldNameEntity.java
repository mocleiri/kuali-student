package org.kuali.student.enrollment.class1.hold.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_HOLD_NAME")
public class HoldNameEntity extends NameEntity<HoldEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private HoldEntity owner;
    
    public HoldNameEntity() {

    }
    
    public HoldNameEntity(Name name) {
        super(name);
    }
    
    public HoldNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public HoldNameEntity(Name name, HoldEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(HoldEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public HoldEntity getOwner() {
        return owner;
    }

}

