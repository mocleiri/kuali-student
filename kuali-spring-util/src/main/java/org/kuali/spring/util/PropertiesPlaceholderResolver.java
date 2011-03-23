package org.kuali.spring.util;

import java.util.Properties;

import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

/**
 * 
 */
public class PropertiesPlaceholderResolver implements PlaceholderResolver {

	Properties properties;

	public PropertiesPlaceholderResolver() {
		this(null);
	}

	public PropertiesPlaceholderResolver(Properties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public String resolvePlaceholder(String placeholderName) {
		return properties.getProperty(placeholderName);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
