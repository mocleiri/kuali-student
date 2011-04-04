package org.kuali.spring.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.kuali.spring.util.PropertiesLoader;
import org.kuali.spring.util.Loader;

public class ProxyTest {

	@Test
	public void loader() throws Exception {
		Loader loader = new PropertiesLoader();
		InvocationHandler handler = new LoggingHandler(loader);
		ClassLoader classLoader = loader.getClass().getClassLoader();
		Class<?>[] interfaces = loader.getClass().getInterfaces();
		System.out.println(interfaces.length);
		Loader proxy = (Loader) Proxy.newProxyInstance(classLoader, interfaces, handler);
		proxy.load();

	}

}
