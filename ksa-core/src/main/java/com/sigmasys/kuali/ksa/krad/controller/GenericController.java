package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.AbstractViewModel;
import com.sigmasys.kuali.ksa.krad.form.KsaStudentAccountsForm;
import com.sigmasys.kuali.ksa.krad.util.PersonPostal;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import com.sigmasys.kuali.ksa.service.AccountService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * GenericController
 *
 * @author Michael Ivanov
 */

public abstract class GenericController extends UifControllerBase {


    private static final String SEARCH_MAPPING_BEAN_NAME = "searchMapping";


    @Autowired
    protected AccountService accountService;



    /**
     * This method can search for entities of different types specified by "searchType" and "searchValue" parameters
     *
     * @param form KSA form
     * @param result binding result
     * @param request HTTP request
     * @param response HTTP response
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=searchByType")
    public ModelAndView searchByType(@ModelAttribute("KualiForm") AbstractViewModel form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        DataDictionaryService ddService = KRADServiceLocatorWeb.getDataDictionaryService();
        if ( ddService.containsDictionaryObject(SEARCH_MAPPING_BEAN_NAME)) {
            throw new IllegalStateException("Cannot find bean '" + SEARCH_MAPPING_BEAN_NAME + "'");
        }



        // do a search by name returning account info
        return getUIFModelAndView(form);
    }


}
