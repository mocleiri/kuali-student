package org.kuali.spring.util;

public class SystemAwarePropertiesRetriever extends PropertiesRetriever {
	public static final SystemPropertiesMode DEFAULT_SYSTEM_PROPERTIES_MODE = SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE;
	public static final boolean DEFAULT_IS_SEARCH_ENVIRONMENT = true;
	SystemPropertiesMode mode = DEFAULT_SYSTEM_PROPERTIES_MODE;
	boolean searchEnvironment = DEFAULT_IS_SEARCH_ENVIRONMENT;

	public SystemAwarePropertiesRetriever() {
		this(DEFAULT_SYSTEM_PROPERTIES_MODE, DEFAULT_IS_SEARCH_ENVIRONMENT);
	}

	public SystemAwarePropertiesRetriever(SystemPropertiesMode mode, boolean searchEnvironment) {
		super();
		this.mode = mode;
		this.searchEnvironment = searchEnvironment;
	}

	@Override
	public String retrieveProperty(String key) {
		String environmentProperty = SystemUtils.getEnvironmentPropertyIgnoreExceptions(key);
		String property = super.retrieveProperty(key);
		String systemProperty = SystemUtils.getSystemPropertyIgnoreExceptions(key);

		if (isUseSystemProperty(systemProperty, property)) {
			return systemProperty;
		}
		if (isUseEnvironmentProperty(environmentProperty, property)) {
			return environmentProperty;
		}
		return property;
	}

	protected boolean isUseSystemProperty(String systemProperty, String regularProperty) {
		// The system property is null, don't use it
		if (systemProperty == null) {
			return false;
		}

		switch (mode) {
		case SYSTEM_PROPERTIES_MODE_NEVER:
			// We've been instructed to always ignore system properties
			return false;
		case SYSTEM_PROPERTIES_MODE_OVERRIDE:
			// The system property is not null and system properties always win
			return true;
		case SYSTEM_PROPERTIES_MODE_FALLBACK:
			// The system property is not null, but we can only use it if the regular property is null
			if (regularProperty == null) {
				return true;
			} else {
				return false;
			}
		default:
			// The system properties mode is unknown
			throw new IllegalArgumentException("Unknown system properties mode [" + mode + "] Available modes are "
					+ SystemPropertiesMode.values());
		}
	}

	protected boolean isUseEnvironmentProperty(String environmentProperty, String regularProperty) {
		// Always ignore environment properties
		if (!searchEnvironment) {
			return false;
		}

		// The environment property is null, don't use it
		if (environmentProperty == null) {
			return false;
		}

		// Never use an environment property over a regular property
		if (regularProperty != null) {
			return false;
		}

		// The regular property is null and the environment property is not
		return true;
	}

	public boolean isSearchEnvironment() {
		return searchEnvironment;
	}

	public void setSearchEnvironment(boolean searchEnvironment) {
		this.searchEnvironment = searchEnvironment;
	}

	public SystemPropertiesMode getMode() {
		return mode;
	}

	public void setMode(SystemPropertiesMode mode) {
		this.mode = mode;
	}

}
