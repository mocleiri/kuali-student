package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class SimplePropertyResolverTest {

	@Test
	public void test() throws IOException {
		String key = "foo";
		String val = "bar";
		Properties properties = new Properties();
		properties.setProperty(key, val);
		PropertyResolver resolver = new SimplePropertyResolver(properties);
		String resolvedProperty = resolver.getProperty(key);
		Assert.assertEquals(val, resolvedProperty);
		String unresolvedProperty = resolver.getProperty("A-Key-That-Does-Not-Exist");
		Assert.assertNull(unresolvedProperty);
	}

}
