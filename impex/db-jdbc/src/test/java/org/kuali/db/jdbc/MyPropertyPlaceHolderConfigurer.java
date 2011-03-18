package org.kuali.db.jdbc;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

public class MyPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(MyPropertyPlaceHolderConfigurer.class);
	boolean maskPropertyValues = true;
	// Mask values if the key contains "password" (case insensitive)
	String maskExpression = "(.)*((?i)password)(.*)";
	String maskValue = "******";
	Pattern pattern;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Properties mergedProps = mergeProperties();

			// Convert the merged properties, if necessary.
			convertProperties(mergedProps);

			if (logger.isInfoEnabled()) {
				logProperties(mergedProps);
			}

			// Let the subclass process the properties.
			processProperties(beanFactory, mergedProps);

		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}

	protected void logProperties(Properties properties) {
		if (properties == null || properties.size() == 0) {
			return;
		}
		if (maskPropertyValues) {
			pattern = Pattern.compile(maskExpression);
		}
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(DEFAULT_PLACEHOLDER_PREFIX,
				DEFAULT_PLACEHOLDER_SUFFIX);

		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			String originalValue = properties.getProperty(key);
			String resolvedValue = helper.replacePlaceholders(originalValue, properties);
			sortedProperties.put(key, resolvedValue);
		}
		logger.info("******************* Spring Properties *********************");
		for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
			logger.info(entry.getKey() + "=" + getMaskedValue(entry));
		}
	}

	protected String getMaskedValue(Map.Entry<String, String> entry) {
		if (!maskPropertyValues) {
			return entry.getValue();
		}
		Matcher matcher = pattern.matcher(entry.getKey());
		boolean match = matcher.matches();
		if (match) {
			return maskValue;
		} else {
			return entry.getValue();
		}
	}

	public String getMaskExpression() {
		return maskExpression;
	}

	public void setMaskExpression(String maskExpression) {
		this.maskExpression = maskExpression;
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
}
