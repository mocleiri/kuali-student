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

/**
 * LocalJndiBinder. Provides local JNDI context.
 *
 * @author ivanovm
 */
public class LocalJndiBinder implements Ordered {

    private static final Log logger = LogFactory.getLog(LocalJndiBinder.class);

    private final InitialContext initialContext;

    public LocalJndiBinder() throws Exception {
    	logger.debug("JNDI binder initializing...");
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException t) {
            logger.error(t.getMessage(), t);
        }

        Properties jndiEnv = new Properties();
        jndiEnv.put(Context.INITIAL_CONTEXT_FACTORY, com.sun.jndi.rmi.registry.RegistryContextFactory.class.getName());
        jndiEnv.put(Context.PROVIDER_URL, "rmi://localhost:" + Registry.REGISTRY_PORT);

        // Adding JNDI props to system properties to make them visible to all JNDI clients within the JVM
        System.getProperties().putAll(jndiEnv);

        initialContext = new InitialContext(jndiEnv);
        
        logger.debug("JNDI binder initialized");
    }

    public void setBindings(Map<String, Object> beans) {
        try {
            for (Map.Entry<String, Object> beanMapping : beans.entrySet()) {
                String name = beanMapping.getKey();
                Object bean = beanMapping.getValue();
                initialContext.rebind(name, bean);
                logger.debug("Added JNDI binding: name = " + name + " value = " + bean.getClass().getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    @Override
    public int getOrder() {
    	// This service should be initialized ahead of all others
    	return 0;
    }

}
