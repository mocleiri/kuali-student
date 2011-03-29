package org.kuali.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertiesPersister;

public class DefaultPropertiesLoader implements PropertiesLoader {
	private static final Logger logger = LoggerFactory.getLogger(DefaultPropertiesLoader.class);

	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";
	public static final boolean DEFAULT_IS_USE_ENVIRONMENT_PROPERTY_PREFIX = false;
	public static final boolean DEFAULT_IS_LOCAL_OVERRIDE = false;
	public static final boolean DEFAULT_IS_IGNORE_RESOURCE_NOT_FOUND = false;
	public static final boolean DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT = true;
	public static final SystemPropertiesMode DEFAULT_SYSTEM_PROPERTIES_MODE = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;

	// Properties with defaults
	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;
	boolean useEnvironmentPropertyPrefix = DEFAULT_IS_USE_ENVIRONMENT_PROPERTY_PREFIX;
	SystemPropertiesMode systemPropertiesMode = DEFAULT_SYSTEM_PROPERTIES_MODE;
	boolean localOverride = DEFAULT_IS_LOCAL_OVERRIDE;
	boolean ignoreResourceNotFound = DEFAULT_IS_IGNORE_RESOURCE_NOT_FOUND;
	boolean searchSystemEnvironment = DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT;

	// Properties without defaults
	String fileEncoding;
	Properties[] localProperties;
	Resource[] locations;

	// Default component beans
	PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	PropertyLogger plogger = new DefaultPropertyLogger();

	// Instance variables filled in during loading
	Properties systemProperties;
	Properties environmentProperties;
	Properties resourceProperties;
	Properties mergedLocalProperties;

	/**
	 * Get a handle to our environment properties. Prefix is optional
	 */
	protected Properties getEnvironmentProperties(String prefix) {
		Map<String, String> environmentMap = SystemUtils.getEnvironmentIgnoreExceptions();
		Properties envProps = new Properties();
		for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
			String key = (prefix == null) ? entry.getKey() : prefix + entry.getKey();
			envProps.setProperty(key, entry.getValue());
		}
		return envProps;
	}

	/**
	 * Given a resource and an InputStream load and return a Properties object. Supports regular as well as XML style
	 * properties files
	 * 
	 * @param location
	 * @param is
	 * @return
	 * @throws IOException
	 */
	protected Properties getProperties(Resource location, InputStream is) throws IOException {
		Properties properties = new Properties();
		// Use XML style loading if it is an XML file
		if (isXMLFile(location)) {
			getPropertiesPersister().loadFromXml(properties, is);
			return properties;
		}

		if (getFileEncoding() != null) {
			// Use a Reader if they've specified a fileEncoding
			getPropertiesPersister().load(properties, new InputStreamReader(is, getFileEncoding()));
		} else {
			// Otherwise use an InputStream
			getPropertiesPersister().load(properties, is);
		}
		return properties;
	}

	/**
	 * Load properties from a Resource object. Returns an empty Properties object if ignoreResourceNotFound is true and
	 * the resource could not be located
	 * 
	 * @param location
	 * @return
	 * @throws IOException
	 */
	protected Properties getProperties(Resource location) throws IOException {
		// Handle locations that don't exist
		if (!location.exists()) {
			return handleResourceNotFound(location);
		}

		// Proceed with loading
		logger.info("Loading properties from {}", location);
		InputStream is = null;
		try {
			is = location.getInputStream();
			return getProperties(location, is);
		} catch (IOException e) {
			throw e;
		} finally {
			nullSafeClose(is);
		}
	}

	/**
	 * True if this location represents an XML file, false otherwise
	 * 
	 * @param location
	 * @return
	 */
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

	/**
	 * Throw an exception unless ignoreResourceNotFound is true
	 * 
	 * @param location
	 * @return
	 */
	protected Properties handleResourceNotFound(Resource location) {
		if (isIgnoreResourceNotFound()) {
			logger.info("Ignoring properties from {}.  Resource not found", location);
			// Return an empty Properties
			return new Properties();
		} else {
			throw new PropertiesLoadException("Resource not found: " + location);
		}
	}

	/**
	 * Close the InputStream
	 * 
	 * @param is
	 * @throws IOException
	 */
	protected void nullSafeClose(InputStream is) throws IOException {
		if (is == null) {
			return;
		}
		is.close();
	}

	/**
	 * Merge the Properties[] into a single Properties object
	 */
	protected Properties getMergedLocalProperties() {
		if (getLocalProperties() == null) {
			// Nothing to do, return an empty Properties object
			return new Properties();
		}
		// Merge the Properties[] into a single Properties object
		Properties result = new Properties();
		for (Properties localProp : getLocalProperties()) {
			CollectionUtils.mergePropertiesIntoMap(localProp, result);
		}
		return result;
	}

	/**
	 * Get properties from the environment
	 */
	protected Properties getEnvironmentProperties() {
		if (isUseEnvironmentPropertyPrefix()) {
			return getEnvironmentProperties(getEnvironmentPropertyPrefix());
		} else {
			return getEnvironmentProperties(null);
		}
	}

	/**
	 * Get system properties
	 * 
	 * @return
	 */
	protected Properties getSystemProperties() {
		return SystemUtils.getSystemPropertiesIgnoreExceptions();
	}

	/**
	 * Merge local, resource, system, and environment properties into a single Properties object. User supplied settings
	 * control which property "wins" if a property is defined in multiple areas
	 */
	public Properties mergeProperties(Properties local, Properties resource, Properties sys, Properties env) {
		// Storage for our merged properties
		Properties result = new Properties();

		// Merge in local properties (nothing to actually merge here, but this also logs them when DEBUG is on)
		PropertiesMergeContext context = new PropertiesMergeContext(result, local, PropertySource.LOCAL);
		mergeProperties(context);

		// Merge in resource properties. localOverride controls what property "wins" if the same
		// property is declared both locally and in a resource
		context = new PropertiesMergeContext(result, resource, isLocalOverride(), PropertySource.RESOURCE);
		mergeProperties(context);

		// Merge in system properties according to the SystemPropertiesMode being used
		if (!getSystemPropertiesMode().equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER)) {
			boolean override = getSystemPropertiesMode().equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE);
			context = new PropertiesMergeContext(result, sys, override, PropertySource.SYSTEM);
			mergeProperties(context);
		}

		// Merge in environment properties. Environment properties never override properties from another source
		context = new PropertiesMergeContext(result, env, false, PropertySource.ENVIRONMENT);
		mergeProperties(context);

		// Return the merged properties
		return result;
	}

	/**
	 * Load all of our properties into a Properties object
	 */
	public Properties loadProperties() {
		try {
			// Populate properties from the default set of locations
			Properties local = getMergedLocalProperties();
			Properties resource = getResourceProperties();
			Properties sys = getSystemProperties();
			Properties env = getEnvironmentProperties();

			// Store the properties locally
			setMergedLocalProperties(local);
			setResourceProperties(resource);
			setSystemProperties(sys);
			setEnvironmentProperties(env);

			// Merge them into a single properties object
			return mergeProperties(local, resource, sys, env);
		} catch (IOException e) {
			throw new PropertiesLoadException("Unexpected error loading properties", e);
		}
	}

	/**
	 * Merge a property under 'key' from newProps into currentProps using the settings from PropertiesMergeContext
	 * 
	 * @param context
	 * @param key
	 */
	public PropertyMergeResult mergeProperty(PropertiesMergeContext context, String key) {
		Properties newProps = context.getNewProperties();
		Properties currentProps = context.getCurrentProperties();
		PropertySource source = context.getSource();
		boolean override = context.isOverride();
		// Extract the new value
		String newValue = newProps.getProperty(key);

		// Extract the existing value
		String currentValue = currentProps.getProperty(key);

		// If the new value is null, there is nothing further to do
		if (newValue == null) {
			PropertyMergeResultReason reason = PropertyMergeResultReason.NOOP_NULL_NEW_VALUE;
			return new PropertyMergeResult(context, key, currentValue, newValue, reason);
		}

		// The newValue is not null, and there is no existing value for this key
		if (currentValue == null) {
			logger.debug("Adding " + source + " property {}=[{}]", key, plogger.getValue(key, newValue));
			currentProps.setProperty(key, newValue);
			PropertyMergeResultReason reason = PropertyMergeResultReason.ADD;
			return new PropertyMergeResult(context, key, currentValue, newValue, reason);
		}

		// Neither value is null and the values are the same, nothing further to do
		if (ObjectUtils.nullSafeEquals(newValue, currentValue)) {
			PropertyMergeResultReason reason = PropertyMergeResultReason.NOOP_IDENTICAL_VALUES;
			return new PropertyMergeResult(context, key, currentValue, newValue, reason);
		}

		if (override) {
			// There is an existing property, but the new property wins
			logger.info(source + " property override for '" + key + "' [{}]->[{}]",
					plogger.getValue(key, currentValue), plogger.getValue(key, newValue));
			currentProps.setProperty(key, newValue);
			PropertyMergeResultReason reason = PropertyMergeResultReason.OVERRIDE;
			return new PropertyMergeResult(context, key, currentValue, newValue, reason);
		} else {
			// There is already an existing property, and the existing property wins
			logger.debug("The existing value for '" + key + "' is not being overridden by the " + source
					+ " value. Existing:[{}] New:[{}]", plogger.getValue(key, currentValue),
					plogger.getValue(key, newValue));
			PropertyMergeResultReason reason = PropertyMergeResultReason.NOOP_EXISTING_WINS;
			return new PropertyMergeResult(context, key, currentValue, newValue, reason);
		}
	}

	public void mergeProperties(PropertiesMergeContext context) {
		List<String> keys = new ArrayList<String>(context.getNewProperties().stringPropertyNames());
		if (context.isSort()) {
			Collections.sort(keys);
		}
		for (String key : keys) {
			mergeProperty(context, key);
		}
	}

	public Properties getResourceProperties() throws IOException {
		if (getLocations() == null || getLocations().length == 0) {
			logger.info("No resource property locations to load from");
			return new Properties();
		}
		Properties result = new Properties();
		for (Resource location : getLocations()) {
			Properties newProps = getProperties(location);
			PropertySource source = PropertySource.RESOURCE;
			// If a property is declared in more than one resource location, the last resource location "wins"
			boolean override = true;
			PropertiesMergeContext context = new PropertiesMergeContext(result, newProps, override, source);
			mergeProperties(context);
		}
		return result;
	}

	public boolean isIgnoreResourceNotFound() {
		return ignoreResourceNotFound;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public Properties[] getLocalProperties() {
		return localProperties;
	}

	public void setLocalProperties(Properties[] localProperties) {
		this.localProperties = localProperties;
	}

	public Resource[] getLocations() {
		return locations;
	}

	public boolean isLocalOverride() {
		return localOverride;
	}

	public PropertiesPersister getPropertiesPersister() {
		return propertiesPersister;
	}

	public String getEnvironmentPropertyPrefix() {
		return environmentPropertyPrefix;
	}

	public void setEnvironmentPropertyPrefix(String environmentPropertyPrefix) {
		this.environmentPropertyPrefix = environmentPropertyPrefix;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = propertiesPersister;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void setLocalOverride(boolean localOverride) {
		this.localOverride = localOverride;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public SystemPropertiesMode getSystemPropertiesMode() {
		return systemPropertiesMode;
	}

	public void setSystemPropertiesMode(SystemPropertiesMode systemPropertiesMode) {
		this.systemPropertiesMode = systemPropertiesMode;
	}

	public boolean isSearchSystemEnvironment() {
		return searchSystemEnvironment;
	}

	public void setSearchSystemEnvironment(boolean searchSystemEnvironment) {
		this.searchSystemEnvironment = searchSystemEnvironment;
	}

	public void setResourceProperties(Properties resourceProperties) {
		this.resourceProperties = resourceProperties;
	}

	public void setSystemProperties(Properties systemProperties) {
		this.systemProperties = systemProperties;
	}

	public void setEnvironmentProperties(Properties environmentProperties) {
		this.environmentProperties = environmentProperties;
	}

	public void setMergedLocalProperties(Properties mergedLocalProperties) {
		this.mergedLocalProperties = mergedLocalProperties;
	}

	public boolean isUseEnvironmentPropertyPrefix() {
		return useEnvironmentPropertyPrefix;
	}

	public void setUseEnvironmentPropertyPrefix(boolean useEnvironmentPropertyPrefix) {
		this.useEnvironmentPropertyPrefix = useEnvironmentPropertyPrefix;
	}

	public PropertyLogger getPlogger() {
		return plogger;
	}

	public void setPlogger(PropertyLogger propertiesLogger) {
		this.plogger = propertiesLogger;
	}

}
