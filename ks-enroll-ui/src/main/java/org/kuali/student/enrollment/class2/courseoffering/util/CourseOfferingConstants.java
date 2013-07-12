package org.kuali.student.enrollment.class2.courseoffering.util;

/**
 * Created by IntelliJ IDEA.
 * User: huangb
 * Date: 5/10/12
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseOfferingConstants {

    //  Keys for features which are institutionally configurable via config file params
    public final static String CONFIG_PARAM_KEY_SELECTIVE_CROSSLISTING = "kuali.ks.enrollment.options.selective-crosslisting-allowed";
    public final static String CONFIG_PARAM_KEY_EDIT_ACTIVITY_CODE = "kuali.ks.enrollment.options.edit-activity-offering-code-allowed";

    //Attributes defined in CourseOfferingInfo
    public final static String COURSEOFFERING_ID = "id";
    public final static String COURSEOFFERING_TERM_ID = "termId";
    public final static String COURSEOFFERING_SUBJECT_AREA = "subjectArea";
    public final static String COURSEOFFERING_SUBJECT_CODE = "subjectCode";
    public final static String COURSEOFFERING_COURSE_OFFERING_CODE = "courseOfferingCode";
    public final static String ATP_CODE = "atpCode";
    public final static String ATP_ID = "atpId";

    // display name for 'Course' option in Grade Roster Level
    public final static String FORMAT_OFFERING_GRADE_ROSTER_LEVEL_COURSE_DISPLAY = "Course";

    //Attributes defined in SocRolloverResultInfo
    public final static String SOCROLLOVERRESULTINFO_ID = "id";
    public final static String SOCROLLOVERRESULTINFO_SOURCE_TERM_ID = "sourceTermId";
    public final static String SOCROLLOVERRESULTINFO_TARGET_TERM_ID = "targetTermId";

    //Attributes defined in SocRolloverResultItemInfo

    //Message keys
    public static final String COURSEOFFERING_MSG_ERROR_TERMCODE_IS_REQUIRED = "error.courseOffering.manage.termcode.required";
    public static final String COURSEOFFERING_MSG_ERROR_COURSECODE_IS_REQUIRED = "error.courseOffering.manage.coursecode.required";
    public static final String COURSEOFFERING_MSG_ERROR_NO_TERM_IS_FOUND = "error.enroll.courseoffering.noTermIsFound";
    public static final String COURSEOFFERING_MSG_ERROR_FOUND_MORE_THAN_ONE_TERM = "error.enroll.courseoffering.findMoreThanOneTerm";
    public static final String COURSEOFFERING_MSG_ERROR_NO_COURSE_OFFERING_IS_FOUND = "error.enroll.courseoffering.noCourseOfferingIsFound";
    public static final String COURSEOFFERING_MSG_ERROR_FOUND_MORE_THAN_ONE_COURSE_OFFERING = "error.enroll.courseoffering.findMoreThanOneCourseOffering";
    public static final String COURSEOFFERING_MSG_ERROR_FOUND_NO_DRAFT_AO_SELECTED = "error.courseoffering.no.selected.AO";
    public static final String AO_NOT_DRAFT_FOR_DELETION_ERROR = "error.courseoffering.selected.aos.notdraft";
    public static final String COURSEOFFERING_MSG_ERROR_SELECTED_AO_TO_DELETE = "error.courseoffering.selected.AO.delete.confirmation";
    public static final String COURSEOFFERING_INVALID_STATE_FOR_DELETE = "error.enroll.courseoffering.delete.invalid.state";
    public static final String COURSEOFFERING_INVALID_AO_STATE_FOR_DELETE = "error.enroll.courseoffering.delete.invalid.ao.state";
    public static final String COURSEOFFERING_WITH_AO_DRAFT_APPROVED_ONLY = "error.courseoffering.aodraftonlyapproved";
    public static final String COURSEOFFERING_WITH_AO_ORG_APPROVED_ONLY = "error.courseoffering.aoorgonlyapproved";
    public static final String COURSEOFFERING_NONE_APPROVED = "error.courseoffering.noCOsApproved";
    public static final String COURSE_OFFERING_STATE_CHANGE_ERROR = "error.courseoffering.statechange";
    public static final String NO_AOS_SELECTED = "error.courseoffering.selected.aos.none";
    public static final String COURSEOFFERING_NONE_SELECTED = "error.courseoffering.selected.none";

    public static final String COURSEOFFERING_APPROVE_FOR_SCHEDULING_NO_AOS_UPDATED = "error.courseoffering.manage_aos.approve_for_scheduling.no_aos_updated";
    public static final String COURSEOFFERING_APPROVE_FOR_SCHEDULING_SOME_AOS_UPDATED = "error.courseoffering.manage_aos.approve_for_scheduling.some_aos_updated";
    public static final String COURSEOFFERING_SET_TO_DRAFT_NO_AOS_UPDATED = "error.courseoffering.manage_aos.set_to_draft.no_aos_updated";
    public static final String COURSEOFFERING_SET_TO_DRAFT_SOME_AOS_UPATED = "error.courseoffering.manage_aos.set_to_draft.some_aos_updated";

    public static final String REGISTRATIONGROUP_MISSING_REGGROUPS = "error.enroll.registrationgroup.missingreggroups";
    public static final String REGISTRATIONGROUP_INVALID_REGGROUPS = "error.enroll.registrationgroup.invalidreggroups";
    public static final String REGISTRATIONGROUP_INCOMPLETE_AOSET = "error.enroll.registrationgroup.aosetnotcomplete";

    public static final String ACTIVITY_OFFERING_SCHEDULING_ACTION = "Scheduling";
    public static final String ACTIVITY_OFFERING_DRAFT_ACTION = "Draft";
    public static final String ACTIVITY_OFFERING_DELETE_ACTION = "Delete";

    public static final String MANAGE_AO_PAGE = "manageActivityOfferingsPage";
    public static final String MANAGE_CO_PAGE = "manageCourseOfferingsPage";
    public static final String MANAGE_CO_LIST_SECTION = "KS-CourseOfferingManagement-CourseOfferingResultSection";
    public static final String CO_DELETE_CONFIRM_PAGE = "coDeleteConfirmationPage";
    public static final String AO_DELETE_CONFIRM_PAGE = "selectedAoDeleteConfirmationPage";
    public static final String REG_GROUP_PAGE = "manageRegistrationGroupsPage";
    public static final String COPY_CO_PAGE = "copyCourseOfferingPage";
    public static final String SEARCH_PAGE = "searchInputPage";
    public static final String SEARCH_PAGE_POC = "searchInputPagePOC";
    public static final String SEARCH_PAGE_JSON_POC = "searchInputPageJSONPOC";
    public static final String MANAGE_THE_CO_PAGE = "manageTheCourseOfferingPage";
    public static final String MANAGE_ARG_CO_PAGE = "manageCourseOfferingsPage";
    public static final String MANAGE_ARG_DELETE_CLUSTER_CONFIRM_PAGE = "ClusterDeleteConfirmationPage";


    // Display text
    public static final String COURSEOFFERING_TEXT_STD_REG_OPTS_EMPTY = "None available";

    /**
     * Course Offering create constants
     */
    public static final String COURSEOFFERING_ERROR_CREATE_DUPLICATECODE = "error.enroll.courseoffering.create.duplicatecode";
    public static final String DELIVERY_FORMAT_SECTION_ID = "KS-Catalog-FormatOfferingSubSection";
    public static final String DELIVERY_FORMAT_REQUIRED_ERROR = "error.courseoffering.create.deliveryformat.required";
    public static final String JOINT_COURSE_FORMATS_DELETE_DIALOG = "jointCourseFormatsDeleteDialog";

    public static final String ERROR_INVALID_CLU_VERSION = "error.enroll.courseoffering.create.clu.version.invalid";
    /**
     * Course Offering Informational Constants
     */
    public static final String COURSEOFFERING_INFO_COPIED_SUCCESSFULLY = "info.enroll.courseoffering.copied.successful";
    public static final String COURSEOFFERING_ROLLOVER_RELEASE_TO_DEPTS_SUCCESSFULLY = "info.enroll.courseoffering.rollover.releaseToDepts.successful";

    //JSON string root keys
    public static final String BREADCRUMB_JSON_ROOT_KEY = "breadCrumb";
    public static final String POPULATIONS_JSON_ROOT_KEY = "populations";

    public static final String MANAGE_CO_CONTROLLER_PATH = "courseOfferingManagement";
    public static final String MANAGE_CO_VIEW_ID = "courseOfferingManagementView";

    //toolbar matrix -- result/warning messages
    public static final String ACTIVITYOFFERING_TOOLBAR_DELETE="info.enroll.activityoffering.toolbar.delete";
    public static final String ACTIVITYOFFERING_TOOLBAR_DRAFT="info.enroll.activityoffering.toolbar.draft" ;
    public static final String ACTIVITYOFFERING_TOOLBAR_APPROVED="info.enroll.activityoffering.toolbar.approved" ;
    public static final String COURSEOFFERING_TOOLBAR_DELETE="info.enroll.courseoffering.toolbar.delete" ;
    public static final String COURSEOFFERING_TOOLBAR_APPROVED="info.enroll.courseoffering.toolbar.approved" ;
    public static final String COURSEOFFERING_TOOLBAR_ADD="info.enroll.courseoffering.toolbar.add.success";

    public static final String ACTIVITYOFFERING_TOOLBAR_ADD_N_SUCCESS="info.enroll.activityoffering.toolbar.add.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_DELETE_N_SUCCESS="info.enroll.activityoffering.toolbar.delete.n.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_DRAFT_N_SUCCESS="info.enroll.activityoffering.toolbar.draft.n.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_APPROVED_N_SUCCESS="info.enroll.activityoffering.toolbar.approved.n.success";
    public static final String COURSEOFFERING_TOOLBAR_DELETE_N_SUCCESS="info.enroll.courseoffering.toolbar.delete.n.success";
    public static final String COURSEOFFERING_TOOLBAR_APPROVED_N_SUCCESS="info.enroll.courseoffering.toolbar.approved.n.success";

    public static final String ACTIVITYOFFERING_TOOLBAR_ADD_1_SUCCESS="info.enroll.activityoffering.toolbar.add.1.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_DELETE_1_SUCCESS="info.enroll.activityoffering.toolbar.delete.1.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_DRAFT_1_SUCCESS="info.enroll.activityoffering.toolbar.draft.1.success";
    public static final String ACTIVITYOFFERING_TOOLBAR_APPROVED_1_SUCCESS="info.enroll.activityoffering.toolbar.approved.1.success";
    public static final String COURSEOFFERING_TOOLBAR_DELETE_1_SUCCESS="info.enroll.courseoffering.toolbar.delete.1.success";
    public static final String COURSEOFFERING_TOOLBAR_APPROVED_1_SUCCESS="info.enroll.courseoffering.toolbar.approved.1.success";

    public static final String ACTIVITYOFFERING_TOOLBAR_ADD_INVALID_ERROR="error.enroll.activityoffering.toolbar.add.invalid";
    
    //controller path
    public static final String CONTROLLER_PATH_COURSEOFFERING_BASE_MAINTENANCE = "courseOffering";
    public static final String CONTROLLER_PATH_COURSEOFFERING_CREATE_MAINTENANCE = "courseOfferingCreate";

    // Final Exam Types
    public final static String COURSEOFFERING_FINAL_EXAM_TYPE_STANDARD = "STANDARD";
    public final static String COURSEOFFERING_FINAL_EXAM_TYPE_ALTERNATE = "ALTERNATE";
    public final static String COURSEOFFERING_FINAL_EXAM_TYPE_NONE = "NONE";

    //colocation
    public static final String COLOCATION_MAX_ENR_SHARED="info.enroll.courseoffering.colocation.enr.shared";
    public static final String COLOCATION_MAX_ENR_SEPARATED="info.enroll.courseoffering.colocation.enr.separated";

    public static final String COURSE_OFFERING_EDIT_SUCCESS = "info.enroll.courseoffering.edit.success";

    /**
         * These are the confirm dialog bean ids used in Manage CO/AO view.
         */
    public static class ConfirmDialogs{
        public static final String DELETE_COLO_COS = "deletingColocationNotSupportedForMultipleCosDialog";
        public static final String DELETE_ONE_COLO_CO = "deletingColocationNotSupportedForCoDialog";
        public static final String DELETE_COLO_AOS = "deletingColocationNotSupportedForMultipleAosDialog";
        public static final String DELETE_ONE_COLO_AO = "deletingColocationNotSupportedForAoDialog";
    }
}
