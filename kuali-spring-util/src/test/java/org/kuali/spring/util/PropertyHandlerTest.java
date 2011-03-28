package org.kuali.spring.util;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertyHandlerTest {

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
	public void postProcessBeanFactory() throws IOException {
		DefaultPropertiesLoader loader = new DefaultPropertiesLoader();
		loader.setLocations(getLocations());
		PropertyHandler handler = new PropertyHandler();
		handler.setLoader(loader);
		handler.postProcessBeanFactory(null);
		// Assert.notNull(handler.getPlogger());
	}

}
