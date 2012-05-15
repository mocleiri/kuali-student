package com.sigmasys.kuali.ksa.gwt.client.view;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
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

   protected final LayoutContainer statusPanel;

   protected final Button addChargeButton;

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


      TableLayout layout = new TableLayout(1);
      layout.setCellPadding(CELL_PADDING);
      layout.setCellSpacing(CELL_SPACING);

      statusPanel = new LayoutContainer(layout);
      statusPanel.add(addChargeButton);

      TableData td = new TableData();
      td.setColspan(1);

      panel.add(WidgetFactory.createTextBold("&nbsp;Extended Transaction Information"));
      panel.add(statusPanel);

      add(panel);

   }

/*   private void ageDebt() {
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
   }
*/
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
