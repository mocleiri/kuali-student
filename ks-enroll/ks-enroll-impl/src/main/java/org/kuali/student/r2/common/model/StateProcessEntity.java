package org.kuali.student.r2.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.dto.StateProcessInfo;
import org.kuali.student.r2.common.entity.AttributeOwner;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.entity.NameOwner;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.infc.Name;
import org.kuali.student.r2.common.infc.StateProcess;

@Entity
@Table(name = "KSEN_STATE_PROCESS")
public class StateProcessEntity extends MetaEntity implements AttributeOwner<StateAttributeEntity>, NameOwner<StateProcessNameEntity> {
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<StateProcessNameEntity> names = new ArrayList<StateProcessNameEntity>();

    @Column(name="DESCR")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFF_DT")
    private Date effectiveDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIR_DT")
    private Date expirationDate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<StateAttributeEntity> attributes;
    
	public List<StateProcessNameEntity> getNames() {
		return names;
	}

	public void setNames(List<StateProcessNameEntity> names) {
		this.names = names;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public List<StateAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<StateAttributeEntity> attributes) {
		this.attributes = attributes;
	}
	
	public StateProcessEntity(){}

	public StateProcessEntity(StateProcess process){
		super();
		try{
			this.setId(process.getKey());
			this.setNames(new ArrayList<StateProcessNameEntity>());
	        if (null != process.getNames()) {
	            for (Name name : process.getNames()) {
	                this.getNames().add(new StateProcessNameEntity(name));
	            }
	        }
			this.setDescription(process.getDescr());
			this.setVersionNumber((long) 0);
			this.setEffectiveDate(process.getEffectiveDate());
	        this.setExpirationDate(process.getExpirationDate());
			this.setAttributes(new ArrayList<StateAttributeEntity>());
			if(null != process.getAttributes()){
				for (Attribute att : process.getAttributes()) {
					StateAttributeEntity attEntity = new StateAttributeEntity(att);
		            this.getAttributes().add(attEntity);
		        }				
			}
		} catch (Exception e){
            e.printStackTrace();
        }		
	}
	
	public StateProcessInfo toDto(){
		StateProcessInfo process = StateProcessInfo.newInstance();
		process.setKey(getId());
		List<NameInfo> names = new ArrayList<NameInfo>();
        for (StateProcessNameEntity name : getNames()) {
            NameInfo nameInfo = name.toDto();
            names.add(nameInfo);
        }
        process.setNames(names);
		process.setDescr(description);
		process.setEffectiveDate(effectiveDate);
		process.setExpirationDate(expirationDate);
		
        List<AttributeInfo> atts = new ArrayList<AttributeInfo>();
        for (StateAttributeEntity att : getAttributes()) {
            AttributeInfo attInfo = att.toDto();
            atts.add(attInfo);
        }
        process.setAttributes(atts);
        
        return process;
	}
}
