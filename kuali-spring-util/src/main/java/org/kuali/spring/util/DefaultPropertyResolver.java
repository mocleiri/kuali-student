package org.kuali.spring.util;

import java.util.Properties;

public class DefaultPropertyResolver implements PropertyResolver {
	public static final SystemPropertiesMode DEFAULT_SYSTEM_PROPERTIES_MODE = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;
	public static final boolean DEFAULT_IS_SEARCH_ENVIRONMENT = true;
	SystemPropertiesMode systemPropertiesMode = DEFAULT_SYSTEM_PROPERTIES_MODE;
	boolean searchEnvironment = DEFAULT_IS_SEARCH_ENVIRONMENT;
	Properties properties;

	public DefaultPropertyResolver() {
		this(DEFAULT_SYSTEM_PROPERTIES_MODE, DEFAULT_IS_SEARCH_ENVIRONMENT);
	}

	public DefaultPropertyResolver(SystemPropertiesMode systemPropertiesMode, boolean searchEnvironment) {
		super();
		this.systemPropertiesMode = systemPropertiesMode;
		this.searchEnvironment = searchEnvironment;
	}

	@Override
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public SystemPropertiesMode getSystemPropertiesMode() {
		return systemPropertiesMode;
	}

	public void setSystemPropertiesMode(SystemPropertiesMode systemPropertiesMode) {
		this.systemPropertiesMode = systemPropertiesMode;
	}

	public boolean isSearchEnvironment() {
		return searchEnvironment;
	}

	public void setSearchEnvironment(boolean searchEnvironment) {
		this.searchEnvironment = searchEnvironment;
	}

}
