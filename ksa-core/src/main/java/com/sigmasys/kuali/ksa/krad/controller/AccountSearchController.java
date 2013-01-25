package com.sigmasys.kuali.ksa.krad.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.sigmasys.kuali.ksa.gwt.client.view.widget.value.DateRangeValue;
import com.sigmasys.kuali.ksa.krad.form.AdminForm;
import com.sigmasys.kuali.ksa.krad.util.AccountSearchResultFieldsKeyValuesFinder;
import com.sigmasys.kuali.ksa.model.UserPreference;

/**
 * This class handles searches for a personal or organizational Accounts.
 * 
 * @author Sergey
 */
@Controller
@RequestMapping(value = "/accountSearch")
@Transactional
public class AccountSearchController extends AccountManagementController {

	/*
	 * Option value finders.
	 */
	private volatile KeyValuesFinder searchResultFieldsOptionsFinder;
	
	
	
	/**
	 * Handles display of the Search Person Account page.
	 * @param form
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, params = "methodToCall=searchPersonAccount")
	public ModelAndView searchPersonAccount(@ModelAttribute("KualiForm") AdminForm form, HttpServletRequest request) {
		// Populate the form:
		populateForSearchPersonAccount(form);
		
		return getUIFModelAndView(form);
	}
	
	/**
	 * Handles Person Account search and redirection to the search result page.
	 * 
	 * @param form
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=doSearchPersonAccount")
	public ModelAndView doSearchPersonAccount(@ModelAttribute("KualiForm") AdminForm form, HttpServletRequest request) {
		// TODO: Inspect the search parameters
		
		// TODO: Search for Accounts:
		
		// TODO: Prepare the result:
		
		// Navigate to the Search Result page:
		form.setPageId("SearchAccountResultsPage");
		
		return getUIFModelAndView(form);
	}

	
	/* ========================================================================================
	 * 
	 * Select control option finders.
	 * 
	 * ========================================================================================*/

	/*
	 * Returns Search Result Fields option finder.
	 */
	public KeyValuesFinder getSearchResultFieldsOptionsFinder() {
		if (searchResultFieldsOptionsFinder == null) {
			synchronized(this) {
				if (searchResultFieldsOptionsFinder == null) {
					searchResultFieldsOptionsFinder = new AccountSearchResultFieldsKeyValuesFinder();
				}
			}
		}
		
		return searchResultFieldsOptionsFinder;
	}
	

	/* *******************************************************************************************************************
	 * 
	 * Helper methods. 
	 * 
	 * ******************************************************************************************************************/
	
	/*
	 * Populates the specified form for a Search Person Account page.
	 */
	private void populateForSearchPersonAccount(AdminForm form) {
		// Populate for New Person Account first:
		populateForNewPersonAccount(form);
		
		// Add additional Search specific info:
		AdminForm.AccountSearchInformation accountSearchInfo = new AdminForm.AccountSearchInformation();
		
		accountSearchInfo.setIsKimAccount(true);
		accountSearchInfo.setLastNameSubstringSearch(true);
		accountSearchInfo.setDobDateRange(new DateRangeValue());
		accountSearchInfo.setLastUpdateDateRange(new DateRangeValue());
		accountSearchInfo.setCreationDateRange(new DateRangeValue());
		accountSearchInfo.setUserPreference(new UserPreference());
		accountSearchInfo.setSearchResultFields(new ArrayList<String>());
		form.setSearchResultFieldsOptionsFinder(getSearchResultFieldsOptionsFinder());
		form.setAccountSearchInfo(accountSearchInfo);
	}
}
