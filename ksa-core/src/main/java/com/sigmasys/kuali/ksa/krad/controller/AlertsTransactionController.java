package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.krad.form.AlertsTransactionForm;
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
import java.util.Date;
import java.util.List;

/**
 * Date: 3/27/12
 * Time: 10:32 AM
 */

@Controller
@RequestMapping(value = "/alertsTransaction")
@Transactional(readOnly = true)
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
   @Transactional(readOnly = false)
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
   @Transactional(readOnly = false)
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

      List<Charge> charges = transactionService.getCharges();

      form.setCharges(charges);

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
