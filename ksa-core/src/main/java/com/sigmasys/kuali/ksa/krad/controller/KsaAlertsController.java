package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaAlertsForm;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Alert;
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
import java.util.Date;

/**
 * Created by: dmulderink on 10/6/12 at 2:28 PM
 */
@Controller
@RequestMapping(value = "/ksaAlertsVw")
public class KsaAlertsController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(KsaAlertsController.class);

   @Autowired
   private InformationService informationService;
   
   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected KsaAlertsForm createInitialForm(HttpServletRequest request) {
      KsaAlertsForm form = new KsaAlertsForm();
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
   public ModelAndView get(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...

      String pageId = request.getParameter("pageId");
      // example user 1
      String userId = request.getParameter("userId");

      if (pageId != null && pageId.equals("AlertsPage")) {

         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         form.setAlerts(informationService.getAlerts(userId));

      } else if (pageId != null && pageId.equals("ViewAlertPage")) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         form.setAeInstructionalText("View an alert");

      } else if (pageId != null && pageId.equals("EditAlertPage")) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         form.setAeInstructionalText("Edit an alert");

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

   @RequestMapping(params = "methodToCall=start")
   public ModelAndView start(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
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
   public ModelAndView submit(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
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
   @Transactional(readOnly = false)
   public ModelAndView save(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") KsaAlertsForm form, BindingResult result,
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
   public ModelAndView refresh(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertAlert")
   public ModelAndView insertAlert(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
      // do insert stuff...

      // TODO validate the field entries before inserting

      // the account should be satisfied by the link that got us into this page
      Account account = form.getAccount();

      Alert alert = form.getAlert();
      alert.setAccount(account);
      alert.setAccountId(account.getId());
      alert.setCreationDate(new Date());
      alert.setLastUpdate(new Date());
      //info.setCreatorId();
      //info.setCreatorId();
      informationService.persistInformation(alert);

      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateAlert")
   public ModelAndView updateAlert(@ModelAttribute("KualiForm") KsaAlertsForm form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

}
