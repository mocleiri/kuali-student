package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;

public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);
	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
	Properties rawProperties;
	Properties resolvedProperties;

	@Override
	public void convertProperties(Properties properties) {
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to convert");
			return;
		}

		logger.info("Resolving placeholders in properties");

		// Clone the original properties
		rawProperties = clone(properties);

		resolvedProperties = getResolvedProperties(properties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(rawProperties, "*** Raw Spring Properties ***"));
			logger.debug(loggerSupport.getLogEntry(resolvedProperties, "*** Resolved Spring Properties ***"));
		}

		logger.debug("*** Merging original properties with resolved properties ***");
		mergeProperties(properties, resolvedProperties);

		if (logger.isInfoEnabled()) {
			logger.info(loggerSupport.getLogEntry(properties, "*** Spring Properties ***"));
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
			logger.debug("Removing " + key);
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
			logger.debug("Adding " + necessaryKey + "=" + necessaryValue);
			currentProperties.setProperty(necessaryKey, necessaryValue);
		}
	}

	protected void mergeProperties(Properties originalProperties, Properties resolvedProperties) {
		removeProperties(originalProperties, resolvedProperties.stringPropertyNames());
		addProperties(originalProperties, resolvedProperties);
		updateOriginalProperties(originalProperties, resolvedProperties);
	}

	protected void updateOriginalProperties(Properties originalProperties, Properties resolvedProperties) {
		List<String> originalNames = new ArrayList<String>(originalProperties.stringPropertyNames());
		Collections.sort(originalNames);
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
			logger.debug("Update " + commonKey + " ["
					+ loggerSupport.getPropertyValue(commonKey, originalPropertyValue) + "]->["
					+ loggerSupport.getPropertyValue(commonKey, resolvedPropertyValue) + "]");
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

	public PropertiesLoggerSupport getLoggerSupport() {
		return loggerSupport;
	}

	public void setLoggerSupport(PropertiesLoggerSupport propertiesLogger) {
		this.loggerSupport = propertiesLogger;
	}

}
