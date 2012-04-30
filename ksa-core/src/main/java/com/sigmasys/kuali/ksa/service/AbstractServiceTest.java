package com.sigmasys.kuali.ksa.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.annotation.UseWebContext;
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

    public static final String TEST_USER_ID = "guest";

    private final boolean isWebContext = isWebContext();

    protected final Log logger = LogFactory.getLog(getClass());

    public AbstractServiceTest() {
        if (isWebContext) {
            // Setting up the test HTTP environment
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRemoteUser(TEST_USER_ID);
            RequestUtils.setServletContext(new MockServletContext());
            RequestUtils.setThreadRequest(request);
            RequestUtils.setThreadResponse(new MockHttpServletResponse());
        }
    }

    private boolean isWebContext() {
        UseWebContext webContextAnnotation = getClass().getAnnotation(UseWebContext.class);
        return webContextAnnotation != null && webContextAnnotation.value();
    }

    protected void init(ApplicationContext applicationContext) {

        ContextUtils.initContext(applicationContext);

        if (isWebContext) {
            UserSessionManager sessionManager = ContextUtils.getBean("userSessionManager");
            sessionManager.createSession(RequestUtils.getThreadRequest(), RequestUtils.getThreadResponse(), TEST_USER_ID);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        init(applicationContext);
    }
}
