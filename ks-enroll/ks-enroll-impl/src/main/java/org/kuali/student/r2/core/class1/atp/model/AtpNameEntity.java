package org.kuali.student.r2.core.class1.atp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_ATP_NAME")
public class AtpNameEntity extends NameEntity<AtpEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private AtpEntity owner;

    public AtpNameEntity(){
    	
    }
    public AtpNameEntity(Name name) {
        super(name);
    }
    
    public AtpNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public AtpNameEntity(Name name, AtpEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(AtpEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public AtpEntity getOwner() {
        return owner;
    }
}
