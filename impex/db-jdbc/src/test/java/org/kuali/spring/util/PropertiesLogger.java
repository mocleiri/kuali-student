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
	final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);
	// If true, strip the new line character when logging values
	boolean flattenPropertyValues;
	// If true, mask values for keys that match the maskExpression
	boolean maskPropertyValues = true;
	// Matches any string containing "password" (case insensitive)
	String maskExpression = ".*((?i)password).*";
	String maskValue = "******";
	String description;
	Pattern pattern;

	public void logProperties(Properties properties) {
		if (!StringUtils.isEmpty(description)) {
			logger.info(description);
		}
		if (properties == null || properties.size() == 0) {
			logger.info("No properties to log");
			return;
		}
		if (maskPropertyValues) {
			pattern = Pattern.compile(maskExpression);
		}
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX);

		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			String originalValue = properties.getProperty(key);
			String resolvedValue = helper.replacePlaceholders(originalValue, properties);
			sortedProperties.put(key, resolvedValue);
		}
		for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
			logger.info(entry.getKey() + "=" + getLogValue(entry));
		}
	}

	protected String getLogValue(Map.Entry<String, String> entry) {
		String value = entry.getValue();
		if (flattenPropertyValues) {
			value = value.replace("\n", "");
		}
		if (!maskPropertyValues) {
			return value;
		}
		Matcher matcher = pattern.matcher(entry.getKey());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
