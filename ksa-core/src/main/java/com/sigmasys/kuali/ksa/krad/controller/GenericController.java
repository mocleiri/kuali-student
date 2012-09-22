package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.AbstractViewModel;
import com.sigmasys.kuali.ksa.model.SearchMappingBean;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * GenericController
 *
 * @author Michael Ivanov
 */

//TODO: create a POC to check if this solution is viable

public abstract class GenericController extends UifControllerBase {


    private static final String SEARCH_MAPPING_BEAN_NAME = "searchMapping";


    /**
     * This method can search for entities of different types specified by "searchType" and "searchValue" parameters
     *
     * @param form     KSA form
     * @param result   binding result
     * @param request  HTTP request
     * @param response HTTP response
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=searchByType")
    public ModelAndView searchByType(@ModelAttribute("KualiForm") AbstractViewModel form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        String searchType = form.getSearchType();
        if (searchType == null || searchType.isEmpty()) {
            throw new IllegalStateException("Search type is null");
        }

        String searchValue = form.getSearchValue();
        if (searchValue == null) {
            searchValue = "";
        }

        DataDictionaryService ddService = KRADServiceLocatorWeb.getDataDictionaryService();
        if (!ddService.containsDictionaryObject(SEARCH_MAPPING_BEAN_NAME)) {
            throw new IllegalStateException("Cannot find bean '" + SEARCH_MAPPING_BEAN_NAME + "'");
        }

        SearchMappingBean searchMapping = (SearchMappingBean) ddService.getDictionaryObject(SEARCH_MAPPING_BEAN_NAME);
        Map<String, String> searchTypeUrls = searchMapping.getMapping();
        if (searchTypeUrls != null) {
            String destinationUrl = searchTypeUrls.get(searchType);
            if (destinationUrl == null) {
                throw new IllegalStateException("No URL found in the mapping for searchType = " + searchType);
            }
            destinationUrl += "&searchValue=" + searchValue;
            response.sendRedirect(destinationUrl);
        }

        return getUIFModelAndView(form);
    }


}
