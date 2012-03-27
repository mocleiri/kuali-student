package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.util.List;

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
@DiscriminatorValue("D")
public class DebitType extends TransactionType {

    /**
     * Transaction priority
     */
    private Integer priority;


    @Column(name = "PRIORITY")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "KSSA_DEBIT_TAG",
            joinColumns = {
                    @JoinColumn(name = "DEBIT_TYPE_ID_FK"),
                    @JoinColumn(name = "DEBIT_TYPE_SUB_CODE_FK")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "TAG_ID_FK")
            }
    )
    @Override
    public List<Tag> getTags() {
        return tags;
    }

}
