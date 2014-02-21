package org.kuali.student.ap.test.mock;

import org.kuali.student.ap.framework.context.CourseHelper;
import org.kuali.student.ap.framework.context.DeconstructedCourseCode;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingDisplayInfo;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.infc.Course;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: johglove
 * Date: 11/19/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CourseHelperMockTest implements CourseHelper {
    @Override
    public void frontLoad(List<String> courseIds, String... termId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CourseInfo getCourseInfo(String courseId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ActivityOfferingDisplayInfo> getActivityOfferingDisplaysByCourseAndTerm(String courseId, String term) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, Map<String, Object>> getAllSectionStatus(Map<String, Map<String, Object>> mapmap, String termId, String courseId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DeconstructedCourseCode getCourseDivisionAndNumber(String courseCode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLastOfferedTermId(Course course) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getScheduledTerms(Course course) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCourseId(String subjectArea, String number) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCourseIdForTerm(String subjectArea, String number, String termId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String buildActivityRefObjId(String atpId, String subject, String number, String activityCd) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSLN(String year, String term, String subject, String number, String activityCd) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String joinStringsByDelimiter(char delimiter, String... list) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getVerifiedCourseId(String courseId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCourseCdFromActivityId(String activityId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCodeFromActivityId(String activityId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Course> getCoursesByCode(String courseCd) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Determines whether a course is in a specific term.
     *
     * @param term
     * @param course
     * @return
     */
    @Override
    public boolean isCourseOffered(Term term, Course course) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
