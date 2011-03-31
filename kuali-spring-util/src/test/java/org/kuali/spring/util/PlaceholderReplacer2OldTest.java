package org.kuali.spring.util;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class PlaceholderReplacer2OldTest {

	@Test
	public void replacePlaceholders() {
		Properties props = new Properties();
		props.setProperty("a", "1");
		PlaceholderReplacer replacer = new PlaceholderReplacer();
		Assert.assertEquals(replacer.replacePlaceholders("${a}", props), "1");
		Assert.assertEquals(replacer.replacePlaceholders("${a", props), "${a");
	}

	@Test
	public void circularReference() {
		Properties props = new Properties();
		props.setProperty("a", "${b}");
		props.setProperty("b", "${c}");
		props.setProperty("c", "${a}");
		PlaceholderReplacer replacer = new PlaceholderReplacer();
		try {
			replacer.replacePlaceholders("${a}", props);
			Assert.fail("Should have thrown an IllegalArgumentException due to a circular reference");
		} catch (IllegalArgumentException e) {
			// This is expected
		}
	}

	@Test
	public void replacePlaceholdersWithValueSeparator() throws IOException {
		Properties props = new Properties();
		props.setProperty("a", "1");
		props.setProperty("b", "2");
		props.setProperty("c", "3");
		PlaceholderReplacer replacer = new PlaceholderReplacer();
		replacer.setValueSeparator("=");
		Assert.assertEquals(replacer.replacePlaceholders("${d=4}", props), "4");
		Assert.assertEquals(replacer.replacePlaceholders("${c=4}", props), "3");
		Assert.assertEquals(replacer.replacePlaceholders("${c}", props), "3");
		replacer.setIgnoreUnresolvablePlaceholders(true);
		replacer.setValueSeparator(null);
		Assert.assertEquals(replacer.replacePlaceholders("${d}", props), "${d}");
		replacer.setIgnoreUnresolvablePlaceholders(false);
		try {
			replacer.replacePlaceholders("${d}", props);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			// This is expected
		}
	}

	@Test
	public void nestedPlaceholders() throws IOException {
		Properties props = new Properties();
		props.setProperty("cat", "lion");
		props.setProperty("cat.prey", "${${cat}.prey}");
		props.setProperty("lion.prey", "zebra");
		PlaceholderReplacer replacer = new PlaceholderReplacer();
		Assert.assertEquals(replacer.replacePlaceholders("${cat}", props), "lion");
		Assert.assertEquals(replacer.replacePlaceholders("${cat.prey}", props), "zebra");
		Assert.assertEquals(replacer.replacePlaceholders("${cat.prey}", props), "zebra");
	}
}
