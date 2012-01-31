package com.sigmasys.kuali.ksa.servlet;

import com.sigmasys.kuali.ksa.annotation.UrlMapping;
import com.sigmasys.kuali.ksa.util.CommonUtils;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.Map;

/**
 * LazyInitDispatcherServlet.
 * User: Michael Ivanov
 */
@SuppressWarnings("serial")
public class LazyInitDispatcherServlet extends DispatcherServlet {

    private static final Log logger = LogFactory.getLog(LazyInitDispatcherServlet.class);

    // Map of <Service URL, Bean Name> for lazy instantiation
    private static final Map<String, String> urlHandlers = new Hashtable<String, String>();


    protected UrlHandlerMapping urlHandlerMapping;


    @Override
    protected void initStrategies(ApplicationContext context) {
        super.initStrategies(context);
        urlHandlerMapping = context.getBean(DispatcherServlet.HANDLER_MAPPING_BEAN_NAME, UrlHandlerMapping.class);

        // Registering bean names with their URLs retrieved from @UrlMapping within the given context
        for (String beanName : context.getBeanDefinitionNames()) {
            UrlMapping urlMapping = context.findAnnotationOnBean(beanName, UrlMapping.class);
            if (urlMapping != null) {
                String url = urlMapping.name();
                if (!url.isEmpty() && !url.endsWith("/")) {
                    url += "/";
                }
                urlHandlers.put(url, beanName);
                logger.debug("Added new URL handler, bean name = '" + beanName + "', URL = '" + url + "'");
            }
        }
    }

    private synchronized void registerHandler(HttpServletRequest request) {

        final String requestedUri = request.getRequestURI();
        logger.debug("registerHandler: requested URI = " + requestedUri);

        for (Map.Entry<String, String> entry : urlHandlers.entrySet()) {

            String url = entry.getKey();

            // If URL does not have "/" consider it a path relative to GWT module base URL
            if (!url.contains("/")) {
                url = CommonUtils.getModuleBaseUrl(request) + url;
            }

            if (requestedUri.contains(url) && !urlHandlerMapping.isRegistered(url)) {
                Object bean = ContextUtils.getBean(entry.getValue());
                // If the bean is a servlet - wrap it with ServletWrappingController
                if (bean instanceof Servlet) {
                    ServletWrappingController wrappingController =
                            ContextUtils.getBean(ServletWrappingController.BEAN_NAME);
                    wrappingController.setServletInstance((Servlet) bean);
                    urlHandlerMapping.registerHandler(url, wrappingController);
                    // If the bean is a Spring MVC controller just register it
                } else {
                    urlHandlerMapping.registerHandler(url, bean);
                }
                logger.debug("registerHandler: Registered URL handler '" + bean + "', bean name = '" + entry.getValue() +
                        "', URL = '" + url + "'");
            }
        }
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        registerHandler(request);
        super.doDispatch(request, response);
    }

}

