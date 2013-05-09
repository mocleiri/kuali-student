package org.kuali.student.r2.core.organization.model;


import org.kuali.student.r2.common.entity.AttributeOwner;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.core.organization.dto.OrgPositionRestrictionInfo;
import org.kuali.student.r2.core.organization.infc.OrgPositionRestriction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="KSEN_ORG_PERS_RELTN")
public class OrgPositionRestrictionEntity extends MetaEntity implements AttributeOwner<OrgPositionRestrictionAttributeEntity> {
    @Column(name = "TYPE")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER, orphanRemoval = true)
    private final Set<OrgPositionRestrictionAttributeEntity> attributes = new HashSet<OrgPositionRestrictionAttributeEntity>();

    public  OrgPositionRestrictionEntity() { }

    public OrgPositionRestrictionEntity(OrgPositionRestriction orgPositionRestriction) {
        //BOOKMARK TODO
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Set<OrgPositionRestrictionAttributeEntity> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(Set<OrgPositionRestrictionAttributeEntity> attributes) {
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.addAll(attributes);
        }
    }

    public void fromDto(OrgPositionRestriction orgPositionRestriction) {
        //BOOKMARK TODO
    }

    public OrgPositionRestrictionInfo toDto() {
        //BOOKMARK TODO
        return null;
    }
}
