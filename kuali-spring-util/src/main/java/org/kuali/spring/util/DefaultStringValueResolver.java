package org.kuali.spring.util;

import org.springframework.util.StringValueResolver;

public class DefaultStringValueResolver implements StringValueResolver {
	PlaceholderReplacer replacer;
	PropertyRetriever retriever;
	String nullValue;

	public DefaultStringValueResolver() {
		this(null, null, null);
	}

	public DefaultStringValueResolver(PlaceholderReplacer replacer, PropertyRetriever retriever, String nullValue) {
		super();
		this.replacer = replacer;
		this.retriever = retriever;
		this.nullValue = nullValue;
	}

	@Override
	public String resolveStringValue(String strVal) {
		String value = replacer.replacePlaceholders(strVal, retriever);
		return value.equals(nullValue) ? null : value;
	}

	public PlaceholderReplacer getReplacer() {
		return replacer;
	}

	public void setReplacer(PlaceholderReplacer replacer) {
		this.replacer = replacer;
	}

	public PropertyRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(PropertyRetriever resolver) {
		this.retriever = resolver;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

}
