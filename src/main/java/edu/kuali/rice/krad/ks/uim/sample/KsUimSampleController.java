/*
 * Copyright 2006-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kuali.rice.krad.ks.uim.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kuali.rice.krad.ks.uim.sample.DummyForm;

import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//import edu.sampleu.travel.bo.FiscalOfficer;
//import edu.sampleu.travel.bo.TravelAccount;
//import edu.sampleu.travel.krad.form.UITestForm;

/**
 * Controller for the Test UI Page
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/ks-uim")
public class KsUimSampleController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected DummyForm createInitialForm(HttpServletRequest request) {
        return new DummyForm();
    }

	@Override
	@RequestMapping(params = "methodToCall=start")
	public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
	    DummyForm uiTestForm = (DummyForm) form;

		uiTestForm.setStringField1("Field One");
		uiTestForm.setStringField2("Field Two");

		//return super.start(uiTestForm, result, request, response);
        return getUIFModelAndView(uiTestForm);
	}

    @RequestMapping(params = "methodToCall=testCall")
    public ModelAndView testCall(@ModelAttribute("KualiForm") DummyForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        form.setStringField1("Field One");
        form.setStringField2("Field Two");
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=buttons")
    public ModelAndView buttons(@ModelAttribute("KualiForm") DummyForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        form.setStringField1("Field #1");
        form.setStringField2("Field #2");
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=someWidget")
    public ModelAndView someWidget(@ModelAttribute("KualiForm") DummyForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        return getUIFModelAndView(form, "someWidget");
    }

/*
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
	public ModelAndView save(@ModelAttribute("KualiForm") DummyForm uiTestForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		//For testing server side errors:
		if(uiTestForm.getField2().equals("server_error")){
			GlobalVariables.getMessageMap().putError("field2", "serverTestError");
			GlobalVariables.getMessageMap().putError("field2", "serverTestError2");
			GlobalVariables.getMessageMap().putWarning("field2", "serverTestWarning");
			GlobalVariables.getMessageMap().putInfo("field2", "serverTestInfo");
			GlobalVariables.getMessageMap().putInfo("field3", "serverTestInfo");
			GlobalVariables.getMessageMap().putError("field13", "serverTestError");
			GlobalVariables.getMessageMap().putWarning("field4", "serverTestWarning");
			GlobalVariables.getMessageMap().putWarning("TEST_WARNING", "serverTestError3");
			GlobalVariables.getMessageMap().putError("TEST_ERROR", "serverTestError3");
			GlobalVariables.getMessageMap().putError("vField5", "serverTestError");
			GlobalVariables.getMessageMap().putError("vField6", "serverTestError");
			//GlobalVariables.getMessageMap().clearErrorMessages();
			return getUIFModelAndView(uiTestForm, uiTestForm.getPageId());
		}

		return getUIFModelAndView(uiTestForm, "page1");
	}
*/

/*
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=close")
	public ModelAndView close(@ModelAttribute("KualiForm") DummyForm uiTestForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {

		return getUIFModelAndView(uiTestForm, "page1");
	}
*/

}

