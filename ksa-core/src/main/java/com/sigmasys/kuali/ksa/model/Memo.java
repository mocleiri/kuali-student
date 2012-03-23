package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;

/**
 * Memo
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue("M")
public class Memo extends Information {

    /**
     * Follow up memo
     */
    private Memo followUpMemo;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWUP_ID")
    public Memo getFollowUpMemo() {
        return followUpMemo;
    }

    public void setFollowUpMemo(Memo followUpMemo) {
        this.followUpMemo = followUpMemo;
    }
}

