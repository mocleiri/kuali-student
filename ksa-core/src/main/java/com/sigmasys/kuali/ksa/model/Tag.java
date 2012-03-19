package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Tag.
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_TAG")
public class Tag extends AuditableEntity {


    private Integer level;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_TAG",
            table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "TAG_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_TAG")
    @Override
    public Long getId() {
        return id;
    }


    @Column(name = "LEVEL")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
