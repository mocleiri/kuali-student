package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaQuickViewForm;
import com.sigmasys.kuali.ksa.krad.util.AlertsFlagsMemos;
import com.sigmasys.kuali.ksa.model.*;
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

    private static final Log logger = LogFactory.getLog(KsaStudentAccountsController.class);

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
            Account accountById = accountService.getFullAccount(userId);
            if (accountById == null) {
                throw new IllegalStateException("Cannot find Account by ID = " + userId);
            } else {
/*
            HashMap userIdMap = new HashMap();
            userIdMap.put("userId", userId);
*/
                form.setUserId(userId);
                form.setAccount(accountById);
            }
        }

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
        String userId = request.getParameter("userId");

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

    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        // populate model for testing

        return super.start(form, result, request, response);

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
        String userId = request.getParameter("userId");

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

        // Account Status summation totals
        // charges by ID
        //List<Charge> charges = transactionService.getCharges(id);

        // payments by ID
        //List<Payment> payments = transactionService.getPayments(id);

        // deferments by ID
        //List<Deferment> deferments = transactionService.getDeferments(id);

        // set the form data

        //form.setCharges(charges);
        //form.setPayments(payments);
        //form.setDeferments(deferments);

        BigDecimal pastDue = accountService.getOutstandingBalance(userId, ignoreDeferment) != null ? accountService.getOutstandingBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal balance = accountService.getDueBalance(userId, ignoreDeferment) != null ? accountService.getDueBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal future = accountService.getUnallocatedBalance(userId) != null ? accountService.getUnallocatedBalance(userId) : BigDecimal.ZERO;
        BigDecimal deferment = accountService.getDeferredAmount(userId) != null ? accountService.getDeferredAmount(userId) : BigDecimal.ZERO;

        // Aging

        Date lastAgeDate = chargeableAccount.getLateLastUpdate();
        form.setLastAgeDate(lastAgeDate);

        form.setAged30(chargeableAccount.getAmountLate1());
        form.setAged60(chargeableAccount.getAmountLate2());
        form.setAged90(chargeableAccount.getAmountLate3());

        BigDecimal agedTotal = BigDecimal.ZERO;

        if (chargeableAccount.getAmountLate1() != null &&
                chargeableAccount.getAmountLate2() != null &&
                chargeableAccount.getAmountLate3() != null) {
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate1());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate2());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate3());
        }

        form.setAgedTotal(agedTotal);

        form.setPastDueAmount(pastDue);
        form.setBalanceAmount(balance);
        form.setFutureAmount(future);
        form.setDefermentAmount(deferment);

        // Alerts, Flags and Memos
        List<Alert> alerts = informationService.getAlerts(userId);

        AlertsFlagsMemos afm = new AlertsFlagsMemos();
        // Alerts
        for (Alert alert : alerts) {

            alert.setCompositeInfo(afm.CreateCompositeAlert(alert));
        }

        form.setAlerts(alerts);

        // Flags
        // Flags do not have a Text field and throws an exception when there are flag records TODO
        List<Flag> flags = informationService.getFlags(userId);

        for (Flag flag : flags) {

            flag.setCompositeInfo(afm.CreateCompositeFlag(flag));
        }

        form.setFlags(flags);

        List<Memo> memos = informationService.getMemos(userId);

        // Memos
        for (Memo memo : memos) {

            memo.setCompositeInfo(afm.CreateCompositeMemo(memo));
        }

        form.setMemos(memos);
    }
}
