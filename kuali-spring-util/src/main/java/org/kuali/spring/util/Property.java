package org.kuali.spring.util;

public class Property {
	String key;
	String value;
	PropertySource source;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PropertySource getSource() {
		return source;
	}

	public void setSource(PropertySource source) {
		this.source = source;
	}
}
