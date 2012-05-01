package com.sigmasys.kuali.ksa.gwt.client.service;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;

import java.util.List;

/**
 * GwtAccountServiceAsync
 *
 * @author Michael Ivanov
 */
public interface GwtAccountServiceAsync  {

    void findAccounts(SearchCriteria searchCriteria, String sortDir,
                                                String sortField, int offset, int limit,
                                                AsyncCallback<PagingLoadResult<AccountModel>> callback);

    void getExistingCountryCodes(AsyncCallback<List<String>> callback);

}
