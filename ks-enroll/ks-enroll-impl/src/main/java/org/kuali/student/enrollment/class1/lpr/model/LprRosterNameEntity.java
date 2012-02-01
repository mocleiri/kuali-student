package org.kuali.student.enrollment.class1.lpr.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.enrollment.class1.hold.model.HoldEntity;
import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LPR_ROSTER_NAME")
public class LprRosterNameEntity extends NameEntity<LprRosterEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private LprRosterEntity owner;
    
    public LprRosterNameEntity() {
    }
    
    public LprRosterNameEntity(Name name) {
        super(name);
    }
    
    public LprRosterNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public LprRosterNameEntity(Name name, LprRosterEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(LprRosterEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public LprRosterEntity getOwner() {
        return owner;
    }

}
