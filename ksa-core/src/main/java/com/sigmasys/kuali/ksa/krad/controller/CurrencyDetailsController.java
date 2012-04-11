/**
 * Copyright 2005-2012 The Kuali Foundation
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
package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.CurrencyDetailsForm;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.CurrencyService;

import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Currency details controller
 *
 * @author Michael Ivanov
 */
@Controller
@RequestMapping(value = "/currencyDetails")
public class CurrencyDetailsController extends UifControllerBase {

    @Autowired
    private CurrencyService currencyService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected CurrencyDetailsForm createInitialForm(HttpServletRequest request) {
        return new CurrencyDetailsForm();
    }


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") CurrencyDetailsForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        currencyService.persistCurrency(form.getCurrency());

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") CurrencyDetailsForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        String iso = request.getParameter("iso");
        if (iso == null || iso.isEmpty()) {
            throw new IllegalArgumentException("'iso' request parameter must be specified");
        }

        Currency currency = currencyService.getCurrency(iso);

        form.setCurrency(currency);

        return getUIFModelAndView(form);
    }

}
