package org.kuali.student.r2.common.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_TYPE_NAME")
public class TypeNameEntity extends NameEntity<BaseTypeEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private BaseTypeEntity owner;

    public TypeNameEntity(Name name) {
        super(name);
    }
    
    public TypeNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public TypeNameEntity(Name name, BaseTypeEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(BaseTypeEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public BaseTypeEntity getOwner() {
        return owner;
    }
}
