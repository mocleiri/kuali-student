package com.sigmasys.kuali.ksa.gwt.client.service;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;


/**
 * GwtTransactionService
 *
 * @author Michael Ivanov
 */
public interface GwtTransactionService extends RemoteService {

    PagingLoadResult<TransactionModel> findTransactions(SearchCriteria searchCriteria, String sortDir,
                                                        String sortField, int offset, int limit) throws GwtError;


}
