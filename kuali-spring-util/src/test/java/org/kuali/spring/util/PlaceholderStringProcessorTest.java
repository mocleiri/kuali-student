package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceholderStringProcessorTest {

	final Logger logger = LoggerFactory.getLogger(PlaceholderStringProcessorTest.class);

	@Test
	public void resolve() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("1.2", "blue");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver processor = new PlaceholderStringResolver();
		String text = "The sky is ${${a}.${b}}";
		Assert.assertEquals("The sky is blue", processor.resolve(text, retriever));
	}

	@Test
	public void process() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver processor = new PlaceholderStringResolver();
		String text = "${foo}";
		Assert.assertEquals("bar", processor.resolve(text, retriever));
	}

	@Test
	public void processNested() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");
		properties.setProperty("c", "3");
		properties.setProperty("1.2", "beans");
		PropertiesRetriever retriever = new PropertiesRetriever(properties);
		PlaceholderStringResolver processor = new PlaceholderStringResolver();
		String text = "I like ${${a}.${b}}";
		Assert.assertEquals("I like beans", processor.resolve(text, retriever));
	}

}
