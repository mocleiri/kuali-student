package com.sigmasys.kuali.ksa.servlet;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Main KSA Filter
 *
 * @author Michael Ivanov
 *         Date: 4/2/12
 */
public class CoreFilter implements Filter {

    private static final Log logger = LogFactory.getLog(CoreFilter.class);

    public static final String TRUSTED_URLS_PARAM_NAME = "trustedUrls";

    // Used for trusted URLs
    private static final String DEFAULT_USER_ID = "admin";

    private static final String DEFAULT_LOGIN_PATH = "/WEB-INF/jsp/login.jsp";

    private final Set<String> trustedUrls = new HashSet<String>();

    private final Object lock = new Object();

    private boolean isInitialized;

    private String loginPath;

    @Override
    public void init(FilterConfig config) throws ServletException {
        loginPath = config.getInitParameter("loginPath");
        if (loginPath == null) {
            loginPath = DEFAULT_LOGIN_PATH;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void initTrustedUrls() {
        ConfigService configService = ContextUtils.getBean(ConfigService.class);
        String trustedUrls = configService.getInitialParameter(TRUSTED_URLS_PARAM_NAME);
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

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {

            // Expose thread local response to the entire application
            RequestUtils.setThreadResponse(response);

            final UserSession kradSession = KRADUtils.getUserSessionFromRequest(request);

            final UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);

            // Initializing services if necessary
            synchronized (lock) {
                if (!isInitialized) {
                    initTrustedUrls();
                    isInitialized = true;
                }
            }

            if (kradSession == null || !sessionManager.isSessionValid(request, response)) {

                request.setAttribute("showPasswordField", true);

                String queryString = request.getQueryString();
                request.setAttribute("redirectUrl",
                        request.getRequestURI() + (queryString != null ? "?" + queryString : ""));

                final String userId = request.getParameter("userId");
                final String password = request.getParameter("password");

                if (userId != null) {

                    // Very simple password checking. Nothing hashed or encrypted.
                    IdentityService identityService = ContextUtils.getBean("kimIdentityService", IdentityService.class);
                    final Principal principal =
                            isTrustedUrl(request) ?
                                    identityService.getPrincipalByPrincipalName(DEFAULT_USER_ID) :
                                    identityService.getPrincipalByPrincipalNameAndPassword(userId, password);

                    if (principal != null) {

                        // Creating HTTP session
                        sessionManager.createSession(request, response, userId);

                        // wrap the request with the remote user
                        // UserLoginFilter and WebAuthenticationService will create the session
                        request = new HttpServletRequestWrapper(request) {
                            @Override
                            public String getRemoteUser() {
                                return userId;
                            }
                        };

                        // Creating KRAD UserSession and put it as an attribute into HTTP session
                        UserSession userSession = new UserSession(userId) {
                            @Override
                            protected void initPerson(String principalName) {
                                try {
                                    final PersonService personService = ContextUtils.getBean(PersonService.class);
                                    Field field = UserSession.class.getDeclaredField("person");
                                    field.setAccessible(true);
                                    field.set(this, personService.getPerson(userId));
                                } catch (Exception e) {
                                    logger.error(e);
                                }
                            }
                        };

                        request.getSession(false).setAttribute(KRADConstants.USER_SESSION_KEY, userSession);

                    } else {
                        handleInvalidLogin(request, response);
                    }

                } else {
                    // No session has been established and this is not a login form submission, so forward to login page
                    request.getRequestDispatcher(loginPath).forward(request, response);
                    return;
                }
            } else {
                request = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getRemoteUser() {
                        return kradSession.getPrincipalName();
                    }
                };
            }

            logger.info("USER-REQUEST-INFO: user = '" + sessionManager.getUserId(request) +
                    "' requested URI = '" + request.getRequestURI() + "'");

            chain.doFilter(request, response);

        } catch (Throwable t) {
            logger.error("Error occured while serving HTTP request: " + t.getMessage(), t);
        } finally {
            RequestUtils.setThreadResponse(null);
        }
    }

    /**
     * Handles and invalid login attempt.
     *
     * @param request  the incoming request
     * @param response the outgoing response
     * @throws ServletException if unable to handle the invalid login
     * @throws IOException      if unable to handle the invalid login
     */
    private void handleInvalidLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request != null) {
            final UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
            if (sessionManager != null) {
                sessionManager.destroySession(request);
            }
            request.setAttribute("invalidLogin", true);
            request.getRequestDispatcher(loginPath).forward(request, response);
        }
    }

    @Override
    public void destroy() {
        loginPath = null;
        trustedUrls.clear();
    }
}
