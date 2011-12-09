/*
 * Copyright 2006-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kuali.rice.krad.ks.uim.sample;

import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthenticationException;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class overrides UserLoginFilter.doFilter and creates the required UserSession
 * for Rice, based on config property "automatic.login.user" or user "admin".
 *
 * @author Kuali Student Team
 */
public class UserLoginBypassFilter extends org.kuali.rice.kew.web.UserLoginFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (null == KRADUtils.getUserSessionFromRequest((HttpServletRequest) request)) {
            String principalName = ConfigContext.getCurrentContextConfig().getProperty("filter.login.automatic.user");
            if (null == principalName) {
                principalName = "admin";
            }
			UserSession userSession = new UserSession(principalName);
			((HttpServletRequest)request).getSession().setAttribute(KRADConstants.USER_SESSION_KEY, userSession);
        }

        super.doFilter(request, response, chain);
    }

}
