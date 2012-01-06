package org.kuali.student.enrollment.class1.lrc.model;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.entity.AttributeOwner;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.entity.NameOwner;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.infc.Name;
import org.kuali.student.r2.common.model.StateEntity;
import org.kuali.student.r2.lum.lrc.dto.ResultValueInfo;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KSEN_LRC_RES_VALUE")
public class ResultValueEntity extends MetaEntity implements AttributeOwner<ResultValueAttributeEntity>, NameOwner<ResultValueNameEntity>  {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<ResultValueNameEntity> names = new ArrayList<ResultValueNameEntity>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RT_DESCR_ID")
    private ResultValueRichTextEntity descr;

    @Column(name = "RES_SCALE_ID")
    private String resultScaleId;

    @Column(name = "NUMERIC_VALUE")
    private String numericValue;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private LrcTypeEntity type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATE_ID")
    private StateEntity state;

    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<ResultValueAttributeEntity> attributes;

    public ResultValueEntity(){
    }

    public ResultValueEntity(ResultValueInfo dto){
        super(dto);
        this.setNames(new ArrayList<ResultValueNameEntity>());
        if (null != dto.getNames()) {
            for (Name name : dto.getNames()) {
                this.getNames().add(new ResultValueNameEntity(name));
            }
        }
        if (dto.getDescr() != null){
            ResultValueRichTextEntity entityDesc = new ResultValueRichTextEntity(dto.getDescr());
            this.setDescr(entityDesc);
        }
        setId(dto.getKey());
        setEffectiveDate(dto.getEffectiveDate());
        setExpirationDate(dto.getExpirationDate());
        setNumericValue(dto.getNumericValue());
        setResultScaleId(dto.getResultScaleKey());
        setValue(dto.getValue());

        this.setAttributes(new ArrayList<ResultValueAttributeEntity>());
        if (null != dto.getAttributes()) {
            for (Attribute att : dto.getAttributes()) {
                ResultValueAttributeEntity attEntity = new ResultValueAttributeEntity(att);
                this.getAttributes().add(attEntity);
            }
        }

    }

    public String getResultScaleId() {
        return resultScaleId;
    }

    public void setResultScaleId(String resultScaleId) {
        this.resultScaleId = resultScaleId;
    }

    public String getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(String numericValue) {
        this.numericValue = numericValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

     public List<ResultValueNameEntity> getNames() {
        return names;
     }

     public void setNames(List<ResultValueNameEntity> names) {
        this.names = names;
     }

     public ResultValueRichTextEntity getDescr() {
        return descr;
     }

     public void setDescr(ResultValueRichTextEntity descr) {
        this.descr = descr;
     }

    public LrcTypeEntity getType() {
        return type;
    }

    public void setType(LrcTypeEntity type) {
        this.type = type;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    @Override
    public void setAttributes(List<ResultValueAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    @Override
    public List<ResultValueAttributeEntity> getAttributes() {
        return attributes;
    }

    public ResultValueInfo toDto() {
        ResultValueInfo info = new ResultValueInfo();
        info.setKey(getId());
        info.setEffectiveDate(getEffectiveDate());
        info.setExpirationDate(getExpirationDate());
        info.setMeta(super.toDTO());

        if (descr != null) {
            info.setDescr(descr.toDto());
        }

        List<NameInfo> names = new ArrayList<NameInfo>();
        for (ResultValueNameEntity name : getNames()) {
            NameInfo nameInfo = name.toDto();
            names.add(nameInfo);
        }
        info.setNames(names);
        info.setNumericValue(getNumericValue());
        info.setValue(getValue());
        info.setScaleKey(getResultScaleId());

        if (getState() != null){
            info.setStateKey(getState().getId());
        }
        if (getType() != null){
            info.setTypeKey(getType().getId());
        }

        List<AttributeInfo> atts = new ArrayList<AttributeInfo>();
        for (ResultValueAttributeEntity att : getAttributes()) {
            AttributeInfo attInfo = att.toDto();
            atts.add(attInfo);
        }
        info.setAttributes(atts);
        info.setMeta(super.toDTO());
        return info;
    }
}
