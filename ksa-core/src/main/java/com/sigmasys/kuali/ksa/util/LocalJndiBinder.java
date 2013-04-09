package com.sigmasys.kuali.ksa.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.Ordered;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LocalJndiBinder. Provides local JNDI context.
 *
 * @author Michael Ivanov
 */
public class LocalJndiBinder implements Ordered {

    private static final Log logger = LogFactory.getLog(LocalJndiBinder.class);

    private static int REGISTRY_PORT_NUMBER = Registry.REGISTRY_PORT;

    private static final AtomicBoolean isInitialized = new AtomicBoolean();

    private static InitialContext initialContext;

    public LocalJndiBinder(Map<String, Object> beans) throws Exception {

        if (isInitialized.get()) {
            return;
        }

        logger.debug("JNDI binder initializing...");

        try {

            LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);

            Properties jndiEnv = new Properties();
            jndiEnv.put(Context.INITIAL_CONTEXT_FACTORY, com.sun.jndi.rmi.registry.RegistryContextFactory.class.getName());
            jndiEnv.put(Context.PROVIDER_URL, "rmi://localhost:" + REGISTRY_PORT_NUMBER);

            // Adding JNDI props to system properties to make them visible to
            // all JNDI clients within the JVM
            System.getProperties().putAll(jndiEnv);

            initialContext = new InitialContext(jndiEnv);

            isInitialized.set(true);

            logger.debug("JNDI binder initialized");

            setBindings(beans);

        } catch (RemoteException t) {
            logger.error(t.getMessage(), t);
        } finally {
            REGISTRY_PORT_NUMBER++;
        }

    }

    protected void setBindings(Map<String, Object> beans) {

        if (initialContext == null) {
            logger.error("Initial context is null");
            throw new IllegalStateException("Initial context is null");
        }

        for (Map.Entry<String, Object> beanMapping : beans.entrySet()) {
            try {
                String name = beanMapping.getKey();
                Object bean = beanMapping.getValue();
                initialContext.rebind(name, bean);
                logger.debug("Added JNDI binding: name = " + name + " value = " + bean.getClass().getName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    @Override
    public int getOrder() {
        // This service should be initialized ahead of all others
        return 0;
    }

}
