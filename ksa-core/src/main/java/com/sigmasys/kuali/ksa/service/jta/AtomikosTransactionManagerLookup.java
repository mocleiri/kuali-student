package com.sigmasys.kuali.ksa.service.jta;


import com.sigmasys.kuali.ksa.util.ContextUtils;
import org.hibernate.HibernateException;
import org.hibernate.transaction.TransactionManagerLookup;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.Properties;


/**
 * AtomikosTransactionManagerLookup.
 *
 * @author Michael Ivanov
 */
public class AtomikosTransactionManagerLookup implements TransactionManagerLookup {


    @Override
    public TransactionManager getTransactionManager(Properties props) throws HibernateException {
        try {
            return ContextUtils.getBean("atomikosTransactionManager", TransactionManager.class);
        } catch (Exception e) {
            throw new HibernateException("Could not obtain Atomikos JTA transaction manager instance", e);
        }
    }

    @Override
    public String getUserTransactionName() {
        return "java:comp/UserTransaction";
    }


    /**
    	 * Determine an identifier for the given transaction appropriate for use in caching/lookup usages.
    	 * <p/>
    	 * Generally speaking the transaction itself will be returned here.  This method was added specifically
    	 * for use in WebSphere and other unfriendly JEE containers (although WebSphere is still the only known
    	 * such brain-dead, sales-driven impl).
    	 *
    	 * @param transaction The transaction to be identified.
    	 * @return An appropriate identifier
    	 */
    @Override
    public Object getTransactionIdentifier(Transaction transaction) {
        return transaction;
    }
}
