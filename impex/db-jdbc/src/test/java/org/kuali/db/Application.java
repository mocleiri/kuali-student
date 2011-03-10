package org.kuali.db;

import java.util.Vector;

import org.kuali.db.jdbc.SQLExecutor;
import org.kuali.db.jdbc.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(final String[] args) {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("org/kuali/db/jdbc.xml");
			SQLExecutor executor = (SQLExecutor) context.getBean("jdbc.init.sql.executor");
			Vector<Transaction> transactions = executor.getTransactions();
			for (Transaction t : transactions) {
				System.out.println(t.getSqlCommand());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
