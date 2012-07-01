package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.Constants;

import javax.jws.WebService;

/**
 * Created by: dmulderink on 6/29/12 at 10:35 AM
 */
@Url(TransactionImportService.SERVICE_URL)
@WebService(serviceName = TransactionImportService.SERVICE_NAME, portName = TransactionImportService.PORT_NAME,
      targetNamespace = Constants.WS_NAMESPACE)
public interface TransactionImportService {

   public static final String SERVICE_URL = "transactionImport.webservice";
   public static final String SERVICE_NAME = "TransactionImportService";
   public static final String PORT_NAME = SERVICE_NAME + "Port";

   public String xmlUpload(String base64Xml);
}
