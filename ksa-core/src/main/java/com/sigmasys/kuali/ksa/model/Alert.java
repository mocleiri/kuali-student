package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Alert
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue(InformationTypeValue.ALERT_CODE)
public class Alert extends Information {

    /**
     * Text
     */
    private String text;

    @Column(name = "TEXT", length = 4000)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

