package org.kuali.student.r2.common.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_STATE_PROCESS_NAME")
public class StateProcessNameEntity extends NameEntity<StateProcessEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private StateProcessEntity owner;

    public StateProcessNameEntity(){
    	
    }
    
    public StateProcessNameEntity(Name name) {
        super(name);
    }
    
    public StateProcessNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public StateProcessNameEntity(Name name, StateProcessEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(StateProcessEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public StateProcessEntity getOwner() {
        return owner;
    }
}
