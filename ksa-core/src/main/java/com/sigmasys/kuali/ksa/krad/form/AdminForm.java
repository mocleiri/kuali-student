package com.sigmasys.kuali.ksa.krad.form;

import java.util.List;

import org.kuali.rice.krad.keyvalues.KeyValuesFinder;

import com.sigmasys.kuali.ksa.krad.util.*;
import com.sigmasys.kuali.ksa.model.*;

/**
 * Created by: dmulderink on 10/5/12 at 6:55 PM
 */
@SuppressWarnings("all")
public class AdminForm extends AbstractViewModel {
	
	/**
	 * Account for creation or editing.
	 */
	private Account account;
	
	/**
	 * Account information holder object
	 */
	private AccountInformationHolder accountInfo;
	
	/**
	 * Account search information holder. 
	 */
	private AccountSearchInformationHolder accountSearchInfo;
	
	/**
	 * Account search results.
	 */
	private List<AccountSearchResultCollectionLine> accountSearchResults;
	
	/*
	 * Option KeyValueFinders.
	 */
	private KeyValuesFinder accountStatusTypeOptionsFinder;
	private KeyValuesFinder latePeriodOptionsFinder;
	private KeyValuesFinder bankTypeOptionsFinder;
	private KeyValuesFinder taxTypeOptionsFinder;
	private KeyValuesFinder idTypeKeyValuesFinder;
	private KeyValuesFinder searchResultFieldsOptionsFinder;
	
    private List<GlTransaction> glTransactions;

   /*
     Activities
   */

   // a list of activities
   private List<Activity> activities;

   /*
     Get / Set methods
   */

   	public AccountSearchInformationHolder getAccountSearchInfo() {
		return accountSearchInfo;
	}
	
	public void setAccountSearchInfo(AccountSearchInformationHolder accountSearchInfo) {
		this.accountSearchInfo = accountSearchInfo;
	}

	public KeyValuesFinder getAccountStatusTypeOptionsFinder() {
		return accountStatusTypeOptionsFinder;
	}
	
	public void setAccountStatusTypeOptionsFinder(
			KeyValuesFinder accountStatusTypeOptionsFinder) {
		this.accountStatusTypeOptionsFinder = accountStatusTypeOptionsFinder;
	}

	public KeyValuesFinder getLatePeriodOptionsFinder() {
   		return latePeriodOptionsFinder;
	}
	
	public void setLatePeriodOptionsFinder(KeyValuesFinder latePeriodOptionsFinder) {
		this.latePeriodOptionsFinder = latePeriodOptionsFinder;
	}
	
	public KeyValuesFinder getBankTypeOptionsFinder() {
		return bankTypeOptionsFinder;
	}
	
	public void setBankTypeOptionsFinder(KeyValuesFinder bankTypeOptionsFinder) {
		this.bankTypeOptionsFinder = bankTypeOptionsFinder;
	}
	
	public KeyValuesFinder getTaxTypeOptionsFinder() {
		return taxTypeOptionsFinder;
	}
	
	public void setTaxTypeOptionsFinder(KeyValuesFinder taxTypeOptionsFinder) {
		this.taxTypeOptionsFinder = taxTypeOptionsFinder;
	}
	
	public KeyValuesFinder getIdTypeKeyValuesFinder() {
		return idTypeKeyValuesFinder;
	}
	
	public void setIdTypeKeyValuesFinder(KeyValuesFinder idTypeKeyValuesFinder) {
		this.idTypeKeyValuesFinder = idTypeKeyValuesFinder;
	}

	public List<Activity> getActivities() {
   		return activities;
   	}

   	public void setActivities(List<Activity> activities) {
   		this.activities = activities;
   	}

	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public AccountInformationHolder getAccountInfo() {
		return accountInfo;
	}
	
	public void setAccountInfo(AccountInformationHolder accountInfo) {
		this.accountInfo = accountInfo;
	}

    public KeyValuesFinder getSearchResultFieldsOptionsFinder() {
		return searchResultFieldsOptionsFinder;
	}

	public void setSearchResultFieldsOptionsFinder(
			KeyValuesFinder searchResultFieldsOptionsFinder) {
		this.searchResultFieldsOptionsFinder = searchResultFieldsOptionsFinder;
	}

	public List<GlTransaction> getGlTransactions() {
        return glTransactions;
    }

    public void setGlTransactions(List<GlTransaction> glTransactions) {
        this.glTransactions = glTransactions;
    }

    public List<AccountSearchResultCollectionLine> getAccountSearchResults() {
		return accountSearchResults;
	}

	public void setAccountSearchResults(
			List<AccountSearchResultCollectionLine> accountSearchResults) {
		this.accountSearchResults = accountSearchResults;
	}
}
