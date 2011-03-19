package org.kuali.spring.util;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;

public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);
	PropertiesLogger propertiesLogger;
	Properties rawProperties;
	Properties resolvedProperties;

	@Override
	public void convertProperties(Properties properties) {
		logger.info("Converting properties");

		// Clone the original properties
		rawProperties = clone(properties);

		resolvedProperties = getResolvedProperties(properties);

		if (logger.isInfoEnabled()) {
			propertiesLogger.logProperties(rawProperties, "*** Raw Spring Properties ***");
			propertiesLogger.logProperties(resolvedProperties, "*** Resolved Spring Properties ***");
		}

		logger.info("*** Merging original properties with resolved properties ***");
		mergeProperties(properties, resolvedProperties);

		if (logger.isInfoEnabled()) {
			propertiesLogger.logProperties(properties, "*** Final Spring Properties ***");
		}
	}

	/**
	 * Remove any properties not in approvedKeys
	 * 
	 * @param properties
	 * @param approvedKeys
	 */
	protected void removeProperties(Properties properties, Set<String> approvedKeys) {
		// Extract the set of existing property names
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			// Don't do anything, this property is approved
			if (approvedKeys.contains(key)) {
				continue;
			}
			logger.info("Removing " + key);
			// Remove this property as it is not in the approved set
			properties.remove(key);
		}
	}

	/**
	 * Add properties to currentProperties from necessaryProperties
	 * 
	 * @param properties
	 * @param necessaryProperties
	 */
	protected void addProperties(Properties currentProperties, Properties necessaryProperties) {
		Set<String> necessaryKeys = necessaryProperties.stringPropertyNames();
		Set<String> keys = currentProperties.stringPropertyNames();
		for (String necessaryKey : necessaryKeys) {
			if (keys.contains(necessaryKey)) {
				continue;
			}
			String necessaryValue = necessaryProperties.getProperty(necessaryKey);
			logger.info("Adding " + necessaryKey + "=" + necessaryValue);
			currentProperties.setProperty(necessaryKey, necessaryValue);
		}
	}

	protected void mergeProperties(Properties originalProperties, Properties resolvedProperties) {
		removeProperties(originalProperties, resolvedProperties.stringPropertyNames());
		addProperties(originalProperties, resolvedProperties);
		updateOriginalProperties(originalProperties, resolvedProperties);
	}

	protected void updateOriginalProperties(Properties originalProperties, Properties resolvedProperties) {
		Set<String> originalNames = originalProperties.stringPropertyNames();
		Iterator<String> itr = originalNames.iterator();
		while (itr.hasNext()) {
			String commonKey = itr.next();
			String originalPropertyValue = originalProperties.getProperty(commonKey);
			String resolvedPropertyValue = resolvedProperties.getProperty(commonKey);
			// The values are the same. Don't do anything
			if (ObjectUtils.nullSafeEquals(originalPropertyValue, resolvedPropertyValue)) {
				continue;
			}

			// Update the original property value with the resolved property value
			logger.info("Updating " + commonKey + " to ["
					+ propertiesLogger.getLogValue(commonKey, resolvedPropertyValue) + "] was ["
					+ propertiesLogger.getLogValue(commonKey, originalPropertyValue) + "]");
			originalProperties.setProperty(commonKey, resolvedPropertyValue);
		}
	}

	protected Properties getResolvedProperties(Properties properties) {
		PPH3 helper = new PPH3();
		Properties resolvedProperties = new Properties();
		Set<String> names = properties.stringPropertyNames();
		for (String name : names) {
			String property = properties.getProperty(name);
			String resolvedName = helper.replacePlaceholders(name, properties);
			String resolvedProperty = helper.replacePlaceholders(property, properties);
			resolvedProperties.setProperty(resolvedName, resolvedProperty);
		}
		return resolvedProperties;

	}

	protected Properties clone(Properties properties) {
		Properties clone = new Properties();
		for (String propertyName : properties.stringPropertyNames()) {
			String propertyValue = properties.getProperty(propertyName);
			clone.setProperty(propertyName, propertyValue);
		}
		return clone;
	}

	public PropertiesLogger getPropertiesLogger() {
		return propertiesLogger;
	}

	public void setPropertiesLogger(PropertiesLogger propertiesLogger) {
		this.propertiesLogger = propertiesLogger;
	}

}
