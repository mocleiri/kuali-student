package org.kuali.student.enrollment.class1.lrc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.enrollment.class1.hold.model.HoldEntity;
import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LRR_RES_SOURCE_NAME")
public class ResultScaleNameEntity extends NameEntity<ResultScaleEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private ResultScaleEntity owner;
    
    public ResultScaleNameEntity(Name name) {
        super(name);
    }
    
    public ResultScaleNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public ResultScaleNameEntity(Name name, ResultScaleEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(ResultScaleEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public ResultScaleEntity getOwner() {
        return owner;
    }

}
