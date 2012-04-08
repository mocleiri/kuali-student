package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.model.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
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
    public void getCurrencies() throws Exception {

        List<Currency> currencies = currencyService.getCurrencies();

        Assert.notNull(currencies);
        Assert.isTrue(!currencies.isEmpty());

        for (Currency currency : currencies) {
            Assert.notNull(currency);
            Assert.notNull(currency.getId());
            Assert.notNull(currency.getIso());
        }

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


    /*@Test
    public void createCurrency() throws Exception {

        Currency currency = new Currency();
        currency.setIso("mavr");
        currency.setName("Mavrody");
        currency.setDescription("Mavrody's currency");

        Long id = currencyService.persistCurrency(currency);

        Assert.notNull(id);
        Assert.isTrue(id > 0);

    }*/

}
