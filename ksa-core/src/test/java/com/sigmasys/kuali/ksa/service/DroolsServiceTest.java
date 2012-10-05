package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.drools.DroolsContext;
import com.sigmasys.kuali.ksa.service.drools.DroolsService;
import org.drools.builder.ResourceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class DroolsServiceTest extends AbstractServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private DroolsService droolsService;

    @Autowired
    private AccountService accountService;

    @Before
        public void setUpWithinTransaction() {
            // set up test data within the transaction
            String userId = "admin";
            accountService.getOrCreateAccount(userId);
        }

    @Test
    public void fireFeeAssessmentRules() throws Exception {

        DroolsContext droolsContext = new DroolsContext();
        droolsContext.setAccount(accountService.getFullAccount("admin"));

        droolsContext = droolsService.fireRules("drools/fee1.dslr", ResourceType.DSLR, droolsContext);

        Assert.notNull(droolsContext);
        Assert.notNull(droolsContext.getAccount());
        Assert.isTrue("admin".equals(droolsContext.getAccount().getId()));

    }

    @Test
    public void fireCurrencyRules() throws Exception {

        Currency currency = currencyService.getCurrency("USD");

        Assert.notNull(currency);
        Assert.isTrue(currency.getCode().equals("USD"));

        currency = droolsService.fireRules("drools/currency.xdrl", ResourceType.XDRL, currency);

        Assert.notNull(currency);
        Assert.isTrue(currency.getEditorId().equals("admin"));

    }


}
