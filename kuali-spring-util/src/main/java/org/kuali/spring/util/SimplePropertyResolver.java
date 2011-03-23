package org.kuali.spring.util;

import java.util.Properties;

/**
 * 
 */
public class SimplePropertyResolver implements PropertyResolver {

	Properties properties;

	public SimplePropertyResolver() {
		this(null);
	}

	public SimplePropertyResolver(Properties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
