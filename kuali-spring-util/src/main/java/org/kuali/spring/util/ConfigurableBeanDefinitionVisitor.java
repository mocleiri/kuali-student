package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.util.StringValueResolver;

public class ConfigurableBeanDefinitionVisitor extends BeanDefinitionVisitor {
	final Logger logger = LoggerFactory.getLogger(ConfigurableBeanDefinitionVisitor.class);
	List<ValueResolutionListener> listeners = new ArrayList<ValueResolutionListener>();

	StringValueResolver stringValueResolver;

	@Override
	protected String resolveStringValue(String strVal) {
		if (this.stringValueResolver == null) {
			throw new IllegalStateException("No StringValueResolver specified");
		}
		String resolvedValue = this.stringValueResolver.resolveStringValue(strVal);
		return (strVal.equals(resolvedValue) ? strVal : resolvedValue);
	}

	public StringValueResolver getStringValueResolver() {
		return stringValueResolver;
	}

	public void setStringValueResolver(StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

	@Override
	protected Object resolveValue(Object value) {
		Object newValue = super.resolveValue(value);
		valueResolved(value, newValue);
		return newValue;
	}

	protected void valueResolved(Object oldValue, Object newValue) {
		ValueResolutionEvent event = new ValueResolutionEvent(oldValue, newValue);
		for (ValueResolutionListener listener : listeners) {
			listener.valueResolved(event);
		}
	}

}
