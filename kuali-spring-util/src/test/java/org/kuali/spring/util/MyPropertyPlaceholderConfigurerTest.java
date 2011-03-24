package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;


import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class MyPropertyPlaceholderConfigurerTest {

	protected Resource[] getLocations() {
		String[] locations = new String[] { "classpath:org/kuali/spring/util/test.properties" };
		ResourceLoader loader = new DefaultResourceLoader();
		Resource[] resources = new Resource[locations.length];
		for (int i = 0; i < locations.length; i++) {
			String location = locations[i];
			Resource resource = loader.getResource(location);
			resources[i] = resource;
		}
		return resources;
	}

	@Test
	public void test1() throws IOException {
		PropertyHandler configurer = new PropertyHandler();
		configurer.setLocations(getLocations());
		Properties properties = configurer.mergeProperties();
		configurer.convertProperties(properties);
	}

}
