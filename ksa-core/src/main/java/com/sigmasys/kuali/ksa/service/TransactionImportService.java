package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.BatchReceipt;
import com.sigmasys.kuali.ksa.model.Constants;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by: dmulderink on 6/29/12 at 10:35 AM
 */
@Url(TransactionImportService.SERVICE_URL)
@WebService(serviceName = TransactionImportService.SERVICE_NAME, portName = TransactionImportService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface TransactionImportService {

    String SERVICE_URL = "transactionImport.webservice";
    String SERVICE_NAME = "TransactionImportService";
    String PORT_NAME = SERVICE_NAME + "Port";

    /**
     * This is the service method exposed to up load and process transactions
     *
     * @param xml XML content
     * @return XML response
     */
    @WebMethod
    public String processTransactions(String xml);

    /**
     * Persist the given batch receipt in the database
     *
     * @param batchReceipt BatchReceipt instance
     * @return BatchReceipt ID
     */
    @WebMethod(exclude = true)
    Long persistBatchReceipt(BatchReceipt batchReceipt);

    /**
     * Returns BatchReceipt by the given ID
     *
     * @param id batchReceipt ID
     * @return BatchReceipt instance
     */
    @WebMethod(exclude = true)
    BatchReceipt getBatchReceipt(Long id);

}
