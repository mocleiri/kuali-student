package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Transaction;
import org.kuali.rice.krad.web.form.LookupForm;

/**
 * Transaction details form
 *
 * @author Michael Ivanov
 * Date: 3/23/12
 */
public class TransactionDetailsForm extends LookupForm {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
