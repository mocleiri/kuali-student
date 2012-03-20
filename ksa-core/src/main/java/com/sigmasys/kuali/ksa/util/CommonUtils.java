package com.sigmasys.kuali.ksa.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;


public final class CommonUtils {

    private static final String PROPERTIES_PATH = "ksa.properties";

    private static final Log logger = LogFactory.getLog(CommonUtils.class);

    private static final Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    private CommonUtils() {
    }

    public static synchronized Properties getProperties(String name, Class<?> clazz) {
        Properties props = propertiesMap.get(name);
        if (props == null) {
            try {
                props = new Properties();
                InputStream in = clazz.getClassLoader().getResourceAsStream(name);
                if (in == null) {
                    logger.warn("Resource '" + name + "' is not found");
                    return null;
                }
                props.load(in);
                propertiesMap.put(name, props);
            } catch (Exception e) {
                logger.error("Exception occured while loading properties: ", e);
                throw new RuntimeException("Exception occured while loading properties: ", e);
            }
        }
        return props;
    }

    public static Properties getProperties(String name) {
        return getProperties(name, CommonUtils.class);
    }

    public static Properties getProperties() {
        return getProperties(PROPERTIES_PATH);
    }

    public static String getResourceAsString(String resource) {
        BufferedInputStream in = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = CommonUtils.class.getClassLoader();
            }
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            in = new BufferedInputStream(classLoader.getResourceAsStream(resource));
            byte[] buffer = new byte[1024];
            StringBuilder stringBuffer = new StringBuilder();
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, bytesRead));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            logger.error("Cannot load resource defined as " + resource, e);
            throw new RuntimeException("Cannot load resource defined as " + resource, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }


    public static String getModuleBaseUrl(HttpServletRequest request) {
        // Initializing base service URL, i.e.
        // http://localhost:8080/sonar/org.finra.sonar.Application/Application.html
        // will return /org.finra.sonar.Application/
        String requestedUri = request.getRequestURI();
        int contextEnd = request.getContextPath().length();
        int folderEnd = requestedUri.lastIndexOf('/') + 1;
        return requestedUri.substring(contextEnd, folderEnd);
    }

}
