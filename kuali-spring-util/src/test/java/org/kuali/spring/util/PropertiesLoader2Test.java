package org.kuali.spring.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesLoader2Test {

	protected Resource[] getLocations() {
		String[] locationStrings = new String[] { "classpath:org/kuali/spring/util/test.properties",
				"classpath:org/kuali/spring/util/test2.properties",
				"classpath:org/kuali/spring/util/test-properties.xml",
				"classpath:org/kuali/spring/util/malformed-properties.xml" };
		ResourceLoader loader = new DefaultResourceLoader();
		Resource[] locations = new Resource[locationStrings.length];
		for (int i = 0; i < locations.length; i++) {
			locations[i] = loader.getResource(locationStrings[i]);
		}
		return locations;
	}

	@Test
	public void resourceNotFound() throws IOException {
		Resource location = new DefaultResourceLoader().getResource("classpath:some-resource-that-does-not-exist.txt");
		DefaultPropertiesLoader loader = new DefaultPropertiesLoader();
		try {
			loader.getProperties(location);
			// Make sure it fails as a default behavior
			Assert.fail("Should have thrown an exception that the resource could not be found");
		} catch (PropertiesLoadException e) {
			// expected
		}
		loader.setIgnoreResourceNotFound(true);
		// Test that we get an empty properties object back
		Properties properties = loader.getProperties(location);
		Assert.assertEquals(0, properties.size());
	}

	@Test
	public void nullSafeClose() throws IOException {
		DefaultPropertiesLoader loader = new DefaultPropertiesLoader();
		// Try to "close" null
		loader.nullSafeClose(null);
		InputStream is = new ByteArrayInputStream(new byte[] { 1 });
		// Close a real InputStrem
		loader.nullSafeClose(is);
	}

}
