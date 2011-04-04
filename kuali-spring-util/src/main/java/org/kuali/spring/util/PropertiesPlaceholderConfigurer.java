package org.kuali.spring.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 
 */
public class PropertiesPlaceholderConfigurer extends PlaceholderConfigurer {
	final Logger logger = LoggerFactory.getLogger(PropertiesPlaceholderConfigurer.class);

	private PropertiesLoader loader = new PropertiesLoader();
	private PropertiesConverter converter = new PropertiesConverter();
	private PropertiesProcessor processor;
	private Properties properties;

	protected void load() {
		this.properties = loader.loadProperties();
	}

	protected void convert() {
		converter.convert(this.properties);
	}

	protected void process(ConfigurableListableBeanFactory beanFactory) {
		processor.process(beanFactory, this.properties);
	}

	public PropertiesLoader getLoader() {
		return loader;
	}

	public void setLoader(PropertiesLoader loader) {
		this.loader = loader;
	}

	public PropertiesConverter getConverter() {
		return converter;
	}

	public void setConverter(PropertiesConverter converter) {
		this.converter = converter;
	}

	public PropertiesProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(PropertiesProcessor processor) {
		this.processor = processor;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
