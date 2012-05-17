package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.NavigationContext;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.TransactionColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ColumnModelFactory;

import java.util.Collection;

/**
 * TransactionListPanel
 *
 * @author dmulderink, mivanov
 *         Date: 5/14/12
 *         Time: 9:24 AM
 */
public class TransactionListPanel extends AbstractListPanel<TransactionModel> {

    private static final TransactionColumnModelFactory COLUMN_FACTORY = new TransactionColumnModelFactory();

    private Button addChargeButton;
    private Button addPaymentButton;
    private Button deferTransactionButton;

    public TransactionListPanel(NavigationContext navigationContext) {
        super(navigationContext);
    }

    @Override
    protected ToolBar createTopComponent() {

        addChargeButton = new Button("Add Charge");
        addChargeButton.setToolTip("Add a new charge");
        addChargeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    //TODO
                    Info.display("Add Charge", "Add Charge");
                }
            }
        });

        addPaymentButton = new Button("Add Payment");
        addPaymentButton.setToolTip("Add a new payment");
        addPaymentButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    //TODO
                    Info.display("Add Payment", "Add Payment");
                }
            }
        });

        deferTransactionButton = new Button("Defer Transaction");
        deferTransactionButton.setToolTip("Defer a transaction");
        deferTransactionButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                TransactionModel model = getSelectedItem();
                if (model != null) {
                    //TODO
                    Info.display("Defer Transaction", "Defer Transaction");
                }
            }
        });


        ToolBar toolBar = new ToolBar();

        toolBar.add(addChargeButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(addPaymentButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(deferTransactionButton);


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
        //addChargeButton.setEnabled(enabled);
        //addPaymentButton.setEnabled(enabled);
        //deferTransactionButton.setEnabled(enabled);
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
