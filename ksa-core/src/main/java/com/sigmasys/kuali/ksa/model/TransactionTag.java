package com.sigmasys.kuali.ksa.model;

/**
 * TransactionTag.
 * //TODO: provide a detail description of it
 * 
 * User: mike
 * Date: 1/22/12
 * Time: 4:40 PM
 */
public class TransactionTag {
    
    private Transaction transaction;
    
    private Tag tag;

    public TransactionTag() {
    }

    public TransactionTag(Transaction transaction, Tag tag) {
        this.transaction = transaction;
        this.tag = tag;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
