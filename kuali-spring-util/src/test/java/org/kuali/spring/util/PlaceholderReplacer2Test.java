package org.kuali.spring.util;

import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class PlaceholderReplacer2Test {
	@Test
	public void duplicatePropertyCheckSameValues() {
		Properties properties = new Properties();
		properties.setProperty("day", "07");
		properties.setProperty("month", "12");
		properties.setProperty("year", "1941");
		properties.setProperty("1941.12.07", "A day that will live in infamy");
		properties.setProperty("${year}.${month}.${day}", "A day that will live in infamy");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Properties resolvedProperties = replacer.replacePlaceholders(properties);
		Assert.assertEquals("A day that will live in infamy", resolvedProperties.getProperty("1941.12.07"));
	}

	@Test
	public void duplicatePropertyCheck() {
		Properties properties = new Properties();
		properties.setProperty("day", "07");
		properties.setProperty("month", "12");
		properties.setProperty("year", "1941");
		properties.setProperty("1941.12.07", "Just a normal day");
		properties.setProperty("${year}.${month}.${day}", "A day that will live in infamy");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		try {
			replacer.replacePlaceholders(properties);
			Assert.fail("Should have thrown an exception for duplicate property with different values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void resolveNestedPropertyKeys() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("a2", "foo");
		properties.setProperty("1.${a${b}}", "bar");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Properties resolvedProperties = replacer.replacePlaceholders(properties);
		String resolvedValue = resolvedProperties.getProperty("1.foo");
		Assert.assertEquals("bar", resolvedValue);
		System.out.println(resolvedProperties);
	}

	@Test
	public void resolvePropertyKeys() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("c", "3");
		properties.setProperty("${a}.${b}.${c}", "foo");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Properties resolvedProperties = replacer.replacePlaceholders(properties);
		String resolvedValue = resolvedProperties.getProperty("1.2.3");
		Assert.assertEquals("foo", resolvedValue);
		System.out.println(resolvedProperties);
	}

	@Test
	public void resolveProperties() {
		Properties properties = new Properties();
		properties.setProperty("cat", "lion");
		properties.setProperty("gender", "female");
		properties.setProperty("cat.agility", "${${cat}.${gender}.agility}");
		properties.setProperty("lion.male.agility", "average");
		properties.setProperty("lion.male.power", "awesome");
		properties.setProperty("lion.female.agility", "awesome");
		properties.setProperty("lion.female.power", "good");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Properties resolvedProperties = replacer.replacePlaceholders(properties);
		String resolvedValue = resolvedProperties.getProperty("cat.agility");
		Assert.assertEquals("awesome", resolvedValue);
		System.out.println(resolvedProperties);
	}

	@Test
	public void circularReference() {
		Properties properties = new Properties();
		properties.setProperty("a", "${b}");
		properties.setProperty("b", "${a}");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		String s = "${a}";
		try {
			replacer.replacePlaceholders(s, properties);
			Assert.fail("Should have thrown an exception due to a circular reference");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void resolvePlaceholders() {
		Properties properties = new Properties();
		properties.setProperty("cat", "lion");
		properties.setProperty("gender", "female");
		properties.setProperty("lion.male.agility", "average");
		properties.setProperty("lion.male.power", "awesome");
		properties.setProperty("lion.female.agility", "awesome");
		properties.setProperty("lion.female.power", "good");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Assert.assertEquals("lion", replacer.replacePlaceholders("${cat}", properties));
		Assert.assertEquals("good", replacer.replacePlaceholders("${${cat}.${gender}.power}", properties));
		Assert.assertEquals("awesome", replacer.replacePlaceholders("${${cat}.${gender}.agility}", properties));
	}

	@Test
	public void resolveNestedProperties() {
		Properties properties = new Properties();
		properties.setProperty("cat", "lion");
		properties.setProperty("gender", "female");
		properties.setProperty("cat.agility", "${${cat}.${gender}.agility}");

		properties.setProperty("lion.male.agility", "average");
		properties.setProperty("lion.male.power", "awesome");
		properties.setProperty("lion.female.agility", "awesome");
		properties.setProperty("lion.female.power", "good");

		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		Assert.assertEquals("awesome", replacer.replacePlaceholders("${cat.agility}", properties));
	}

	@Test
	public void getPlaceholders() {
		PropertiesPlaceholderProcessor replacer = new PropertiesPlaceholderProcessor();
		String s = "I like ${food}";
		List<Placeholder> placeholders = replacer.parsePlaceholders(s);
		// Should be one placeholder
		Assert.assertEquals(1, placeholders.size());
		Placeholder placeholder = placeholders.get(0);
		// No nested placeholders
		Assert.assertEquals(0, placeholder.getPlaceholders().size());
		s = "I like food";
		placeholders = replacer.parsePlaceholders(s);
		// No placeholders
		Assert.assertEquals(0, placeholders.size());
		s = "I like ${food";
		placeholders = replacer.parsePlaceholders(s);
		// No placeholders
		Assert.assertEquals(0, placeholders.size());
		s = "I like ${${color}.${food}}";
		placeholders = replacer.parsePlaceholders(s);
		placeholder = placeholders.get(0);
		Assert.assertEquals(1, placeholders.size());
		Assert.assertEquals(2, placeholder.getPlaceholders().size());
	}

}
