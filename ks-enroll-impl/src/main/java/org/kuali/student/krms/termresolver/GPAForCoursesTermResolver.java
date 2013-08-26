/**
 * Copyright 2011 The Kuali Foundation Licensed under the
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
 */

package org.kuali.student.krms.termresolver;

import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.student.enrollment.academicrecord.dto.GPAInfo;
import org.kuali.student.enrollment.academicrecord.dto.StudentCourseRecordInfo;
import org.kuali.student.enrollment.academicrecord.service.AcademicRecordService;
import org.kuali.student.krms.util.KSKRMSExecutionUtil;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.AcademicRecordServiceConstants;
import org.kuali.student.r2.core.constants.KSKRMSServiceConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Rule statement examples:
 * 1) Must have earned a minimum GPA of <GPA> in <courses>
 *
 * @author Kuali Student Team
 */
public class GPAForCoursesTermResolver implements TermResolver<Float> {

    private TermResolver<List<StudentCourseRecordInfo>> courseRecordsForCourseSetTermResolver;
    private AcademicRecordService academicRecordService;

    @Override
    public Set<String> getPrerequisites() {
        Set<String> prereqs = new HashSet<String>(2);
        prereqs.add(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID);
        prereqs.add(KSKRMSServiceConstants.TERM_PREREQUISITE_CONTEXTINFO);
        return Collections.unmodifiableSet(prereqs);
    }

    @Override
    public String getOutput() {
        return KSKRMSServiceConstants.TERM_RESOLVER_GPAFORCOURSES;
    }

    @Override
    public Set<String> getParameterNames() {
        return Collections.singleton(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY);
    }

    @Override
    public int getCost() {
        return 5;
    }

    @Override
    public Float resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) throws TermResolutionException {
        ContextInfo context = (ContextInfo) resolvedPrereqs.get(KSKRMSServiceConstants.TERM_PREREQUISITE_CONTEXTINFO);
        //Retrieve the completed course records from cluset.
        List<StudentCourseRecordInfo> studentCourseRecordInfoList = this.getCourseRecordsForCourseSetTermResolver().resolve(resolvedPrereqs, parameters);
        Float result = null;
        if(studentCourseRecordInfoList.size() > 0){
            try {
                GPAInfo gpa = academicRecordService.calculateGPA(studentCourseRecordInfoList, AcademicRecordServiceConstants.ACADEMIC_RECORD_CALCULATION_GPA_TYPE_KEY, context);
                result = Float.valueOf(gpa.getValue());
            } catch (Exception e) {
                KSKRMSExecutionUtil.convertExceptionsToTermResolutionException(parameters, e, this);
            }
        }
        return result;
    }

    public AcademicRecordService getAcademicRecordService() {
        return academicRecordService;
    }

    public void setAcademicRecordService(AcademicRecordService academicRecordService) {
        this.academicRecordService = academicRecordService;
    }

    public TermResolver<List<StudentCourseRecordInfo>> getCourseRecordsForCourseSetTermResolver() {
        return courseRecordsForCourseSetTermResolver;
    }

    public void setCourseRecordsForCourseSetTermResolver(TermResolver<List<StudentCourseRecordInfo>> courseRecordsForCourseSetTermResolver) {
        this.courseRecordsForCourseSetTermResolver = courseRecordsForCourseSetTermResolver;
    }
}
