package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.CurrencyForm;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.CurrencyService;
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

/**
 * Created by: dmulderink on 10/10/12 at 8:06 PM
 */
@Controller
@RequestMapping(value = "/settingsView")
public class SettingsController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(SponsorController.class);

   @Autowired
   private CurrencyService currencyService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected CurrencyForm createInitialForm(HttpServletRequest request) {
      CurrencyForm form = new CurrencyForm();
      form.setStatusMessage("");

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
   public ModelAndView get(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known currencyId
      String currencyId = request.getParameter("currencyId");

      logger.info("View: " + viewId + " Page: " + pageId + " Currency ID: " + currencyId);

      // Currency type
      if (pageId != null && pageId.equals("CurrencyPage")) {
         // a currency instance for the view and model. User may add a currency in this page.
         // this is not a persisted currency. currencyId should be null - table record index
         form.setCurrency(new Currency());
         // this is the existing currencies in the system
         form.setCurrencies(currencyService.getCurrencies());

      } else if (pageId != null && pageId.equals("CurrencyDetailsPage")) {
            if (currencyId == null || currencyId.isEmpty()) {
               throw new IllegalArgumentException("'currencyId' request parameter must be specified");
            }
         // editing an existing currency
         form.setCurrency(currencyService.getCurrency(new Long(currencyId)));
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
   public ModelAndView start(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
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
   public ModelAndView submit(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
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
   public ModelAndView save(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") CurrencyForm form, BindingResult result,
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
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=replace")
   public ModelAndView replace(@ModelAttribute ("KualiForm") CurrencyForm form, BindingResult result,
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
   public ModelAndView refresh(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...

      // refresh the list of currencies. the form and view manage the refresh

      // a currency instance for the view and model. User may add a currency in this page.
      // this is not a persisted currency
      form.setCurrency(new Currency());
      // this is the existing currencies in the system
      form.setCurrencies(currencyService.getCurrencies());
      // clear the status message
      form.setStatusMessage("");

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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=insertCurrency")
   public ModelAndView insertCurrency(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do insert stuff...
      String statusMsg = "";

      // the view requires/validates the code and symbol value inputs exist, not the description

      // check for duplicate before persisting
      Currency tmpCurrency = form.getCurrency();
      String currencyCode = tmpCurrency.getCode();
      try {
         tmpCurrency = currencyService.getCurrency(currencyCode);
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
      Long saveResult = currencyService.persistCurrency(currency);

      if (saveResult > 0) {
         // a currency instance for the view and model.
         // this is not a persisted currency.
         // On insert we would provide the view with an initialized
         // currency instance after persisting. User may add another
         form.setCurrency(new Currency());

         // refresh the list of currencies. the form and view manage the refresh
         form.setCurrencies(currencyService.getCurrencies());

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
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=updateCurrency")
   public ModelAndView updateCurrency(@ModelAttribute("KualiForm") CurrencyForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do update stuff...
      String viewId = request.getParameter("viewId");
      String pageId = request.getParameter("pageId");
      // a record index from a table selection or a known currencyId
      String currencyId = request.getParameter("currencyId");

      String statusMsg = "";

      logger.info("View: " + viewId + " Page: " + pageId + " Currency ID: " + currencyId);

      Currency currency = form.getCurrency();
      // occurs in the detail page.
      Long saveResult = currencyService.persistCurrency(currency);

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
}
