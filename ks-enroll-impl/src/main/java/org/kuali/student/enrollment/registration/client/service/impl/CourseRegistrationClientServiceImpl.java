package org.kuali.student.enrollment.registration.client.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.RegistrationGroupInfo;
import org.kuali.student.enrollment.courseregistration.dto.RegistrationRequestInfo;
import org.kuali.student.enrollment.courseregistration.dto.RegistrationRequestItemInfo;
import org.kuali.student.enrollment.courseregistration.dto.RegistrationResponseInfo;
import org.kuali.student.enrollment.courseregistration.service.CourseRegistrationService;
import org.kuali.student.enrollment.lpr.dto.LprInfo;
import org.kuali.student.enrollment.registration.client.service.CourseRegistrationClientService;
import org.kuali.student.enrollment.registration.client.service.ScheduleOfClassesService;
import org.kuali.student.enrollment.registration.client.service.ScheduleOfClassesServiceConstants;
import org.kuali.student.enrollment.registration.client.service.dto.ActivityOfferingScheduleComponentResult;
import org.kuali.student.enrollment.registration.client.service.dto.InstructorSearchResult;
import org.kuali.student.enrollment.registration.client.service.dto.RegGroupSearchResult;
import org.kuali.student.enrollment.registration.client.service.dto.ScheduleCalendarEventResult;
import org.kuali.student.enrollment.registration.client.service.dto.StudentScheduleActivityOfferingResult;
import org.kuali.student.enrollment.registration.client.service.dto.StudentScheduleCourseResult;
import org.kuali.student.enrollment.registration.client.service.dto.StudentScheduleTermResult;
import org.kuali.student.enrollment.registration.client.service.dto.TermSearchResult;
import org.kuali.student.enrollment.registration.client.service.impl.util.CourseRegistrationAndScheduleOfClassesUtil;
import org.kuali.student.enrollment.registration.client.service.impl.util.statistics.RegEngineMqStatisticsGenerator;
import org.kuali.student.enrollment.registration.engine.util.MQPerformanceCounter;
import org.kuali.student.enrollment.registration.search.service.impl.CourseRegistrationSearchServiceImpl;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.CourseRegistrationServiceConstants;
import org.kuali.student.r2.common.util.constants.LprServiceConstants;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultCellInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValueInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValuesGroupInfo;
import org.kuali.student.r2.lum.util.constants.LrcServiceConstants;

import javax.jms.JMSException;
import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CourseRegistrationClientServiceImpl implements CourseRegistrationClientService {

    public static final Logger LOGGER = Logger.getLogger(CourseRegistrationClientServiceImpl.class);

    @Override
    public Response registerForRegistrationGroup(String userId, String termCode, String courseCode, String regGroupName, String regGroupId, String credits, String gradingOptionId) {
        Response.ResponseBuilder response;

        try {
            response = Response.ok(registerForRegistrationGroupLocal(userId,termCode,courseCode,regGroupName,regGroupId,credits,gradingOptionId));
        } catch (Throwable t) {
            LOGGER.warn(t);
            response = Response.serverError().entity(t.getMessage());
        }

        return response.build();
    }

    public RegistrationResponseInfo registerForRegistrationGroupLocal(String userId, String termCode, String courseCode, String regGroupName, String regGroupId, String credits, String gradingOptionId) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DataValidationErrorException, DoesNotExistException, ReadOnlyException, AlreadyExistsException, LoginException {
        LOGGER.debug(String.format("REGISTRATION: user[%s] termCode[%s] courseCode[%s] regGroup[%s]", userId, termCode, courseCode, regGroupName));
        ContextInfo contextInfo = ContextUtils.createDefaultContextInfo();

        if (!StringUtils.isEmpty(userId)) {
            contextInfo.setPrincipalId(userId);
        }  else if (StringUtils.isEmpty(contextInfo.getPrincipalId())) {
            throw new LoginException("[CourseRegistrationClientServiceImpl::registerForRegistrationGroupLocal]User must be logged in to access this service");
        }

        // get the regGroup
        RegGroupSearchResult rg = CourseRegistrationAndScheduleOfClassesUtil.getRegGroup(termCode, courseCode, regGroupName, regGroupId, contextInfo);

        // get the registration group, returns default (from Course Offering) credits (as creditId) and grading options (as a string of options)
        CourseOfferingInfo courseOfferingInfo = CourseRegistrationAndScheduleOfClassesUtil.getCourseOfferingIdCreditGrading(rg.getCourseOfferingId(), courseCode, rg.getTermId(), termCode);

        // verify passed credits (must be non-empty unless fixed) and grading option (can be null)
        credits = verifyRegistrationRequestCreditsGradingOption(courseOfferingInfo, credits, gradingOptionId, contextInfo);

        //Create the request object
        RegistrationRequestInfo regReqInfo = createAddRegistrationRequest(contextInfo.getPrincipalId(), rg.getTermId(), rg.getRegGroupId(), credits, gradingOptionId);

        // persist the request object in the service
        RegistrationRequestInfo newRegReq = CourseRegistrationAndScheduleOfClassesUtil.getCourseRegistrationService().createRegistrationRequest(LprServiceConstants.LPRTRANS_REGISTER_TYPE_KEY, regReqInfo, contextInfo);

        // submit the request to the registration engine.
        RegistrationResponseInfo registrationResponseInfo = CourseRegistrationAndScheduleOfClassesUtil.getCourseRegistrationService().submitRegistrationRequest(newRegReq.getId(), contextInfo);

        return registrationResponseInfo;
      
    }

    @Override
    public Response getRegEngineStats() {
        Response.ResponseBuilder response;

        try {
            Map<String, List> stats = getStatsFromRegEngine();
            response = Response.ok(stats);
        } catch (Throwable t) {
            LOGGER.warn(t);
            response = Response.serverError().entity(t.getMessage());
        }

        return response.build();
    }

    @Override
    public Response clearRegEngineStats() {

        Response.ResponseBuilder response;

        try {
            // This might not be the best way to do this...
            // I would rather have one point of entry into a singleton but
            // this is incredibly easy.
            MQPerformanceCounter.INSTANCE.clearPerformanceStats();

            response = Response.fromResponse(getRegEngineStats());
        } catch (Throwable t) {
            LOGGER.warn(t);
            response = Response.serverError().entity(t.getMessage());
        }

        return response.build();
    }

    private Map<String, List> getStatsFromRegEngine() throws JMSException {

        // define types of stats to collect
        List<RegEngineMqStatisticsGenerator.RegistrationEngineStatsType> statTypesToRequest = new LinkedList<RegEngineMqStatisticsGenerator.RegistrationEngineStatsType>();
        statTypesToRequest.add(RegEngineMqStatisticsGenerator.RegistrationEngineStatsType.BROKER);
        statTypesToRequest.add(RegEngineMqStatisticsGenerator.RegistrationEngineStatsType.INITIALIZATION_QUEUE);
        statTypesToRequest.add(RegEngineMqStatisticsGenerator.RegistrationEngineStatsType.VERIFICATION_QUEUE);
        statTypesToRequest.add(RegEngineMqStatisticsGenerator.RegistrationEngineStatsType.SEAT_CHECK_QUEUE);
        statTypesToRequest.add(RegEngineMqStatisticsGenerator.RegistrationEngineStatsType.REGISTRATION_ENGINE_STATS);

        // collect the stats
        RegEngineMqStatisticsGenerator generator = new RegEngineMqStatisticsGenerator();
        generator.initiateRequestForStats(statTypesToRequest);

        return generator.getStats();
    }

    /**
     * SEARCH for STUDENT REGISTRATION INFO based on person and termCode *
     */
    @Override
    public List<StudentScheduleTermResult> searchForScheduleByPersonAndTerm(String userId, String termId, String termCode) throws LoginException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DoesNotExistException {

        ContextInfo contextInfo = ContextUtils.createDefaultContextInfo();

        if (StringUtils.isEmpty(userId)) {
            userId = contextInfo.getPrincipalId();
        }

        if(StringUtils.isEmpty(userId)){
            throw new LoginException("[CourseRegistrationClientServiceImpl::searchForScheduleByPersonAndTerm] User must be logged in to access this service");
        }

        if (StringUtils.isEmpty(termId) && StringUtils.isEmpty(termCode)) {
            termId = "";
        } else {
            termId = CourseRegistrationAndScheduleOfClassesUtil.getTermId(termId, termCode);
        }
        return getRegistrationScheduleByPersonAndTerm(userId, termId, contextInfo);
    }

    @Override
    public List<List<ScheduleCalendarEventResult>> searchForScheduleCalendarByPersonAndTerm(String userId, String termId, String termCode) throws LoginException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DoesNotExistException {
        //Colours this is just a poc!
        String[] colours = new String[]{"#BDF", "#DFB", "#FBD", "#FDB", "#DBF", "#BFD",
                "#DDF", "#DFD", "#FDD",
                "#DDB", "#DBD", "#BDD",
                "#FBB", "#BBF", "#BFB",
                "#DBB", "#BBD", "#BDB",
                "#FFB", "#FBF", "#BFF",
                "#FFD", "#FDF", "#DFF"};
        int colourIndex = 0;

        //Use existing services to get the schedule
        List<StudentScheduleTermResult> schedule = searchForScheduleByPersonAndTerm(userId, termId, termCode);

        //Initialize a map with lists for each day of the week
        Map<String, List<ScheduleCalendarEventResult>> dayToEventListMap = new HashMap<String, List<ScheduleCalendarEventResult>>();
        dayToEventListMap.put("M", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("T", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("W", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("H", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("F", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("S", new ArrayList<ScheduleCalendarEventResult>());
        dayToEventListMap.put("U", new ArrayList<ScheduleCalendarEventResult>());

        //Create events per day from the schedule
        for(StudentScheduleTermResult scheduleItem : schedule){
            for(StudentScheduleCourseResult course:scheduleItem.getCourseOfferings()){
                String colour = colours[colourIndex++];
                for(StudentScheduleActivityOfferingResult ao : course.getActivityOfferings()){
                    for(ActivityOfferingScheduleComponentResult component : ao.getScheduleComponents()){
                        if(component.getStartTime() != null){
                            //Calculate the text and start time/end time in minutes
                            String text = course.getCourseCode() + " " + (ao.getActivityOfferingTypeShortName()!=null&&ao.getActivityOfferingTypeShortName().length()>=3?ao.getActivityOfferingTypeShortName().substring(0,3).toUpperCase():"");
                            int startTimeMin = toMins(component.getStartTime());
                            int duration = toMins(component.getEndTime()) - startTimeMin ;
                            if(component.isMon()){dayToEventListMap.get("M").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isTue()){dayToEventListMap.get("T").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isWed()){dayToEventListMap.get("W").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isThu()){dayToEventListMap.get("H").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isFri()){dayToEventListMap.get("F").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isSat()){dayToEventListMap.get("S").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                            if(component.isSun()){dayToEventListMap.get("U").add(new ScheduleCalendarEventResult(duration, text, startTimeMin, colour));}
                        }
                    }
                }
            }
        }

        //Sort each day's list by start time
        Comparator<ScheduleCalendarEventResult> comparator = new Comparator<ScheduleCalendarEventResult>(){
            @Override
            public int compare(ScheduleCalendarEventResult event1, ScheduleCalendarEventResult event2) {
                return  event1.getStartTimeMin() - event2.getStartTimeMin();
            }
        };
        Collections.sort(dayToEventListMap.get("M"), comparator);
        Collections.sort(dayToEventListMap.get("T"), comparator);
        Collections.sort(dayToEventListMap.get("W"), comparator);
        Collections.sort(dayToEventListMap.get("H"), comparator);
        Collections.sort(dayToEventListMap.get("F"), comparator);
        Collections.sort(dayToEventListMap.get("S"), comparator);
        Collections.sort(dayToEventListMap.get("U"), comparator);

        //Sum up the offset (setSumPrecedingDurations) (and calculate time conflicts in the future)
        for(List<ScheduleCalendarEventResult> events : dayToEventListMap.values()){
            int offset = 0;
            for(ScheduleCalendarEventResult event : events){
                event.setSumPrecedingDurations(offset);
                offset += event.getDurationMin();
            }
        }

        //Convert Map to list of lists
        List<List<ScheduleCalendarEventResult>> results = new ArrayList<List<ScheduleCalendarEventResult>>();
        results.add(dayToEventListMap.get("M"));
        results.add(dayToEventListMap.get("T"));
        results.add(dayToEventListMap.get("W"));
        results.add(dayToEventListMap.get("H"));
        results.add(dayToEventListMap.get("F"));
        results.add(dayToEventListMap.get("S"));
        results.add(dayToEventListMap.get("U"));

        return results;
    }

    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        if(hourMin.length == 2){
            int hour = Integer.parseInt(hourMin[0]);
            int mins = Integer.parseInt(hourMin[1]);
            int hoursInMins = hour * 60;
            return hoursInMins + mins;
        }
        return -1;
    }

    /**
     * This method call search service to retrieve registration schedule data for the person
     * @param userId
     * @param termId
     * @return StudentScheduleCourseResults
     * @throws OperationFailedException
     * @throws InvalidParameterException
     **/
    private List<StudentScheduleTermResult> getRegistrationScheduleByPersonAndTerm(String userId, String termId, ContextInfo contextInfo) throws LoginException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DoesNotExistException {
        List<StudentScheduleTermResult> studentScheduleTermResults = new ArrayList<StudentScheduleTermResult>();
        List<String> activityOfferingList = new ArrayList<String>();
        HashMap<String, StudentScheduleCourseResult> hmCourseOffering = new HashMap<String, StudentScheduleCourseResult>();
        HashMap<String, TermSearchResult> hmTermInfo = new HashMap<String, TermSearchResult>();
        HashMap<String, List<String>> hmTerm = new HashMap<String, List<String>>();

        SearchRequestInfo searchRequest = new SearchRequestInfo(CourseRegistrationSearchServiceImpl.REG_INFO_BY_PERSON_TERM_SEARCH_TYPE.getKey());
        searchRequest.addParam(CourseRegistrationSearchServiceImpl.SearchParameters.PERSON_ID, userId);
        searchRequest.addParam(CourseRegistrationSearchServiceImpl.SearchParameters.ATP_ID, termId);

        SearchResultInfo searchResult;
        try {
            searchResult = CourseRegistrationAndScheduleOfClassesUtil.getSearchService().search(searchRequest, contextInfo);
        } catch (Exception e) {
            throw new OperationFailedException("Search of registration schedule for person " + userId + " and term " + termId + " failed: ", e);
        }

        for (SearchResultRowInfo row : searchResult.getRows()) {
            String atpId = "", atpCode = "", atpName = "",
                   luiId = "", masterLuiId = "", personLuiType = "", credits = "",
                   luiCode = "", luiName = "", luiDesc = "", luiType = "", luiLongName = "",
                   roomCode = "", buildingCode = "", weekdays = "", startTimeMs = "", endTimeMs = "";
            for (SearchResultCellInfo cellInfo : row.getCells()) {
                if (CourseRegistrationSearchServiceImpl.SearchResultColumns.ATP_ID.equals(cellInfo.getKey())) {
                    atpId = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.ATP_CD.equals(cellInfo.getKey())) {
                    atpCode = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.ATP_NAME.equals(cellInfo.getKey())) {
                    atpName = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_ID.equals(cellInfo.getKey())) {
                    luiId = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.MASTER_LUI_ID.equals(cellInfo.getKey())) {
                    masterLuiId = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.PERSON_LUI_TYPE.equals(cellInfo.getKey())) {
                    personLuiType = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.CREDITS.equals(cellInfo.getKey())) {
                    credits = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_CODE.equals(cellInfo.getKey())) {
                    luiCode = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_NAME.equals(cellInfo.getKey())) {
                    luiName = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_DESC.equals(cellInfo.getKey())) {
                    luiDesc = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_TYPE.equals(cellInfo.getKey())) {
                    luiType = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.LUI_LONG_NAME.equals(cellInfo.getKey())) {
                    luiLongName = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.ROOM_CODE.equals(cellInfo.getKey())) {
                    roomCode = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.BUILDING_CODE.equals(cellInfo.getKey())) {
                    buildingCode = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.WEEKDAYS.equals(cellInfo.getKey())) {
                    weekdays = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.START_TIME_MS.equals(cellInfo.getKey())) {
                    startTimeMs = cellInfo.getValue();
                } else if (CourseRegistrationSearchServiceImpl.SearchResultColumns.END_TIME_MS.equals(cellInfo.getKey())) {
                    endTimeMs = cellInfo.getValue();
                }
            }

            // running over the list of results returned. One CO can have multiple AOs
            if (hmCourseOffering.containsKey(masterLuiId)) {
                StudentScheduleCourseResult studentScheduleCourseResult = hmCourseOffering.get(masterLuiId);
                if (StringUtils.equals(personLuiType, LprServiceConstants.REGISTRANT_CO_TYPE_KEY)) {
                    studentScheduleCourseResult.setCourseCode(luiCode);
                    studentScheduleCourseResult.setDescription(luiDesc);
                    studentScheduleCourseResult.setCredits(credits);
                    studentScheduleCourseResult.setLongName(luiLongName);
                    hmCourseOffering.put(masterLuiId, studentScheduleCourseResult);
                } else if (StringUtils.equals(personLuiType, LprServiceConstants.REGISTRANT_AO_TYPE_KEY)) {
                    // Scheduling info
                    ActivityOfferingScheduleComponentResult scheduleComponent = CourseRegistrationAndScheduleOfClassesUtil.getActivityOfferingScheduleComponent(roomCode, buildingCode,
                            weekdays, startTimeMs, endTimeMs);

                    // have to check if we already have the AO in our list, because we can have multiple schedules for the same AO
                    HashMap<String, StudentScheduleActivityOfferingResult> aoHm = new HashMap<String, StudentScheduleActivityOfferingResult>();
                    for (StudentScheduleActivityOfferingResult activityOfferingResult : studentScheduleCourseResult.getActivityOfferings()) {
                        aoHm.put(activityOfferingResult.getActivityOfferingId(), activityOfferingResult);
                    }
                    if (!aoHm.containsKey(luiId)) {
                        StudentScheduleActivityOfferingResult activityOffering = new StudentScheduleActivityOfferingResult();

                        // AO basic info
                        activityOffering.setActivityOfferingId(luiId);
                        activityOffering.setActivityOfferingTypeShortName(luiName);
                        activityOffering.setActivityOfferingType(luiType);  // to sort over priorities

                        // adding schedule to AO
                        List<ActivityOfferingScheduleComponentResult> scheduleComponents = new ArrayList<ActivityOfferingScheduleComponentResult>();
                        scheduleComponents.add(scheduleComponent);
                        activityOffering.setScheduleComponents(scheduleComponents);

                        // adding AO to result
                        if (studentScheduleCourseResult.getActivityOfferings().isEmpty()) {
                            List<StudentScheduleActivityOfferingResult> activityOfferings = new ArrayList<StudentScheduleActivityOfferingResult>();
                            activityOfferings.add(activityOffering);
                            studentScheduleCourseResult.setActivityOfferings(activityOfferings);
                        } else {
                            studentScheduleCourseResult.getActivityOfferings().add(activityOffering);
                        }
                    } else {
                        StudentScheduleActivityOfferingResult activityOffering = aoHm.get(luiId);
                        studentScheduleCourseResult.getActivityOfferings().remove(activityOffering);
                        activityOffering.getScheduleComponents().add(scheduleComponent);
                        studentScheduleCourseResult.getActivityOfferings().add(activityOffering);
                    }

                    hmCourseOffering.put(masterLuiId, studentScheduleCourseResult);

                    // adding AOID to the list to search for instructors
                    if (!activityOfferingList.contains(luiId)) {
                        activityOfferingList.add(luiId);
                    }
                }
            } else {
                StudentScheduleCourseResult studentScheduleCourseResult = new StudentScheduleCourseResult();
                if (StringUtils.equals(personLuiType, LprServiceConstants.REGISTRANT_CO_TYPE_KEY)) {
                    studentScheduleCourseResult.setCourseCode(luiCode);
                    studentScheduleCourseResult.setDescription(luiDesc);
                    studentScheduleCourseResult.setCredits(credits);
                    studentScheduleCourseResult.setLongName(luiLongName);
                    hmCourseOffering.put(masterLuiId, studentScheduleCourseResult);
                } else if (StringUtils.equals(personLuiType, LprServiceConstants.REGISTRANT_AO_TYPE_KEY)) {
                    List<StudentScheduleActivityOfferingResult> activityOfferings = new ArrayList<StudentScheduleActivityOfferingResult>();
                    StudentScheduleActivityOfferingResult activityOffering = new StudentScheduleActivityOfferingResult();
                    // AO basic info
                    activityOffering.setActivityOfferingId(luiId);
                    activityOffering.setActivityOfferingTypeShortName(luiName);
                    activityOffering.setActivityOfferingType(luiType);  // to sort over priorities

                    // Scheduling info
                    List<ActivityOfferingScheduleComponentResult> scheduleComponents = new ArrayList<ActivityOfferingScheduleComponentResult>();
                    ActivityOfferingScheduleComponentResult scheduleComponent = CourseRegistrationAndScheduleOfClassesUtil.getActivityOfferingScheduleComponent(roomCode, buildingCode,
                            weekdays, startTimeMs, endTimeMs);
                    scheduleComponents.add(scheduleComponent);
                    activityOffering.setScheduleComponents(scheduleComponents);
                    // End scheduling info

                    activityOfferings.add(activityOffering);
                    studentScheduleCourseResult.setActivityOfferings(activityOfferings);
                    hmCourseOffering.put(masterLuiId, studentScheduleCourseResult);

                    // adding AOID to the list to search for instructors
                    if (!activityOfferingList.contains(luiId)) {
                        activityOfferingList.add(luiId);
                    }
                }
            }

            // Adding all course offerings for the particular term
            if (!hmTerm.containsKey(termId)) {
                List<String> courseOfferingIds = new ArrayList<String>();
                courseOfferingIds.add(masterLuiId);
                hmTerm.put(termId, courseOfferingIds);
                TermSearchResult termInfo = new TermSearchResult();
                termInfo.setTermId(atpId);
                termInfo.setTermCode(atpCode);
                termInfo.setTermName(atpName);
                hmTermInfo.put(termId, termInfo);
            } else {
                if (!hmTerm.get(termId).contains(masterLuiId)) {
                    hmTerm.get(termId).add(masterLuiId);
                }
            }
        }

        // getting instructor info for AOs
        Map<String, List<InstructorSearchResult>> hmAOInstructors = new HashMap<String, List<InstructorSearchResult>>();
        if (!activityOfferingList.isEmpty()) {
            hmAOInstructors = CourseRegistrationAndScheduleOfClassesUtil.searchForInstructorsByAoIds(activityOfferingList, contextInfo);
        }

        for (Map.Entry<String, List<String>> pair : hmTerm.entrySet()) {
            List<StudentScheduleCourseResult> studentScheduleCourseResults = new ArrayList<StudentScheduleCourseResult>();
            TermSearchResult termInfo = hmTermInfo.get(pair.getKey());
            List<String> courseOfferingIds = pair.getValue();
            for (String courseOfferingId : courseOfferingIds) {
                StudentScheduleCourseResult studentScheduleCourseResult = hmCourseOffering.get(courseOfferingId);
                if (studentScheduleCourseResult.getActivityOfferings().size() > 1) {
                    CourseRegistrationAndScheduleOfClassesUtil.sortActivityOfferingReslutList(studentScheduleCourseResult.getActivityOfferings(), contextInfo);
                }
                for (StudentScheduleActivityOfferingResult activityOffering : studentScheduleCourseResult.getActivityOfferings()) {
                    if (hmAOInstructors.containsKey(activityOffering.getActivityOfferingId())) {
                        activityOffering.setInstructors(hmAOInstructors.get(activityOffering.getActivityOfferingId()));
                    }
                }
                studentScheduleCourseResults.add(studentScheduleCourseResult);
            }
            StudentScheduleTermResult studentScheduleTermResult = new StudentScheduleTermResult();
            studentScheduleTermResult.setTerm(termInfo);
            studentScheduleTermResult.setCourseOfferings(studentScheduleCourseResults);

            studentScheduleTermResults.add(studentScheduleTermResult);
        }

        return studentScheduleTermResults;
    }

    /**
     * Finds all LPRs for a given personId and deletes them
     * Returns an empty List of StudentScheduleCourseResult
     *
     * @param personId Principal ID
     * @return Empty Response Object or Response object with Error text
     * @throws InvalidParameterException
     * @throws MissingParameterException
     * @throws OperationFailedException
     * @throws PermissionDeniedException
     * @throws DoesNotExistException
     */
    public Response clearLPRsByPerson(String personId) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DoesNotExistException {
        Response.ResponseBuilder response;
        try {
            ContextInfo contextInfo = ContextUtils.createDefaultContextInfo();
            List<LprInfo> lprs = CourseRegistrationAndScheduleOfClassesUtil.getLprService().getLprsByPerson(personId, contextInfo);
            for (LprInfo lprInfo : lprs) {
                CourseRegistrationAndScheduleOfClassesUtil.getLprService().deleteLpr(lprInfo.getId(), contextInfo);
            }
            response = Response.noContent();
        } catch (Throwable t) {
            LOGGER.warn(t);
            response = Response.serverError().entity(t.getMessage());
        }

        return response.build();
    }


    /**
     * This method creates a registration request for the add operation of a single registration group.
     *
     * @param principalId principal id
     * @param termId      term id
     * @param regGroupid  Registration Group id
     * @return registration request
     */
    private RegistrationRequestInfo createAddRegistrationRequest(String principalId, String termId, String regGroupid, String credits, String gradingOptionId) {
        RegistrationRequestInfo regReqInfo = new RegistrationRequestInfo();
        regReqInfo.setRequestorId(principalId);
        regReqInfo.setTermId(termId); // bad bc we have it from the load call above
        regReqInfo.setStateKey(LprServiceConstants.LPRTRANS_NEW_STATE_KEY); // new reg request
        regReqInfo.setTypeKey(LprServiceConstants.LPRTRANS_REGISTER_TYPE_KEY);

        RegistrationRequestItemInfo registrationRequestItem = new RegistrationRequestItemInfo();
        registrationRequestItem.setTypeKey(LprServiceConstants.REQ_ITEM_ADD_TYPE_KEY);
        registrationRequestItem.setStateKey(LprServiceConstants.LPRTRANS_ITEM_NEW_STATE_KEY);
        registrationRequestItem.setRegistrationGroupId(regGroupid);
        registrationRequestItem.setPersonId(principalId);
        registrationRequestItem.setCredits(new KualiDecimal(credits));
        registrationRequestItem.setGradingOptionId(gradingOptionId);

        regReqInfo.getRegistrationRequestItems().add(registrationRequestItem);

        return regReqInfo;
    }

    private String verifyRegistrationRequestCreditsGradingOption(CourseOfferingInfo courseOfferingInfo, String credits, String gradingOptionId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException,  DoesNotExistException {
        int firstValue = 0;

        // checking grading option. If null - just keep it that way
        if (!StringUtils.isEmpty(gradingOptionId) && (courseOfferingInfo.getStudentRegistrationGradingOptions().isEmpty() ||
                    !courseOfferingInfo.getStudentRegistrationGradingOptions().contains(gradingOptionId))) {
                throw new InvalidParameterException("Grading option doesn't match");
        }

        //Lookup the selected credit option and set from persisted values
        if (!courseOfferingInfo.getCreditOptionId().isEmpty()) {
            //Lookup the resultValueGroup Information
            ResultValuesGroupInfo resultValuesGroupInfo = CourseRegistrationAndScheduleOfClassesUtil.getLrcService().getResultValuesGroup(courseOfferingInfo.getCreditOptionId(), contextInfo);
            String typeKey = resultValuesGroupInfo.getTypeKey();

            //Get the actual values
            List<ResultValueInfo> resultValueInfos = CourseRegistrationAndScheduleOfClassesUtil.getLrcService().getResultValuesByKeys(resultValuesGroupInfo.getResultValueKeys(), contextInfo);

            if (!resultValueInfos.isEmpty()) {
                if (typeKey.equals(LrcServiceConstants.RESULT_VALUES_GROUP_TYPE_KEY_FIXED)) {
                    credits = resultValueInfos.get(firstValue).getValue(); // fixed credits
                } else if (typeKey.equals(LrcServiceConstants.RESULT_VALUES_GROUP_TYPE_KEY_RANGE)) {  // range
                    if (credits.isEmpty() || (Float.valueOf(credits).floatValue() < Float.valueOf(resultValuesGroupInfo.getResultValueRange().getMinValue()).floatValue()) ||
                            (Float.valueOf(credits).floatValue() > Float.valueOf(resultValuesGroupInfo.getResultValueRange().getMaxValue()).floatValue())) {
                        throw new InvalidParameterException("Credits are incorrect");
                    }
                } else if (typeKey.equals(LrcServiceConstants.RESULT_VALUES_GROUP_TYPE_KEY_MULTIPLE)) {  // multiple
                    List<String> creditOptions = new ArrayList<String>();
                    for (ResultValueInfo resultValueInfo : resultValueInfos) {
                        creditOptions.add(resultValueInfo.getValue());
                    }
                    if (credits.isEmpty() || !creditOptions.contains(credits)) {
                        throw new InvalidParameterException("Credits are incorrect");
                    }
                }
            }
        }

        return credits;
    }

}


