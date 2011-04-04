package org.kuali.spring.util;

public class HelloWorldConverter extends PropertiesConverter {

	@Override
	protected String convert(String originalValue) {
		return "Hello world";
	}

}
