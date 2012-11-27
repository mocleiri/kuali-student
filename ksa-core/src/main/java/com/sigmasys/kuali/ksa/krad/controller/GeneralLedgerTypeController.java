package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.GeneralLedgerTypeForm;
import com.sigmasys.kuali.ksa.model.GeneralLedgerType;
import com.sigmasys.kuali.ksa.model.GeneralLedgerType;
import com.sigmasys.kuali.ksa.service.GeneralLedgerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/generalLedgerTypeView")
public class GeneralLedgerTypeController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(SponsorController.class);

   @Autowired
   private GeneralLedgerService generalLedgerService;


   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected GeneralLedgerTypeForm createInitialForm(HttpServletRequest request) {
       GeneralLedgerTypeForm form = new GeneralLedgerTypeForm();
      form.setStatusMessage("");
      return form;
   }

   /**
    *
    * @param form
    * @param request
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") GeneralLedgerTypeForm form, HttpServletRequest request) {

      // do get stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known generalLedgerTypeId
      String generalLedgerTypeId = request.getParameter("generalLedgerTypeId");

      logger.info("View: " + viewId + " Page: " + pageId + " GeneralLedgerType ID: " + generalLedgerTypeId);

      // GeneralLedgerType type
      if (pageId != null && pageId.equals("GeneralLedgerTypePage")) {
         // a generalLedgerType instance for the view and model. User may add a generalLedgerType in this page.
         // this is not a persisted generalLedgerType. generalLedgerTypeId should be null - table record index
         form.setGeneralLedgerType(new GeneralLedgerType());
         // this is the existing generalLedgerTypes in the system
         form.setGeneralLedgerTypes(generalLedgerService.getGeneralLedgerTypes());

      } else if (pageId != null && pageId.equals("GeneralLedgerTypeDetailsPage")) {
            if (generalLedgerTypeId == null || generalLedgerTypeId.isEmpty()) {
               throw new IllegalArgumentException("'generalLedgerTypeId' request parameter must be specified");
            }
         // editing an existing generalLedgerType
         form.setGeneralLedgerType(generalLedgerService.getGeneralLedgerType(generalLedgerTypeId));
      }

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute("KualiForm") GeneralLedgerTypeForm form) {
      // do refresh stuff...

      // refresh the list of generalLedgerTypes. the form and view manage the refresh

      // a generalLedgerType instance for the view and model. User may add a generalLedgerType in this page.
      // this is not a persisted generalLedgerType
      form.setGeneralLedgerType(new GeneralLedgerType());
      // this is the existing generalLedgerTypes in the system
      form.setGeneralLedgerTypes(generalLedgerService.getGeneralLedgerTypes());
      // clear the status message
      form.setStatusMessage("");

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=insertGeneralLedgerType")
   public ModelAndView insertGeneralLedgerType(@ModelAttribute("KualiForm") GeneralLedgerTypeForm form) {

      // do insert stuff...
      String statusMsg = "";

      // the view requires/validates the code and symbol value inputs exist, not the description

      // check for duplicate before persisting
      GeneralLedgerType tmpGeneralLedgerType = form.getGeneralLedgerType();
      String generalLedgerTypeCode = tmpGeneralLedgerType.getCode();
      try {
         tmpGeneralLedgerType = generalLedgerService.getGeneralLedgerType(generalLedgerTypeCode);
         // existing generalLedgerType in the system found. Leave the generalLedgerType information in the view
         statusMsg = "Existing GeneralLedgerType code " + tmpGeneralLedgerType.getCode() + " found";
         form.setStatusMessage(statusMsg);
         logger.error(statusMsg);
         return getUIFModelAndView(form);
      } catch (IllegalArgumentException iae) {
         // not found. log the message exception and continue
         logger.error(iae.getMessage(), iae);
         form.setStatusMessage(iae.getMessage());
      }

      // a record id is returned from persisting
      // need to add a create method possibly to enforce business rules
      GeneralLedgerType generalLedgerType = form.getGeneralLedgerType();
      Long saveResult = generalLedgerService.persistGeneralLedgerType(generalLedgerType);

      if (saveResult > 0) {
         // a generalLedgerType instance for the view and model.
         // this is not a persisted generalLedgerType.
         // On insert we would provide the view with an initialized
         // generalLedgerType instance after persisting. User may add another
         form.setGeneralLedgerType(new GeneralLedgerType());

         // refresh the list of generalLedgerTypes. the form and view manage the refresh
         form.setGeneralLedgerTypes(generalLedgerService.getGeneralLedgerTypes());

         // success in creating the generalLedgerType.
         statusMsg = "Success: GeneralLedgerType code " + generalLedgerType.getCode() + " saved";
         form.setStatusMessage(statusMsg);
         logger.info(statusMsg);
      } else {
         // failed to create the generalLedgerType. Leave the generalLedgerType information in the view
         statusMsg = "Failure: GeneralLedgerType code " + generalLedgerType.getCode() + " did not save";
         form.setStatusMessage(statusMsg);
         logger.error(statusMsg);
      }

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param request
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=updateGeneralLedgerType")
   public ModelAndView updateGeneralLedgerType(@ModelAttribute("KualiForm") GeneralLedgerTypeForm form, HttpServletRequest request) {

      // do update stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known generalLedgerTypeId
      String generalLedgerTypeId = request.getParameter("generalLedgerTypeId");

      String statusMsg = "";

      logger.info("View: " + viewId + " Page: " + pageId + " GeneralLedgerType ID: " + generalLedgerTypeId);

      GeneralLedgerType generalLedgerType = form.getGeneralLedgerType();
      // occurs in the detail page.
      Long saveResult = generalLedgerService.persistGeneralLedgerType(generalLedgerType);

      if (saveResult > 0) {
         // success in updating the generalLedgerType.
         statusMsg = "Success: GeneralLedgerType code " + generalLedgerType.getCode() + "updated";
         form.setStatusMessage(statusMsg);
         logger.info(statusMsg);
      } else {
         // failed to update the generalLedgerType. Leave the generalLedgerType information in the view
         statusMsg = "Failure: GeneralLedgerType code " + generalLedgerType.getCode() + " did not update";
         form.setStatusMessage(statusMsg);
         logger.error(statusMsg);
      }

      return getUIFModelAndView(form);
   }
}
