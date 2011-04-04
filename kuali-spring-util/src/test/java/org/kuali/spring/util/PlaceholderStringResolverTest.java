package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PlaceholderStringResolverTest {

	final Logger logger = LoggerFactory.getLogger(PlaceholderStringResolverTest.class);

	@Test
	public void unresolvablePlaceholder() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "${b}");
		properties.setProperty("b", "${a}");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "Hello ${c} World";
		try {
			resolver.resolve(text, retriever);
			Assert.fail("Should have thrown an exception due to an unresolvable placeholder");
		} catch (IllegalArgumentException e) {
			// expected
		}

		resolver.setIgnoreUnresolvablePlaceholders(true);
		// Make sure nothing changed
		Assert.assertEquals(text, resolver.resolve(text, retriever));

	}

	@Test
	public void noMatchingSuffix() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "${b}");
		properties.setProperty("b", "${a}");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "Hello ${a World";
		Assert.assertEquals(text, resolver.resolve(text, retriever));

	}

	@Test
	public void constructorTest() throws IOException {
		PlaceholderStringResolver resolver = new PlaceholderStringResolver("foo", "bar", "=", true);
		Assert.assertEquals("foo", resolver.getPlaceholderPrefix());
		Assert.assertEquals("bar", resolver.getPlaceholderSuffix());
		Assert.assertEquals("=", resolver.getValueSeparator());
		Assert.assertEquals(resolver.getSimplePrefix(), resolver.getPlaceholderPrefix());

		PlaceholderStringResolver resolver2 = new PlaceholderStringResolver("foo2", "bar2");
		Assert.assertEquals("foo2", resolver2.getPlaceholderPrefix());
		Assert.assertEquals("bar2", resolver2.getPlaceholderSuffix());
		Assert.assertNull(resolver2.getValueSeparator());
		Assert.assertEquals(PlaceholderStringResolver.DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS,
				resolver2.isIgnoreUnresolvablePlaceholders());

		PlaceholderStringResolver resolver3 = new PlaceholderStringResolver();
		Assert.assertEquals(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX, resolver3.getPlaceholderPrefix());
		Assert.assertEquals(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, resolver3.getPlaceholderSuffix());
	}

	@Test
	public void circularReference() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "${b}");
		properties.setProperty("b", "${a}");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "${a}";
		try {
			resolver.resolve(text, retriever);
			Assert.fail("Should have thrown a circular reference exception");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void resolve2() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "${a}");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "${b}";
		Assert.assertEquals("1", resolver.resolve(text, retriever));
	}

	@Test
	public void resolve() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("1.2", "blue");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "The sky is ${${a}.${b}}";
		Assert.assertEquals("The sky is blue", resolver.resolve(text, retriever));
	}

	@Test
	public void process() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "${foo}";
		Assert.assertEquals("bar", resolver.resolve(text, retriever));
	}

	@Test
	public void processNested() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("c", "3");
		properties.setProperty("1.2", "beans");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver resolver = new PlaceholderStringResolver();
		String text = "I like ${${a}.${b}}";
		Assert.assertEquals("I like beans", resolver.resolve(text, retriever));
	}

}
