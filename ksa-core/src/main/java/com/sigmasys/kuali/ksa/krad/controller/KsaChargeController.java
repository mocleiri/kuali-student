package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.exception.TransactionNotAllowedException;
import com.sigmasys.kuali.ksa.krad.form.KsaChargeForm;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Charge;
import com.sigmasys.kuali.ksa.model.TransactionType;
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
import java.util.Enumeration;

/**
 * Created by: dmulderink on 9/28/12 at 7:56 AM
 */
@Controller
@RequestMapping(value = "/ksaChargeVw")
public class KsaChargeController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(KsaStudentAccountsController.class);

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected KsaChargeForm createInitialForm(HttpServletRequest request) {
      KsaChargeForm form = new KsaChargeForm();
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
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") KsaChargeForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...

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

   @RequestMapping(params = "methodToCall=start")
   public ModelAndView start(@ModelAttribute("KualiForm") KsaChargeForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

      // populate model for testing

      return super.start(form, result, request, response);

   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=submit")
   @Transactional(readOnly = false)
   public ModelAndView submit(@ModelAttribute("KualiForm") KsaChargeForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...
       Enumeration<String> enumeration = request.getParameterNames();
       while(enumeration.hasMoreElements()){
           String name = enumeration.nextElement();
           logger.info("Parameter: " + name + " Value: " + request.getParameter(name));
       }
       //String userId = request.getParameter("userId");

       String userId = form.getActionParamaterValue("userId");
       logger.info("Charge Form Userid: " + userId);
       if(userId == null){
           throw new IllegalArgumentException("Invalid Userid: " + userId);
       }


       Charge charge = form.getCharge();
       logger.info("Charge: " + charge);

       logger.info("Amt: " + charge.getAmount());

       logger.info("Dt: " + charge.getEffectiveDate());

       String typeIdString = form.getChargeTransactionTypeId();
       logger.info("Type: " + typeIdString);


       TransactionType tt = transactionService.getTransactionType(typeIdString, charge.getEffectiveDate());

       if(tt == null){
           // Error handler here.
           form.setStatusMessage("Invalid Transaction Type");
           return getUIFModelAndView(form);
       }

       try{
           Charge newCharge = (Charge)transactionService.createTransaction(typeIdString, "", userId,
                   charge.getEffectiveDate(), null,  charge.getAmount());


           if(newCharge != null){
               form.setCharge(newCharge);
               form.setStatusMessage("Charge saved");
           }
       } catch(TransactionNotAllowedException e){
           form.setStatusMessage(e.getMessage());
       }


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
   @Transactional(readOnly = false)
   public ModelAndView save(@ModelAttribute("KualiForm") KsaChargeForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

      // do save stuff...

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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") KsaChargeForm form, BindingResult result,
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute("KualiForm") KsaChargeForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }
}
