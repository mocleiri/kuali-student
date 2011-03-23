package org.kuali.spring.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtils {
	final static Logger logger = LoggerFactory.getLogger(SystemUtils.class);

	public static Map<String, String> getEnvironmentIgnoreSecurity() {
		try {
			return System.getenv();
		} catch (Throwable e) {
			logger.warn("Unable to access environment.  {}", e.getMessage());
			return new HashMap<String, String>();
		}
	}

	public static Properties getSystemPropertiesIgnoreSecurity() {
		try {
			return System.getProperties();
		} catch (Throwable e) {
			logger.warn("Unable to access system properties.  {}", e.getMessage());
			return new Properties();
		}
	}

	public static final String getSystemPropertyIgnoreSecurity(String key) {
		try {
			return System.getProperty(key);
		} catch (Throwable e) {
			logger.warn("Unable to access system property {}: {}", key, e.getMessage());
			return null;
		}
	}

	public static final String getEnvironmentPropertyIgnoreSecurity(String key) {
		try {
			return System.getenv(key);
		} catch (Throwable e) {
			logger.warn("Unable to access environment property {}: {}", key, e.getMessage());
			return null;
		}
	}

}
