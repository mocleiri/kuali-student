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
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * This class takes advantage of the convertProperties() hook provided by Spring to resolve placeholders in Spring
 * properties before proceeding with resolving placeholders in Spring bean references. This allows you to do something
 * useful (eg logging them, debugging them etc) with the complete set of Spring properties known to this configurer
 */
public class ResolvePropertiesFirstPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private final Logger logger = LoggerFactory.getLogger(ResolvePropertiesFirstPlaceholderConfigurer.class);
	PropertiesLoggerSupport loggerSupport;
	PropertyPlaceholderHelper helper;

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
		rawProperties = getClone(properties);

		resolvedProperties = getResolvedProperties(properties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(rawProperties, "*** Raw Spring Properties ***"));
			logger.debug(loggerSupport.getLogEntry(resolvedProperties, "*** Resolved Spring Properties ***"));
		}

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
	 * Add any missing properties from necessaryProperties
	 * 
	 * @param properties
	 * @param necessaryProperties
	 */
	protected void addProperties(Properties properties, Properties necessaryProperties) {
		// Extract the set of necessary property names
		Set<String> necessaryKeys = necessaryProperties.stringPropertyNames();
		// Extract the set of existing property names
		Set<String> keys = properties.stringPropertyNames();
		for (String necessaryKey : necessaryKeys) {
			// Don't do anything, the property is already present
			if (keys.contains(necessaryKey)) {
				continue;
			}

			// Add the missing property
			String necessaryValue = necessaryProperties.getProperty(necessaryKey);
			logger.debug("Adding " + necessaryKey + "=" + necessaryValue);
			properties.setProperty(necessaryKey, necessaryValue);
		}
	}

	protected void mergeProperties(Properties originalProperties, Properties resolvedProperties) {
		logger.debug("*** Merging original properties with resolved properties ***");
		removeProperties(originalProperties, resolvedProperties.stringPropertyNames());
		addProperties(originalProperties, resolvedProperties);
		updateProperties(originalProperties, resolvedProperties);
	}

	protected void updateProperties(Properties oldProperties, Properties newProperties) {
		List<String> oldNames = new ArrayList<String>(oldProperties.stringPropertyNames());
		Collections.sort(oldNames);
		Iterator<String> itr = oldNames.iterator();
		while (itr.hasNext()) {
			String commonKey = itr.next();
			String oldPropertyValue = oldProperties.getProperty(commonKey);
			String newPropertyValue = newProperties.getProperty(commonKey);
			// The values are the same. Don't do anything
			if (ObjectUtils.nullSafeEquals(oldPropertyValue, newPropertyValue)) {
				continue;
			}

			// Update the original property value with the resolved property value
			logger.debug("Update " + commonKey + "='" + loggerSupport.getPropertyValue(commonKey, newPropertyValue)
					+ "' was [" + loggerSupport.getPropertyValue(commonKey, oldPropertyValue) + "]");
			oldProperties.setProperty(commonKey, newPropertyValue);
		}
	}

	protected Properties getResolvedProperties(Properties properties) {
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

	protected Properties getClone(Properties properties) {
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

	public Properties getRawProperties() {
		return rawProperties;
	}

	public Properties getResolvedProperties() {
		return resolvedProperties;
	}

	public PropertyPlaceholderHelper getHelper() {
		return helper;
	}

	public void setHelper(PropertyPlaceholderHelper helper) {
		this.helper = helper;
	}

}
