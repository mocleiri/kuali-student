package com.sigmasys.kuali.ksa.krad.model;

import com.sigmasys.kuali.ksa.model.*;

/**
 * Created by: dmulderink on 9/27/12 at 7:46 AM
 */
public class TransactionModel extends Transaction {

    private TransactionTypeValue transactionTypeValue;

    // Constructor
    public TransactionModel() {
    }

    public TransactionModel(Transaction transaction) {
        transactionTypeValue = transaction.getTransactionTypeValue();
        // populate TransactionModel's properties from Transaction instance
        setId(transaction.getId());
        setTransactionType(transaction.getTransactionType());
        setRollup(transaction.getRollup());
        setExternalId(transaction.getExternalId());
        setCreationDate(transaction.getCreationDate());
        setEffectiveDate(transaction.getEffectiveDate());
        setOriginationDate(transaction.getOriginationDate());
        setRecognitionDate(transaction.getRecognitionDate());
        setAmount(transaction.getAmount());
        setNativeAmount(transaction.getNativeAmount());
        setCurrency(transaction.getCurrency());
        setInternal(transaction.isInternal());
        setAllocatedAmount(transaction.getAllocatedAmount());
        setLockedAllocatedAmount(transaction.getLockedAllocatedAmount());
        setStatementText(transaction.getStatementText());
        setDocument(transaction.getDocument());
        setAccount(transaction.getAccount());
        setGlEntryGenerated(transaction.isGlEntryGenerated());
        setGeneralLedgerType(transaction.getGeneralLedgerType());
        setGlOverridden(transaction.isGlOverridden());

        // charge, payment or deferment specific data members
    }

    @Override
    public TransactionTypeValue getTransactionTypeValue() {
        return transactionTypeValue;
    }
}
