package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.Constants;

import javax.jws.WebService;
import java.util.Date;

/**
 * This service provides a generic KSA Reporting API.
 * <p/>
 *
 * @author Michael Ivanov
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


}
