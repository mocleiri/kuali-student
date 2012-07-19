package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Currency;
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

    @Test
    public void fireCurrencyRules() throws Exception {

        Currency currency = currencyService.getCurrency("USD");

        Assert.notNull(currency);
        Assert.isTrue(currency.getCode().equals("USD"));

        currency = droolsService.fireRules("/drools/currency.xdrl", currency);

        Assert.notNull(currency);
        Assert.isTrue(currency.getEditorId().equals("admin"));

    }


}
