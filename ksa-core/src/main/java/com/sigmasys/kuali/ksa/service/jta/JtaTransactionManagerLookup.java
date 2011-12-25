package com.sigmasys.kuali.ksa.service.jta;

import org.hibernate.transaction.JNDITransactionManagerLookup;

import javax.transaction.Transaction;

/**
 * JtaTransactionManagerLookup.
 * This implementation is based on Hibernate's version of TransactionManagerLookup.
 *
 * @author Michael Ivanov
 */
public class JtaTransactionManagerLookup extends JNDITransactionManagerLookup {

    public static final String TRANSACTION_MANAGER_JNDI_NAME = "javax.transaction.TransactionManager";
    public static final String USER_TRANSACTION_JNDI_NAME = "javax.transaction.UserTransaction";

    /**
     * @see org.hibernate.transaction.JNDITransactionManagerLookup#getName()
     */
    @Override
    protected String getName() {
        return TRANSACTION_MANAGER_JNDI_NAME;
    }

    @Override
    public String getUserTransactionName() {
        return USER_TRANSACTION_JNDI_NAME;
    }

    /**
     * Determine an identifier for the given transaction appropriate for use in caching/lookup usages.
     * <p/>
     * Generally speaking the transaction itself will be returned here.  This method was added specifically
     * for use in WebSphere and other unfriendly JEE containers (although WebSphere is still the only known
     * such brain-dead, sales-driven impl).
     *
     * @param transaction The transaction to be identified.
     * @return An appropropriate identifier
     */
    public Object getTransactionIdentifier(Transaction transaction) {
        return transaction;
    }

}
