package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Drools knowledge base JPA entity.
 * <p/>
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_RULE_SET")
public class RuleSet implements Identifiable {

    /**
     * Document ID
     */
    private String id;

    /**
     * A set of rules in plain-text format
     */
    private String rules;


    @Id
    @Column(name = "ID", nullable = false)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Lob
    @Column(name = "RULE_SET", nullable = false)
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
