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
import org.springframework.util.StringUtils;

/**
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * <code>${name}</code>. Using <code>PropertyPlaceholderHelper</code> these placeholders can be substituted for
 * user-supplied values.
 * <p>
 * Values for substitution can be supplied using a {@link Properties} instance or using a {@link PlaceholderResolver}.
 * 
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 3.0
 */
public class PPH3 {

	private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}

	private final String placeholderPrefix;

	private final String placeholderSuffix;

	private final String simplePrefix;

	private final String valueSeparator;

	private final boolean ignoreUnresolvablePlaceholders;

	public PPH3() {
		this(true);
	}
	public PPH3(boolean ignoreUnresolvablePlaceholders) {
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
	public PPH3(String placeholderPrefix, String placeholderSuffix) {
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
	public PPH3(String placeholderPrefix, String placeholderSuffix, String valueSeparator,
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
		Assert.notNull(value, "Argument 'value' must not be null.");
		logger.info("Examining " + value);
		return parseStringValue(value, properties, new HashSet<String>());
	}

	protected String parseStringValue(String strVal, Properties properties, Set<String> visitedPlaceholders) {
		logger.debug("Parsing " + strVal);
		StringBuilder buf = new StringBuilder(strVal);
		int startIndex = strVal.indexOf(this.placeholderPrefix);
		while (startIndex != -1) {
			startIndex = processString(startIndex, properties, visitedPlaceholders, buf);
		}
		return buf.toString();
	}

	protected int processString(int startIndex, Properties properties, Set<String> visitedPlaceholders,
			StringBuilder buf) {

		// Locate the index of the end placeholder
		int endIndex = findPlaceholderEndIndex(buf, startIndex);

		// There is no terminating placeholder, we are done
		if (endIndex == -1) {
			return -1;
		}

		// Trim prefix and suffix off of the placeholder
		String originalKey = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);

		// Add this placeholder to the set
		logger.info("Adding " + originalKey + " to visited keys");
		boolean added = visitedPlaceholders.add(originalKey);

		if (!added) {
			throw new IllegalArgumentException("Circular reference '" + originalKey + "' in property definitions");
		}

		// Recursive invocation, resolve any placeholders inside the key
		String resolvedKey = parseStringValue(originalKey, properties, visitedPlaceholders);

		// Obtain a value for the resolved placeholder
		String value = getValue(resolvedKey, properties);

		logger.debug("Processing " + resolvedKey + "=" + value);
		int bufIndex = processValue(value, properties, visitedPlaceholders, startIndex, endIndex, resolvedKey, buf);

		logger.info("Removing " + originalKey + " from visited keys");
		visitedPlaceholders.remove(originalKey);

		return bufIndex;
	}

	/**
	 * Attempt to get a value for this placeholder
	 */
	protected String getValue(String placeholder, Properties properties) {
		// If the resolver gives us something, we're done
		String propVal = properties.getProperty(placeholder);
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
		propVal = properties.getProperty(actualPlaceholder);

		// If the resolver found something, use it
		if (propVal != null) {
			return propVal;
		} else {
			// Otherwise return the default value
			return defaultValue;
		}
	}

	protected int processValue(String value, Properties properties, Set<String> visitedPlaceholders, int startIndex,
			int endIndex, String key, StringBuilder buf) {

		// We could not locate a value and we are not ignoring unresolved placeholders
		if (value == null && !ignoreUnresolvablePlaceholders) {
			throw new IllegalArgumentException("Could not resolve placeholder '" + key + "'");
		}

		// The value is null, but we can ignore the fact that we encountered an unresolved placeholder
		if (value == null) {
			// Leave the unresolved placeholder intact and continue processing the rest of the string
			return buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
		}

		// Recursive invocation, resolve any placeholders inside the value
		value = parseStringValue(value, properties, visitedPlaceholders);

		// log that we resolved the placeholder
		if (logger.isInfoEnabled()) {
			logger.debug("Resolved key '" + key + "' to '" + value + "'");
		}

		// Replace the placeholder with the value
		buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), value);

		// Move past the string we just replaced
		return buf.indexOf(this.placeholderPrefix, startIndex + value.length());
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
