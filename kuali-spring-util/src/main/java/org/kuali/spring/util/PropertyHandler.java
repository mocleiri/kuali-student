package org.kuali.spring.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.Resource;

/**
 * This class is similar to PropertyPlaceholderConfigurer from Spring. It is used to update bean properties with values
 * from properties files. It has all of the features from the Spring configurer, fixes a few bugs, adds a few new
 * features and is much more pluggable
 */
public class PropertyHandler implements BeanNameAware, BeanFactoryAware, BeanFactoryPostProcessor, PriorityOrdered {
	final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);

	private int order = Ordered.LOWEST_PRECEDENCE; // default: same as non-Ordered

	private String beanName;
	private BeanFactory beanFactory;
	private PropertiesLogger propertiesLogger = new DefaultPropertiesLogger();
	private PropertiesLoader loader = new DefaultPropertiesLoader();
	Resource[] locations;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Properties properties = loader.loadProperties();
		} catch (Exception e) {
			throw new BeanInitializationException("Could not load properties", e);
		}
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public PropertiesLogger getPropertiesLogger() {
		return propertiesLogger;
	}

	public void setPropertiesLogger(PropertiesLogger propertiesLogger) {
		this.propertiesLogger = propertiesLogger;
	}

	public PropertiesLoader getLoader() {
		return loader;
	}

	public void setLoader(PropertiesLoader loader) {
		this.loader = loader;
	}

	public Resource[] getLocations() {
		return locations;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

}
