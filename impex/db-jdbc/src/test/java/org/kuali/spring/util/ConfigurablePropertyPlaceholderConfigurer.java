package org.kuali.spring.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ConfigurablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	String placeholderPrefix;
	String placeholderSuffix;
	String valueSeparator;
	SystemPropertiesMode systemPropertiesModeEnum;
	boolean searchSystemEnvironment;
	boolean ignoreUnresolvablePlaceholders;

	String nullValue;
	String beanName;
	BeanFactory beanFactory;
	PlaceholderResolvingStringValueResolver stringValueResolver;
	ConfigurableBeanDefinitionVisitor beanDefinitionVisitor;
	MyPlaceholderResolver placeholderResolver;

	/**
	 * Mimic the default configuration from PropertyPlaceholderConfigurer
	 */
	public ConfigurablePropertyPlaceholderConfigurer() {
		super();
		this.setPlaceholderPrefix(DEFAULT_PLACEHOLDER_PREFIX);
		this.setPlaceholderSuffix(DEFAULT_PLACEHOLDER_SUFFIX);
		this.setValueSeparator(DEFAULT_VALUE_SEPARATOR);
		this.setSystemPropertiesModeEnum(SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK);
		this.setSearchSystemEnvironment(true);
		this.setIgnoreUnresolvablePlaceholders(false);

		NestedPropertyPlaceholderHelper helper = new NestedPropertyPlaceholderHelper();
		helper.setIgnoreUnresolvablePlaceholders(ignoreUnresolvablePlaceholders);
		helper.setPlaceholderPrefix(placeholderPrefix);
		helper.setPlaceholderSuffix(placeholderSuffix);
		helper.setValueSeparator(valueSeparator);

		MyPlaceholderResolver resolver = new MyPlaceholderResolver();
		resolver.setSearchSystemEnvironment(searchSystemEnvironment);
		resolver.setSystemPropertiesMode(systemPropertiesModeEnum);

		stringValueResolver = new PlaceholderResolvingStringValueResolver();
		stringValueResolver.setHelper(helper);
		stringValueResolver.setNullValue(nullValue);
		stringValueResolver.setResolver(resolver);

	}

	protected boolean currentBeanIsMe(String name, ConfigurableListableBeanFactory beanFactory) {
		if (!name.equals(this.beanName)) {
			return false;
		}
		if (!beanFactory.equals(this.beanFactory)) {
			return false;
		}
		return true;
	}

	protected void processBeanDefinitions(ConfigurableListableBeanFactory beanFactory) {
		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String curName : beanNames) {
			// Skip processing our own bean definition
			// Prevent failing on unresolvable placeholders in the locations property
			if (currentBeanIsMe(curName, beanFactory)) {
				continue;
			}
			BeanDefinition bd = beanFactory.getBeanDefinition(curName);
			try {
				beanDefinitionVisitor.visitBeanDefinition(bd);
			} catch (Exception ex) {
				throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage());
			}
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		placeholderResolver.setProperties(props);
		stringValueResolver.setProperties(props);
		beanDefinitionVisitor.setStringValueResolver(stringValueResolver);

		// Process placeholders in the bean definitions
		processBeanDefinitions(beanFactory);

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactory.resolveAliases(stringValueResolver);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactory.addEmbeddedValueResolver(stringValueResolver);
	}

	// ********* These setters have custom behavior ****
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
	// ********* End of setters with custom behavior ****

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

	public PlaceholderResolvingStringValueResolver getStringValueResolver() {
		return stringValueResolver;
	}

	public void setStringValueResolver(PlaceholderResolvingStringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

	public ConfigurableBeanDefinitionVisitor getBeanDefinitionVisitor() {
		return beanDefinitionVisitor;
	}

	public void setBeanDefinitionVisitor(ConfigurableBeanDefinitionVisitor beanDefinitionVisitor) {
		this.beanDefinitionVisitor = beanDefinitionVisitor;
	}

	public boolean isIgnoreUnresolvablePlaceholders() {
		return ignoreUnresolvablePlaceholders;
	}

	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public MyPlaceholderResolver getPlaceholderResolver() {
		return placeholderResolver;
	}

	public void setPlaceholderResolver(MyPlaceholderResolver placeholderResolver) {
		this.placeholderResolver = placeholderResolver;
	}

}
