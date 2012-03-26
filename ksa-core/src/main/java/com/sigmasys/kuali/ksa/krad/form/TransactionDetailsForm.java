package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Transaction;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;

/**
 * Transaction details form
 *
 * @author Michael Ivanov
 * Date: 3/23/12
 */
public class TransactionDetailsForm extends UifFormBase {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public BigDecimal getFormattedAmount() {
        if ( transaction != null ) {
            BigDecimal amount = transaction.getAmount();
            if (amount != null) {
                 return amount.setScale(5, BigDecimal.ROUND_CEILING);
            }
        }
        return BigDecimal.ZERO;
    }
}
