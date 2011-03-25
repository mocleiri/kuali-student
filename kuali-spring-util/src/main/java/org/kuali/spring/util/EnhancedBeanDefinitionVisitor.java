package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.kuali.spring.util.event.BeanVisitationEvent;
import org.kuali.spring.util.event.DefaultBeanVisitationListener;
import org.kuali.spring.util.event.PropertyValueVisitationEvent;
import org.kuali.spring.util.event.ValueResolutionEvent;
import org.kuali.spring.util.event.VisitationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringValueResolver;

public class EnhancedBeanDefinitionVisitor extends BeanDefinitionVisitor {
	final Logger logger = LoggerFactory.getLogger(EnhancedBeanDefinitionVisitor.class);
	List<VisitationListener> listeners = new ArrayList<VisitationListener>();
	StringValueResolver valueResolver;

	public EnhancedBeanDefinitionVisitor() {
		this(null);
	}

	@Override
	protected String resolveStringValue(String strVal) {
		if (this.valueResolver == null) {
			throw new IllegalStateException("No StringValueResolver specified - pass a resolver "
					+ "object into the constructor or override the 'resolveStringValue' method");
		}
		String resolvedValue = this.valueResolver.resolveStringValue(strVal);
		// Return original String if not modified.
		return (strVal.equals(resolvedValue) ? strVal : resolvedValue);
	}

	public EnhancedBeanDefinitionVisitor(StringValueResolver valueResolver) {
		super();
		this.valueResolver = valueResolver;
		addListener(new DefaultBeanVisitationListener());
	}

	public void addListener(VisitationListener listener) {
		listeners.add(listener);
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

	@Override
	protected void visitPropertyValues(MutablePropertyValues pvs) {
		PropertyValue[] pvArray = pvs.getPropertyValues();
		for (PropertyValue pv : pvArray) {
			visitPropertyValue(pvs, pv);
		}
	}

	protected void visitPropertyValue(MutablePropertyValues pvs, PropertyValue pv) {
		beforeVisit(pvs, pv);
		Object oldVal = pv.getValue();
		Object newVal = resolveValue(pv.getValue());
		if (!ObjectUtils.nullSafeEquals(newVal, oldVal)) {
			pvs.add(pv.getName(), newVal);
		}
		afterVisit(pvs, pv, oldVal, newVal);
	}

	protected void beforeVisit(MutablePropertyValues pvs, PropertyValue pv) {
		PropertyValueVisitationEvent event = new PropertyValueVisitationEvent(pvs, pv);
		for (VisitationListener listener : listeners) {
			listener.afterVisit(event);
		}
	}

	protected void afterVisit(MutablePropertyValues pvs, PropertyValue pv, Object oldVal, Object newVal) {
		PropertyValueVisitationEvent event = new PropertyValueVisitationEvent(pvs, pv, oldVal, newVal);
		for (VisitationListener listener : listeners) {
			listener.afterVisit(event);
		}
	}

	protected void beforeVisit(BeanDefinition beanDefinition) {
		BeanVisitationEvent event = new BeanVisitationEvent(beanDefinition);
		for (VisitationListener listener : listeners) {
			listener.beforeVisit(event);
		}
	}

	protected void afterVisit(BeanDefinition beanDefinition) {
		BeanVisitationEvent event = new BeanVisitationEvent(beanDefinition);
		for (VisitationListener listener : listeners) {
			listener.afterVisit(event);
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

	public StringValueResolver getValueResolver() {
		return valueResolver;
	}

	public void setValueResolver(StringValueResolver valueResolver) {
		this.valueResolver = valueResolver;
	}

}
