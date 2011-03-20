package org.kuali.spring.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class ConfigurablePlaceholderResolver implements PlaceholderResolver {
	final Logger logger = LoggerFactory.getLogger(ConfigurablePlaceholderResolver.class);
	boolean searchSystemEnvironment;
	Properties properties;
	SystemPropertiesMode systemPropertiesMode;

	@Override
	public String resolvePlaceholder(String placeholder) {
		String propVal = null;
		if (systemPropertiesMode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE)) {
			propVal = resolveSystemProperty(placeholder);
		}
		if (propVal == null) {
			propVal = properties.getProperty(placeholder);
		}
		if (propVal == null && systemPropertiesMode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK)) {
			propVal = resolveSystemProperty(placeholder);
		}
		return propVal;
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

	public boolean isSearchSystemEnvironment() {
		return searchSystemEnvironment;
	}

	public void setSearchSystemEnvironment(boolean searchSystemEnvironment) {
		this.searchSystemEnvironment = searchSystemEnvironment;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public SystemPropertiesMode getSystemPropertiesMode() {
		return systemPropertiesMode;
	}

	public void setSystemPropertiesMode(SystemPropertiesMode systemPropertiesMode) {
		this.systemPropertiesMode = systemPropertiesMode;
	}

}
