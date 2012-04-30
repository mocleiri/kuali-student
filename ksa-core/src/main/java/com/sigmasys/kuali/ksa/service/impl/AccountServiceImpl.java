package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.CalendarService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Tim Bornholtz, Michael Ivanov
 */
@Service("accountService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccountServiceImpl extends GenericPersistenceService implements AccountService {

    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);


    @Autowired
    private CalendarService calendarService;

    @Autowired
    private TransactionService transactionService;

    /**
     * This process creates a temporary subset of the account as if the account were being administered
     * as a balance forward account. This permits aging the account in a way that is not affected by the
     * payment application methodology. This temporary array is passed to the ageDebt() method.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a list of pairs [Debit, BigDecimal]
     */
    @Override
    public List<Pair<Debit, BigDecimal>> rebalance(String userId, boolean ignoreDeferment) {

        Query query = em.createQuery("select t from Transaction t " +
                " where t.account.id = :userId and " +
                "       t.effectiveDate <= CURRENT_DATE and type(t) in (:chargeCode) " +
                " order by t.effectiveDate desc");

        query.setParameter("userId", userId);
        query.setParameter("chargeCode", Charge.class);

        List<Debit> debits = query.getResultList();

        List<Pair<Debit, BigDecimal>> balancedDebits = new LinkedList<Pair<Debit, BigDecimal>>();

        // Distributing balance due among the most recent debits
        BigDecimal dueBalance = getDueBalance(userId, ignoreDeferment);
        BigDecimal remainingBalance = dueBalance;

        for (Debit debit : debits) {
            BigDecimal amount = debit.getAmount() != null ? debit.getAmount() : BigDecimal.ZERO;
            if (amount.compareTo(remainingBalance) < 0) {
                balancedDebits.add(new Pair<Debit, BigDecimal>(debit, amount));
                remainingBalance = remainingBalance.subtract(amount);
            } else {
                balancedDebits.add(new Pair<Debit, BigDecimal>(debit, remainingBalance));
                break;
            }
        }

        // Checking that the due balance equals the sum of split amounts
        BigDecimal accountBalance = BigDecimal.ZERO;
        for (Pair<Debit, BigDecimal> pair : balancedDebits) {
            accountBalance = accountBalance.add(pair.getB());
        }

        if (accountBalance.compareTo(dueBalance) != 0) {
            String errMsq = "The balance due " + dueBalance + " does not match " +
                    "the summarized distributed amount " + accountBalance;
            logger.error(errMsq);
            throw new IllegalStateException(errMsq);
        }

        return balancedDebits;

    }


    /**
     * Aging debts for a chargeable account.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a chargeable account being updated
     */
    @Override
    public ChargeableAccount ageDebt(String userId, boolean ignoreDeferment) {

        Account account = getFullAccount(userId);
        if (account == null) {
            String errMsg = "Account with ID '" + userId + "' does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        if (!(account instanceof ChargeableAccount)) {
            String errMsg = "Account must be of '" + ChargeableAccount.class.getName() + "' type";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        LatePeriod latePeriod = account.getLatePeriod();

        final Date curDate = new Date();
        final Date lateDate1 = calendarService.addCalendarDays(curDate, -latePeriod.getDaysLate1());
        final Date lateDate2 = calendarService.addCalendarDays(curDate, -latePeriod.getDaysLate2());
        final Date lateDate3 = calendarService.addCalendarDays(curDate, -latePeriod.getDaysLate3());

        BigDecimal lateAmount1 = BigDecimal.ZERO;
        BigDecimal lateAmount2 = BigDecimal.ZERO;
        BigDecimal lateAmount3 = BigDecimal.ZERO;

        List<Pair<Debit, BigDecimal>> balancedDebits = rebalance(userId, ignoreDeferment);
        for (Pair<Debit, BigDecimal> pair : balancedDebits) {
            Debit debit = pair.getA();
            BigDecimal amount = pair.getB();
            if (debit.getEffectiveDate().compareTo(lateDate1) <= 0 &&
                    debit.getEffectiveDate().compareTo(lateDate2) > 0) {
                lateAmount1 = lateAmount1.add(amount);
            } else if (debit.getEffectiveDate().compareTo(lateDate2) <= 0 &&
                    debit.getEffectiveDate().compareTo(lateDate3) > 0) {
                lateAmount2 = lateAmount2.add(amount);
            } else if (debit.getEffectiveDate().compareTo(lateDate3) <= 0) {
                lateAmount3 = lateAmount3.add(amount);
            }
        }

        ChargeableAccount chargeableAccount = (ChargeableAccount) account;
        chargeableAccount.setAmountLate1(lateAmount1);
        chargeableAccount.setAmountLate2(lateAmount2);
        chargeableAccount.setAmountLate3(lateAmount3);
        chargeableAccount.setLateLastUpdate(curDate);

        persistEntity(chargeableAccount);

        return chargeableAccount;
    }

    /**
     * Returns the total balance due of all active transactions.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of balance due
     */
    @Override
    public BigDecimal getDueBalance(String userId, boolean ignoreDeferment) {
        return getBalance(userId, ignoreDeferment, false);
    }

    /**
     * Returns the outstanding balance for the given account
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of outstanding balance
     */
    @Override
    public BigDecimal getOutstandingBalance(String userId, boolean ignoreDeferment) {
        return getBalance(userId, ignoreDeferment, true);
    }

    public BigDecimal getBalance(String userId, boolean ignoreDeferment, boolean notYetEffective) {
        final String sign = notYetEffective ? ">" : "<=";
        Query query = em.createQuery("select t from Transaction t " +
                " where t.account.id = :userId and t.effectiveDate " + sign + " CURRENT_DATE");
        query.setParameter("userId", userId);
        List<Transaction> transactions = query.getResultList();
        BigDecimal amountBilled = BigDecimal.ZERO;
        BigDecimal amountPaid = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction instanceof Debit) {
                Debit debit = (Debit) transaction;
                BigDecimal amount = debit.getAmount() != null ? debit.getAmount() : BigDecimal.ZERO;
                amountBilled = amountBilled.add(amount);
            } else if (transaction instanceof Credit) {
                boolean includeAmount = true;
                if (transaction instanceof Deferment) {
                    if (ignoreDeferment || ((Deferment) transaction).isExpired()) {
                        includeAmount = false;
                    }
                }
                if (includeAmount) {
                    Credit credit = (Credit) transaction;
                    BigDecimal allocatedAmount = credit.getAllocatedAmount();
                    if (allocatedAmount == null) {
                        allocatedAmount = BigDecimal.ZERO;
                    }
                    BigDecimal lockedAllocatedAmount = credit.getLockedAllocatedAmount();
                    if (lockedAllocatedAmount == null) {
                        lockedAllocatedAmount = BigDecimal.ZERO;
                    }
                    amountPaid = amountPaid.add(allocatedAmount);
                    amountPaid = amountPaid.add(lockedAllocatedAmount);
                }
            }
        }
        return amountBilled.subtract(amountPaid);
    }

    /**
     * Returns unallocated balance for the given Account ID
     *
     * @param userId Account ID
     * @return unallocated balance
     */
    @Override
    public BigDecimal getUnallocatedBalance(String userId) {
        List<Payment> payments = transactionService.getPayments(userId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Payment payment : payments) {
            BigDecimal amount = (payment.getAmount() != null) ? payment.getAmount() : BigDecimal.ZERO;
            BigDecimal allocatedAmount = (payment.getAllocatedAmount() != null) ?
                    payment.getAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal lockedAllocatedAmount = (payment.getLockedAllocatedAmount() != null) ?
                    payment.getLockedAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal difference = amount.subtract(allocatedAmount.add(lockedAllocatedAmount));
            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                totalAmount = totalAmount.add(difference);
            }
        }
        return totalAmount;
    }

    /**
     * Returns the deferred amount
     *
     * @param userId Account ID
     * @return deferred amount
     */
    @Override
    public BigDecimal getDeferredAmount(String userId) {
        List<Deferment> deferments = transactionService.getDeferments(userId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        Date curDate = new Date();
        for (Deferment deferment : deferments) {
            Date expirationDate = deferment.getExpirationDate();
            if (expirationDate == null || curDate.before(expirationDate)) {
                totalAmount = totalAmount.add(deferment.getAmount());
            }
        }
        return totalAmount;
    }


    @Override
    public void paymentApplication(String userId) {
        // TODO
    }

    /**
     * Checks if KSA account exists
     *
     * @param userId Account ID
     * @return true if the account exists, false otherwise
     */
    @Override
    public boolean doesKsaAccountExist(String userId) {
        return getEntity(userId, Account.class) != null;
    }

    /**
     * This methods fetches Account and all its associations by account ID.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    public Account getFullAccount(String userId) {
        Query query = em.createQuery("select a from Account a " +
                "left outer join fetch a.personNames pn " +
                "left outer join fetch a.postalAddresses pa " +
                "left outer join fetch a.electronicContacts ec " +
                "left outer join fetch a.statusType st " +
                "left outer join fetch a.latePeriod lp " +
                "where a.id = :id");
        query.setParameter("id", userId);
        List<Account> accounts = query.getResultList();
        return (accounts != null && !accounts.isEmpty()) ? accounts.get(0) : null;
    }

    /**
     * This methods fetches all KSA accounts and all their associations.
     *
     * @return the list account instances
     */
    @Override
    public List<Account> getFullAccounts() {
        Query query = em.createQuery("select distinct a from Account a " +
                "left outer join fetch a.personNames pn " +
                "left outer join fetch a.postalAddresses pa " +
                "left outer join fetch a.electronicContacts ec " +
                "left outer join fetch a.statusType st " +
                "left outer join fetch a.latePeriod lp " +
                "where pn.default = true and " +
                "      pa.default = true and " +
                "      ec.default = true");
        return query.getResultList();
    }

    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    @Transactional(readOnly = false)
    public Account getOrCreateAccount(String userId) {
        Account account = getEntity(userId, Account.class);
        if (account == null) {
            PersonService personService = KimApiServiceLocator.getPersonService();
            Person person = personService.getPersonByPrincipalName(userId);
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

        final String creatorId = "system";
        final Date creationDate = new Date();

        Account account = new DirectChargeAccount();
        account.setId(person.getPrincipalName());
        account.setCreationDate(creationDate);
        account.setAbleToAuthenticate(true);
        account.setEntityId(person.getEntityId());
        account.setKimAccount(true);
        account.setCreditLimit(new BigDecimal(0.0));

        PersonName personName = new PersonName();
        personName.setCreatorId(creatorId);
        personName.setLastUpdate(creationDate);
        personName.setDefault(true);
        personName.setFirstName(person.getFirstName());
        personName.setMiddleName(person.getMiddleName());
        personName.setLastName(person.getLastName());
        personName.setKimNameType(person.getEntityTypeCode());
        personName.setDefault(true);

        // Making PersonName persistent and generate ID
        persistEntity(personName);

        PostalAddress address = new PostalAddress();
        address.setCreatorId(creatorId);
        address.setLastUpdate(creationDate);
        address.setDefault(true);
        address.setPostalCode(person.getAddressPostalCode());
        address.setCountry(person.getAddressCountryCode());
        address.setState(person.getAddressStateProvinceCode());
        address.setCity(person.getAddressCity());
        address.setStreetAddress1(person.getAddressLine1());
        address.setStreetAddress2(person.getAddressLine2());
        address.setStreetAddress3(person.getAddressLine3());

        // Making PostalAddress persistent and generate ID
        persistEntity(address);

        ElectronicContact electronicContact = new ElectronicContact();
        electronicContact.setCreatorId(creatorId);
        electronicContact.setLastUpdate(creationDate);
        electronicContact.setDefault(true);
        electronicContact.setKimEmailAddressType(person.getEmailAddress());
        electronicContact.setPhoneNumber(person.getPhoneNumber());
        electronicContact.setPhoneCountry(person.getAddressCountryCode());

        // Making ElectronicContact persistent and generate ID
        persistEntity(electronicContact);

        // Setting references to Account
        account.setPersonNames(new HashSet<PersonName>(Arrays.asList(personName)));
        account.setPostalAddresses(new HashSet<PostalAddress>(Arrays.asList(address)));
        account.setElectronicContacts(new HashSet<ElectronicContact>(Arrays.asList(electronicContact)));

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

        // Linking PersonName, PostalAddress and ElectronicContact back to already persisted Account
        personName.setAccount(account);
        address.setAccount(account);
        electronicContact.setAccount(account);

        return account;

    }
}
