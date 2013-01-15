package com.sigmasys.kuali.ksa.krad.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.kuali.rice.kim.api.identity.Person;

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
	private List<UserPreference> accountUserPreferences;
	
   /*
     Activities
   */

   // a list of activities
   private List<Activity> activities;

   /*
     Get / Set methods
   */

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
		private Person person;
		private PersonName name;
		private PostalAddress address;
		private ElectronicContact electronicContact;
		private String accountType;
		private Date dateOfBirth;
		private BankType bankType;
		private TaxType taxType;
		private AccountStatusType statusType;
		private LatePeriod latePeriod;
		private Boolean ableToAuthenticate;
		
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
		public Person getPerson() {
			return person;
		}
		public void setPerson(Person person) {
			this.person = person;
		}
	}


}
