package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Tim Bornholtz
 */
@Service("accountService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccountServiceImpl extends GenericPersistenceService implements AccountService {

    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);

    @Override
    public void rebalance(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void ageDebt(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDueBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getOutstandingBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getUnallocatedBalance() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDeferredAmount() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void makeEffective() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void paymentApplication() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createLockedAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This methods fetches Account and all its associations by account ID.
     *
     * @param accountId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    @Transactional(readOnly = false)
    public Account getFullAccount(String accountId) {
        Query query = em.createQuery("select a from Account a " +
                "left outer join fetch a.personNames pn " +
                "left outer join fetch a.postalAddresses pa " +
                "left outer join fetch a.electronicContacts ec " +
                "left outer join fetch a.statusType st " +
                "left outer join fetch a.latePeriod lp " +
                "where a.id = :id");
        query.setParameter("id", accountId);
        List<Account> accounts = query.getResultList();
        return (accounts != null && !accounts.isEmpty()) ? accounts.get(0) : null;
    }

    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param accountId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    @Transactional(readOnly = false)
    public Account getOrCreateAccount(String accountId) {
        Account account = getEntity(accountId, Account.class);
        if (account == null) {
            PersonService personService = KimApiServiceLocator.getPersonService();
            Person person = personService.getPersonByPrincipalName(accountId);
            if (person == null) {
                String errMsg = "The user '" + person + "' does not exist";
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }
            // If the person exists in KIM we have to create a new KSA account based on that
            account = createAccount(person);
        }
        return account;
    }

    private Account createAccount(Person person) {

        // TODO - populate the missing fields
        // TODO: figure out how to distinguish Delegate and DirectCharge account types

        Account account = new DirectChargeAccount();
        account.setId(person.getPrincipalName());
        account.setCreationDate(new Date());
        account.setAbleToAuthenticate(true);
        account.setEntityId(person.getEntityId());
        account.setKimAccount(true);
        account.setCreditLimit(new BigDecimal(0.0));

        PersonName personName = new PersonName();
        personName.setCreatorId("system");
        personName.setLastUpdate(new Date());
        personName.setDefault(true);
        personName.setFirstName(person.getFirstName());
        personName.setMiddleName(person.getMiddleName());
        personName.setLastName(person.getLastName());
        personName.setKimNameType(person.getEntityTypeCode());
        personName.setDefault(true);

        // Making PersonName persistent and generate ID
        persistEntity(personName);

        PostalAddress address = new PostalAddress();
        address.setCreatorId("system");
        address.setLastUpdate(new Date());
        address.setDefault(true);
        address.setPostalCode(person.getAddressPostalCode());
        address.setCountry(person.getAddressCountryCode());
        address.setState(person.getAddressStateProvinceCode());
        // TODO: no city ??
        address.setStreetAddress1(person.getAddressLine1());
        address.setStreetAddress2(person.getAddressLine2());
        address.setStreetAddress3(person.getAddressLine3());

        // Making PostalAddress persistent and generate ID
        persistEntity(address);

        account.setPersonNames(new HashSet<PersonName>(Arrays.asList(personName)));
        account.setPostalAddresses(new HashSet<PostalAddress>(Arrays.asList(address)));

        // "Account is in good standing" (Paul) ID = 1
        AccountStatusType statusType = getEntity(1L, AccountStatusType.class);
        if (statusType != null) {
            account.setStatusType(statusType);
        }

        // Late Period with ID = 1
        LatePeriod latePeriod = getEntity(1L, LatePeriod.class);
        if (latePeriod != null) {
            account.setLatePeriod(latePeriod);
        }

        // Making Account persistent
        persistEntity(account);

        // Linking PersonName and PostalAddress back to already persisted Account
        personName.setAccount(account);
        address.setAccount(account);

        return account;

    }
}
