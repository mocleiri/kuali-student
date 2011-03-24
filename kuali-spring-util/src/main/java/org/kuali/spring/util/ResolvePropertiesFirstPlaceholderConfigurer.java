package org.kuali.spring.util;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class uses the convertProperties() hook provided by Spring to resolve placeholders in Spring properties before
 * resolving placeholders in Spring beans. This allows you to do something useful with the complete set of resolved
 * Spring properties known to this configurer. (eg logging them, debugging them etc)
 */
public class ResolvePropertiesFirstPlaceholderConfigurer extends MyPropertyPlaceholderConfigurer {
	private final Logger logger = LoggerFactory.getLogger(ResolvePropertiesFirstPlaceholderConfigurerTest.class);
	Properties rawProperties;
	Properties resolvedProperties;

	@Override
	public void convertProperties(Properties properties) {
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to convert");
			return;
		}

		// Clone the original properties
		rawProperties = propertiesHelper.getClone(properties);

		// Properties after all placeholders have been resolved
		resolvedProperties = getResolvedProperties(properties);

		if (logger.isDebugEnabled()) {
			logger.debug(loggerSupport.getLogEntry(rawProperties, "*** Raw Spring Properties ***"));
			logger.debug(loggerSupport.getLogEntry(resolvedProperties, "*** Resolved Spring Properties ***"));
		}

		// Update the original properties with our resolved properties
		propertiesHelper.mergeProperties(properties, resolvedProperties);

		placeholderHelper.setResolvedCache(resolvedProperties);

		if (logger.isInfoEnabled()) {
			logger.info(loggerSupport.getLogEntry(properties, "*** Spring Properties ***"));
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
		String resolvedKey = placeholderHelper.replacePlaceholders(key, originalProperties);
		if (!key.equals(resolvedKey)) {
			logger.trace("Resolved key [{}]->[{}]", key, resolvedKey);
		}
		// Get a value for the key
		String rawValue = placeholderHelper.resolvePlaceholder(key);
		logger.trace("Raw value for '{}' is [{}]", key, rawValue);
		logger.trace("Resolving placeholders in value [{}]", rawValue);
		// Now resolve any placeholders in the value
		String resolvedValue = placeholderHelper.replacePlaceholders(rawValue, originalProperties);
		if (!rawValue.equals(resolvedValue)) {
			logger.trace("Resolved value [{}]->[{}]", rawValue, resolvedValue);
		}
		// The only items allowed into resolvedProperties are fully resolved keys and values
		logger.trace("Adding to resolved properties {}=[{}]", resolvedKey, resolvedValue);
		resolvedProperties.setProperty(resolvedKey, resolvedValue);
	}

	public Properties getRawProperties() {
		return rawProperties;
	}

	public Properties getResolvedProperties() {
		return resolvedProperties;
	}

}
