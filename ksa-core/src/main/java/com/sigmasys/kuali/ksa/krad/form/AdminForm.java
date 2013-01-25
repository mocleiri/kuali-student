package com.sigmasys.kuali.ksa.krad.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;

import com.sigmasys.kuali.ksa.gwt.client.view.widget.value.DateRangeValue;
import com.sigmasys.kuali.ksa.krad.util.AccountSearchResultCollectionLine;
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
	private AccountInformation accountInfo;
	
	/**
	 * Account's user preferences.
	 */
	private List<UserPreference> accountUserPreferences = new ArrayList<UserPreference>();
	
	/**
	 * Account search information holder. 
	 */
	private AccountSearchInformation accountSearchInfo;
	
	/**
	 * Account search results.
	 */
	private List<AccountSearchResultCollectionLine> accountSearchResults;
	
	/*
	 * Option KeyValueFinders.
	 */
	private KeyValuesFinder nameTypeOptionsFinder;
	private KeyValuesFinder addressTypeOptionsFinder;
	private KeyValuesFinder emailTypeOptionsFinder;
	private KeyValuesFinder phoneTypeOptionsFinder;
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

   	public AccountSearchInformation getAccountSearchInfo() {
		return accountSearchInfo;
	}
	
	public void setAccountSearchInfo(AccountSearchInformation accountSearchInfo) {
		this.accountSearchInfo = accountSearchInfo;
	}

	public KeyValuesFinder getNameTypeOptionsFinder() {
		return nameTypeOptionsFinder;
	}
	
	public void setNameTypeOptionsFinder(KeyValuesFinder nameTypeOptionsFinder) {
		this.nameTypeOptionsFinder = nameTypeOptionsFinder;
	}
	
	public KeyValuesFinder getAddressTypeOptionsFinder() {
		return addressTypeOptionsFinder;
	}
	
	public void setAddressTypeOptionsFinder(KeyValuesFinder addressTypeOptionsFinder) {
		this.addressTypeOptionsFinder = addressTypeOptionsFinder;
	}
	
	public KeyValuesFinder getEmailTypeOptionsFinder() {
		return emailTypeOptionsFinder;
	}
	
	public void setEmailTypeOptionsFinder(KeyValuesFinder emailTypeOptionsFinder) {
		this.emailTypeOptionsFinder = emailTypeOptionsFinder;
	}
	
	public KeyValuesFinder getPhoneTypeOptionsFinder() {
		return phoneTypeOptionsFinder;
	}
	
	public void setPhoneTypeOptionsFinder(KeyValuesFinder phoneTypeOptionsFinder) {
		this.phoneTypeOptionsFinder = phoneTypeOptionsFinder;
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

	public List<UserPreference> getAccountUserPreferences() {
   		return accountUserPreferences;
   	}

   	public void setAccountUserPreferences(
   			List<UserPreference> accountUserPreferences) {
   		this.accountUserPreferences = accountUserPreferences;
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
	
	public AccountInformation getAccountInfo() {
		return accountInfo;
	}
	
	public void setAccountInfo(AccountInformation accountInfo) {
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

	/**
	 * Account Biographic information container class.
	 * 
	 * @author Sergey
	 *
	 */
	public static class AccountInformation implements Serializable {
		private PersonName name;
		private PostalAddress address;
		private ElectronicContact electronicContact;
		private String accountType;
		private Date dateOfBirth;
		private BankType bankType;
		private TaxType taxType;
		private IdType idType;
		private AccountStatusType statusType;
		private LatePeriod latePeriod;
		private Boolean ableToAuthenticate;
		private AccountProtectedInfo accountProtectedInfo;
		private BigDecimal creditLimit;
		
		public IdType getIdType() {
			return idType;
		}
		public void setIdType(IdType idType) {
			this.idType = idType;
		}
		public AccountProtectedInfo getAccountProtectedInfo() {
			return accountProtectedInfo;
		}
		public void setAccountProtectedInfo(AccountProtectedInfo accountProtectedInfo) {
			this.accountProtectedInfo = accountProtectedInfo;
		}
		public BigDecimal getCreditLimit() {
			return creditLimit;
		}
		public void setCreditLimit(BigDecimal creditLimit) {
			this.creditLimit = creditLimit;
		}
		public AccountStatusType getStatusType() {
			return statusType;
		}
		public void setStatusType(AccountStatusType statusType) {
			this.statusType = statusType;
		}
		public LatePeriod getLatePeriod() {
			return latePeriod;
		}
		public void setLatePeriod(LatePeriod latePeriod) {
			this.latePeriod = latePeriod;
		}
		public Boolean getAbleToAuthenticate() {
			return ableToAuthenticate;
		}
		public void setAbleToAuthenticate(Boolean ableToAuthenticate) {
			this.ableToAuthenticate = ableToAuthenticate;
		}
		public PersonName getName() {
			return name;
		}
		public void setName(PersonName name) {
			this.name = name;
		}
		public PostalAddress getAddress() {
			return address;
		}
		public void setAddress(PostalAddress address) {
			this.address = address;
		}
		public ElectronicContact getElectronicContact() {
			return electronicContact;
		}
		public void setElectronicContact(ElectronicContact electronicContact) {
			this.electronicContact = electronicContact;
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public Date getDateOfBirth() {
			return dateOfBirth;
		}
		public void setDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
		public BankType getBankType() {
			return bankType;
		}
		public void setBankType(BankType bankType) {
			this.bankType = bankType;
		}
		public TaxType getTaxType() {
			return taxType;
		}
		public void setTaxType(TaxType taxType) {
			this.taxType = taxType;
		}
	}

	/**
	 * Account Search information holder.
	 * 
	 * @author Sergey
	 *
	 */
	public static class AccountSearchInformation implements Serializable {
		private String accountId;
		private String creatorId;
		private String editorId;
		private Boolean isKimAccount;
		private UserPreference userPreference;
		private DateRangeValue dobDateRange;
		private DateRangeValue creationDateRange;
		private DateRangeValue lastUpdateDateRange;
		private Boolean lastNameSubstringSearch;
		private List<String> searchResultFields;
		
		
		public DateRangeValue getDobDateRange() {
			return dobDateRange;
		}
		public void setDobDateRange(DateRangeValue dobDateRange) {
			this.dobDateRange = dobDateRange;
		}
		public DateRangeValue getCreationDateRange() {
			return creationDateRange;
		}
		public void setCreationDateRange(DateRangeValue creationDateRange) {
			this.creationDateRange = creationDateRange;
		}
		public DateRangeValue getLastUpdateDateRange() {
			return lastUpdateDateRange;
		}
		public void setLastUpdateDateRange(DateRangeValue lastUpdateDateRange) {
			this.lastUpdateDateRange = lastUpdateDateRange;
		}
		public String getAccountId() {
			return accountId;
		}
		public void setAccountId(String accountId) {
			this.accountId = accountId;
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
		public Boolean getIsKimAccount() {
			return isKimAccount;
		}
		public void setIsKimAccount(Boolean isKimAccount) {
			this.isKimAccount = isKimAccount;
		}
		public UserPreference getUserPreference() {
			return userPreference;
		}
		public void setUserPreference(UserPreference userPreference) {
			this.userPreference = userPreference;
		}
		public Boolean getLastNameSubstringSearch() {
			return lastNameSubstringSearch;
		}
		public void setLastNameSubstringSearch(Boolean lastNameSubstringSearch) {
			this.lastNameSubstringSearch = lastNameSubstringSearch;
		}
		public List<String> getSearchResultFields() {
			return searchResultFields;
		}
		public void setSearchResultFields(List<String> searchResultFields) {
			this.searchResultFields = searchResultFields;
		}
	}

}
