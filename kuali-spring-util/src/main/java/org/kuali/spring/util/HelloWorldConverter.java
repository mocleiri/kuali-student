package org.kuali.spring.util;

public class HelloWorldConverter extends DefaultPropertiesConverter {

	@Override
	protected String convert(String originalValue) {
		return "Hello world";
	}

}
