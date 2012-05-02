package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.service.AccountColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;


/**
 * AccountListPanel
 *
 * @author Michael Ivanov
 */
public class AccountListPanel extends AbstractListPanel<AccountModel> {

    private static final AccountColumnModelFactory columnFactory = new AccountColumnModelFactory();

    private Button addChargeButton;
    private Button makePaymentButton;
    private Button ageDebtButton;


    @Override
    protected ToolBar createTopComponent() {

        addChargeButton = new Button("Add Charge");
        addChargeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                // TODO
            }
        });

        makePaymentButton = new Button("Make Payment");
        makePaymentButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                // TODO
            }
        });

        ageDebtButton = new Button("Age Transactions");
        ageDebtButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                // TODO
            }
        });


        ToolBar toolBar = new ToolBar();

        toolBar.add(addChargeButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(makePaymentButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(ageDebtButton);

        enableButtons(false);

        return toolBar;
    }

    @Override
    protected ColumnModelFactory<AccountModel> getColumnModelFactory() {
        return columnFactory;
    }

    @Override
    protected void loadData(PagingLoadConfig loadConfig,
                            AsyncCallback<PagingLoadResult<AccountModel>> callback) {
        enableButtons(false);
        SortInfo sortInfo = loadConfig.getSortInfo();
        ServiceFactory.getAccountService().findAccounts(
                getSearchCriteria(),
                sortInfo.getSortDir().name(),
                sortInfo.getSortField(),
                loadConfig.getOffset(),
                loadConfig.getLimit(), callback);

    }

    @Override
    protected String getAutoExpandColumn() {
        return AccountModel.COLUMN_STREET_ADDRESS;
    }


    protected void enableButtons(boolean enabled) {
        addChargeButton.setEnabled(enabled);
        makePaymentButton.setEnabled(enabled);
        ageDebtButton.setEnabled(enabled);
    }

    @Override
    protected void onSingleRowSelection(AccountModel item) {
        enableButtons(item != null);
        super.onSingleRowSelection(item);
    }

    @Override
    protected void onEmptyRowSelection() {
        enableButtons(false);
        super.onEmptyRowSelection();
    }
}
