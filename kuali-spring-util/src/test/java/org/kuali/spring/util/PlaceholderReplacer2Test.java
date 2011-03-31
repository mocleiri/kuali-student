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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Properties resolvedProperties = replacer.resolvePlaceholders(properties);
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		try {
			replacer.resolvePlaceholders(properties);
			Assert.fail("Should have thrown an exception for duplicate property");
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Properties resolvedProperties = replacer.resolvePlaceholders(properties);
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Properties resolvedProperties = replacer.resolvePlaceholders(properties);
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Properties resolvedProperties = replacer.resolvePlaceholders(properties);
		String resolvedValue = resolvedProperties.getProperty("cat.agility");
		Assert.assertEquals("awesome", resolvedValue);
		System.out.println(resolvedProperties);
	}

	@Test
	public void circularReference() {
		Properties properties = new Properties();
		properties.setProperty("a", "${b}");
		properties.setProperty("b", "${a}");

		PlaceholderResolver replacer = new PlaceholderResolver();
		String s = "${a}";
		try {
			replacer.resolvePlaceholders(s, properties);
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Assert.assertEquals("lion", replacer.resolvePlaceholders("${cat}", properties));
		Assert.assertEquals("good", replacer.resolvePlaceholders("${${cat}.${gender}.power}", properties));
		Assert.assertEquals("awesome", replacer.resolvePlaceholders("${${cat}.${gender}.agility}", properties));
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

		PlaceholderResolver replacer = new PlaceholderResolver();
		Assert.assertEquals("awesome", replacer.resolvePlaceholders("${cat.agility}", properties));
	}

	@Test
	public void getPlaceholders() {
		PlaceholderResolver replacer = new PlaceholderResolver();
		String s = "I like ${food}";
		List<Placeholder> placeholders = replacer.getPlaceholders(s);
		// Should be one placeholder
		Assert.assertEquals(1, placeholders.size());
		Placeholder placeholder = placeholders.get(0);
		// No nested placeholders
		Assert.assertEquals(0, placeholder.getPlaceholders().size());
		s = "I like food";
		placeholders = replacer.getPlaceholders(s);
		// No placeholders
		Assert.assertEquals(0, placeholders.size());
		s = "I like ${food";
		placeholders = replacer.getPlaceholders(s);
		// No placeholders
		Assert.assertEquals(0, placeholders.size());
		s = "I like ${${color}.${food}}";
		placeholders = replacer.getPlaceholders(s);
		placeholder = placeholders.get(0);
		Assert.assertEquals(1, placeholders.size());
		Assert.assertEquals(2, placeholder.getPlaceholders().size());
	}

}
