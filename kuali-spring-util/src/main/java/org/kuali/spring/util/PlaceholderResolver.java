package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 
 */
public class PlaceholderResolver {

	final Logger logger = LoggerFactory.getLogger(PlaceholderResolver.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);
	public static final boolean DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}

	String placeholderPrefix;
	String placeholderSuffix;
	String valueSeparator;
	boolean ignoreUnresolvablePlaceholders;

	String simplePrefix;
	Properties resolvedCache = new Properties();

	public PlaceholderResolver() {
		this(DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
	}

	public PlaceholderResolver(boolean ignoreUnresolvablePlaceholders) {
		this(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null, ignoreUnresolvablePlaceholders);
	}

	public PlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
	}

	public PlaceholderResolver(String placeholderPrefix, String placeholderSuffix, String valueSeparator,
			boolean ignoreUnresolvablePlaceholders) {

		Assert.notNull(placeholderPrefix, "placeholderPrefix must not be null");
		Assert.notNull(placeholderSuffix, "placeholderSuffix must not be null");

		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;

		String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
		if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
			this.simplePrefix = simplePrefixForSuffix;
		} else {
			this.simplePrefix = this.placeholderPrefix;
		}
		this.valueSeparator = valueSeparator;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	protected boolean isEmpty(Collection<?> c) {
		return c == null || c.size() == 0;
	}

	protected String parseString(String string) {
		List<Placeholder> placeholders = getPlaceholders(string);
		if (isEmpty(placeholders)) {
			return string;
		}
		return string;

	}

	protected String getTrimmedPlaceholder(String placeholder) {
		int prefixLength = this.placeholderPrefix.length();
		int suffixLength = this.placeholderSuffix.length();
		int beginIndex = prefixLength;
		int endIndex = placeholder.length() - suffixLength;
		return placeholder.substring(beginIndex, endIndex);
	}

	protected boolean isBaseCase(Placeholder placeholder) {
		return isEmpty(placeholder.getPlaceholders());
	}

	protected String getResolvedKey(Placeholder placeholder, Properties properties, Set<String> resolving) {
		// The key is the placeholder minus suffix and prefix
		String key = getTrimmedPlaceholder(placeholder.getOriginalPlaceholder());

		// The key may have placeholders in it
		return replacePlaceholders(key, properties, resolving);
	}

	/**
	 * Our base case is a placeholder that does not contain nested placeholders
	 * 
	 * @param placeholder
	 * @param properties
	 */
	protected void doBaseCase(Placeholder placeholder, Properties properties, Set<String> resolving) {
		String expandedPlaceholder = placeholder.getOriginalPlaceholder();
		resolve(placeholder, properties, resolving, expandedPlaceholder);
	}

	protected String getValue(String key, Placeholder placeholder, Properties properties, Set<String> resolving) {
		// Get a value for the key
		String value = properties.getProperty(key);

		// If there is no value, use the original placeholder string ie ${foo}
		if (value == null) {
			return placeholder.getOriginalPlaceholder();
		}

		// The value may have placeholders in it
		return replacePlaceholders(value, properties, resolving);
	}

	protected void resolve(Placeholder placeholder, Properties properties, Set<String> resolving,
			String expandedPlaceholder) {
		String key = getResolvedKey(placeholder, properties, resolving);
		String value = getValue(key, placeholder, properties, resolving);

		placeholder.setExpandedPlaceholder(expandedPlaceholder);
		placeholder.setValue(value);

		// This placeholder is now "resolved". Resolved means there are no more nested placeholders to
		// expand and we have obtained a value for the placeholder
		placeholder.setResolved(true);
	}

	/**
	 * Recursion is used since placeholders may contain nested placeholders. The logic here recurses down the nested
	 * placeholder chain until it arrives at a placeholder that does not contain nested placeholders. A value is
	 * obtained for this "base" placeholder. The logic then recurses back up the chain. The value is substituted into
	 * the placeholder string (aka "the placeholder is expanded"). eg ${${foo}.${bar}} is now ${<fooValue>.<barValue>}.
	 * Once expansion has taken place, a value can be looked up for the placeholder
	 * 
	 * @param placeholder
	 * @param properties
	 */
	protected void doRecursion(Placeholder placeholder, Properties properties, Set<String> resolving) {
		String source = placeholder.getOriginalPlaceholder();
		String expandedPlaceholder = getExpandedString(source, placeholder, properties, resolving);
		resolve(placeholder, properties, resolving, expandedPlaceholder);
	}

	protected String getExpandedString(String source, Placeholder placeholder, Properties properties,
			Set<String> resolving) {
		return replacePlaceholders(source, placeholder.getPlaceholders(), properties, resolving);
	}

	public Properties resolvePlaceholders(Properties properties) {
		Properties resolvedProperties = new Properties();
		List<String> keys = new ArrayList<String>(properties.stringPropertyNames());
		Collections.sort(keys);
		for (String key : keys) {
			String resolvedKey = resolvePlaceholders(key, properties);
			String resolvedValue = resolvePlaceholders(properties.getProperty(key), properties);
			resolvedProperties.setProperty(resolvedKey, resolvedValue);
		}
		return resolvedProperties;
	}

	protected String replacePlaceholders(String source, List<Placeholder> placeholders, Properties properties,
			Set<String> resolving) {
		StringBuilder sb = new StringBuilder(source);
		int offset = 0;
		for (Placeholder placeholder : placeholders) {
			resolvePlaceholder(placeholder, properties, resolving);
			int startIndex = placeholder.getStartIndex() + offset;
			int endIndex = placeholder.getEndIndex() + offset;
			sb.replace(startIndex, endIndex, placeholder.getValue());
			int diff = placeholder.getValue().length() - placeholder.getOriginalPlaceholder().length();
			offset += diff;
		}
		return sb.toString();
	}

	public String resolvePlaceholders(String source, Properties properties) {
		return replacePlaceholders(source, getPlaceholders(source), properties, new HashSet<String>());
	}

	protected String replacePlaceholders(String source, Properties properties, Set<String> resolving) {
		return replacePlaceholders(source, getPlaceholders(source), properties, resolving);
	}

	protected void circularReferenceCheck(Placeholder placeholder, Set<String> resolving) {
		boolean added = resolving.add(placeholder.getOriginalPlaceholder());

		if (!added) {
			// Can't ask to resolve a placeholder we are already resolving
			throw new IllegalArgumentException("Circular reference detected on " + placeholder.getOriginalPlaceholder());
		}
	}

	protected void resolvePlaceholder(Placeholder placeholder, Properties properties, Set<String> resolving) {
		// Make sure we are not in an infinite loop
		circularReferenceCheck(placeholder, resolving);

		if (isBaseCase(placeholder)) {
			// Handle our base case
			doBaseCase(placeholder, properties, resolving);
		} else {
			// Recurse to handle nested placeholders
			doRecursion(placeholder, properties, resolving);
		}

		// It is resolved, remove it from the Set
		resolving.remove(placeholder.getOriginalPlaceholder());
	}

	protected List<Placeholder> getPlaceholders(String source) {
		return getPlaceholders(source, 0);
	}

	protected List<Placeholder> getPlaceholders(String source, int fromIndex) {
		// Locate the first occurrence of our prefix
		int startIndex = source.indexOf(this.placeholderPrefix, fromIndex);
		List<Placeholder> placeholders = new ArrayList<Placeholder>();

		// While there is at least one prefix remaining
		while (startIndex != -1) {
			// Get a placeholder object
			Placeholder placeholder = getPlaceholder(source, startIndex);

			// No more placeholders
			if (placeholder == null) {
				return placeholders;
			}

			// Add our placeholder to the list
			placeholders.add(placeholder);

			// Start looking again *after* the end of the placeholder we just found
			int newFromIndex = placeholder.getEndIndex();

			// Attempt to locate another prefix
			startIndex = source.indexOf(this.placeholderPrefix, newFromIndex);
		}
		return placeholders;
	}

	protected Placeholder getPlaceholder(String source, int startIndex) {
		int suffixLength = this.placeholderSuffix.length();
		int endIndex = findPlaceholderEndIndex(source, startIndex);
		if (endIndex == -1) {
			// No more placeholders remain
			return null;
		} else {
			endIndex = endIndex + suffixLength;
		}
		// The placeholder exactly as it appears in the source string eg "${foo}"
		String originalPlaceholder = source.substring(startIndex, endIndex);

		// Skip past the prefix
		int fromIndex = this.placeholderPrefix.length();

		// Recursive call, placeholders may contain nested placeholders eg ${${foo}.${bar}}
		List<Placeholder> placeholders = getPlaceholders(originalPlaceholder, fromIndex);

		// Populate a placeholder object
		Placeholder placeholder = new Placeholder();
		placeholder.setStartIndex(startIndex);
		placeholder.setEndIndex(endIndex);
		placeholder.setOriginalPlaceholder(originalPlaceholder);
		placeholder.setPlaceholders(placeholders);
		return placeholder;
	}

	protected boolean haveArrivedAtSuffix(CharSequence buf, int index) {
		return StringUtils.substringMatch(buf, index, this.placeholderSuffix);
	}

	protected boolean haveArrivedAtPrefix(CharSequence buf, int index) {
		return StringUtils.substringMatch(buf, index, this.simplePrefix);
	}

	/**
	 * Given a buffer and a startingIndex, find the suffix that matches our prefix. The default prefix is "${" and the
	 * default suffix is "}"
	 */
	protected int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
		// Skip past the prefix
		int index = startIndex + this.placeholderPrefix.length();

		// Track nested place holders as we encounter them
		int nestedPlaceHolderCount = 0;

		// Iterate through the buffer looking for the closing bracket that matches our opening bracket
		while (index < buf.length()) {
			if (haveArrivedAtSuffix(buf, index)) {
				// We aren't nested, return the index
				if (nestedPlaceHolderCount == 0) {
					return index;
				}

				// Decrement the nested count
				nestedPlaceHolderCount--;

				// Skip past this nested suffix
				index = index + this.placeholderSuffix.length();

			} else if (haveArrivedAtPrefix(buf, index)) {
				// Increment the counter
				nestedPlaceHolderCount++;

				// Skip past the nested prefix
				index = index + this.simplePrefix.length();
			} else {
				// We are not on a prefix or a suffix, keep searching
				index++;
			}
		}

		// We never found a suffix to match our prefix
		return -1;
	}

}
