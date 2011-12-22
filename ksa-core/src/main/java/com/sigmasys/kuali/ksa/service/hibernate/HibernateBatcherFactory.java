package com.sigmasys.kuali.ksa.service.hibernate;

import org.hibernate.Interceptor;
import org.hibernate.jdbc.Batcher;
import org.hibernate.jdbc.BatcherFactory;
import org.hibernate.jdbc.ConnectionManager;

/**
 * Custom HibernateBatcherFactory that implements Hibernate's BatcherFactory
 * <p/>
 * @author Michael Ivanov
 * 
 */
public class HibernateBatcherFactory implements BatcherFactory {

    @Override
    public Batcher createBatcher(ConnectionManager connectionManager, Interceptor interceptor) {
        return new HibernateBatcher(connectionManager, interceptor);
    }
}
