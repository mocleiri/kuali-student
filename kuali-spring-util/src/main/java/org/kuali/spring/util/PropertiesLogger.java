package org.kuali.spring.util;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLogger {
	final Logger logger = LoggerFactory.getLogger(PropertiesLogger.class);

	// Matches any string containing "password" (case insensitive)
	public static final String DEFAULT_MASK_EXPRESSION = ".*((?i)password).*";
	public static final String DEFAULT_MASKED_VALUE = "******";
	public static final boolean DEFAULT_IS_MASK_PROPERTY_VALUES = true;
	public static final boolean DEFAULT_IS_FLATTEN_PROPERTY_VALUES = false;
	public static final boolean DEFAULT_IS_TRIM_PROPERTY_VALUES = false;
	public static final String DEFAULT_REPLACEMENT_STRING = " ";

	// If true, replace \n and \r when logging values
	boolean flattenPropertyValues = DEFAULT_IS_FLATTEN_PROPERTY_VALUES;
	// The value to replace linefeeds with
	String linefeedReplacement = DEFAULT_REPLACEMENT_STRING;
	// The value to replace carriage returns with
	String carriageReturnReplacement = DEFAULT_REPLACEMENT_STRING;
	// If true, call trim() on property values before logging them
	boolean trimPropertyValues = DEFAULT_IS_TRIM_PROPERTY_VALUES;
	// If true, mask values for keys that match the maskExpression
	boolean maskPropertyValues = DEFAULT_IS_MASK_PROPERTY_VALUES;
	// Regular expression to compare property keys against
	String maskExpression = DEFAULT_MASK_EXPRESSION;
	// The value to log in place of the actual value if we need to mask it
	String maskValue = DEFAULT_MASKED_VALUE;
	// Compiled representation of the regular expression
	Pattern pattern = Pattern.compile(getMaskExpression());

	/**
	 * This setter also invokes Pattern.compile(maskExpression)
	 * 
	 * @param maskExpression
	 */
	public void setMaskExpression(String maskExpression) {
		this.maskExpression = maskExpression;
		this.pattern = Pattern.compile(maskExpression);
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

	public String getPropertyValue(String key, String value) {
		if (flattenPropertyValues) {
			value = value.replace("\n", getLinefeedReplacement());
			value = value.replace("\r", getCarriageReturnReplacement());
		}
		if (trimPropertyValues) {
			value = value.trim();
		}
		if (!maskPropertyValues) {
			return value;
		}
		Matcher matcher = pattern.matcher(key);
		boolean match = matcher.matches();
		if (match) {
			return maskValue;
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

	public String getMaskExpression() {
		return maskExpression;
	}

	public String getMaskValue() {
		return maskValue;
	}

	public void setMaskValue(String maskValue) {
		this.maskValue = maskValue;
	}

	public Pattern getPattern() {
		return pattern;
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

}
