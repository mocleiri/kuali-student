package org.kuali.spring.util;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLogger {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceholderConfigurer.class);
	// If true, strip the new line character when logging values
	boolean flattenPropertyValues;
	// If true, mask values for keys that match the maskExpression
	boolean maskPropertyValues = true;
	// Matches any string containing "password" (case insensitive)
	String maskExpression = ".*((?i)password).*";
	String maskValue = "******";
	Pattern pattern;

	public void logProperty(String key, String value) {
		logger.info(key + "=" + getLogValue(key, value));
	}

	public void logProperties(Properties properties) {
		logProperties(properties, null);
	}

	public void logProperties(Properties properties, String comment) {
		if (!StringUtils.isEmpty(comment)) {
			logger.info(comment);
		}
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to log");
			return;
		}
		if (maskPropertyValues) {
			pattern = Pattern.compile(maskExpression);
		}
		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			sortedProperties.put(key, value);
		}
		for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
			logProperty(entry.getKey(), entry.getValue());
		}
	}

	public String getLogValue(String key, String value) {
		if (flattenPropertyValues) {
			value = value.replace("\n", " ");
			value = value.replace("\r", " ");
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

	public void setMaskExpression(String maskExpression) {
		this.maskExpression = maskExpression;
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

}
