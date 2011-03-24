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

public class PropertiesHelperTest {

	@Test
	public void test1() throws IOException {
		String[] locations = new String[] { "classpath:org/kuali/spring/util/test.properties",
				"classpath:org/kuali/spring/util/test2.properties",
				"classpath:org/kuali/spring/util/test-properties.xml",
				"classpath:org/kuali/spring/util/malformed-properties.xml" };
		ResourceLoader loader = new DefaultResourceLoader();
		Resource[] resourceLocations = new Resource[locations.length];
		for (int i = 0; i < locations.length; i++) {
			resourceLocations[i] = loader.getResource(locations[i]);
		}
		Properties properties = new Properties();
		PropertiesHelper helper = new PropertiesHelper();
		helper.setIgnoreResourceNotFound(true);
		helper.loadProperties(properties, null);
		helper.loadProperties(properties, resourceLocations);
		helper.setFileEncoding("UTF-8");
		helper.loadProperties(properties, resourceLocations);
	}

	@Test
	public void nullSafeClose() throws IOException {
		PropertiesHelper helper = new PropertiesHelper();
		helper.nullSafeClose(null);
		InputStream is = new ByteArrayInputStream(new byte[] { 1 });
		helper.nullSafeClose(is);
	}

	@Test
	public void handleIOException() throws IOException {
		PropertiesHelper helper = new PropertiesHelper();
		try {
			helper.handleIOException(null, new IOException("Just testing"));
			Assert.assertFalse(true);
		} catch (IOException e) {
			// this is expected
		}
		helper.setIgnoreResourceNotFound(true);
		helper.handleIOException(null, new IOException("Just testing"));
	}

	@Test
	public void replaceProperties() {
		Properties oldProps = new Properties();
		oldProps.setProperty("a", "1");
		oldProps.setProperty("b", "2");

		Properties newProps = new Properties();
		newProps.setProperty("b", "3");
		newProps.setProperty("c", "4");
		newProps.setProperty("d", "5");

		PropertiesHelper helper = new PropertiesHelper();
		helper.syncProperties(oldProps, newProps);
		for (String key : oldProps.stringPropertyNames()) {
			Assert.assertEquals(newProps.getProperty(key), oldProps.getProperty(key));
		}
	}

	@Test
	public void mergeProperty() {
		Properties oldProps = new Properties();
		oldProps.setProperty("a", "1");
		oldProps.setProperty("b", "2");

		Properties newProps = new Properties();
		newProps.setProperty("b", "3");
		newProps.setProperty("c", "4");
		newProps.setProperty("d", "5");

		PropertiesHelper helper = new PropertiesHelper();
		helper.mergeProperty(oldProps, newProps, "b", false, "Unit Test");
		helper.mergeProperty(oldProps, newProps, "foo", false, "Unit Test");
	}

	@Test
	public void cloneProperties() {
		Properties oldProps = new Properties();
		oldProps.setProperty("a", "1");
		oldProps.setProperty("b", "2");

		PropertiesHelper helper = new PropertiesHelper();
		Properties newProps = helper.getClone(oldProps);

		for (String key : oldProps.stringPropertyNames()) {
			Assert.assertEquals(newProps.getProperty(key), oldProps.getProperty(key));
		}
	}

	@Test
	public void mergeSystemProperties() {
		Properties currentProps = new Properties();
		currentProps.setProperty("a", "1");
		currentProps.setProperty("b", "2");

		PropertiesHelper helper = new PropertiesHelper();
		helper.mergeSystemProperties(currentProps, SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER);

	}
}
