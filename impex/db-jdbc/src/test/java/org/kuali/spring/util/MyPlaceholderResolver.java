package org.kuali.spring.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class MyPlaceholderResolver implements PlaceholderResolver {
	final Logger logger = LoggerFactory.getLogger(MyPlaceholderResolver.class);
	boolean searchSystemEnvironment;
	Properties properties;
	SystemPropertiesMode systemPropertiesMode;

	@Override
	public String resolvePlaceholder(String placeholderName) {
		return resolvePlaceholder(placeholderName, properties, systemPropertiesMode);
	}

	protected String resolvePlaceholder(String placeholder, Properties props, SystemPropertiesMode systemPropertiesMode) {
		String propVal = null;
		if (systemPropertiesMode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE)) {
			propVal = resolveSystemProperty(placeholder);
		}
		if (propVal == null) {
			propVal = resolvePlaceholder(placeholder, props);
		}
		if (propVal == null && systemPropertiesMode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK)) {
			propVal = resolveSystemProperty(placeholder);
		}
		return propVal;
	}

	protected String resolvePlaceholder(String placeholder, Properties props) {
		return props.getProperty(placeholder);
	}

	protected String resolveSystemProperty(String key) {
		try {
			String value = System.getProperty(key);
			if (value == null && this.searchSystemEnvironment) {
				value = System.getenv(key);
			}
			return value;
		} catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not access system property '" + key + "': " + ex);
			}
			return null;
		}
	}

}
