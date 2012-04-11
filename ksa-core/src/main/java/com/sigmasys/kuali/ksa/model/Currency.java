package com.sigmasys.kuali.ksa.model;


import javax.persistence.*;

/**
 * Currency model.
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
@Entity
@Table(name = "KSSA_CURRENCY")
public class Currency extends AuditableEntity {

    private String iso;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_CURRENCY",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "CURRENCY_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_CURRENCY")
    @Override
    public Long getId() {
        return id;
    }

    @Column(name = "ISO", unique = true, nullable = false, length = 10)
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}
