package org.kuali.spring.util;

import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.util.StringValueResolver;

public class ConfigurableBeanDefinitionVisitor extends BeanDefinitionVisitor {
	StringValueResolver stringValueResolver;

	@Override
	protected String resolveStringValue(String strVal) {
		if (this.stringValueResolver == null) {
			throw new IllegalStateException("No StringValueResolver specified");
		}
		String resolvedValue = this.stringValueResolver.resolveStringValue(strVal);
		// Return original String if not modified.
		return (strVal.equals(resolvedValue) ? strVal : resolvedValue);
	}

	public StringValueResolver getStringValueResolver() {
		return stringValueResolver;
	}

	public void setStringValueResolver(StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

}
