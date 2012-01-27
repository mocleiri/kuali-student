package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DebitType
 * Defines information about the debit.
 * Expressed as a transaction code, this defines what general ledger accounts the debit will pay,
 * the percentage allocations to those accounts,
 * etc. The effective date of a debit also can alter the attributes of the debitType.
 * <p/>
 * <p/>
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 4:13 PM
 */
@Entity
@Table(name = "KSSA_DEBIT_TYPE")
public class DebitType extends TransactionType {

    /**
     * Transaction priority
     */
	@Column(name = "PRIORITY")
    private Integer priority;


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
