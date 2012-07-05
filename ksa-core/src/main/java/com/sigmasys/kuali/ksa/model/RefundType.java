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

    private CreditType creditType;

    private DebitType debitType;


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
            @JoinColumn(name = "CREDIT_TYPE_ID_FK", referencedColumnName = "ID"),
            @JoinColumn(name = "CREDIT_TYPE_SUB_CODE_FK", referencedColumnName = "SUB_CODE")
    })
    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "DEBIT_TYPE_ID_FK", referencedColumnName = "ID"),
            @JoinColumn(name = "DEBIT_TYPE_SUB_CODE_FK", referencedColumnName = "SUB_CODE")
    })
    public DebitType getDebitType() {
        return debitType;
    }

    public void setDebitType(DebitType debitType) {
        this.debitType = debitType;
    }
}
