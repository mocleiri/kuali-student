package com.sigmasys.kuali.ksa.gwt.client.service;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;

/**
 * GwtAccountService
 *
 * @author Michael Ivanov
 */
public interface GwtAccountService extends RemoteService {

    PagingLoadResult<AccountModel> findAccounts(SearchCriteria searchCriteria, String sortDir,
                                                           String sortField, int offset, int limit) throws GwtError;

}
