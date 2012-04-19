package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Debit;
import com.sigmasys.kuali.ksa.model.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class AccountServiceTest extends AbstractServiceTest {


    @Autowired
    private AccountService accountService;

    @Test
    public void getAccount() throws Exception {

        Account account = accountService.getOrCreateAccount("admin1");

        Assert.notNull(account);
        Assert.notNull(account.getEntityId());

        PersonService personService = KimApiServiceLocator.getPersonService();

        Assert.notNull(personService);

        Person person = personService.getPersonByPrincipalName("admin1");

        Assert.notNull(person);
        Assert.notNull(person.getEntityId());

        Assert.isTrue(account.getEntityId().equals(person.getEntityId()));

        // Add more assertions when we have some test data
    }

    @Test
    public void getFullAccount() throws Exception {

        Account account = accountService.getOrCreateAccount("admin1");

        Assert.notNull(account);
        Assert.notNull(account.getEntityId());

        account = accountService.getFullAccount(account.getId());

        Assert.notNull(account);

        Assert.notNull(account.getPersonNames());
        Assert.notNull(account.getPostalAddresses());
        Assert.notNull(account.getElectronicContacts());

        Assert.notEmpty(account.getPersonNames());
        Assert.notEmpty(account.getPostalAddresses());
        Assert.notEmpty(account.getElectronicContacts());

        Assert.notNull(account.getDefaultPersonName());
        Assert.notNull(account.getDefaultPostalAddress());
        Assert.notNull(account.getDefaultElectronicContact());

        // Add more assertions when we have some test data
    }

    @Test
    public void getFullAccounts() throws Exception {

        List<Account> accounts = accountService.getFullAccounts();

        Assert.notNull(accounts);
        Assert.notEmpty(accounts);

        for (Account account : accounts) {

            Assert.notNull(account);

            Assert.notNull(account.getPersonNames());
            Assert.notNull(account.getPostalAddresses());
            Assert.notNull(account.getElectronicContacts());

            Assert.notEmpty(account.getPersonNames());
            Assert.notEmpty(account.getPostalAddresses());
            Assert.notEmpty(account.getElectronicContacts());

            Assert.notNull(account.getDefaultPersonName());
            Assert.notNull(account.getDefaultPostalAddress());
            Assert.notNull(account.getDefaultElectronicContact());

        }

        // Add more assertions when we have some test data
    }

    @Test
    public void getDueBalance() {

        String userId = "admin";

        BigDecimal balanceDue = accountService.getDueBalance(userId, false);

        Assert.notNull(balanceDue);
        Assert.isTrue(balanceDue.compareTo(BigDecimal.ZERO) >= 0);

    }

    @Test
    public void rebalance() {

        String userId = "admin";

        List<Pair<Debit, BigDecimal>> amounts = accountService.rebalance(userId, false);

        Assert.notNull(amounts);

        for ( Pair<Debit, BigDecimal> pair : amounts) {
           System.out.println("Debit = " + pair.getA() + ", amount = " + pair.getB());
        }

    }
}
