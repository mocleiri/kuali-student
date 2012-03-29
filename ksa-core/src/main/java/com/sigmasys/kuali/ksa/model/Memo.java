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
    private Memo nextMemo;

    /**
     * Prior memo
     */
    private Memo previousMemo;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_ID")
    public Memo getNextMemo() {
        return nextMemo;
    }

    public void setNextMemo(Memo nextMemo) {
        this.nextMemo = nextMemo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREV_ID")
    public Memo getPreviousMemo() {
        return previousMemo;
    }

    public void setPreviousMemo(Memo previousMemo) {
        this.previousMemo = previousMemo;
    }
}

