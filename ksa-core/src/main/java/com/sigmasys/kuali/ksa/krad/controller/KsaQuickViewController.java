package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaQuickViewForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by: dmulderink on 9/28/12 at 2:25 PM
 */
@Controller
@RequestMapping(value = "/ksaQuickVw")
public class KsaQuickViewController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaQuickViewController.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private InformationService informationService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaQuickViewForm createInitialForm(HttpServletRequest request) {
        KsaQuickViewForm form = new KsaQuickViewForm();
        String userId = request.getParameter("userId");

        if (userId != null) {

            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                String errMsg = "Cannot find Account by ID = " + userId;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

             form.setAccount(account);
        } /*else {
           String errMsg = "'userId' request parameter cannot be null";
           logger.error(errMsg);
           throw new IllegalStateException(errMsg);
        }*/

       return form;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");
        String userId = request.getParameter("userId");

        logger.info("View: " + viewId + " User: " + userId);

        if ("KsaQuickView".equals(viewId)) {
            if (!accountService.accountExists(userId)) {
                throw new IllegalArgumentException("Unknown account for userid '" + userId + "'");
            }

            if (pageId == null) {
               populateForm(userId, form);
            }  else if (pageId != null && pageId.equals("QuickViewAddMemoPage")) {
               if (userId == null || userId.isEmpty()) {
                  throw new IllegalArgumentException("'userId' request parameter must be specified");
               }

               Memo memo = new Memo();
               memo.setEffectiveDate(new Date());
               /*form.setMemo(memo);
             form.setAefInstructionalText("Add a memo");*/
            }
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    @Transactional(readOnly = false)
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
    @Transactional(readOnly = false)
    public ModelAndView save(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

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
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        String viewId = request.getParameter("viewId");
        String userId = request.getParameter("actionParameters[userId]");

        logger.info("View: " + viewId + " User: " + userId);

        if ("KsaQuickView".equals(viewId)) {
            if (!accountService.accountExists(userId)) {
                throw new IllegalArgumentException("Unknown account for userid '" + userId + "'");
            }

            populateForm(userId, form);

        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=ageDebt")
    public ModelAndView ageDebt(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        // do aging of transactions stuff...
        String accountId = form.getAccount().getId();
        boolean ignoreDeferment = Boolean.parseBoolean(form.getIgnoreDeferment());

        if (accountId != null && !accountId.trim().isEmpty()) {
            // age the indexed Account Transactions
            ChargeableAccount chargeableAccount = accountService.ageDebt(accountId, ignoreDeferment);
            // populate the form using the id
            populateForm(accountId, form);
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addMemo")
    public ModelAndView addMemo(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do cancel stuff...
        return getUIFModelAndView(form);
    }

    /**
     * Populate the form per business needs for a single account by the account identifier
     *
     * @param userId
     * @param form
     */
    private void populateForm(String userId, KsaQuickViewForm form) {

        // store the selected account ID
        //form.setSelectedId(id);

        boolean ignoreDeferment = Boolean.parseBoolean(form.getIgnoreDeferment());

        Account accountById = accountService.getFullAccount(userId);
        if (accountById == null) {
            throw new IllegalStateException("Cannot find Account by ID = " + userId);
        }

        form.setAccount(accountById);

        ChargeableAccount chargeableAccount = (ChargeableAccount) accountById;

        List<Account> accountList = new ArrayList<Account>();
        accountList.add(accountById);

        // no session scope
        //form.setStudentLookupByName(accountById.getDefaultPersonName().getLastName());
        // a list of one
        //form.setAccountBrowseList(accountList);
        form.setCompositeDefaultPersonName(accountById.getCompositeDefaultPersonName());
        form.setCompositeDefaultPostalAddress(accountById.getCompositeDefaultPostalAddress());

        BigDecimal pastDue = accountService.getOutstandingBalance(userId, ignoreDeferment) != null ? accountService.getOutstandingBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal balance = accountService.getDueBalance(userId, ignoreDeferment) != null ? accountService.getDueBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal future = accountService.getUnallocatedBalance(userId) != null ? accountService.getUnallocatedBalance(userId) : BigDecimal.ZERO;
        BigDecimal deferment = accountService.getDeferredAmount(userId) != null ? accountService.getDeferredAmount(userId) : BigDecimal.ZERO;

        // Aging

        Date lastAgeDate = chargeableAccount.getLateLastUpdate();
        form.setLastAgeDate(lastAgeDate);

        Currency currency = currencyService.getCurrency("USD");
        form.setCurrency(currency);

        LatePeriod latePeriod = chargeableAccount.getLatePeriod();

        form.setDaysLate1(latePeriod.getDaysLate1() != null ? latePeriod.getDaysLate1().toString() : "30");
        form.setDaysLate2(latePeriod.getDaysLate2() != null ? latePeriod.getDaysLate2().toString() : "60");
        form.setDaysLate3(latePeriod.getDaysLate3() != null ? latePeriod.getDaysLate3().toString() : "90");

        form.setAged30(chargeableAccount.getAmountLate1().toString());
        form.setAged60(chargeableAccount.getAmountLate2().toString());
        form.setAged90(chargeableAccount.getAmountLate3().toString());

        BigDecimal agedTotal = BigDecimal.ZERO;

        if (chargeableAccount.getAmountLate1() != null &&
                chargeableAccount.getAmountLate2() != null &&
                chargeableAccount.getAmountLate3() != null) {
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate1());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate2());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate3());
        }

        form.setAgedTotal(agedTotal.toString());

        form.setPastDueAmount(pastDue.toString());
        form.setBalanceAmount(balance.toString());
        form.setFutureAmount(future.toString());
        form.setDefermentAmount(deferment.toString());

        form.setAlerts(informationService.getAlerts(userId));

        form.setFlags(informationService.getFlags(userId));

        form.setMemos(informationService.getMemos(userId));
    }
}
