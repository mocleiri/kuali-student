package org.kuali.db.jdbc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try {
			new ClassPathXmlApplicationContext("application-context.xml");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
