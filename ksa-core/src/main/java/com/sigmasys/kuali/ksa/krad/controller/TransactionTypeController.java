package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.SettingsForm;
import com.sigmasys.kuali.ksa.krad.form.TransactionTypeForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
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
@RequestMapping(value = "/transactionTypeView")
public class TransactionTypeController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(TransactionTypeController.class);

    @Autowired
    private AuditableEntityService auditableEntityService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected TransactionTypeForm createInitialForm(HttpServletRequest request) {
        TransactionTypeForm form = new TransactionTypeForm();
        form.setStatusMessage("");
        return form;
    }

    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") TransactionTypeForm form, HttpServletRequest request) {

        // do get stuff...
        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");

        String entityId = request.getParameter("entityId");

        logger.info("View: " + viewId + " Page: " + pageId + " Entity ID: " + entityId);

        if ("TransactionTypePage".equals(pageId)) {
            //form.setTransactionType(new TransactionType());
            List<TransactionType> entities = auditableEntityService.getAuditableEntities(TransactionType.class);
            logger.info("Transaction Type Count: " + entities.size());
            form.setTransactionTypes(entities);
        } else if ("TransactionTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setTransactionType(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), TransactionType.class));
        }

        return getUIFModelAndView(form);
    }


    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insert")
    public ModelAndView insert(@ModelAttribute("KualiForm") SettingsForm form) {

        AuditableEntity entity = form.getAuditableEntity();

        if(entity == null){
            logger.info("Entity is null");
        } else {
            logger.info("Entity is of type : " + entity.getClass().getName());
            logger.info("Entity code: " + entity.getCode());
        }

        try {

            String code = entity.getCode();
            String name = entity.getName();
            String description = entity.getDescription();

            auditableEntityService.createAuditableEntity(code, name, description, entity.getClass());

            form.setAuditableEntities((List<TransactionType>) auditableEntityService.getAuditableEntities(entity.getClass()));

            // success in creating the currency.
            String statusMsg = "Success: Transaction Type saved, ID = " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to create the currency. Leave the currency information in the view
            String statusMsg = "Failure: Transaction Type entity did not save, ID = " + entity.getId() +
                    ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=update")
    public <T extends AuditableEntity> ModelAndView update(@ModelAttribute("KualiForm")
                                                               SettingsForm form) {

        AuditableEntity entity = form.getAuditableEntity();

        try {
            // occurs in the detail page.
            auditableEntityService.persistAuditableEntity(entity);
            // success in updating the currency.
            String statusMsg = "Success: Transaction Type entity updated. Entity ID =  " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to update the currency. Leave the currency information in the view
            String statusMsg = "Failure: Transaction Type entity did not update, Entity ID =  " + entity.getId() + ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

}
