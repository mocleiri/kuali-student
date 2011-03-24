package org.kuali.spring.util;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ResolvePropertiesFirstPlaceholderConfigurerTest {

	@Test
	public void helloWorld() throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		String location = "classpath:org/kuali/spring/util/test.properties";
		Resource resource = loader.getResource(location);
		Resource[] resourceLocations = new Resource[] { resource };
		ResolvePropertiesFirstPlaceholderConfigurer rpfpc = new ResolvePropertiesFirstPlaceholderConfigurer();
		rpfpc.setResourceLocations(resourceLocations);
		rpfpc.mergeProperties();
	}

}
