package org.kuali.student.enrollment.class1.hold.model;

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
import org.kuali.student.r2.core.hold.dto.HoldInfo;
import org.kuali.student.r2.core.hold.infc.Hold;

@Entity
@Table(name = "KSEN_HOLD")
public class HoldEntity extends MetaEntity implements AttributeOwner<HoldAttributeEntity>, NameOwner<HoldNameEntity>{
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<HoldNameEntity> names = new ArrayList<HoldNameEntity>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RT_DESCR_ID")
    private HoldRichTextEntity descr;   
   
    @ManyToOne(optional=false)
    @JoinColumn(name = "TYPE_ID")
    private HoldTypeEntity holdType;

    @ManyToOne(optional=false)
    @JoinColumn(name = "STATE_ID")
    private StateEntity holdState;
 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFF_DT")
	private Date effectiveDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DT")
    private Date releasedDate;
    
    @Column(name="IS_WARNING")
    private boolean isWarning;
 
    @Column(name="IS_OVERRIDABLE")
    private boolean isOverridable;

    @ManyToOne(optional=false)
    @JoinColumn(name = "ISSUE_ID")
    private IssueEntity issue;
    
    @Column(name = "PERS_ID")
    private String personId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<HoldAttributeEntity> attributes;
    
	@Override
	public void setAttributes(List<HoldAttributeEntity> attributes) {
		this.attributes = attributes;		
	}

	public HoldEntity(){}
	
    public HoldEntity(Hold hold){
        super(hold); 
        try {
	        this.setId(hold.getId());
	        this.setNames(new ArrayList<HoldNameEntity>());
	        if (null != hold.getNames()) {
	            for (Name name : hold.getNames()) {
	                this.getNames().add(new HoldNameEntity(name));
	            }
	        }
	        
	        if (hold.getEffectiveDate() != null)
	        	this.setEffectiveDate(hold.getEffectiveDate());
	        if (hold.getReleasedDate() != null)
	        	this.setReleasedDate(hold.getReleasedDate());
	        this.setWarning(hold.getIsWarning());
	        this.setOverridable(hold.getIsOverridable());
	        this.setPersonId(hold.getPersonId());
	        if(hold.getDescr() != null)
	            this.setDescr(new HoldRichTextEntity(hold.getDescr()));

	        this.setAttributes(new ArrayList<HoldAttributeEntity>());
	        if (null != hold.getAttributes()) {
	            for (Attribute att : hold.getAttributes()) {
	            	HoldAttributeEntity attEntity = new HoldAttributeEntity(att);
	                this.getAttributes().add(attEntity);
	            }
	        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public HoldInfo toDto() {
    	HoldInfo obj = new HoldInfo();
    	obj.setId(getId());
    	List<NameInfo> names = new ArrayList<NameInfo>();
        for (HoldNameEntity name : getNames()) {
            NameInfo nameInfo = name.toDto();
            names.add(nameInfo);
        }
        obj.setNames(names);
        
        obj.setEffectiveDate(effectiveDate);
        obj.setReleasedDate(releasedDate);
        obj.setIsWarning(isWarning);
        obj.setIsOverridable(isOverridable);
        obj.setPersonId(personId);
        if(holdType != null)
            obj.setTypeKey(holdType.getId());
        if(holdState != null)
            obj.setStateKey(holdState.getId());
        if(issue != null)
        	obj.setIssueKey(issue.getId());
        obj.setMeta(super.toDTO());
        if(descr != null)
            obj.setDescr(descr.toDto());

        List<AttributeInfo> atts = new ArrayList<AttributeInfo>();
        for (HoldAttributeEntity att : getAttributes()) {
            AttributeInfo attInfo = att.toDto();
            atts.add(attInfo);
        }
        obj.setAttributes(atts);
        
        return obj;
    }


	public List<HoldNameEntity> getNames() {
		return names;
	}

	public void setNames(List<HoldNameEntity> names) {
		this.names = names;
	}

	public HoldRichTextEntity getDescr() {
		return descr;
	}

	public void setDescr(HoldRichTextEntity descr) {
		this.descr = descr;
	}

	public HoldTypeEntity getHoldType() {
		return holdType;
	}

	public void setHoldType(HoldTypeEntity holdType) {
		this.holdType = holdType;
	}

	public StateEntity getHoldState() {
		return holdState;
	}

	public void setHoldState(StateEntity holdState) {
		this.holdState = holdState;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}

	public boolean isOverridable() {
		return isOverridable;
	}

	public void setOverridable(boolean isOverridable) {
		this.isOverridable = isOverridable;
	}

	public IssueEntity getIssue() {
		return issue;
	}

	public void setIssue(IssueEntity issue) {
		this.issue = issue;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public List<HoldAttributeEntity> getAttributes() {
		 return attributes;
	}
}
