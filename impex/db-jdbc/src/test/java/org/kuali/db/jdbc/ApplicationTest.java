package org.kuali.db.jdbc;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {

	@Test
	public void appTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("dba-context.xml");
		String fooBean = (String) context.getBean("foo.bean");
		System.out.println(fooBean);
	}
}
