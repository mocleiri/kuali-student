package org.kuali.student.ap.coursesearch.service.impl;

import org.apache.cxf.common.util.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.coursesearch.CreditsFormatter;
import org.kuali.student.ap.coursesearch.dataobject.CourseDetailsPopoverWrapper;
import org.kuali.student.ap.coursesearch.dataobject.CourseDetailsWrapper;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.PlanConstants;
import org.kuali.student.ap.framework.util.KsapHelperUtil;
import org.kuali.student.ap.utils.CourseLinkBuilder;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.infc.RichText;
import org.kuali.student.r2.common.util.date.DateFormatters;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.constants.KSKRMSServiceConstants;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.infc.SearchResult;
import org.kuali.student.r2.core.search.infc.SearchResultRow;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.infc.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inquiry Helper for the CourseDetails-InquiryView
 * @see org.kuali.rice.kns.inquiry.KualiInquirableImpl
 */
public class CourseDetailsInquiryHelperImpl2 extends KualiInquirableImpl {

    private static final long serialVersionUID = 4933435913745621395L;

    private static final Logger LOG = LoggerFactory.getLogger(CourseDetailsInquiryHelperImpl2.class);

    private final String COURSE_DETAILS_INQUIRY_VIEW = "CourseDetails-InquiryView";
    private final String COURSE_DETAILS_POPOVER_INQUIRY_VIEW = "CourseDetailsPopover-InquiryView";

    /**
     * Key to look up a configuration value to determine the sorted terms offered
     */
    private final String TERMS_OFFERED_SORTED_KEY = "ks.ap.search.terms.offered.sorted";

    /**
     * @see org.kuali.rice.kns.inquiry.KualiInquirableImpl#retrieveDataObject(java.util.Map)
     */
    @Override
    public Object retrieveDataObject(@SuppressWarnings("rawtypes") Map fieldValues) {
        String studentId = KsapFrameworkServiceLocator.getUserSessionHelper().getStudentId();
        String viewId = (String) fieldValues.get(PlanConstants.PARAM_VIEW_ID);
        String courseId = (String) fieldValues.get(PlanConstants.PARAM_COURSE_ID);
        String termId = (String) fieldValues.get(PlanConstants.PARAM_TERM_ID);
        boolean loadActivityOfferings = fieldValues.get(PlanConstants.PARAM_OFFERINGS_FLAG) != null
                && Boolean.valueOf(fieldValues.get(PlanConstants.PARAM_OFFERINGS_FLAG).toString());

        if(viewId.equals(COURSE_DETAILS_INQUIRY_VIEW)){
            return retrieveCourseDetails(courseId,termId,studentId,loadActivityOfferings);
        }else if(viewId.equals(COURSE_DETAILS_POPOVER_INQUIRY_VIEW)){
            return retrieveCoursePopoverDetails(courseId);
        }
        return null;
    }

    /**
     * Retreive and compile data needed for the page into a single object
     *
     * @param courseId - Id of the course being displayed
     * @param termId -
     * @param studentId -
     * @param loadActivityOffering -
     * @return Compiled data object for inquiry page
     */
    public CourseDetailsWrapper retrieveCourseDetails(String courseId, String termId, String studentId,
                                                      boolean loadActivityOffering) {
        final CourseInfo course = KsapFrameworkServiceLocator.getCourseHelper().getCourseInfo(courseId);

        CourseDetailsWrapper courseDetails = retrieveCourseSummary(course);

        return courseDetails;
    }

    /**
     * Retriece and compile data needed for the course popover on the course details page.
     *
     * @param courseId - Id of the course being displayed in popover
     * @return Compiled data object for popover
     */
    public CourseDetailsPopoverWrapper retrieveCoursePopoverDetails(String courseId){
        final CourseInfo course = KsapFrameworkServiceLocator.getCourseHelper().getCourseInfo(courseId);

        if(course == null) return null;

        CourseDetailsPopoverWrapper courseDetails = new CourseDetailsPopoverWrapper();

        courseDetails.setCourseId(course.getId());
        courseDetails.setCourseCode(course.getCode());
        courseDetails.setCourseCredits(CreditsFormatter.formatCredits(course));
        courseDetails.setCourseTitle(course.getCourseTitle());

        courseDetails.setCourseRequisites(getCourseRequisites(course));

        // Load Term information
        courseDetails.setScheduledTerms(KsapFrameworkServiceLocator.getCourseHelper()
                .getScheduledTermsForCourse(course));
        courseDetails.setProjectedTerms(getProjectedTerms(course));

        // Load Last Offered Term information if course is not scheduled
        if(courseDetails.getScheduledTerms().isEmpty()){
            Term lastOfferedTerm = KsapFrameworkServiceLocator.getCourseHelper().getLastOfferedTermForCourse(course);
            if (lastOfferedTerm != null){
                courseDetails.setLastOffered(lastOfferedTerm.getName());
            }
            else {
                // If no last offered is found set as null
                courseDetails.setLastOffered(null);
            }
        }else{
            courseDetails.setLastOffered(null);
        }

        //Load plan status information
        List<PlanItem> planItems = KsapFrameworkServiceLocator.getPlanHelper().loadStudentsPlanItemsForCourse(course);
        courseDetails.setPlanStatusMessage(createPlanningStatusMessages(planItems));
        courseDetails.setBookmarkStatusMessage(createBookmarkStatusMessages(planItems));

        return courseDetails;
    }

    /**
     * Populates course details with catalog information (title, id, code, description) and other summary information
     *
     * @param course
     * @return
     */
    public CourseDetailsWrapper retrieveCourseSummary(Course course) {

        if (null == course) {
            return null;
        }

        CourseDetailsWrapper courseDetails = new CourseDetailsWrapper();

        // Load basic information
        courseDetails.setCourseId(course.getId());
        courseDetails.setCourseCode(course.getCode());
        courseDetails.setCourseCredits(CreditsFormatter.formatCredits(course));
        courseDetails.setCourseTitle(course.getCourseTitle());
        String subjectCode = course.getSubjectArea().trim();
        Map<String, String> subjectAreas = KsapFrameworkServiceLocator.getOrgHelper().getTrimmedSubjectAreas();
        courseDetails.setSubject(subjectAreas.get(subjectCode));

        // Load formated information
        courseDetails.setCourseDescription(getCourseDescription(course));
        courseDetails.setCourseRequisites(getCourseRequisites(course));
        courseDetails.setCourseGenEdRequirements(getCourseGenEdRequirements(course));

        // Load Term information
        courseDetails.setScheduledTerms(KsapFrameworkServiceLocator.getCourseHelper()
                .getScheduledTermsForCourse(course));
        courseDetails.setProjectedTerms(getProjectedTerms(course));

        // Load Last Offered Term information if course is not scheduled
        if(courseDetails.getScheduledTerms().isEmpty()){
            Term lastOfferedTerm = KsapFrameworkServiceLocator.getCourseHelper().getLastOfferedTermForCourse(course);
            if (lastOfferedTerm != null){
                courseDetails.setLastOffered(lastOfferedTerm.getName());
            }
            else {
                // If no last offered is found set as null
                courseDetails.setLastOffered(null);
            }
        }else{
            courseDetails.setLastOffered(null);
        }

        //Load plan status information
        List<PlanItem> planItems = KsapFrameworkServiceLocator.getPlanHelper().loadStudentsPlanItemsForCourse(course);
        courseDetails.setPlannedMessage(createPlanningStatusMessages(planItems));
        courseDetails.setBookmarkMessage(createBookmarkStatusMessages(planItems));
        courseDetails.setBookmarked(KsapFrameworkServiceLocator.getCourseHelper().isCourseBookmarked(course, planItems));

        return courseDetails;
    }

    /**
     * Creates a message regarding the Planned status of the course to be displayed on the page
     *
     * @param planItems - The list of plan items related to the course
     * @return - Formated message if bookmarked, "" if not planned
     */
    protected String createPlanningStatusMessages(List<PlanItem> planItems){
        List<String> plannedStatus = new ArrayList<String>();

        // Create message segments for each planned instance
        for(PlanItem item : planItems){
            StringBuilder message = new StringBuilder("<b>");
            switch (item.getCategory()){
                case PLANNED:
                    for(String termId : item.getPlanTermIds()){
                        message.append(KsapFrameworkServiceLocator.getTermHelper().getYearTerm(termId)
                                .getLongName()+ " ");
                    }
                    message.append("plan</b> on " + DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.format(item.getMeta().getUpdateTime()));
                    plannedStatus.add(message.toString());
                    break;
                case BACKUP:
                    for(String termId : item.getPlanTermIds()){
                        message.append(KsapFrameworkServiceLocator.getTermHelper().getYearTerm(termId)
                                .getLongName()+ " ");
                    }
                    message.append("backup</b> on " + DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.format(item.getMeta().getUpdateTime()));
                    plannedStatus.add(message.toString());
                    break;
                default:
                    break;
            }
        }

        // If not planned return empty
        if(plannedStatus.isEmpty()) return "";

        // Compile segments into a single planned summary message
        StringBuilder plannedMessages = new StringBuilder();
        plannedMessages.append("Added to ");
        for(int i=0;i<plannedStatus.size();i++){
            String message = plannedStatus.get(i);
            if(i==0){
                plannedMessages.append(message);
            }else{
                if(i == plannedStatus.size()-1){
                    plannedMessages.append(" and "+message);
                }else{
                    plannedMessages.append(", "+message);
                }
            }
        }

        return plannedMessages.toString();
    }

    /**
     * Creates a message regarding the Bookmark status of the course to be displayed on the page
     *
     * @param planItems - The list of plan items related to the course
     * @return - Formated message if bookmarked, "" if not bookmarked
     */
    protected String createBookmarkStatusMessages(List<PlanItem> planItems){
        for(PlanItem item : planItems){
            // Return message based on first bookmarked entry found (there should be only one).
            if(item.getCategory().equals(AcademicPlanServiceConstants.ItemCategory.WISHLIST)){
                return "<b>Bookmarked on</b> " + DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.format(item.getMeta().getUpdateTime());
            }
        }

        return "";
    }

    /**
     * Retrieve and format the Course description to be displayed on the page
     *
     * @param course - Course that is being displayed
     * @return Formatted course description
     */
    protected String getCourseDescription(Course course){
        String description = "";
        RichText richTextDescription = course.getDescr();
        if(!StringUtils.isEmpty(richTextDescription.getFormatted())){
            description = richTextDescription.getFormatted();
        }else if(!StringUtils.isEmpty(richTextDescription.getPlain())){
            description = richTextDescription.getPlain();
        }

        description = CourseLinkBuilder.makeLinks(description,KsapFrameworkServiceLocator.getContext().getContextInfo());

        return description;
    }

    /**
     * Retrieve and format the list of course requisites to be displayed on the page
     *
     * @param course - Course that is being displayed
     * @return Formatted list of requisites
     */
    protected List<String> getCourseRequisites(Course course){
        List<String> courseRequisites = new ArrayList<String>();

        RuleManagementService rms = KsapFrameworkServiceLocator.getRuleManagementService();
        try{
            // Gather components for natural language translation
            TypeInfo typeInfo = KsapFrameworkServiceLocator.getTypeService().getType(
                    course.getTypeKey(), KsapFrameworkServiceLocator.getContext().getContextInfo());
            List<ReferenceObjectBinding> referenceObjectBindings = rms.findReferenceObjectBindingsByReferenceObject(
                    typeInfo.getRefObjectUri(), course.getId());
            String language = KsapFrameworkServiceLocator.getContext().getContextInfo().getLocale().getLocaleLanguage();

            // Get requisites as natural language descriptions
            for(ReferenceObjectBinding referenceObjectBinding : referenceObjectBindings){
                NaturalLanguageUsage nlu = rms.getNaturalLanguageUsageByNameAndNamespace(
                        KSKRMSServiceConstants.KRMS_NL_RULE_EDIT, referenceObjectBinding.getNamespace());
                String description = rms.translateNaturalLanguageForObject(
                        nlu.getId(),referenceObjectBinding.getKrmsDiscriminatorType().toLowerCase(),
                        referenceObjectBinding.getKrmsObjectId(),language);
                String formattedDescription = CourseLinkBuilder.makeLinks(description,KsapFrameworkServiceLocator.getContext().getContextInfo());
                courseRequisites.add(formattedDescription);

            }
        } catch (PermissionDeniedException e) {
            throw new IllegalArgumentException("Type lookup error", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("Type lookup error", e);
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("Type lookup error", e);
        } catch (OperationFailedException e) {
            throw new IllegalArgumentException("Type lookup error", e);
        } catch (DoesNotExistException e) {
            throw new IllegalArgumentException("Type lookup error", e);
        }

        return courseRequisites;
    }

    /**
     * Retrieve and format the list of general education requirements to be displayed on the page
     *
     * @param course - Course that is being displayed
     * @return Formatted list of general education requirements
     */
    protected List<String> getCourseGenEdRequirements(Course course){
        List<String> courseGenEdRequirements = new ArrayList<String>();

        // Create search for requirements
        String independentVersionId = course.getVersion().getVersionIndId();
        SearchRequestInfo request = new SearchRequestInfo(
                "ksap.course.info.gened");
        request.addParam("courseIDs", independentVersionId);

        // Search for the requirements
        SearchResult result;
        try {
            result = KsapFrameworkServiceLocator.getCluService().search(
                    request,
                    KsapFrameworkServiceLocator.getContext().getContextInfo());
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException(
                    "Invalid course ID or CLU lookup error", e);
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException(
                    "Invalid course ID or CLU lookup error", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("CLU lookup error", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalArgumentException("CLU lookup error", e);
        }

        for (SearchResultRow row : result.getRows()) {
            String code = KsapHelperUtil.getCellValue(row, "gened.code");
            String name = KsapHelperUtil.getCellValue(row, "gened.name");
            courseGenEdRequirements.add(name+" ("+code+")");
        }

        return courseGenEdRequirements;
    }

    /**
     * Retrieve and format the list of projected terms to be displayed on the page
     *
     * @param course - Course that is being displayed
     * @return Formatted list of projected terms
     */
    protected List<String> getProjectedTerms(Course course){
        List<String> courseTermsOffered = course.getTermsOffered();
        Map<String, String> projectedTerms = new HashMap<String, String>();
        if (courseTermsOffered != null) {
            try {
                for (TypeInfo typeInfo : KsapFrameworkServiceLocator.getTypeService().getTypesByKeys(courseTermsOffered,
                        KsapFrameworkServiceLocator.getContext().getContextInfo()))
                    projectedTerms.put(typeInfo.getKey(), typeInfo.getName());
            } catch (org.kuali.student.r2.common.exceptions.DoesNotExistException e) {
                throw new IllegalArgumentException("Type lookup error", e);
            } catch (InvalidParameterException e) {
                throw new IllegalArgumentException("Type lookup error", e);
            } catch (MissingParameterException e) {
                throw new IllegalArgumentException("Type lookup error", e);
            } catch (OperationFailedException e) {
                throw new IllegalStateException("Type lookup error", e);
            } catch (PermissionDeniedException e) {
                throw new IllegalStateException("Type lookup error", e);
            }
        }
        return sortTerms(projectedTerms);
    }

    /**
     * Sort the terms offered (projected) based off of config values
     * @param projectedTerms
     * @return
     */
    private List<String> sortTerms(Map<String, String> projectedTerms) {
        List<String> sortedTerms = new ArrayList<String>();
        String[] terms = ConfigContext.getCurrentContextConfig().getProperty(TERMS_OFFERED_SORTED_KEY).split(",");
        for (int i = 0; i < terms.length; i++) {
            String typeKey = terms[i];
            if (projectedTerms.containsKey(typeKey))
                sortedTerms.add(projectedTerms.get(typeKey));
        }
        return sortedTerms;
    }
}
