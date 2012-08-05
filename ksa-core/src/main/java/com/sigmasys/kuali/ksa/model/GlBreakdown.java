package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * General Ledger Breakdown model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_BREAKDOWN")
public class GlBreakdown implements Identifiable {

    /**
     * The unique identifier
     */
    private Long id;

    /**
     * GL account
     */
    private String glAccount;

    /**
     * GL operation
     */
    private String glOperation;


    private BigDecimal percentageBreakdown;


    /**
     * Reference to DEBIT type
     */
    private DebitType debitType;

    /**
     * Reference to general ledger type
     */
    private GeneralLedgerType generalLedgerType;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_GL_BR",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_BREAKDOWN_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_GL_BR")
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

    @Column(name = "GL_OPERATION", length = 1)
    public String getGlOperation() {
        return glOperation;
    }

    public void setGlOperation(String glOperation) {
        this.glOperation = glOperation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "TRANSACTION_TYPE_ID_FK", referencedColumnName = "ID"),
            @JoinColumn(name = "TRANSACTION_TYPE_SUB_CODE_FK", referencedColumnName = "SUB_CODE")
    })
    public DebitType getDebitType() {
        return debitType;
    }

    public void setDebitType(DebitType debitType) {
        this.debitType = debitType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GL_TYPE_ID_FK")
    public GeneralLedgerType getGeneralLedgerType() {
        return generalLedgerType;
    }

    public void setGeneralLedgerType(GeneralLedgerType generalLedgerType) {
        this.generalLedgerType = generalLedgerType;
    }
}
	


