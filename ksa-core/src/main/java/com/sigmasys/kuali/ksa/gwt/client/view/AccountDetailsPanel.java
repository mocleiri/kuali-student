package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtErrorHandler;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

public class AccountDetailsPanel extends AbstractDetailsPanel<AccountModel> {

    protected final Text pastDue = WidgetFactory.createText("");
    protected final Text balance = WidgetFactory.createText("");
    protected final Text future = WidgetFactory.createText("");
    protected final Text deferments = WidgetFactory.createText("");

    protected final Text userId = WidgetFactory.createText("");
    protected final Text userName = WidgetFactory.createText("");
    protected final Text address = WidgetFactory.createText("");
    protected final Text phone = WidgetFactory.createText("");

    protected final Text lastAgeDate = WidgetFactory.createText("");
    protected final Text latePeriod1 = WidgetFactory.createText("");
    protected final Text latePeriod2 = WidgetFactory.createText("");
    protected final Text latePeriod3 = WidgetFactory.createText("");


    protected final CheckBox ignoreDefermentCheckBox;

    protected final Button addChargeButton = new Button("Add Charge");
    protected final Button makePaymentButton = new Button("Make Payment");
    protected final Button ageDebtButton = new Button("Age Transactions");

    protected final LayoutContainer contactPanel;
    protected final LayoutContainer statusPanel;
    protected final LayoutContainer ageDebtPanel;
    //protected final LayoutContainer alertPanel;
    //protected final LayoutContainer flagPanel;
    //protected final LayoutContainer memoPanel;

    public AccountDetailsPanel() {

        setScrollMode(Style.Scroll.AUTO);

        VerticalPanel panel = new VerticalPanel();
        panel.setHorizontalAlign(Style.HorizontalAlignment.LEFT);

        TableLayout layout = new TableLayout(4);

        contactPanel = new LayoutContainer(layout);
        contactPanel.add(WidgetFactory.createTextBold("Account ID"));
        contactPanel.add(WidgetFactory.createTextBold("User Name"));
        contactPanel.add(WidgetFactory.createTextBold("Address"));
        contactPanel.add(WidgetFactory.createTextBold("Phone #"));
        contactPanel.add(userId);
        contactPanel.add(userName);
        contactPanel.add(address);
        contactPanel.add(phone);

        layout = new TableLayout(6);

        statusPanel = new LayoutContainer(layout);
        statusPanel.add(WidgetFactory.createTextBold("Past Due"));
        statusPanel.add(WidgetFactory.createTextBold("Balance"));
        statusPanel.add(WidgetFactory.createTextBold("Future"));

        TableData td = new TableData();
        td.setColspan(3);
        statusPanel.add(WidgetFactory.createTextBold("Deferments"), td);

        statusPanel.add(pastDue);
        statusPanel.add(balance);
        statusPanel.add(future);
        statusPanel.add(deferments);

        statusPanel.add(addChargeButton);
        statusPanel.add(makePaymentButton);

        layout = new TableLayout(7);

        ageDebtPanel = new LayoutContainer(layout);
        ageDebtPanel.add(WidgetFactory.createTextBold("Last age date"));
        ageDebtPanel.add(WidgetFactory.createTextBold("30"));
        ageDebtPanel.add(WidgetFactory.createTextBold("60"));

        td = new TableData();
        td.setColspan(3);

        ageDebtPanel.add(WidgetFactory.createTextBold("90"), td);

        ignoreDefermentCheckBox = new CheckBox();
        ignoreDefermentCheckBox.setValue(true);
        ignoreDefermentCheckBox.setBoxLabel("Ignore Deferment");

        ageDebtPanel.add(lastAgeDate);
        ageDebtPanel.add(latePeriod1);
        ageDebtPanel.add(latePeriod2);
        ageDebtPanel.add(latePeriod3);
        ageDebtPanel.add(ignoreDefermentCheckBox);
        ageDebtPanel.add(ageDebtButton);

        panel.add(WidgetFactory.createTextBold("<h1>Account Information</h1>"));
        panel.add(contactPanel);

        panel.add(WidgetFactory.createTextBold("<h1>Account Status</h1>"));
        panel.add(statusPanel);

        panel.add(WidgetFactory.createTextBold("<h1>Aged Transactions</h1>"));
        panel.add(ageDebtPanel);


    }

    @Override
    public void updateView(AccountModel model) {

        if (model == null) {
            GwtErrorHandler.error("AccountModel is null");
            return;
        }

        userId.setText(model.getId());
        userName.setText(model.getFirstName() + " " +
                ((model.getMiddleName() != null) ? model.getMiddleName() : "") +
                model.getLastName());
        address.setText(model.getCountry() + ", " + model.getPostalCode() + ", " +
                model.getState() + ", " + model.getCity() + ", " + model.getStreetAddress());
        phone.setText(model.getPhoneNumber());

        // TODO - populate the other fields

    }
}
