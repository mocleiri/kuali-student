package com.sigmasys.kuali.ksa.krad.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;

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
	
	/*
	 * Option KeyValueFinders.
	 */
	private KeyValuesFinder accountStatusTypeOptionsFinder;
	private KeyValuesFinder latePeriodOptionsFinder;
	private KeyValuesFinder bankTypeOptionsFinder;
	private KeyValuesFinder taxTypeOptionsFinder;
	private KeyValuesFinder idTypeKeyValuesFinder;
	
	
   /*
     Activities
   */

   // a list of activities
   private List<Activity> activities;

   /*
     Get / Set methods
   */

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


}
