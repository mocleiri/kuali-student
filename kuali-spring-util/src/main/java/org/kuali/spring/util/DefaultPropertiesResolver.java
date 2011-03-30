package org.kuali.spring.util;

import java.util.Properties;

public class DefaultPropertiesResolver implements PropertiesResolver {

	@Override
	public Properties resolvePlaceholders(Properties properties) {
		Properties resolvedProperties = new Properties();
		return resolvedProperties;
	}

}
