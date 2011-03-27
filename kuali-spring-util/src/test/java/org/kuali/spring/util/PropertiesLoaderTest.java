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

public class PropertiesLoaderTest {

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
	public void test1() throws IOException {
		Resource[] locations = getLocations();
		Properties properties = new Properties();
		DefaultPropertiesLoaderOld loader = new DefaultPropertiesLoaderOld();
		loader.setIgnoreResourceNotFound(true);
		loader.loadProperties(properties, null);
		loader.loadProperties(properties, locations);
		loader.setFileEncoding("UTF-8");
		loader.loadProperties(properties, locations);
	}

	@Test
	public void nullSafeClose() throws IOException {
		DefaultPropertiesLoaderOld loader = new DefaultPropertiesLoaderOld();
		loader.nullSafeClose(null);
		InputStream is = new ByteArrayInputStream(new byte[] { 1 });
		loader.nullSafeClose(is);
	}

	@Test
	public void handleIOException() throws IOException {
		DefaultPropertiesLoaderOld loader = new DefaultPropertiesLoaderOld();
		try {
			loader.handleIOException(null, new IOException("Just testing"));
			Assert.assertFalse(true);
		} catch (IOException e) {
			// this is expected
		}
		loader.setIgnoreResourceNotFound(true);
		loader.handleIOException(null, new IOException("Just testing"));
	}

}
