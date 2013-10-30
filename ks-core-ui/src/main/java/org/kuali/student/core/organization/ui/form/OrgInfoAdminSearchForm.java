package org.kuali.student.core.organization.ui.form;

import java.util.ArrayList;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.core.organization.ui.form.model.OrgUIModel;
import org.kuali.student.r2.core.organization.dto.OrgInfo;

public class OrgInfoAdminSearchForm extends UifFormBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4703388779356717776L;
	
	private String keywordSearch;
	private String orgId;
	private String selectedOrgId;
	private String typeInfoKey;
	private String stateInfoKey;
	private OrgUIModel selectedOrg;
	private OrgInfo orgInfo;
	private ArrayList<OrgUIModel> OrgUIModel;
		

	/**
     * @return the typeInfoKey
     */
    public String getTypeInfoKey() {
        return typeInfoKey;
    }

    /**
     * @param typeInfoKey the typeInfoKey to set
     */
    public void setTypeInfoKey(String typeInfoKey) {
        this.typeInfoKey = typeInfoKey;
    }

    /**
     * @return the stateInfoKey
     */
    public String getStateInfoKey() {
        return stateInfoKey;
    }

    /**
     * @param stateInfoKey the stateInfoKey to set
     */
    public void setStateInfoKey(String stateInfoKey) {
        this.stateInfoKey = stateInfoKey;
    }

    public ArrayList<OrgUIModel> getOrgUIModel() {
		return OrgUIModel;
	}

	public void setOrgUIModel(
			ArrayList<OrgUIModel> OrgUIModel) {
		this.OrgUIModel = OrgUIModel;
	}

	public String getKeywordSearch() {
		return keywordSearch;
	}

	public void setKeywordSearch(String keywordSearch) {
		this.keywordSearch = keywordSearch;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSelectedOrgId() {
		return selectedOrgId;
	}

	public void setSelectedOrgId(String selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}

	public OrgUIModel getSelectedOrg() {
		return selectedOrg;
	}

	public void setSelectedOrg(
			OrgUIModel selectedOrg) {
		this.selectedOrg = selectedOrg;
	}

	public OrgInfo getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrgInfo orgInfo) {
		this.orgInfo = orgInfo;
	}
	
	
	
	
}