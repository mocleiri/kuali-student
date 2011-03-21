package org.kuali.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringValueResolver;

public class ConfigurableBeanDefinitionVisitor extends BeanDefinitionVisitor {
	final Logger logger = LoggerFactory.getLogger(ConfigurableBeanDefinitionVisitor.class);

	StringValueResolver stringValueResolver;
	PropertiesLoggerSupport loggerSupport;

	@Override
	protected void visitPropertyValues(MutablePropertyValues pvs) {
		PropertyValue[] pvArray = pvs.getPropertyValues();
		for (PropertyValue pv : pvArray) {
			Object newVal = resolveValue(pv.getValue());
			if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
				logger.info("Updating '" + pv.getName() + "' [" + pv.getValue() + "]->["
						+ loggerSupport.getPropertyValue(pv.getValue() + "", newVal + "") + "]");
				pvs.add(pv.getName(), newVal);
			}
		}
	}

	@Override
	protected String resolveStringValue(String strVal) {
		if (this.stringValueResolver == null) {
			throw new IllegalStateException("No StringValueResolver specified");
		}
		String resolvedValue = this.stringValueResolver.resolveStringValue(strVal);
		boolean equal = strVal.equals(resolvedValue);
		if (!equal) {
			logger.debug("Resolved " + strVal + "->" + loggerSupport.getPropertyValue(strVal, resolvedValue));
		}
		// Return original String if not modified.
		return (equal ? strVal : resolvedValue);
	}

	public StringValueResolver getStringValueResolver() {
		return stringValueResolver;
	}

	public void setStringValueResolver(StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

	public PropertiesLoggerSupport getLoggerSupport() {
		return loggerSupport;
	}

	public void setLoggerSupport(PropertiesLoggerSupport loggerSupport) {
		this.loggerSupport = loggerSupport;
	}

}
