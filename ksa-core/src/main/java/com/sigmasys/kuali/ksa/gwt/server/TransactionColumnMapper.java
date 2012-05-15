package com.sigmasys.kuali.ksa.gwt.server;

import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dmulderink
 * Date: 5/14/12
 * Time: 8:11 AM
 * This class represents common transaction key/value attributes
 * so Charge, Payment and Deferment use shared fields
 */
public class TransactionColumnMapper extends AbstractColumnMapper {

   @Override
   protected Map<String, String> getFieldTranslationMapping() {
      return new HashMap<String, String>();
   }

   @Override
   protected Map<String, String> getFieldSortMapping() {
      Map<String, String> sortMap = new HashMap<String, String>();
      sortMap.put(TransactionModel.COLUMN_ID, TransactionModel.ID);
      sortMap.put(TransactionModel.COLUMN_ORIG_DATE, TransactionModel.ORIG_DATE);
      sortMap.put(TransactionModel.EFFECTIVE_DATE, TransactionModel.EFFECTIVE_DATE);
      sortMap.put(TransactionModel.COLUMN_AMNT, TransactionModel.AMNT);
      sortMap.put(TransactionModel.COLUMN_CURRENCY_ID_FK, TransactionModel.CURRENCY_ID_FK);
      sortMap.put(TransactionModel.TRANSACTION_TYPE_ID_FK, TransactionModel.TRANSACTION_TYPE_ID_FK);
      sortMap.put(TransactionModel.COLUMN_TRANSACTION_TYPE_SUB_CODE_FK, TransactionModel.TRANSACTION_TYPE_SUB_CODE_FK);
      sortMap.put(TransactionModel.COLUMN_STATEMENT_TXT, TransactionModel.STATEMENT_TXT);

      return sortMap;
   }

}
