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

import edu.kuali.rice.krad.ks.uim.sample.KsUimSampleForm;
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
    protected KsUimSampleForm createInitialForm(HttpServletRequest request) {
        return new KsUimSampleForm();
    }

	@Override
	@RequestMapping(params = "methodToCall=start")
	public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
	    KsUimSampleForm uiTestForm = (KsUimSampleForm) form;

		uiTestForm.setField5("a14");

		uiTestForm.setField1("Field1");
		uiTestForm.setField2("Field2");

        uiTestForm.setHidden1("Hidden1");
        uiTestForm.setHidden2("Hidden2");

		return super.start(uiTestForm, result, request, response);
	}

	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
	public ModelAndView save(@ModelAttribute("KualiForm") KsUimSampleForm uiTestForm, BindingResult result,
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

	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=close")
	public ModelAndView close(@ModelAttribute("KualiForm") KsUimSampleForm uiTestForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {

		return getUIFModelAndView(uiTestForm, "page1");
	}

}

