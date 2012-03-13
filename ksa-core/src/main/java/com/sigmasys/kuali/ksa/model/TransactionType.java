package com.sigmasys.kuali.ksa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
 * TransactionType defines different transaction types existing in KSA.
 * <p/>
 * User: mivanov
 * Date: 1/22/12
 * Time: 3:46 PM
 *
 * @author Michael Ivanov
 */
@Entity
@IdClass(TransactionTypeId.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TransactionType implements Identifiable {

    protected TransactionTypeId id;

    protected Date startDate;

    protected Date endDate;

    protected String description;

    protected List<Tag> tags;

    /**
     * Creator user ID
     */
    private String creatorId;

    /**
     * Editor user ID
     */
    private String editorId;

    /**
     * Timestamp
     */
    private Date lastUpdate;


    /**
     * It needs to be implemented in DebitType and CreditType
     *
     * @return list of tags
     */
    public abstract List<Tag> getTags();


    @Override
    @EmbeddedId
    public TransactionTypeId getId() {
        return id;
    }

    public void setId(TransactionTypeId id) {
        this.id = id;
    }

    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "DEF_TRN_TXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Column(name = "CREATOR_ID")
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "EDITOR_ID")
    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    @Column(name = "LAST_UPDATE")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
