package org.kuali.spring.util;

import java.util.Properties;

public interface PropertiesResolver {
	public Properties resolvePlaceholders(Properties properties);
}
