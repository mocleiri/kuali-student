package com.sigmasys.kuali.ksa.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.ChargeableAccount;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.GlRecognitionPeriod;
import com.sigmasys.kuali.ksa.model.GlTransaction;
import com.sigmasys.kuali.ksa.model.GlTransactionStatus;
import com.sigmasys.kuali.ksa.model.GlTransmission;
import com.sigmasys.kuali.ksa.model.LatePeriod;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.GeneralLedgerService;
import com.sigmasys.kuali.ksa.service.ReportService;
import com.sigmasys.kuali.ksa.transform.AccountReport;
import com.sigmasys.kuali.ksa.transform.AgedBalanceReport;
import com.sigmasys.kuali.ksa.transform.FailedTransactionsReport;
import com.sigmasys.kuali.ksa.transform.GeneralLedgerReport;
import com.sigmasys.kuali.ksa.transform.GeneralLedgerReport.GeneralLedgerReportEntry;
import com.sigmasys.kuali.ksa.transform.GeneralLedgerReport.ReportingPeriod;
import com.sigmasys.kuali.ksa.transform.Irs1098T;
import com.sigmasys.kuali.ksa.transform.ObjectFactory;
import com.sigmasys.kuali.ksa.transform.Receipt;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.JaxbUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;

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

    @PostConstruct
    private void postConstruct() {
        schemaValidator = new XmlSchemaValidator(XML_SCHEMA_LOCATION, REPORT_SCHEMA_LOCATION);
    }


    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param startDate       Start date
     * @param endDate         End date
     * @param glAccountId     GL Account ID
     * @param transmittedOnly indicates whether only transmitted GL transactions should be retrieved or not
     * @return The generated report in XML
     */
    @Override
    public String generateGeneralLedgerReport(Date startDate, Date endDate, String glAccountId, boolean transmittedOnly) {

        List<GlTransaction> glTransactions = glService.getGlTransactions(startDate, endDate, glAccountId);

        ObjectFactory objectFactory = new ObjectFactory();

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

        ReportingPeriod reportingPeriod = objectFactory.createGeneralLedgerReportReportingPeriod();

        reportingPeriod.setStartDate(CalendarUtils.toXmlGregorianCalendar(startDate, true));
        reportingPeriod.setEndDate(CalendarUtils.toXmlGregorianCalendar(endDate, true));

        glReport.setReportingPeriod(reportingPeriod);

        String reportXml = JaxbUtils.toXml(glReport);

        // Validate XML against the schema
        if (!schemaValidator.validateXml(reportXml)) {
            String errMsg = "XML content is invalid:\n" + reportXml;
            logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        return reportXml;
    }

    /**
     * Generates a reconciliation report in XML for KSA transactions to the general ledger.
     *
     * @param startDate   Start date
     * @param endDate     End date
     * @param glAccountId GL Account ID
     * @return The generated report in XML
     */
    @WebMethod(exclude = true)
    public String generateGeneralLedgerReport(Date startDate, Date endDate, String glAccountId) {
        return generateGeneralLedgerReport(startDate, endDate, glAccountId, false);
    }


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
	@Override
	public String generate1098TReport(String accountId, Integer year, Integer ssnMask, Boolean noRecord, Date dateFrom, Date dateTo) {
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
	 * @param year Tax year.
	 * @param ssnMask The number of final digits of the social security number that will be stored in the local record. 
	 * @param noRecord Whether to keep the record of the produces 1098T
	 * @return String representation of an IRS 1098T form.
	 * @see Irs1098T
	 */
	@Override
	@WebMethod(exclude = true)
	public String generate1098TReport(String accountId, Integer year, Integer ssnMask, Boolean noRecord) {
		// TODO Auto-generated method stub
		return null;
	}


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
	@Override
	public String generateAgedBalanceReport(List<String> accountIds, Boolean ignoreDeferments, Boolean ageAccount) {
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
			AgedBalanceReport.AgedAccountDetail agedAccountDetail = new AgedBalanceReport.AgedAccountDetail();
			
			agedAccountDetail.setAccountIdentifier(accountId);
			agedAccountDetail.setAccountName(account.getCompositeDefaultPersonName());
			
			// Set amounts late:
			if (account instanceof ChargeableAccount) {
				AgedBalanceReport.AgedAccountDetail.AccountBalances accountBalances = new AgedBalanceReport.AgedAccountDetail.AccountBalances();
				
				accountBalances.setPeriod1Balance(((ChargeableAccount)account).getAmountLate1());
				accountBalances.setPeriod2Balance(((ChargeableAccount)account).getAmountLate2());
				accountBalances.setPeriod3Balance(((ChargeableAccount)account).getAmountLate3());
				agedAccountDetail.setAccountBalances(accountBalances);
			}
			
			// Add the ageAccountDetail to the list of all details:
			if (indexOfExistingAgeBreakdown == -1) {
				// Append at the end:
				detailsList.add(agedAccountDetail);
			} else {
				// Insert after the AgeBreakdown object:
				detailsList.add(indexOfExistingAgeBreakdown+1, agedAccountDetail);
			}
		}
		
		return JaxbUtils.toXml(report);
	}


	/**
	 * An overloaded method to produce a failed transaction report for all batches.
	 * 
	 * @param dateFrom Start date of transaction period tracking.
	 * @param dateTo End date of transaction period tracking.
	 * @param onlyFailed Include only transactions that failed on their own.
	 * @return String representation of an XML report.
	 * @see FailedTransactionsReport
	 */
	@Override
	@WebMethod(exclude = true)
	public String generateFailedTransactionReport(Date dateFrom, Date dateTo, Boolean onlyFailed) {
		// TODO Auto-generated method stub
		return null;
	}


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
	@Override
	public String generateFailedTransactionReport(Date dateFrom, Date dateTo, Boolean onlyFailed, String entityId) {
		// TODO Auto-generated method stub
		return null;
	}


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
	@Override
	public String generateAccountReport(String accountId, Date dateFrom, Date dateTo, Boolean details, Boolean paymentApplication, Boolean ageAccount) {
		// TODO Auto-generated method stub
		return null;
	}


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
	@Override
	public String generateAccountsReport(List<String> accountIds, Date dateFrom, Date dateTo, Boolean details, Boolean paymentApplication, Boolean ageAccount) {
		// TODO Auto-generated method stub
		return null;
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
	 * @param ageBreakdownAndAgeAccountDetailList A list of report detail objects.
	 * @param account Account to match its late date periods.
	 * @return Index of a matching <code>AgedBalanceReport.AgeBreakdown</code> object.
	 */
	private int indexOfAgeBreakdown(List<Object> ageBreakdownAndAgeAccountDetailList, Account account) {
		// Get the Account's LatePeriod object:
		LatePeriod latePeriod = account.getLatePeriod();
		
		if (latePeriod != null) {
			// Iterate through the list and try to match an AgeBreakdown:
			for (int i=0,sz=ageBreakdownAndAgeAccountDetailList.size(); i<sz; i++) {
				Object o = ageBreakdownAndAgeAccountDetailList.get(i);
				
				if (o instanceof AgedBalanceReport.AgeBreakdown) {
					AgedBalanceReport.AgeBreakdown ageBreakdown = (AgedBalanceReport.AgeBreakdown)o;
					
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
	 * @param i Primitive
	 * @param ii Integer.
	 * @return Comparison result. 
	 */
	private static int safeCompare(int i, Integer ii) {
		return (ii != null) ? ii.compareTo(i) : 1;
	}
}
