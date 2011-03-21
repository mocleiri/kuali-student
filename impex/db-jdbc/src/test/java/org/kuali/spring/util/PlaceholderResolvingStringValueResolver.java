package org.kuali.spring.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringValueResolver;

public class PlaceholderResolvingStringValueResolver implements StringValueResolver {
	NestedPropertyPlaceholderHelper helper;
	PlaceholderResolver resolver;
	Properties properties;
	String nullValue;

	@Override
	public String resolveStringValue(String strVal) throws BeansException {
		String value = this.helper.replacePlaceholders(strVal, this.resolver);
		return (value.equals(nullValue) ? null : value);
	}

	public NestedPropertyPlaceholderHelper getHelper() {
		return helper;
	}

	public void setHelper(NestedPropertyPlaceholderHelper helper) {
		this.helper = helper;
	}

	public PlaceholderResolver getResolver() {
		return resolver;
	}

	public void setResolver(PlaceholderResolver resolver) {
		this.resolver = resolver;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

}
