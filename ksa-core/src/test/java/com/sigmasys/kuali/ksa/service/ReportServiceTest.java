package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.GlTransaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class ReportServiceTest extends GeneralLedgerServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionExportService transactionExportService;

    @Autowired
    private GeneralLedgerService glService;


    @Before
    public void setUpWithinTransaction() {
        super.setUpWithinTransaction();
    }

    @Test
    public void generateGeneralLedgerReport() throws Exception {

        final String glAccountId = "01-0-13112 3456";

        GlTransaction glTransaction1 = createGlTransaction(transaction1, glAccountId);
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction2, glAccountId);
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        GlTransaction glTransaction3 = createGlTransaction(transaction2, glAccountId);
        Assert.notNull(glTransaction3);
        Assert.notNull(glTransaction3.getId());
        Assert.notNull(glTransaction3.getStatus());
        Assert.notNull(glTransaction3.getRecognitionPeriod());


        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date startDate = dateFormat.parse("01/01/1970");
        Date endDate = dateFormat.parse("01/01/2020");

        String xml = reportService.generateGeneralLedgerReport(startDate, endDate, glAccountId);

        logger.debug("GL Report: \n" + xml);

        Assert.notNull(xml);
        Assert.hasText(xml);

    }

    @Test
    public void generateGeneralLedgerReport2() throws Exception {

        final String glAccountId = "01-0-13112 3456";

        GlTransaction glTransaction1 = createGlTransaction(transaction1, glAccountId);
        Assert.notNull(glTransaction1);
        Assert.notNull(glTransaction1.getId());
        Assert.notNull(glTransaction1.getStatus());
        Assert.notNull(glTransaction1.getRecognitionPeriod());

        GlTransaction glTransaction2 = createGlTransaction(transaction2, glAccountId);
        Assert.notNull(glTransaction2);
        Assert.notNull(glTransaction2.getId());
        Assert.notNull(glTransaction2.getStatus());
        Assert.notNull(glTransaction2.getRecognitionPeriod());

        transactionService.makeEffective(transaction1.getId(), true);
        transactionService.makeEffective(transaction2.getId(), true);

        glService.prepareGlTransmissions();

        String xml = transactionExportService.exportTransactions();

        logger.debug("Transaction export: \n" + xml);

        Assert.notNull(xml);
        Assert.hasText(xml);

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date startDate = dateFormat.parse("12/01/2010");
        Date endDate = dateFormat.parse("12/01/2015");

        xml = reportService.generateGeneralLedgerReport(startDate, endDate, glAccountId, true);

        logger.debug("GL Report: \n" + xml);

        Assert.notNull(xml);
        Assert.hasText(xml);

    }

}