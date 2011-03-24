package org.kuali.spring.util;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class PropertiesHelperTest {

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
