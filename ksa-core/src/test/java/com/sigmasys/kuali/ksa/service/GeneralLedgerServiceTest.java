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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
        transaction1 = transactionService.createTransaction("1020", userId, new Date(), new BigDecimal(10e7));
        transaction2 = transactionService.createTransaction("cash", userId, new Date(), new BigDecimal(300.99));
        transaction3 = transactionService.createTransaction("chip", userId, new Date(), new BigDecimal(77777.980));
    }

    private GlTransaction createGlTransaction(Transaction transaction, String userId) {
        return glService.createGlTransaction(transaction.getId(), userId, new BigDecimal(10e4), GlOperationType.DEBIT);
    }

    @Test
    public void createGlTransaction() throws Exception {

        GlTransaction glTransaction = createGlTransaction(transaction1, "admin");

        Assert.notNull(glTransaction);
        Assert.notNull(glTransaction.getId());
        Assert.notNull(glTransaction.getGlAccountId());
        Assert.hasLength(glTransaction.getDescription());

        Assert.isTrue(GlOperationType.DEBIT == glTransaction.getGlOperation());

    }

    @Test
    public void summarizeGlTransactions() throws Exception {

        GlTransaction glTransaction2 = createGlTransaction(transaction2, "admin");
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        GlTransaction glTransaction3 = createGlTransaction(transaction3, "admin");
        Assert.notNull(glTransaction3);
        Assert.notNull(glTransaction3.getId());
        Assert.notNull(glTransaction3.getStatus());
        Assert.notNull(glTransaction3.getRecognitionPeriod());

        List<GlTransaction> glTransactions =
                glService.summarizeGlTransactions(Arrays.asList(glTransaction2, glTransaction3));

        Assert.notNull(glTransactions);
        Assert.notEmpty(glTransactions);

    }

    @Test
    public void prepareGlTransmission1() throws Exception {

        GlTransaction glTransaction1 = createGlTransaction(transaction1, "admin");
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction2, "admin");
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        glService.prepareGlTransmissions();

    }

    @Test
    public void prepareGlTransmission2() throws Exception {

        GlTransaction glTransaction1 = createGlTransaction(transaction2, "admin");
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction3, "admin");
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        glService.prepareGlTransmissions("SR2012", "SR2013");

    }

    @Test
    public void prepareGlTransmission3() throws Exception {

        GlTransaction glTransaction1 = createGlTransaction(transaction2, "admin");
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction3, "admin");
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date startDate = dateFormat.parse("01/01/2001");
        Date endDate = dateFormat.parse("01/01/2014");

        glService.prepareGlTransmissionsForEffectiveDates(startDate, endDate);

    }

    @Test
    public void prepareGlTransmission4() throws Exception {

        GlTransaction glTransaction1 = createGlTransaction(transaction1, "admin");
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction3, "admin");
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date startDate = dateFormat.parse("01/01/2012");
        Date endDate = dateFormat.parse("01/01/2015");

        glService.prepareGlTransmissionsForRecognitionDates(startDate, endDate);

    }

    @Test
    public void createGeneralLedgerType() throws Exception {

        GeneralLedgerType glType = glService.createGeneralLedgerType("GL_TYPE1", "Test GL type1",
                "Test GL Description 1", "01-0-13112 1326", GlOperationType.CREDIT);

        Assert.notNull(glType);
        Assert.notNull(glType.getId());
        Assert.notNull(glType.getCode());
        Assert.notNull(glType.getName());
        Assert.notNull(glType.getDescription());
        Assert.notNull(glType.getGlAccountId());
        Assert.notNull(glType.getGlOperationOnCharge());
        Assert.notNull(glType.getCreationDate());
        Assert.notNull(glType.getCreatorId());

        Assert.isTrue(GlOperationType.CREDIT == glType.getGlOperationOnCharge());

    }

    @Test
    public void persistGeneralLedgerType() throws Exception {

        GeneralLedgerType glType = glService.createGeneralLedgerType("GL_TYPE1", "Test GL type1",
                "Test GL Description 1", "01-0-13112 1326", GlOperationType.CREDIT);

        Assert.notNull(glType);
        Assert.notNull(glType.getId());
        Assert.notNull(glType.getCode());
        Assert.notNull(glType.getName());
        Assert.notNull(glType.getDescription());
        Assert.notNull(glType.getGlAccountId());
        Assert.notNull(glType.getGlOperationOnCharge());
        Assert.notNull(glType.getCreationDate());
        Assert.notNull(glType.getCreatorId());

        Assert.isTrue(GlOperationType.CREDIT == glType.getGlOperationOnCharge());

        glType.setCode("GL_TYPE2");
        glType.setGlOperationOnCharge(GlOperationType.DEBIT);

        Long glTypeId = glService.persistGeneralLedgerType(glType);

        Assert.notNull(glTypeId);
        Assert.notNull(glType.getId());

        Assert.isTrue(glTypeId.equals(glTypeId));
        Assert.isTrue("GL_TYPE2".equals(glType.getCode()));
        Assert.isTrue("Test GL type1".equals(glType.getName()));
        Assert.isTrue(GlOperationType.DEBIT == glType.getGlOperationOnCharge());

    }

    @Test
    public void getGeneralLedgerTypes() throws Exception {

        GeneralLedgerType glType = glService.createGeneralLedgerType("GL_TYPE10000000", "Test GL type2",
                "Test GL Description 2", "01-0-13112 3456", GlOperationType.DEBIT);

        Assert.notNull(glType);
        Assert.notNull(glType.getId());
        Assert.notNull(glType.getCode());
        Assert.notNull(glType.getName());
        Assert.notNull(glType.getDescription());
        Assert.notNull(glType.getGlAccountId());
        Assert.notNull(glType.getGlOperationOnCharge());
        Assert.notNull(glType.getCreationDate());
        Assert.notNull(glType.getCreatorId());

        Assert.isTrue(GlOperationType.DEBIT == glType.getGlOperationOnCharge());

        List<GeneralLedgerType> glTypes = glService.getGeneralLedgerTypes();

        Assert.notNull(glTypes);
        Assert.notEmpty(glTypes);

        for (GeneralLedgerType type : glTypes) {

            logger.info("General Ledger Type = " + type);

            Assert.notNull(type);
            Assert.notNull(type.getId());
            Assert.notNull(type.getCode());
            Assert.notNull(type.getGlOperationOnCharge());
            Assert.notNull(type.getGlAccountId());
        }

    }

}
