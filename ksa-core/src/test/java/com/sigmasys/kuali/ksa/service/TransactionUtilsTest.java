package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.util.TransactionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.util.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class TransactionUtilsTest extends AbstractServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    @Test
    public void calculateMatrixScores() throws Exception {

        Transaction transaction1 =
                transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(10e3));

        notNull(transaction1);
        notNull(transaction1.getId());

        Transaction transaction2 =
                transactionService.createTransaction("1020", "admin", new Date(), new BigDecimal(501.999));

        notNull(transaction2);
        notNull(transaction2.getId());

        TransactionUtils.calculateMatrixScores(Arrays.asList(transaction1, transaction2));

        logger.info("Matrix score 1 = " + transaction1.getMatrixScore());
        logger.info("Matrix score 2 = " + transaction2.getMatrixScore());

        notNull(transaction1.getMatrixScore());
        notNull(transaction2.getMatrixScore());
        isTrue(transaction1.getMatrixScore() == 1);
        isTrue(transaction2.getMatrixScore() == 1);

    }

    @Test
    public void getUnallocatedAmount() throws Exception {

        Transaction transaction1 =
                transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(200.00));

        notNull(transaction1);
        notNull(transaction1.getId());

        Transaction transaction2 =
                transactionService.createTransaction("1020", "admin", new Date(), new BigDecimal(501.999));

        notNull(transaction2);
        notNull(transaction2.getId());

        transactionService.createAllocation(transaction1, transaction2, new BigDecimal(100.00), true, true);

        BigDecimal amount = TransactionUtils.getUnallocatedAmount(transaction1);

        notNull(amount);
        isTrue(amount.compareTo(transaction1.getAmount().subtract(transaction1.getLockedAllocatedAmount())) == 0);
        isTrue(amount.compareTo(new BigDecimal(100.00)) == 0);

        amount = TransactionUtils.getUnallocatedAmount(transaction2);

        notNull(amount);
        isTrue(amount.compareTo(transaction2.getAmount().subtract(transaction2.getLockedAllocatedAmount())) == 0);
        isTrue(amount.compareTo(new BigDecimal(401.999)) == 0);

    }

    @Test
    public void removeFullyAllocatedTransactions() throws Exception {

        Transaction transaction1 =
                transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(200.00));

        notNull(transaction1);
        notNull(transaction1.getId());

        Transaction transaction2 =
                transactionService.createTransaction("1020", "admin", new Date(), new BigDecimal(501.999));

        notNull(transaction2);
        notNull(transaction2.getId());

        transactionService.createAllocation(transaction1, transaction2, new BigDecimal(200.00), true, true);

        isTrue(TransactionUtils.getUnallocatedAmount(transaction1).compareTo(BigDecimal.ZERO) == 0);
        isTrue(TransactionUtils.getUnallocatedAmount(transaction2).compareTo(BigDecimal.ZERO) > 0);

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        transactions = TransactionUtils.removeFullyAllocatedTransactions(transactions);

        logger.info("Transaction list = " + transactions);

        notNull(transactions);
        notEmpty(transactions);
        isTrue(transactions.size() == 1);

        isTrue(transaction2.equals(transactions.get(0)));
        isTrue(new BigDecimal(301.999).compareTo(TransactionUtils.getUnallocatedAmount(transactions.get(0))) == 0);
    }

    @Test
    public void filterByUnallocatedAmount() throws Exception {

        Transaction transaction1 =
                transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(200.00));

        notNull(transaction1);
        notNull(transaction1.getId());

        Transaction transaction2 =
                transactionService.createTransaction("1020", "admin", new Date(), new BigDecimal(501.999));

        notNull(transaction2);
        notNull(transaction2.getId());

        transactionService.createAllocation(transaction1, transaction2, new BigDecimal(100.00), true, true);

        isTrue(TransactionUtils.getUnallocatedAmount(transaction1).compareTo(BigDecimal.ZERO) > 0);
        isTrue(TransactionUtils.getUnallocatedAmount(transaction2).compareTo(BigDecimal.ZERO) > 0);

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        transactions = TransactionUtils.filterByUnallocatedAmount(transactions, BigDecimal.ZERO, new BigDecimal(200));

        logger.info("Transaction list = " + transactions);

        notNull(transactions);
        notEmpty(transactions);
        isTrue(transactions.size() == 1);

        isTrue(transaction1.equals(transactions.get(0)));
        isTrue(new BigDecimal(100).compareTo(TransactionUtils.getUnallocatedAmount(transactions.get(0))) == 0);
    }

}
