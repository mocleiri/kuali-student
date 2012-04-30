package com.sigmasys.kuali.ksa.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestUtils {

    private static final ThreadLocal<HttpServletRequest> perThreadRequest = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> perThreadResponse = new ThreadLocal<HttpServletResponse>();
    private static ServletContext servletContext;

    private RequestUtils() {
    }

    public static void setThreadRequest(HttpServletRequest request) {
        perThreadRequest.set(request);
    }

    public static HttpServletRequest getThreadRequest() {
        return perThreadRequest.get();
    }

    public static void setThreadResponse(HttpServletResponse response) {
        perThreadResponse.set(response);
    }

    public static HttpServletResponse getThreadResponse() {
        return perThreadResponse.get();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void setServletContext(ServletContext servletContext) {
        RequestUtils.servletContext = servletContext;
    }

}
