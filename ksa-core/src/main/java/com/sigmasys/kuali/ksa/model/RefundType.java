package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Refund type.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_REFUND_TYPE")
public class RefundType extends AuditableEntity {

    private TransactionType transactionType;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_REFUND_TYPE",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "REFUND_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_REFUND_TYPE")
    @Override
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "TRANSACTION_TYPE_ID_FK", referencedColumnName = "ID"),
            @JoinColumn(name = "TRANSACTION_TYPE_SUB_CODE_FK", referencedColumnName = "SUB_CODE")
    })
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
