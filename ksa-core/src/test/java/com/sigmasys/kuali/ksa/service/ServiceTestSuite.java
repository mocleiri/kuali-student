package com.sigmasys.kuali.ksa.service;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;

import com.sigmasys.kuali.ksa.service.AbstractServiceTest;

/**
 * ServiceTestSuite is a test suite that runs all the declared service tests
 * <p/>
 * User: Michael Ivanov
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {TransactionServiceTest.class})
@ContextConfiguration(locations = {ServiceTestSuite.TEST_CONTEXT})
public class ServiceTestSuite extends AbstractServiceTest {

    public static final String TEST_CONTEXT = "/META-INF/ksa-context.xml";

    public static Test suite() {
        return new JUnit4TestAdapter(ServiceTestSuite.class);
    }

}