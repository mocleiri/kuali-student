/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kuali.spring.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringUtils;

/**
 * 
 */
public class PropertyPlaceholderHelper {

	private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}

	private String placeholderPrefix;
	private String placeholderSuffix;
	private String simplePrefix;
	private String valueSeparator;
	private boolean ignoreUnresolvablePlaceholders;

	public PropertyPlaceholderHelper() {
		this(false);
	}

	public PropertyPlaceholderHelper(boolean ignoreUnresolvablePlaceholders) {
		this(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null, ignoreUnresolvablePlaceholders);
	}

	/**
	 * Creates a new <code>PropertyPlaceholderHelper</code> that uses the supplied prefix and suffix. Unresolvable
	 * placeholders are ignored.
	 * 
	 * @param placeholderPrefix
	 *            the prefix that denotes the start of a placeholder.
	 * @param placeholderSuffix
	 *            the suffix that denotes the end of a placeholder.
	 */
	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, true);
	}

	/**
	 * Creates a new <code>PropertyPlaceholderHelper</code> that uses the supplied prefix and suffix.
	 * 
	 * @param placeholderPrefix
	 *            the prefix that denotes the start of a placeholder
	 * @param placeholderSuffix
	 *            the suffix that denotes the end of a placeholder
	 * @param valueSeparator
	 *            the separating character between the placeholder variable and the associated default value, if any
	 * @param ignoreUnresolvablePlaceholders
	 *            indicates whether unresolvable placeholders should be ignored (<code>true</code>) or cause an
	 *            exception (<code>false</code>).
	 */
	public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, String valueSeparator,
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
	 * Replaces all placeholders of format <code>${name}</code> with the corresponding property from the supplied
	 * {@link Properties}.
	 * 
	 * @param value
	 *            the value containing the placeholders to be replaced.
	 * @param properties
	 *            the <code>Properties</code> to use for replacement.
	 * @return the supplied value with placeholders replaced inline.
	 */
	public String replacePlaceholders(String value, final Properties properties) {
		Assert.notNull(properties, "Argument 'properties' must not be null.");
		return replacePlaceholders(value, new PlaceholderResolver() {
			public String resolvePlaceholder(String placeholderName) {
				return properties.getProperty(placeholderName);
			}
		});
	}

	/**
	 * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied
	 * {@link PlaceholderResolver}.
	 * 
	 * @param value
	 *            the value containing the placeholders to be replaced.
	 * @param placeholderResolver
	 *            the <code>PlaceholderResolver</code> to use for replacement.
	 * @return the supplied value with placeholders replaced inline.
	 */
	public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
		Assert.notNull(value, "Argument 'value' must not be null.");
		return parseStringValue(value, placeholderResolver, new HashSet<String>());
	}

	protected String parseStringValue(String strVal, PlaceholderResolver resolver, Set<String> visitedPlaceholders) {
		logger.debug("Parsing " + strVal);
		StringBuilder buf = new StringBuilder(strVal);
		int startIndex = strVal.indexOf(this.placeholderPrefix);
		while (startIndex != -1) {
			startIndex = processString(new ProcessStringContext(resolver, visitedPlaceholders, startIndex, buf));
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
		logger.debug("Adding " + originalKey + " to visited keys");
		boolean added = ctx.getVisitedPlaceholders().add(originalKey);

		// Check to make sure we aren't in an infinite loop
		if (!added) {
			throw new IllegalArgumentException("Circular reference '" + originalKey + "' in property definitions");
		}

		// Recursive invocation, resolve any placeholders inside the key
		String resolvedKey = parseStringValue(originalKey, ctx.getResolver(), ctx.getVisitedPlaceholders());

		// Obtain a value for the resolved key
		String value = getValue(resolvedKey, ctx.getResolver());

		logger.debug("Processing " + resolvedKey + "=" + value);
		int bufIndex = processValue(ctx, value, endIndex, resolvedKey);

		logger.debug("Removing " + originalKey + " from visited keys");
		ctx.getVisitedPlaceholders().remove(originalKey);

		return bufIndex;
	}

	/**
	 * Attempt to get a value for this placeholder
	 */
	protected String getValue(String placeholder, PlaceholderResolver resolver) {
		// If the resolver gives us something, we're done
		String propVal = resolver.resolvePlaceholder(placeholder);
		if (propVal != null) {
			return propVal;
		}

		// If they haven't specified a valueSeparator, we're done
		if (this.valueSeparator == null) {
			return null;
		}

		// If they supplied a valueSeparator but this placeholder isn't using it, we're done
		int separatorIndex = placeholder.indexOf(this.valueSeparator);
		if (separatorIndex == -1) {
			return null;
		}

		/**
		 * Handle the case when placeholders are supplied using the syntax:<br>
		 * 
		 * ${jdbc.vendor=oracle}
		 * 
		 * The user wants the placeholder set to "oracle" if a "jdbc.vendor" property cannot be located
		 */
		// Extract "jdbc.vendor"
		String actualPlaceholder = placeholder.substring(0, separatorIndex);

		// Extract the default value they supplied
		String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());

		// Give the resolver a chance to locate a value
		propVal = resolver.resolvePlaceholder(actualPlaceholder);

		// If the resolver found something, use it
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
			// Leave the unresolved placeholder intact and continue processing the rest of the string
			return ctx.getBuffer().indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
		}

		// Recursive invocation, resolve any placeholders inside the value
		value = parseStringValue(value, ctx.getResolver(), ctx.getVisitedPlaceholders());

		// log that we resolved the placeholder
		logger.debug("Resolved key '" + key + "' to '" + value + "'");

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

}
