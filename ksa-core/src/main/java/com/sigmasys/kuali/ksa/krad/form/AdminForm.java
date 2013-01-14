package com.sigmasys.kuali.ksa.krad.form;

import java.io.Serializable;
import java.util.List;

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
	
   /*
     Activities
   */

   // a list of activities
   private List<Activity> activities;

   /*
     Get / Set methods
   */

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
	}

}
