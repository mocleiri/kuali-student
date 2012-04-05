package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.model.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class CurrencyServiceTest extends AbstractServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Test
    public void getCurrency() throws Exception {

        Currency currency = currencyService.getCurrency("usd");

        Assert.notNull(currency);
        Assert.isTrue(currency.getIso().equalsIgnoreCase("usd"));

        currency = currencyService.getCurrency(1L);

        Assert.notNull(currency);
        Assert.isTrue(currency.getIso().equalsIgnoreCase("usd"));

    }

    @Test
    public void updateCurrency() throws Exception {

        Currency currency = currencyService.getCurrency("usd");

        Assert.notNull(currency);
        Assert.isTrue(currency.getIso().equalsIgnoreCase("usd"));

        currency.setDescription("Test description");

        currencyService.persistCurrency(currency);

        currency = currencyService.getCurrency(1L);

        Assert.notNull(currency);
        Assert.isTrue(currency.getDescription().equals("Test description"));

    }

}
