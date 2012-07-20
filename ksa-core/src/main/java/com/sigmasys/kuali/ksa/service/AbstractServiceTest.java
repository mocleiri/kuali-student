package com.sigmasys.kuali.ksa.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.util.ContextUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;

/**
 * Base class for all service tests.
 * It provides access to Spring application context, major services and HTTP mock-ups.
 *
 * @author IvanovM
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractServiceTest implements ApplicationContextAware {

    public static final String TEST_USER_ID = "admin";

    protected final Log logger = LogFactory.getLog(getClass());

    public AbstractServiceTest() {

        // Setting up the test HTTP environment

        MockServletContext context = new MockServletContext();

        MockHttpServletRequest request = new MockHttpServletRequest(context);
        request.setRemoteUser(TEST_USER_ID);
        request.setSession(new MockHttpSession());

        RequestUtils.setServletContext(new MockServletContext());

        RequestUtils.setThreadRequest(request);
        RequestUtils.setThreadResponse(new MockHttpServletResponse());
    }

    protected void init(ApplicationContext applicationContext) {
        ContextUtils.initContext(applicationContext);
        UserSessionManager sessionManager = ContextUtils.getBean("userSessionManager");
        sessionManager.createSession(RequestUtils.getThreadRequest(), RequestUtils.getThreadResponse(), TEST_USER_ID);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        init(applicationContext);
    }
}
