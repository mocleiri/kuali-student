package org.kuali.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc-context.xml");
			String sql = (String) context.getBean("impex.oracle.sql.drop");
			System.out.println(sql);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
