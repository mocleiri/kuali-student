package com.sigmasys.kuali.ksa.krad.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.sigmasys.kuali.ksa.krad.form.AdminForm;
import com.sigmasys.kuali.ksa.krad.util.AccountInformationHolder;
import com.sigmasys.kuali.ksa.krad.util.AuditableEntityKeyValuesFinder;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.util.RequestUtils;

@Controller
@RequestMapping(value = "/accountManagement")
@Transactional
public class AccountManagementController extends GenericSearchController {
	
	@Autowired
	private AuditableEntityService auditableEntityService;
	
	@Autowired
	private PersistenceService persistenceService;
	
	@Autowired
	private UserPreferenceService userPreferenceService;
	
	@Autowired
    protected UserSessionManager userSessionManager;

	/*
	 * Option KeyValueFinders.
	 */
	private volatile KeyValuesFinder accountStatusTypeOptionsFinder;
	private volatile KeyValuesFinder latePeriodOptionsFinder;
	private volatile KeyValuesFinder bankTypeOptionsFinder;
	private volatile KeyValuesFinder taxTypeOptionsFinder;
	private volatile KeyValuesFinder idTypeKeyValuesFinder;
	

	

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
		// Populate the form:
		populateForNewPersonAccount(form, false);
		
		return getUIFModelAndView(form);
	}
	
	/**
	 * Cancels the current page and goes back to the landing page.
	 * @param form
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
	public ModelAndView returnToLandingPage(@ModelAttribute("KualiForm") AdminForm form, HttpServletRequest request) {
		// Nullify the Account and Account info:
		form.setAccount(null);
		form.setAccountInfo(null);
		form.setPageId("AccountManagement");
		form.setViewId("AdminView");
		
		return getUIFModelAndView(form);
	}
	
	/**
	 * Handles saving of a new account.
	 * @param form
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=saveNewPersonAccount")
	public ModelAndView saveNewPersonAccount(@ModelAttribute("KualiForm") AdminForm form, HttpServletRequest request) {
		// Get the objects to be persisted:
		Account account = setUpAccountFromForm(form);
		AccountProtectedInfo accountProtectedInfo = form.getAccountInfo().getAccountProtectedInfo();
		List<UserPreference> userPreferences = form.getAccountInfo().getUserPreferences();
		String accountId = account.getId();
		
		// Persist the objects:
		persistenceService.persistEntity(account);
		accountProtectedInfo.setId(accountId);
		persistenceService.persistEntity(accountProtectedInfo);
		
		// Persist UserPreferences:
		for (UserPreference userPreference : userPreferences) {
			userPreference.setAccountId(accountId);
			userPreferenceService.persistUserPreference(userPreference);
		}
		
		// Return to the landing page:
		return returnToLandingPage(form, request);
	}
	
	/**
	 * Handles edit of a person's account.
	 * 
	 * @param form		Admin form.
	 * @param request	HTTP Request.
	 * @return			Page ModelAndView.
	 */
	@RequestMapping(method = RequestMethod.GET, params = "methodToCall=editPersonAccount")
	public ModelAndView editPersonAccount(@ModelAttribute("KualiForm") AdminForm form, @RequestParam("accountId") String accountId) {
		// Find the Account with the given ID:
		Account account = accountService.getFullAccount(accountId);
		
		// Populate the form with the Account details:
		populateForExistingAccount(form, account, false);

		return getUIFModelAndView(form);
	}
	
	
	/* ========================================================================================
	 * 
	 * Select control option finders.
	 * 
	 * ========================================================================================*/


	/*
	 * Returns Account status option finder. 
	 */
	public KeyValuesFinder getAccountStatusTypeOptionsFinder(boolean blankOption) {
		if (accountStatusTypeOptionsFinder == null) {
			synchronized(this) {
				if (accountStatusTypeOptionsFinder == null) {
					accountStatusTypeOptionsFinder = new AuditableEntityKeyValuesFinder<AccountStatusType>(auditableEntityService, AccountStatusType.class, blankOption);
				}
			}
		}
		
		return accountStatusTypeOptionsFinder;
	}

	/*
	 * Returns LatePeriod type option finder. 
	 */
	public KeyValuesFinder getLatePeriodOptionsFinder(boolean blankOption) {
		if (latePeriodOptionsFinder == null) {
			synchronized(this) {
				if (latePeriodOptionsFinder == null) {
					latePeriodOptionsFinder = new AuditableEntityKeyValuesFinder<LatePeriod>(auditableEntityService, LatePeriod.class, blankOption);
				}
			}
		}
		
		return latePeriodOptionsFinder;
	}

	/*
	 * Returns BankType option finder. 
	 */
	public KeyValuesFinder getBankTypeOptionsFinder(boolean blankOption) {
		if (bankTypeOptionsFinder == null) {
			synchronized(this) {
				if (bankTypeOptionsFinder == null) {
					bankTypeOptionsFinder = new AuditableEntityKeyValuesFinder<BankType>(auditableEntityService, BankType.class, blankOption);
				}
			}
		}
		
		return bankTypeOptionsFinder;
	}

	/*
	 * Returns TaxType option finder.
	 */
	public KeyValuesFinder getTaxTypeOptionsFinder(boolean blankOption) {
		if (taxTypeOptionsFinder == null) {
			synchronized(this) {
				if (taxTypeOptionsFinder == null) {
					taxTypeOptionsFinder = new AuditableEntityKeyValuesFinder<TaxType>(auditableEntityService, TaxType.class, blankOption);
				}
			}
		}
		
		return taxTypeOptionsFinder;
	}

	/*
	 * Returns IdentityType option finder.
	 */
	public KeyValuesFinder getIdTypeKeyValuesFinder(boolean blankOption) {
		if (idTypeKeyValuesFinder == null) {
			synchronized(this) {
				if (idTypeKeyValuesFinder == null) {
					idTypeKeyValuesFinder = new AuditableEntityKeyValuesFinder<IdentityType>(auditableEntityService, IdentityType.class, blankOption);
				}
			}
		}
		
		return idTypeKeyValuesFinder;
	}
	
	
	/* *******************************************************************************************************************
	 * 
	 * Helper methods. 
	 * 
	 * ******************************************************************************************************************/
	
	/*
	 * Populates the given form for a New Person Account page.
	 */
	protected void populateForNewPersonAccount(AdminForm form, boolean addBlankOption) {
		// Create a new AccountInformationHolder object:
		AccountInformationHolder accountInfo = new AccountInformationHolder();
		Account account = new AccountWrapper();
		AccountProtectedInfo accountProtectedInfo = new AccountProtectedInfo();
		PersonName personName = new PersonName();
		PostalAddress postalAddress = new PostalAddress();
		ElectronicContact electronicContact = new ElectronicContact();
		
		// Set up the objects:
		accountProtectedInfo.setBankType(new BankType());
		accountProtectedInfo.setIdentityType(new IdentityType());
		accountProtectedInfo.setTaxType(new TaxType());
		personName.setDefault(true);
		postalAddress.setDefault(true);
		electronicContact.setDefault(true);
		account.setPersonNames(new HashSet<PersonName>(Arrays.asList(personName)));
		account.setPostalAddresses(new HashSet<PostalAddress>(Arrays.asList(postalAddress)));
		account.setElectronicContacts(new HashSet<ElectronicContact>(Arrays.asList(electronicContact)));
		account.setCreditLimit(new BigDecimal(0));
		account.setStatusType(new AccountStatusType());
		account.setLatePeriod(new LatePeriod());
		account.setAbleToAuthenticate(Boolean.TRUE);
		accountInfo.setAccountProtectedInfo(accountProtectedInfo);
		accountInfo.setDateOfBirth(new Date());
		accountInfo.setUserPreferences(new ArrayList<UserPreference>());
		accountInfo.setAccountType(AccountTypeValue.DIRECT_CHARGE_CODE);
		
		// Set the form's objects:
		form.setAccount(account);
		form.setAccountInfo(accountInfo);
		
		// Set option finders:
		setUpOptionFinders(form, addBlankOption);
	}
	
	/*
	 * Populates the given form for an existing Account.
	 */
	protected void populateForExistingAccount(AdminForm form, Account account, boolean addBlankOption) {
		// Set the Account attributes:
		String accountId = account.getId();
		AccountInformationHolder accountInfo = new AccountInformationHolder();
		AccountProtectedInfo accountProtectedInfo = accountService.getAccountProtectedInfo(accountId);
		List<UserPreference> userPreferences = userPreferenceService.getUserPreferences(accountId);
		
		accountInfo.setAccountProtectedInfo(accountProtectedInfo);
		accountInfo.setDateOfBirth(getAccountDateOfBirth(account));
		accountInfo.setUserPreferences(userPreferences);
		accountInfo.setAccountType(getAccountType(account).getCode());
		
		// Set the form's objects:
		form.setAccountInfo(accountInfo);
		form.setAccount(account);
		
		// Set option finders:
		setUpOptionFinders(form, addBlankOption);
	}
	
	/*
	 * Sets ups form's Select controls (drop-down list) option finders.
	 */
	protected void setUpOptionFinders(AdminForm form, boolean addBlankOption) {
		form.setLatePeriodOptionsFinder(getLatePeriodOptionsFinder(addBlankOption));
		form.setBankTypeOptionsFinder(getBankTypeOptionsFinder(addBlankOption));
		form.setTaxTypeOptionsFinder(getTaxTypeOptionsFinder(addBlankOption));
		form.setIdTypeKeyValuesFinder(getIdTypeKeyValuesFinder(addBlankOption));
		form.setAccountStatusTypeOptionsFinder(getAccountStatusTypeOptionsFinder(addBlankOption));
	}
	
	/*
	 * Determines if the given Account has a Date of Birth and returns it. Otherwise, returns null.
	 */
	protected Date getAccountDateOfBirth(Account account) {
		return (account instanceof DirectChargeAccount) ? ((DirectChargeAccount)account).getDateOfBirth() : null;
	}
	
	/*
	 * Determines the account type.
	 */
	protected AccountTypeValue getAccountType(Account account) {
		return (account instanceof DirectChargeAccount) ? AccountTypeValue.DIRECT_CHARGE : AccountTypeValue.DELEGATE;
	}
	
	/*
	 * Get the Account from the form object. 
	 * Creates a new Account object if the type selected on the form is different
	 * from the one stored in the form object.
	 */
	private Account setUpAccountFromForm(AdminForm form) {
		// Determine if we need to create a new account from the New Account form or can reuse the existing accout:
		Account formAccount = form.getAccount();
		Account result = formAccount;
		
		// If there is no ID, then it's a new Account:
		if (StringUtils.isBlank(formAccount.getId())) {
			String selectedAccountType = form.getAccountInfo().getAccountType();
			Account newAccount = null;
			
			if (StringUtils.equals(selectedAccountType, AccountTypeValue.DELEGATE_CODE)) {
				// Create a new DelegateAccount:
				newAccount = new DelegateAccount();
			} else if (StringUtils.equals(selectedAccountType, AccountTypeValue.DIRECT_CHARGE_CODE)) {
				// Create a new DirectChargeAccount:
				DirectChargeAccount dcAccount = new DirectChargeAccount();
				
				dcAccount.setDateOfBirth(form.getAccountInfo().getDateOfBirth());
				newAccount = dcAccount;
			}
			
			// Copy properties from the form stored Account to the new one:
			BeanUtils.copyProperties(formAccount, newAccount);
			
			// For new accounts, set up account id and auditable information:
			setAccountIdAndAuditableInfo(newAccount);
			
			// Find the existing objects:
			LatePeriod existingLatePeriod = auditableEntityService.getAuditableEntity(formAccount.getLatePeriod().getId(), LatePeriod.class);
			AccountStatusType existingAccountStatusType = auditableEntityService.getAuditableEntity(formAccount.getStatusType().getId(), AccountStatusType.class);
			
			newAccount.setLatePeriod(existingLatePeriod);
			newAccount.setStatusType(existingAccountStatusType);
			result = newAccount;
		} else {
			// For existing accounts just update the auditable information:
			String currentUser = userSessionManager.getUserId(RequestUtils.getThreadRequest());
			
			formAccount.setEditorId(currentUser);
			formAccount.setLastUpdate(new Date());
		}
		
		return result;
	}
	
	/*
	 * If a new Account, sets up account ID and Auditable entries.
	 */
	private void setAccountIdAndAuditableInfo(Account account) {
		// TODO: Determine what to do about the account ID:
		String id = RandomStringUtils.randomAlphabetic(15);
		String currentUser = userSessionManager.getUserId(RequestUtils.getThreadRequest());
		// TODO: Figure out what to do with KIM IDs!
		boolean isKimAccount = false;
		
		// Set the properties:
		account.setId(id);
		account.setEntityId(id);
		account.setCreationDate(new Date());
		account.setCreatorId(currentUser);
		account.setKimAccount(isKimAccount);
	}
	
	
	/*
	 * Account class wrapper. Provides additional property access methods.
	 * This class is useful when used with forms.
	 */
	@SuppressWarnings("all")
	public static class AccountWrapper extends Account {
		public Boolean getAbleToAuthenticate() {
			return isAbleToAuthenticate();
		}
		public Boolean getIsKimAccount() {
			return isKimAccount();
		}
	}
}
