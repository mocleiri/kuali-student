package org.kuali.student.enrollment.class1.lpr.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.enrollment.class1.hold.model.HoldEntity;
import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LPR_TRANS_NAME")
public class LprTransNameEntity extends NameEntity<LprTransactionEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private LprTransactionEntity owner;
    
    public LprTransNameEntity(Name name) {
        super(name);
    }
    
    public LprTransNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public LprTransNameEntity(Name name, LprTransactionEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(LprTransactionEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public LprTransactionEntity getOwner() {
        return owner;
    }
}
