package com.sigmasys.kuali.ksa.model;

import com.sigmasys.kuali.ksa.util.EnumUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * General ledger transaction model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_TRANSACTION")
public class GlTransaction implements Identifiable {

    /**
     * The unique identifier
     */
    private Long id;

    /**
     * Transaction that prompted this GL transaction
     */
    private Transaction transaction;

    /**
     * Transmission of the current GL transaction
     */
    private GlTransmission transmission;

    /**
     * General ledger account ID
     */
    private String glAccountId;

    /**
     * Transaction date
     */
    private Date date;

    /**
     * Transaction amount
     */
    private BigDecimal amount;

    /**
     * Generated text
     */
    private String generatedText;

    private GlTransactionStatus status;

    private String statusCode;


    @PrePersist
    void populateDBFields() {
        statusCode = (status != null) ? status.getId() : null;
    }

    @PostLoad
    void populateTransientFields() {
        status = (statusCode != null) ? EnumUtils.findById(GlTransactionStatus.class, statusCode) : null;
    }


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_GL_TRANSACTION",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_TRANSACTION_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_GL_TRANSACTION")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_ID_FK", unique = true, nullable = false)
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSMISSION_ID_FK")
    public GlTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(GlTransmission transmission) {
        this.transmission = transmission;
    }

    @Column(name = "GL_ACCOUNT_ID", length = 45)
    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    @Column(name = "TRANSACTION_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "AMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "GENERATED_TEXT", length = 1024)
    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    @Transient
    public GlTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(GlTransactionStatus status) {
        this.status = status;
    }

    protected void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Column(name = "STATUS", length = 1)
    protected String getStatusCode() {
        return statusCode;
    }
}
	


