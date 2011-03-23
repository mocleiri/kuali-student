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

public class ConfigurablePropertyPlaceholderConfigurer extends PropertyResourceConfigurer implements BeanNameAware,
		BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(ConfigurablePropertyPlaceholderConfigurer.class);
	public static final String DEFAULT_ENVIRONMENT_PROPERTY_PREFIX = "env.";

	String environmentPropertyPrefix = DEFAULT_ENVIRONMENT_PROPERTY_PREFIX;
	String beanName;
	BeanFactory beanFactory;
	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper();
	PropertiesHelper propertiesHelper = new PropertiesHelper();
	ConfigurableBeanDefinitionVisitor beanDefinitionVisitor = new ConfigurableBeanDefinitionVisitor();
	Properties properties;
	Properties originalProperties;
	Resource[] locations;

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

	@Override

	/**
	 * Load properties into the given instance.
	 * @param props the Properties instance to load into
	 * @throws java.io.IOException in case of I/O errors
	 * @see #setLocations
	 */
	protected void loadProperties(Properties props) throws IOException {
		this.propertiesHelper.loadProperties(props, this.locations);
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties properties = super.mergeProperties();
		this.originalProperties = propertiesHelper.getClone(properties);
		propertiesHelper.mergeSystemProperties(properties, this.placeholderHelper.getSystemPropertiesMode());
		if (placeholderHelper.isSearchSystemEnvironment()) {
			propertiesHelper.mergeEnvironmentProperties(properties);
		}
		setProperties(properties);
		this.placeholderHelper.setProperties(properties);
		return properties;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		logger.info("Resolving placeholders in bean definitions");

		// TODO Refactor how these beans collaborate so we don't have to do this
		if (beanDefinitionVisitor.getStringValueResolver() == null) {
			beanDefinitionVisitor.setStringValueResolver(placeholderHelper);
		}

		// Process placeholders in the bean definitions
		processBeanDefinitions(beanFactory);

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactory.resolveAliases(placeholderHelper);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactory.addEmbeddedValueResolver(placeholderHelper);
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

	public PropertyPlaceholderHelper getPlaceholderHelper() {
		return placeholderHelper;
	}

	public void setPlaceholderHelper(PropertyPlaceholderHelper helper) {
		this.placeholderHelper = helper;
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

	public String getEnvironmentPropertyPrefix() {
		return environmentPropertyPrefix;
	}

	public void setEnvironmentPropertyPrefix(String environmentPropertyPrefix) {
		this.environmentPropertyPrefix = environmentPropertyPrefix;
	}

	public Properties getOriginalProperties() {
		return originalProperties;
	}

}
