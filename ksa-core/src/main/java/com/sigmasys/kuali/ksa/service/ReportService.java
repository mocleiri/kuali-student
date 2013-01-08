package com.sigmasys.kuali.ksa.service;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.transform.AccountReport;
import com.sigmasys.kuali.ksa.transform.AgedBalanceReport;
import com.sigmasys.kuali.ksa.transform.FailedTransactionsReport;
import com.sigmasys.kuali.ksa.transform.Irs1098T;
import com.sigmasys.kuali.ksa.transform.Receipt;

/**
 * This interface represents a collection of useful KSA reporting methods to 
 * produce reports as described in the "Process Diagrams" document. 
 * Reports being produces are targeted to show the current state of the accounts
 * and transactions as well as payments for the purposes of IRS reporting. 
 * <p/>
 *
 * @author Michael Ivanov
 * @author Sergey Godunov
 */
@Url(ReportService.SERVICE_URL)
@WebService(serviceName = ReportService.SERVICE_NAME, portName = ReportService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface ReportService {

    String SERVICE_URL = "report.webservice";
    String SERVICE_NAME = "ReportService";
    String PORT_NAME = SERVICE_NAME + "Port";

    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param startDate       Start date
     * @param endDate         End date
     * @param glAccountId     GL Account ID
     * @param transmittedOnly indicates whether only transmitted GL transactions should be retrieved or not
     * @return The generated report in XML
     */
    String generateGeneralLedgerReport(Date startDate, Date endDate, String glAccountId, boolean transmittedOnly);

    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param startDate   Start date
     * @param endDate     End date
     * @param glAccountId GL Account ID
     * @return The generated report in XML
     */
    @WebMethod(exclude = true)
    String generateGeneralLedgerReport(Date startDate, Date endDate, String glAccountId);

	/**
	 * An overloaded version of the <code>prepare1098TReport</code> method that produces a partial year federal 1098T form.
	 * Note that the <code>dateFrom</code> and <code>dateTo</code> must fall within the <code>year</code>. Otherwise, 
	 * a runtime validation <code>IllegalArgumentException</code> will be thrown.
	 * 
	 * @param accountId ID of an account for which to produce the report.
	 * @param year Tax year.
	 * @param ssnMask The number of final digits of the social security number that will be stored in the local record. 
	 * @param noRecord Whether to keep the record of the produces 1098T
	 * @param dateFrom Payments and Refunds tracking start date.
	 * @param dateTo Payments and Refunds tracking end date date
	 * @return String representation of an IRS 1098T form.
	 * @see Irs1098T
	 * @throws IllegalArgumentException If <code>dateFrom</code> or <code>dateTo</code> are invalid or do not fall within the <code>year</code>.
	 */
	String generate1098TReport(String accountId, Integer year, Integer ssnMask, Boolean noRecord, Date dateFrom, Date dateTo);
	
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
	 * @param year Tax year.
	 * @param ssnMask The number of final digits of the social security number that will be stored in the local record. 
	 * @param noRecord Whether to keep the record of the produces 1098T
	 * @return String representation of an IRS 1098T form.
	 * @see Irs1098T
	 */
	@WebMethod(exclude = true)
	String generate1098TReport(String accountId, Integer year, Integer ssnMask, Boolean noRecord);
	
	/**
	 * Produce an XML aged balance report for the accounts in the list. If <code>ageAccounts</code> is <code>true</code>, then each
	 * account will be aged before the report is run. If <code>ignoreDeferments</code> is <code>true</code>, then deferments will be
	 * ignored in the calculation of the balances, ONLY if <code>ageAccounts</code> is set to <code>true</code>.
	 * 
	 * @param accountIds List of account IDs for which to produce an Aged Balance report.
	 * @param ignoreDeferments Whether to ignore deferments in calculations.
	 * @param ageAccount Whether to age account prior to producing this report.
	 * @return String representation of an XML form report.
	 * @see AgedBalanceReport
	 */
	String generateAgedBalanceReport(List<String> accountIds, Boolean ignoreDeferments, Boolean ageAccount);

	/**
	 * An overloaded method to produce a failed transaction report for all batches.
	 * 
	 * @param dateFrom Start date of transaction period tracking.
	 * @param dateTo End date of transaction period tracking.
	 * @param onlyFailed Include only transactions that failed on their own.
	 * @return String representation of an XML report.
	 * @see FailedTransactionsReport
	 */
	@WebMethod(exclude = true)
	String generateFailedTransactionReport(Date dateFrom, Date dateTo, Boolean onlyFailed);
	
	/**
	 * Checks the batch records for transactions that have been sent and rejected. The method can filter by
	 * date range, and by entity. If no entity is passed, all batches in the date range will be included. If
	 * onlyFailed is set to true, only the transactions that failed on their own will be reported (i.e. if a whole
	 * batch was rejected due to a failure, the transactions that would have posted ok will not be reported.) If
	 * onlyFailed is true, all transactions that were not accepted, even those which were inherently "valid" will
	 * be reported.
	 * 
	 * @param dateFrom Start date of transaction period tracking.
	 * @param dateTo End date of transaction period tracking.
	 * @param onlyFailed Include only transactions that failed on their own.
	 * @param entityId Batch transaction ID.
	 * @return String representation of an XML report.
	 * @see FailedTransactionsReport
	 */
	String generateFailedTransactionReport(Date dateFrom, Date dateTo, Boolean onlyFailed, String entityId);
	
	/**
	 * Return an XML account report for the account id, between the dates listed. 
	 * If <code>details</code> is <code>true</code>, then all transactions will also be reported. 
	 * If <code>paymentApplication</code> is <code>true</code> then <code>paymentApplication</code> method will be run before the account is reported. 
	 * If <code>ageAccount</code> is <code>true</code>, the account will be aged before the report is generated.
	 * 
	 * @param accountId ID of an account for which to produce a report.
	 * @param dateFrom Start date of reporting period.
	 * @param dateTo End date of reporting period.
	 * @param details Whether to report all transactions.
	 * @param paymentApplication Whether to run "paymentApplication" before executing the report.
	 * @param ageAccount Whether to age the account before generating a report.
	 * @return String representation of an account report.
	 * @see AccountReport
	 */
	String generateAccountReport(String accountId, Date dateFrom, Date dateTo, Boolean details, Boolean paymentApplication, Boolean ageAccount);
	
	/**
	 * Return an XML account report for all accounts in the <code>accountIds</code> list, between the dates listed. 
	 * If <code>details</code> is <code>true</code>, then all transactions will also be reported. 
	 * If <code>paymentApplication</code> is <code>true</code> then <code>paymentApplication</code> method will be run before the account is reported. 
	 * If <code>ageAccount</code> is <code>true</code>, the account will be aged before the report is generated.
	 * 
	 * @param accountId ID of an account for which to produce a report.
	 * @param dateFrom Start date of reporting period.
	 * @param dateTo End date of reporting period.
	 * @param details Whether to report all transactions.
	 * @param paymentApplication Whether to run "paymentApplication" before executing the report.
	 * @param ageAccount Whether to age the account before generating a report.
	 * @return String representation of an account report.
	 * @see AccountReport
	 */
	String generateAccountsReport(List<String> accountIds, Date dateFrom, Date dateTo, Boolean details, Boolean paymentApplication, Boolean ageAccount);
	
	/**
	 * Produces a receipt for a transaction with the specified ID.
	 * 
	 * @param transactionId ID of a transaction for which to produce a receipt.
	 * @return String representation of a transaction receipt.
	 * @see Receipt
	 */
	String generateReceipt(Long transactionId);
}
