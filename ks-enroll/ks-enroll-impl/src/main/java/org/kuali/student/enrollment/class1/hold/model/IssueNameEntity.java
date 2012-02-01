package org.kuali.student.enrollment.class1.hold.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_ISSUE_NAME")
public class IssueNameEntity extends NameEntity<IssueEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private IssueEntity owner;
    
    public IssueNameEntity() {
    }
    
    public IssueNameEntity(Name name) {
        super(name);
    }
    
    public IssueNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public IssueNameEntity(Name name, IssueEntity owner) {
        super(name);
        setOwner(owner);
    }
    
    @Override
    public void setOwner(IssueEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public IssueEntity getOwner() {
        return owner;
    }

}
