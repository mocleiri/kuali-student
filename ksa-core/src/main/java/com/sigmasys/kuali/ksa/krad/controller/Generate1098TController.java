package com.sigmasys.kuali.ksa.krad.controller;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sigmasys.kuali.ksa.util.ErrorUtils;
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
import com.sigmasys.kuali.ksa.util.CalendarUtils;


@Controller
@RequestMapping(value = "/generate1098T")
@Transactional
public class Generate1098TController extends DownloadController {

    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(Generate1098TController.class);


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
     * Invoked on a success callback to download the report form.
     *
     * @param response HTTP response.
     * @return Nothing to stay on the same page.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=download1098TForm")
    public ModelAndView download1098TForm(@ModelAttribute("KualiForm") Generate1098TForm form, HttpServletResponse response,
                                          @RequestParam("reportYear") int reportYear) throws Exception {
        // Read the report file:
        String form1098T = null;
        String error = null;
        String accountId = form.getAccount().getId();

        // Get the form from the ReportService:
        try {
            form1098T = reportService.generate1098TReportByYear(accountId, reportYear, false);
        } catch (Exception e) {
            error = String.format("<h2>Error generating form 1098T. See log file for details.</h2><p><h4>%s</h4>",
                    getExceptionMessage(e, "Contact your administrator for details."));
            logger.error(error, e);
        }

        // If the form content is available, start download:
        if (StringUtils.isNotEmpty(form1098T)) {
            // Start download:
            String reportFileName = generateReportFileName(form);

            doDownload(form1098T, reportFileName, "application/xml", response);
        } else {
            // Write an error message into the HTTP response:
            PrintWriter out = response.getWriter();
            out.println(error);
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

        // Set the report year to the current year:
        form.setReportYears(Arrays.asList(String.valueOf(CalendarUtils.getYear(new Date()))));
    }

    /**
     * Finds an Account with the specified ID.
     *
     * @param userId ID of a user which Account to select.
     * @return Account with the specified ID.
     * @throws IllegalArgumentException If <code>userId</code> is null or blank.
     * @throws UserNotFoundException    If there is no such Account with the specified ID.
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
     * Generates a name for a report file.
     *
     * @param form A form.
     * @return Generated report file name.
     */
    private static String generateReportFileName(Generate1098TForm form) {
        PersonName name = form.getAccount().getDefaultPersonName();
        return String.format("1098T_%s_%s.xml", name.getLastName(), name.getFirstName());
    }

    /**
     * Extracts the message from a Throwable. If the message is missing, checks the cause Throwable.
     *
     * @param t Throwable to get its or its cause's message.
     * @return Error message.
     */
    private static String getExceptionMessage(Throwable t, String defaultMessage) {
        String errorMessage = ErrorUtils.getMessage(t);
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = defaultMessage;
        }
        return errorMessage;
    }
}
