package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class SimplePropertyResolverTest {

	@Test
	public void test1() throws IOException {
		String key = "foo";
		String val = "bar";
		Properties properties = new Properties();
		properties.setProperty(key, val);
		PropertyRetriever resolver = new PropertiesRetriever(properties);

		String resolvedProperty = resolver.retrieveProperty(key);
		String unresolvedProperty = resolver.retrieveProperty("A-Key-That-Does-Not-Exist");

		Assert.assertEquals(val, resolvedProperty);
		Assert.assertNull(unresolvedProperty);

	}

	@Test
	public void test2() throws IOException {
		String key = "foo";
		String val = "bar";
		Properties properties = new Properties();
		properties.setProperty(key, val);
		PropertiesRetriever resolver = new PropertiesRetriever();
		resolver.setProperties(properties);

		String resolvedProperty = resolver.retrieveProperty(key);
		String unresolvedProperty = resolver.retrieveProperty("A-Key-That-Does-Not-Exist");

		Assert.assertEquals(val, resolvedProperty);
		Assert.assertNull(unresolvedProperty);

		Properties resolverProperties = resolver.getProperties();
		Assert.assertEquals(properties.getProperty(key), resolverProperties.getProperty(key));

	}

}
