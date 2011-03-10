package org.kuali.db.impex;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext("classpath:org/kuali/db/impex/impex-context.xml");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
