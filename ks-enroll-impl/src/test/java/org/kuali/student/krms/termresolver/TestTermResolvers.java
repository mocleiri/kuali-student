package org.kuali.student.krms.termresolver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.student.enrollment.academicrecord.dto.StudentCourseRecordInfo;
import org.kuali.student.enrollment.academicrecord.service.AcademicRecordService;
import org.kuali.student.enrollment.courseoffering.dto.RegistrationGroupInfo;
import org.kuali.student.enrollment.courseoffering.infc.CourseOffering;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.enrollment.courseregistration.dto.RegistrationRequestInfo;
import org.kuali.student.enrollment.courseregistration.service.CourseRegistrationService;
import org.kuali.student.krms.data.KRMSEnrollmentEligibilityDataLoader;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.LocaleInfo;
import org.kuali.student.r2.core.organization.service.OrgTestDataLoader;
import org.kuali.student.r2.core.constants.KSKRMSServiceConstants;
import org.kuali.student.r2.core.organization.service.OrganizationService;
import org.kuali.student.r2.core.population.service.PopulationService;
import org.kuali.student.r2.lum.clu.service.CluService;
import org.kuali.student.r2.lum.course.service.CourseService;
import org.kuali.student.r2.lum.lu.service.impl.CluDataLoader;
import org.kuali.student.r2.lum.lu.service.impl.CluSetDataLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:krms-test-with-mocks-context.xml"})
public class TestTermResolvers {

    public ContextInfo contextInfo = null;
    Map<String, Object> resolvedPrereqs = null;
    Map<String, String> parameters = null;
    private String termID = "1001";
    private String calcTypeID = "mockTypeKey3";

    @Resource(name = "orgServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "cluService")
    private CluService cluService;

    @Resource(name = "courseService")
    private CourseService courseService;

    @Resource(name = "acadRecordService")
    private AcademicRecordService academicRecordService;

    @Resource
    private KRMSEnrollmentEligibilityDataLoader dataLoader;

    @Resource(name = "courseRegistrationService")
    private CourseRegistrationService courseRegistrationService;

    @Resource(name = "courseOfferingService")
    private CourseOfferingService courseOfferingService;

    @Resource(name = "populationService")
    private PopulationService populationService;

    @Before
    public void setUp() {

        contextInfo = new ContextInfo();
        contextInfo.setLocale(new LocaleInfo());
        contextInfo.setPrincipalId("admin");
        dataLoader.setContextInfo(contextInfo);

        loadCluData();
        loadAcadRecordData();
        loadRegistrationData();

        resolvedPrereqs = getDefaultPrerequisites();
        parameters = getDefaultParameters();
    }

    @After
    public void teardown() throws Exception {
        dataLoader.afterTest();
    }

    @Test
    public void testCompletedCourseTermResolver() throws Exception {
        //Setup the term resolver
        CompletedCourseTermResolver termResolver = new CompletedCourseTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluVersionService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        String versionIndId = cluService.getClu("COURSE1", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSES);

        //Evaluate term Resolver
        Boolean isCompleted = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(isCompleted);
        assertTrue(isCompleted);

        //Replace clu parameter with non existing course.
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, "AAA999");

        //Evaluate term Resolver
        isCompleted = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(isCompleted);
        assertFalse(isCompleted);
    }

    @Test
    public void testCompletedCoursesTermResolver() {
        //Setup the term resolver
        CompletedCoursesTermResolver termResolver = new CompletedCoursesTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY, "COURSE-SET2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSES);

        //Evaluate term Resolver
        Boolean isAllCompleted = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(isAllCompleted);
        assertFalse(isAllCompleted);

        //Reset to student 2 and re-evaluate.
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID);
        isAllCompleted = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(isAllCompleted);
        assertTrue(isAllCompleted);
    }

    @Test
    public void testNumberOfCompletedCoursesTermResolver() {
        //Setup the term resolver
        NumberOfCompletedCoursesTermResolver termResolver = new NumberOfCompletedCoursesTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY, "COURSE-SET3");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_NUMBEROFCOMPLETEDCOURSES);

        //Evaluate term Resolver
        Integer numberOfCompletedCourses = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(numberOfCompletedCourses);
        assertEquals(new Integer(1), numberOfCompletedCourses);

        //Reset to student 2 and re-evaluate.
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID);
        numberOfCompletedCourses = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(numberOfCompletedCourses);
        assertEquals(new Integer(2), numberOfCompletedCourses);
    }

    @Test
    public void testNumberOfCreditsTermResolver() {
        //Setup the term resolver
        CompletedCourseCreditsTermResolver termResolver = new CompletedCourseCreditsTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        //parameters.put(KSKRMSServiceConstants.CALC_TYPE_KEY_TERM_PROPERTY, calcTypeID);

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_NUMBEROFCREDITSFROMCOMPLETEDCOURSES);

        //Evaluate term Resolver
        //Integer numberOfCredits = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(numberOfCredits);
        //assertEquals(new Integer(0), numberOfCredits);
    }

    @Test
    public void testEnrolledCourseTermResolver() throws Exception {
        //Setup the term resolver
        EnrolledCourseTermResolver termResolver = new EnrolledCourseTermResolver();
        termResolver.setCourseRegistrationService(courseRegistrationService);
        termResolver.setCluService(cluService);
        termResolver.setCourseOfferingService(courseOfferingService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID);
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_TERM_ID,dataLoader.getFallTermId());
        String versionIndId = cluService.getClu("COURSE8", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ENROLLEDCOURSE);

        //Evaluate term Resolver
        //Boolean isEnrolled = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isEnrolled);
        //assertTrue(isEnrolled);
    }

    @Test
    public void testEnrolledCoursesTermResolver() {
        //Setup the term resolver
        EnrolledCoursesTermResolver termResolver = new EnrolledCoursesTermResolver();
        termResolver.setCourseRegistrationService(courseRegistrationService);
        termResolver.setCluService(cluService);
        termResolver.setCourseOfferingService(courseOfferingService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_TERM_ID, dataLoader.getFallTermId());
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ENROLLEDCOURSES);

        //Evaluate term Resolver
        //Boolean isAllEnrolled = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isAllEnrolled);
        //assertTrue(isAllEnrolled);
    }

    @Test
    public void testNumberOfEnrolledCoursesTermResolver() {
        //Setup the term resolver
        NumberOfEnrolledCoursesTermResolver termResolver = new NumberOfEnrolledCoursesTermResolver();
        termResolver.setCourseRegistrationService(courseRegistrationService);
        termResolver.setCluService(cluService);
        termResolver.setCourseOfferingService(courseOfferingService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY, "DTC101");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_NUMBEROFENROLLEDCOURSES);

        //Evaluate term Resolver
        //Integer numberOfEnrolledCourses = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(numberOfEnrolledCourses);
        //assertEquals(new Integer(0), numberOfEnrolledCourses);
    }

    @Test
    public void testGPATermResolver() {
        //Setup the term resolver
        GPATermResolver termResolver = new GPATermResolver();
        termResolver.setAcademicRecordService(academicRecordService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, "DTC101");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_GPAFORCOURSES);

        //Evaluate term Resolver
        Integer gpa = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(gpa);
        //assertEquals(new Integer(0), gpa);
    }

    @Test
    public void testGradeTypeTermResolver() {
        //Setup the term resolver
        GradeTypeTermResolver termResolver = new GradeTypeTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLUSET_KEY, "1");
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_KEY, "1");
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_TYPE_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_GRADETYPEFORCOURSES);

        //Evaluate term Resolver
        Integer gradeType = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(gradeType);
        //assertEquals(new Integer(0), gradeType);
    }

    @Test
    public void testOrganizationCreditsTermResolver() {
        //Setup the term resolver
        OrganizationCreditsTermResolver termResolver = new OrganizationCreditsTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_ORGANIZATION_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_NUMBEROFCREDITSFROMORGANIZATION);

        //Evaluate term Resolver
        Integer credits = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(credits);
        //assertEquals(new Integer(0), credits);
    }

    @Test
    public void testAdminOrgPermissionRequiredTermResolver() {
        OrgTestDataLoader orgDataLoader = new OrgTestDataLoader(organizationService);
        orgDataLoader.loadData();

        //Setup the term resolver
        AdminOrgPermissionTermResolver termResolver = new AdminOrgPermissionTermResolver();
        termResolver.setOrganizationService(organizationService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_ORGANIZATION_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ADMINORGANIZATIONPERMISSIONREQUIRED);

        //Evaluate term Resolver
        Boolean hasPermission = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(hasPermission);
        assertTrue(hasPermission);
    }

    @Test
    public void testScoreTermResolver() {
        //Setup the term resolver
        ScoreTermResolver termResolver = new ScoreTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TEST_CLU_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_SCOREONTEST);

        //Evaluate term Resolver
        //Integer score = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(score);
        //assertEquals(new Integer(0), score);
    }

    @Test
    public void testFreeTextTermResolver() {
        //Setup the term resolver
        FreeFormTextTermResolver termResolver = new FreeFormTextTermResolver();

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_FREEFORMTEXT);

        //Evaluate term Resolver
        Boolean result = termResolver.resolve(resolvedPrereqs, parameters);
        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testProgramCoursesOrgDurationTermResolver() {
        //Setup the term resolver
        ProgramCoursesOrgDurationTermResolver termResolver = new ProgramCoursesOrgDurationTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_ORGANIZATION_KEY, "1");
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_DURATION_KEY, "1");
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_DURATION_TYPE_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ADMITTEDTOPROGRAMLIMITCOURSESINORGFORDURATION);

        //Evaluate term Resolver
        Integer result = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(result);
        //assertEquals(new Integer(0), result);
    }

    @Test
    public void testAdmittedToProgramTermResolver() {
        //Setup the term resolver
        AdmittedProgramTermResolver termResolver = new AdmittedProgramTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ADMITTEDTOPROGRAM);

        //Evaluate term Resolver
        Boolean isAdmitted = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isAdmitted);
        //assertTrue(isAdmitted);
    }

    @Test
    public void testAdmittedToProgramAtCourseCampusTermResolver() {
        //Setup the term resolver
        AdmittedProgramAtCourseCampusTermResolver termResolver = new AdmittedProgramAtCourseCampusTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_CLU_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ADMITTEDTOPROGRAMATCOURSECAMPUS);

        //Evaluate term Resolver
        Boolean isAdmitted = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isAdmitted);
        //assertTrue(isAdmitted);
    }

    @Test
    public void testAdmittedToProgramWithClassStandingTermResolver() {
        //Setup the term resolver
        AdmittedProgramTermResolver termResolver = new AdmittedProgramTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_ADMITTEDTOPROGRAM);

        //Evaluate term Resolver
        Boolean isAdmitted = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isAdmitted);
        //assertTrue(isAdmitted);
    }

    @Test
    public void testCompletedCourseForTermTermResolver() throws Exception{
        //Setup the term resolver
        CompletedCourseForTermTermResolver termResolver = new CompletedCourseForTermTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluVersionService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        String versionIndId = cluService.getClu("COURSE1", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSEFORTERM);

        //Evaluate term Resolver
        Boolean completed = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(completed);
        //assertEquals(new Integer(0), completed);
    }

    @Test
    public void testCompletedCourseAsOfTermTermResolver() throws Exception{
        //Setup the term resolver
        CompletedCourseAsOfTermTermResolver termResolver = new CompletedCourseAsOfTermTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluVersionService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        String versionIndId = cluService.getClu("COURSE1", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSEFORTERM);

        //Evaluate term Resolver
        Boolean completed = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(completed);
        //assertEquals(new Integer(0), completed);
    }

    @Test
    public void testCompletedCoursePriorToTermTermResolver() throws Exception{
        //Setup the term resolver
        CompletedCoursePriorToTermTermResolver termResolver = new CompletedCoursePriorToTermTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluVersionService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        String versionIndId = cluService.getClu("COURSE1", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY, "2");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSEFORTERM);

        //Evaluate term Resolver
        Boolean completed = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(completed);
        //assertEquals(new Integer(0), completed);
    }

    @Test
    public void testCompletedCourseBetweenTermsTermResolver() throws Exception {
        //Setup the term resolver
        CompletedCourseBetweenTermsTermResolver termResolver = new CompletedCourseBetweenTermsTermResolver();
        termResolver.setAcademicRecordService(academicRecordService);
        termResolver.setCluVersionService(cluService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        String versionIndId = cluService.getClu("COURSE1", contextInfo).getVersion().getVersionIndId();
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, versionIndId);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY, "2");
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE2_KEY, "3");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_COMPLETEDCOURSEBETWEENTERMS);

        //Evaluate term Resolver
        Boolean isCompleted = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isCompleted);
        //assertTrue(isCompleted);
    }

    @Test
    public void testClassStandingTermResolver() {
        //Setup the term resolver
        ClassStandingTermResolver termResolver = new ClassStandingTermResolver();
        termResolver.setPopulationService(populationService);

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        parameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLASS_STANDING_KEY, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_CLASSSTANDING);

        //Evaluate term Resolver
        //Boolean isInClassStanding = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(isInClassStanding);
        //assertTrue(isInClassStanding);

    }

    @Test
    public void testInstructorPermissionTermResolver() {
        //Setup the term resolver
        InstructorPermissionTermResolver termResolver = new InstructorPermissionTermResolver();

        //Setup data
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_PERSON_ID, KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID);
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_CLU_ID, "1");

        //Validate the term resolver
        validateTermResolver(termResolver, resolvedPrereqs, parameters,
                KSKRMSServiceConstants.TERM_RESOLVER_INSTRUCTORPERMISSION);

        //Evaluate term Resolver
        Boolean hasPermission = termResolver.resolve(resolvedPrereqs, parameters);
        //assertNotNull(hasPermission);
        //assertTrue(hasPermission);
    }

    //@Test
    //public void testNumberOfEnrollmentsForCourseTermResolver() {
    //Setup the term resolver
    //    NumberOfEnrollmentsForCourseTermResolver termResolver = new NumberOfEnrollmentsForCourseTermResolver();
    //}

    private void validateTermResolver(TermResolver termResolver, Map<String, Object> prereqs, Map<String, String> parameters, String output) {

        //Check the term name.
        assertEquals(output, termResolver.getOutput());

        //Check if prerequisites are listed
        validateAttributeSet("Prerequisites list does not contain ", prereqs.keySet(), termResolver.getPrerequisites());

        //Check if paramteters are listed
        validateAttributeSet("Parameters list does not contain ", parameters.keySet(), termResolver.getParameterNames());
    }

    private void validateAttributeSet(String message, Set<String> names, Set<String> keys) {
        if (keys == null) {
            return;
        }

        for (String key : keys) {
            assertTrue(message + key, names.contains(key));
        }
    }

    private Map<String, Object> getDefaultPrerequisites() {
        Map<String, Object> resolvedPrereqs = new HashMap<String, Object>();
        resolvedPrereqs.put(KSKRMSServiceConstants.TERM_PREREQUISITE_CONTEXTINFO, contextInfo);
        return resolvedPrereqs;
    }

    private Map<String, String> getDefaultParameters() {
        Map<String, String> parameters = new HashMap<String, String>();
        return parameters;
    }

    private void loadCluData() {
        CluDataLoader cluDataLoader = new CluDataLoader();
        cluDataLoader.setCluService(cluService);
        cluDataLoader.setContextInfo(contextInfo);
        cluDataLoader.load();

        CluSetDataLoader cluSetDataLoader = new CluSetDataLoader();
        cluSetDataLoader.setCluService(cluService);
        cluSetDataLoader.setContextInfo(contextInfo);
        cluSetDataLoader.load();
    }

    private void loadAcadRecordData() {

        try {
            dataLoader.beforeTest();

            // setup the test data for student 1
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID, dataLoader.getFallTermId(), "COURSE1");
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID, dataLoader.getFallTermId(), "COURSE2");
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_ONE_ID, dataLoader.getFallTermId(), "COURSE4");

            // setup the test data for student 2
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE2");
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE3");
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE4");
            createStudentCourseRecord(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE5");

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void createStudentCourseRecord(String personId, String termId, String courseId) throws Exception {
        CourseOffering courseOffering = dataLoader.getCourseOffering(courseId, termId);
        StudentCourseRecordInfo courseRecord = dataLoader.createStudentCourseRecord(personId, termId, courseOffering.getCourseCode(), courseOffering.getCourseOfferingTitle());
        courseRecord.setCourseOfferingId(courseOffering.getId());
        dataLoader.storeStudentCourseRecord(personId, termId, courseId, courseRecord);
    }

    private void loadRegistrationData() {
        try {
            // setup the test data for student 1
            RegistrationRequestInfo regReqInfo = createRegistrationRequest(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE3", "COURSE5");
            dataLoader.submitRegistrationRequest(regReqInfo.getId());
            // setup the test data for student 2
            createRegistrationRequest(KRMSEnrollmentEligibilityDataLoader.STUDENT_TWO_ID, dataLoader.getFallTermId(), "COURSE6", "COURSE7", "COURSE8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RegistrationRequestInfo createRegistrationRequest(String studentId, String termId, String... courseIds) throws Exception {
        RegistrationRequestInfo request = dataLoader.createRegistrationRequest(studentId, termId);
        for(String courseId : courseIds){
            RegistrationGroupInfo regGroup = dataLoader.getRegistrationGroup(courseId, termId);
            request.getRegistrationRequestItems().add(dataLoader.createRegistrationItem(studentId, regGroup.getId()));
        }
        return dataLoader.persistRegistrationRequest(request);
    }

}

