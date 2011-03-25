package org.kuali.spring.util;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class PropertiesHelperTest {

	protected PropertiesHelper getPropertiesHelper() {
		PropertiesLoggerSupport loggerSupport = new PropertiesLoggerSupport();
		loggerSupport.setFlattenPropertyValues(true);
		loggerSupport.setLinefeedReplacement("LF");
		loggerSupport.setCarriageReturnReplacement("CR");
		PropertiesHelper helper = new PropertiesHelper();
		helper.setLoggerSupport(loggerSupport);
		return helper;
	}

	@Test
	public void syncProperties() {
		Properties oldProps = new Properties();
		oldProps.setProperty("a", "1");
		oldProps.setProperty("b", "2");

		Properties newProps = new Properties();
		newProps.setProperty("b", "3");
		newProps.setProperty("c", "4");
		newProps.setProperty("d", "5");

		PropertiesHelper helper = getPropertiesHelper();

		helper.syncProperties(oldProps, newProps);
		for (String key : oldProps.stringPropertyNames()) {
			Assert.assertEquals(newProps.getProperty(key), oldProps.getProperty(key));
		}
	}

	@Test
	public void mergeProperty() {
		String source = "UNITTEST";
		String key = "a";
		String newKey = "b";
		String a = "a";
		String a1 = "a1";
		String b = "b";

		Properties currentProps = new Properties();
		currentProps.setProperty(key, a);

		Properties newProps = new Properties();
		newProps.setProperty(key, a1);
		newProps.setProperty(newKey, b);

		PropertiesHelper helper = getPropertiesHelper();

		PropertiesMergeContext context = new PropertiesMergeContext(currentProps, newProps, false, source, true);

		// Test that the value was NOT overridden
		helper.mergeProperty(context, key);
		Assert.assertEquals(a, currentProps.getProperty(key));

		// Test that the value WAS overridden
		context.setOverride(true);
		helper.mergeProperty(context, key);
		Assert.assertEquals(a1, currentProps.getProperty(key));

		// Test that the new property was merged in
		helper.mergeProperty(context, newKey);
		Assert.assertEquals(b, currentProps.getProperty(newKey));
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
	public void mergeSystemPropertiesNever() {
		String key = "a";
		String val = "1";
		Properties currentProps = new Properties();
		currentProps.setProperty(key, val);

		PropertiesHelper helper = getPropertiesHelper();

		System.setProperty(key, "some-other-value");
		helper.mergeSystemProperties(currentProps, SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_NEVER);
		Assert.assertEquals(val, currentProps.getProperty(key));
		System.getProperties().remove(key);
	}

	@Test
	public void mergeSystemPropertiesOverride() {
		String key = "a";
		String val = "1";
		Properties currentProps = new Properties();
		currentProps.setProperty(key, val);

		PropertiesHelper helper = getPropertiesHelper();

		System.setProperty(key, "some-other-value");
		helper.mergeSystemProperties(currentProps, SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		Assert.assertEquals("some-other-value", currentProps.getProperty(key));
		System.getProperties().remove(key);
	}

	@Test
	public void mergeSystemPropertiesFallback() {
		String key = "a";
		String val = "1";
		Properties currentProps = new Properties();
		currentProps.setProperty(key, val);

		PropertiesHelper helper = getPropertiesHelper();

		System.setProperty(key, "some-other-value");
		System.setProperty("b", "2");
		helper.mergeSystemProperties(currentProps, SystemPropertiesMode.SYSTEM_PROPERTIES_MODE_FALLBACK);
		Assert.assertEquals(val, currentProps.getProperty(key));
		Assert.assertEquals("2", currentProps.getProperty("b"));
		System.getProperties().remove(key);
		System.getProperties().remove("b");
	}

}
