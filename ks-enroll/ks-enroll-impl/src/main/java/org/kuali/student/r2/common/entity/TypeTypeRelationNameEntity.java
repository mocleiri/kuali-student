package org.kuali.student.r2.common.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;

@Entity
@Table(name = "KSEN_TYPE_NAME")
public class TypeTypeRelationNameEntity extends NameEntity<TypeTypeRelationEntity>{

    @ManyToOne
    @JoinColumn(name = "OWNER")
    private TypeTypeRelationEntity owner;

    public TypeTypeRelationNameEntity(Name name) {
        super(name);
    }
    
    public TypeTypeRelationNameEntity(String locale, String name) {
        super(locale, name);
    }
    
    public TypeTypeRelationNameEntity(Name name, TypeTypeRelationEntity owner) {
        super(name);
        setOwner(owner);
    }

    @Override
    public void setOwner(TypeTypeRelationEntity owner) {
        this.owner = owner;
        
    }

    @Override
    public TypeTypeRelationEntity getOwner() {
        return owner;
    }
}
