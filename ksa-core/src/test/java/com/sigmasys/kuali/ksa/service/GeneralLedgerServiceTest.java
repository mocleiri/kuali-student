package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class GeneralLedgerServiceTest extends AbstractServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GeneralLedgerService glService;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    private GlTransaction createGlTransaction(String userId) {
        Transaction transaction = transactionService.createTransaction("1020", userId, new Date(), new BigDecimal(10e5));
        return glService.createGlTransaction(transaction.getId(), userId, new BigDecimal(10e4), GlOperationType.DEBIT);
    }

    @Test
    public void createGlTransaction() throws Exception {

        GlTransaction glTransaction = createGlTransaction("admin");

        Assert.notNull(glTransaction);
        Assert.notNull(glTransaction.getId());
        Assert.notNull(glTransaction.getGlAccountId());
        Assert.hasLength(glTransaction.getDescription());

        Assert.isTrue(GlOperationType.DEBIT == glTransaction.getGlOperation());

    }

    @Test
    public void setRecognitionPeriod() throws Exception {

        GlTransaction glTransaction = createGlTransaction("admin");

        int offset = 20000;
        glService.setRecognitionPeriod("SR062012", new Date(System.currentTimeMillis() - offset),
                new Date(System.currentTimeMillis() - offset));

        // TODO: add assertions

    }


}
