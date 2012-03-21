package com.sigmasys.kuali.ksa.util;

import org.springframework.context.ApplicationContext;

/**
 * ContextUtils
 *
 * @author ivanovm
 */

public class ContextUtils {

    private static ApplicationContext applicationContext;

    private ContextUtils() {
    }

    public static ApplicationContext initContext(ApplicationContext context) {
        if (context != null) {
            applicationContext = context;
            return applicationContext;
        }
        throw new IllegalArgumentException("Application context cannot be null");
    }

    public static ApplicationContext getContext() {
        if (applicationContext == null) {
            throw new IllegalArgumentException("Application context cannot be null");
        }
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        if (applicationContext == null) {
            throw new IllegalStateException("Application context is not initialized. Call one of init() methods prior to calling getBean()");
        }
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> beanInterface) {
        if (applicationContext == null) {
            throw new IllegalStateException("Application context is not initialized. Call one of init() methods prior to calling getBean()");
        }
        return applicationContext.getBean(beanInterface);
    }

    public static <T> T getBean(String name, Class<T> beanInterface) {
        if (applicationContext == null) {
            throw new IllegalStateException("Application context is not initialized. Call one of init() methods prior to calling getBean()");
        }
        return applicationContext.getBean(name, beanInterface);
    }
}
