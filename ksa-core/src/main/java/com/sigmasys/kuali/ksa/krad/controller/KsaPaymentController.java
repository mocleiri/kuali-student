package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.exception.InvalidTransactionTypeException;
import com.sigmasys.kuali.ksa.exception.TransactionNotAllowedException;
import com.sigmasys.kuali.ksa.krad.form.KsaPaymentForm;
import com.sigmasys.kuali.ksa.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by: dmulderink on 9/30/12 at 11:27 AM
 */
@Controller
@RequestMapping(value = "/ksaPaymentVw")
public class KsaPaymentController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaPaymentController.class);

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaPaymentForm createInitialForm(HttpServletRequest request) {

        KsaPaymentForm form = new KsaPaymentForm();
        form.setStatusMessage("");
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
    public ModelAndView get(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        // do get stuff...

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
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do submit stuff...
        this.savePayment(form);

        return getUIFModelAndView(form);

    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submitAge")
    @Transactional(readOnly = false)
    public ModelAndView submitAge(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        // do submit stuff...

        this.savePayment(form);
        Payment payment = form.getPayment();

        if (payment != null && payment.getId() != null) {
            accountService.ageDebt(payment.getAccount().getId(), false);
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    @Transactional(readOnly = false)
    public ModelAndView save(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
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
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
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
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaPaymentForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do refresh stuff...
        return getUIFModelAndView(form);
    }

    private void savePayment(KsaPaymentForm form) {

        Payment payment = form.getPayment();

        payment.setAccount(form.getAccount());

        String typeIdString = form.getPaymentTransactionTypeId();

        TransactionType tt;
        try {
            tt = transactionService.getTransactionType(typeIdString, payment.getEffectiveDate());
        } catch (InvalidTransactionTypeException e) {
            logger.error(e.getMessage(), e);
            form.setStatusMessage(e.getMessage());
            return;
        }

        if (tt == null) {
            // Error handler here.
            form.setStatusMessage("Invalid Transaction Type");
            return;
        } else if (!(tt instanceof CreditType)) {
            form.setStatusMessage("Transaction Type must be a payment");
            return;
        }

        try {
            payment = (Payment)transactionService.createTransaction(typeIdString, payment.getExternalId(), payment.getAccount().getId(),
                                    payment.getEffectiveDate(), null, payment.getAmount() );
            if (payment.getId() != null) {
                form.setPayment(payment);
                form.setStatusMessage("Payment saved");
            }

        } catch (TransactionNotAllowedException e) {
            logger.error(e.getMessage(), e);
            form.setStatusMessage(e.getMessage());
        }

    }
}
