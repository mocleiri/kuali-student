package org.kuali.student.enrollment.class1.lui.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LUI_NAME")
public class LuiNameEntity extends NameEntity<LuiEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private LuiEntity owner;
    
    public LuiNameEntity() {
    }
    
    public LuiNameEntity(Name name) {
        super(name);
    }
    
    public LuiNameEntity(Name name, LuiEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(LuiEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public LuiEntity getOwner() {
        return owner;
    }

}