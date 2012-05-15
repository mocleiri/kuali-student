package com.sigmasys.kuali.ksa.gwt.client.view;

import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import com.sigmasys.kuali.ksa.gwt.client.model.TransactionModel;

/**
  * User: dmulderink
 * Date: 5/14/12
 * Time: 9:26 AM
  */
public class TransactionCompositePanel extends AbstractCompositePanel<TransactionModel> {

   public TransactionCompositePanel() {
      this(null);
   }

   public TransactionCompositePanel(SearchCriteria searchCriteria) {
      super(
            new TransactionSearchPanel(),
            new TransactionListPanel(),
            new TransactionDetailsPanel(),
            300,
            300,
            350,
            searchCriteria != null
      );
   }
}
