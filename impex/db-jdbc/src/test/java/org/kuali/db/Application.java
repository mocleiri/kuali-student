package org.kuali.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc.xml");
			System.out.println(context.getBean("jdbc.sql.drop"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
