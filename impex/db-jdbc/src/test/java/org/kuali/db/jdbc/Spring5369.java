package org.kuali.db.jdbc;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper;

public class Spring5369 {
	final Logger logger = LoggerFactory.getLogger(Spring5369.class);

	@Test
	public void testSpring5369() {
		String oracleUser = "MYDB";
		String oraclePassword = "MYDB";
		String expectedSql = getOracleSql(oracleUser, oraclePassword);
		String placeHolderSql = getOracleSql("${jdbc.user}", "${jdbc.password}");
		Properties props = new Properties();
		props.put("jdbc.vendor", "oracle");
		props.put("jdbc.user", "${jdbc.${jdbc.vendor}.user}");
		props.put("jdbc.password", "${jdbc.${jdbc.vendor}.password}");
		props.put("jdbc.sql", "${jdbc.${jdbc.vendor}.sql}");
		props.put("jdbc.oracle.user", oracleUser);
		props.put("jdbc.oracle.password", oraclePassword);
		props.put("jdbc.oracle.sql", placeHolderSql);
		props.put("jdbc.mysql.sql", "DROP DATABASE ${jdbc.user}\n/\nCREATE DATABASE ${jdbc.user}\n/\n");
		props.put("jdbc.mysql.user", "mydb");
		props.put("jdbc.mysql.password", "mydb");
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
		// This fails throwing IllegalArgumentException
		String actualSql = helper.replacePlaceholders("${jdbc.sql}", props);
		Assert.assertEquals(expectedSql, actualSql);
	}

	protected String getOracleSql(String userSubsitution, String passwordSubsitution) {
		return "DROP USER " + userSubsitution + " CASCADE\n/\nCREATE USER " + userSubsitution + " IDENTIFIED BY "
				+ passwordSubsitution + "\n/\n";
	}
}
