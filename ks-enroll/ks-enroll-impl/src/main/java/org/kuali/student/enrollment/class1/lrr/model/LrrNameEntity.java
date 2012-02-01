package org.kuali.student.enrollment.class1.lrr.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_LRR_NAME")
public class LrrNameEntity extends NameEntity<LearningResultRecordEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private LearningResultRecordEntity owner;
    
    public LrrNameEntity() {
    }
    
    public LrrNameEntity(Name name) {
        super(name);
    }
    
    public LrrNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public LrrNameEntity(Name name, LearningResultRecordEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(LearningResultRecordEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public LearningResultRecordEntity getOwner() {
        return owner;
    }

}
