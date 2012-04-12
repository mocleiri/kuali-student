package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class AccountServiceTest extends AbstractServiceTest {


    @Autowired
    private AccountService accountService;

    @Test
    public void getAccount() throws Exception {

        Account account = accountService.getAccount("admin1");

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

}
