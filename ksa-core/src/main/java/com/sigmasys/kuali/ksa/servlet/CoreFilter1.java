package com.sigmasys.kuali.ksa.servlet;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * CoreFilter
 * Controls and dispatches every HTTP request to a webapp
 *
 * @author ivanovm
 */

public class CoreFilter1 implements Filter {

    public static final String TRUSTED_URLS_PARAM = "trustedUrls";

    protected final Log logger = LogFactory.getLog(getClass());

    private final Set<String> trustedUrls = new HashSet<String>();

    private final Object lock = new Object();

    protected UserSessionManager sessionManager;


    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CoreFilter is initialized.");
    }

    private void initTrustedUrls() {
        ConfigService configService = ContextUtils.getBean(ConfigService.class);
        String trustedUrls = configService.getInitialParameter(TRUSTED_URLS_PARAM);
        if (trustedUrls != null && !trustedUrls.isEmpty()) {
            String[] patterns = trustedUrls.split(",");
            for (String pattern : patterns) {
                pattern = pattern.trim();
                if (!pattern.isEmpty()) {
                    this.trustedUrls.add(pattern.toLowerCase());
                }
            }
        }
    }

    private void initServices(HttpServletRequest httpRequest) {
        logger.info("Initializing services...");
        try {
            initTrustedUrls();
            sessionManager = ContextUtils.getBean(UserSessionManager.class);
        } catch (RuntimeException t) {
            handleError(t, httpRequest);
            throw t;
        }
    }

    /**
     * Checks whether the URL is trusted so the webapp can proceed with request handling
     * without user authentication.
     *
     * @param request HttpServletRequest
     * @return true if trusted URL
     */
    private boolean isTrustedUrl(HttpServletRequest request) {
        String url = request.getRequestURI().toLowerCase();
        for (String trustedUrl : trustedUrls) {
            if (url.contains(trustedUrl)) {
                return true;
            }
        }
        return false;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Initializing services if necessary
        synchronized (lock) {
            if (sessionManager == null) {
                initServices(httpRequest);
            }
        }

        try {
            // Expose thread local response to the entire application
            RequestUtils.setThreadResponse(httpResponse);

            boolean isTrustedUrl = isTrustedUrl(httpRequest);

            assert sessionManager != null;

            boolean isSessValid = sessionManager.isSessionValid(httpRequest, httpResponse);

            HttpServletRequest wrappedRequest = null;

            if (!isTrustedUrl && !isSessValid) {
                // TODO: Get the real user name from the HTTP request
                final String userId = "admin";
                if (userId != null) {
                    sessionManager.createSession(httpRequest, httpResponse, userId);
                    // Creating KRAD UserSession and put it as an attribute into HTTP session
                    UserSession userSession = new UserSession(userId) {
                         @Override
                        protected void initPerson(String principalName) {
                            try {
                                PersonService personService = ContextUtils.getBean("kimAuthenticationManager");
                                Field field = UserSession.class.getDeclaredField("person");
                                field.setAccessible(true);
                                field.set(this, personService.getPerson(userId));
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        }

                        @Override
                        public String getPrincipalId() {
                            return userId;
                        }

                        @Override
                        public String getPrincipalName() {
                            return userId;
                        }

                        @Override
                        public String getLoggedInUserPrincipalName() {
                            return userId;
                        }
                    };
                    wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                        @Override
                        public String getRemoteUser() {
                            return userId;
                        }
                    };
                    wrappedRequest.getSession(false).setAttribute(KRADConstants.USER_SESSION_KEY, userSession);
                    logger.info("New HTTP session is created for " + userId);
                } else {
                    httpResponse.sendError(401, "User can not be authenticated");
                    return;
                }
            }

            if (isTrustedUrl) {
                logger.debug("USER-SESSION-INFO: requested trusted URI = '" + httpRequest.getRequestURI() + "'");
            } else {
                logger.debug("USER-SESSION-INFO: user = '" + sessionManager.getUserId(httpRequest) +
                        "' requested URI = '" + httpRequest.getRequestURI() + "'");
            }

            // Chaining
            chain.doFilter(wrappedRequest != null ? wrappedRequest : request, response);

        } catch (Throwable t) {
            logger.error("Error occured while serving HTTP request: " + t.getMessage(), t);
        } finally {
            RequestUtils.setThreadResponse(null);
        }
    }

    private void handleError(Throwable t, HttpServletRequest request) {
        if (request != null && sessionManager != null) {
            sessionManager.destroySession(request);
        }
        sessionManager = null;
        trustedUrls.clear();
        logger.error("Exception raised in CoreFilter. " + t.getMessage(), t);
    }

    public void destroy() {
        logger.info("CoreFilter: cleaning up resources...");
        sessionManager = null;
        trustedUrls.clear();
        logger.info("CoreFilter: clean up is done.");
    }
}