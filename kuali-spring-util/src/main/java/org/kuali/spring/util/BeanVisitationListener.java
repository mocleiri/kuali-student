package org.kuali.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class BeanVisitationListener implements VisitationListener {
	final Logger logger = LoggerFactory.getLogger(BeanVisitationListener.class);

	@Override
	public void valueResolved(ValueResolutionEvent event) {
		if (ObjectUtils.nullSafeEquals(event.getOldValue(), event.getNewValue())) {
			logger.trace("oldValue=[{}] newValue=[{}]", event.getOldValue(), event.getNewValue());
			return;
		}
		logger.info("oldValue=[{}] newValue=[{}]", event.getOldValue(), event.getNewValue());
	}

	@Override
	public void beforeBeanVisit(BeanVisitationEvent event) {
		logger.info("About to visit {}", event.getBeanDefinition().getDescription());
	}

	@Override
	public void afterBeanVisit(BeanVisitationEvent event) {
		logger.info("Just finished visiting {}", event.getBeanDefinition().getDescription());
	}
}
