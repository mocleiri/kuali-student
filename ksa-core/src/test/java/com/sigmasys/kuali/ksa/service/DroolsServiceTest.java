package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.service.brm.BrmContext;
import com.sigmasys.kuali.ksa.service.brm.BrmPersistenceService;
import com.sigmasys.kuali.ksa.service.brm.BrmService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class DroolsServiceTest extends AbstractServiceTest {

    @Autowired
    private BrmService brmService;

    @Autowired
    private BrmPersistenceService brmPersistenceService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FeeManagementService feeManagementService;


    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    //@Test
    public void fireFeeAssessmentRules1() throws Exception {

        BrmContext brmContext = new BrmContext();
        brmContext.setAccount(accountService.getFullAccount("admin"));

        Map<String, Object> globalParams = new HashMap<String, Object>();
        globalParams.put("feeBase", feeManagementService.getFeeBase("admin"));

        brmContext = brmService.fireRules(1L, brmContext, globalParams);

        Assert.notNull(brmContext);
        Assert.notNull(brmContext.getAccount());
        Assert.isTrue("admin".equals(brmContext.getAccount().getId()));

    }


    @Test
    public void fireFeeAssessmentRules2() throws Exception {

        BrmContext brmContext = new BrmContext();
        brmContext.setAccount(accountService.getFullAccount("admin"));

        Map<String, Object> globalParams = new HashMap<String, Object>();
        globalParams.put("feeBase", feeManagementService.getFeeBase("admin"));

        brmContext = brmService.fireRules(2L, brmContext, globalParams);

        Assert.notNull(brmContext);
        Assert.notNull(brmContext.getAccount());
        Assert.isTrue("admin".equals(brmContext.getAccount().getId()));

    }


    @Test
    public void persistRules() throws Exception {

        // TODO!

    }


}
