package org.kuali.spring.util;

import java.util.Properties;

public interface PropertyLogger {

	public String getLogEntry(String key, String value);

	public String getLogEntry(Properties properties);

	public String getLogValue(String key, String value);

}
