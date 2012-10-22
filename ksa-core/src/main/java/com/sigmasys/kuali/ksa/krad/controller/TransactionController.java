package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.TransactionForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.ActivityService;
import com.sigmasys.kuali.ksa.service.CurrencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/transactionView")
public class TransactionController extends GenericSearchController {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private CurrencyService currencyService;


    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected TransactionForm createInitialForm(HttpServletRequest request) {
        return new TransactionForm();
    }


    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") TransactionForm form) {
        currencyService.persistCurrency(form.getCurrency());
        return getUIFModelAndView(form);
    }


    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") TransactionForm form, HttpServletRequest request) {
        // just for the transactions by person page
        String pageId = request.getParameter("pageId");

        if (pageId != null && pageId.compareTo("bursaActivityPage") == 0) {
            form.setActivities(activityService.getActivities());
        }

        // do refresh stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(@ModelAttribute("KualiForm") TransactionForm form, HttpServletRequest request) {

        // just for the transactions by person page
        String pageId = request.getParameter("pageId");

        if (pageId != null && pageId.compareTo("bursaTransByPersonPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            String compositePersonName = getAccount(id).getCompositeDefaultPersonName();
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
                String compositePersonName = charge.getAccount().getCompositeDefaultPersonName();
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
                String compositePersonName = payment.getAccount().getCompositeDefaultPersonName();
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
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=searchByName")
    public ModelAndView searchByName(@ModelAttribute("KualiForm") TransactionForm form) {

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

                // add each account copy to a list

                accountList.add(account.getCopy());
            }
        }

        // set the account list derived from the full search list

        form.setAccountBrowseList(accountList);

        // do a search by name returning account info
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addCurrType")
    @Transactional(readOnly = false)
    public ModelAndView addCurrType(@ModelAttribute("KualiForm") TransactionForm form) {

        // add a Currency Type
        Currency currency = new Currency();

        currency.setCode(form.getCode());
        currency.setName(form.getCurrencyName());
        currency.setDescription(form.getCurrencyDescription());

        // remove a currency record by ISO type
        currencyService.persistCurrency(currency);

        // refresh the list of currencies. the form and view manage the refresh
        form.setCurrencies(currencyService.getCurrencies());

        return getUIFModelAndView(form);
    }

    private Account getAccount(String accountId) {
        Account account = accountService.getFullAccount(accountId);
        if (account != null) {
            return account;
        }
        throw new IllegalStateException("Cannot find Account by ID = " + accountId);
    }
}
