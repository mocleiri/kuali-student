package org.kuali.spring.util;

import java.util.Properties;

/**
 * 
 */
public class SimplePropertyRetriever implements PropertyRetriever {

	Properties properties;

	public SimplePropertyRetriever() {
		this(null);
	}

	public SimplePropertyRetriever(Properties properties) {
		super();
		setProperties(properties);
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
