package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.StringModelData;
import com.sigmasys.kuali.ksa.gwt.client.service.GenericCallback;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.validator.DateComparatorValidator;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.DateRangeField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.EntityNameField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.ListViewAdapter;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

import java.util.Date;
import java.util.List;

import static com.sigmasys.kuali.ksa.gwt.client.service.validator.DateComparatorValidator.*;

public class AccountSearchPanel extends AbstractSearchPanel<AccountModel> {

    private static final int ELEMENT_WIDTH = 170;

    private EntityNameField userId;
    private EntityNameField firstName;
    private EntityNameField lastName;
    private EntityNameField phoneNumber;
    private EntityNameField emailAddress;

    private DateRangeField creationDate;

    private ListViewAdapter<StringModelData> country;
    private EntityNameField city;

    @Override
    protected void fillSearchElementsContainer(LayoutContainer panel) {

        userId = new EntityNameField();
        userId.setWidth(ELEMENT_WIDTH);
        Text userIdLabel = WidgetFactory.createText("Account ID");
        panel.add(userIdLabel);
        panel.add(userId);

        firstName = new EntityNameField();
        firstName.setWidth(ELEMENT_WIDTH);
        Text firstNameLabel = WidgetFactory.createText("First Name");
        panel.add(firstNameLabel);
        panel.add(firstName);

        lastName = new EntityNameField();
        lastName.setWidth(ELEMENT_WIDTH);
        Text lastNameLabel = WidgetFactory.createText("Last Name");
        panel.add(lastNameLabel);
        panel.add(lastName);

        phoneNumber = new EntityNameField();
        phoneNumber.setWidth(ELEMENT_WIDTH);
        Text phoneNumberLabel = WidgetFactory.createText("Phone number");
        panel.add(phoneNumberLabel);
        panel.add(phoneNumber);

        emailAddress = new EntityNameField();
        emailAddress.setWidth(ELEMENT_WIDTH);
        Text emailAddressLabel = WidgetFactory.createText("Email address");
        panel.add(emailAddressLabel);
        panel.add(emailAddress);

        creationDate = new DateRangeField("From Date", "To Date");
        creationDate.addToValidator(new DateComparatorValidator(Operation.LESS_OR_EQUAL, new Date()));
        creationDate.setAllowBlank(false);
        Text creationDateLabel = WidgetFactory.createText("Creation Date");
        panel.add(creationDateLabel);
        panel.add(creationDate);

        country = new ListViewAdapter<StringModelData>();
        country.setWidth(ELEMENT_WIDTH);
        country.setHeight(200);
        ServiceFactory.getAccountService().getExistingCountryCodes(new GenericCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> countries) {
                if (countries != null && !countries.isEmpty()) {
                    ListStore<StringModelData> store = country.getListView().getStore();
                    for (String code : countries) {
                        store.add(new StringModelData(code));
                    }
                }
            }
        });
        Text countryLabel = WidgetFactory.createText("Country");
        panel.add(countryLabel);
        panel.add(country);

        city = new EntityNameField();
        city.setWidth(ELEMENT_WIDTH);
        Text cityLabel = WidgetFactory.createText("City");
        panel.add(cityLabel);
        panel.add(city);


        // TODO -- add form bindings if necessary
    }


    @Override
    protected Component[] getEnterKeyDownEventAwareComponents() {
        return new Component[]{userId, firstName, lastName, phoneNumber, emailAddress, creationDate, city};
    }

}
