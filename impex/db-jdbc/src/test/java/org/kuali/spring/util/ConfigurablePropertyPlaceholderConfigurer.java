package org.kuali.spring.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ConfigurablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	int ignoreUnresolvablePlaceholders;
	String placeholderPrefix;
	String placeholderSuffix;
	boolean searchSystemEnvironment;
	String valueSeparator;
	SystemPropertiesMode systemPropertiesModeEnum;
	String nullValue;
	String beanName;
	BeanFactory beanFactory;

	/**
	 * Invoke setters that mimic the default configuration used by Spring's PropertyPlaceholderConfigurer
	 */
	public ConfigurablePropertyPlaceholderConfigurer() {
		super();
		setPlaceholderPrefix(DEFAULT_PLACEHOLDER_PREFIX);
		setPlaceholderSuffix(DEFAULT_PLACEHOLDER_SUFFIX);
		setValueSeparator(DEFAULT_VALUE_SEPARATOR);
		setSystemPropertiesModeEnum(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK);
		setSearchSystemEnvironment(true);
		setIgnoreUnresolvablePlaceholders(false);
	}

	// ********* Begin setters with custom behavior ****
	public void setSystemPropertiesModeEnum(SystemPropertiesMode systemPropertiesModeEnum) {
		this.systemPropertiesModeEnum = systemPropertiesModeEnum;
		super.setSystemPropertiesModeName(this.systemPropertiesModeEnum.name());
	}

	@Override
	public void setPlaceholderPrefix(String placeholderPrefix) {
		this.placeholderPrefix = placeholderPrefix;
		super.setPlaceholderPrefix(placeholderPrefix);
	}

	@Override
	public void setPlaceholderSuffix(String placeholderSuffix) {
		this.placeholderSuffix = placeholderSuffix;
		super.setPlaceholderSuffix(placeholderSuffix);
	}

	@Override
	public void setSearchSystemEnvironment(boolean searchSystemProperties) {
		this.searchSystemEnvironment = searchSystemProperties;
		super.setSearchSystemEnvironment(searchSystemProperties);
	}

	@Override
	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
		super.setValueSeparator(valueSeparator);
	}

	@Override
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
		super.setNullValue(nullValue);
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
		super.setBeanName(beanName);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		super.setBeanFactory(beanFactory);
	}

	// ********* End setters with custom behavior ****

	public int getIgnoreUnresolvablePlaceholders() {
		return ignoreUnresolvablePlaceholders;
	}

	public void setIgnoreUnresolvablePlaceholders(int ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public String getPlaceholderPrefix() {
		return placeholderPrefix;
	}

	public String getPlaceholderSuffix() {
		return placeholderSuffix;
	}

	public boolean isSearchSystemEnvironment() {
		return searchSystemEnvironment;
	}

	public String getValueSeparator() {
		return valueSeparator;
	}

	public SystemPropertiesMode getSystemPropertiesModeEnum() {
		return systemPropertiesModeEnum;
	}

	public String getNullValue() {
		return nullValue;
	}

	public String getBeanName() {
		return beanName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
