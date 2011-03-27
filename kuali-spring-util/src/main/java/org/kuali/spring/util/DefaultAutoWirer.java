package org.kuali.spring.util;

import org.kuali.spring.util.event.DefaultVisitListener;
import org.kuali.spring.util.event.VisitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;

public class DefaultAutoWirer implements Wirer {
	final Logger logger = LoggerFactory.getLogger(DefaultAutoWirer.class);

	PropertyHandler handler;

	public DefaultAutoWirer() {
		this(null);
	}

	public DefaultAutoWirer(PropertyHandler handler) {
		super();
		this.handler = handler;
	}

	public void wire() {
		Assert.notNull(handler);
		wireLoggerSupport();
		wireHelper();
		wireLoader();
		wireReplacer();
		wireRetriever();
		wireResolver();
		wireVisitor();
	}

	protected void wireLoggerSupport() {
		if (handler.getPropertiesLogger() != null) {
			return;
		}
		DefaultPropertiesLogger loggerSupport = new DefaultPropertiesLogger();
		handler.setPropertiesLogger(loggerSupport);
		logWiringEvent(handler.getPropertiesLogger(), handler);
	}

	protected void wireHelper() {
		if (handler.getHelper() == null) {
			handler.setHelper(new PropertiesHelper());
			logWiringEvent(handler.getHelper(), handler);
		}
		PropertiesHelper helper = handler.getHelper();
		if (helper.getPropertiesLogger() == null) {
			helper.setPropertiesLogger(handler.getPropertiesLogger());
			logWiringEvent(helper.getPropertiesLogger(), helper);
		}
	}

	protected void wireLoader() {
		if (handler.getLoader() == null) {
			handler.setLoader(new DefaultPropertiesLoader());
			logWiringEvent(handler.getLoader(), handler);
		}
		PropertiesHelper helper = handler.getHelper();
		DefaultPropertiesLoader loader = handler.getLoader();
		DefaultPropertiesLogger loggerSupport = handler.getPropertiesLogger();
		if (loader.getPropertiesLogger() == null) {
			loader.setPropertiesLogger(loggerSupport);
			logWiringEvent(loader.getPropertiesLogger(), loader);
		}
		if (loader.getHelper() == null) {
			loader.setHelper(helper);
			logWiringEvent(loader.getHelper(), loader);
		}
	}

	protected void wireReplacer() {
		if (handler.getReplacer() != null) {
			return;
		}
		PlaceholderReplacer replacer = new PlaceholderReplacer(
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
				PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, null,
				PropertyHandler.DEFAULT_IS_IGNORE_UNRESOLVABLE_PLACEHOLDERS);
		handler.setReplacer(replacer);
		logWiringEvent(handler.getReplacer(), handler);
	}

	protected void wireRetriever() {
		if (handler.getRetriever() != null) {
			return;
		}
		PropertiesRetriever retriever = new PropertiesRetriever();
		handler.setRetriever(retriever);
		logWiringEvent(handler.getRetriever(), handler);
	}

	protected void wireResolver() {
		if (handler.getResolver() == null) {
			handler.setResolver(new DefaultStringValueResolver());
			logWiringEvent(handler.getResolver(), handler);
		}
		StringValueResolver resolver = handler.getResolver();
		if (!(handler.getResolver() instanceof DefaultStringValueResolver)) {
			return;
		}
		wireDefaultResolver((DefaultStringValueResolver) resolver);
	}

	protected void wireDefaultResolver(DefaultStringValueResolver resolver) {
		if (resolver.getReplacer() == null) {
			resolver.setReplacer(handler.getReplacer());
			logWiringEvent(handler.getReplacer(), resolver);
		}
		if (resolver.getRetriever() == null) {
			resolver.setRetriever(handler.getRetriever());
			logWiringEvent(handler.getRetriever(), resolver);
		}
	}

	protected void wireVisitor() {
		if (handler.getVisitor() == null) {
			handler.setVisitor(new EnhancedBeanDefinitionVisitor());
			logWiringEvent(handler.getVisitor(), handler);
		}
		BeanDefinitionVisitor visitor = handler.getVisitor();
		if (!(visitor instanceof EnhancedBeanDefinitionVisitor)) {
			return;
		}
		wireEnhancedVisitor((EnhancedBeanDefinitionVisitor) visitor);
	}

	protected void wireEnhancedVisitor(EnhancedBeanDefinitionVisitor visitor) {
		if (visitor.getValueResolver() == null) {
			visitor.setValueResolver(handler.getResolver());
			logWiringEvent(handler.getResolver(), visitor);
		}
		if (visitor.getListeners().size() == 0) {
			VisitListener listener = new DefaultVisitListener();
			visitor.addListener(listener);
			logWiringEvent(listener, visitor);
		}
	}

	protected void logWiringEvent(Object objectToWire, Object beingWiredTo) {
		logger.info("Wiring [" + objectToWire + "] -> [" + beingWiredTo + "]");
	}

}
