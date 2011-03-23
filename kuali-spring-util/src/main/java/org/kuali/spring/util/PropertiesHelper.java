package org.kuali.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
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
	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();

	public PropertiesHelper() {
		this(DEFAULT_IGNORE_RESOURCE_NOT_FOUND, null);
	}

	public PropertiesHelper(boolean ignoreResourceNotFound, String fileEncoding) {
		super();
		this.ignoreResourceNotFound = ignoreResourceNotFound;
		this.fileEncoding = fileEncoding;
	}

	/**
	 * Remove any properties not in approvedKeys
	 * 
	 * @param properties
	 * @param approvedKeys
	 */
	protected void removeProperties(Properties properties, Set<String> approvedKeys) {
		// Extract the set of existing property names
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			// Don't do anything, this property is approved
			if (approvedKeys.contains(key)) {
				continue;
			}
			logger.trace("Removing key '{}'", key);
			// Remove this property as it is not in the approved set
			properties.remove(key);
		}
	}

	/**
	 * Add any missing properties from necessaryProperties
	 * 
	 * @param properties
	 * @param necessaryProperties
	 */
	protected void addProperties(Properties properties, Properties necessaryProperties) {
		// Extract the set of necessary property names
		Set<String> necessaryKeys = necessaryProperties.stringPropertyNames();
		// Extract the set of existing property names
		Set<String> keys = properties.stringPropertyNames();
		for (String necessaryKey : necessaryKeys) {
			// Don't do anything, the property is already present
			if (keys.contains(necessaryKey)) {
				continue;
			}

			// Add the missing property
			String necessaryValue = necessaryProperties.getProperty(necessaryKey);
			logger.trace("Adding property {}=[{}]", necessaryKey, necessaryValue);
			properties.setProperty(necessaryKey, necessaryValue);
		}
	}

	public void mergeProperties(Properties oldProperties, Properties newProperties) {
		removeProperties(oldProperties, newProperties.stringPropertyNames());
		addProperties(oldProperties, newProperties);
		updateProperties(oldProperties, newProperties);
	}

	protected void updateProperties(Properties oldProperties, Properties newProperties) {
		List<String> oldNames = new ArrayList<String>(oldProperties.stringPropertyNames());
		Collections.sort(oldNames);
		Iterator<String> itr = oldNames.iterator();
		while (itr.hasNext()) {
			String commonKey = itr.next();
			String oldPropertyValue = oldProperties.getProperty(commonKey);
			String newPropertyValue = newProperties.getProperty(commonKey);
			// The values are the same. Don't do anything
			if (ObjectUtils.nullSafeEquals(oldPropertyValue, newPropertyValue)) {
				continue;
			}

			// Update the old property value with the new property value
			logger.trace("Updating property '" + commonKey + "' [{}]->[{}]",
					loggerSupport.getPropertyValue(commonKey, oldPropertyValue),
					loggerSupport.getPropertyValue(commonKey, newPropertyValue));
			oldProperties.setProperty(commonKey, newPropertyValue);
		}
	}

	protected Properties loadProperties(Resource location, InputStream is) throws IOException {
		Properties properties = new Properties();
		if (isXMLFile(location)) {
			this.propertiesPersister.loadFromXml(properties, is);
			return properties;
		}

		// It is not an xml file
		if (this.fileEncoding == null) {
			this.propertiesPersister.load(properties, is);
		} else {
			this.propertiesPersister.load(properties, new InputStreamReader(is, this.fileEncoding));
		}
		return properties;
	}

	protected Properties getProperties(Resource location) throws IOException {
		logger.info("Loading properties from {}", location);
		InputStream is = null;
		try {
			is = location.getInputStream();
			return loadProperties(location, is);
		} catch (IOException e) {
			handleIOException(location, e);
		} finally {
			nullSafeClose(is);
		}
		return new Properties();
	}

	protected boolean isXMLFile(Resource location) {
		String filename = null;
		try {
			filename = location.getFilename();
		} catch (IllegalStateException ex) {
			// resource is not file-based. See SPR-7552.
			return false;
		}
		// May not have thrown an exception, but might still be null
		if (filename == null) {
			return false;
		}
		// It's an XML file
		if (filename.endsWith(PropertiesLoaderSupport.XML_FILE_EXTENSION)) {
			return true;
		} else {
			return false;
		}
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
		if (locations == null || locations.length == 0) {
			logger.info("No property locations to load from");
			return;
		}
		for (Resource location : locations) {
			Properties newProps = getProperties(location);
			mergeProperties(properties, newProps, true, PropertiesSource.RESOURCE.toString());
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

	protected Properties getEnvironmentAsProperties(String prefix) {
		Map<String, String> environmentMap = getEnvironment();
		Properties envProps = new Properties();
		for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
			envProps.setProperty(prefix + entry.getKey(), entry.getValue());
		}
		return envProps;
	}

	protected Map<String, String> getEnvironment() {
		try {
			return System.getenv();
		} catch (SecurityException e) {
			logger.warn("Unable to access system environment.  {}", e.getMessage());
			return new HashMap<String, String>();
		}
	}

	public void mergeEnvironmentProperties(Properties currentProps) {
		logger.info("Merging environment properties");
		String source = PropertiesSource.ENVIRONMENT.toString();
		mergeProperties(currentProps, getEnvironmentAsProperties(getEnvironmentPropertyPrefix()), true, source);
	}

	protected Properties getSystemProperties() {
		try {
			return System.getProperties();
		} catch (SecurityException e) {
			logger.warn("Unable to access system properties.  {}", e.getMessage());
			return new Properties();
		}
	}

	protected String getSystemProperty(String key) {
		try {
			return System.getProperty(key);
		} catch (SecurityException e) {
			logger.warn("Unable to access system property '{}'.  {}", key, e.getMessage());
			return null;
		}
	}

	protected void mergeProperty(Properties currentProps, Properties newProps, String key, boolean override, String src) {
		// Extract the new value
		String newValue = newProps.getProperty(key);

		// Ignore values that are null
		if (newValue == null) {
			return;
		}

		// Extract the existing property
		String currentValue = currentProps.getProperty(key);

		// There is no existing property
		if (currentValue == null) {
			logger.debug("Adding " + src + " property {}=[{}]", key, newValue);
			currentProps.setProperty(key, newValue);
			return;
		}

		// Values are the same, nothing to do
		if (ObjectUtils.nullSafeEquals(newValue, currentValue)) {
			return;
		}

		// There is an existing property, but the new property wins
		if (override) {
			logger.info(src + " property override for '" + key + "' [{}]->[{}]", currentValue, newValue);
			currentProps.setProperty(key, newValue);
		}
	}

	public void mergeProperties(Properties currentProps, Properties newProps, boolean override, String source) {
		for (String key : newProps.stringPropertyNames()) {
			mergeProperty(currentProps, newProps, key, override, source);
		}
	}

	public void mergeSystemProperties(Properties currentProps, SystemPropertiesMode mode) {
		if (mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER)) {
			logger.info("Ignoring system properties because mode is {}", mode);
			// Nothing to do
			return;
		}

		// Merge in the system properties
		logger.info("Merging system properties with Spring properties using mode {}", mode);
		boolean override = mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		mergeProperties(currentProps, getSystemProperties(), override, PropertiesSource.SYSTEM.toString());
	}

	public String getEnvironmentPropertyPrefix() {
		return environmentPropertyPrefix;
	}

	public void setEnvironmentPropertyPrefix(String environmentPropertyPrefix) {
		this.environmentPropertyPrefix = environmentPropertyPrefix;
	}

	public boolean isIgnoreResourceNotFound() {
		return ignoreResourceNotFound;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public PropertiesPersister getPropertiesPersister() {
		return propertiesPersister;
	}

	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = propertiesPersister;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}
}
