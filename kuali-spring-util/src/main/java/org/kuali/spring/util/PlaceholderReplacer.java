package org.kuali.spring.util;

import java.util.HashMap;
import java.util.HashSet;
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
public class PlaceholderReplacer {

	final Logger logger = LoggerFactory.getLogger(PlaceholderReplacer.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

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

	public PlaceholderReplacer() {
		this(false);
	}

	public PlaceholderReplacer(boolean ignoreUnresolvablePlaceholders) {
		this(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null, ignoreUnresolvablePlaceholders);
	}

	public PlaceholderReplacer(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, true);
	}

	public PlaceholderReplacer(String placeholderPrefix, String placeholderSuffix, String valueSeparator,
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

	public String replacePlaceholders(String value, Properties properties) {
		Assert.notNull(properties, "Argument 'properties' must not be null.");
		PropertyRetriever retriever = new PropertiesRetriever(properties);
		return replacePlaceholders(value, retriever);
	}

	public String replacePlaceholders(String value, PropertyRetriever retriever) {
		Assert.notNull(value, "Argument 'value' must not be null.");
		return parseStringValue(value, retriever, new HashSet<String>());
	}

	protected String parseStringValue(String strVal, PropertyRetriever retriever, Set<String> visitedPlaceholders) {
		StringBuilder buf = new StringBuilder(strVal);
		int startIndex = strVal.indexOf(this.placeholderPrefix);
		if (startIndex == -1) {
			logger.trace("Skip parsing.  Prefix '{}' not detected in [{}]", placeholderPrefix, strVal);
			return buf.toString();
		}
		logger.trace("Parsing [{}]", strVal);
		while (startIndex != -1) {
			startIndex = processString(new ProcessStringContext(retriever, visitedPlaceholders, startIndex, buf));
		}
		return buf.toString();
	}

	protected int processString(ProcessStringContext ctx) {

		// Locate the index of the end placeholder
		int endIndex = findPlaceholderEndIndex(ctx.getBuffer(), ctx.getStartIndex());

		// There is no terminating placeholder, we are done
		if (endIndex == -1) {
			return -1;
		}

		// Trim prefix and suffix off of the placeholder
		String originalKey = ctx.getBuffer().substring(ctx.getStartIndex() + this.placeholderPrefix.length(), endIndex);

		// Add this placeholder to the set
		logger.trace("Adding '{}' to visited keys", originalKey);
		boolean added = ctx.getVisitedPlaceholders().add(originalKey);

		// Check to make sure we aren't in an infinite loop
		if (!added) {
			throw new IllegalArgumentException("Circular reference '" + originalKey + "' in property definitions");
		}

		// Recursive invocation, replace any placeholders inside the key
		String finalKey = parseStringValue(originalKey, ctx.getRetriever(), ctx.getVisitedPlaceholders());

		// Obtain a value for key now that placeholders in the key have been replaced

		// Check the resolved cache first
		String value = resolvedCache.getProperty(finalKey);

		// Nothing in the cache
		if (value == null) {
			value = getValue(finalKey, ctx.getRetriever());
		} else {
			logger.trace("Resolved the value for '{}' from cache", finalKey);
		}

		logger.trace("Processing value [{}]", value);
		int bufIndex = processValue(ctx, value, endIndex, finalKey);

		logger.trace("Removing '{}' from visited keys", originalKey);
		ctx.getVisitedPlaceholders().remove(originalKey);

		return bufIndex;
	}

	/**
	 * Attempt to get a value for this placeholder
	 */
	protected String getValue(String key, PropertyRetriever retriever) {
		// If the retriever gives us something, we're done
		String propVal = retriever.retrieveProperty(key);
		if (propVal != null) {
			return propVal;
		}

		// If they haven't specified a valueSeparator, we're done
		if (this.valueSeparator == null) {
			return null;
		}

		// If they supplied a valueSeparator but this key isn't using it, we are done
		int separatorIndex = key.indexOf(this.valueSeparator);
		if (separatorIndex == -1) {
			return null;
		}

		// Handle the case when placeholders are supplied using the syntax:<br>
		// ${jdbc.vendor:oracle}
		// The user wants the placeholder set to "oracle" if a "jdbc.vendor" property cannot be located
		// Extract "jdbc.vendor"
		String actualKey = key.substring(0, separatorIndex);

		// Extract the default value they supplied
		String defaultValue = key.substring(separatorIndex + this.valueSeparator.length());

		// Give the retriever a chance to locate a value
		propVal = retriever.retrieveProperty(actualKey);

		// If the retriever found something, use it
		if (propVal != null) {
			return propVal;
		} else {
			// Otherwise return the default value
			return defaultValue;
		}
	}

	protected int processValue(ProcessStringContext ctx, String value, int endIndex, String key) {

		// We could not locate a value and we are not ignoring unresolved placeholders
		if (value == null && !ignoreUnresolvablePlaceholders) {
			throw new IllegalArgumentException("Could not resolve placeholder '" + key + "'");
		}

		// The value is null, but we can ignore the fact that we encountered an unresolved placeholder
		if (value == null) {
			logger.trace("Ignoring unresolvable placeholder for '" + key + "'");
			// Leave the unresolved placeholder intact and continue processing the rest of the string
			return ctx.getBuffer().indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
		}

		// Recursive invocation, resolve any placeholders inside the value
		value = parseStringValue(value, ctx.getRetriever(), ctx.getVisitedPlaceholders());

		// log that we resolved the placeholder
		logger.trace("Resolved key '{}' to [{}]", key, value);

		// Cache the key->value pair now that all the placeholders have been removed
		resolvedCache.setProperty(key, value);
		logger.trace("Caching property '{}'", key);

		// Replace the placeholder with the value
		ctx.getBuffer().replace(ctx.getStartIndex(), endIndex + this.placeholderSuffix.length(), value);

		// Move past the string we just replaced
		int newIndex = ctx.getBuffer().indexOf(this.placeholderPrefix, ctx.getStartIndex() + value.length());

		// Return the new index
		return newIndex;
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

	public String getSimplePrefix() {
		return simplePrefix;
	}

	public void setSimplePrefix(String simplePrefix) {
		this.simplePrefix = simplePrefix;
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

	public Properties getResolvedCache() {
		return resolvedCache;
	}

	public void setResolvedCache(Properties resolvedCache) {
		this.resolvedCache = resolvedCache;
	}

}
