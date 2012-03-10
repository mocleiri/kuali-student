package com.sigmasys.kuali.ksa.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.persistence.ojb.RiceDataSourceConnectionFactory;
import org.kuali.rice.core.framework.persistence.ojb.TransactionManagerFactory;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
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

import javax.transaction.TransactionManager;
import javax.xml.namespace.QName;
import java.io.IOException;

/**
 * Base class for all service tests.
 * It provides access to Spring application context, major services and HTTP mock-ups.
 *
 * @author IvanovM
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractServiceTest implements ApplicationContextAware {

    private static final Log logger = LogFactory.getLog(AbstractServiceTest.class);

    public static final String TEST_USER_ID = "guest";

    public static final String RICE_RESOURCE_LOADER_BEANS_CONTEXT = "classpath:/META-INF/rice-resource-loader-beans.xml";
    public static final String RICE_CONFIG_CONTEXT = "classpath:/META-INF/ksa-rice-config.xml";

    private static final MockServletContext servletContext = new MockServletContext();

    static {
        try {
            initConfigContext(RICE_CONFIG_CONTEXT);
            ResourceLoader resourceLoader =
                    new SpringResourceLoader(new QName(""), RICE_RESOURCE_LOADER_BEANS_CONTEXT, servletContext);
            GlobalResourceLoader.addResourceLoader(resourceLoader);
            GlobalResourceLoader.start();
        } catch (Exception e) {
            logger.error("Cannot initialize GlobalResourceLoader: " + e.getMessage(), e);
        }
    }

    private final boolean isWebContext = isWebContext();

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

    private static void initConfigContext(String contextPath) throws IOException {
	    JAXBConfigImpl config = new JAXBConfigImpl(contextPath);
		config.parseConfig();
        ConfigContext.init(config);
    }

    private boolean isWebContext() {
        UseWebContext webContextAnnotation = getClass().getAnnotation(UseWebContext.class);
        return webContextAnnotation != null && webContextAnnotation.value();
    }

    protected void init(ApplicationContext applicationContext) {

        ContextUtils.initContext(applicationContext);

        RiceDataSourceConnectionFactory.addBeanFactory(applicationContext);

        if (isWebContext) {
            UserSessionManager sessionManager = ContextUtils.getBean("userSessionManager");
            sessionManager.createSession(RequestUtils.getThreadRequest(), RequestUtils.getThreadResponse(), TEST_USER_ID);
        }

        TransactionManager transactionManager = ContextUtils.getBean("atomikosTransactionManager");
        TransactionManagerFactory.setTransactionManager(transactionManager);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        init(applicationContext);
    }
}
