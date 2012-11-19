package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.util.Date;

/**
 * General Ledger Recognition Period model.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_GL_RECOGNITION_PERIOD")
public class GlRecognitionPeriod extends AuditableEntity {


    /**
     * Starting date of the period.
     */
    private Date startDate;

    /**
     * End date of the period.
     */
    private Date endDate;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GL_RECOGNITION_PERIOD",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "GL_RECOGNITION_PERIOD_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GL_RECOGNITION_PERIOD")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "START_DATE", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    @Column(name = "END_DATE", nullable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
