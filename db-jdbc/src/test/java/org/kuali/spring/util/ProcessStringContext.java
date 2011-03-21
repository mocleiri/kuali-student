package org.kuali.spring.util;

import java.util.Set;

import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

/**
 * 
 *
 */
public class ProcessStringContext {

	PlaceholderResolver resolver;
	Set<String> visitedPlaceholders;
	int startIndex;
	StringBuilder buffer;

	public ProcessStringContext() {
		this(null, null, 0, null);
	}

	public ProcessStringContext(PlaceholderResolver resolver, Set<String> visitedPlaceholders, int startIndex,
			StringBuilder buffer) {
		super();
		this.resolver = resolver;
		this.visitedPlaceholders = visitedPlaceholders;
		this.startIndex = startIndex;
		this.buffer = buffer;
	}

	public Set<String> getVisitedPlaceholders() {
		return visitedPlaceholders;
	}

	public void setVisitedPlaceholders(Set<String> visitedPlaceholders) {
		this.visitedPlaceholders = visitedPlaceholders;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public PlaceholderResolver getResolver() {
		return resolver;
	}

	public void setResolver(PlaceholderResolver resolver) {
		this.resolver = resolver;
	}

}
