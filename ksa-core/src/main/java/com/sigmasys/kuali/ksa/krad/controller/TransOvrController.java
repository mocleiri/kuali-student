package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.TransOvrForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;

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
import java.util.*;

@Controller
@RequestMapping(value = "/transOvrVw")
public class TransOvrController extends UifControllerBase {

   @Autowired
   private AccountService accountService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected TransOvrForm createInitialForm(HttpServletRequest request) {
      TransOvrForm form = new TransOvrForm();
      return form;
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=submit")
   public ModelAndView submit(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
   public ModelAndView save(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

/*      List<AccountTrans> accntTransLst = form.getAccntTransLst();

      BigDecimal totalAmnt = form.getTotalAmnt();

      for (int i = 0; i < 3; i++)
      {
         Transaction trns = createTransaction(false);
         accntTransLst.add(new AccountTrans(trns.getLedgerDate(), trns.getStatementText(),
               trns.getExternalId(), trns.getAmount()));

         totalAmnt = totalAmnt.add(trns.getAmount());
      }

      form.setTotalAmnt(totalAmnt);

      form.setAccntTransLst(accntTransLst);

      form.setTransaction(createTransaction(true));*/

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=cancel")
   public ModelAndView cancel(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do cancel stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      form.setAccounts(accountService.getFullAccounts());

      return getUIFModelAndView(form);
   }

}
