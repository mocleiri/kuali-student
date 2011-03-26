package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;

/**
 * This class is similar to PropertyPlaceholderConfigurer from Spring. It is used to update bean properties with values
 * from properties files. It has all of the features from the Spring configurer, fixes a few bugs, adds a few new
 * features and is much more pluggable
 */
public class PropertyHandler extends PropertyResourceConfigurer implements BeanNameAware, BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);
	public static final boolean DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT = true;
	public static final boolean DEFAULT_IS_AUTO_WIRE = true;
	public static final boolean DEFAULT_IS_VALIDATE = true;
	public static final boolean DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;
	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";
	public static final SystemPropertiesMode DEFAULT_SYSTEM_PROPERTIES_MODE = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;

	String beanName;
	BeanFactory beanFactory;
	boolean autoWire = DEFAULT_IS_AUTO_WIRE;
	boolean validate = DEFAULT_IS_VALIDATE;

	/**
	 * Automatically wire together a default set of components for handling properties.<br>
	 * 
	 * The intent here is to lessen the configuration burden imposed on users of this class. Allow users to think at the
	 * property level.<br>
	 * 
	 * For example, setting ignoreResourceNotFound to true on the PropertiesLoader will have the desired affect without
	 * also requiring a user to configure how PropertiesLogger and PropertiesHelper are wired into the PropertiesLoader.<br>
	 * 
	 * Nothing prevents altering how components are wired together.<br>
	 * 
	 * The default wiring logic only takes action when null values are detected on a component that the default
	 * PropertyHandler logic needs and there is a default implementation available.<br>
	 * 
	 * Disable automatic wiring by setting autoWire=false
	 */
	Wirer wirer = new DefaultAutoWirer(this);

	/**
	 * Contains all of the properties known to this configurer AFTER they have been resolved (properties can come from
	 * resources, system, environment etc)
	 */
	Properties properties;

	/**
	 * Contains all of the properties known to this configurer BEFORE they have been resolved
	 */
	Properties unresolvedProperties;

	/**
	 * Contains the resolved properties that originate from resources
	 */
	Properties springProperties;

	/**
	 * Contains the unresolved properties that originate from resources
	 */
	Properties unresolvedSpringProperties;

	/**
	 * A list of resource locations to load properties from
	 */
	Resource[] locations;

	/**
	 * Properties that originate from the environment are prefixed with "env" by default
	 */
	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;

	/**
	 * Include environment properties?
	 */
	boolean searchSystemEnvironment = DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT;

	/**
	 * Controls how system properties are handled
	 */
	SystemPropertiesMode systemPropertiesMode = DEFAULT_SYSTEM_PROPERTIES_MODE;

	/**
	 * Provides control over how properties are logged
	 */
	PropertiesLogger propertiesLogger;

	/**
	 * Utility class for working with properties
	 */
	PropertiesHelper helper;

	/**
	 * Utility class for loading properties from resources
	 */
	PropertiesLoader loader;

	/**
	 * Utility class for replacing placeholders with values
	 */
	PlaceholderReplacer replacer;

	/**
	 * Strategy for obtaining property values
	 */
	PropertyRetriever retriever;

	/**
	 * Strategy for resolving string values
	 */
	StringValueResolver resolver;

	/**
	 * Updates bean definitions with property values
	 */
	BeanDefinitionVisitor visitor;

	protected void autoWire() {
		wirer.wire();
	}

	/**
	 * Make sure we have all of the components needed by the default property handling logic
	 */
	protected void validate() {
		Assert.notNull(getPropertiesLogger());
		Assert.notNull(getHelper());
		Assert.notNull(getLoader());
		Assert.notNull(getReplacer());
		Assert.notNull(getRetriever());
		Assert.notNull(getResolver());
		Assert.notNull(getVisitor());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (isAutoWire()) {
			autoWire();
		}
		if (isValidate()) {
			validate();
		}
		super.postProcessBeanFactory(beanFactory);
	}

	@Override
	public void setLocation(Resource location) {
		super.setLocation(location);
		this.locations = new Resource[] { location };
	}

	@Override
	public void setLocations(Resource[] locations) {
		super.setLocations(locations);
		this.locations = locations;
	}

	public void resolvePlaceholders(Properties properties) {
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to resolve");
			return;
		}

		// Properties after all placeholders have been resolved
		Properties resolvedProperties = getResolvedProperties(properties);
		springProperties = getResolvedProperties(unresolvedSpringProperties);

		if (logger.isDebugEnabled()) {
			logger.debug(propertiesLogger.getLogEntry(unresolvedProperties, "*** Unresolved Properties ***"));
		}

		// Synchronize the properties passed in with our resolved properties
		helper.syncProperties(properties, resolvedProperties);

		if (logger.isInfoEnabled()) {
			logger.info(propertiesLogger.getLogEntry(springProperties, "*** Spring Properties ***"));
			logger.info(propertiesLogger.getLogEntry(properties, "*** All Properties ***"));
		}
	}

	protected Properties getResolvedProperties(Properties properties) {
		logger.info("Resolving placeholders in properties");
		Properties resolvedProperties = new Properties();
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			resolveProperty(key, properties, resolvedProperties);
		}
		return resolvedProperties;
	}

	protected void resolveProperty(String key, Properties originalProperties, Properties resolvedProperties) {
		// First resolve any placeholders in the key itself
		logger.trace("Resolving placeholders in key '{}'", key);
		String resolvedKey = replacer.replacePlaceholders(key, originalProperties);
		if (!key.equals(resolvedKey)) {
			logger.debug("Resolved key [{}]->[{}]", key, resolvedKey);
		}
		// Get a value for the key
		String rawValue = retriever.retrieveProperty(key);
		logger.trace("Raw value for '{}' is [{}]", key, rawValue);
		logger.trace("Replacing placeholders in value [{}]", rawValue);
		// Now replace any placeholders in the value
		String resolvedValue = replacer.replacePlaceholders(rawValue, originalProperties);
		if (!rawValue.equals(resolvedValue)) {
			logger.debug("Resolved value for '" + resolvedKey + "' [{}]->[{}]", rawValue, resolvedValue);
		}
		// The only items allowed into resolvedProperties are fully resolved keys and values
		logger.trace("Adding to resolved properties {}=[{}]", resolvedKey, resolvedValue);
		resolvedProperties.setProperty(resolvedKey, resolvedValue);
	}

	protected boolean currentBeanIsMe(String currentBean, ConfigurableListableBeanFactory beanFactory) {
		if (!currentBean.equals(this.beanName)) {
			return false;
		}
		if (!beanFactory.equals(this.beanFactory)) {
			return false;
		}
		return true;
	}

	protected void processBeanDefinition(String currentBean, BeanDefinition bd) {
		try {
			visitor.visitBeanDefinition(bd);
		} catch (Exception e) {
			throw new BeanDefinitionStoreException(bd.getResourceDescription(), currentBean, e.getMessage(), e);
		}
	}

	protected void processBeanDefinitions(ConfigurableListableBeanFactory beanFactory) {
		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String currentBean : beanNames) {
			BeanDefinition bd = beanFactory.getBeanDefinition(currentBean);
			// Skip processing our own bean definition
			// Prevent failing on unresolvable placeholders in the locations property
			if (currentBeanIsMe(currentBean, beanFactory)) {
				logger.info("Skipping placeholder resolution on my own bean definition " + bd);
				continue;
			}
			processBeanDefinition(currentBean, bd);
		}
	}

	/**
	 * Override the default resource loading logic to clean it up and emit sensible log messages. Anytime a property
	 * value is overridden an INFO level logging message is produced. With TRACE turned on, all properties are logged in
	 * the order they are loaded
	 */
	@Override
	protected void loadProperties(Properties properties) throws IOException {
		this.loader.loadProperties(properties, getLocations());
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		// The super class method loads properties from resources as well as properties defined directly on this bean
		Properties properties = super.mergeProperties();
		// Preserve just the Spring properties
		setUnresolvedSpringProperties(helper.getClone(properties));
		// Merge in the system properties as appropriate
		helper.mergeSystemProperties(properties, getSystemPropertiesMode());
		// Merge in environment properties as appropriate
		if (isSearchSystemEnvironment()) {
			helper.mergeEnvironmentProperties(properties, getEnvironmentPropertyPrefix());
		}
		if (retriever instanceof PropertiesRetriever) {
			// Give the retriever a handle to the properties
			((PropertiesRetriever) retriever).setProperties(properties);
		}
		// Retain a reference to the unresolved properties
		setUnresolvedProperties(helper.getClone(properties));
		// resolve placeholders in the properties
		resolvePlaceholders(properties);
		// Store the complete set of resolved properties
		setProperties(properties);
		// return the complete set of properties
		return properties;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		logger.info("Resolving placeholders in bean definitions");

		// Process placeholders in the bean definitions
		processBeanDefinitions(beanFactory);

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactory.resolveAliases(resolver);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactory.addEmbeddedValueResolver(resolver);
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public boolean isAutoWire() {
		return autoWire;
	}

	public void setAutoWire(boolean autoWire) {
		this.autoWire = autoWire;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public Wirer getWirer() {
		return wirer;
	}

	public void setWirer(Wirer wirer) {
		this.wirer = wirer;
	}

	public String getEnvironmentPropertyPrefix() {
		return environmentPropertyPrefix;
	}

	public void setEnvironmentPropertyPrefix(String environmentPropertyPrefix) {
		this.environmentPropertyPrefix = environmentPropertyPrefix;
	}

	public boolean isSearchSystemEnvironment() {
		return searchSystemEnvironment;
	}

	public void setSearchSystemEnvironment(boolean searchSystemEnvironment) {
		this.searchSystemEnvironment = searchSystemEnvironment;
	}

	public SystemPropertiesMode getSystemPropertiesMode() {
		return systemPropertiesMode;
	}

	public void setSystemPropertiesMode(SystemPropertiesMode systemPropertiesMode) {
		this.systemPropertiesMode = systemPropertiesMode;
	}

	public PropertiesHelper getHelper() {
		return helper;
	}

	public void setHelper(PropertiesHelper helper) {
		this.helper = helper;
	}

	public PropertiesLoader getLoader() {
		return loader;
	}

	public void setLoader(PropertiesLoader loader) {
		this.loader = loader;
	}

	public PlaceholderReplacer getReplacer() {
		return replacer;
	}

	public void setReplacer(PlaceholderReplacer replacer) {
		this.replacer = replacer;
	}

	public PropertyRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(PropertyRetriever retriever) {
		this.retriever = retriever;
	}

	public StringValueResolver getResolver() {
		return resolver;
	}

	public void setResolver(StringValueResolver resolver) {
		this.resolver = resolver;
	}

	public BeanDefinitionVisitor getVisitor() {
		return visitor;
	}

	public void setVisitor(BeanDefinitionVisitor visitor) {
		this.visitor = visitor;
	}

	public Logger getLogger() {
		return logger;
	}

	public String getBeanName() {
		return beanName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public Properties getProperties() {
		return properties;
	}

	public Properties getUnresolvedProperties() {
		return unresolvedProperties;
	}

	public Properties getSpringProperties() {
		return springProperties;
	}

	public Properties getUnresolvedSpringProperties() {
		return unresolvedSpringProperties;
	}

	public Resource[] getLocations() {
		return locations;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setUnresolvedProperties(Properties unresolvedProperties) {
		this.unresolvedProperties = unresolvedProperties;
	}

	public void setSpringProperties(Properties springProperties) {
		this.springProperties = springProperties;
	}

	public void setUnresolvedSpringProperties(Properties unresolvedSpringProperties) {
		this.unresolvedSpringProperties = unresolvedSpringProperties;
	}

	public PropertiesLogger getPropertiesLogger() {
		return propertiesLogger;
	}

	public void setPropertiesLogger(PropertiesLogger propertiesLogger) {
		this.propertiesLogger = propertiesLogger;
	}

}
