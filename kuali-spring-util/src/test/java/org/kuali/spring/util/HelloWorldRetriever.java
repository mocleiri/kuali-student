package org.kuali.spring.util;

/**
 * Always return the same thing
 */
public class HelloWorldRetriever extends PropertiesRetriever {

	@Override
	public String retrieveProperty(String key) {
		return "Hello World";
	}

}
