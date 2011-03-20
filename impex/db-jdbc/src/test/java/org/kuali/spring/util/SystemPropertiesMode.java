package org.kuali.spring.util;

public enum SystemPropertiesMode {

	SYSTEM_PROPERTIES_MODE_NEVER(0), //
	SYSTEM_PROPERTIES_MODE_FALLBACK(1), //
	SYSTEM_PROPERTIES_MODE_OVERRIDE(2);

	private final int value;

	private SystemPropertiesMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
