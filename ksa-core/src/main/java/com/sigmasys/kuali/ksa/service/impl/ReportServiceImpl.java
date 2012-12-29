package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.GeneralLedgerService;
import com.sigmasys.kuali.ksa.service.ReportService;
import com.sigmasys.kuali.ksa.transform.GeneralLedgerReport;
import com.sigmasys.kuali.ksa.transform.ObjectFactory;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.JaxbUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.*;

import static com.sigmasys.kuali.ksa.transform.GeneralLedgerReport.*;

/**
 * Report Service implementation.
 *
 * @author Michael Ivanov
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


}
