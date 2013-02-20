package com.sigmasys.kuali.ksa.krad.form;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sigmasys.kuali.ksa.model.*;

/**
 * This class serves as a form behind the "Generate 1098T" screen. 
 * 
 * @author Sergey
 */
@SuppressWarnings("serial")
public class Generate1098TForm extends AbstractViewModel {

	private static final String LINE_BREAK = "\n";
	
	/**
	 * Account associated with the 1098T View.
	 */
	private Account account;
	
	/**
	 * A List of reporting years. Multiple selection is allowed. 
	 */
	private List<String> reportYears;
	

	/**
	 * Returns the page header, which defaults to the Account holder's name.
	 * 
	 * @return Page header.
	 */
	public String getPageHeader() {
		return (account != null) && (account.getDefaultPersonName() != null) 
				? account.getDefaultPersonName().getDisplayValue() : "Generate 1098T";
	}
	
	/**
	 * Returns a display value for the default address.
	 * 
	 * @return Default Address display value.
	 */
	public String getDefaultAddressDisplay() {
		String displayValue = "UNKNOWN";
		
		if (account != null) {
			PostalAddress address = account.getDefaultPostalAddress();
			
			if (address != null) {
				StringBuffer sb = new StringBuffer();
				
				// Append street addresses:
				sb.append(address.getStreetAddress1()).append(LINE_BREAK);
				
				if (StringUtils.isNotEmpty(address.getStreetAddress2())) {
					sb.append(address.getStreetAddress2()).append(LINE_BREAK);
				}
				
				if (StringUtils.isNotEmpty(address.getStreetAddress3())) {
					sb.append(address.getStreetAddress3()).append(LINE_BREAK);
				}
				
				// Append City, State, Zip Code:
				sb.append(address.getCity()).append(", ");
				sb.append(address.getState()).append(" ");
				sb.append(address.getPostalCode());
				displayValue = sb.toString();
			}
		}

		return displayValue;
	}
	
	/**
	 * Returns the Default Contact display value.
	 * 
	 * @return	Default Contact display value.
	 */
	public String getDefaultContactDisplay() {
		String displayValue = "UNKNOWN";
		
		if (account != null) {
			ElectronicContact contact = account.getDefaultElectronicContact();
			
			if (contact != null) {
				StringBuffer sb = new StringBuffer();
				
				// Append contact info:
				sb.append("Phone: ").append(contact.getPhoneNumber()).append(LINE_BREAK);
				sb.append("Email: ").append(contact.getEmailAddress());
				displayValue = sb.toString();
			}
		}
		
		return displayValue;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<String> getReportYears() {
		return reportYears;
	}

	public void setReportYears(List<String> reportYears) {
		this.reportYears = reportYears;
	}
}
