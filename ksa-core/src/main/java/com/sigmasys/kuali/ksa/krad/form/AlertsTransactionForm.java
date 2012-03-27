package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Transaction;
import org.kuali.rice.krad.web.form.UifFormBase;

public class AlertsTransactionForm extends UifFormBase {

   private Transaction alertTransaction;

   public Transaction getTransaction() {
      return alertTransaction;
   }

   public void setTransaction(Transaction transaction) {
      this.alertTransaction = transaction;
   }
}
