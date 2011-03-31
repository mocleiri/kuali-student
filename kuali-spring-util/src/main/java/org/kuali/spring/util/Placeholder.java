package org.kuali.spring.util;

import java.util.List;

public class Placeholder {
	int startIndex;
	int endIndex;
	String originalPlaceholder;
	String expandedPlaceholder;
	String value;
	boolean resolved;
	List<Placeholder> placeholders;

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getOriginalPlaceholder() {
		return originalPlaceholder;
	}

	public void setOriginalPlaceholder(String rawPlaceholder) {
		this.originalPlaceholder = rawPlaceholder;
	}

	public String getExpandedPlaceholder() {
		return expandedPlaceholder;
	}

	public void setExpandedPlaceholder(String resolvedPlaceholder) {
		this.expandedPlaceholder = resolvedPlaceholder;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String replacementValue) {
		this.value = replacementValue;
	}

	public List<Placeholder> getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(List<Placeholder> placeholders) {
		this.placeholders = placeholders;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
}
