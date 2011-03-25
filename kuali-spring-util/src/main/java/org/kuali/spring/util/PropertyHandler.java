package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.kuali.spring.util.event.DefaultBeanVisitListener;
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
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;

public class PropertyHandler extends PropertyResourceConfigurer implements BeanNameAware, BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);
	public static final boolean DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT = true;
	public static final boolean DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;
	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";
	public static final SystemPropertiesMode DEFAULT_SYSTEM_PROPERTIES_MODE = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;

	String beanName;
	BeanFactory beanFactory;
	Properties properties;
	Properties unresolvedSpringProperties;
	Properties unresolvedProperties;
	Properties springProperties;
	Resource[] locations;

	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;
	boolean searchSystemEnvironment = DEFAULT_IS_SEARCH_SYSTEM_ENVIRONMENT;
	SystemPropertiesMode systemPropertiesMode = DEFAULT_SYSTEM_PROPERTIES_MODE;

	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	PropertiesHelper helper = new PropertiesHelper(loggerSupport);
	PropertiesLoader loader = new PropertiesLoader(loggerSupport, helper);
	PlaceholderReplacer replacer = new PlaceholderReplacer(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
			PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null, DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
	PropertiesRetriever retriever = new PropertiesRetriever();
	StringValueResolver resolver = new DefaultStringValueResolver(replacer, retriever, null);
	BeanDefinitionVisitor visitor = new EnhancedBeanDefinitionVisitor(resolver);

	protected void autoWire() {
		if (helper.getLoggerSupport() == null) {
			helper.setLoggerSupport(getLoggerSupport());
			logger.debug("Auto-wiring helper with loggerSupport");
		}
		if (loader.getLoggerSupport() == null) {
			loader.setLoggerSupport(getLoggerSupport());
			logger.debug("Auto-wiring loader with loggerSupport");
		}
		if (loader.getHelper() == null) {
			loader.setHelper(getHelper());
			logger.debug("Auto-wiring loader with helper");
		}
		if (resolver instanceof DefaultStringValueResolver) {
			DefaultStringValueResolver defaultResolver = (DefaultStringValueResolver) resolver;
			if (defaultResolver.getReplacer() == null) {
				defaultResolver.setReplacer(getReplacer());
				logger.debug("Auto-wiring resolver with replacer");
			}
			if (defaultResolver.getRetriever() == null) {
				defaultResolver.setRetriever(getRetriever());
				logger.debug("Auto-wiring resolver with retriever");
			}
		}
		if (visitor instanceof EnhancedBeanDefinitionVisitor) {
			EnhancedBeanDefinitionVisitor enhancedVisitor = (EnhancedBeanDefinitionVisitor) visitor;
			if (enhancedVisitor.getValueResolver() == null) {
				enhancedVisitor.setValueResolver(getResolver());
				logger.debug("Auto-wiring visitor with resolver");
			}
			if (enhancedVisitor.getListeners().size() == 0) {
				enhancedVisitor.addListener(new DefaultBeanVisitListener());
			}
		}
	}

	protected void validate() {
		Assert.notNull(getLoggerSupport());
		Assert.notNull(getHelper());
		Assert.notNull(getLoader());
		Assert.notNull(getReplacer());
		Assert.notNull(getRetriever());
		Assert.notNull(getResolver());
		Assert.notNull(getVisitor());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		validate();
		autoWire();
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
			logger.info("No properties to convert");
			return;
		}

		// Clone the original properties
		unresolvedProperties = helper.getClone(properties);

		// Properties after all placeholders have been resolved
		Properties resolvedProperties = getResolvedProperties(properties);
		springProperties = getResolvedProperties(unresolvedSpringProperties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(unresolvedProperties, "*** Unresolved Properties ***"));
		}

		// Synchronize the properties passed in with our resolved properties
		helper.syncProperties(properties, resolvedProperties);

		if (logger.isInfoEnabled()) {
			logger.info(loggerSupport.getLogEntry(springProperties, "*** Spring Properties ***"));
			logger.info(loggerSupport.getLogEntry(properties, "*** All Properties ***"));
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
		// Give the retriever a handle to the properties
		retriever.setProperties(properties);
		// Preserve just the Spring properties
		setUnresolvedSpringProperties(helper.getClone(properties));
		// Merge in the system properties as appropriate
		helper.mergeSystemProperties(properties, getSystemPropertiesMode());
		// Merge in environment properties as appropriate
		if (isSearchSystemEnvironment()) {
			helper.mergeEnvironmentProperties(properties, getEnvironmentPropertyPrefix());
		}
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

	public String getBeanName() {
		return beanName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
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

	public Properties getProperties() {
		return properties;
	}

	public Properties getUnresolvedSpringProperties() {
		return unresolvedSpringProperties;
	}

	public Resource[] getLocations() {
		return locations;
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

	public StringValueResolver getResolver() {
		return resolver;
	}

	public void setResolver(StringValueResolver stringResolver) {
		this.resolver = stringResolver;
	}

	public void setUnresolvedSpringProperties(Properties springProperties) {
		this.unresolvedSpringProperties = springProperties;
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

	public void setProperties(Properties managedProperties) {
		this.properties = managedProperties;
	}

	public Properties getSpringProperties() {
		return springProperties;
	}

	public void setSpringProperties(Properties springProperties) {
		this.springProperties = springProperties;
	}

	public BeanDefinitionVisitor getVisitor() {
		return visitor;
	}

	public void setVisitor(BeanDefinitionVisitor beanDefinitionVisitor) {
		this.visitor = beanDefinitionVisitor;
	}

}
