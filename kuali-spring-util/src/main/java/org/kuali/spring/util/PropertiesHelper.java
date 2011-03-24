package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class PropertiesHelper {
	final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

	PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();

	/**
	 * Remove any keys from properties that are not in approvedKeys
	 * 
	 * @param properties
	 * @param approvedKeys
	 */
	public void removeKeys(Properties properties, Set<String> approvedKeys) {
		// Extract the set of existing property names
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			// Don't do anything, this property is approved
			if (approvedKeys.contains(key)) {
				continue;
			}
			logger.trace("Removing key '{}'", key);
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
	public void addProperties(Properties properties, Properties necessaryProperties) {
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
			logger.trace("Adding property {}=[{}]", necessaryKey, necessaryValue);
			properties.setProperty(necessaryKey, necessaryValue);
		}
	}

	/**
	 * Make oldProperties the same as newProperties. Remove any properties from old that are not in new. Add any
	 * properties from new that are not in old. Update property values in old to the values from new
	 * 
	 * @param oldProperties
	 * @param newProperties
	 */
	public void syncProperties(Properties oldProperties, Properties newProperties) {
		// Remove any keys from oldProperties that are not in newProperties
		removeKeys(oldProperties, newProperties.stringPropertyNames());
		// Add properties from newProperties to oldProperties
		addProperties(oldProperties, newProperties);
		// Set property values in oldProperties to the values from newProperties
		syncValues(oldProperties, newProperties);
	}

	public void syncValues(Properties oldProperties, Properties newProperties) {
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

			// Update the old property value with the new property value
			logger.trace("Updating property '" + commonKey + "' [{}]->[{}]",
					loggerSupport.getPropertyValue(commonKey, oldPropertyValue),
					loggerSupport.getPropertyValue(commonKey, newPropertyValue));
			oldProperties.setProperty(commonKey, newPropertyValue);
		}
	}

	/**
	 * Create a clone of the properties passed in
	 * 
	 * @param properties
	 * @return
	 */
	public Properties getClone(Properties properties) {
		Properties clone = new Properties();
		for (String propertyName : properties.stringPropertyNames()) {
			String propertyValue = properties.getProperty(propertyName);
			clone.setProperty(propertyName, propertyValue);
		}
		return clone;
	}

	public Properties getEnvironmentAsProperties(String prefix) {
		Map<String, String> environmentMap = SystemUtils.getEnvironmentIgnoreExceptions();
		Properties envProps = new Properties();
		for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
			envProps.setProperty(prefix + entry.getKey(), entry.getValue());
		}
		return envProps;
	}

	public void mergeEnvironmentProperties(Properties currentProps, String prefix) {
		logger.info("Merging environment properties");
		String source = PropertiesSource.ENVIRONMENT.toString();
		mergeProperties(currentProps, getEnvironmentAsProperties(prefix), true, source);
	}

	/**
	 * Merge the property under 'key' from newProps into currentProps. If override is false and the property already
	 * exists do not update currentProps
	 * 
	 * @param currentProps
	 * @param newProps
	 * @param key
	 * @param override
	 * @param src
	 */
	public void mergeProperty(Properties currentProps, Properties newProps, String key, boolean override, String src) {
		// Extract the new value
		String newValue = newProps.getProperty(key);

		// Ignore values that are null
		if (newValue == null) {
			return;
		}

		// Extract the existing property
		String currentValue = currentProps.getProperty(key);

		// There is no existing property
		if (currentValue == null) {
			logger.debug("Adding " + src + " property {}=[{}]", key, newValue);
			currentProps.setProperty(key, newValue);
			return;
		}

		// Values are the same, nothing to do
		if (ObjectUtils.nullSafeEquals(newValue, currentValue)) {
			return;
		}

		// There is an existing property, but the new property wins
		if (override) {
			logger.info(src + " property override for '" + key + "' [{}]->[{}]", currentValue, newValue);
			currentProps.setProperty(key, newValue);
		}
	}

	public void mergeProperties(Properties currentProps, Properties newProps, boolean override, String source) {
		for (String key : newProps.stringPropertyNames()) {
			mergeProperty(currentProps, newProps, key, override, source);
		}
	}

	public void mergeSystemProperties(Properties currentProps, SystemPropertiesMode mode) {
		if (mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER)) {
			logger.info("Ignoring system properties because mode is {}", mode);
			// Nothing to do
			return;
		}

		// Merge in the system properties
		logger.info("Merging system properties with Spring properties using mode {}", mode);
		boolean override = mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		Properties systemProperties = SystemUtils.getSystemPropertiesIgnoreExceptions();
		mergeProperties(currentProps, systemProperties, override, PropertiesSource.SYSTEM.toString());
	}
}
