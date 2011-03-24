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
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

public class PropertyHandler extends PropertyResourceConfigurer implements BeanNameAware, BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);
	public static final boolean DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT = true;
	public static final boolean DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;
	public static final boolean DEFAULT_IS_IGNORE_RESOURCE_NOT_FOUND = false;
	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";

	String beanName;
	BeanFactory beanFactory;
	Properties managedProperties;
	Properties springProperties;
	Properties unresolvedProperties;
	Properties resolvedProperties;
	Resource[] locations;

	String nullValue;
	String valueSeparator;
	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;
	boolean searchSystemEnvironment = DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT;
	SystemPropertiesMode systemPropertiesMode = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;
	String placeholderPrefix = org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX;
	String placeholderSuffix = org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX;
	boolean ignoreUnresolvablePlaceholders = DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS;

	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	PropertiesHelper helper = new PropertiesHelper();
	PropertiesLoader loader = new PropertiesLoader();
	PlaceholderReplacer replacer = new PlaceholderReplacer(placeholderPrefix, placeholderSuffix, valueSeparator,
			ignoreUnresolvablePlaceholders);
	PropertiesRetriever retriever = new PropertiesRetriever();
	StringValueResolver stringResolver = new DefaultStringValueResolver(replacer, retriever, nullValue);
	NotifyingBeanDefinitionVisitor beanDefinitionVisitor = new NotifyingBeanDefinitionVisitor(stringResolver);

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

	@Override
	public void convertProperties(Properties properties) {
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to convert");
			return;
		}

		// Clone the original properties
		unresolvedProperties = helper.getClone(properties);

		// Properties after all placeholders have been resolved
		resolvedProperties = getResolvedProperties(properties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(unresolvedProperties, "*** Raw Properties ***"));
			logger.debug(loggerSupport.getLogEntry(resolvedProperties, "*** Resolved Properties ***"));
		}

		// Synchronize the properties passed in with our resolved properties
		helper.syncProperties(properties, resolvedProperties);

		if (logger.isInfoEnabled()) {
			logger.info(loggerSupport.getLogEntry(properties, "*** Spring Properties ***"));
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
			logger.info("Resolved key [{}]->[{}]", key, resolvedKey);
		}
		// Get a value for the key
		String rawValue = retriever.retrieveProperty(key);
		logger.trace("Raw value for '{}' is [{}]", key, rawValue);
		logger.trace("Replacing placeholders in value [{}]", rawValue);
		// Now replace any placeholders in the value
		String resolvedValue = replacer.replacePlaceholders(rawValue, originalProperties);
		if (!rawValue.equals(resolvedValue)) {
			logger.info("Resolved value for '" + resolvedKey + "' [{}]->[{}]", rawValue, resolvedValue);
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
			beanDefinitionVisitor.visitBeanDefinition(bd);
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
	 * Override the default resource loading logic to clean it up and add fine grained logging. Anytime a property value
	 * is overridden an INFO level logging message is produced. With TRACE turned on, all properties are logged in the
	 * order they are loaded
	 */
	@Override
	protected void loadProperties(Properties properties) throws IOException {
		this.loader.loadProperties(properties, getLocations());
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		// The super class method loads properties from resources as well as properties defined directly on this bean
		Properties managedProperties = super.mergeProperties();
		// Give the retriever a handle to the properties
		retriever.setProperties(managedProperties);
		// Preserve just the Spring properties
		setSpringProperties(helper.getClone(managedProperties));
		// Merge in the system properties as appropriate
		helper.mergeSystemProperties(managedProperties, getSystemPropertiesMode());
		// Merge in environment properties as appropriate
		if (isSearchSystemEnvironment()) {
			helper.mergeEnvironmentProperties(managedProperties, getEnvironmentPropertyPrefix());
		}
		// Store the complete set of properties that will be used during placeholder replacement
		setManagedProperties(managedProperties);
		// return the complete set of properties
		return managedProperties;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		logger.info("Resolving placeholders in bean definitions");

		// Process placeholders in the bean definitions
		processBeanDefinitions(beanFactory);

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactory.resolveAliases(stringResolver);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactory.addEmbeddedValueResolver(stringResolver);
	}

	public String getBeanName() {
		return beanName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public NotifyingBeanDefinitionVisitor getBeanDefinitionVisitor() {
		return beanDefinitionVisitor;
	}

	public void setBeanDefinitionVisitor(NotifyingBeanDefinitionVisitor beanDefinitionVisitor) {
		this.beanDefinitionVisitor = beanDefinitionVisitor;
	}

	public PropertiesLoggerSupport getLoggerSupport() {
		return loggerSupport;
	}

	public void setLoggerSupport(PropertiesLoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Properties getManagedProperties() {
		return managedProperties;
	}

	public Properties getSpringProperties() {
		return springProperties;
	}

	public Resource[] getLocations() {
		return locations;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
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

	public void setHelper(PropertiesHelper propertiesHelper) {
		this.helper = propertiesHelper;
	}

	public PlaceholderReplacer getReplacer() {
		return replacer;
	}

	public void setReplacer(PlaceholderReplacer replacer) {
		this.replacer = replacer;
	}

	public StringValueResolver getStringResolver() {
		return stringResolver;
	}

	public void setStringResolver(StringValueResolver stringResolver) {
		this.stringResolver = stringResolver;
	}

	public void setSpringProperties(Properties springProperties) {
		this.springProperties = springProperties;
	}

	public String getPlaceholderPrefix() {
		return placeholderPrefix;
	}

	public void setPlaceholderPrefix(String placeholderPrefix) {
		this.placeholderPrefix = placeholderPrefix;
	}

	public String getPlaceholderSuffix() {
		return placeholderSuffix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix) {
		this.placeholderSuffix = placeholderSuffix;
	}

	public String getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}

	public boolean isIgnoreUnresolvablePlaceholders() {
		return ignoreUnresolvablePlaceholders;
	}

	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public PropertiesRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(PropertiesRetriever propertyResolver) {
		this.retriever = propertyResolver;
	}

	public Properties getUnresolvedProperties() {
		return unresolvedProperties;
	}

	public void setUnresolvedProperties(Properties rawProperties) {
		this.unresolvedProperties = rawProperties;
	}

	public Properties getResolvedProperties() {
		return resolvedProperties;
	}

	public void setResolvedProperties(Properties resolvedProperties) {
		this.resolvedProperties = resolvedProperties;
	}

	public String getEnvironmentPropertyPrefix() {
		return environmentPropertyPrefix;
	}

	public void setEnvironmentPropertyPrefix(String environmentPropertyPrefix) {
		this.environmentPropertyPrefix = environmentPropertyPrefix;
	}

	public PropertiesLoader getLoader() {
		return loader;
	}

	public void setLoader(PropertiesLoader loader) {
		this.loader = loader;
	}

	public void setManagedProperties(Properties managedProperties) {
		this.managedProperties = managedProperties;
	}

}
