package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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

    protected final Button addChargeButton;
    protected final Button makePaymentButton;
    protected final Button ageDebtButton;

    protected final LayoutContainer contactPanel;
    protected final LayoutContainer statusPanel;
    protected final LayoutContainer ageDebtPanel;
    //protected final LayoutContainer alertPanel;
    //protected final LayoutContainer flagPanel;
    //protected final LayoutContainer memoPanel;

    public AccountDetailsPanel() {

        setHeading("Account Details");

        setScrollMode(Style.Scroll.AUTO);

        setLayout(new FitLayout());

        VerticalPanel panel = new VerticalPanel();
        panel.setHorizontalAlign(Style.HorizontalAlignment.LEFT);
        panel.setStyleName("bg-white");

        addChargeButton = new Button("Add Charge");
        addChargeButton.setIconStyle("icon-add");

        makePaymentButton = new Button("Make Payment");
        makePaymentButton.setIconStyle("icon-upload");

        ageDebtButton = new Button("Age Transactions");
        ageDebtButton.setIconStyle("icon-calendar");

        TableLayout layout = new TableLayout(4);
        layout.setCellPadding(10);

        contactPanel = new LayoutContainer(layout);
        contactPanel.add(WidgetFactory.createText("Account ID"));
        contactPanel.add(WidgetFactory.createText("User Name"));
        contactPanel.add(WidgetFactory.createText("Address"));
        contactPanel.add(WidgetFactory.createText("Phone #"));
        contactPanel.add(userId);
        contactPanel.add(userName);
        contactPanel.add(address);
        contactPanel.add(phone);

        layout = new TableLayout(6);
        layout.setCellPadding(10);

        statusPanel = new LayoutContainer(layout);
        statusPanel.add(WidgetFactory.createText("Past Due"));
        statusPanel.add(WidgetFactory.createText("Balance"));
        statusPanel.add(WidgetFactory.createText("Future"));
        statusPanel.add(WidgetFactory.createText("Deferments"));
        statusPanel.add(addChargeButton);
        statusPanel.add(makePaymentButton);

        statusPanel.add(pastDue);
        statusPanel.add(balance);
        statusPanel.add(future);

        TableData td = new TableData();
        td.setColspan(3);
        statusPanel.add(deferments);


        layout = new TableLayout(6);
        layout.setCellPadding(10);

        ageDebtPanel = new LayoutContainer(layout);
        ageDebtPanel.add(WidgetFactory.createText("Last Age Date"));
        ageDebtPanel.add(WidgetFactory.createText("30"));
        ageDebtPanel.add(WidgetFactory.createText("60"));
        ageDebtPanel.add(WidgetFactory.createText("90"));

        ignoreDefermentCheckBox = new CheckBox();
        ignoreDefermentCheckBox.setValue(true);
        ignoreDefermentCheckBox.setBoxLabel("Ignore Deferment");

        ageDebtPanel.add(ignoreDefermentCheckBox);
        ageDebtPanel.add(ageDebtButton);

        ageDebtPanel.add(lastAgeDate);
        ageDebtPanel.add(latePeriod1);
        ageDebtPanel.add(latePeriod2);

        td = new TableData();
        td.setColspan(3);
        ageDebtPanel.add(latePeriod3, td);


        panel.add(WidgetFactory.createTextBold("Account Information"));
        panel.add(contactPanel);

        panel.add(WidgetFactory.createTextBold("Account Status"));
        panel.add(statusPanel);

        panel.add(WidgetFactory.createTextBold("Aged Transactions"));
        panel.add(ageDebtPanel);

        add(panel);

    }

    @Override
    public void updateView(AccountModel model) {

        if (model == null) {
            GwtErrorHandler.error("AccountModel is null");
            return;
        }

        userId.setText(model.getId());

        userName.setText(
                ((model.getFirstName() != null) ? model.getFirstName() : "") + " " +
                        ((model.getMiddleName() != null) ? model.getMiddleName() : "") +
                        ((model.getLastName() != null) ? model.getLastName() : ""));

        address.setText(
                model.getStreetAddress() + ", " +
                        model.getCity() + ", " +
                        model.getState() + ", " +
                        model.getPostalCode() + ", " +
                        model.getCountry());

        phone.setText(model.getPhoneNumber());

        // TODO - populate the other fields

    }
}
