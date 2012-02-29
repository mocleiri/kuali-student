package org.kuali.student.r2.lum.util.constants;


import org.kuali.student.r2.common.constants.CommonServiceConstants;
import org.kuali.student.r2.lum.course.dto.CourseInfo;

/**
 * Course Service contants.
 * 
 * @author Kuali Student Team (sambitpa@kuali.org)
 */
public class CourseServiceConstants {
    public static final String NAMESPACE = CommonServiceConstants.REF_OBJECT_URI_GLOBAL_PREFIX + "course";
    public static final String REF_OBJECT_URI_COURSE_OFFERING = NAMESPACE + "/" + CourseInfo.class.getSimpleName();
	// TODO KSCM NINA confirm this is ok, copied these constants from CM-1.2.2
    public static final String COURSE_NAMESPACE = "http://student.kuali.org/wsdl/course";
    public static final String COURSE_NAMESPACE_URI = "{" + COURSE_NAMESPACE + "}courseInfo";


}
