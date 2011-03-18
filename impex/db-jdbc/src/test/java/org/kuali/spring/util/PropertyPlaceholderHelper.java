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
public class PropertyPlaceholderHelper {

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

	private final boolean ignoreUnresolvablePlaceholders;

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
	 *            exception (<code>false</code> ).
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
		return parseStringValue(value, properties, new HashSet<String>());
	}

	protected String parseStringValue(String strVal, Properties properties, Set<String> visitedPlaceholders) {
		int startIndex = strVal.indexOf(this.placeholderPrefix);
		if (startIndex == -1) {
			return strVal;
		}
		int endIndex = findPlaceholderEndIndex(strVal, startIndex);
		if (endIndex == -1) {
			return strVal;
		}
		StringBuilder buf = new StringBuilder(strVal);
		processString(startIndex, endIndex, properties, visitedPlaceholders, buf);
		return buf.toString();
	}

	protected void processString(int startIndex, int endIndex, Properties properties, Set<String> visitedKeys,
			StringBuilder buf) {
		String key = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
		if (!visitedKeys.add(key)) {
			throw new IllegalArgumentException("Circular placholder reference '" + key + "' in property definitions");
		}
		// Recursive invocation, parsing placeholders contained in the key.
		key = parseStringValue(key, properties, visitedKeys);
		String propVal = properties.getProperty(key);

		if (propVal == null && !ignoreUnresolvablePlaceholders) {
			throw new IllegalArgumentException("Could not resolve placeholder '" + key + "'");
		}
		// Recursive invocation, parsing placeholders contained in the property value
		propVal = parseStringValue(propVal, properties, visitedKeys);

		// Replace the placeholder with the property value
		buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
		if (logger.isTraceEnabled()) {
			logger.trace("Resolved placeholder '" + key + "'");
		}

		visitedKeys.remove(key);
	}

	protected boolean isSuffix(CharSequence buf, int index) {
		return StringUtils.substringMatch(buf, index, placeholderSuffix);
	}

	protected boolean isPrefix(CharSequence buf, int index) {
		return StringUtils.substringMatch(buf, index, simplePrefix);
	}

	private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
		// Skip past the prefix
		int index = startIndex + this.placeholderPrefix.length();

		// Not nested to start
		int nestedPlaceholderCount = 0;

		// While our index is smaller than the buffer size
		while (index < buf.length()) {
			// Have we arrived at a suffix?
			if (isSuffix(buf, index)) {
				// We are at a suffix but we might be nested
				if (nestedPlaceholderCount == 0) {
					// We aren't nested, we've found the end
					return index;
				}
				// Decrement our nested count
				nestedPlaceholderCount--;
				// Skip past this suffix
				index = index + this.placeholderSuffix.length();
			} else if (isPrefix(buf, index)) {
				// We've hit a prefix, increment our count
				nestedPlaceholderCount++;
				// Skip past the prefix
				index = index + this.simplePrefix.length();
			} else {
				// Move to the next character in the buffer
				index++;
			}
		}
		// We never found a suffix
		return -1;
	}

}
