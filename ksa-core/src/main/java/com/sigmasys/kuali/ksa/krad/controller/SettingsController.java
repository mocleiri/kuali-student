package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.SettingsForm;
import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.Rollup;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by: dmulderink on 10/10/12 at 8:06 PM
 */
@Controller
@RequestMapping(value = "/settingsView")
public class SettingsController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(SponsorController.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AuditableEntityService auditableEntityService;

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
        if (entityId == null) {
            entityId = currencyId;
        }

        logger.info("View: " + viewId + " Page: " + pageId + " Entity ID: " + entityId);

        // Currency type
        if ("CurrencyPage".equals(pageId)) {
            // a currency instance for the view and model. User may add a currency in this page.
            // this is not a persisted currency. currencyId should be null - table record index
            form.setCurrency(new Currency());
            // this is the existing currencies in the system
            form.setCurrencies(currencyService.getCurrencies());

        } else if ("CurrencyDetailsPage".equals(pageId)) {
            if (currencyId == null || currencyId.trim().isEmpty()) {
                throw new IllegalArgumentException("'currencyId' request parameter must be specified");
            }
            // editing an existing currency
            form.setCurrency(currencyService.getCurrency(Long.valueOf(currencyId)));
        } else if ("RollupPage".equals(pageId)) {
            form.setAuditableEntity(new Rollup());
            List<Rollup> rollups = auditableEntityService.getAuditableEntities(Rollup.class);
            List<AuditableEntity> entities = new ArrayList<AuditableEntity>(rollups.size());
            entities.addAll(rollups);
            form.setAuditableEntities(entities);
        } else if ("RollupDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            // editing an existing currency
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), Rollup.class));
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") SettingsForm form) {
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
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertCurrency")
    public ModelAndView insertCurrency(@ModelAttribute("KualiForm") SettingsForm form) {

        // a record id is returned from persisting
        // need to add a create method possibly to enforce business rules
        Currency currency = form.getCurrency();

        try {

            currencyService.persistCurrency(currency);

            // refresh the list of currencies. the form and view manage the refresh
            form.setCurrencies(currencyService.getCurrencies());

            // success in creating the currency.
            String statusMsg = "Success: Currency " + currency.getCode() + " saved";
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to create the currency. Leave the currency information in the view
            String statusMsg = "Failure: Currency " + currency.getCode() + " did not save. " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertRollup")
    public ModelAndView insertRollup(@ModelAttribute("KualiForm") SettingsForm form) {

        AuditableEntity entity = form.getAuditableEntity();
        if (!(entity instanceof Rollup)) {
            String errMsg = "Entity must be of Rollup type";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        try {

            auditableEntityService.persistAuditableEntity(entity);

            List<Rollup> rollups = auditableEntityService.getAuditableEntities(Rollup.class);
            List<AuditableEntity> entities = new ArrayList<AuditableEntity>(rollups.size());
            entities.addAll(rollups);

            form.setAuditableEntities(entities);

            // success in creating the currency.
            String statusMsg = "Success: " + entity.getClass().getName() + " saved, ID = " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to create the currency. Leave the currency information in the view
            String statusMsg = "Failure: " + entity.getClass().getName() + "entity did not save, ID = " + entity.getId() +
                    ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateCurrency")
    public ModelAndView updateCurrency(@ModelAttribute("KualiForm") SettingsForm form, HttpServletRequest request) {

        Currency currency = form.getCurrency();

        // occurs in the detail page.
        currencyService.persistCurrency(currency);

        try {
            // success in updating the currency.
            String statusMsg = "Success: Currency with ID =  " + currency.getId() + " updated";
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to update the currency. Leave the currency information in the view
            String statusMsg = "Failure: Currency with ID = " + currency.getId() + " did not update. " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateRollup")
    public ModelAndView updateRollup(@ModelAttribute("KualiForm") SettingsForm form) {

        return updateAuditableEntity(form, Rollup.class);
    }

    private <T extends AuditableEntity> ModelAndView updateAuditableEntity(@ModelAttribute("KualiForm")
                                                                           SettingsForm form, Class<T> entityType) {

        AuditableEntity entity = form.getAuditableEntity();

        if (!(entity.getClass().equals(entityType))) {
            String errMsg = "Entity must be of " + entityType.getName() + " type";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        try {
            // occurs in the detail page.
            auditableEntityService.persistAuditableEntity(entity);
            // success in updating the currency.
            String statusMsg = "Success: " + entityType + " entity updated. Entity ID =  " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to update the currency. Leave the currency information in the view
            String statusMsg = "Failure: " + entityType + " entity did not update, Entity ID =  " + entity.getId() + ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

}
