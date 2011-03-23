package org.kuali.spring.util;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PropertyPlaceholderConfigurerSystemOverrideTest {
	static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerSystemOverrideTest.class);
	static String key = "cat";
	static String cheetah = "cheetah";
	static Properties systemProperties = System.getProperties();

	@Autowired
	private ApplicationContext applicationContext;

	@BeforeClass
	public static void oneTimeSetUp() {
		logger.info("Setting system property {}={}", key, cheetah);
		systemProperties.setProperty(key, cheetah);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		logger.info("Removing system property '{}'", key, cheetah);
		systemProperties.remove(key);
	}

	@Test
	public void systemPropertyOverrideTest() {
		try {
			ResolvePropertiesFirstPlaceholderConfigurer configurer = (ResolvePropertiesFirstPlaceholderConfigurer) applicationContext
					.getBean("placeholder.configurer.test");
			Properties properties = configurer.getProperties();
			String cat = properties.getProperty("cat");
			Assert.assertEquals(cat, cheetah);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
