package org.kuali.spring.util;

import java.util.Set;

/**
 * 
 *
 */
public class ProcessStringContext {

	PropertyRetriever retriever;
	Set<String> visitedPlaceholders;
	int startIndex;
	StringBuilder buffer;

	public ProcessStringContext() {
		this(null, null, 0, null);
	}

	public ProcessStringContext(PropertyRetriever resolver, Set<String> visitedPlaceholders, int startIndex,
			StringBuilder buffer) {
		super();
		this.retriever = resolver;
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

	public PropertyRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(PropertyRetriever resolver) {
		this.retriever = resolver;
	}
}
