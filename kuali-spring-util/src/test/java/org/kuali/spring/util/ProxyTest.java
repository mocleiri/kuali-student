package org.kuali.spring.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.kuali.spring.util.DefaultPropertiesLoader;
import org.kuali.spring.util.PropertiesLoader;

public class ProxyTest {

	@Test
	public void loader() throws Exception {
		PropertiesLoader loader = new DefaultPropertiesLoader();
		InvocationHandler handler = new LoggingHandler(loader);
		ClassLoader classLoader = loader.getClass().getClassLoader();
		Class<?>[] interfaces = loader.getClass().getInterfaces();
		System.out.println(interfaces.length);
		PropertiesLoader proxy = (PropertiesLoader) Proxy.newProxyInstance(classLoader, interfaces, handler);
		proxy.loadProperties();

	}

}
