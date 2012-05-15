package com.sigmasys.kuali.ksa.gwt.client.view;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.google.gwt.user.client.Command;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.gwt.client.service.GenericCallback;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtAccountServiceAsync;
import com.sigmasys.kuali.ksa.gwt.client.service.GwtErrorHandler;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.OkCancelDialog;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

/**
 * User: dmulderink
 * Date: 5/14/12
 * Time: 9:26 AM
 */
public class TransactionDetailsPanel extends AbstractDetailsPanel<TransactionModel> {

   // Common fields

   protected final Text accountId = WidgetFactory.createStyledText("", "text-link");

   // Charge - aka Debit type
   protected final Text isDefered = WidgetFactory.createStyledText("", "text-link");

   // Payment - aka Credit type
   protected final Text isRefundable = WidgetFactory.createStyledText("", "text-link");
   protected final Text refundRule = WidgetFactory.createStyledText("", "text-link");
   protected final Text clearDate = WidgetFactory.createStyledText("", "text-link");

   // Deferment - aka a deferred debit
   protected final Text expireDate = WidgetFactory.createStyledText("", "text-link");

   protected final LayoutContainer commonPanel;
   protected final LayoutContainer debitPanel;
   protected final LayoutContainer creditPanel;
   protected final LayoutContainer specialPanel;
   //protected final LayoutContainer statusPanel;

   protected final Button addChargeButton;
   protected final Button makePaymentButton;
   protected final Button ageDebtButton;

   public TransactionDetailsPanel() {

      final int CELL_PADDING = 10;
      final int CELL_SPACING = 0;

      setHeading("Transaction Details");

      setScrollMode(Style.Scroll.AUTO);

      setLayout(new FitLayout());

      VerticalPanel panel = new VerticalPanel();
      panel.setHorizontalAlign(Style.HorizontalAlignment.LEFT);
      panel.setStyleName("bg-white");

      addChargeButton = new Button("Add Charge");
      addChargeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
         public void handleEvent(BaseEvent be) {
            // TODO
            createYesNoDialog("Add Charge", "Are you sure you want to add a charge?", null).show();
         }
      });

      makePaymentButton = new Button("Make Payment");
      makePaymentButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
         public void handleEvent(BaseEvent be) {
            // TODO
            createYesNoDialog("Make Payment", "Are you sure you want to make a payment?", null).show();
         }
      });

      ageDebtButton = new Button("Age Transactions");
      ageDebtButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
         public void handleEvent(BaseEvent be) {
            Command command = new Command() {
               @Override
               public void execute() {
                  ageDebt();
               }
            };
            createYesNoDialog("Age Transactions", "Are you sure you want to age transactions?", command).show();
         }
      });


      // -----
      TableLayout layout = new TableLayout(1);
      layout.setCellPadding(CELL_PADDING);
      layout.setCellSpacing(CELL_SPACING);

      commonPanel = new LayoutContainer(layout);
      commonPanel.add(WidgetFactory.createText("Account ID"));
      commonPanel.add(accountId);

      // -----
      layout = new TableLayout(2);
      layout.setCellPadding(CELL_PADDING);
      layout.setCellSpacing(CELL_SPACING);

      debitPanel = new LayoutContainer(layout);
      debitPanel.add(WidgetFactory.createText("Deferred"));
      debitPanel.add(addChargeButton);

      TableData td = new TableData();
      td.setColspan(1);
      debitPanel.add(isDefered);

      // -----
      layout = new TableLayout(4);
      layout.setCellPadding(CELL_PADDING);
      layout.setCellSpacing(CELL_SPACING);

      creditPanel = new LayoutContainer(layout);
      creditPanel.add(WidgetFactory.createText("Refundable"));
      creditPanel.add(WidgetFactory.createText("Refund Rule"));
      creditPanel.add(WidgetFactory.createText("Clear Date"));
      creditPanel.add(makePaymentButton);

      creditPanel.add(isRefundable);
      creditPanel.add(refundRule);

      td = new TableData();
      td.setColspan(2);
      creditPanel.add(clearDate);

      // -----
      // deferment and special information
      layout = new TableLayout(1);
      layout.setCellPadding(CELL_PADDING);
      layout.setCellSpacing(CELL_SPACING);

      specialPanel = new LayoutContainer(layout);
      specialPanel.add(WidgetFactory.createText("Expiration Date"));

      td = new TableData();
      td.setColspan(1);
      specialPanel.add(expireDate);

      panel.add(WidgetFactory.createTextBold("&nbsp;Extended Transaction Information"));
      panel.add(commonPanel);
      panel.add(WidgetFactory.createTextBold("&nbsp;Charges"));
      panel.add(debitPanel);
      panel.add(WidgetFactory.createTextBold("&nbsp;Payments"));
      panel.add(creditPanel);
      panel.add(WidgetFactory.createTextBold("&nbsp;Deferments"));
      panel.add(specialPanel);
      add(panel);

   }

   private void ageDebt() {
/*
      final AccountModel model = ageDebtButton.getData("model");
      if (model != null && model.getId() != null) {
         try {
            AccountDetailsPanel.this.mask("Processing...");
            boolean ignoreDeferment = ignoreDefermentCheckBox.getValue() != null ?
                  ignoreDefermentCheckBox.getValue() : false;
            ServiceFactory.getAccountService().ageDebt(model.getId(), ignoreDeferment,
                  new GenericCallback<AccountModel>() {
                     @Override
                     public void onSuccess(AccountModel result) {
                        updateView(result);
                        Grid<AccountModel> grid = getListPanel().getGrid();
                        ListStore<AccountModel> listStore = grid.getStore();
                        int index = listStore.indexOf(model);
                        if (index >= 0) {
                           listStore.remove(model);
                           listStore.insert(result, index);
                           getListPanel().refreshGridRows(listStore.getModels());
                           GridSelectionModel<AccountModel> selectionModel = grid.getSelectionModel();
                           selectionModel.select(result, false);
                           grid.setSelectionModel(selectionModel);
                           grid.getView().refresh(false);
                        }
                        AccountDetailsPanel.this.unmask();
                        Info.display("Success", "Aging completed");
                        if ( index >= 0) {
                           grid.getView().focusRow(index);
                        }
                     }

                     @Override
                     public void onFailure(Throwable t) {
                        AccountDetailsPanel.this.unmask();
                        super.onFailure(t);
                     }
                  });
         } catch (Exception e) {
            Log.error(e.getMessage(), e);
            AccountDetailsPanel.this.unmask();
         }
      }
*/
   }

   @Override
   public void updateView(TransactionModel model) {

      if (model == null) {
         GwtErrorHandler.error("TransactionModel is null");
         return;
      }

      // TODO: TransactionService implementation
      //GwtTransactionServiceAsync accountService = ServiceFactory.getTransactionService();

   }

   private Dialog createYesNoDialog(final String title, final String message, final Command command) {
      return new OkCancelDialog(title) {

         {
            okText = "Yes";
            cancelText = "No";
         }

         @Override
         protected void init() {
            setLayout(new BorderLayout());
            BorderLayoutData layoutData = new BorderLayoutData(Style.LayoutRegion.CENTER);
            layoutData.setMargins(new Margins(10, 10, 10, 10));
            add(WidgetFactory.createTextBold(message), layoutData);
         }

         @Override
         protected void onOkClicked() {
            hide();
            if (command != null) {
               command.execute();
            }
         }
      };
   }

}
