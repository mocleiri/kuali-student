package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Flag type.
 * <p/>
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
@Entity
@Table(name = "KSSA_FLAG_TYPE")
public class FlagType extends AuditableEntity {

    /**
     * Level
     */
    private Integer level;

    /**
     * Flag info
     */
    private String flagInfo;

    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    @TableGenerator(name = "TABLE_GEN_FLAG_TYPE",
            table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "FLAG_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_FLAG_TYPE")
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

    @Column(name = "FLAG_INFO")
    public String getFlagInfo() {
        return flagInfo;
    }

    public void setFlagInfo(String flagInfo) {
        this.flagInfo = flagInfo;
    }

}
