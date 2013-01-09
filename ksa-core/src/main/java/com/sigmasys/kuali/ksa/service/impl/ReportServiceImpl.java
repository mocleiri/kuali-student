package com.sigmasys.kuali.ksa.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.transform.*;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.GeneralLedgerService;
import com.sigmasys.kuali.ksa.service.ReportService;
import com.sigmasys.kuali.ksa.transform.GeneralLedgerReport.GeneralLedgerReportEntry;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.JaxbUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;

import static com.sigmasys.kuali.ksa.transform.AgedBalanceReport.AgedAccountDetail;

/**
 * Report Service implementation.
 *
 * @author Michael Ivanov
 * @author Sergey Godunov
 */
@Service("reportService")
@Transactional(readOnly = true)
@WebService(serviceName = ReportService.SERVICE_NAME, portName = ReportService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class ReportServiceImpl extends GenericPersistenceService implements ReportService {

    private static final Log logger = LogFactory.getLog(ReportServiceImpl.class);

    private static final String REPORT_SCHEMA_LOCATION = "classpath*:/xsd/transaction-report.xsd";
    private static final String XML_SCHEMA_LOCATION = "classpath*:/xsd/xml.xsd";

    private XmlSchemaValidator schemaValidator;

    @Autowired
    private GeneralLedgerService glService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    private void postConstruct() {
        schemaValidator = new XmlSchemaValidator(XML_SCHEMA_LOCATION, REPORT_SCHEMA_LOCATION);
    }

    /**
     * Adds AOP advice to the current instance.
     */
    @Override
    @WebMethod(exclude = true)
    public List<Advice> getAdvices(BeanFactory beanFactory) {
        List<Advice> advices = super.getAdvices(beanFactory);
        if (advices == null) {
            advices = new LinkedList<Advice>();
        }
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Method method = invocation.getMethod();
                if (method.getReturnType() != null && method.getReturnType().equals(String.class)) {
                    String reportXml = (String) invocation.proceed();
                    // Validate XML against the schema
                    if (!schemaValidator.validateXml(reportXml)) {
                        String errMsg = "XML content is invalid:\n" + reportXml;
                        logger.error(errMsg);
                        throw new RuntimeException(errMsg);
                    }
                    return reportXml;
                }
                return invocation.proceed();
            }
        };
        advices.add(methodInterceptor);
        return advices;
    }


    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param glAccountId     GL Account ID
     * @param startDate       Start date
     * @param endDate         End date
     * @param transmittedOnly indicates whether only transmitted GL transactions should be retrieved or not
     * @return The generated report in XML
     */
    @Override
    public String generateGeneralLedgerReport(String glAccountId, Date startDate, Date endDate, boolean transmittedOnly) {

        List<GlTransaction> glTransactions = glService.getGlTransactions(startDate, endDate, glAccountId);

        ObjectFactory objectFactory = ObjectFactory.getInstance();

        GeneralLedgerReport glReport = objectFactory.createGeneralLedgerReport();

        if (CollectionUtils.isNotEmpty(glTransactions)) {
            for (GlTransaction glTransaction : glTransactions) {
                if (!transmittedOnly || GlTransactionStatus.COMPLETED.equals(glTransaction.getStatus())) {

                    GeneralLedgerReportEntry glReportEntry = new GeneralLedgerReportEntry();

                    Set<Transaction> transactions = glTransaction.getTransactions();
                    if (CollectionUtils.isNotEmpty(transactions)) {
                        List<String> transactionIds = new ArrayList<String>(transactions.size());
                        for (Transaction transaction : transactions) {
                            transactionIds.add(String.valueOf(transaction.getId()));
                        }
                        Collections.sort(transactionIds);
                        glReportEntry.getTransactionIdentifier().addAll(transactionIds);
                    }

                    glReportEntry.setGeneralLedgerAccount(glAccountId);
                    glReportEntry.setAmount(glTransaction.getAmount());
                    glReportEntry.setDate(CalendarUtils.toXmlGregorianCalendar(glTransaction.getDate()));
                    glReportEntry.setSystem(glTransaction.getDescription());

                    if (glTransaction.getStatus() != null) {
                        glReportEntry.setTransactionStatus(glTransaction.getStatus().getId());
                    }

                    if (glTransaction.getGlOperation() != null) {
                        glReportEntry.setGeneralLedgerOperation(glTransaction.getGlOperation().toString().toLowerCase());
                    }

                    GlTransmission glTransmission = glTransaction.getTransmission();
                    if (glTransmission != null) {

                        glReportEntry.setTransmissionIdentifier(String.valueOf(glTransmission.getId()));
                        glReportEntry.setBatch(glTransmission.getBatchId());
                        Date transmissionDate = glTransmission.getDate();
                        glReportEntry.setTransmissionDate(CalendarUtils.toXmlGregorianCalendar(transmissionDate, true));
                        glReportEntry.setTransmissionTime(CalendarUtils.toXmlGregorianCalendar(transmissionDate));
                        GlRecognitionPeriod recognitionPeriod = glTransmission.getRecognitionPeriod();

                        if (recognitionPeriod != null) {
                            glReportEntry.setTransmissionPeriod(recognitionPeriod.getCode());
                        }

                        if (glTransmission.getStatus() != null) {
                            glReportEntry.setTransmissionResult(glTransmission.getStatus().toString());
                        }
                    }

                    glReport.setGeneralLedgerReportEntry(glReportEntry);
                }
            }
        }

        glReport.setReportingPeriod(createReportingPeriod(startDate, endDate));

        return JaxbUtils.toXml(glReport);
    }

    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param glAccountId GL Account ID
     * @param startDate   Start date
     * @param endDate     End date
     * @return The generated report in XML
     */
    @Override
    @WebMethod(exclude = true)
    public String generateGeneralLedgerReport(String glAccountId, Date startDate, Date endDate) {
        return generateGeneralLedgerReport(glAccountId, startDate, endDate, false);
    }


    /**
     * An overloaded version of the <code>prepare1098TReport</code> method that produces a partial year federal 1098T form.
     * Note that the <code>dateFrom</code> and <code>dateTo</code> must fall within the <code>year</code>. Otherwise,
     * a runtime validation <code>IllegalArgumentException</code> will be thrown.
     *
     * @param accountId ID of an account for which to produce the report.
     * @param startDate Payments and Refunds tracking start date.
     * @param endDate   Payments and Refunds tracking end date date
     * @param year      Tax year.
     * @param ssnMask   The number of final digits of the social security number that will be stored in the local record.
     * @param noRecord  Whether to keep the record of the produces 1098T
     * @return String representation of an IRS 1098T form.
     * @throws IllegalArgumentException If <code>dateFrom</code> or <code>dateTo</code> are invalid or do not fall within the <code>year</code>.
     * @see Irs1098T
     */
    public String generate1098TReport(String accountId, Date startDate, Date endDate, Integer year, Integer ssnMask, boolean noRecord) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Returns an XML representation of a complete year federal 1098T form. Note that many of the parameters for producing a correct 1098T
     * must be established in the system preferences, and transactions must carry the appropriate tags in order to be counted correctly.
     * <br>
     * <code>ssnMask<code> is the number of final digits of the social security number that will be stored in the local record.
     * The XML file produced will contain the entire social security number, and it is imperative that the institution provide appropriate controls over this data.<br>
     * If <code>noRecord</code> is passed as true, then the system will not keep a record of the produced 1098T.
     * This option is ONLY to be used if the 1098T is being used to verify previous year's data, as the system does in order to complete the current year's return.
     * This form will automatically be returned as void, and a paper 1098T should not be produced from this data.
     * <br>
     *
     * @param accountId ID of an account for which to produce the report.
     * @param year      Tax year.
     * @param ssnMask   The number of final digits of the social security number that will be stored in the local record.
     * @param noRecord  Whether to keep the record of the produces 1098T
     * @return String representation of an IRS 1098T form.
     * @see Irs1098T
     */
    @Override
    @WebMethod(exclude = true)
    public String generate1098TReport(String accountId, Integer year, Integer ssnMask, boolean noRecord) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Produce an XML aged balance report for the accounts in the list. If <code>ageAccounts</code> is <code>true</code>, then each
     * account will be aged before the report is run. If <code>ignoreDeferments</code> is <code>true</code>, then deferments will be
     * ignored in the calculation of the balances, ONLY if <code>ageAccounts</code> is set to <code>true</code>.
     *
     * @param accountIds       List of account IDs for which to produce an Aged Balance report.
     * @param ignoreDeferments Whether to ignore deferments in calculations.
     * @param ageAccount       Whether to age account prior to producing this report.
     * @return String representation of an XML form report.
     * @see AgedBalanceReport
     */
    @Override
    public String generateAgedBalanceReport(List<String> accountIds, boolean ignoreDeferments, boolean ageAccount) {

        // Create a new report object:
        AgedBalanceReport report = new AgedBalanceReport();
        List<Object> detailsList = report.getAgeBreakdownAndAgedAccountDetail();
        XMLGregorianCalendar reportDate = CalendarUtils.toXmlGregorianCalendar(new Date());

        // Set the report date to current date:
        report.setDateOfReport(reportDate);

        // Iterate through the account IDs:
        for (String accountId : accountIds) {
            // Get the account:
            Account account = accountService.getOrCreateAccount(accountId);

            // Optionally age the account's debt:
            if (ageAccount) {
                accountService.ageDebt(accountId, ignoreDeferments);
            }

            // Try to find a matching AgeBreakdown:
            int indexOfExistingAgeBreakdown = indexOfAgeBreakdown(detailsList, account);

            // Create a new AgeBreakdown if there is no match:
            if (indexOfExistingAgeBreakdown == -1) {
                LatePeriod latePeriod = account.getLatePeriod();

                if (latePeriod != null) {
                    AgedBalanceReport.AgeBreakdown ageBreakdown = new AgedBalanceReport.AgeBreakdown();

                    ageBreakdown.setPeriod1(latePeriod.getDaysLate1());
                    ageBreakdown.setPeriod2(latePeriod.getDaysLate2());
                    ageBreakdown.setPeriod3(latePeriod.getDaysLate3());
                    detailsList.add(ageBreakdown);
                }
            }

            // Create a new AgedAccountDetail:
            AgedAccountDetail agedAccountDetail = new AgedAccountDetail();

            agedAccountDetail.setAccountIdentifier(accountId);
            agedAccountDetail.setAccountName(account.getCompositeDefaultPersonName());

            // Set amounts late:
            if (account instanceof ChargeableAccount) {

                AgedAccountDetail.AccountBalances accountBalances = new AgedAccountDetail.AccountBalances();

                accountBalances.setPeriod1Balance(((ChargeableAccount) account).getAmountLate1());
                accountBalances.setPeriod2Balance(((ChargeableAccount) account).getAmountLate2());
                accountBalances.setPeriod3Balance(((ChargeableAccount) account).getAmountLate3());
                agedAccountDetail.setAccountBalances(accountBalances);
            }

            // Add the ageAccountDetail to the list of all details:
            if (indexOfExistingAgeBreakdown == -1) {
                // Append at the end:
                detailsList.add(agedAccountDetail);
            } else {
                // Insert after the AgeBreakdown object:
                detailsList.add(indexOfExistingAgeBreakdown + 1, agedAccountDetail);
            }
        }

        return JaxbUtils.toXml(report);
    }


    /**
     * An overloaded method to produce a failed transaction report for all batches.
     *
     * @param startDate  Start date of transaction period tracking.
     * @param endDate    End date of transaction period tracking.
     * @param failedOnly Include only transactions that failed on their own.
     * @return String representation of an XML report.
     * @see FailedTransactionsReport
     */
    @Override
    @WebMethod(exclude = true)
    public String generateRejectedTransactionsReport(Date startDate, Date endDate, boolean failedOnly) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Checks the batch records for transactions that have been sent and rejected. The method can filter by
     * date range, and by entity. If no entity is passed, all batches in the date range will be included. If
     * failedOnly is set to true, only the transactions that failed on their own will be reported (i.e. if a whole
     * batch was rejected due to a failure, the transactions that would have posted ok will not be reported.) If
     * failedOnly is true, all transactions that were not accepted, even those which were inherently "valid" will
     * be reported.
     *
     * @param batchId    Batch transaction ID.
     * @param startDate  Start date of transaction period tracking.
     * @param endDate    End date of transaction period tracking.
     * @param failedOnly Include only transactions that failed on their own.
     * @return String representation of an XML report.
     * @see FailedTransactionsReport
     */
    @Override
    public String generateRejectedTransactionsReport(String batchId, Date startDate, Date endDate, boolean failedOnly) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Return an XML account report for the given account in the <code>accountId</code> list, between the dates listed.
     * If <code>details</code> is <code>true</code>, then all transactions will also be reported.
     * If <code>paymentApplication</code> is <code>true</code> then <code>paymentApplication</code> method will be run
     * before the account is reported.
     * If <code>ageAccount</code> is <code>true</code>, the account will be aged before the report is generated.
     *
     * @param accountId          List of Account IDs for which to produce a report.
     * @param startDate          Start date of reporting period.
     * @param endDate            End date of reporting period.
     * @param details            Whether to report all transactions.
     * @param paymentApplication Whether to run "paymentApplication" before executing the report.
     * @param ageAccount         Whether to age the account before generating a report.
     * @param ignoreDeferments   Whether to ignore deferments.
     * @return String representation of an account report.
     * @see AccountReport
     */
    @Override
    public String generateAccountReport(String accountId, Date startDate, Date endDate, boolean details,
                                        boolean paymentApplication, boolean ageAccount, boolean ignoreDeferments) {

        Account account = accountService.getFullAccount(accountId);
        if (account == null) {
            String errMsg = "Account with ID = " + accountId + " does not exist";
            logger.error(errMsg);
            throw new UserNotFoundException(errMsg);
        }

        if (paymentApplication) {
            accountService.paymentApplication(accountId);
        }

        if (ageAccount) {
            accountService.ageDebt(accountId, ignoreDeferments);
        }

        BigDecimal netBalance = BigDecimal.ZERO;
        BigDecimal chargeAmount = BigDecimal.ZERO;
        BigDecimal paymentAmount = BigDecimal.ZERO;
        BigDecimal defermentAmount = BigDecimal.ZERO;
        BigDecimal unallocatedAmount = BigDecimal.ZERO;
        BigDecimal writtenOffAmount = BigDecimal.ZERO;

        List<Transaction> transactions = transactionService.getTransactions(accountId, startDate, startDate);
        for (Transaction transaction : transactions) {
            TransactionTypeValue transactionType = transaction.getTransactionTypeValue();
            BigDecimal unallocatedTransactionAmount = transactionService.getUnallocatedAmount(transaction);
            if (transactionType == TransactionTypeValue.CHARGE) {
                if (TransactionStatus.WRITTEN_OFF.equals(transaction.getStatus())) {
                    writtenOffAmount = writtenOffAmount.add(transaction.getAmount());
                }
                unallocatedAmount = unallocatedAmount.add(unallocatedTransactionAmount);
                netBalance = netBalance.add(unallocatedTransactionAmount);
                chargeAmount = chargeAmount.add(transaction.getAmount());
            } else if (transactionType == TransactionTypeValue.PAYMENT) {
                unallocatedAmount = unallocatedAmount.add(unallocatedTransactionAmount);
                netBalance = netBalance.subtract(unallocatedTransactionAmount);
                paymentAmount = paymentAmount.add(transaction.getAmount());
            } else if (!ignoreDeferments && transactionType == TransactionTypeValue.DEFERMENT) {
                unallocatedAmount = unallocatedAmount.add(unallocatedTransactionAmount);
                netBalance = netBalance.subtract(unallocatedTransactionAmount);
                defermentAmount = defermentAmount.add(transaction.getAmount());
            }
        }

        ObjectFactory objectFactory = ObjectFactory.getInstance();

        AccountReport accountReport = objectFactory.createAccountReport();
        accountReport.setAccountIdentifier(accountId);
        accountReport.setReportingPeriod(createReportingPeriod(startDate, endDate));

        AccountReport.Balances balances = objectFactory.createAccountReportBalances();

        if (ageAccount && account instanceof ChargeableAccount) {
            ChargeableAccount chargeableAccount = (ChargeableAccount) account;
            LatePeriod latePeriod = account.getLatePeriod();
            if (latePeriod != null) {
                AccountReport.Balances.AgedBalance agedBalance = objectFactory.createAccountReportBalancesAgedBalance();
                agedBalance.getPeriodLengthAndPeriodBalance().add(latePeriod.getDaysLate1());
                agedBalance.getPeriodLengthAndPeriodBalance().add(chargeableAccount.getAmountLate1());
                agedBalance.getPeriodLengthAndPeriodBalance().add(latePeriod.getDaysLate2());
                agedBalance.getPeriodLengthAndPeriodBalance().add(chargeableAccount.getAmountLate2());
                agedBalance.getPeriodLengthAndPeriodBalance().add(latePeriod.getDaysLate3());
                agedBalance.getPeriodLengthAndPeriodBalance().add(chargeableAccount.getAmountLate3());
                balances.setAgedBalance(agedBalance);
            }
        }

        balances.setNetBalance(netBalance);
        balances.setTotalCharges(chargeAmount);
        balances.setTotalPayments(paymentAmount);
        balances.setTotalPayments(defermentAmount);
        balances.setWrittenOff(writtenOffAmount);
        balances.setUnallocatedBalance(unallocatedAmount);

        balances.setPriorBalance(BigDecimal.ZERO);
        balances.setFutureBalance(BigDecimal.ZERO);

        accountReport.setBalances(balances);

        return JaxbUtils.toXml(accountReport);

    }


    /**
     * Produces a receipt for a transaction with the specified ID.
     *
     * @param transactionId ID of a transaction for which to produce a receipt.
     * @return String representation of a transaction XML receipt.
     * @see Receipt
     */
    @Override
    public String generateReceipt(Long transactionId) {
        // TODO Auto-generated method stub
        return null;
    }


/* =====================================================================
*
* Helper methods.
*
* ==================================================================== */

    /**
     * Tries to find an object of type <code>AgedBalanceReport.AgeBreakdown</code> that
     * has Late Dates 1 through 3 matching the given Account. Returns the index of a
     * matching object in the given list.
     *
     * @param ageBreakdownAndAgeAccountDetailList
     *                A list of report detail objects.
     * @param account Account to match its late date periods.
     * @return Index of a matching <code>AgedBalanceReport.AgeBreakdown</code> object.
     */
    private int indexOfAgeBreakdown(List<Object> ageBreakdownAndAgeAccountDetailList, Account account) {
        // Get the Account's LatePeriod object:
        LatePeriod latePeriod = account.getLatePeriod();

        if (latePeriod != null) {
            // Iterate through the list and try to match an AgeBreakdown:
            for (int i = 0, sz = ageBreakdownAndAgeAccountDetailList.size(); i < sz; i++) {
                Object o = ageBreakdownAndAgeAccountDetailList.get(i);

                if (o instanceof AgedBalanceReport.AgeBreakdown) {
                    AgedBalanceReport.AgeBreakdown ageBreakdown = (AgedBalanceReport.AgeBreakdown) o;

                    if ((safeCompare(ageBreakdown.getPeriod1(), latePeriod.getDaysLate1()) == 0)
                            && (safeCompare(ageBreakdown.getPeriod2(), latePeriod.getDaysLate2()) == 0)
                            && (safeCompare(ageBreakdown.getPeriod3(), latePeriod.getDaysLate3()) == 0)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Safely compares a primitive int to an Integer wrapper object.
     * If Integer is null, returns 1.
     *
     * @param i  Primitive
     * @param ii Integer.
     * @return Comparison result.
     */
    private int safeCompare(int i, Integer ii) {
        return (ii != null) ? ii.compareTo(i) : 1;
    }

    private ReportingPeriod createReportingPeriod(Date startDate, Date endDate) {
        ReportingPeriod reportingPeriod = ObjectFactory.getInstance().createReportingPeriod();
        reportingPeriod.setStartDate(CalendarUtils.toXmlGregorianCalendar(startDate, true));
        reportingPeriod.setEndDate(CalendarUtils.toXmlGregorianCalendar(endDate, true));
        return reportingPeriod;
    }
}
