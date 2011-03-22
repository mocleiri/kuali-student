package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * This class uses the convertProperties() hook provided by Spring to resolve placeholders in Spring properties before
 * resolving placeholders in Spring beans. This allows you to do something useful with the complete set of resolved
 * Spring properties known to this configurer. (eg logging them, debugging them etc)
 */
public class ResolvePropertiesFirstPlaceholderConfigurer extends ConfigurablePropertyPlaceholderConfigurer {
	private final Logger logger = LoggerFactory.getLogger(ResolvePropertiesFirstPlaceholderConfigurer.class);
	Properties rawProperties;
	Properties resolvedProperties;

	@Override
	public void convertProperties(Properties properties) {
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to convert");
			return;
		}

		// Clone the original properties
		rawProperties = getClone(properties);

		// Properties after all placeholders have been resolved
		resolvedProperties = getResolvedProperties(properties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(rawProperties, "*** Raw Spring Properties ***"));
			logger.debug(loggerSupport.getLogEntry(resolvedProperties, "*** Resolved Spring Properties ***"));
		}

		// Update the original properties with our resolved properties
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
			logger.trace("Adding property {}=[{}]", necessaryKey, necessaryValue);
			properties.setProperty(necessaryKey, necessaryValue);
		}
	}

	protected void mergeProperties(Properties originalProperties, Properties resolvedProperties) {
		logger.trace("*** Merging original properties with resolved properties ***");
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

			// Update the old property value with the new property value
			logger.trace("Updating property '" + commonKey + "' [{}]->[{}]",
					loggerSupport.getPropertyValue(commonKey, oldPropertyValue),
					loggerSupport.getPropertyValue(commonKey, newPropertyValue));
			oldProperties.setProperty(commonKey, newPropertyValue);
		}
	}

	protected Properties getResolvedProperties(Properties properties) {
		logger.info("Resolving placeholders in properties");
		Properties resolvedProperties = new Properties();
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			resolveProperty(key, properties, resolvedProperties);
		}
		return resolvedProperties;

	}

	protected void resolveProperty(String key, Properties originalProperties, Properties resolvedProperties) {
		// First resolve any placeholders in the key itself
		logger.trace("Resolving placeholders in key '{}'", key);
		String resolvedKey = helper.replacePlaceholders(key, helper);
		if (!key.equals(resolvedKey)) {
			logger.trace("Resolved key [{}]->[{}]", key, resolvedKey);
		}
		// Extract a property value for the key from the original properties
		String rawValue = helper.resolvePlaceholder(key);
		logger.trace("Unresolved value for '{}' is [{}]", key, rawValue);
		logger.trace("Resolving placeholders in value [{}]", rawValue);
		// Now resolve any placeholders in the property value
		String resolvedValue = helper.replacePlaceholders(rawValue, helper);
		if (!rawValue.equals(resolvedValue)) {
			logger.trace("Resolved value [{}]->[{}]", rawValue, resolvedValue);
		}
		// The only items allowed into resolvedProperties are fully resolved keys and values
		logger.trace("Adding to resolved properties {}=[{}]", resolvedKey, resolvedValue);
		resolvedProperties.setProperty(resolvedKey, resolvedValue);
	}

	protected Properties getClone(Properties properties) {
		logger.trace("Cloning original properties");
		Properties clone = new Properties();
		for (String propertyName : properties.stringPropertyNames()) {
			String propertyValue = properties.getProperty(propertyName);
			clone.setProperty(propertyName, propertyValue);
		}
		return clone;
	}

	public Properties getRawProperties() {
		return rawProperties;
	}

	public Properties getResolvedProperties() {
		return resolvedProperties;
	}

}
