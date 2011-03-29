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

	PropertyLogger plogger = new DefaultPropertyLogger();

	/**
	 * Remove keys from properties that are not in approvedKeys
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
	 * Add missing properties from necessaryProperties
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
			logger.trace("Adding property {}=[{}]", necessaryKey,
					plogger.getPropertyValue(necessaryKey, necessaryValue));
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
					plogger.getPropertyValue(commonKey, oldPropertyValue),
					plogger.getPropertyValue(commonKey, newPropertyValue));
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

	public Properties getEnvironmentProperties(String prefix) {
		Map<String, String> environmentMap = SystemUtils.getEnvironmentIgnoreExceptions();
		Properties envProps = new Properties();
		for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
			String key = (prefix == null) ? entry.getKey() : prefix + entry.getKey();
			envProps.setProperty(key, entry.getValue());
		}
		return envProps;
	}

	public void mergeEnvironmentProperties(Properties currentProps, String prefix) {
		logger.info("Merging environment properties");
		PropertySource source = PropertySource.ENVIRONMENT;
		Properties envProps = getEnvironmentProperties(prefix);
		PropertiesMergeContext context = new PropertiesMergeContext(currentProps, envProps, true, source, true);
		mergeProperties(context);
	}

	/**
	 * Merge the property under 'key' from newProps into currentProps using the settings from PropertiesMergeContext
	 * 
	 * @param context
	 * @param key
	 */
	public void mergeProperty(PropertiesMergeContext context, String key) {
		Properties newProps = context.getNewProperties();
		Properties currentProps = context.getCurrentProperties();
		PropertySource source = context.getSource();
		boolean override = context.isOverride();
		// Extract the new value
		String newValue = newProps.getProperty(key);

		// If the new value is null, there is nothing further to do
		if (newValue == null) {
			return;
		}

		// Extract the existing value
		String currentValue = currentProps.getProperty(key);

		// There is no existing value for this key
		if (currentValue == null) {
			logger.debug("Adding " + source + " property {}=[{}]", key, plogger.getPropertyValue(key, newValue));
			currentProps.setProperty(key, newValue);
			return;
		}

		// Neither value is null, but they are the same, nothing further to do
		if (ObjectUtils.nullSafeEquals(newValue, currentValue)) {
			return;
		}

		if (override) {
			// There is an existing property, but the new property wins
			logger.info(source + " property override for '" + key + "' [{}]->[{}]",
					plogger.getPropertyValue(key, currentValue), plogger.getPropertyValue(key, newValue));
			currentProps.setProperty(key, newValue);
		} else {
			// There is already an existing property, and the existing property wins
			logger.debug("The existing value for '" + key + "' is not being overridden by the " + source
					+ " value. Existing:[{}] New:[{}]", plogger.getPropertyValue(key, currentValue),
					plogger.getPropertyValue(key, newValue));
		}
	}

	public void mergeProperties(PropertiesMergeContext context) {
		List<String> keys = new ArrayList<String>(context.getNewProperties().stringPropertyNames());
		if (context.isSort()) {
			Collections.sort(keys);
		}
		for (String key : keys) {
			mergeProperty(context, key);
		}
	}

	public void mergeSystemProperties(Properties currentProps, Properties systemProperties, SystemPropertiesMode mode) {
		if (mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER)) {
			logger.info("Ignoring system properties because mode is {}", mode);
			// Nothing to do
			return;
		}

		// Merge in the system properties
		logger.info("Merging system properties using {}", mode);
		boolean override = mode.equals(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		PropertySource source = PropertySource.SYSTEM;
		PropertiesMergeContext context = new PropertiesMergeContext(currentProps, systemProperties, override, source);
		mergeProperties(context);
	}

	public void mergeSystemProperties(Properties currentProps, SystemPropertiesMode mode) {
		Properties systemProperties = SystemUtils.getSystemPropertiesIgnoreExceptions();
		mergeSystemProperties(currentProps, systemProperties, mode);
	}

	public PropertyLogger getPlogger() {
		return plogger;
	}

	public void setPlogger(PropertyLogger propertiesLogger) {
		this.plogger = propertiesLogger;
	}

}
