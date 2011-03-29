package org.kuali.spring.util;

public enum PropertyMergeResultReason {
	NOOP_NULL_NEW_VALUE, // New value was null, nothing changes
	NOOP_IDENTICAL_VALUES, // New value is the same as the old value, nothing changes
	EXISTING_PROPERTY_WINS, // Existing property wins over the new property
	ADD, // No existing property, property was added
	OVERRIDE; // Existing property overridden by new property
}
