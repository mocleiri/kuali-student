package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.TransOvrForm;
import com.sigmasys.kuali.ksa.krad.util.PersonPostal;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.ActivityService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.TransactionService;

import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/transOvrVw")
public class TransOvrController extends UifControllerBase {

    @Autowired
    private AccountService accountService;

    @Autowired
    ActivityService activityService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private TransactionService transactionService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected TransOvrForm createInitialForm(HttpServletRequest request) {

        return new TransOvrForm();
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    public ModelAndView submit(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do submit stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        // do save stuff...

        currencyService.persistCurrency(form.getCurrency());
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do cancel stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        // just for the transactions by person page
        String pageId = request.getParameter("pageId");

        if (pageId != null && pageId.compareTo("bursaTransByPersonPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            String compositePersonName = createCompositeDefaultPersonName(id);
            form.setSelectedPersonName(compositePersonName);

            // charges by ID
            List<Charge> charges = transactionService.getCharges(id);

            // payments by ID
            List<Payment> payments = transactionService.getPayments(id);

            form.setChargeList(charges);

            form.setPaymentList(payments);
        }

        if (pageId != null && pageId.compareTo("bursaChargePage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            // charge by ID
            Charge charge = transactionService.getCharge(Long.valueOf(id));

            if (charge != null) {
                PersonName personName = charge.getAccount().getDefaultPersonName();
                PersonPostal personPostal = new PersonPostal();
                String compositePersonName = personPostal.CreateCompositePersonName(personName);
                form.setSelectedPersonName(compositePersonName);
            }

            form.setCharge(charge);
        }

        if (pageId != null && pageId.compareTo("bursaPaymentPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            // payment by ID
            Payment payment = transactionService.getPayment(Long.valueOf(id));

            if (payment != null) {
                PersonName personName = payment.getAccount().getDefaultPersonName();
                PersonPostal personPostal = new PersonPostal();
                String compositePersonName = personPostal.CreateCompositePersonName(personName);
                form.setSelectedPersonName(compositePersonName);
            }

            form.setPayment(payment);
        }

        // Currency type
        if (pageId != null && pageId.compareTo("bursaCurrencyPage") == 0) {
            String subMethod = request.getParameter("subMethod");
            if (subMethod != null && subMethod.compareTo("deleteCurr") == 0) {
                String id = request.getParameter("id");
                if (id == null || id.isEmpty()) {
                    throw new IllegalArgumentException("'id' request parameter must be specified");
                }

                // remove a currency record by ID
                boolean rowsAffected = currencyService.deleteCurrency(Long.valueOf(id));
            }

            form.setCurrencies(currencyService.getCurrencies());
        }

        // Currency type edit
        if (pageId != null && pageId.compareTo("bursaCurrencyEditPage") == 0) {

            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                throw new IllegalArgumentException("'code' request parameter must be specified");
            }

            Currency currency = currencyService.getCurrency(code);

            form.setCurrency(currency);
        }

        if (pageId != null && pageId.compareTo("bursaActivityPage") == 0) {
            form.setActivities(activityService.getActivities());
        }

        return getUIFModelAndView(form);
    }

    /**
     * User searches on a (last) name. The result set is iterated over to create the composite PersonName
     * and composite address using default records, eventfully creating a browse list that can be displayed
     * and selected from for further processing as desired.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=searchByName")
    public ModelAndView searchByName(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        // we do not have a query by name or partial name via last name or contains yet
        // if no result set from getting full accounts than the List is empty
        // otherwise the lit contains records and a compsite person name and postal address

        String studentLookupByName = form.getStudentLookupByName();

        // query for all accounts

        List<Account> accountSearchList = accountService.getFullAccounts();

        // create a a list of Account objects for display requirements

        List<Account> accountList = new ArrayList<Account>();

        // if we have a result set of Accounts from the query

        for (Account account : accountSearchList) {

            PersonName personName = account.getDefaultPersonName();

            if (personName != null && personName.getLastName().contains(studentLookupByName)) {

                // an account should have a default PersonName and default PostalAddress

                PostalAddress postalAddress = account.getDefaultPostalAddress();

                Account accountCopy = account.getCopy();

                PersonPostal personPostal = new PersonPostal();

                String compositePersonName = personPostal.CreateCompositePersonName(personName);
                accountCopy.setCompositeDefaultPersonName(compositePersonName);
                String compositePostalAddress = personPostal.CreateCompositePostalAddress(postalAddress);
                accountCopy.setCompositeDefaultPostalAddress(compositePostalAddress);

                // add each account copy to a list

                accountList.add(accountCopy);
            }
        }

        // set the account list derived from the full search list

        form.setAccountBrowseList(accountList);

        // do a search by name returning account info
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addCurrType")
    @Transactional(readOnly = false)
    public ModelAndView addCurrType(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {

        // add a Currency Type
        Currency currency = new Currency();

        currency.setCode(form.getCode());
        currency.setName(form.getCurrencyName());
        currency.setDescription(form.getCurrencyDescription());

        // remove a currency record by ISO type
        currencyService.persistCurrency(currency);
        StringBuilder sb = new StringBuilder();
        sb.append("New currency ");
        sb.append(currency.getName());

        // refresh the list of currencies. the form and view manage the refresh
        form.setCurrencies(currencyService.getCurrencies());

        return getUIFModelAndView(form);
    }

    private String createCompositeDefaultPersonName(String id) {
        Account accountById = accountService.getFullAccount(id);
        if (accountById == null) {
            throw new IllegalStateException("Cannot find Account by ID = " + id);
        }

        PersonPostal personPostal = new PersonPostal();
        PersonName personName = accountById.getDefaultPersonName();
        return personPostal.CreateCompositePersonName(personName);
    }
}
