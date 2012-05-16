package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.TransactionColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

import java.util.Collection;

/**
 * User: dmulderink
 * Date: 5/14/12
 * Time: 9:24 AM
 */
public class TransactionListPanel extends AbstractListPanel<TransactionModel> {

    private static final TransactionColumnModelFactory COLUMN_FACTORY = new TransactionColumnModelFactory();

    private Button viewAccountButton;
    private Button viewPaymentsButton;
    private Button viewDefermentsButton;
    //private Button ageDebtButton;

    private void addTabItem(String name) {
        KsaPanel panel = Registry.get(KsaPanel.class.getName());
        if (panel != null) {
            TabItem item = new TabItem(name);
            item.setLayout(new FitLayout());
            item.setClosable(true);
            item.setIconStyle("tabs");
            // TODO: provide logic for rendering the tab content
            item.add(WidgetFactory.createTextBold("COMING SOON!!!"));
            panel.add(item);
            panel.setSelection(item);
        }
    }

    @Override
    protected ToolBar createTopComponent() {

        viewAccountButton = new Button("View Accounts");
        viewAccountButton.setToolTip("Display the account(s) list and detail");
        viewAccountButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Charges (Account ID: " + model.getId() + ")");
                }
            }
        });

        viewPaymentsButton = new Button("View Payments");
        viewPaymentsButton.setToolTip("Payments for the selected account will be shown on the new tab");
        viewPaymentsButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Payments (Account ID: " + model.getId() + ")");
                }
            }
        });

        viewDefermentsButton = new Button("View Deferments");
        viewDefermentsButton.setToolTip("Deferments for the selected account will be shown on the new tab");
        viewDefermentsButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Deferments (Account ID: " + model.getId() + ")");
                }
            }
        });


        ToolBar toolBar = new ToolBar();

        toolBar.add(viewAccountButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(viewPaymentsButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(viewDefermentsButton);


        enableButtons(false);

        return toolBar;
    }

    @Override
    protected ColumnModelFactory<TransactionModel> getColumnModelFactory() {
        return COLUMN_FACTORY;
    }

    @Override
    protected void loadData(PagingLoadConfig loadConfig,
                            AsyncCallback<PagingLoadResult<TransactionModel>> callback) {
        enableButtons(false);
        SortInfo sortInfo = loadConfig.getSortInfo();

        ServiceFactory.getTransactionService().findTransactions(
                getSearchCriteria(),
                sortInfo.getSortDir().name(),
                sortInfo.getSortField(),
                loadConfig.getOffset(),
                loadConfig.getLimit(), callback);

    }

    @Override
    protected String getAutoExpandColumn() {
        return TransactionModel.COLUMN_STATEMENT_TEXT;
    }


    protected void enableButtons(boolean enabled) {
        viewAccountButton.setEnabled(enabled);
        viewPaymentsButton.setEnabled(enabled);
        viewDefermentsButton.setEnabled(enabled);
    }

    @Override
    protected void onSingleRowSelection(TransactionModel item) {
        enableButtons(item != null);
        super.onSingleRowSelection(item);
    }

    @Override
    protected void onEmptyRowSelection() {
        enableButtons(false);
        super.onEmptyRowSelection();
    }

    @Override
    protected void onMultipleRowSelection(Collection<TransactionModel> items) {
        enableButtons(false);
        super.onMultipleRowSelection(items);
    }

}
