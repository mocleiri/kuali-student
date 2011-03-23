package org.kuali.spring.util;

import org.springframework.beans.factory.config.BeanDefinition;

public class BeanVisitationEvent {
	BeanDefinition beanDefinition;

	public BeanVisitationEvent() {
		this(null);
	}

	public BeanVisitationEvent(BeanDefinition beanDefinition) {
		super();
		this.beanDefinition = beanDefinition;
	}

	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}

	public void setBeanDefinition(BeanDefinition beanDefinition) {
		this.beanDefinition = beanDefinition;
	}

}
