package org.kuali.student.enrollment.class1.hold.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_RESTRICTION_NAME")
public class RestrictionNameEntity extends NameEntity<RestrictionEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private RestrictionEntity owner;
    
    public RestrictionNameEntity() {
    }
    
    public RestrictionNameEntity(Name name) {
        super(name);
    }
    
    public RestrictionNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public RestrictionNameEntity(Name name, RestrictionEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(RestrictionEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public RestrictionEntity getOwner() {
        return owner;
    }

}
