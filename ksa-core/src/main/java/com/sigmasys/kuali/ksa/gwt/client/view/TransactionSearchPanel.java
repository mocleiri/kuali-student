package com.sigmasys.kuali.ksa.gwt.client.view;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.StringModelData;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.gwt.client.service.GenericCallback;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.validator.DateComparatorValidator;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.DateRangeField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.EntityNameField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.ListViewAdapter;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

import java.util.Date;
import java.util.List;

/**
 * User: dmulderink
 * Date: 5/14/12
 * Time: 12:31 PM
 */
public class TransactionSearchPanel extends AbstractSearchPanel<TransactionModel> {

   private static final int ELEMENT_WIDTH = 200;

   private EntityNameField userId;
   private EntityNameField firstName;
   private EntityNameField lastName;
   private EntityNameField phoneNumber;
   private EntityNameField emailAddress;

   private DateRangeField creationDate;

   private EntityNameField city;
   private EntityNameField state;
   private ListViewAdapter<StringModelData> country;

   private SimpleComboBox<String> transactionTypes;

   @Override
   protected void fillSearchElementsContainer(LayoutContainer panel) {

      userId = new EntityNameField();
      userId.setWidth(ELEMENT_WIDTH);
      Text userIdLabel = WidgetFactory.createText("Account ID:");
      panel.add(userIdLabel);
      panel.add(userId);

      firstName = new EntityNameField();
      firstName.setWidth(ELEMENT_WIDTH);
      Text firstNameLabel = WidgetFactory.createText("First Name:");
      panel.add(firstNameLabel);
      panel.add(firstName);

      lastName = new EntityNameField();
      lastName.setWidth(ELEMENT_WIDTH);
      Text lastNameLabel = WidgetFactory.createText("Last Name:");
      panel.add(lastNameLabel);
      panel.add(lastName);

      phoneNumber = new EntityNameField();
      phoneNumber.setWidth(ELEMENT_WIDTH);
      Text phoneNumberLabel = WidgetFactory.createText("Phone number:");
      panel.add(phoneNumberLabel);
      panel.add(phoneNumber);

      emailAddress = new EntityNameField();
      emailAddress.setWidth(ELEMENT_WIDTH);
      Text emailAddressLabel = WidgetFactory.createText("Email address:");
      panel.add(emailAddressLabel);
      panel.add(emailAddress);

      creationDate = new DateRangeField(Style.Orientation.HORIZONTAL, "From Date", "To Date", 95, 6);
      creationDate.addToValidator(new DateComparatorValidator(DateComparatorValidator.Operation.LESS_OR_EQUAL, new Date()));
      creationDate.setAllowBlank(true);
      Text creationDateLabel = WidgetFactory.createText("Creation Date:");
      panel.add(creationDateLabel);
      panel.add(creationDate);

      city = new EntityNameField();
      city.setWidth(ELEMENT_WIDTH);
      Text cityLabel = WidgetFactory.createText("City:");
      panel.add(cityLabel);
      panel.add(city);

      state = new EntityNameField();
      state.setWidth(ELEMENT_WIDTH);
      Text stateLabel = WidgetFactory.createText("State:");
      panel.add(stateLabel);
      panel.add(state);

      country = new ListViewAdapter<StringModelData>( new ListStore<StringModelData>());
      country.setDisplayProperty(StringModelData.DISPLAY_VALUE_KEY);
      country.setWidth(ELEMENT_WIDTH / 2);
      country.setHeight(100);
      ServiceFactory.getAccountService().getExistingCountryCodes(new GenericCallback<List<String>>() {
         @Override
         public void onSuccess(List<String> countries) {
            if (countries != null) {
               for (String code : countries) {
                  Log.debug("Adding country code: " + code);
                  country.addToStore(new StringModelData(code));
               }
            }
         }
      });
      Text countryLabel = WidgetFactory.createText("Country:");
      panel.add(countryLabel);
      panel.add(country);

      transactionTypes = new SimpleComboBox<String>();
      transactionTypes.add(TransactionModel.transactionTypes);
      transactionTypes.setSimpleValue("All");
      transactionTypes.setTriggerAction(ComboBox.TriggerAction.ALL);
      transactionTypes.setForceSelection(true);
      transactionTypes.setAllowBlank(false);
      transactionTypes.setEditable(false);
      transactionTypes.setWidth(100);
      transactionTypes.addListener(Events.Select, new Listener<FieldEvent>() {
         public void handleEvent(FieldEvent be) {
            @SuppressWarnings("unchecked")
            String newTransType = ((SimpleComboValue<String>) be.getField().getValue()).getValue();
            TransactionModel.setSelectedTransactionType(newTransType);
         }
      });
      Text transactionTypesLabel = WidgetFactory.createText("Transaction Type:");
      panel.add(transactionTypesLabel);
      panel.add(transactionTypes);

      // TODO -- add form bindings if necessary
      addFieldBinding(userId, AccountModel.ID);
      addFieldBinding(firstName, AccountModel.FIRST_NAME);
      addFieldBinding(lastName, AccountModel.LAST_NAME);
      addFieldBinding(phoneNumber, AccountModel.PHONE_NUMBER);
      addFieldBinding(emailAddress, AccountModel.EMAIL_ADDRESS);
      addFieldBinding(creationDate, AccountModel.CREATION_DATE);
      addFieldBinding(city, AccountModel.CITY);
      addFieldBinding(state, AccountModel.STATE);
      addFieldBinding(country, AccountModel.COUNTRY);
      addFieldBinding(transactionTypes, TransactionModel.TYPE);

   }


   @Override
   protected Component[] getEnterKeyDownEventAwareComponents() {
      return new Component[]{userId, firstName, lastName, phoneNumber, emailAddress, creationDate, city, state};
   }

}
