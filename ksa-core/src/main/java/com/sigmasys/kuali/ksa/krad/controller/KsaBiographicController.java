package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaBiographicForm;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.PersonName;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by: dmulderink on 9/25/12 at 6:59 PM
 */
@Controller
@RequestMapping(value = "/ksaBiographicInformationVw")
public class KsaBiographicController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaBiographicController.class);

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaBiographicForm createInitialForm(HttpServletRequest request) {
        KsaBiographicForm form = new KsaBiographicForm();
        form.setStatusMessage("");
        String userId = request.getParameter("userId");

        if (userId != null) {

            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                String errMsg = "Cannot find Account by ID = " + userId;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            form.setAccount(account);

        } /*else {
          String errMsg = "'userId' request parameter cannot be null";
          logger.error(errMsg);
          throw new IllegalStateException(errMsg);
       }*/

        return form;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");

        Account account = form.getAccount();

        if (account == null) {
            String errMsg = "No account available";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        if ("BiographicAddPersonPage".equals(pageId)) {

            return getUIFModelAndView(form);
        }

        account = accountService.getFullAccount(account.getId());
        // TODO refactor to the id in the request parameters
        if (account == null) {
            String errMsg = "Cannot find Account by ID = " + account.getId();
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        PersonName name = account.getDefaultPersonName();

        form.setKimNameType(name.getKimNameType());
        form.setFirstName(name.getFirstName());
        form.setLastName(name.getLastName());
        form.setMiddleName(name.getMiddleName());
        form.setSuffix(name.getSuffix());
        form.setTitle(name.getTitle());
        form.setPersonDefault(name.isDefault().toString());

        form.setAccount(account);

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    @Transactional(readOnly = false)
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do submit stuff...


        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    @Transactional(readOnly = false)
    public ModelAndView save(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do cancel stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do refresh stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertPerson")
    @Transactional(readOnly = false)
    public ModelAndView addPerson(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {

        Account account = form.getAccount();

        PersonName name = new PersonName();
        name.setKimNameType(form.getKimNameType());
        name.setFirstName(form.getFirstName());
        name.setMiddleName(form.getMiddleName());
        name.setLastName(form.getLastName());
        name.setSuffix(form.getSuffix());
        name.setTitle(form.getTitle());
        name.setDefault(new Boolean(form.getPersonDefault()));

        accountService.addPersonName(account.getId(), name);

        form.setStatusMessage("Person added");
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updatePerson")
    @Transactional(readOnly = false)
    public ModelAndView updatePerson(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        // @TODO: This is wrong.  Don't create a new one, get the one to update
        PersonName name = new PersonName();

        name.setKimNameType(form.getKimNameType());
        name.setFirstName(form.getFirstName());
        name.setMiddleName(form.getMiddleName());
        name.setLastName(form.getLastName());
        name.setSuffix(form.getSuffix());
        name.setTitle(form.getTitle());
        name.setDefault(new Boolean(form.getPersonDefault()));


        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertPostal")
    @Transactional(readOnly = false)
    public ModelAndView insertPostal(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...
        Account account = form.getAccount();

        PostalAddress address = new PostalAddress();
        address.setKimAddressType(form.getAddressType());
        address.setStreetAddress1(form.getAddress1());
        address.setStreetAddress2(form.getAddress2());
        address.setStreetAddress3(form.getAddress3());
        address.setCity(form.getCity());
        address.setState(form.getStateCode());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountryCode());
        address.setDefault(new Boolean(form.getPostalDefault()));

        accountService.addPostalAddress(account.getId(), address);

        form.setStatusMessage("Address added");

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updatePostal")
    @Transactional(readOnly = false)
    public ModelAndView updatePostal(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertElectronicContact")
    @Transactional(readOnly = false)
    public ModelAndView insertElectronicContact(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateElectronicContact")
    @Transactional(readOnly = false)
    public ModelAndView updateElectronicContact(@ModelAttribute("KualiForm") KsaBiographicForm form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

        return getUIFModelAndView(form);
    }
}
