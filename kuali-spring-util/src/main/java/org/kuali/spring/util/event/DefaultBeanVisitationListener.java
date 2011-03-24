package org.kuali.spring.util.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class DefaultBeanVisitationListener implements VisitationListener {
	final Logger logger = LoggerFactory.getLogger(DefaultBeanVisitationListener.class);

	@Override
	public void valueResolved(ValueResolutionEvent event) {
		if (ObjectUtils.nullSafeEquals(event.getOldValue(), event.getNewValue())) {
			// Nothing changed
			logger.trace("Resolved value is the same as the original value");
		} else {
			// The property was updated
			logger.info("Value updated: [{}]->[{}]", event.getOldValue(), event.getNewValue());
		}
	}

	@Override
	public void beforeVisit(BeanVisitationEvent event) {
		logger.info("Visiting {}", event.getBeanDefinition());
	}

	@Override
	public void afterVisit(BeanVisitationEvent event) {
		logger.trace("Visit completed. {}", event.getBeanDefinition());
	}

	@Override
	public void beforeVisit(PropertyValueVisitationEvent event) {
		logger.trace("Visiting {}", event.getPropertyValue());
	}

	@Override
	public void afterVisit(PropertyValueVisitationEvent event) {
		if (ObjectUtils.nullSafeEquals(event.getOldValue(), event.getNewValue())) {
			logger.trace("Value for property '{}' was left unchanged", event.getPropertyValue().getName());
		} else {
			logger.info("Value for property '" + event.getPropertyValue().getName() + "' updated: [{}]->[{}]",
					event.getOldValue(), event.getNewValue());
		}
	}
}
