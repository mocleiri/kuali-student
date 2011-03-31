package org.kuali.spring.util;

public interface PlaceholderReplacer {

	public String replacePlaceholders(String source, ValueRetriever retriever);

}
