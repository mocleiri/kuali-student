package org.kuali.spring.util;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PropertyPlaceholderConfigurerSystemOverrideTest {
	static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerSystemOverrideTest.class);
	static String key = "cat";
	static String val = "cheetah";
	static Properties systemProperties = System.getProperties();

	@BeforeClass
	public static void oneTimeSetUp() {
		logger.info("Setting system property {}={}", key, val);
		System.setProperty(key, val);
	}
	
    @AfterClass
    public static void oneTimeTearDown() {
		logger.info("Removing system property {}={}", key, val);
		systemProperties.remove(key);
    }



	@Test
	public void systemPropertyOverrideTest() {
		// Nothing to do. This test exists just to make sure Spring can process and load
		// PropertyPlaceholderConfigurerSystemOverrideTest-context.xml
	}

}
