package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

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

public class MyPropertyPlaceholderConfigurer extends PropertyResourceConfigurer implements BeanNameAware,
		BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);

	String beanName;
	BeanFactory beanFactory;
	Properties properties;
	String nullValue;
	boolean searchSystemEnvironment;
	boolean ignoreResourceNotFound;
	String fileEncoding;
	SystemPropertiesMode systemPropertiesMode;
	String placeholderPrefix;
	String placeholderSuffix;
	String valueSeparator;
	boolean ignoreUnresolvablePlaceholders;

	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	PropertiesHelper propertiesHelper = new PropertiesHelper(ignoreResourceNotFound, fileEncoding);
	PlaceholderReplacer replacer = new PlaceholderReplacer(placeholderPrefix, placeholderSuffix, valueSeparator,
			ignoreUnresolvablePlaceholders);
	PropertyResolver propertyResolver = new SimplePropertyResolver(properties);
	StringValueResolver stringResolver = new DefaultStringValueResolver(replacer, propertyResolver, nullValue);
	ConfigurableBeanDefinitionVisitor beanDefinitionVisitor = new ConfigurableBeanDefinitionVisitor(stringResolver);
	Properties springProperties;
	Resource[] resourceLocations;

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
				logger.info("Skipping placeholder resolution for " + bd);
				continue;
			}
			processBeanDefinition(currentBean, bd);
		}
	}

	/**
	 * Override the default resource loading logic to clean it up and add fine grained logging. Anytime a property value
	 * is overridden an INFO level logging message is produced. If TRACE is turned on, all properties are logged in the
	 * order they are loaded
	 */
	@Override
	protected void loadProperties(Properties properties) throws IOException {
		this.propertiesHelper.loadProperties(properties, getResourceLocations());
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		// The super class loads properties from resources as well as properties defined directly on this bean
		Properties properties = super.mergeProperties();
		// Preserve just the Spring properties
		setSpringProperties(propertiesHelper.getClone(properties));
		// Merge in the system properties as appropriate
		propertiesHelper.mergeSystemProperties(properties, getSystemPropertiesMode());
		// Merge in environment properties as appropriate
		if (isSearchSystemEnvironment()) {
			propertiesHelper.mergeEnvironmentProperties(properties);
		}
		// Store the complete set of properties that will be used during placeholder replacement
		setProperties(properties);
		// return the complete set of properties
		return properties;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		logger.info("Resolving placeholders in bean definitions");

		// TODO Refactor how these beans collaborate so we don't have to do this
		if (beanDefinitionVisitor.getStringValueResolver() == null) {
			beanDefinitionVisitor.setStringValueResolver(stringResolver);
		}

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

	public ConfigurableBeanDefinitionVisitor getBeanDefinitionVisitor() {
		return beanDefinitionVisitor;
	}

	public void setBeanDefinitionVisitor(ConfigurableBeanDefinitionVisitor beanDefinitionVisitor) {
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

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getSpringProperties() {
		return springProperties;
	}

	public Resource[] getResourceLocations() {
		return resourceLocations;
	}

	public void setResourceLocations(Resource[] resourceLocations) {
		this.resourceLocations = resourceLocations;
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

	public boolean isIgnoreResourceNotFound() {
		return ignoreResourceNotFound;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public String getFileEncoding() {
		return fileEncoding;
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

	public PropertiesHelper getPropertiesHelper() {
		return propertiesHelper;
	}

	public void setPropertiesHelper(PropertiesHelper propertiesHelper) {
		this.propertiesHelper = propertiesHelper;
	}

	public PlaceholderReplacer getReplacer() {
		return replacer;
	}

	public void setReplacer(PlaceholderReplacer replacer) {
		this.replacer = replacer;
	}

	public PropertyResolver getPropertyResolver() {
		return propertyResolver;
	}

	public void setPropertyResolver(PropertyResolver propertyResolver) {
		this.propertyResolver = propertyResolver;
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

}
