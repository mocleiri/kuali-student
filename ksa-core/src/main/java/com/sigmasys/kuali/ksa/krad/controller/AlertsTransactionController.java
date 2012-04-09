package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.krad.form.AlertsTransactionForm;

import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.temp.AccountTrans;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 3/27/12
 * Time: 10:32 AM
 */

@Controller
@RequestMapping(value = "/alertsTransaction")
public class AlertsTransactionController extends UifControllerBase {

   @Autowired
   private TransactionService transactionService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected AlertsTransactionForm createInitialForm(HttpServletRequest request) {
      AlertsTransactionForm form = new AlertsTransactionForm();
      form.setWorkSetRows("2");
      form.setMinQueryDate(new Date());

      return form;
   }

   /**
    * Submit
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=submit")
   public ModelAndView submit(@ModelAttribute ("KualiForm") AlertsTransactionForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...
      return getUIFModelAndView(form);
   }

   /**
    * Save
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
   public ModelAndView save(@ModelAttribute("KualiForm") AlertsTransactionForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

      //form.setTransactions(transactionService.getTransactions());

      return getUIFModelAndView(form);
   }

   /**
    * Cancel
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=cancel")
   public ModelAndView cancel(@ModelAttribute ("KualiForm") AlertsTransactionForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do cancel stuff...
      return getUIFModelAndView(form);
   }

   /**
    * Refresh
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute ("KualiForm") AlertsTransactionForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

   /**
    * Get
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") AlertsTransactionForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      List<Charge> charges = transactionService.getCharges();

      form.setCharges(charges);

      return getUIFModelAndView(form);
   }

}
