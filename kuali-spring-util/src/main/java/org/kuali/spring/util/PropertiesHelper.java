package org.kuali.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.Assert;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertiesPersister;

public class PropertiesHelper {
	final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);
	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";
	public static final boolean DEFAULT_IGNORE_RESOURCE_NOT_FOUND = false;

	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;
	boolean ignoreResourceNotFound = DEFAULT_IGNORE_RESOURCE_NOT_FOUND;
	PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	String fileEncoding;

	protected void loadProperties(Properties properties, Resource location, InputStream is) throws IOException {
		if (isXMLFile(location)) {
			this.propertiesPersister.loadFromXml(properties, is);
			return;
		}
		if (this.fileEncoding == null) {
			this.propertiesPersister.load(properties, is);
		} else {
			this.propertiesPersister.load(properties, new InputStreamReader(is, this.fileEncoding));
		}
	}

	protected void loadProperties(Properties properties, Resource location) throws IOException {
		logger.info("Loading properties from {}", location);
		InputStream is = null;
		try {
			is = location.getInputStream();
			loadProperties(properties, location, is);
		} catch (IOException e) {
			handleIOException(location, e);
		} finally {
			nullSafeClose(is);
		}
	}

	protected boolean isXMLFile(Resource location) {
		String filename = null;
		try {
			filename = location.getFilename();
		} catch (IllegalStateException ex) {
			// resource is not file-based. See SPR-7552.
			return false;
		}
		return (filename != null) && (filename.endsWith(PropertiesLoaderSupport.XML_FILE_EXTENSION));
	}

	protected void handleIOException(Resource location, IOException e) throws IOException {
		if (!this.ignoreResourceNotFound) {
			throw e;
		}
		logger.warn("Could not load properties from {}: {}", location, e.getMessage());
	}

	protected void nullSafeClose(InputStream is) throws IOException {
		if (is == null) {
			return;
		}
		is.close();
	}

	public void loadProperties(Properties properties, Resource[] locations) throws IOException {
		if (locations == null) {
			logger.info("No property locations to load from");
			return;
		}
		for (Resource location : locations) {
			loadProperties(properties, location);
		}
	}

	/**
	 * Create a clone of the properties passed in
	 * 
	 * @param properties
	 * @return
	 */
	public Properties getClone(Properties properties) {
		Properties clone = new Properties();
		for (String propertyName : properties.stringPropertyNames()) {
			String propertyValue = properties.getProperty(propertyName);
			clone.setProperty(propertyName, propertyValue);
		}
		return clone;
	}

	protected Map<String, String> getSystemEnvironment() {
		try {
			return System.getenv();
		} catch (SecurityException e) {
			logger.warn("Unable to access system environment.  {}", e.getMessage());
			return new HashMap<String, String>();
		}
	}

	protected void mergeEnvironmentProperty(Properties properties, Map.Entry<String, String> environmentEntry) {
		String key = this.environmentPropertyPrefix + environmentEntry.getKey();
		String existingValue = properties.getProperty(key);
		String environmentValue = environmentEntry.getValue();
		if (existingValue != null) {
			logger.warn("Environment property override for '" + key + "' [{}]->[{}]", existingValue, environmentValue);
		} else {
			logger.trace("Adding environment property {}=[{}]", key, environmentValue);
		}
		properties.setProperty(key, environmentValue);
	}

	public void mergeEnvironmentProperties(Properties properties) {
		logger.info("Merging environment properties");
		Map<String, String> environment = getSystemEnvironment();
		for (Map.Entry<String, String> environmentEntry : environment.entrySet()) {
			mergeEnvironmentProperty(properties, environmentEntry);
		}
	}

	protected String getSystemPropertyValue(String key) {
		try {
			return System.getProperty(key);
		} catch (SecurityException e) {
			logger.warn("Unable to access system property '{}'.  {}", key, e.getMessage());
			return null;
		}
	}

	protected void mergeSystemProperty(Properties properties, String systemPropertyKey, SystemPropertiesMode mode) {
		// Extract the system property
		String systemPropertyValue = getSystemPropertyValue(systemPropertyKey);

		// Ignore system properties that are null
		if (systemPropertyValue == null) {
			return;
		}

		// Extract the existing property
		String existingPropertyValue = properties.getProperty(systemPropertyKey);

		// Values are the same, nothing to do
		if (ObjectUtils.nullSafeEquals(systemPropertyValue, existingPropertyValue)) {
			return;
		}

		// Double check our system properties mode
		Assert.isTrue(mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE)
				|| mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK));

		// There is no existing property
		if (existingPropertyValue == null) {
			logger.debug("Adding system property {}=[{}]", systemPropertyKey, systemPropertyValue);
			properties.setProperty(systemPropertyKey, systemPropertyValue);
			return;
		}

		// There is an existing property, but system properties win
		if (mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE)) {
			logger.info("System property override for '" + systemPropertyKey + "' [{}]->[{}]", existingPropertyValue,
					systemPropertyValue);
			properties.setProperty(systemPropertyKey, systemPropertyValue);
		}
	}

	public void mergeSystemProperties(Properties properties, SystemPropertiesMode mode) {
		if (mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER)) {
			logger.info("{} - Ignoring system properties.", mode);
			// Nothing to do
			return;
		}

		// Merge in the system properties
		logger.info("{} - Merging system properties with Spring properties", mode);
		for (String systemPropertyKey : System.getProperties().stringPropertyNames()) {
			mergeSystemProperty(properties, systemPropertyKey, mode);
		}
	}
}
