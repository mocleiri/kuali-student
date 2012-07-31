package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class TransactionServiceTest extends AbstractServiceTest {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    private Transaction transaction1;
    private Transaction transaction2;
    private Allocation allocation;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    @Test
    public void createTransaction() throws Exception {

        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        Assert.notNull(transaction);
        Assert.notNull(transaction.getId());
        Assert.notNull(transaction.getTransactionType());
        Assert.notNull(transaction.getAccount());
        Assert.notNull(transaction.getCurrency());

        Assert.isTrue("USD".equals(transaction.getCurrency().getCode()));
        Assert.isTrue("admin".equals(transaction.getAccount().getId()));
        Assert.isTrue(new Date().compareTo(transaction.getEffectiveDate()) >= 0);
        Assert.isTrue(new BigDecimal(10e5).equals(transaction.getNativeAmount()));

    }


    @Test
    public void deferTransaction() throws Exception {

        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        Assert.notNull(transaction);
        Assert.notNull(transaction.getId());

        Deferment deferment = transactionService.deferTransaction(transaction.getId(), new BigDecimal(10e5 / 2),
                new Date(System.currentTimeMillis() + 100 * 1000), "New deferment for 1020", "DEF");

        Assert.notNull(deferment);
        Assert.notNull(deferment.getId());
        Assert.notNull(deferment.getTransactionType());
        Assert.notNull(deferment.getAccount());
        Assert.notNull(deferment.getCurrency());
        Assert.notNull(deferment.getAmount());

        Assert.isTrue("USD".equals(deferment.getCurrency().getCode()));
        Assert.isTrue("admin".equals(deferment.getAccount().getId()));
        Assert.isTrue(TEST_USER_ID.equals(deferment.getResponsibleEntity()));

        Assert.isTrue(new Date().compareTo(deferment.getEffectiveDate()) >= 0);
        Assert.isTrue(new BigDecimal(10e5).equals(deferment.getNativeAmount()));

    }

    private void createAllocation(boolean locked) {

        String id = "1020";

        transaction1 = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(100));
        transaction2 = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(-100));

        Assert.notNull(transaction1);
        Assert.notNull(transaction2);
        Assert.notNull(transaction1.getId());
        Assert.notNull(transaction2.getId());
        Assert.notNull(transaction1.getAmount());
        Assert.notNull(transaction2.getAmount());

        allocation =
                locked ?
                        transactionService.createLockedAllocation(transaction1.getId(), transaction2.getId(), new BigDecimal(90)) :
                        transactionService.createAllocation(transaction1.getId(), transaction2.getId(), new BigDecimal(90));

        Assert.notNull(allocation);
        Assert.notNull(allocation.getId());
        Assert.notNull(allocation.getFirstTransaction());
        Assert.notNull(allocation.getSecondTransaction());
        Assert.notNull(allocation.getFirstTransaction().getId());
        Assert.notNull(allocation.getSecondTransaction().getId());
        Assert.isTrue(allocation.getFirstTransaction().getId().equals(transaction1.getId()));
        Assert.isTrue(allocation.getSecondTransaction().getId().equals(transaction2.getId()));

        transaction1 = transactionService.getTransaction(transaction1.getId());
        transaction2 = transactionService.getTransaction(transaction2.getId());

        Assert.notNull(transaction1);
        Assert.notNull(transaction2);
        Assert.notNull(transaction1.getId());
        Assert.notNull(transaction2.getId());

        Assert.isTrue(new BigDecimal(90).equals(allocation.getAmount()));

        BigDecimal allocatedAmount1 = locked ?
                transaction1.getLockedAllocatedAmount() :
                transaction1.getAllocatedAmount();

        BigDecimal allocatedAmount2 = locked ?
                transaction2.getLockedAllocatedAmount() :
                transaction2.getAllocatedAmount();

        Assert.isTrue(new BigDecimal(90).equals(allocatedAmount1));
        Assert.isTrue(new BigDecimal(90).equals(allocatedAmount2));
    }

    @Test
    public void createAllocation() throws Exception {

        createAllocation(false);

    }

    @Test
    public void createLockedAllocation() throws Exception {

        createAllocation(true);

    }

    @Test
    public void removeAllocation() throws Exception {

        createAllocation();

        transactionService.removeAllocation(transaction1.getId(), transaction2.getId());

    }

    @Test
    public void removeLockedAllocation() throws Exception {

        createLockedAllocation();

        transactionService.removeLockedAllocation(transaction1.getId(), transaction2.getId());

    }

    @Test
    public void removeAllocations() throws Exception {

        createAllocation();

        transactionService.removeAllocations(transaction2.getId());

    }

    @Test
    public void getTransactions() throws Exception {

        List<Transaction> transactions = transactionService.getTransactions();

        Assert.notNull(transactions);
        Assert.notEmpty(transactions);

        // Add more assertions when we have some test data
    }

    @Test
    public void getCharges() throws Exception {

        List<Charge> charges = transactionService.getCharges();

        Assert.notNull(charges);
        Assert.notEmpty(charges);

        // Add more assertions when we have some test data
    }

    @Test
    public void getTransaction() throws Exception {

        Transaction transaction = transactionService.getTransaction(7777777L);

        // Check that the entity does not exist
        Assert.isNull(transaction);

        // Add more assertions when we have some test data
    }

    @Test
    public void getTransactionByUserId() throws Exception {

        List<Transaction> transactions = transactionService.getTransactions("dukakis");

        Assert.notNull(transactions);

        Assert.isTrue(transactions.isEmpty());

        // Add more assertions when we have some test data
    }

    @Test
    public void getChargesWithFormattedAmounts() throws Exception {

        List<Charge> charges = transactionService.getCharges();

        Assert.notNull(charges);
        Assert.notEmpty(charges);

        for (Charge charge : charges) {
            Assert.notNull(charge.getFormattedAmount());
            logger.info("Formatted amount = " + charge.getFormattedAmount());
        }

    }

    @Test
    public void getTransactionType() throws Exception {

        String id = "1020";

        TransactionType transactionType = transactionService.getTransactionType(id, new Date());

        Assert.notNull(transactionType);
        Assert.notNull(transactionType.getId());
        Assert.isTrue("1020".equals(transactionType.getId().getId()));

    }

    @Test
    public void getTransactionTypeClass() throws Exception {

        String id = "1020";

        Class<TransactionType> debitTypeClass = transactionService.getTransactionTypeClass(id);

        Assert.notNull(debitTypeClass);
        Assert.notNull(debitTypeClass.equals(DebitType.class));

    }


}
