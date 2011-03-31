package org.kuali.spring.util;

/**
 * Always return the same thing
 */
public class HelloWorldRetriever extends PropertiesRetriever {

	@Override
	public String retrieveValue(String key) {
		return "Hello World";
	}

}
