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
	private final Logger logger = LoggerFactory.getLogger(ResolvePropertiesFirstPlaceholderConfigurer.class);
	Properties rawProperties;
	Properties resolvedProperties;


	public Properties getRawProperties() {
		return rawProperties;
	}

	public Properties getResolvedProperties() {
		return resolvedProperties;
	}

}
