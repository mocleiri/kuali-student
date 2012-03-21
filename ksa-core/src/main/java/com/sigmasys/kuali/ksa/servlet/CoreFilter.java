package com.sigmasys.kuali.ksa.servlet;

import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CoreFilter
 * Controls and dispatches every HTTP request to a webapp
 *
 * @author ivanovm
 */

public class CoreFilter implements Filter {


    protected final Log logger = LogFactory.getLog(getClass());

    private final Object lock = new Object();


    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CoreFilter is initialized.");
    }


    private void initServices(HttpServletRequest httpRequest) {
        logger.info("Initializing services...");
        try {
            // TODO - initialize user session manager
        } catch (RuntimeException t) {
            handleError(t, httpRequest);
            throw t;
        }
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Initializing services if necessary
        synchronized (lock) {
                initServices(httpRequest);
        }


        try {
            // Expose thread local response to the entire application
            RequestUtils.setThreadResponse(httpResponse);

            // TODO - provide user session creation


            // Chaining
            chain.doFilter(request, response);

        } catch (Throwable t) {
            logger.error("Error occured while serving HTTP request: " + t.getMessage(), t);
        } finally {
            RequestUtils.setThreadResponse(null);
        }
    }

    protected void setUserForTrustedUrl(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // The default version does nothing.
        // It can be used in subclasses for setting application account with the session manager
    }

    private void handleError(Throwable t, HttpServletRequest request) {
        // TODO - destroy user session
        logger.error("Exception raised in CoreFilter. " + t.getMessage(), t);
    }

    public void destroy() {
        logger.info("CoreFilter: cleaning up resources...");
        // TODO - provide resource clean up
        logger.info("CoreFilter: clean up is done.");
    }
}