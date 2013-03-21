package org.kuali.student.enrollment.class1.krms.controller;

import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.api.repository.rule.RuleDefinitionContract;
import org.kuali.rice.krms.dto.AgendaEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.util.AgendaBuilder;
import org.kuali.student.enrollment.class1.krms.form.AgendaManagementForm;
import org.kuali.rice.krms.service.AgendaManagementViewHelperService;
import org.kuali.student.r2.core.class1.state.service.StateService;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping(value = "/agendaManagement")
public class AgendaManagementController extends UifControllerBase  {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AgendaManagementController.class);

    private TypeService typeService;
    private StateService stateService;
    private AgendaManagementViewHelperService viewHelperService;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new AgendaManagementForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
            @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {

        if (!(form instanceof AgendaManagementForm)){
            throw new RuntimeException("Form object passed into start method was not of expected type AgendaManagementForm. Got "+form.getClass().getSimpleName());
        }

        AgendaManagementForm theForm = (AgendaManagementForm) form;
        theForm.setAgendas(this.getViewHelperService(theForm).getAgendaEditors());

        return getUIFModelAndView(theForm);
    }

    /*
     * Method used to invoke the CO inquiry view from Manage Course Offering screen while search input is Course Offering
     * Code (04a screen)
     */
    @RequestMapping(params = "methodToCall=goToRuleView")
    public ModelAndView goToRuleView(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                  @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        AgendaManagementForm agendaManagementForm =  (AgendaManagementForm) form;
        String ruleId = agendaManagementForm.getActionParamaterValue("ruleId");
        RuleEditor ruleEditor = getRuleEditor(agendaManagementForm.getAgendas(), ruleId);
        AgendaBuilder agendaBuilder = new AgendaBuilder(agendaManagementForm.getView());

        Properties urlParameters = agendaBuilder.buildAgendaURLParameters(ruleEditor, "maintenanceEdit");
        String controllerPath = "krmsRuleStudentEditor";
        return super.performRedirect(form, controllerPath, urlParameters);
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form     - test form
     * @param result   - Spring form binding result
     * @param request  - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=compareRules")
    public ModelAndView compareRules(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        AgendaManagementForm agendaManagementForm =  (AgendaManagementForm) form;
        String ruleId = agendaManagementForm.getActionParamaterValue("ruleId");
        RuleEditor ruleEditor = getRuleEditor(agendaManagementForm.getAgendas(), ruleId);

        //Build the compare rule tree
        agendaManagementForm.setCompareTree(this.getViewHelperService(agendaManagementForm).buildCompareTree((RuleDefinitionContract) ruleEditor));

        // redirect back to client to display lightbox
        return showDialog("compareRuleLightBox", form, request, response);
    }

    private RuleEditor getRuleEditor(List<AgendaEditor> agendaEditors, String ruleId) {
        RuleEditor rule = null;
        for(AgendaEditor agendaEditor : agendaEditors) {
            List<RuleEditor> ruleEditors = agendaEditor.getRuleEditors();
            for(RuleEditor ruleEditor : ruleEditors) {
                if(ruleEditor.getId() != null) {
                    if(ruleEditor.getId().equals(ruleId)) {
                        rule = ruleEditor;
                    }
                }
            }
        }
        return rule;
    }

    public AgendaManagementViewHelperService getViewHelperService(AgendaManagementForm theForm){

        if (viewHelperService == null) {
            if (theForm.getView().getViewHelperServiceClass() != null){
                viewHelperService = (AgendaManagementViewHelperService) theForm.getView().getViewHelperService();
            }else{
                viewHelperService= (AgendaManagementViewHelperService) theForm.getPostedView().getViewHelperService();
            }
        }

        return viewHelperService;
    }

}