package org.kuali.student.enrollment.class1.lrr.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LRR_RES_SOURCE_NAME")
public class ResultSourceNameEntity extends NameEntity<ResultSourceEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private ResultSourceEntity owner;
    
    public ResultSourceNameEntity(Name name) {
        super(name);
    }
    
    public ResultSourceNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public ResultSourceNameEntity(Name name, ResultSourceEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(ResultSourceEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public ResultSourceEntity getOwner() {
        return owner;
    }
}