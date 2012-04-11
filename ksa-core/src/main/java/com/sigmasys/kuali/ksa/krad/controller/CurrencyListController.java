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

import com.sigmasys.kuali.ksa.krad.form.CurrencyListForm;
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
 * Currency list controller
 *
 * @author Michael Ivanov
 */
@Controller
@RequestMapping(value = "/currencyList")
public class CurrencyListController extends UifControllerBase {

    @Autowired
    private CurrencyService currencyService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected CurrencyListForm createInitialForm(HttpServletRequest request) {
        return new CurrencyListForm();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(@ModelAttribute("KualiForm") CurrencyListForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

       String methodValue = request.getParameter("methodToCall");
       if (methodValue.compareTo("deleteLine") == 0)
       {
          String id = request.getParameter("id");
          if (id == null || id.isEmpty()) {
             throw new IllegalArgumentException("'id' request parameter must be specified");
          }

          // remove a currency record by ID
          boolean rowsAffected = currencyService.deleteCurrency(Long.valueOf(id));
       }

        form.setCurrencies(currencyService.getCurrencies());

        return getUIFModelAndView(form);
    }


   @RequestMapping(method=RequestMethod.POST, params="methodToCall=addCurrType")
   public ModelAndView addCurrType(@ModelAttribute ("KualiForm") CurrencyListForm form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {

      // add a Currency Type
      Currency currency = new Currency();

      String isoVal = form.getIso();
      String currName = form.getName();
      String currDes = form.getDescription();

      currency.setIso(isoVal);
      currency.setName(form.getName());
      currency.setDescription(form.getDescription());

      // remove a currency record by ISO type
      Long rowsAffected = currencyService.persistCurrency(currency);

      // refresh the list of currencies. the form and view manage the refresh
      form.setCurrencies(currencyService.getCurrencies());

      return getUIFModelAndView(form);
   }
/*
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=delete")
   public ModelAndView delete(@ModelAttribute ("KualiForm") CurrencyListForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {

      // do delete on a single id type record

      String id = request.getParameter("id");
      if (id == null || id.isEmpty()) {
         throw new IllegalArgumentException("'id' request parameter must be specified");
      }

      // remove a currency record by ISO type
      int rowsAffected = currencyService.deleteCurrency(Long.valueOf(id));

      // refresh the list of currencies. the form and view manage the refresh
      form.setCurrencies(currencyService.getCurrencies());

      return getUIFModelAndView(form);
   }
*/
}
