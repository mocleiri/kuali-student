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
 * Created by venkat on 2/24/14
 */
package org.kuali.student.cm.course.rule;

import org.apache.commons.lang.StringUtils;

import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.util.GlobalVariables;

import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.student.cm.common.util.CurriculumManagementConstants;
import org.kuali.student.cm.course.form.CourseInfoWrapper;

import org.kuali.student.common.uif.rule.KsMaintenanceDocumentRuleBase;

/**
 * This class contains all the course creation business rules
 *
 * @author Kuali Student Team
 */
public class CourseRule extends KsMaintenanceDocumentRuleBase {

    public static final String DATA_OBJECT_PATH = KRADPropertyConstants.DOCUMENT + "."
            + KRADPropertyConstants.NEW_MAINTAINABLE_OBJECT + ".dataObject";

    @Override
    public boolean processSaveDocument(Document document) {

        boolean success = super.processSaveDocument(document);

        if (!success){
            return success;
        }

        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) document;
        CourseInfoWrapper dataObject = (CourseInfoWrapper) maintenanceDocument.getNewMaintainableObject().getDataObject();

        if (StringUtils.isBlank(dataObject.getProposalInfo().getName())){
            GlobalVariables.getMessageMap().putError(DATA_OBJECT_PATH + ".proposalInfo.name",
                    CurriculumManagementConstants.MessageKeys.ERROR_PROPOSAL_TITLE_REQUIRED);
            success = false;
        }

        if (StringUtils.isBlank(dataObject.getCourseInfo().getCourseTitle())){
            GlobalVariables.getMessageMap().putError(DATA_OBJECT_PATH + ".courseInfo.courseTitle",
                    CurriculumManagementConstants.MessageKeys.ERROR_COURSE_TITLE_REQUIRED);
            success = false;
        }

        return success;
    }

    @Override
    public boolean processRouteDocument(Document document) {
        boolean success = super.processRouteDocument(document);
        return success;
    }
}
