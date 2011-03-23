package org.kuali.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class DefaultBeanVisitationListener implements VisitationListener {
	final Logger logger = LoggerFactory.getLogger(DefaultBeanVisitationListener.class);

	@Override
	public void valueResolved(ValueResolutionEvent event) {
		if (ObjectUtils.nullSafeEquals(event.getOldValue(), event.getNewValue())) {
			// Nothing changed
			return;
		}
		logger.info("Value updated: [{}]->[{}]", event.getOldValue(), event.getNewValue());
	}

	@Override
	public void beforeBeanVisit(BeanVisitationEvent event) {
		logger.info("Visiting {}", event.getBeanDefinition());
	}

	@Override
	public void afterBeanVisit(BeanVisitationEvent event) {
		logger.trace("Visit completed. {}", event.getBeanDefinition());
	}
}
