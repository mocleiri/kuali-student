package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * General Ledger Breakdown Override model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_BREAKDOWN_OVERRIDE")
public class GlBreakdownOverride implements Identifiable {

    /**
     * The unique identifier
     */
    private Long id;

    /**
     * GL account
     */
    private String glAccount;


    private BigDecimal percentageBreakdown;


    /**
     * Reference to Transaction
     */
    private Transaction transaction;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_GL_BR_OVERRIDE",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_BREAKDOWN_OVERRIDE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_GL_BR_OVERRIDE")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "BREAKDOWN")
    public BigDecimal getPercentageBreakdown() {
        return percentageBreakdown;
    }

    public void setPercentageBreakdown(BigDecimal percentageBreakdown) {
        this.percentageBreakdown = percentageBreakdown;
    }

    @Column(name = "GL_ACCOUNT", length = 45)
    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_ID_FK")
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
	


