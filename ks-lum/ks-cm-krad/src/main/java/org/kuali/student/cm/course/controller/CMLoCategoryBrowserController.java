/**
 * Copyright 2014 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * Created by chongzhu on 6/23/14
 */
package org.kuali.student.cm.course.controller;

import org.kuali.student.cm.course.form.LoCategoryTypeBrowseForm;
import org.kuali.student.common.uif.controller.KSLookupController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * /**
 * This class handles the LO Category browsing functionality for a proposal.
 *
 * @author Kuali Student Team
 */


@Controller
@RequestMapping(value = "/CategoryTypelookup")
public class CMLoCategoryBrowserController extends KSLookupController {

    private static final Logger LOG = LoggerFactory.getLogger(CMLoCategoryBrowserController.class);

    @Override
    protected LoCategoryTypeBrowseForm createInitialForm(HttpServletRequest request) {
        return new LoCategoryTypeBrowseForm();
    }

}

