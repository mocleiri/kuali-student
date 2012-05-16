package com.sigmasys.kuali.ksa.gwt.client.view;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.sigmasys.kuali.ksa.gwt.client.model.StringModelData;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionType;
import com.sigmasys.kuali.ksa.gwt.client.service.GenericCallback;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.DateRangeField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.EntityNameField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.ListViewAdapter;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

import java.util.List;

/**
 * TransactionSearchPanel
 *
 * @author dmulderink, mivanov
 *         Date: 5/14/12
 *         Time: 12:31 PM
 */
public class TransactionSearchPanel extends AbstractSearchPanel<TransactionModel> {

    private static final int ELEMENT_WIDTH = 200;

    private EntityNameField transactionId;
    private EntityNameField accountId;
    private EntityNameField statementText;

    private DateRangeField originationDate;
    private DateRangeField effectiveDate;

    private ListViewAdapter<StringModelData> currency;
    private ListViewAdapter<StringModelData> transactionType;

    @Override
    protected void fillSearchElementsContainer(LayoutContainer panel) {

        transactionId = new EntityNameField();
        transactionId.setWidth(ELEMENT_WIDTH);
        panel.add(WidgetFactory.createText("Transaction ID:"));
        panel.add(transactionId);

        accountId = new EntityNameField();
        accountId.setWidth(ELEMENT_WIDTH);
        panel.add(WidgetFactory.createText("Account ID:"));
        panel.add(accountId);

        statementText = new EntityNameField();
        statementText.setWidth(ELEMENT_WIDTH);
        panel.add(WidgetFactory.createText("Statement text:"));
        panel.add(statementText);

        originationDate = new DateRangeField(Style.Orientation.HORIZONTAL, "From Date", "To Date", 95, 6);
        originationDate.setAllowBlank(true);
        panel.add(WidgetFactory.createText("Origination Date:"));
        panel.add(originationDate);

        effectiveDate = new DateRangeField(Style.Orientation.HORIZONTAL, "From Date", "To Date", 95, 6);
        effectiveDate.setAllowBlank(true);
        panel.add(WidgetFactory.createText("Effective Date:"));
        panel.add(originationDate);

        currency = new ListViewAdapter<StringModelData>(new ListStore<StringModelData>());
        currency.setDisplayProperty(StringModelData.DISPLAY_VALUE_KEY);
        currency.setWidth(ELEMENT_WIDTH / 2);
        currency.setHeight(100);
        ServiceFactory.getTransactionService().getExistingCurrencyCodes(new GenericCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> currencies) {
                if (currencies != null) {
                    for (String code : currencies) {
                        Log.debug("Adding currency code: " + code);
                        currency.addToStore(new StringModelData(code));
                    }
                }
            }
        });
        panel.add(WidgetFactory.createText("Currency:"));
        panel.add(currency);

        transactionType = new ListViewAdapter<StringModelData>(new ListStore<StringModelData>());
        transactionType.setDisplayProperty(StringModelData.DISPLAY_VALUE_KEY);
        transactionType.setWidth(ELEMENT_WIDTH / 2);
        transactionType.setHeight(100);
        for (TransactionType type : TransactionType.values()) {
            Log.debug("Adding transaction type: " + type);
            transactionType.addToStore(new StringModelData(type.toString()));
        }
        Text transactionTypesLabel = WidgetFactory.createText("Transaction Type:");
        panel.add(transactionTypesLabel);
        panel.add(transactionType);

        // TODO -- add form bindings if necessary
        addFieldBinding(transactionId, TransactionModel.ID);
        addFieldBinding(accountId, TransactionModel.ACCOUNT_ID);
        addFieldBinding(statementText, TransactionModel.STATEMENT_TEXT);
        addFieldBinding(originationDate, TransactionModel.ORIGINATION_DATE);
        addFieldBinding(effectiveDate, TransactionModel.EFFECTIVE_DATE);
        addFieldBinding(currency, TransactionModel.CURRENCY_CODE);

    }


    @Override
    protected Component[] getEnterKeyDownEventAwareComponents() {
        return new Component[]{transactionId, accountId, statementText, originationDate, effectiveDate};
    }

}
