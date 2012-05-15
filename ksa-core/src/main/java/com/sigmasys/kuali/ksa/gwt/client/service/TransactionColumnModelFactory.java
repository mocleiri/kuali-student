package com.sigmasys.kuali.ksa.gwt.client.service;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;
import com.sigmasys.kuali.ksa.model.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dmulderink
 * Date: 5/14/12
 * Time: 9:51 AM
 * This class represents common transaction table list attributes
 * so Charge, Payment and Deferment use shared fields of a Transaction
 */
public class TransactionColumnModelFactory extends AbstractColumnModelFactory<TransactionModel> {

   public static final String LABEL_ID = "Transaction ID";
   public static final String LABEL_ORIGINATION_DATE = "Origination Date";
   public static final String LABEL_EFFECTIVE_DATE = "Effective Date";
   public static final String LABEL_AMOUNT = "Amount";
   public static final String LABEL_CURRENCY = "Currency";
   public static final String LABEL_TYPE = "Type";
   public static final String LABEL_SUBCODE = "SubCode";
   public static final String LABEL_DESCRIPTION = "Description";

   public List<ColumnConfig> getColumnConfigs() {

      final DateTimeFormat dateFormat = DateTimeFormat.getFormat(Constants.DATE_FORMAT_US);
      final DateTimeFormat timeFormat = DateTimeFormat.getFormat(Constants.TIME_FORMAT);

      List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

      ColumnConfig column = new ColumnConfig(TransactionModel.COLUMN_ID, LABEL_ID, 100);
      column.setAlignment(Style.HorizontalAlignment.LEFT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_ORIG_DATE, LABEL_ORIGINATION_DATE, 110);
      column.setDateTimeFormat(dateFormat);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_EFFECTIVE_DATE, LABEL_EFFECTIVE_DATE, 110);
      column.setDateTimeFormat(dateFormat);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_AMNT, LABEL_AMOUNT, 90);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_CURRENCY_ID_FK, LABEL_CURRENCY, 70);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_TYPE, LABEL_TYPE, 80);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_TRANSACTION_TYPE_SUB_CODE_FK, LABEL_SUBCODE, 80);
      column.setAlignment(Style.HorizontalAlignment.RIGHT);
      columns.add(column);

      column = new ColumnConfig(TransactionModel.COLUMN_STATEMENT_TXT, LABEL_DESCRIPTION, 120);
      column.setAlignment(Style.HorizontalAlignment.LEFT);
      columns.add(column);

      return columns;
   }
}
