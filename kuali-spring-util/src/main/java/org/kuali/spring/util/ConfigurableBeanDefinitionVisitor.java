package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.util.StringValueResolver;

public class ConfigurableBeanDefinitionVisitor extends BeanDefinitionVisitor {
	final Logger logger = LoggerFactory.getLogger(ConfigurableBeanDefinitionVisitor.class);
	List<VisitationListener> listeners = new ArrayList<VisitationListener>();

	public ConfigurableBeanDefinitionVisitor() {
		super();
		listeners.add(new BeanVisitationListener());
	}

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

	@Override
	public void visitBeanDefinition(BeanDefinition beanDefinition) {
		beforeVisit(beanDefinition);
		super.visitBeanDefinition(beanDefinition);
		afterVisit(beanDefinition);
	}

	protected void beforeVisit(BeanDefinition beanDefinition) {
		BeanVisitationEvent event = new BeanVisitationEvent(beanDefinition);
		for (VisitationListener listener : listeners) {
			listener.beforeBeanVisit(event);
		}
	}

	protected void afterVisit(BeanDefinition beanDefinition) {
		BeanVisitationEvent event = new BeanVisitationEvent(beanDefinition);
		for (VisitationListener listener : listeners) {
			listener.afterBeanVisit(event);
		}
	}

	protected void valueResolved(Object oldValue, Object newValue) {
		ValueResolutionEvent event = new ValueResolutionEvent(oldValue, newValue);
		for (VisitationListener listener : listeners) {
			listener.valueResolved(event);
		}
	}

	public List<VisitationListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<VisitationListener> listeners) {
		this.listeners = listeners;
	}

}
