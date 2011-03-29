package org.kuali.spring.util;

import junit.framework.Assert;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.springframework.beans.factory.FactoryBean;

public class SimpleProxyFactoryBean implements FactoryBean<Object> {
	String classname;
	Callback callback = NoOp.INSTANCE;

	public SimpleProxyFactoryBean() {
		this(null, NoOp.INSTANCE);
	}

	public SimpleProxyFactoryBean(String classname) {
		this(classname, NoOp.INSTANCE);
	}

	public SimpleProxyFactoryBean(String classname, Callback callback) {
		super();
		this.classname = classname;
		this.callback = callback;
	}

	@Override
	public Object getObject() throws Exception {
		Assert.assertNotNull(classname);
		Assert.assertNotNull(callback);

		Class<?> targetClass = Class.forName(classname);
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallback(getCallback());
		Object proxy = enhancer.create();
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return Object.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

}
