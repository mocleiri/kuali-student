package org.kuali.spring.util;

public class PropertyMergeResult {
	PropertiesMergeContext context;
	String key;
	String oldValue;
	String newValue;
	PropertyMergeResultReason reason;

	public PropertyMergeResult() {
		this(null, null, null, null, null);
	}

	public PropertyMergeResult(PropertiesMergeContext context, String key, String oldValue, String newValue,
			PropertyMergeResultReason reason) {
		super();
		this.context = context;
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.reason = reason;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public PropertyMergeResultReason getReason() {
		return reason;
	}

	public void setReason(PropertyMergeResultReason reason) {
		this.reason = reason;
	}

	public PropertiesMergeContext getContext() {
		return context;
	}

	public void setContext(PropertiesMergeContext context) {
		this.context = context;
	}
}
