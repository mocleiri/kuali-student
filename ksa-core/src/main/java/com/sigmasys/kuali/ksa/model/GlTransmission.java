package com.sigmasys.kuali.ksa.model;


import javax.persistence.*;
import java.util.Date;


/**
 * General ledger transmission model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_TRANSMISSION")
public class GlTransmission implements Identifiable {

    /**
     * The unique identifier
     */
    private Long id;

    /**
     * General ledger account ID
     */
    private String glAccountId;

    /**
     * Transmission timestamp
     */
    private Date timestamp;

    /**
     * Earliest transaction date
     */
    private Date earliestDate;

    /**
     * Latest transaction date
     */
    private Date latestDate;

    /**
     * Batch ID
     */
    private String batchId;

    /**
     * Optional GL result
     */
    private String result;

    /**
     * Recognition period
     */
    private String recognitionPeriod;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_GL_TRANSMISSION",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_TRANSMISSION_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_GL_TRANSMISSION")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "GL_ACCOUNT_ID", length = 45)
    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    @Column(name = "TRANSMISSION_DATE")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "EARLIEST_DATE")
    public Date getEarliestDate() {
        return earliestDate;
    }

    public void setEarliestDate(Date earliestDate) {
        this.earliestDate = earliestDate;
    }

    @Column(name = "LATEST_DATE")
    public Date getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }

    @Column(name = "RESULT", length = 2048)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Column(name = "BATCH_ID", length = 100)
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Column(name = "RECOGNITION_PERIOD", length = 45)
    public String getRecognitionPeriod() {
        return recognitionPeriod;
    }

    public void setRecognitionPeriod(String recognitionPeriod) {
        this.recognitionPeriod = recognitionPeriod;
    }
}
	


