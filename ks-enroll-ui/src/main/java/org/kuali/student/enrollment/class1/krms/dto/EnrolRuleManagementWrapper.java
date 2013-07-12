/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.student.enrollment.class1.krms.dto;

import org.kuali.rice.krms.dto.RuleManagementWrapper;
import org.kuali.student.enrollment.class2.courseoffering.dto.CourseOfferingContextBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Student Team
 */
public class EnrolRuleManagementWrapper extends RuleManagementWrapper {

    private String cluDescription;
    private String cluSubjectCode;
    private String cluTermCode;
    private List<CluInformation> clusInRange;
    private CourseOfferingContextBar contextBar = CourseOfferingContextBar.NULL_SAFE_INSTANCE;
    private String socStateKey;

    public String getCluDescription() {
        return cluDescription;
    }

    public void setCluDescription(String cluDescription) {
        this.cluDescription = cluDescription;
    }

    public EnrolRuleEditor getEnrolRuleEditor(){
        return (EnrolRuleEditor) this.getRuleEditor();
    }

    public List<CluInformation> getClusInRange() {
        if(this.clusInRange==null){
            this.clusInRange = new ArrayList<CluInformation>();
        }
        return this.clusInRange;
    }

    public void setClusInRange(List<CluInformation> clusInRange) {
        this.clusInRange = clusInRange;
    }

    public int getClusInRangeSize(){
        return this.getClusInRange().size();
    }

    public CourseOfferingContextBar getContextBar() {
        return contextBar;
    }

    public void setContextBar(CourseOfferingContextBar contextBar) {
        this.contextBar = contextBar;
    }

    public String getCluSubjectCode() {
        return cluSubjectCode;
    }

    public void setCluSubjectCode(String cluSubjectCode) {
        this.cluSubjectCode = cluSubjectCode;
    }

    public String getCluTermCode() {
        return cluTermCode;
    }

    public void setCluTermCode(String cluTermCode) {
        this.cluTermCode = cluTermCode;
    }
}
