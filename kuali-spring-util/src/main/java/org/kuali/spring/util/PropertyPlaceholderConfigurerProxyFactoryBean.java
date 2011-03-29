package org.kuali.spring.util;

import net.sf.cglib.proxy.Enhancer;

import org.kuali.spring.proxy.MyCallback;
import org.springframework.beans.factory.FactoryBean;

public class PropertyPlaceholderConfigurerProxyFactoryBean implements
		FactoryBean<ConfigurablePropertyPlaceholderConfigurer> {

	@Override
	public ConfigurablePropertyPlaceholderConfigurer getObject() throws Exception {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(ConfigurablePropertyPlaceholderConfigurer.class);
		enhancer.setCallback(new MyCallback());
		ConfigurablePropertyPlaceholderConfigurer proxy = (ConfigurablePropertyPlaceholderConfigurer) enhancer.create();
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return ConfigurablePropertyPlaceholderConfigurer.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
