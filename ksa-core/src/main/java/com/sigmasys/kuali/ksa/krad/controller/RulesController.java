package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.RulesForm;
import com.sigmasys.kuali.ksa.model.RuleSet;
import com.sigmasys.kuali.ksa.service.drools.DroolsPersistenceService;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping(value = "/rulesView")
public class RulesController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(RulesController.class);

    @Autowired
    private DroolsPersistenceService droolsPersistenceService;


    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected RulesForm createInitialForm(HttpServletRequest request) {
        RulesForm rulesForm = new RulesForm();
        rulesForm.initRuleSetNameFinder(droolsPersistenceService.getRuleIds());
        return rulesForm;
    }

    /**
     * @param form RuleForm instance
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") RulesForm form) {

        // TODO

        return getUIFModelAndView(form);
    }

    /**
     * @param form RuleForm instance
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") RulesForm form) {

        String ruleSetId = form.getRuleSetId();
        String ruleSetBody = form.getRuleSetBody();

        droolsPersistenceService.persistRules(new RuleSet(ruleSetId, ruleSetBody));

        return getUIFModelAndView(form);
    }

    /**
     * @param form RulesForm
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=select")
    public ModelAndView select(@ModelAttribute("KualiForm") RulesForm form) {

        String ruleSetId = form.getRuleSetId();
        if (StringUtils.isBlank(ruleSetId)) {
            String errMsg = "Rule Set ID must be specified";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        RuleSet ruleSet = droolsPersistenceService.getRules(ruleSetId);
        if (ruleSet == null) {
            String errMsg = "Rule Set specified by ID = " + ruleSetId + " does not exist";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        form.setRuleSetId(ruleSetId);
        form.setRuleSetBody(ruleSet.getRules());

        return getUIFModelAndView(form);
    }


}
