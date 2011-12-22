package com.sigmasys.kuali.ksa.service.jta;

import org.hibernate.transaction.JNDITransactionManagerLookup;

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
    protected String getName() {
        return TRANSACTION_MANAGER_JNDI_NAME;
    }

    public String getUserTransactionName() {
        return USER_TRANSACTION_JNDI_NAME;
    }

}
