package org.kuali.db.jdbc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {

	@Test
	public void appTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("dba-context.xml");
		showContext(context);
	}

	protected void showContext(ApplicationContext context) {
		String[] names = context.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = context.getBean(name);
			System.out.println(name + "=" + bean);
		}
	}
}
