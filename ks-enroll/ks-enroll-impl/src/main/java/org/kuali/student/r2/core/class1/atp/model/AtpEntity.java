package org.kuali.student.r2.core.class1.atp.model;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.entity.AttributeOwner;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.entity.NameOwner;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.infc.Name;
import org.kuali.student.r2.common.model.StateEntity;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.atp.infc.Atp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KSEN_ATP")
public class AtpEntity extends MetaEntity implements AttributeOwner<AtpAttributeEntity>, NameOwner<AtpNameEntity> {
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<AtpNameEntity> names = new ArrayList<AtpNameEntity>();
    
    @Column(name = "ADMIN_ORG_ID")
    private String adminOrgId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RT_DESCR_ID")
    private AtpRichTextEntity descr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DT")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DT")
    private Date endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ATP_TYPE_ID")
    private AtpTypeEntity atpType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ATP_STATE_ID")
    private StateEntity atpState;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<AtpAttributeEntity> attributes = new ArrayList<AtpAttributeEntity>();


    public AtpEntity() {
    }

    public AtpEntity(Atp atp) {
        super(atp);
        this.setId(atp.getId());
        this.setNames(new ArrayList<AtpNameEntity>());
        if (null != atp.getNames()) {
            for (Name name : atp.getNames()) {
                this.getNames().add(new AtpNameEntity(name));
            }
        }
		this.setAdminOrgId(atp.getAdminOrgId());
        if (atp.getStartDate() != null) {
            this.setStartDate(atp.getStartDate());
        }
        if (atp.getEndDate() != null) {
            this.setEndDate(atp.getEndDate());
        }
        if (atp.getDescr() != null) {
            this.setDescr(new AtpRichTextEntity(atp.getDescr()));
        }
        
        this.setAttributes(new ArrayList<AtpAttributeEntity>());
        if (null != atp.getAttributes()) {
            for (Attribute att : atp.getAttributes()) {
                this.getAttributes().add(new AtpAttributeEntity(att, this));
            }
        }
    }

    public List<AtpNameEntity> getNames() {
        return names;
    }

    public void setNames(List<AtpNameEntity> names) {
        this.names = names;
    }

    public AtpRichTextEntity getDescr() {
        return descr;
    }

    public void setDescr(AtpRichTextEntity descr) {
        this.descr = descr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public AtpTypeEntity getAtpType() {
        return atpType;
    }

    public void setAtpType(AtpTypeEntity atpType) {
        this.atpType = atpType;
    }

    public StateEntity getAtpState() {
        return atpState;
    }

    public void setAtpState(StateEntity atpState) {
        this.atpState = atpState;
    }

    @Override
    public void setAttributes(List<AtpAttributeEntity> attributes) {
        this.attributes = attributes;

    }

    @Override
    public List<AtpAttributeEntity> getAttributes() {
        return attributes;
    }

    public String getAdminOrgId() {
        return adminOrgId;
    }

    public void setAdminOrgId(String adminOrgId) {
        this.adminOrgId = adminOrgId;
    }

    public AtpInfo toDto() {
        AtpInfo atp = new AtpInfo();
        atp.setId(getId());
        List<NameInfo> names = new ArrayList<NameInfo>();
        for (AtpNameEntity name : getNames()) {
            NameInfo nameInfo = name.toDto();
            names.add(nameInfo);
        }
        atp.setNames(names);
        
        atp.setStartDate(startDate);
        atp.setEndDate(endDate);
        atp.setAdminOrgId(getAdminOrgId());
        if (atpType != null)
            atp.setTypeKey(atpType.getId());
        if (atpState != null)
            atp.setStateKey(atpState.getId());
        atp.setMeta(super.toDTO());
        if (descr != null)
            atp.setDescr(descr.toDto());

        List<AttributeInfo> atts = new ArrayList<AttributeInfo>();
        for (AtpAttributeEntity att : getAttributes()) {
            AttributeInfo attInfo = att.toDto();
            atts.add(attInfo);
        }
        atp.setAttributes(atts);

        return atp;
    }
}
