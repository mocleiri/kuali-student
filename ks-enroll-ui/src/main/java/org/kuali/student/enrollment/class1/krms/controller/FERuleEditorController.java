package org.kuali.student.enrollment.class1.krms.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.dto.AgendaTypeInfo;
import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.dto.RuleManagementWrapper;
import org.kuali.rice.krms.dto.RuleTypeInfo;
import org.kuali.rice.krms.util.AgendaUtilities;
import org.kuali.rice.krms.util.KRMSConstants;
import org.kuali.rice.krms.util.PropositionTreeUtil;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.class1.krms.dto.FEAgendaEditor;
import org.kuali.student.enrollment.class1.krms.dto.FERuleEditor;
import org.kuali.student.enrollment.class1.krms.service.impl.FERuleViewHelperServiceImpl;
import org.kuali.student.enrollment.class1.krms.util.EnrolKRMSConstants;
import org.kuali.student.r2.core.constants.KSKRMSServiceConstants;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Override of RuleEditorController for Student
 *
 * @author Kuali Student Team
 */
@Controller
@RequestMapping(value = "/finalExamRules")
public class FERuleEditorController extends EnrolRuleEditorController {

    /**
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @Override
    @RequestMapping(params = "methodToCall=addRule")
    public ModelAndView addRule(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {


        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper(document);
        ruleWrapper.setAgendaEditor(this.getSelectedAgenda(document, "Edit"));

        //Set a copy on the wrapper so that we can allow the user to cancel his/her action.
        RuleEditor rule = new FERuleEditor();
        AgendaTypeInfo agendaType = ruleWrapper.getAgendaEditor().getAgendaTypeInfo();
        for(RuleTypeInfo ruleType : agendaType.getRuleTypes()){
            rule.setTypeId(ruleType.getId());
            rule.setRuleTypeInfo(ruleType);
            break;
        }
        rule.setDummy(Boolean.TRUE);
        ruleWrapper.setRuleEditor(rule);
        this.getViewHelper(form).refreshInitTrees(ruleWrapper.getRuleEditor());

        form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, EnrolKRMSConstants.KSKRMS_RULE_FE_MAINTENANCE_PAGE_ID);
        return super.navigate(form, result, request, response);
    }

    /**
     * Deletes selected rule from agenda on Manage Course Requistes page
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=deleteRule")
    public ModelAndView deleteRule(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                   @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {

        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper(document);

        FEAgendaEditor agenda = this.getSelectedAgenda(document, "Delete");
        if (agenda != null) {
            RuleEditor ruleEditor = getSelectedRule(document, "Delete");

            //Only add rules to delete list that are already persisted.
            if (ruleEditor.getId() != null) {
                agenda.getDeletedRules().add(ruleEditor);
            }
            agenda.getRules().remove(ruleEditor);

        }

        return getUIFModelAndView(document);
    }

    /**
     * Method used to invoke the FE inquiry view from Manage Course Offering screen while search input is Course Offering
     * Code (04a screen)
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=goToRuleView")
    public ModelAndView goToRuleView(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                     @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {

        //Clear the client state on new edit rule.
        form.getClientStateForSyncing().clear();

        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper(document);
        RuleEditor ruleEditor = getSelectedRule(document, "Edit");

        //Set a copy on the wrapper so that we can allow the user to cancel his/her action.
        ruleWrapper.setRuleEditor((RuleEditor) ObjectUtils.deepCopy(ruleEditor));
        ruleWrapper.setAgendaEditor(this.getSelectedAgenda(document, "Edit"));
        this.getViewHelper(form).refreshInitTrees(ruleWrapper.getRuleEditor());

        form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, EnrolKRMSConstants.KSKRMS_RULE_FE_MAINTENANCE_PAGE_ID);
        return super.navigate(form, result, request, response);
    }

    /**
     * Reverts rule to previous state and navigates to agenda maintenance page.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=cancelEditRule")
    public ModelAndView cancelEditRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {

        form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, EnrolKRMSConstants.KSKRMS_AGENDA_FE_MAINTENANCE_PAGE_ID);
        return super.cancelEditRule(form, result, request, response);
    }

    /**
     * Updates rule and redirects to agenda maintenance page.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=updateRule")
    public ModelAndView updateRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {

        FERuleEditor ruleEditor = (FERuleEditor) getRuleEditor(form);
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper((MaintenanceDocumentForm) form);

        //Return with error message if user is currently editing a proposition.
        PropositionEditor proposition = PropositionTreeUtil.getProposition(ruleEditor);
        if ((proposition != null) && (proposition.isEditMode())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(KRMSConstants.KRMS_PROPOSITION_DETAILSECTION_ID, KRMSConstants.KRMS_MSG_ERROR_RULE_PREVIEW);
            return getUIFModelAndView(form);
        }

        if(ruleEditor.isDummy()) {
            ruleEditor.setDummy(Boolean.FALSE);
            ruleEditor.setName(ruleWrapper.getRefObjectId() + ":" + ruleEditor.getTypeId() + ":" + (((FEAgendaEditor)ruleWrapper.getAgendaEditor()).getRules().size() + 1));
            PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
            PropositionTreeUtil.resetNewProp(ruleEditor.getPropositionEditor());
            ruleEditor.setDescription(ruleEditor.getPropositionEditor().getNaturalLanguage().get(KSKRMSServiceConstants.KRMS_NL_RULE_EDIT));
            ((FEAgendaEditor) ruleWrapper.getAgendaEditor()).getRules().add(ruleEditor);
        } else {
            if (!(ruleEditor.getProposition() == null && ruleEditor.getPropId() == null)) {
                PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
                ruleEditor.setDummy(Boolean.FALSE);
                PropositionTreeUtil.resetNewProp(ruleEditor.getPropositionEditor());
                ruleEditor.setDescription(ruleEditor.getPropositionEditor().getNaturalLanguage().get(KSKRMSServiceConstants.KRMS_NL_RULE_EDIT));
                this.getViewHelper(form).rebuildActions(ruleEditor);
            }

            //Replace edited rule with existing rule.
            List<RuleEditor> rules = ((FEAgendaEditor) ruleWrapper.getAgendaEditor()).getRules();
            int index = 0;
            for (RuleEditor existingRule : rules) {
                if (existingRule.getId().equals(ruleEditor.getId())) {
                    index = rules.indexOf(existingRule);
                }
            }
            rules.set(index, ruleEditor);
        }

        form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, EnrolKRMSConstants.KSKRMS_AGENDA_FE_MAINTENANCE_PAGE_ID);
        return super.navigate(form, result, request, response);
    }

    /**
     * Retrieves selected proposition key and initializes edit on propostion.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=getSelectedKey")
    public ModelAndView getSelectedKey(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {

        //Clear the current states of the tabs to open the first tab again with the edit tree.
        Map<String, String> states = (Map<String, String>) form.getClientStateForSyncing().get(EnrolKRMSConstants.KSKRMS_RULE_FE_TABS_ID);
        states.put(KRMSConstants.KRMS_PARM_ACTIVE_TAB, EnrolKRMSConstants.KSKRMS_RULE_FE_EDITWITHOBJECT_ID);

        //Set the selected rule statement key.
        String selectedKey = request.getParameter(KRMSConstants.KRMS_PARM_SELECTED_KEY);
        getRuleEditor(form).setSelectedKey(selectedKey);

        return this.goToEditProposition(form, result, request, response);
    }

    private RuleEditor getSelectedRule(MaintenanceDocumentForm form, String actionLink){
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for " + actionLink);
        }

        int selectedLineIndex = -1;
        String selectedLine = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        if (StringUtils.isNotBlank(selectedLine)) {
            selectedLineIndex = Integer.parseInt(selectedLine);
        }

        if (selectedLineIndex == -1) {
            throw new RuntimeException("Selected line index was not set");
        }

        Collection<RuleEditor> collection = ObjectPropertyUtils.getPropertyValue(form, selectedCollectionPath);
        RuleEditor rule = ((List<RuleEditor>) collection).get(selectedLineIndex);

        return rule;
    }

    private FEAgendaEditor getSelectedAgenda(MaintenanceDocumentForm form, String actionLink){
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for " + actionLink);
        }
        selectedCollectionPath = selectedCollectionPath.substring(0, selectedCollectionPath.lastIndexOf("."));

        int selectedLineIndex = -1;
        String selectedLine = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        if (StringUtils.isNotBlank(selectedLine)) {
            selectedLineIndex = Integer.parseInt(selectedLine);
        }

        if (selectedLineIndex == -1) {
            throw new RuntimeException("Selected line index was not set");
        }

        return ObjectPropertyUtils.getPropertyValue(form, selectedCollectionPath);
    }

    protected void compareRulePropositions(MaintenanceDocumentForm form, RuleEditor ruleEditor) {
        //We don't do comparisons on Final Exam.
        return;
    }

    /**
     *
     * @param form
     * @return
     */
    @Override
    protected FERuleViewHelperServiceImpl getViewHelper(UifFormBase form) {
        return (FERuleViewHelperServiceImpl) KSControllerHelper.getViewHelperService(form);
    }
}
