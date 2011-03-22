package org.kuali.spring.util;

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

public class PropertyPlaceholderConfigurer extends PropertyResourceConfigurer implements BeanNameAware,
		BeanFactoryAware {
	final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);

	String beanName;
	BeanFactory beanFactory;
	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper();
	ConfigurableBeanDefinitionVisitor beanDefinitionVisitor = new ConfigurableBeanDefinitionVisitor();

	protected boolean thisBeanIsMe(String name, ConfigurableListableBeanFactory beanFactory) {
		if (!name.equals(this.beanName)) {
			return false;
		}
		if (!beanFactory.equals(this.beanFactory)) {
			return false;
		}
		return true;
	}

	protected void processBeanDefinition(String beanName, ConfigurableListableBeanFactory beanFactory) {
		// Skip processing our own bean definition
		// Prevent failing on unresolvable placeholders in the locations property
		if (thisBeanIsMe(beanName, beanFactory)) {
			logger.info("Skipping placeholder resolution for " + beanName);
			return;
		}
		BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
		try {
			logger.info("Resolving placeholders for bean '" + beanName + "' [" + bd.getBeanClassName() + "]");
			beanDefinitionVisitor.visitBeanDefinition(bd);
		} catch (Exception ex) {
			throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, ex.getMessage());
		}
	}

	protected void processBeanDefinitions(ConfigurableListableBeanFactory beanFactory) {
		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			processBeanDefinition(beanName, beanFactory);
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		logger.info("Resolving placeholders in bean definitions");

		helper.setProperties(props);
		if (beanDefinitionVisitor.getStringValueResolver() == null) {
			beanDefinitionVisitor.setStringValueResolver(helper);
		}

		// Process placeholders in the bean definitions
		processBeanDefinitions(beanFactory);

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactory.resolveAliases(helper);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactory.addEmbeddedValueResolver(helper);
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

	public PropertyPlaceholderHelper getHelper() {
		return helper;
	}

	public void setHelper(PropertyPlaceholderHelper helper) {
		this.helper = helper;
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

}
