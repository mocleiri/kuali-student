package com.sigmasys.kuali.ksa.krad.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.sigmasys.kuali.ksa.krad.form.AdminForm;
import com.sigmasys.kuali.ksa.model.*;

@Controller
@RequestMapping(value = "/accountManagement")
@Transactional
public class AccountManagementController extends GenericSearchController {

	
	/**
	 * Creates an initial form, which is the Admin page form.
	 */
	@Override
	protected UifFormBase createInitialForm(HttpServletRequest request) {
		return new AdminForm();
	}
	
	/**
	 * Handles creation of a new form for a new personal account.
	 * 
	 * @param form		Admin form.
	 * @param request	HTTP Servlet request.
	 * @return	The new form with a new personal account.
	 */
	@RequestMapping(method = RequestMethod.GET, params = "methodToCall=newPersonAccount")
	public ModelAndView newPersonAccount(@ModelAttribute("KualiForm") AdminForm form, HttpServletRequest request) {
		// Nullify the Account to make sure a new one is being created:
		form.setAccount(null);
		
		// Create a new AccountInformation holder object:
		AdminForm.AccountInformation accountInfo = new AdminForm.AccountInformation();
		PersonName name = new PersonName();
		PostalAddress address = new PostalAddress();
		ElectronicContact electronicContact = new ElectronicContact();
		BankType bankType = new BankType();
		TaxType taxType = new TaxType();
		String accountType = "CA";
		PersonService personService = KimApiServiceLocator.getPersonService();
        Person person = personService.getPersonByPrincipalName("admin1");
        AccountStatusType statusType = new AccountStatusType();
        LatePeriod latePeriod = new LatePeriod();
		
		accountInfo.setName(name);
		accountInfo.setAddress(address);
		accountInfo.setElectronicContact(electronicContact);
		accountInfo.setDateOfBirth(new Date());
		accountInfo.setBankType(bankType);
		accountInfo.setTaxType(taxType);
		accountInfo.setAccountType(accountType);
		accountInfo.setPerson(person);
		statusType.setName("A");
		latePeriod.setName("30");
		accountInfo.setStatusType(statusType);
		accountInfo.setLatePeriod(latePeriod);
		accountInfo.setAbleToAuthenticate(Boolean.TRUE);
		form.setAccountInfo(accountInfo);
		
		return getUIFModelAndView(form);
	}

}
