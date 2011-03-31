package org.kuali.spring.util;

import org.springframework.util.StringValueResolver;

public class DefaultStringValueResolver implements StringValueResolver {
	PlaceholderReplacerOld replacer;
	ValueRetriever retriever;
	String nullValue;

	public DefaultStringValueResolver() {
		this(null, null, null);
	}

	public DefaultStringValueResolver(PlaceholderReplacerOld replacer, ValueRetriever retriever, String nullValue) {
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

	public PlaceholderReplacerOld getReplacer() {
		return replacer;
	}

	public void setReplacer(PlaceholderReplacerOld replacer) {
		this.replacer = replacer;
	}

	public ValueRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(ValueRetriever resolver) {
		this.retriever = resolver;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

}
