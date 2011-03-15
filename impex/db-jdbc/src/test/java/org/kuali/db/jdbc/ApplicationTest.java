package org.kuali.db.jdbc;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {
	final Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

	@Test
	public void appTest1() {

		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc/dba.xml");
			showContext(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void appTest2() {
		ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc/dba-test.xml",
				"org/kuali/db/jdbc/dba.xml");
		showContext(context);
	}

	protected void showContext(ApplicationContext context) {
		String[] names = context.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = context.getBean(name);
			logger.info(name + "=" + bean);
		}
	}

	protected void showProperties(ApplicationContext context) {
	}
}
