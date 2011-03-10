package org.kuali.db.jdbc;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;
import org.springframework.util.PropertyPlaceholderHelper;

public class PropertyPlaceholderHelperTest {

	@Test
	public void replacePlaceholders() {
		Properties properties = new Properties();
		properties.put("schema", "KSEMBEDDED");
		String value = "DROP USER ${schema} CASCADE;";
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
		value = helper.replacePlaceholders(value, properties);
		assertEquals(value, "DROP USER KSEMBEDDED CASCADE;");
	}

	@Test
	public void nestedProperties() {
		Properties properties = new Properties();
		properties.put("db.vendor", "derby");
		properties.put("datasource.url.oracle", "jdbc:oracle:thin:@localhost:1521:XE");
		properties.put("datasource.url.derby", "jdbc:derby://localhost:1527/derby/rice-db;create=true");
		properties.put("datasource.url", "${datasource.url.${db.vendor}}");
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
		String value = "${datasource.url}";
		value = helper.replacePlaceholders(value, properties);
		// System.out.println(value);
	}

}
