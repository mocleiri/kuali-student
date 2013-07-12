package org.kuali.student.krms.termresolver;

import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.student.enrollment.academicrecord.dto.StudentCourseRecordInfo;
import org.kuali.student.enrollment.academicrecord.service.AcademicRecordService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.KSKRMSServiceConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: SW
 * Date: 2013/01/25
 * Time: 2:19 PM
 *
 * This TermResolver returns TRUE if a student has passed all the courses in the list of courses passed as a parameter.
 *
 * The "list of courses" could be only a single courseId or courseCode, or a courseSetId of a comma seperated list of
 * coursecodes. The CluService is used to retrieve courseCodes based on the courseId and courseSetId.
 *
 * The studentId is passed as a resolvedPrereq.
 *
 */
public class CompletedCoursesTermResolver implements TermResolver<Boolean> {

    @Override
    public Set<String> getPrerequisites() {
        Set<String> temp = new HashSet<String>(2);
        temp.add(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID);
        temp.add(KSKRMSServiceConstants.TERM_PREREQUISITE_CONTEXTINFO);
        return Collections.unmodifiableSet(temp);
    }

    @Override
    public String getOutput() {
        return KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSES;
    }

    @Override
    public Set<String> getParameterNames() {
        return Collections.singleton(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY);
    }

    @Override
    public int getCost() {
        // TODO Analyze, though probably not much to check here
        return 1;
    }

    @Override
    public Boolean resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) throws TermResolutionException {

        //Get the number of completed courses in list.
        TermResolver<Integer> numberOfCompletedCoursesTermResolver = new NumberOfCompletedCoursesTermResolver();
        int completedCourses = numberOfCompletedCoursesTermResolver.resolve(resolvedPrereqs, parameters);

        String cluSetId = parameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY);

        return completedCourses >= 2;
    }
}
