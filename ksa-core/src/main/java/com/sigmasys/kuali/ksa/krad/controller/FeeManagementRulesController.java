package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.FeeManagementRulesForm;
import com.sigmasys.kuali.ksa.krad.form.RateRolloverForm;
import com.sigmasys.kuali.ksa.service.fm.RateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller to serve requests from the "Fee Management Rules" page.
 * User: Sergey
 * Date: 12/4/13
 * Time: 10:56 PM
 */
@Controller
@RequestMapping(value = "/feeManagementRulesView")
public class FeeManagementRulesController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(FeeManagementRulesController.class);


    /**
     * Creates the page's form.
     */
    @Override
    protected FeeManagementRulesForm createInitialForm(HttpServletRequest request) {
        FeeManagementRulesForm form = new FeeManagementRulesForm();

        return form;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, params = "methodToCall=displayInitialPage")
    public ModelAndView displayInitialPage(@ModelAttribute("KualiForm") FeeManagementRulesForm form) {

        return getUIFModelAndView(form);
    }
}
