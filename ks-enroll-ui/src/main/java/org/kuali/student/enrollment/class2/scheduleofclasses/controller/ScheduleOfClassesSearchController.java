/**
 * Copyright 2012 The Kuali Foundation Licensed under the
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
 * Created by vgadiyak on 9/10/12
 */
package org.kuali.student.enrollment.class2.scheduleofclasses.controller;

/**
 * This class provides a controller for the Schedule of Classes ui
 *
 * @author Kuali Student Team
 */

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingClusterWrapper;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingWrapper;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.class2.scheduleofclasses.dto.CourseOfferingDisplayWrapper;
import org.kuali.student.enrollment.class2.scheduleofclasses.form.ScheduleOfClassesSearchForm;
import org.kuali.student.enrollment.class2.scheduleofclasses.service.ScheduleOfClassesViewHelperService;
import org.kuali.student.enrollment.class2.scheduleofclasses.util.ScheduleOfClassesConstants;
import org.kuali.student.enrollment.class2.scheduleofclasses.util.ScheduleOfClassesUtil;
import org.kuali.student.enrollment.courseofferingset.service.CourseOfferingSetService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.kuali.student.r2.core.class1.search.ActivityOfferingSearchServiceImpl;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.dto.TypeTypeRelationInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.constants.AtpServiceConstants;
import org.kuali.student.r2.core.constants.TypeServiceConstants;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/scheduleOfClassesSearch")
public class ScheduleOfClassesSearchController extends UifControllerBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ScheduleOfClassesSearchController.class);

    private static final String MODEL_ATTRIBUTE_FORM = "KualiForm";
    private static final String SEARCH_TYPE_COURSE = "course";
    private static final String SEARCH_FIELD = "course";
    private static final String ERROR_SEARCH_FIELD_CANNOT_BE_EMPTY = "Error: search field can't be empty";

    private ScheduleOfClassesViewHelperService viewHelperService;
    private AcademicCalendarService acalService;
    private CourseOfferingSetService courseOfferingSetService;
    private AtpService atpService;
    private TypeService typeService;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new ScheduleOfClassesSearchForm();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=start")
    public ModelAndView start( @ModelAttribute( MODEL_ATTRIBUTE_FORM ) UifFormBase form,
                               BindingResult result, HttpServletRequest request, HttpServletResponse response) {

        ScheduleOfClassesSearchForm scheduleOfClassesSearchForm = (ScheduleOfClassesSearchForm) form;
        scheduleOfClassesSearchForm.setSearchType( SEARCH_TYPE_COURSE );

        configureCurrentTermCodeOnInitialRequest( scheduleOfClassesSearchForm );
        configureDefaultAoDisplayFormat( scheduleOfClassesSearchForm );
        configureSelectableAoRenderingWidget( scheduleOfClassesSearchForm, request );

        return super.start(form, result, request, response);
    }

    private void configureCurrentTermCodeOnInitialRequest( ScheduleOfClassesSearchForm form ) {
        if (form.getTermCode() == null) {
            ContextInfo context = ContextUtils.getContextInfo();
            List<AtpInfo> atps = ScheduleOfClassesUtil.getValidSocTerms(getCourseOfferingSetService(), getAtpService(), context);
            if (!atps.isEmpty()) {
                form.setTermCode(ScheduleOfClassesUtil.getCurrentAtp(atps).getId());
            }
        }
    }

    private void configureDefaultAoDisplayFormat( ScheduleOfClassesSearchForm form ) {
        String aoDisplayFormat = ConfigContext.getCurrentContextConfig().getProperty(ScheduleOfClassesConstants.ConfigProperties.AO_DISPLAY_FORMAT);
        if (StringUtils.isNotBlank(aoDisplayFormat)){
            ScheduleOfClassesSearchForm.AoDisplayFormat format = ScheduleOfClassesSearchForm.AoDisplayFormat.valueOf(StringUtils.upperCase(aoDisplayFormat));
            form.setAoDisplayFormat(format);
        }
    }

    private void configureSelectableAoRenderingWidget( ScheduleOfClassesSearchForm form, HttpServletRequest request ) {

        boolean displaySelectableAoRenderingWidget = false;

        // URL-parameter overrides setting in kuali config file
        if( request.getParameter( "showAoDisplayWidget" ) != null ) {
            displaySelectableAoRenderingWidget = Boolean.valueOf( request.getParameter( "showAoDisplayWidget" ) );
        }

        // Setting in kuali config file is applied if URL-parameter was not supplied
        // (and in event kuali config file does not specify, then UI widget will be set to not show)
        else {
            String allowSelectableAoRendering = ConfigContext.getCurrentContextConfig().getProperty( ScheduleOfClassesConstants.ConfigProperties.ALLOW_SELECTABLE_AO_RENDERING );
            displaySelectableAoRenderingWidget = Boolean.valueOf( allowSelectableAoRendering );
        }

        form.setAllowSelectableAoRendering(displaySelectableAoRenderingWidget);
    }

    /**
     * Method used to
     * Search for course offerings based on search parameters: term and courseCode/Title&Desc/Instructor/Department
     */
    @RequestMapping(params = "methodToCall=show")
    public ModelAndView show(@ModelAttribute( MODEL_ATTRIBUTE_FORM ) ScheduleOfClassesSearchForm theForm)
            throws Exception, MissingParameterException, DoesNotExistException, PermissionDeniedException, OperationFailedException
    {

        //First, find termName based on termCode
        String termCode = theForm.getTermCode();
        if (StringUtils.isNotBlank(termCode)) {
            String termName = getAcademicCalendarService().getTerm(termCode, ContextUtils.createDefaultContextInfo()).getName();
            theForm.setTermName(termName);
        } else {
            LOG.error("Error: term can't be empty");
            GlobalVariables.getMessageMap().putError("termCode", ScheduleOfClassesConstants.SOC_MSG_ERROR_TERM_IS_EMPTY);
            return getUIFModelAndView(theForm);
        }

        // Second, handle searchType
        if (theForm.getSearchType().equals( SEARCH_TYPE_COURSE )) {
            String course = theForm.getCourse();
            if (StringUtils.isNotBlank(course)) {
                getViewHelperService(theForm).loadCourseOfferingsByTermAndCourseCode(termCode, course, theForm);
                theForm.setSearchParameter("Course: " + course);
            } else {
                LOG.error( ERROR_SEARCH_FIELD_CANNOT_BE_EMPTY );
                GlobalVariables.getMessageMap().putError( SEARCH_FIELD, ScheduleOfClassesConstants.SOC_MSG_ERROR_COURSE_IS_EMPTY);
                return getUIFModelAndView(theForm);
            }
        } else if (theForm.getSearchType().equals("instructor")) {
            String instructorId = theForm.getInstructor();
            String instructorName = theForm.getInstructorName();
            if ((StringUtils.isNotBlank(instructorId)) || (StringUtils.isNotBlank(instructorName))) {
                getViewHelperService(theForm).loadCourseOfferingsByTermAndInstructor(termCode, instructorId, instructorName, theForm);
                theForm.setSearchParameter("Instructor: " + instructorName);
            } else {
                LOG.error( ERROR_SEARCH_FIELD_CANNOT_BE_EMPTY );
                GlobalVariables.getMessageMap().putError( SEARCH_FIELD, ScheduleOfClassesConstants.SOC_MSG_ERROR_COURSE_IS_EMPTY);
                return getUIFModelAndView(theForm);
            }
        } else if (theForm.getSearchType().equals("department")) {
            String departmentId = theForm.getDepartment();
            String departmentName = theForm.getDepartmentName();
            if ((StringUtils.isNotBlank(departmentId)) || (StringUtils.isNotBlank(departmentName))) {
                getViewHelperService(theForm).loadCourseOfferingsByTermAndDepartment(termCode, departmentId, departmentName, theForm);
                theForm.setSearchParameter("Department: " + departmentName);
            } else {
                LOG.error( ERROR_SEARCH_FIELD_CANNOT_BE_EMPTY );
                GlobalVariables.getMessageMap().putError( SEARCH_FIELD, ScheduleOfClassesConstants.SOC_MSG_ERROR_COURSE_IS_EMPTY);
                return getUIFModelAndView(theForm);
            }
        } else if (theForm.getSearchType().equals("titleDesc")) {
            String titleDesc = theForm.getTitleDesc();
            if (StringUtils.isNotBlank(titleDesc)) {
                getViewHelperService(theForm).loadCourseOfferingsByTitleAndDescription(termCode, titleDesc, theForm);
                theForm.setSearchParameter("Title & Description: " + titleDesc);
            } else {
                LOG.error(ERROR_SEARCH_FIELD_CANNOT_BE_EMPTY );
                GlobalVariables.getMessageMap().putError( SEARCH_FIELD, ScheduleOfClassesConstants.SOC_MSG_ERROR_COURSE_IS_EMPTY);
                return getUIFModelAndView(theForm);
            }
        }

        return getUIFModelAndView(theForm, ScheduleOfClassesConstants.SOC_RESULT_PAGE);
    }

    /**
     *
     * Populates the AOs and clusters when the user clicks on the disclosure at the CO display.
     *
     * @param theForm
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=populateAOs")
    public ModelAndView populateAOs(@ModelAttribute( MODEL_ATTRIBUTE_FORM ) ScheduleOfClassesSearchForm theForm) throws Exception {

        CourseOfferingDisplayWrapper coDisplayWrapper = (CourseOfferingDisplayWrapper)KSControllerHelper.getSelectedCollectionItem(theForm);
        theForm.setCourseOfferingId(coDisplayWrapper.getCoDisplayInfo().getId());

        SearchRequestInfo searchRequestInfo = new SearchRequestInfo(ActivityOfferingSearchServiceImpl.AOS_AND_CLUSTERS_BY_CO_ID_SEARCH_KEY);

        List<String> aoStates = getViewHelperService(theForm).getAOStateFilter();
        searchRequestInfo.addParam(ActivityOfferingSearchServiceImpl.SearchParameters.AO_STATES, aoStates);

        getViewHelperService(theForm).build_AOs_RGs_AOCs_Lists_For_TheCourseOffering(theForm, searchRequestInfo);

        coDisplayWrapper.getClusterResultList().clear();
        coDisplayWrapper.getClusterResultList().addAll(theForm.getClusterResultList());
        coDisplayWrapper.getActivityWrapperList().clear();
        coDisplayWrapper.getActivityWrapperList().addAll(theForm.getActivityWrapperList());

        String requisites =  getViewHelperService(theForm).getRequisitiesForCourseOffering(coDisplayWrapper.getCoDisplayInfo().getId());
        coDisplayWrapper.setRequisites(requisites);

        getViewHelperService(theForm).sortActivityOfferings(theForm,coDisplayWrapper);

        Map<String, String> subTermInfoMap = new HashMap<String, String>();

        ContextInfo contextInfo = ContextUtils.createDefaultContextInfo();

        for (ActivityOfferingWrapper aoWrapper : coDisplayWrapper.getActivityWrapperList()){
            // Adding Information (icons)
            StringBuilder information = new StringBuilder();
            if (aoWrapper.getAoInfo().getIsHonorsOffering() != null && aoWrapper.getAoInfo().getIsHonorsOffering()) {
                information.append("<img src=").append(ScheduleOfClassesConstants.SOC_RESULT_PAGE_HONORS_COURSE_IMG).append(" title=\"").append(ScheduleOfClassesConstants.SOC_RESULT_PAGE_HELP_HONORS_ACTIVITY).append("\"> ");
            }

            // Adding subterm
            String termId = aoWrapper.getAoInfo().getTermId();
            String subTermDisplay = "";
            if (!theForm.getTermCode().equals(termId)) {
                if (!subTermInfoMap.containsKey(termId)) {
                    TermInfo subTerm = getAcademicCalendarService().getTerm(termId, contextInfo);
                    // check if term or subterm
                    List<TypeTypeRelationInfo> terms = getTypeService().getTypeTypeRelationsByRelatedTypeAndType(subTerm.getTypeKey(), TypeServiceConstants.TYPE_TYPE_RELATION_CONTAINS_TYPE_KEY, contextInfo);
                    // if subterm
                    if (!terms.isEmpty()) {
                        TypeInfo subTermType = getTypeService().getType(subTerm.getTypeKey(), contextInfo);
                        subTermDisplay = "This activity is in " + subTermType.getName() + " - " + getViewHelperService(theForm).getTermStartEndDate(subTerm);
                        subTermInfoMap.put(termId, subTermDisplay);
                        // displaying information
                        information.append("<img src=").append(ScheduleOfClassesConstants.SOC_RESULT_PAGE_SUBTERM_IMG).append(" title=\"").append(subTermDisplay).append("\"> ");
                    }
                } else {
                    subTermDisplay = subTermInfoMap.get(termId);
                    information.append("<img src=").append(ScheduleOfClassesConstants.SOC_RESULT_PAGE_SUBTERM_IMG).append(" title=\"").append(subTermDisplay).append("\"> ");
                }
            }

            aoWrapper.setSchOfClassesRenderHelper(aoWrapper.new SchOfClassesRenderHelper());
            aoWrapper.getSchOfClassesRenderHelper().setInformation(information.toString());
        }


        return getUIFModelAndView(theForm, ScheduleOfClassesConstants.SOC_RESULT_PAGE);
    }

    /**
     * Populates the reg group when the user clicks on the disclosure at the CO display.
     *
     * @param theForm
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=populateRegGroups")
    public ModelAndView populateRegGroups(@ModelAttribute( MODEL_ATTRIBUTE_FORM ) ScheduleOfClassesSearchForm theForm) throws Exception {

        SearchRequestInfo searchRequestInfo = new SearchRequestInfo(ActivityOfferingSearchServiceImpl.AOS_AND_CLUSTERS_BY_CO_ID_SEARCH_KEY);

        CourseOfferingDisplayWrapper coDisplayWrapper = (CourseOfferingDisplayWrapper)KSControllerHelper.getSelectedCollectionItem(theForm);
        theForm.setCourseOfferingId(coDisplayWrapper.getCoDisplayInfo().getId());

        List<String> regGroupStates = getViewHelperService(theForm).getRegGroupStateFilter();
        searchRequestInfo.addParam(ActivityOfferingSearchServiceImpl.SearchParameters.REGGROUP_STATES, regGroupStates);

        getViewHelperService(theForm).build_AOs_RGs_AOCs_Lists_For_TheCourseOffering(theForm,searchRequestInfo);

        coDisplayWrapper.getClusterResultList().clear();
        coDisplayWrapper.getClusterResultList().addAll(theForm.getClusterResultList());

        /**
         * Sort the RegGroups first by the ID and then by institutionally configured list of comparators
         */
        for (ActivityOfferingClusterWrapper clusterWrapper : coDisplayWrapper.getClusterResultList()){
            if(clusterWrapper.getRgWrapperList().size() >1){
                //Sort Reg Groups by Reg Group name (which is not institutionally configurable)
               // Collections.sort(clusterWrapper.getRgWrapperList(),new RegGroupNameComparator());
                //Sort by whatever configured at the xml (which are institutionally configurable)
                getViewHelperService(theForm).sortRegGroups(clusterWrapper.getRgWrapperList());
            }
        }

        return getUIFModelAndView(theForm, ScheduleOfClassesConstants.SOC_RESULT_PAGE);
    }

    public ScheduleOfClassesViewHelperService getViewHelperService(ScheduleOfClassesSearchForm theForm) {
        if (viewHelperService == null) {
            if (theForm.getView().getViewHelperService() != null) {
                viewHelperService = (ScheduleOfClassesViewHelperService) theForm.getView().getViewHelperService();
            } else {
                viewHelperService = (ScheduleOfClassesViewHelperService) theForm.getPostedView().getViewHelperService();
            }
        }
        return viewHelperService;
    }

    protected AcademicCalendarService getAcademicCalendarService() {
        if (acalService == null) {
            acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE, AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.acalService;
    }

    protected CourseOfferingSetService getCourseOfferingSetService() {
        if (courseOfferingSetService == null) {

            courseOfferingSetService = (CourseOfferingSetService) GlobalResourceLoader.getService(new QName(CourseOfferingSetServiceConstants.NAMESPACE, CourseOfferingSetServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.courseOfferingSetService;
    }

    protected AtpService getAtpService() {
        if (atpService == null) {
            atpService = (AtpService) GlobalResourceLoader.getService(new QName(AtpServiceConstants.NAMESPACE, AtpServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.atpService;
    }

    public TypeService getTypeService() {
        if (typeService == null) {
            typeService = CourseOfferingResourceLoader.loadTypeService();
        }
        return this.typeService;
    }
}
