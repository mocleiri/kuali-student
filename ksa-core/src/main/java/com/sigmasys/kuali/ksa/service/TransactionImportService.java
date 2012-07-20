package com.sigmasys.kuali.ksa.service;


/**
 * Created by: dmulderink on 6/29/12 at 10:35 AM
 */
public interface TransactionImportService {

    /**
     * This is the service method exposed to up load and process transactions
     *
     * @param xml XML content
     * @return XML response
     */
    public String processTransactions(String xml);


}
