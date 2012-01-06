package org.kuali.student.r2.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.kuali.student.common.entity.KSEntityConstants;

@Entity
@AttributeOverrides({
@AttributeOverride(name="id", column=@Column(name="TYPE_KEY"))})
public class BaseTypeEntity extends BaseVersionEntity implements NameOwner<TypeNameEntity> {
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<TypeNameEntity> names = new ArrayList<TypeNameEntity>();
	
	@Column(name = "TYPE_DESC",length=KSEntityConstants.LONG_TEXT_LENGTH)
	private String descr;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFF_DT")
	private Date effectiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIR_DT")
	private Date expirationDate;

	public List<TypeNameEntity> getNames() {
		return names;
	}

	public void setNames(List<TypeNameEntity> names) {
		this.names = names;
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

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}
