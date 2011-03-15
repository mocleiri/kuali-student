package org.kuali.db.jdbc;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class MyPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceHolderConfigurer.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			logger.info("Hello");
			Properties mergedProps = mergeProperties();

			if (logger.isDebugEnabled()) {
				logger.debug("******************* Merged Props *********************");
				showProperties(mergedProps);
				logger.debug("******************* Merged Props *********************");
			}

			// Convert the merged properties, if necessary.
			convertProperties(mergedProps);

			if (logger.isDebugEnabled()) {
				logger.debug("******************* Converted Props *********************");
				showProperties(mergedProps);
				logger.debug("******************* Converted Props *********************");
			}

			// Let the subclass process the properties.
			processProperties(beanFactory, mergedProps);

			if (logger.isDebugEnabled()) {
				logger.debug("******************* Processed Props *********************");
				showProperties(mergedProps);
				logger.debug("******************* Processed Props *********************");
			}

			logger.info("Goodbye");
		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}

	protected void showProperties(Properties properties) {
		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (Object key : properties.keySet()) {
			String property = properties.getProperty(key + "");
			property = property.replace("\n", "");
			sortedProperties.put(key + "", property);
		}
		for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
			logger.debug(entry.getKey() + "=" + entry.getValue());
		}
	}
}
