package org.kuali.spring.util;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class PropertyHandlerOldTest {

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
	public void autoWire() throws IOException {
		PropertyHandlerOld handler = new PropertyHandlerOld();
		handler.autoWire();
		Assert.notNull(handler.getPropertiesLogger());
	}

	@Test
	public void validate() throws IOException {
		PropertyHandlerOld handler = new PropertyHandlerOld();
		handler.autoWire();
		handler.validate();
	}

	@Test
	public void mergeProperties() throws IOException {
		PropertyHandlerOld handler = new PropertyHandlerOld();
		handler.autoWire();
		handler.validate();
		handler.setLocations(getLocations());
		handler.mergeProperties();
	}

}
