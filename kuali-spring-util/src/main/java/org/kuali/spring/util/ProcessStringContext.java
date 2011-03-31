package org.kuali.spring.util;

import java.util.Set;

/**
 * 
 *
 */
public class ProcessStringContext {

	ValueRetriever retriever;
	Set<String> visitedPlaceholders;
	int startIndex;
	StringBuilder buffer;

	public ProcessStringContext() {
		this(null, null, 0, null);
	}

	public ProcessStringContext(ValueRetriever retriever, Set<String> visitedPlaceholders, int startIndex,
			StringBuilder buffer) {
		super();
		this.retriever = retriever;
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

	public ValueRetriever getRetriever() {
		return retriever;
	}

	public void setRetriever(ValueRetriever retriever) {
		this.retriever = retriever;
	}
}
