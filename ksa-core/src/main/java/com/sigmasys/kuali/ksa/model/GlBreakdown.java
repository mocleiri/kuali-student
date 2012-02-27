package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * GL Breakdown model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_BREAKDOWN")
public class GlBreakdown {

    /**
     * The unique transaction identifier for the KSA product.
     */
    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @TableGenerator(name = "TABLE_GEN",
            table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_BREAKDOWN_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    /**
     * GL account
     */
    @Column(name = "GL_ACCOUNT")
    private String name;


    @Column(name = "BREAKDOWN")
    private BigDecimal amount;


    /**
     * Reference to DEBIT type
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "DEBIT_TYPE_ID_FK", referencedColumnName = "ID"),
            @JoinColumn(name = "DEBIT_TYPE_SUB_CODE_FK", referencedColumnName = "SUB_CODE")
    })
    protected DebitType debitType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DebitType getDebitType() {
        return debitType;
    }

    public void setDebitType(DebitType debitType) {
        this.debitType = debitType;
    }
}
	


