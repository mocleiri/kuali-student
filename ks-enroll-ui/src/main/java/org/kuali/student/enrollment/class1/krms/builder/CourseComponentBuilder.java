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
package org.kuali.student.enrollment.class1.krms.builder;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.builder.ComponentBuilder;
import org.kuali.rice.krms.util.PropositionTreeUtil;
import org.kuali.student.enrollment.class1.krms.dto.EnrolPropositionEditor;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingConstants;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.r2.common.constants.CommonServiceConstants;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.KSKRMSServiceConstants;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.class1.search.CourseOfferingManagementSearchImpl;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.kuali.student.r2.core.search.service.SearchService;
import org.kuali.student.r2.core.versionmanagement.dto.VersionDisplayInfo;
import org.kuali.student.r2.lum.clu.service.CluService;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.service.CourseService;
import org.kuali.student.r2.lum.util.constants.CluServiceConstants;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Student Team
 */
public class CourseComponentBuilder implements ComponentBuilder<EnrolPropositionEditor> {

    private final static Logger LOG = Logger.getLogger(CourseComponentBuilder.class);

    private CourseService courseService;
    private CluService cluService;
    private AcademicCalendarService acalService = null;
    private SearchService searchService = null;

    @Override
    public List<String> getComponentIds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resolveTermParameters(EnrolPropositionEditor propositionEditor, Map<String, String> termParameters) {
        String courseId = termParameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY);
        String termCode1 = termParameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY);
        String termCode2 = termParameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE2_KEY);

        if (courseId != null) {
            try {
                VersionDisplayInfo versionInfo = this.getCluService().getCurrentVersion(CluServiceConstants.CLU_NAMESPACE_URI, courseId, null);
                CourseInfo courseInfo = this.getCourseService().getCourse(versionInfo.getId(), ContextUtils.getContextInfo());
                propositionEditor.setCourseInfo(courseInfo);
            } catch (DoesNotExistException e) {
                throw new RuntimeException("Clu does not exist");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        if (termCode1 != null) {
            propositionEditor.setTermCode(termCode1);
            propositionEditor.setTermInfo(this.getTermForTermCode(propositionEditor.getTermCode(), null));
        }
        if (termCode2 != null) {
            propositionEditor.setTermCode2(termCode2);
            propositionEditor.setTermInfo2(this.getTermForTermCode(propositionEditor.getTermCode2(), null));
        }
    }

    @Override
    public Map<String, String> buildTermParameters(EnrolPropositionEditor propositionEditor) {
        Map<String, String> termParameters = new HashMap<String, String>();
        if (propositionEditor.getCourseInfo() != null) {
            termParameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_CLU_KEY, propositionEditor.getCourseInfo().getVersion().getVersionIndId());
        }

        if (propositionEditor.getTermInfo() != null) {
            termParameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERM_KEY, propositionEditor.getTermInfo().getId());
            termParameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE_KEY, propositionEditor.getTermCode());
            String propName = PropositionTreeUtil.getBindingPath(propositionEditor, "termCode");
            loadCourseOfferingsByTermAndCourseCode(propositionEditor.getTermInfo().getId(), propositionEditor.getCourseInfo().getCode(), propName);
        }

        if (propositionEditor.getTermInfo2() != null) {
            termParameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERM2_KEY, propositionEditor.getTermInfo2().getId());
            termParameters.put(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_TERMCODE2_KEY, propositionEditor.getTermCode2());
            String propName = PropositionTreeUtil.getBindingPath(propositionEditor, "termCode2");
            loadCourseOfferingsByTermAndCourseCode(propositionEditor.getTermInfo2().getId(), propositionEditor.getCourseInfo().getCode(),propName );
        }

        return termParameters;
    }

    @Override
    public void onSubmit(EnrolPropositionEditor propositionEditor) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validate(EnrolPropositionEditor propositionEditor) {

        if (propositionEditor.getTermCode() != null) {
            String propName = PropositionTreeUtil.getBindingPath(propositionEditor, "termCode");
            propositionEditor.setTermInfo(this.getTermForTermCode(propositionEditor.getTermCode(), propName));
        }

        if (propositionEditor.getTermCode2() != null) {
            String propName = PropositionTreeUtil.getBindingPath(propositionEditor, "termCode2");
            propositionEditor.setTermInfo2(this.getTermForTermCode(propositionEditor.getTermCode2(), propName));
        }
    }

    /**
     * Method used to
     * 1) search to get TermInfo based on termCode. Only accept one valid TermInfo. If find more than one TermInfo or
     * don't find any termInfo, log and report an error message.
     *
     * @param termCode
     * @return termInfo
     */
    public TermInfo getTermForTermCode(String termCode, String propName) {

        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(PredicateFactory.equal("atpCode", termCode));

        List<TermInfo> terms = null;
        try {
            terms = getAcalService().searchForTerms(qbcBuilder.build(), ContextUtils.getContextInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (terms.isEmpty()) {
            GlobalVariables.getMessageMap().putError(propName, CourseOfferingConstants.COURSEOFFERING_MSG_ERROR_NO_TERM_IS_FOUND, termCode);
        } else if (terms.size() > 1) {
            GlobalVariables.getMessageMap().putError(propName, CourseOfferingConstants.COURSEOFFERING_MSG_ERROR_FOUND_MORE_THAN_ONE_TERM, termCode);
        } else {
            return terms.get(0);
        }

        return null;
    }

    /**
     * Method used to
     * find THE course based on termId and courseCode. If find more than one CO or don't find
     * any CO, log and report an error message.
     *
     * @param termId
     * @param courseCode
     */
    public void loadCourseOfferingsByTermAndCourseCode(String termId, String courseCode, String propName ) {

        SearchRequestInfo searchRequest = new SearchRequestInfo(CourseOfferingManagementSearchImpl.CO_MANAGEMENT_SEARCH.getKey());
        searchRequest.addParam(CourseOfferingManagementSearchImpl.SearchParameters.COURSE_CODE, courseCode);
        searchRequest.addParam(CourseOfferingManagementSearchImpl.SearchParameters.ATP_ID, termId);
        searchRequest.addParam(CourseOfferingManagementSearchImpl.SearchParameters.CROSS_LIST_SEARCH_ENABLED, BooleanUtils.toStringTrueFalse(true));

        SearchResultInfo searchResult = null;
        try {
            searchResult = getSearchService().search(searchRequest, ContextUtils.getContextInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (searchResult.getRows().isEmpty()) {
            LOG.error("Error: Can't find any Course Offering for a Course Code: " + courseCode + " in term: " + termId);
            GlobalVariables.getMessageMap().putError(propName, CourseOfferingConstants.COURSEOFFERING_MSG_ERROR_NO_COURSE_OFFERING_IS_FOUND,  "Course Code",courseCode, termId);
        }
    }

    protected CourseService getCourseService() {
        if (courseService == null) {
            courseService = CourseOfferingResourceLoader.loadCourseService();
        }
        return courseService;
    }

    protected CluService getCluService() {
        if (cluService == null) {
            cluService = CourseOfferingResourceLoader.loadCluService();
        }
        return cluService;
    }

    private AcademicCalendarService getAcalService() {
        if (acalService == null) {
            acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE,
                    AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return acalService;
    }

    public SearchService getSearchService() {
        if (searchService == null) {
            searchService = (SearchService) GlobalResourceLoader.getService(new QName(CommonServiceConstants.REF_OBJECT_URI_GLOBAL_PREFIX + "search", SearchService.class.getSimpleName()));
        }
        return searchService;
    }

}
