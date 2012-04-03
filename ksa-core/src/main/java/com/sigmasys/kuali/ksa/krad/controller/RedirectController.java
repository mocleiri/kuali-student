/**
 * Copyright 2005-2012 The Kuali Foundation
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
package com.sigmasys.kuali.ksa.krad.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirect controller
 *
 * @author Michael Ivanov
 */
@Controller
@RequestMapping(value = "/redirect")
public class RedirectController extends UifControllerBase {

    private static final Log logger = LogFactory.getLog(RedirectController.class);

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
       return null;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView redirect(UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        String url = request.getParameter("redirectUrl");

               if ( url != null && !url.isEmpty() ) {
                   response.sendRedirect(url);
                   return null;
               }

               String errorMessage = "'redirectUrl' parameter cannot be empty";
               logger.error(errorMessage);
               throw new IllegalStateException(errorMessage);


    }

}
