package org.kuali.spring.util;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPropertyLogger implements PropertyLogger {
	final Logger logger = LoggerFactory.getLogger(DefaultPropertyLogger.class);

	// Matches any string containing "password" (case insensitive)
	public static final String DEFAULT_MASK_EXPRESSION = ".*((?i)password).*";
	public static final String DEFAULT_MASKED_VALUE = "******";
	public static final boolean DEFAULT_IS_MASK_PROPERTY_VALUES = true;
	public static final boolean DEFAULT_IS_FLATTEN_PROPERTY_VALUES = false;
	public static final boolean DEFAULT_IS_TRIM_PROPERTY_VALUES = false;
	// Space
	public static final String DEFAULT_REPLACEMENT_STRING = " ";
	// Carriage return
	public static final String CR = "\r";
	// Linefeed
	public static final String LF = "\n";

	// If true, replace \n and \r when logging values
	boolean flattenPropertyValues = DEFAULT_IS_FLATTEN_PROPERTY_VALUES;
	// The value to replace linefeeds with
	String linefeedReplacement = DEFAULT_REPLACEMENT_STRING;
	// The value to replace carriage returns with
	String carriageReturnReplacement = DEFAULT_REPLACEMENT_STRING;
	// If true, call trim() on property values before logging them
	boolean trimPropertyValues = DEFAULT_IS_TRIM_PROPERTY_VALUES;
	// If true, mask values for keys that match any of the maskExpressions
	boolean maskPropertyValues = DEFAULT_IS_MASK_PROPERTY_VALUES;
	// Regular expressions to compare property keys against
	String[] maskExpressions;
	// The value to log in place of the actual value if we need to mask it
	String maskValue = DEFAULT_MASKED_VALUE;
	// Compiled representation of the regular expression
	Pattern[] patterns;

	public DefaultPropertyLogger() {
		super();
		setMaskExpression(DEFAULT_MASK_EXPRESSION);
	}

	/**
	 * This setter invokes setMaskExpressions() - plural
	 * 
	 * @param maskExpression
	 */
	public void setMaskExpression(String maskExpression) {
		setMaskExpressions(new String[] { maskExpression });
	}

	/**
	 * This setter invokes Pattern.compile() for each maskExpression
	 * 
	 * @param maskExpressions
	 */
	public void setMaskExpressions(String[] maskExpressions) {
		this.maskExpressions = maskExpressions;
		this.patterns = new Pattern[maskExpressions.length];
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(maskExpressions[i]);
		}
	}

	public String getLogEntry(String key, String value) {
		return key + "=" + getPropertyValue(key, value);
	}

	public String getLogEntry(Properties properties) {
		return getLogEntry(properties, null);
	}

	protected boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		if (s.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public String getLogEntry(Properties properties, String comment) {
		StringBuilder sb = new StringBuilder();
		if (!isEmpty(comment)) {
			sb.append(comment + "\n");
		}
		if (properties == null || properties.size() == 0) {
			sb.append("No properties to log\n");
			return sb.toString();
		}
		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			sortedProperties.put(key, value);
		}
		for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
			sb.append(getLogEntry(entry.getKey(), entry.getValue()) + "\n");
		}
		return sb.toString();
	}

	protected boolean isMatch(Pattern[] patterns, String key) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(key);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	public String getPropertyValue(String key, String value) {
		if (isFlattenPropertyValues()) {
			value = value.replace(LF, getLinefeedReplacement());
			value = value.replace(CR, getCarriageReturnReplacement());
		}
		if (isTrimPropertyValues()) {
			value = value.trim();
		}
		if (!isMaskPropertyValues()) {
			return value;
		}
		boolean match = isMatch(getPatterns(), key);
		if (match) {
			return getMaskValue();
		} else {
			return value;
		}
	}

	public boolean isFlattenPropertyValues() {
		return flattenPropertyValues;
	}

	public void setFlattenPropertyValues(boolean flattenPropertyValues) {
		this.flattenPropertyValues = flattenPropertyValues;
	}

	public boolean isMaskPropertyValues() {
		return maskPropertyValues;
	}

	public void setMaskPropertyValues(boolean maskPropertyValues) {
		this.maskPropertyValues = maskPropertyValues;
	}

	public String getMaskValue() {
		return maskValue;
	}

	public void setMaskValue(String maskValue) {
		this.maskValue = maskValue;
	}

	public String getLinefeedReplacement() {
		return linefeedReplacement;
	}

	public void setLinefeedReplacement(String linefeedReplacement) {
		this.linefeedReplacement = linefeedReplacement;
	}

	public String getCarriageReturnReplacement() {
		return carriageReturnReplacement;
	}

	public void setCarriageReturnReplacement(String carriageReturnReplacement) {
		this.carriageReturnReplacement = carriageReturnReplacement;
	}

	public boolean isTrimPropertyValues() {
		return trimPropertyValues;
	}

	public void setTrimPropertyValues(boolean trimPropertyValues) {
		this.trimPropertyValues = trimPropertyValues;
	}

	public String[] getMaskExpressions() {
		return maskExpressions;
	}

	public Pattern[] getPatterns() {
		return patterns;
	}

}
