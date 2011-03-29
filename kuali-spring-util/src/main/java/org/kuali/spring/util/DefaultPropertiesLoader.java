package org.kuali.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DefaultPropertiesPersister;
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
	PropertiesHelper helper = new PropertiesHelper();
	PropertyLogger plogger = new DefaultPropertyLogger();

	// Filled in during loading
	Properties systemProperties;
	Properties environmentProperties;
	Properties resourceProperties;
	Properties mergedLocalProperties;

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

	protected Properties getProperties(Resource location) throws IOException {
		// Handle locations that don't exist
		if (!location.exists()) {
			return handleResourceNotFound(location);
		}

		// Otherwise proceed with loading
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

	protected Properties handleResourceNotFound(Resource location) {
		if (isIgnoreResourceNotFound()) {
			logger.info("Ignoring properties from {}.  Resource not found", location);
			// Return an empty Properties
			return new Properties();
		} else {
			throw new PropertiesLoadException("Resource not found: " + location);
		}
	}

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
			return getHelper().getEnvironmentProperties(getEnvironmentPropertyPrefix());
		} else {
			return getHelper().getEnvironmentProperties(null);
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
		getHelper().mergeProperties(context);

		// Merge in resource properties. isLocalOverride() controls what property "wins" if the same
		// property is declared both locally and in a resource
		context = new PropertiesMergeContext(result, resource, isLocalOverride(), PropertySource.RESOURCE);
		getHelper().mergeProperties(context);

		// Merge in system properties. systemPropertiesMode controls system property overrides
		getHelper().mergeSystemProperties(result, sys, getSystemPropertiesMode());

		// Merge in environment properties. Environment properties never override properties from another source
		context = new PropertiesMergeContext(result, env, false, PropertySource.ENVIRONMENT);
		getHelper().mergeProperties(context);

		// Return the merged properties
		return result;
	}

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
			helper.mergeProperties(context);
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

	public PropertiesHelper getHelper() {
		return helper;
	}

	public void setHelper(PropertiesHelper propertiesHelper) {
		this.helper = propertiesHelper;
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
