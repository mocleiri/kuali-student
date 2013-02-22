package com.sigmasys.kuali.ksa.krad.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class Generate1098TController extends DownloadController {
	
	/**
	 * The logger.
	 */
	private static final Log logger = LogFactory.getLog(Generate1098TController.class);

	/**
	 * A place to store temporary 1098T report generate files.
	 */
	private static final String TEMP_FILE_DIR = System.getProperty("user.home") + File.separator;
	
	
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
     * @param form   The form behind this screen.
     * @param userId ID of the Account displayed.
     * @return ModelAndView to be displayed.
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
     * @param form Form behind the screen.
     * @return        <code>null</code> to stay on the same page.
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
            String reportFileName = generateReportFileName(form);

            // Save the report file:
            writeReportToFile(form1098T, reportFileName);
            
            // Display the download link:
            form.setDisplayDownloadLink(true);
            form.setError(null);
            form.setReportYears(new ArrayList<String>());
        } else {
        	form.setDisplayDownloadLink(false);
    		form.setError("Select a valid report year.");
    	}

        return getUIFModelAndView(form);
    }
    
    /**
     * Invoked on a success callback to download the report form.
     * 
     * @param request			HTTP request.
     * @param response			HTTP response.
     * @param reportFileName	Report file name.
     * @return					Nothing to stay on the same page.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=download1098TForm")
    public ModelAndView download1098TForm(@ModelAttribute("KualiForm") Generate1098TForm form, HttpServletResponse response) throws Exception {
    	// Read the report file:
    	String reportFileName = generateReportFileName(form);
    	String form1098T = readReportFromFile(reportFileName, true);

    	// If the form content is available, start download:
    	if (StringUtils.isNotEmpty(form1098T)) {
    		// Start download:
    		doDownload(form1098T, reportFileName, "application/xml", response);
    	} else {
    		// Write an error message into the HTTP response:
    		PrintWriter out = response.getWriter();
    		
    		out.println("<h2>Requested 1098T form is no longer available.</h2><br><h4>Click Back on your browser and regenerate the 1098T form.</h4>");
    	}
    	
    	return null;
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
     * @param userId ID of a user which Account to select.
     * @throws IllegalArgumentException If <code>userId</code> is null or blank.
     * @return Account with the specified ID.
     * @throws UserNotFoundException If there is no such Account with the specified ID.
     */
    private Account findAccount(String userId) {

        if (StringUtils.isNotBlank(userId)) {
            // Get the full Account from the Account service:
            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                throw new UserNotFoundException("Account not found for ID: " + userId);
            }

            return account;
        }
        throw new IllegalArgumentException("Invalid user ID selected: " + userId);
    }
    
    /**
     * Reads a 1098T report from a file on the disk.
     * 
     * @param fileName				Name of the file containing 1098T report.
     * @param deleteAfterReading	Whether to delete the file after reading.
     * @return						Contents of the file as String. 
     */
    private static String readReportFromFile(String fileName, boolean deleteAfterReading) {
    	// Get the report file and read the contents from it:
    	File file = new File(TEMP_FILE_DIR + fileName);
    	String form1098T = null;
    	
    	try {
    		// Read the file contents into a String:
    		form1098T = FileUtils.readFileToString(file);
    	} catch (Exception e) {
    		// Log an error:
    		logger.error("Error reading from 1098T report file at: " + TEMP_FILE_DIR + fileName);
    	} finally {
    		// Delete after reading if required:
    		if (deleteAfterReading) {
    			FileUtils.deleteQuietly(file);
    		}
    	}
    	
    	return form1098T;
    }
    
    /**
     * Writes a 1098T report into a file on the disk.
     * 
     * @param form1098T	Contents of a 1098T form to write into a file.
     * @param fileName	File name under which to write the contents.
     */
    private static void writeReportToFile(String form1098T, String fileName) throws Exception {
    	// Create a temporary file and save the contents into it:
    	File file = new File(TEMP_FILE_DIR + fileName);
    	
    	FileUtils.writeStringToFile(file, form1098T);
    }
    
    /**
     * Generates a name for a report file.
     * 
     * @param form 	A form.
     * @return		Generated report file name.
     */
    private static String generateReportFileName(Generate1098TForm form) {
    	PersonName name = form.getAccount().getDefaultPersonName();
        String fileName = String.format("1098T_%s_%s.xml", name.getLastName(), name.getFirstName());
        
        return fileName;
    }
}
