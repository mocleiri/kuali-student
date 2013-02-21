package com.sigmasys.kuali.ksa.krad.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.krad.form.Generate1098TForm;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.ReportService;


@Controller
@RequestMapping(value = "/generate1098T")
@Transactional
public class Generate1098TController extends DownloadEnabledController {

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private AccountService accountService;
	
	
	/**
	 * Creates an initial form, which is the Admin page form.
	 */
	@Override
	protected UifFormBase createInitialForm(HttpServletRequest request) {
		// Create a new Form:
		Generate1098TForm form = new Generate1098TForm();
		String userId = request.getParameter("userId");
		
		// Initialize the form:
		initializeForm(form, userId);
		
		return form;
	}
	
	/**
	 * Invoked on display of the form. 
	 * 
	 * @param form		The form behind this screen.
	 * @param userId	ID of the Account displayed.
	 * @return			ModelAndView to be displayed.
	 */
	@RequestMapping(method = RequestMethod.GET, params = "methodToCall=displayGenerate1098TForm")
	public ModelAndView displayGenerate1098TForm(@ModelAttribute("KualiForm") Generate1098TForm form, @RequestParam("userId") String userId) {
		// Initialize the form if it hasn't been initialized yet:
		initializeForm(form, userId);
		
		return getUIFModelAndView(form);
	}
	
	/**
	 * Invoked to generate a 1098T report and start download.
	 * 
	 * @param form	Form behind the screen.
	 * @return		<code>null</code> to stay on the same page. 
	 */
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, params = "methodToCall=generate1098TForm")
	public ModelAndView generate1098TReport(@ModelAttribute("KualiForm") Generate1098TForm form, HttpServletResponse response) throws Exception {
		// Check if the report year has been selected:
		if (CollectionUtils.isNotEmpty(form.getReportYears())) {
			// Get the input parameters:
			int reportYear = Integer.parseInt(form.getReportYears().get(0));
			String accountId = form.getAccount().getId();
			
			// Generate the form:
			String form1098T = "<a>some text</a>";//reportService.getIrs1098TReportByYear(accountId, reportYear);
			PersonName name = form.getAccount().getDefaultPersonName();
			String fileName = String.format("1098T_%s_%s.xml", name.getLastName(), name.getFirstName());
			String mimeType = "application/xml";
			
			// Start download:
			doDownload(form1098T, fileName, mimeType, response);
		}
		
		return getUIFModelAndView(form);
	}
	
	
	/* *****************************************************************************************************
	 * 
	 * Helper methods.
	 * 
	 * ****************************************************************************************************/
	
	private void initializeForm(Generate1098TForm form, String userId) {
		// Get the full Account if it has not been selected yet:
		if (form.getAccount() == null) {
			Account account = findAccount(userId);
			
			form.setAccount(account);
		}
		
		// Set the Reporting Years if needed:
		if (CollectionUtils.isEmpty(form.getReportYears())) {
			form.setReportYears(new ArrayList<String>());
		}
	}
	
	/**
	 * Finds an Account with the specified ID.
	 * 
	 * @param userId	ID of a user which Account to select.
	 * @return			Account with the specified ID.
	 * @throws 			IllegalArgumentException If <code>userId</code> is null or blank.
	 * @throws			UserNotFoundException If there is no such Account with the specified ID. 
	 */
	private Account findAccount(String userId) {
		Account account = null;
		
		if (StringUtils.isNotBlank(userId)) {
			// Get the full Account from the Account service:
			account = accountService.getFullAccount(userId);
			
			if (account == null) {
				throw new UserNotFoundException("Account not found for ID: " + userId);
			}
		} else {
			throw new IllegalArgumentException("Invalid user ID selected: " + userId);
		}
		
		return account;
	}
}
