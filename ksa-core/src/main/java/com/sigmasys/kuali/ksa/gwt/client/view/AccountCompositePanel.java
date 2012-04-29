package com.sigmasys.kuali.ksa.gwt.client.view;


import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;

/**
 * AccountCompositePanel
 *
 * @author ivanovm
 */
public class AccountCompositePanel extends AbstractCompositePanel<AccountModel> {

    public AccountCompositePanel() {
        this(null);
    }

    public AccountCompositePanel(SearchCriteria searchCriteria) {
        super(
                new AccountSearchPanel(),
                new AccountListPanel(),
                null,
                235,
                100,
                searchCriteria != null
        );
    }

}
