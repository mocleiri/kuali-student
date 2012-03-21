package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Alert
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue("A")
public class Alert extends Information {

    /**
     * Text
     */
    private String text;

    @Column(name = "TEXT")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

