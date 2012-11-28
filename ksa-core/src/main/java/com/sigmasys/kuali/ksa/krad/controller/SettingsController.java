package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.SettingsForm;
import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.Rollup;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.PersistenceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by: dmulderink on 10/10/12 at 8:06 PM
 */
@Controller
@RequestMapping(value = "/settingsView")
public class SettingsController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(SponsorController.class);

   //@Autowired
   //private CurrencyService currencyService;

    @Autowired
    private PersistenceService persistenceService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected SettingsForm createInitialForm(HttpServletRequest request) {
      SettingsForm form = new SettingsForm();
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
   public ModelAndView get(@ModelAttribute("KualiForm") SettingsForm form, HttpServletRequest request) {

      // do get stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known currencyId
      String currencyId = request.getParameter("currencyId");
      String entityId = request.getParameter("entityId");
      if(entityId == null){
          entityId = currencyId;
      }

       logger.info("View: " + viewId + " Page: " + pageId + " Entity ID: " + entityId);

      // Currency type
      if ("CurrencyPage".equals(pageId)) {
         // a currency instance for the view and model. User may add a currency in this page.
         // this is not a persisted currency. currencyId should be null - table record index
         form.setCurrency(new Currency());
         // this is the existing currencies in the system
         form.setCurrencies(persistenceService.getEntities(Currency.class));

      } else if ("CurrencyDetailsPage".equals(pageId)) {
            if (currencyId == null || currencyId.isEmpty()) {
               throw new IllegalArgumentException("'currencyId' request parameter must be specified");
            }
         // editing an existing currency
         form.setCurrency(persistenceService.getEntity(new Long(currencyId), Currency.class));
      } else if("RollupPage".equals(pageId)){
          form.setAuditableEntity(new Rollup());
          List<Rollup> e = persistenceService.getEntities(Rollup.class);
          form.setAuditableEntities((List<AuditableEntity>)(List<?>)e);
      } else if("RollupDetailsPage".equals(pageId)){
          if (entityId == null || entityId.isEmpty()) {
              throw new IllegalArgumentException("'entityId' request parameter must be specified");
          }
          // editing an existing currency
          form.setAuditableEntity(persistenceService.getEntity(new Long(entityId), Rollup.class));
      }

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute("KualiForm") SettingsForm form) {
      // do refresh stuff...

      // refresh the list of currencies. the form and view manage the refresh

      // a currency instance for the view and model. User may add a currency in this page.
      // this is not a persisted currency
      form.setCurrency(new Currency());
      // this is the existing currencies in the system
      form.setCurrencies(persistenceService.getEntities(Currency.class));
      // clear the status message
      form.setStatusMessage("");

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=insertCurrency")
   public ModelAndView insertCurrency(@ModelAttribute("KualiForm") SettingsForm form) {

      // do insert stuff...
      String statusMsg = "";

      // the view requires/validates the code and symbol value inputs exist, not the description

      // check for duplicate before persisting
      Currency tmpCurrency = form.getCurrency();
      String currencyCode = tmpCurrency.getCode();
      try {
         tmpCurrency = persistenceService.getEntity(new Long(currencyCode), Currency.class);
         // existing currency in the system found. Leave the currency information in the view
         statusMsg = "Existing Currency code " + tmpCurrency.getCode() + " found";
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
      Currency currency = form.getCurrency();
      Long saveResult = persistenceService.persistEntity(currency);

      if (saveResult > 0) {
         // a currency instance for the view and model.
         // this is not a persisted currency.
         // On insert we would provide the view with an initialized
         // currency instance after persisting. User may add another
         form.setCurrency(new Currency());

         // refresh the list of currencies. the form and view manage the refresh
         form.setCurrencies( persistenceService.getEntities(Currency.class));

         // success in creating the currency.
         statusMsg = "Success: Currency code " + currency.getCode() + " saved";
         form.setStatusMessage(statusMsg);
         logger.info(statusMsg);
      } else {
         // failed to create the currency. Leave the currency information in the view
         statusMsg = "Failure: Currency code " + currency.getCode() + " did not save";
         form.setStatusMessage(statusMsg);
         logger.error(statusMsg);
      }

      return getUIFModelAndView(form);
   }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(method= RequestMethod.POST, params="methodToCall=insertRollup")
    public ModelAndView insertRollup(@ModelAttribute("KualiForm") SettingsForm form) {
        // do insert stuff...
        String statusMsg = "";
        String type = "Rollup";

        // check for duplicate before persisting
        AuditableEntity tmpEntity = form.getAuditableEntity();
        if(! (tmpEntity instanceof Rollup)){

        }
        String code = tmpEntity.getCode();
        try {
            tmpEntity = persistenceService.getEntity(new Long(code), Rollup.class);
            if(tmpEntity != null){
                statusMsg = "Existing " + type + " code " + tmpEntity.getCode() + " found";
                form.setStatusMessage(statusMsg);
                logger.error(statusMsg);
                return getUIFModelAndView(form);
            }
        } catch (IllegalArgumentException iae) {
            // not found. log the message exception and continue
            logger.error(iae.getMessage(), iae);
            form.setStatusMessage(iae.getMessage());
        }

        AuditableEntity entity = form.getAuditableEntity();
        Long saveResult = persistenceService.persistEntity(entity);

        if (saveResult > 0) {
            form.setAuditableEntity(new Rollup());

            form.setAuditableEntities( (List<AuditableEntity>)(List<?>)persistenceService.getEntities(Currency.class));

            // success in creating the currency.
            statusMsg = "Success: " + type + " code " + entity.getCode() + " saved";
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } else {
            // failed to create the currency. Leave the currency information in the view
            statusMsg = "Failure: " + type + " code " + entity.getCode() + " did not save";
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=updateCurrency")
   public ModelAndView updateCurrency(@ModelAttribute("KualiForm") SettingsForm form, HttpServletRequest request) {

      // do update stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known currencyId
      String currencyId = request.getParameter("currencyId");

      String statusMsg = "";

      logger.info("View: " + viewId + " Page: " + pageId + " Currency ID: " + currencyId);

      Currency currency = form.getCurrency();
      // occurs in the detail page.
      Long saveResult = persistenceService.persistEntity(currency);

      if (saveResult > 0) {
         // success in updating the currency.
         statusMsg = "Success: Currency code " + currency.getCode() + "updated";
         form.setStatusMessage(statusMsg);
         logger.info(statusMsg);
      } else {
         // failed to update the currency. Leave the currency information in the view
         statusMsg = "Failure: Currency code " + currency.getCode() + " did not update";
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
    @RequestMapping(method= RequestMethod.POST, params="methodToCall=updateRollup")
    public ModelAndView updateRollup(@ModelAttribute("KualiForm") SettingsForm form, HttpServletRequest request) {

        String type = "Rollup";

        return updateAuditableEntity(form, type);
    }

    private ModelAndView updateAuditableEntity(@ModelAttribute("KualiForm") SettingsForm form, String type){

        String statusMsg = "";

        AuditableEntity entity = form.getAuditableEntity();
        // occurs in the detail page.
        Long saveResult = persistenceService.persistEntity(entity);

        if (saveResult > 0) {
            // success in updating the currency.
            statusMsg = "Success: " + type + " code " + entity.getCode() + "updated";
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } else {
            // failed to update the currency. Leave the currency information in the view
            statusMsg = "Failure: " + type + " code " + entity.getCode() + " did not update";
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

}
