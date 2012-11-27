package com.sigmasys.kuali.ksa.krad.controller;


import com.sigmasys.kuali.ksa.krad.form.GeneralLedgerTypeForm;
import com.sigmasys.kuali.ksa.model.GeneralLedgerType;
import com.sigmasys.kuali.ksa.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class GeneralLedgerTypeControllerTest extends AbstractServiceTest {


    @Autowired
    private GeneralLedgerTypeController generalLedgerTypeController;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GeneralLedgerService generalLedgerService;

    private GeneralLedgerTypeForm generalLedgerTypeForm;

    private String userId = "admin";


    @Before
    public void setUpWithinTransaction() {

        // set up test data within the transaction
        accountService.getOrCreateAccount(userId);

        // Initializing request parameters
        MockHttpServletRequest request = getRequest();

        request.setParameter("userId", userId);

        // Initializing the form
        generalLedgerTypeForm = generalLedgerTypeController.createInitialForm(request);
    }

    @Test
    public void getTransactionList() throws Exception {

        // Passing request parameters needed to perform get() method
        MockHttpServletRequest request = getRequest();
        MockHttpServletResponse response = getResponse();

        Map map = new HashMap();
        BindingResult bindingResult = BindingResultUtils.getBindingResult(map, "form");

        request.setParameter("userId", userId);
        request.setParameter("pageId", "GeneralLedgerTypePage");

        List<GeneralLedgerType> types = generalLedgerService.getGeneralLedgerTypes();

        ModelAndView modelAndView = generalLedgerTypeController.get(generalLedgerTypeForm, request);

        // Checking assertions
        Assert.notNull(modelAndView);
        Assert.notNull(generalLedgerTypeForm);
        Assert.notNull(generalLedgerTypeForm.getGeneralLedgerTypes());
        Assert.isTrue(generalLedgerTypeForm.getGeneralLedgerTypes().size() > 0);

        // TODO: add more assertions

    }


}
