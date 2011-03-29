package org.kuali.spring.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesHelper {
	final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

	PropertyLogger plogger = new DefaultPropertyLogger();



	//public void mergeEnvironmentProperties(Properties currentProps, String prefix) {
		//logger.info("Merging environment properties");
		//PropertySource source = PropertySource.ENVIRONMENT;
		//Properties envProps = getEnvironmentProperties(prefix);
		//PropertiesMergeContext context = new PropertiesMergeContext(currentProps, envProps, true, source, true);
		//mergeProperties(context);
	//}


	public PropertyLogger getPlogger() {
		return plogger;
	}

	public void setPlogger(PropertyLogger propertiesLogger) {
		this.plogger = propertiesLogger;
	}

}
