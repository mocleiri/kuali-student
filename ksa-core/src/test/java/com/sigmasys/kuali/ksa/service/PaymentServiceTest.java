package com.sigmasys.kuali.ksa.service;


import com.google.gwt.user.client.rpc.core.java.math.BigDecimal_CustomFieldSerializer;
import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.sigmasys.kuali.ksa.util.TransactionUtils.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class PaymentServiceTest extends AbstractServiceTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    private GeneralLedgerService glService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;


    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    @Test
    public void applyPayments1() throws Exception {

        List<Transaction> transactions = new LinkedList<Transaction>();

        transactions.add(transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(10e5)));
        transactions.add(transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(10e3)));
        transactions.add(transactionService.createTransaction("1020", "admin", new Date(), new BigDecimal(10e8)));
        transactions.add(transactionService.createTransaction("ach", "admin", new Date(), new BigDecimal(300.07)));
        transactions.add(transactionService.createTransaction("1292", "admin", new Date(), new BigDecimal(0.11)));
        transactions.add(transactionService.createTransaction("ach", "admin", new Date(), new BigDecimal(524.39)));
        transactions.add(transactionService.createTransaction("pp", "admin", new Date(), new BigDecimal(99.01)));
        transactions.add(transactionService.createTransaction("chip", "admin", new Date(), new BigDecimal(230.34)));
        transactions.add(transactionService.createTransaction("1310", "admin", new Date(), new BigDecimal(35.99)));

        List<GlTransaction> glTransactions = paymentService.applyPayments(transactions, new BigDecimal(7000.09), false);

        Assert.notEmpty(glTransactions);

        for (GlTransaction glTransaction : glTransactions) {
            Assert.notNull(glTransaction.getId());
            Assert.notNull(glTransaction.getGlOperation());
            Assert.notNull(glTransaction.getStatus());
            Assert.isTrue(GlTransactionStatus.WAITING == glTransaction.getStatus());
        }

    }

    @Test
    public void applyPayments2() throws Exception {

        List<Transaction> transactions = new LinkedList<Transaction>();

        transactions.add(transactionService.createTransaction("1310", "admin", new Date(), new BigDecimal(-35.99)));
        transactions.add(transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(10e11)));
        transactions.add(transactionService.createTransaction("cash", "admin", new Date(), new BigDecimal(-10e3)));
        transactions.add(transactionService.createTransaction("1540", "admin", new Date(), new BigDecimal(10e8)));
        transactions.add(transactionService.createTransaction("ach", "admin", new Date(), new BigDecimal(300.07)));
        transactions.add(transactionService.createTransaction("1292", "admin", new Date(), new BigDecimal(-0.98)));
        transactions.add(transactionService.createTransaction("ach", "admin", new Date(), new BigDecimal(524.39)));
        transactions.add(transactionService.createTransaction("pp", "admin", new Date(), new BigDecimal(-99.01)));
        transactions.add(transactionService.createTransaction("chip", "admin", new Date(), new BigDecimal(230.34)));
        transactions.add(transactionService.createTransaction("1539", "admin", new Date(), new BigDecimal(35.99)));

        List<GlTransaction> glTransactions = paymentService.applyPayments(transactions, new BigDecimal(300.09), true);

        Assert.notEmpty(glTransactions);

        for (GlTransaction glTransaction : glTransactions) {
            Assert.notNull(glTransaction.getId());
            Assert.notNull(glTransaction.getGlOperation());
            Assert.notNull(glTransaction.getStatus());
            Assert.isTrue(GlTransactionStatus.QUEUED == glTransaction.getStatus());
        }

    }

    @Test
    public void paymentApplication() throws Exception {

        String userId = "admin";

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);
        Date transactionDate = dateFormat.parse("12/12/2012");

        transactionService.createTransaction("1310", userId, transactionDate, new BigDecimal(-350.99));
        transactionService.createTransaction("cash", userId, transactionDate, new BigDecimal(10e11));
        transactionService.createTransaction("cash", userId, transactionDate, new BigDecimal(-10e3));
        transactionService.createTransaction("1540", userId, transactionDate, new BigDecimal(10e8));
        transactionService.createTransaction("ach", userId, transactionDate, new BigDecimal(78800.07));
        transactionService.createTransaction("1292", userId, transactionDate, new BigDecimal(-0.98));
        transactionService.createTransaction("ach", userId, transactionDate, new BigDecimal(524.39));
        transactionService.createTransaction("pp", userId, transactionDate, new BigDecimal(-998.01));
        transactionService.createTransaction("chip", userId, transactionDate, new BigDecimal(100111.34));
        transactionService.createTransaction("finaid", userId, transactionDate, new BigDecimal(20000.88));
        transactionService.createTransaction("finaid2", userId, transactionDate, new BigDecimal(5500));

        // Start

        String paramValue = configService.getParameter(Constants.KSA_PAYMENT_FINAID_MAX_AMOUNT);
        if (StringUtils.isBlank(paramValue)) {
            String errMsg = "Configuration parameter '" + Constants.KSA_PAYMENT_FINAID_MAX_AMOUNT + "' is required";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        final BigDecimal maxAmount = new BigDecimal(paramValue);

        Integer[] paymentYears = paymentService.getPaymentYears();

        logger.info("Payment years: " + paymentYears);

        int minYear = paymentYears[paymentYears.length - 1];
        int maxYear = paymentYears[0];

        Date startDate = CalendarUtils.getFirstDateOfYear(minYear);
        Date endDate = CalendarUtils.getLastDateOfYear(maxYear);

        List<Transaction> transactions = transactionService.getTransactions(userId, startDate, endDate);

        logger.debug("All transactions: " + transactions);

        List<GlTransaction> generatedGlTransactions = new LinkedList<GlTransaction>();

        List<GlTransaction> glTransactions = transactionService.removeAllocations(transactions);

        generatedGlTransactions.addAll(glTransactions);

        glTransactions = transactionService.allocateReversals(transactions);

        generatedGlTransactions.addAll(glTransactions);

        Map<Integer, List<Transaction>> transactionsByYears = sortTransactionsByYears(transactions, paymentYears);

        logger.debug("Transactions by years: " + transactionsByYears);

        List<Transaction> remainingChargesAndPayments = new LinkedList<Transaction>();

        for (Map.Entry<Integer, List<Transaction>> entry : transactionsByYears.entrySet()) {

            int currentYear = entry.getKey();

            List<Transaction> transactionsForYear = entry.getValue();

            Map<TransactionTypeValue, List<Transaction>> transactionMap = sortTransactionsByTypes(transactionsForYear,
                    TransactionTypeValue.CHARGE, TransactionTypeValue.PAYMENT);

            List<Transaction> chargesForYear = transactionMap.get(TransactionTypeValue.CHARGE);
            List<Transaction> paymentsForYear = transactionMap.get(TransactionTypeValue.PAYMENT);

            Map<String, List<Transaction>> finAidPaymentMap = sortTransactionsByTags(transactionsForYear,
                    Constants.KSA_PAYMENT_TAG_FINAID);

            List<Transaction> finAidPaymentsForYear = finAidPaymentMap.get(Constants.KSA_PAYMENT_TAG_FINAID);

            List<Transaction> chargesAndFinAidPayments = union(chargesForYear, finAidPaymentsForYear);

            glTransactions = paymentService.applyPayments(chargesAndFinAidPayments);

            generatedGlTransactions.addAll(glTransactions);

            // Applying the rest of financial aid payments to charges for the previous years
            for (int year : paymentYears) {

                if (currentYear > year) {

                    // Collecting all FinAid payments with non-zero amounts
                    List<Transaction> remainingFinAidPayments = new LinkedList<Transaction>();
                    for (Transaction finAidPayment : finAidPaymentsForYear) {
                        if (finAidPayment.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            logger.info("Adding FinAid Payment(id = " + finAidPayment.getId() +
                                    ", amount = " + finAidPayment.getAmount() +
                                    ") to the list of remaining FinAid payments...");
                            remainingFinAidPayments.add(finAidPayment);
                        }
                    }

                    logger.info("The number of remaining FinAid payments for " + currentYear + " year is " +
                            remainingFinAidPayments.size());

                    if (!remainingFinAidPayments.isEmpty()) {

                        Map<TransactionTypeValue, List<Transaction>> chargeMap =
                                sortTransactionsByTypes(transactionsByYears.get(year), TransactionTypeValue.CHARGE);

                        List<Transaction> chargesForPrevYear = chargeMap.get(TransactionTypeValue.CHARGE);

                        if (chargesForPrevYear != null && !chargesForPrevYear.isEmpty()) {
                            List<Transaction> finAidTransactions = union(chargesForYear, finAidPaymentsForYear);
                            glTransactions = paymentService.applyPayments(finAidTransactions, maxAmount);
                            logger.info("Generated FinAid GL transactions for " + year + " year: \n" + glTransactions);
                            generatedGlTransactions.addAll(glTransactions);
                        }
                    }
                }
            }

            // Subtracting financial aid payments from all the payments and store the result
            remainingChargesAndPayments.addAll(subtract(paymentsForYear, finAidPaymentsForYear));

            // Adding charges to remaining payments
            remainingChargesAndPayments.addAll(chargesForYear);

        }

        calculateMatrixScores(remainingChargesAndPayments);

        orderByMatrixScore(remainingChargesAndPayments, true);

        glTransactions = paymentService.applyPayments(remainingChargesAndPayments);

        generatedGlTransactions.addAll(glTransactions);

        generatedGlTransactions = glService.summarizeGlTransactions(generatedGlTransactions);

        // End
        logger.info("The number of generated GL transactions is " + generatedGlTransactions.size());
        logger.info("Generated GL transactions: \n" + generatedGlTransactions);

        Assert.notNull(generatedGlTransactions);
        Assert.notEmpty(generatedGlTransactions);

    }

}