package com.sigmasys.kuali.ksa.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserSessionManager {


    /**
     * Creates a new HTTP session
     *
     * @param request    the HTTP request
     * @param response   the HTTP response
     * @param userId     the User ID
     */
    void createSession(HttpServletRequest request, HttpServletResponse response, String userId);

    /**
     * Destroys the current HTTP session
     *
     * @param request the HTTP request
     */
    void destroySession(HttpServletRequest request);

    /**
     * Checks if the user session is valid.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return true if the session is valid.
     */
    boolean isSessionValid(HttpServletRequest request, HttpServletResponse response);

    /**
     * Returns the user ID without domain name. This method should be called after session creation and validation.
     *
     * @param request HttpServletRequest
     * @return the user ID
     */
    String getUserId(HttpServletRequest request);

}

