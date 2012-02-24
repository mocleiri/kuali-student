package com.sigmasys.kuali.ksa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Currency model.
 *
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
@Entity
@Table(name="KSSA_CURRENCY")
public class Currency {

    /**
     * Currency ID
     */
	@Id
	@Column(name = "ID", nullable = false, unique = true, updatable = false)
    private String id;

    /**
     * Currency name
     */
	@Column(name = "NAME")
    private String name;
	
	/**
     * Creator user ID
     */
	@Column(name = "CREATOR_ID")
    private String creatorId;

	/**
     * Editor user ID
     */
	@Column(name = "EDITOR_ID")
    private String editorId;
	
	/**
     * Editor user ID
     */
	@Column(name = "LAST_UPDATE")
    private Date lastUpdate;
   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getEditorId() {
		return editorId;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
