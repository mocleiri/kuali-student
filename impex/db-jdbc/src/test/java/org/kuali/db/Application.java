package org.kuali.db;

import org.kuali.db.jdbc.ConnectionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc-context.xml");
			ConnectionHandler handler = (ConnectionHandler) context.getBean("jdbc.dba.connectionHandler");
			System.out.println(handler.getDriver());
			/**
			 * SQLExecutor executor = (SQLExecutor)
			 * context.getBean("jdbc.init.sql.executor"); Vector<Transaction>
			 * transactions = executor.getTransactions(); for (Transaction t :
			 * transactions) { System.out.println(t.getSqlCommand()); }
			 */
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
