package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);
	PropertiesLogger propertiesLogger;
	Properties properties;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			logger.info("Processing properties");

			properties = mergeProperties();

			// Convert the merged properties, if necessary.
			convertProperties(properties);

			if (logger.isInfoEnabled()) {
				propertiesLogger.logProperties(properties);
			}

			// Let the subclass process the properties.
			processProperties(beanFactory, properties);

		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}

	public PropertiesLogger getPropertiesLogger() {
		return propertiesLogger;
	}

	public void setPropertiesLogger(PropertiesLogger propertiesLogger) {
		this.propertiesLogger = propertiesLogger;
	}

	public Properties getProperties() {
		return properties;
	}

}
