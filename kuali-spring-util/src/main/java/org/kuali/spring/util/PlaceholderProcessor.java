package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * This class contains logic for parsing/expanding placeholders in a string. For example, given the phrase:<br>
 * <code>
 * The quick brown fox jumped over the lazy dog.
 * </code>
 * 
 * We'll replace a few words with placeholders:<br>
 * <code>
 * The quick ${${animal.1}.color} ${animal.1} jumped over the ${${animal.2}.adjective} ${animal.2}.
 * </code>
 * 
 * The string is parsed to obtain Placeholder objects. In this case there are 4 top level Placeholder objects
 * 
 * 1 - ${${animal.1}.color}<br>
 * 2 - ${animal.1}<br>
 * 3 - ${${animal.2}.adjective}<br>
 * 4 - ${animal.2}<br>
 * 
 * Placeholders #1 and #3 are special. They contain nested placeholders. When the parsing logic encounters a placeholder
 * it uses recursion to parse its own substring to find any placeholders inside itself. When the parsing logic goes to
 * create the placeholder objects corresponding to #1 and #3, it re-parses the substrings ${${animal.1}.color} and
 * ${${animal.2}.adjective}. It creates placeholder objects for ${animal.1} and ${animal.2}. These placeholders are
 * attached to the placeholder as "nested" or "child" placeholders.
 */
public class PlaceholderProcessor {

	final Logger logger = LoggerFactory.getLogger(PlaceholderProcessor.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);
	public static final boolean DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS = false;
	public static final boolean DEFAULT_IS_SORT = true;

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}

	PropertyLogger plogger = new DefaultPropertyLogger();
	String placeholderPrefix;
	String placeholderSuffix;
	String valueSeparator;
	boolean ignoreUnresolvablePlaceholders;
	boolean sort = DEFAULT_IS_SORT;
	String simplePrefix;

	public PlaceholderProcessor() {
		this(DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
	}

	public PlaceholderProcessor(boolean ignoreUnresolvablePlaceholders) {
		this(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null, ignoreUnresolvablePlaceholders);
	}

	public PlaceholderProcessor(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
	}

	public PlaceholderProcessor(String placeholderPrefix, String placeholderSuffix, String valueSeparator,
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

	/**
	 * Return true if the collection is null or has no elements
	 * 
	 * @param c
	 * @return
	 */
	protected boolean isEmpty(Collection<?> c) {
		return c == null || c.size() == 0;
	}

	/**
	 * Trim the prefix and suffix off of the placeholder ie change ${foo}->foo
	 * 
	 * @param placeholder
	 * @return
	 */
	protected String getTrimmedPlaceholder(String placeholder) {
		int prefixLength = this.placeholderPrefix.length();
		int suffixLength = this.placeholderSuffix.length();
		int beginIndex = prefixLength;
		int endIndex = placeholder.length() - suffixLength;
		return placeholder.substring(beginIndex, endIndex);
	}

	/**
	 * True if the placeholder does not contain any nested placeholders, false otherwise
	 * 
	 * @param placeholder
	 * @return
	 */
	protected boolean isBaseCase(Placeholder placeholder) {
		return isEmpty(placeholder.getPlaceholders());
	}

	/**
	 * The key is the placeholder string itself after any nested placeholders have been expanded
	 */
	protected String getResolvedKey(Placeholder placeholder, ValueRetriever retriever, Set<String> resolving) {
		// The key is the placeholder minus suffix and prefix
		String key = getTrimmedPlaceholder(placeholder.getText());

		// The key may have placeholders in it
		return replacePlaceholders(key, retriever, resolving);
	}

	/**
	 * The base case is a placeholder that does not contain nested placeholders.
	 * 
	 * @param placeholder
	 * @param properties
	 */
	protected void doBaseCase(Placeholder placeholder, ValueRetriever retriever, Set<String> resolving) {
		resolve(placeholder, retriever, resolving, placeholder.getText());
	}

	/**
	 * Handle placeholders that have default values supplied with them ie ${jdbc.vendor=mysql}
	 * 
	 * @param key
	 * @param properties
	 * @return
	 */
	protected String getDefaultValue(String key, ValueRetriever retriever) {
		// If they haven't specified a valueSeparator, we're done
		if (this.valueSeparator == null) {
			return null;
		}

		// If they supplied a valueSeparator but this key isn't using it, we are done
		int separatorIndex = key.indexOf(this.valueSeparator);
		if (separatorIndex == -1) {
			return null;
		}

		// Handle the case when placeholders are supplied using the syntax: ${jdbc.vendor=mysql}
		// The user wants the placeholder set to "mysql" if a "jdbc.vendor" property cannot be located
		// Extract "jdbc.vendor"
		String actualKey = key.substring(0, separatorIndex);

		// Extract the default value they supplied
		String defaultValue = key.substring(separatorIndex + this.valueSeparator.length());

		// Give the retriever a chance to locate a value
		String value = retriever.retrieveValue(actualKey);

		// If the retriever found something, use it
		if (value != null) {
			return value;
		} else {
			// Otherwise return the default value
			return defaultValue;
		}
	}

	/**
	 * Get a value for this key
	 * 
	 * @param key
	 * @param placeholder
	 * @param properties
	 * @param resolving
	 * @return
	 */
	protected String getResolvedValue(String key, Placeholder placeholder, ValueRetriever retriever,
			Set<String> resolving) {
		// Get a value for the key
		String value = retriever.retrieveValue(key);

		// There might be a default value
		if (value == null) {
			value = getDefaultValue(key, retriever);
		}

		// We found something
		if (value != null) {
			// The value may have placeholders in it
			return replacePlaceholders(value, retriever, resolving);
		}

		// If we get here, value is null. We could not get a value for this placeholder
		if (!this.ignoreUnresolvablePlaceholders) {
			// We couldn't resolve the placeholder and we need to fail fast
			throw new IllegalArgumentException("Could not resolve a value for " + placeholder.getText());
		} else {
			// If we couldn't get a value, and we are ignoring unresolvable placeholders, leave the original placeholder
			// string intact (ie ${foo}) and let processing continue
			return placeholder.getText();
		}
	}

	/**
	 * Resolve the placeholder by filling in the "value" and "expandedPlaceholder" properties and setting "resolved" to
	 * true
	 * 
	 * @param placeholder
	 * @param properties
	 * @param resolving
	 * @param expandedPlaceholder
	 */
	protected void resolve(Placeholder placeholder, ValueRetriever retriever, Set<String> resolving,
			String expandedPlaceholder) {
		String key = getResolvedKey(placeholder, retriever, resolving);
		String value = getResolvedValue(key, placeholder, retriever, resolving);

		placeholder.setExpandedText(expandedPlaceholder);
		placeholder.setValue(value);

		// This placeholder is now "resolved". Resolved means there are no more nested placeholders to
		// expand and we have obtained a value
		placeholder.setResolved(true);
	}

	/**
	 * Recursion is used since placeholders may contain nested placeholders. The logic here recurses down the nested
	 * placeholder chain until it arrives at a placeholder that does not contain nested placeholders. A value is
	 * obtained for this "base" placeholder. The logic then recurses back up the chain and the value is substituted into
	 * the placeholder string (aka "the placeholder is expanded").<br>
	 * 
	 * For example:<br>
	 * Given a properties file with the entries:<br>
	 * foo=a<br>
	 * bar=b<br>
	 * a.b=purple<br>
	 * 
	 * The nested placeholder ${${foo}.${bar}} will be expanded to ${a.b} After expansion has taken place, the value
	 * "purple" will be looked up and stored in the "value" property of the corresponding placeholder
	 */
	protected void doRecursion(Placeholder placeholder, ValueRetriever retriever, Set<String> resolving) {
		// Our source string is the placeholder string itself
		// eg ${${foo}.${bar}}
		String source = placeholder.getText();
		// The "children" of this placeholder are ${foo} and ${bar}
		List<Placeholder> children = placeholder.getPlaceholders();
		// If "foo" is "a" and "bar" is "b" the expanded placeholder for ${${foo}.${bar}} will be ${a.b}
		String expandedPlaceholder = replacePlaceholders(source, children, retriever, resolving);
		// Resolve the placeholder by finding a value for the property "a.b"
		resolve(placeholder, retriever, resolving, expandedPlaceholder);
	}

	protected String replacePlaceholders(String source, List<Placeholder> placeholders, ValueRetriever retriever,
			Set<String> resolving) {
		StringBuilder sb = new StringBuilder(source);
		int offset = 0;
		for (Placeholder placeholder : placeholders) {
			resolvePlaceholder(placeholder, retriever, resolving);
			int startIndex = placeholder.getStartIndex() + offset;
			int endIndex = placeholder.getEndIndex() + offset;
			sb.replace(startIndex, endIndex, placeholder.getValue());
			int diff = placeholder.getValue().length() - placeholder.getText().length();
			offset += diff;
		}
		return sb.toString();
	}

	public String replacePlaceholders(String source, ValueRetriever retriever) {
		return replacePlaceholders(source, getPlaceholders(source), retriever, new HashSet<String>());
	}

	protected String replacePlaceholders(String source, ValueRetriever retriever, Set<String> resolving) {
		return replacePlaceholders(source, getPlaceholders(source), retriever, resolving);
	}

	protected void circularReferenceCheck(Placeholder placeholder, Set<String> resolving) {
		logger.trace("Adding '{}' to the list of placeholders being resolved", placeholder.getText());
		boolean added = resolving.add(placeholder.getText());

		if (!added) {
			// If we get here, one of the placeholders we are trying to resolve contains a reference to another
			// placeholder that we are already trying to resolve. This can happen if someone does something like this
			// in a properties file:
			// a=${b}
			// b=${a}
			throw new IllegalArgumentException("Circular reference detected on " + placeholder.getText());
		}
	}

	protected void resolvePlaceholder(Placeholder placeholder, ValueRetriever retriever, Set<String> resolving) {
		// Make sure we are not in an infinite loop
		circularReferenceCheck(placeholder, resolving);

		if (isBaseCase(placeholder)) {
			// Handle our base case
			doBaseCase(placeholder, retriever, resolving);
		} else {
			// Recurse to handle nested placeholders
			doRecursion(placeholder, retriever, resolving);
		}

		logger.trace("Removing '{}' from the list of placeholders being resolved", placeholder.getText());
		// It is resolved, remove it from the Set
		resolving.remove(placeholder.getText());
	}

	protected List<Placeholder> getPlaceholders(String source) {
		// Locate the first occurrence of our prefix
		int startIndex = source.indexOf(this.placeholderPrefix);
		if (startIndex == -1) {
			logger.trace("Skip parsing.  No placeholders found in [{}]", source);
		}

		// Storage for the placeholders we find
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

			// Start looking again, skipping past the placeholder we just found
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
		// The placeholder exactly as it appears in the source string ie ${foo}
		String originalPlaceholder = source.substring(startIndex, endIndex);

		// Trim off prefix and suffix so we can check for nested placeholders
		String trimmedPlaceholder = getTrimmedPlaceholder(originalPlaceholder);

		// Recursive call, placeholders may contain nested placeholders ie ${${foo}.${bar}}
		List<Placeholder> placeholders = getPlaceholders(trimmedPlaceholder);

		// Populate a placeholder object
		Placeholder placeholder = new Placeholder();
		placeholder.setStartIndex(startIndex);
		placeholder.setEndIndex(endIndex);
		placeholder.setText(originalPlaceholder);
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

	public String getPlaceholderPrefix() {
		return placeholderPrefix;
	}

	public void setPlaceholderPrefix(String placeholderPrefix) {
		this.placeholderPrefix = placeholderPrefix;
	}

	public String getPlaceholderSuffix() {
		return placeholderSuffix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix) {
		this.placeholderSuffix = placeholderSuffix;
	}

	public String getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}

	public boolean isIgnoreUnresolvablePlaceholders() {
		return ignoreUnresolvablePlaceholders;
	}

	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public PropertyLogger getPlogger() {
		return plogger;
	}

	public void setPlogger(PropertyLogger plogger) {
		this.plogger = plogger;
	}

	public String getSimplePrefix() {
		return simplePrefix;
	}

	public void setSimplePrefix(String simplePrefix) {
		this.simplePrefix = simplePrefix;
	}

}
