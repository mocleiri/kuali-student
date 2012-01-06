package org.kuali.student.r2.core.class1.atp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.entity.AttributeOwner;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.entity.NameOwner;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.infc.Name;
import org.kuali.student.r2.common.model.StateEntity;
import org.kuali.student.r2.core.atp.dto.MilestoneInfo;
import org.kuali.student.r2.core.atp.infc.Milestone;

@Entity
@Table(name = "KSEN_MSTONE")
public class MilestoneEntity extends MetaEntity implements AttributeOwner<MilestoneAttributeEntity>, NameOwner<MilestoneNameEntity> {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<MilestoneNameEntity> names = new ArrayList<MilestoneNameEntity>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "RT_DESCR_ID")
    private AtpRichTextEntity descr;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DT")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DT")
    private Date endDate;
    
    @ManyToOne
    @JoinColumn(name = "MILESTONE_TYPE_ID")
    private AtpTypeEntity atpType;

    @ManyToOne
    @JoinColumn(name = "MILESTONE_STATE_ID")
    private StateEntity atpState;
    
    @Column(name="IS_ALL_DAY")
    private boolean isAllDay;
    
    @Column(name="IS_DATE_RANGE")
    private boolean isDateRange;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<MilestoneAttributeEntity> attributes;

    public MilestoneEntity() {
    }

    public MilestoneEntity(Milestone milestone) {
        super(milestone);
        this.setId(milestone.getId());
        this.setAllDay(milestone.getIsAllDay());
        this.setDateRange(milestone.getIsDateRange());
        this.setDescr(new AtpRichTextEntity(milestone.getDescr()));
        StateEntity state = new StateEntity();
        state.setId(milestone.getStateKey());
        this.setAtpState(state);
        AtpTypeEntity type = new AtpTypeEntity();
        type.setId(milestone.getTypeKey());
        this.setAtpType(type);
        this.setNames(new ArrayList<MilestoneNameEntity>());
        if (null != milestone.getNames()) {
            for (Name name : milestone.getNames()) {
                this.getNames().add(new MilestoneNameEntity(name));
            }
        }
        
        this.descr = null != milestone.getDescr() ? new AtpRichTextEntity(milestone.getDescr()) : null;
        this.startDate = null != milestone.getStartDate() ? new Date(milestone.getStartDate().getTime()) : null;
        this.endDate = null != milestone.getEndDate() ? new Date(milestone.getEndDate().getTime()) : null;
        
        this.setAttributes(new ArrayList<MilestoneAttributeEntity>());
        if (null != milestone.getAttributes()) {
            for (Attribute att : milestone.getAttributes()) {
                this.getAttributes().add(new MilestoneAttributeEntity(att, this));
            }
        }
    }
    
    public List<MilestoneNameEntity> getNames() {
        return names;
    }

    public void setNames(List<MilestoneNameEntity> names) {
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

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    public boolean isDateRange() {
        return isDateRange;
    }

    public void setDateRange(boolean isDateRange) {
        this.isDateRange = isDateRange;
    }

    @Override
    public void setAttributes(List<MilestoneAttributeEntity> attributes) {
       this.attributes = attributes;
        
    }

    @Override
    public List<MilestoneAttributeEntity> getAttributes() {
        return attributes;
    }
    
    public MilestoneInfo toDto() {
        MilestoneInfo info = new MilestoneInfo();
        
        info.setId(getId());
        List<NameInfo> names = new ArrayList<NameInfo>();
        for (MilestoneNameEntity name : getNames()) {
            NameInfo nameInfo = name.toDto();
            names.add(nameInfo);
        }
        info.setNames(names);
        
        info.setTypeKey(null != atpType ? atpType.getId() : null);
        info.setStateKey(null != atpState ? atpState.getId() : null);
        info.setStartDate(getStartDate());
        info.setEndDate(getEndDate());
        info.setIsAllDay(isAllDay());
        info.setIsDateRange(isDateRange());
        info.setMeta(super.toDTO());
        info.setDescr((getDescr()!= null) ? getDescr().toDto() : null);
        
        if(getAttributes() != null) {
            List<AttributeInfo> atts = new ArrayList<AttributeInfo>(getAttributes().size());
            for (MilestoneAttributeEntity att : getAttributes()) {
                AttributeInfo attInfo = att.toDto();
                atts.add(attInfo);
            }
            
            info.setAttributes(atts);
        }
        
        return info;
    }

}
