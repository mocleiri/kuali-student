package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Enumeration;

/**
 * Abstract JAX WS service class that provides access to the underlying HTTP session.
 * <p/>
 * @author Michael Ivanov
 */
public abstract class AbstractSessionAwareWebService {

    private static final Log logger = LogFactory.getLog(AbstractSessionAwareWebService.class);


    public static final byte MAX_SESSION_ATTRIBUTES = 50;

    @Resource
    private WebServiceContext wsContext;

    private HttpSession getSession() {
        HttpServletRequest request;
        try {
            MessageContext msgContext = wsContext.getMessageContext();
            request = (HttpServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);
            return request.getSession();
        } catch (Throwable t) {
            logger.info("JAX WS MessageContext is not present, getting HTTP request from RequestUtils. " +
                    t.getMessage());
            request = RequestUtils.getThreadRequest();
            if (request != null) {
                return request.getSession(false);
            }
            return null;
        }
    }

    private synchronized void adjustSessionAttributes(HttpSession session) {
        Enumeration attrNames = session.getAttributeNames();
        boolean adjustAttributes = false;
        String attrToRemove = null;
        for (int i = 0; attrNames != null && attrNames.hasMoreElements(); i++) {
            attrToRemove = attrNames.nextElement().toString();
            // if there are more than the maximum allowed number of attributes in the session - clean it up
            if (i > getMaxSessionAttributes()) {
                adjustAttributes = true;
                break;
            }
        }
        if (adjustAttributes) {
            session.removeAttribute(attrToRemove);
        }
    }

    protected byte getMaxSessionAttributes() {
        return MAX_SESSION_ATTRIBUTES;
    }

    protected void setAttribute(String name, Object value) {
        HttpSession session = getSession();
        if (session != null) {
            adjustSessionAttributes(session);
            session.setAttribute(name, value);
        }
        logger.debug("setAttribute: name = " + name + ", value = " + value);
    }

    protected <T> T getAttribute(String name) {
        T value = null;
        HttpSession session = getSession();
        if (session != null) {
            value = (T) session.getAttribute(name);
        }
        logger.debug("getAttribute: name = " + name + ", value = " + value);
        return value;
    }

}
