package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaFlagsForm;
import com.sigmasys.kuali.ksa.krad.model.FlagModel;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Flag;
import com.sigmasys.kuali.ksa.model.FlagType;
import com.sigmasys.kuali.ksa.model.InformationTypeValue;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 10/6/12 at 2:29 PM
 */
@Controller
@RequestMapping(value = "/ksaFlagsVw")
public class KsaFlagsController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(KsaFlagsController.class);

   @Autowired
   private InformationService informationService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected KsaFlagsForm createInitialForm(HttpServletRequest request) {
      KsaFlagsForm form = new KsaFlagsForm();
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
   public ModelAndView get(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...

      String pageId = request.getParameter("pageId");
      // example user 1
      String userId = request.getParameter("userId");

      if (pageId != null && pageId.equals("FlagsPage")) {

         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }
         List<Flag> flags = informationService.getFlags(userId);
         List<FlagModel> flagModels = new ArrayList<FlagModel>();

         for (Flag flag : flags) {
            FlagModel flagModel = new FlagModel(flag);
            flagModels.add(flagModel);
         }

         form.setFlagModels(flagModels);

      }else if (pageId != null && pageId.equals("AddFlagPage")) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }
         form.setAeInstructionalText("Add a flag");

         Flag flag = new Flag();
         // set default values
         FlagType flagType = new FlagType();
         flagType.setAccessLevel(0);
         // Text is not saved if the type is not set
         // we need a list of flags for the view so the user can make a choice
         flag.setType(flagType);
         flag.setAccount(accountService.getFullAccount(userId));
         flag.setAccessLevel(0);
         flag.setSeverity(0);

         form.setFlagModel(new FlagModel(flag));
         form.setAeInstructionalText("Add a flag");

      } else if (pageId != null && pageId.equals("ViewFlagPage")) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         form.setAeInstructionalText("View a flag");

      } else if (pageId != null && pageId.equals("EditFlagPage")) {
         if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("'userId' request parameter must be specified");
         }

         form.setAeInstructionalText("Edit a flag");

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
   public ModelAndView start(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
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
   public ModelAndView submit(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
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
   public ModelAndView save(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") KsaFlagsForm form, BindingResult result,
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
   public ModelAndView refresh(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
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
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertFlag")
   public ModelAndView insertFlag(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
      // do insert stuff...

      // TODO validate the field entries before inserting

      // the account should be satisfied by the link that got us into this page
      Account account = form.getAccount();

      Flag flag = new Flag();
      FlagModel flagModel = form.getFlagModel();
      flag.setAccount(flagModel.getAccount());
      flag.setAccountId(flagModel.getAccountId());
      flag.setCreationDate(new Date());
      flag.setLastUpdate(new Date());

      // from the view input to the flagModel
      flag.setEffectiveDate(flagModel.getEffectiveDate());
      flag.setExpirationDate(flagModel.getExpirationDate());
      flag.setType(flagModel.getType());
      flag.setText(flagModel.getText());
      flag.setAccessLevel(Integer.parseInt(flagModel.getInfoAccessLevel()));
      flag.setSeverity(Integer.parseInt(flagModel.getInfoSeverity()));

      Long persistResult = informationService.persistInformation(flag);

      if (persistResult >= 0) {
         form.setStatusMessage("Success");
      } else {
         form.setStatusMessage("Failed to add flag. result code: " + persistResult.toString());
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
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateFlag")
   public ModelAndView updateFlag(@ModelAttribute("KualiForm") KsaFlagsForm form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }
}
