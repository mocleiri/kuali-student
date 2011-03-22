package org.kuali.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class DefaultValueResolutionListener implements ValueResolutionListener {
	final Logger logger = LoggerFactory.getLogger(DefaultValueResolutionListener.class);

	@Override
	public void valueResolved(ValueResolutionEvent event) {
		if (ObjectUtils.nullSafeEquals(event.getOldValue(), event.getNewValue())) {
			logger.trace("oldValue=[{}] newValue=[{}]", event.getOldValue(), event.getNewValue());
			return;
		}
		logger.info("oldValue=[{}] newValue=[{}]", event.getOldValue(), event.getNewValue());
	}
}
