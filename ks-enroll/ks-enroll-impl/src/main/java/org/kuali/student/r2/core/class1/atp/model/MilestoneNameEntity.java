package org.kuali.student.r2.core.class1.atp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_MSTONE_NAME")
public class MilestoneNameEntity extends NameEntity<MilestoneEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private MilestoneEntity owner;
    
    public MilestoneNameEntity(Name name) {
        super(name);
    }
    
    public MilestoneNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public MilestoneNameEntity(Name name, MilestoneEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(MilestoneEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public MilestoneEntity getOwner() {
        return owner;
    }
}
