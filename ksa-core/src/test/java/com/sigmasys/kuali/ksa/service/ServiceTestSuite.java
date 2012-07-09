package com.sigmasys.kuali.ksa.service;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * ServiceTestSuite is a test suite that runs all the declared service tests
 * <p/>
 *
 * @author Michael Ivanov
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        DatabaseUtilsTest.class,
        TransactionServiceTest.class,
        CurrencyServiceTest.class,
        AccountServiceTest.class,
        ActivityServiceTest.class,
        KimServiceTest.class,
        DroolsServiceTest.class,
        InformationServiceTest.class,
        LanguageServiceTest.class,
        XliffParserTest.class,
        LocalizationServiceTest.class,
        LocalizationWebServiceTest.class,
        TransactionImportServiceTest.class,
        AccessControlServiceTest.class})
public class ServiceTestSuite extends AbstractServiceTest {

    public static final String TEST_KSA_CONTEXT = "/META-INF/test-context.xml";

    public static Test suite() {
        return new JUnit4TestAdapter(ServiceTestSuite.class);
    }

}