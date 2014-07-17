package org.kuali.student.ap.framework.context;

import java.util.List;

import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.coursesearch.CourseSearchItem;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingDisplayInfo;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.infc.Course;

/**
 * Helper class that provides some convenience methods around Course interactions.
 */
public interface CourseHelper {

    /**
     * Pre-load the listed courses and terms (and related objects) and add them to a cache so that they are available for future calls
     * @param courseIds
     * @param termId
     */
	void frontLoad(List<String> courseIds, String... termId);

    /**
     * returns the courseInfo for the given courseId by verifying the courseId to
     * be a verifiedCourseId
     *
     * @param courseId
     * @return
     */
	CourseInfo getCourseInfo(String courseId);
	
	/**
	 * Gets a list of activity offerings for a course.
	 * 
	 * @param courseId
	 *            course ID
	 * @return list of activity offerings
	 */
	List<ActivityOfferingInfo> getActivityOfferings(String courseId);

    /**
     * Get the list of ActivityOfferingDisplayInfo objects for the given courseId and term
     * @param courseId
     * @param term
     * @return
     */
	List<ActivityOfferingDisplayInfo> getActivityOfferingDisplaysByCourseAndTerm(String courseId, String term);

    /**
     * Get the last/latest offered term in the past for a course
     * @param course
     * @return  the last offered term
     */
	Term getLastOfferedTermForCourse(Course course);


    /**
     * Get a list of term ids where the given course is scheduled
     * @param course
     * @return
     */
	List<String> getScheduledTermsForCourse(Course course);

    /**
     * Get a list of offerings for a list of courses in a given list of terms
     * @param courses - List of courses
     * @return List of all offerings for each course id that occurs during one of the listed terms
     */
    List<CourseOfferingInfo> getCourseOfferingsForCourses(List<CourseSearchItem> courses);

    /**
     * Get a list of offerings for a list of course ids in a given list of terms
     * @param courseIds - List of courses
     * @param terms - List of terms
     * @return List of all offerings for each course id that occurs during one of the listed terms
     */
    List<CourseOfferingInfo> getCourseOfferingsForCoursesAndTerms(List<String> courseIds, List<Term> terms);

    /**
     * returns a SLN for given params
     *
     * @param year
     * @param term
     * @param subject
     * @param number
     * @param activityCd
     * @return
     */
	String getSLN(String year, String term, String subject, String number,
			String activityCd);

    /**
     * Takes a courseId that can be either a version independent Id or a version
     * dependent Id and returns a version dependent Id. In case of being passed
     * in a version depend
     *
     * @param courseId
     * @return
     */
	String getCurrentVersionIdOfCourse(String courseId);

    /**
     * Takes a courseId that can be either a version independent Id or a version
     * dependent Id and returns a version dependent course. In case of being passed
     * in a version depend
     *
     * @param courseId
     * @return
     */
    Course getCurrentVersionOfCourse(String courseId);

    /**
     * returns the course code from given activityId
     * <p/>
     * eg: for activityId '2013:2:CHEM:152:A' course code CHEM 152 is returned
     *
     * @param activityId
     * @return
     */
	String getCourseCdFromActivityId(String activityId);

    /**
     * Get a list of Course objects that have a matching course code
     * @param courseCd Course code used to find all courses
     * @return List of Course objects
     */
	List<Course> getCoursesByCode(String courseCd);

    /**
     * Determines whether a course is in a specific term.
     *
     * @param term Term to search for the given course
     * @param course Course to find within the given term
     * @return True if found, false if not
     */
    boolean isCourseOffered(Term term, Course course);

    /**
     * Determine if the course is already bookmarked in the student's plan
     *
     * @param course - Course that is being displayed
     * @param planItems - The list of plan items for the course
     * @return True if the course is already bookmarked for the plan
     */
    boolean isCourseBookmarked(Course course, List<PlanItem> planItems);

}
