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
 */
package org.kuali.student.enrollment.class2.acal.service.impl;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.control.SelectControl;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.student.common.uif.service.impl.KSViewHelperServiceImpl;
import org.kuali.student.enrollment.class2.acal.dto.AcademicTermWrapper;
import org.kuali.student.enrollment.class2.acal.dto.AcalEventWrapper;
import org.kuali.student.enrollment.class2.acal.dto.HolidayCalendarWrapper;
import org.kuali.student.enrollment.class2.acal.dto.HolidayWrapper;
import org.kuali.student.enrollment.class2.acal.dto.KeyDateWrapper;
import org.kuali.student.enrollment.class2.acal.dto.KeyDatesGroupWrapper;
import org.kuali.student.enrollment.class2.acal.dto.TimeSetWrapper;
import org.kuali.student.enrollment.class2.acal.form.AcademicCalendarForm;
import org.kuali.student.enrollment.class2.acal.service.AcademicCalendarViewHelperService;
import org.kuali.student.enrollment.class2.acal.util.CalendarConstants;
import org.kuali.student.enrollment.class2.acal.util.CommonUtils;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.util.date.DateFormatters;
import org.kuali.student.r2.core.acal.dto.AcademicCalendarInfo;
import org.kuali.student.r2.core.acal.dto.AcalEventInfo;
import org.kuali.student.r2.core.acal.dto.HolidayCalendarInfo;
import org.kuali.student.r2.core.acal.dto.HolidayInfo;
import org.kuali.student.r2.core.acal.dto.KeyDateInfo;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.acal.service.TermCodeGenerator;
import org.kuali.student.r2.core.acal.service.impl.TermCodeGeneratorImpl;
import org.kuali.student.r2.core.atp.dto.AtpAtpRelationInfo;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.kuali.student.r2.core.class1.state.dto.StateInfo;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.constants.AtpServiceConstants;
import org.kuali.student.r2.core.constants.TypeServiceConstants;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equalIgnoreCase;


/**
 * This is the helper class for AcademicCalendar Controller
 *
 * @author Kuali Student Team
 */
public class AcademicCalendarViewHelperServiceImpl extends KSViewHelperServiceImpl implements AcademicCalendarViewHelperService {

    private final static Logger LOG = Logger.getLogger(AcademicCalendarViewHelperServiceImpl.class);

    private AcademicCalendarService acalService;
    private TypeService typeService;
    private AtpService atpService;
    private TermCodeGenerator termCodeGenerator;

    public AcademicCalendarViewHelperServiceImpl getInstance(){
        return this;
    }

    /**
     * This method builds an academic calendar for ui processing. Basically, it builds the wrappers
     * around acal,events,terms,holidays,keydates etc.
     *
     * @param acalId academic calendar id
     * @param acalForm AcademicCalendarForm
     */
    public void populateAcademicCalendar(String acalId, AcademicCalendarForm acalForm){

        if (LOG.isDebugEnabled()){
            LOG.debug("Loading Academic calendar for the id " + acalId);
        }

        try{

            AcademicCalendarInfo acalInfo = getAcalService().getAcademicCalendar(acalId,createContextInfo());

            acalForm.setAcademicCalendarInfo(acalInfo);
            acalForm.setAdminOrgName(getAdminOrgNameById(acalInfo.getAdminOrgId()));
            acalForm.setNewCalendar(false);
            acalForm.setOfficialCalendar(StringUtils.equals(acalInfo.getStateKey(),AtpServiceConstants.ATP_OFFICIAL_STATE_KEY));

            //Events
            List<AcalEventWrapper> events = populateEventWrappers(acalInfo.getId());
            acalForm.setEvents(events);

            //Holiday calendars associated with acal.
            List<HolidayCalendarWrapper> holidayCalendarWrapperList = populateHolidayCalendars(acalInfo.getHolidayCalendarIds());
            acalForm.setHolidayCalendarList(holidayCalendarWrapperList);

            //Terms (which in turn builds keydate groups and keydates)
            boolean calculateInstrDays = !holidayCalendarWrapperList.isEmpty();
            List<AcademicTermWrapper> termWrappers = populateTermWrappers(acalId, false,true);
            acalForm.setTermWrapperList(termWrappers);

            // set the meta info on the form
            acalForm.setMeta(acalInfo.getMeta());

        }catch(Exception e){
            if (LOG.isDebugEnabled()){
                LOG.debug("Error loading academic calendar [id=" + acalId + "] - " + e.getMessage());
            }
            throw convertServiceExceptionsToUI(e);
        }

    }

    /**
     * Builds the wrappers for all the holiday calendars associated with acal.
     *
     * @param holidayCalendarIds list of holiday calendars to populate
     * @return list of wrappers for the holiday calendars
     * @throws Exception
     */
    protected List<HolidayCalendarWrapper> populateHolidayCalendars(List<String> holidayCalendarIds) throws Exception {

        if (LOG.isDebugEnabled()){
            LOG.debug("Loading all the holiday calendars associated with the Acal");
        }

        List<HolidayCalendarWrapper> holidayCalendarWrapperList = new ArrayList<HolidayCalendarWrapper>();

        ContextInfo contextInfo = createContextInfo();
        for (String hcId : holidayCalendarIds){

            HolidayCalendarWrapper holidayCalendarWrapper = new HolidayCalendarWrapper();
            List<HolidayWrapper> holidays = new ArrayList<HolidayWrapper>();

            //need to retrieve HolidayCalendarInfo and all Holidays to form the HolidayCalendarWrapper.
            HolidayCalendarInfo holidayCalendarInfo = getAcalService().getHolidayCalendar(hcId, contextInfo);
            holidayCalendarWrapper.setHolidayCalendarInfo(holidayCalendarInfo);
            holidayCalendarWrapper.setId(holidayCalendarInfo.getId());
            holidayCalendarWrapper.setAdminOrgName(CommonUtils.getAdminOrgNameById(holidayCalendarInfo.getAdminOrgId()));
            StateInfo hcState = getAcalService().getHolidayCalendarState(holidayCalendarInfo.getStateKey(), contextInfo);
            holidayCalendarWrapper.setStateName(hcState.getName());

            List<HolidayInfo> holidayInfoList = getAcalService().getHolidaysForHolidayCalendar(holidayCalendarInfo.getId(), contextInfo);
            for(HolidayInfo holidayInfo : holidayInfoList){
                HolidayWrapper holiday = new HolidayWrapper(holidayInfo);
                TypeInfo typeInfo = getAcalService().getHolidayType(holidayInfo.getTypeKey(), contextInfo);
                holiday.setTypeName(typeInfo.getName());
                holidays.add(holiday);
            }

            holidayCalendarWrapper.setHolidays(holidays);
            holidayCalendarWrapperList.add(holidayCalendarWrapper);
        }

        return holidayCalendarWrapperList;

    }

    /**
     * Builds the wrapper for Events
     *
     * @param acalId
     * @return
     * @throws Exception
     */
    public List<AcalEventWrapper> populateEventWrappers(String acalId) throws Exception {

        if (LOG.isDebugEnabled()){
            LOG.debug("Loading all the holiday calendars associated with the Acal");
        }

        List<AcalEventInfo> eventInfos = getAcalService().getAcalEventsForAcademicCalendar(acalId, createContextInfo());
        List<AcalEventWrapper> events = new ArrayList<AcalEventWrapper>();

        for (AcalEventInfo eventInfo: eventInfos) {
            AcalEventWrapper event  = new AcalEventWrapper(eventInfo,false);
            TypeInfo type = getTypeInfo(event.getEventTypeKey());
            event.setEventTypeName(type.getName());
            events.add(event);
        }

        return events;
    }

    /**
     * Builds wrappers around the terms
     *
     * @param acalId
     * @param isCopy
     * @return
     */
    public List<AcademicTermWrapper> populateTermWrappers(String acalId, boolean isCopy,boolean calculateInstrDays){
        ContextInfo contextInfo = createContextInfo();

        if (LOG.isDebugEnabled()){
            LOG.debug("Loading all the terms associated with an acal [id=" + acalId + "]");
        }

        List<AcademicTermWrapper> termWrappers = new ArrayList<AcademicTermWrapper>();

        try {
            List<TermInfo> termInfos = getAcalService().getTermsForAcademicCalendar(acalId, contextInfo);
            // we go through the terms once to process all parent and sub terms. This list is to process everything else
            List<TermInfo> processedTerms = new ArrayList < TermInfo >();

            for (TermInfo termInfo : termInfos) {
                if(!processedTerms.contains(termInfo)){
                    List<AtpAtpRelationInfo> atpRelations = getAtpService().getAtpAtpRelationsByTypeAndAtp(termInfo.getId(), AtpServiceConstants.ATP_ATP_RELATION_INCLUDES_TYPE_KEY, contextInfo);
                    if (atpRelations != null && atpRelations.size() > 0) { // if you're a parent term
                        AcademicTermWrapper termWrapper = populateTermWrapper(termInfo, isCopy,calculateInstrDays); // create the term wrapper for the parent term
                        //add the parent term into the term wrapper list
                        termWrappers.add(termWrapper);
                        processedTerms.add(termInfo);

                        //add the sub terms into the term wrapper list
                        for (AtpAtpRelationInfo parentTermRelations : atpRelations) {
                            // we already have all the terms in the wrappers. We just need to set the parent child relationships
                            for(TermInfo tInfo : termInfos){
                                // Find the subterms
                                if(parentTermRelations.getRelatedAtpId().equals(tInfo.getId())){
                                    AcademicTermWrapper subTermWrapper = populateTermWrapper(tInfo, isCopy,calculateInstrDays);
                                    subTermWrapper.setParentTerm(termInfo.getTypeKey());   // the name here is ambigious
                                    subTermWrapper.setSubTerm(true);
                                    termWrapper.setHasSubterm(true);
                                    termWrapper.getSubterms().add(subTermWrapper);
                                    if (!isCopy){
                                        subTermWrapper.setParentTermInfo(termInfo);
                                    }
                                    termWrappers.add(subTermWrapper);
                                    processedTerms.add(tInfo);  // this term has now been processed
                                }
                            }
                        }
                    }
                }
            }
            // The previous loop deals with parents and children. Now we have to deal with term that aren't parents or children
            for (TermInfo termInfo : termInfos) {
                if(!processedTerms.contains(termInfo)){
                    AcademicTermWrapper termWrapper = populateTermWrapper(termInfo, isCopy,calculateInstrDays); // create the term wrapper for the parent term
                    //add the parent term into the term wrapper list
                    termWrappers.add(termWrapper);
                    processedTerms.add(termInfo);
                }
            }

            //Sort the termWrappers by start date
            Collections.sort(termWrappers, new Comparator<AcademicTermWrapper>() {
                @Override
                public int compare(AcademicTermWrapper termInfo1, AcademicTermWrapper termInfo2) {
                    return termInfo1.getStartDate().compareTo(termInfo2.getStartDate());
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return termWrappers;
    }

    public AcademicTermWrapper populateTermWrapper(TermInfo termInfo, boolean isCopy,boolean calculateInstrDays) throws Exception {

        if (LOG.isDebugEnabled()){
            LOG.debug("Populating Term - " + termInfo.getId());
        }

        TypeInfo type = getAcalService().getTermType(termInfo.getTypeKey(),createContextInfo());

        AcademicTermWrapper termWrapper = new AcademicTermWrapper(termInfo,isCopy);
        termWrapper.setTypeInfo(type);
        termWrapper.setTermNameForUI(type.getName());
        if (isCopy){
            termWrapper.setName(type.getName());
        }

        //Populate keydates
        List<KeyDateInfo> keydateList = getAcalService().getKeyDatesForTerm(termInfo.getId(),createContextInfo());
        List<TypeInfo> keyDateTypes = getTypeService().getAllowedTypesForType(termInfo.getTypeKey(),createContextInfo());

        Map<String,KeyDatesGroupWrapper> keyDateGroup = new HashMap<String,KeyDatesGroupWrapper>();

        for (KeyDateInfo keyDateInfo : keydateList) {
            KeyDateWrapper keyDateWrapper = new KeyDateWrapper(keyDateInfo,isCopy);
            type = getTypeService().getType(keyDateInfo.getTypeKey(),createContextInfo());
            keyDateWrapper.setTypeInfo(type);
            keyDateWrapper.setKeyDateNameUI(type.getName());

            addKeyDateGroup(keyDateTypes,keyDateWrapper,keyDateGroup);
        }

        for (KeyDatesGroupWrapper group : keyDateGroup.values()) {
            if (!group.getKeydates().isEmpty()){
                termWrapper.getKeyDatesGroupWrappers().add(group);
            }
        }

        if (calculateInstrDays){
            populateInstructionalDays(termWrapper);
        }

        return termWrapper;
    }

    /**
     * Adds a keydate to a proper group.
     *
     * @param keyDateTypes
     * @param keyDateWrapper
     * @param keyDateGroup
     */
    protected void addKeyDateGroup(List<TypeInfo> keyDateTypes,KeyDateWrapper keyDateWrapper,Map<String,KeyDatesGroupWrapper> keyDateGroup){
        if (LOG.isDebugEnabled()){
            LOG.debug("Adding key date to a group");
        }
        for (TypeInfo keyDateType : keyDateTypes) {
            try {
                List<TypeInfo> allowedTypes = getTypeService().getTypesForGroupType(keyDateType.getKey(), createContextInfo());
                for (TypeInfo allowedType : allowedTypes) {
                    if (StringUtils.equals(allowedType.getKey(),keyDateWrapper.getKeyDateType())){
                        KeyDatesGroupWrapper keyDatesGroup = keyDateGroup.get(keyDateType.getKey());
                        if (keyDatesGroup == null){
                            keyDatesGroup = new KeyDatesGroupWrapper(keyDateType.getKey(),keyDateType.getName());
                            keyDateGroup.put(keyDateType.getKey(),keyDatesGroup);
                        }
                        keyDatesGroup.getKeydates().add(keyDateWrapper);
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method finds the latest Academic Calendar.
     * It first tries to find the current year acal. If there is no match found, it looks for last year
     *
     * @return
     * @throws Exception
     */
    public AcademicCalendarInfo getLatestAcademicCalendar() throws Exception {

        if (LOG.isDebugEnabled()){
            LOG.debug("Finding the latest Academic calendar");
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<AcademicCalendarInfo> academicCalendarInfoList =
                getAcalService().getAcademicCalendarsByStartYear(currentYear, createContextInfo());
        if ((null == academicCalendarInfoList) || academicCalendarInfoList.isEmpty()) {
            academicCalendarInfoList =
                    getAcalService().getAcademicCalendarsByStartYear((currentYear - 1), createContextInfo());
        }

        if ((null == academicCalendarInfoList) || (academicCalendarInfoList.size() == 0)) {
            return null;
        }
        else {
            // If Calendars are found search through them to find the most recently created.
            // The number of calendars should be small so naive search possible.
            AcademicCalendarInfo newestCalendar =  academicCalendarInfoList.get(0);
            for(AcademicCalendarInfo calendarTemp: academicCalendarInfoList){
                // Compare the time when the calendars are created and pick the higher one (most recent).
                if(calendarTemp.getMeta().getCreateTime().compareTo(newestCalendar.getMeta().getCreateTime())>0){
                    newestCalendar = calendarTemp;
                }
            }

            return newestCalendar;
        }
    }

    public void copyToCreateAcademicCalendar(AcademicCalendarForm form){

           AcademicCalendarInfo orgAcalInfo = form.getCopyFromAcal();

           if (orgAcalInfo == null || StringUtils.isBlank(orgAcalInfo.getId())){
               throw new RuntimeException("ACal Info doesn't exists to copy.");
           }

           // 1. copy over events
        List<AcalEventInfo> orgEventInfoList= null;
        try {
            orgEventInfoList = getAcalService().getAcalEventsForAcademicCalendar(orgAcalInfo.getId(), createContextInfo());
        } catch (Exception e) {
            throw convertServiceExceptionsToUI(e);
        }

        List<AcalEventWrapper> newEventList = new ArrayList<AcalEventWrapper>();
           for (AcalEventInfo orgEventInfo : orgEventInfoList){
               AcalEventWrapper newEvent= new AcalEventWrapper(orgEventInfo,true);
               try {
                   TypeInfo type = getTypeInfo(orgEventInfo.getTypeKey());
                   newEvent.setEventTypeName(type.getName());
               }catch (Exception e){
                   throw convertServiceExceptionsToUI(e);
               }
               newEventList.add(newEvent);
           }
           form.setEvents(newEventList);

          // 2. copy over terms
          List<AcademicTermWrapper> newTermList = populateTermWrappers(orgAcalInfo.getId(), true,false);
          form.setTermWrapperList(newTermList);
          form.setMeta(orgAcalInfo.getMeta());

    }

    /**
     * Performs validation on adding holiday calendar, key date groups, key date or event.
     *
     * @param view
     * @param collectionGroup
     * @param model
     * @param addLine
     * @return
     */
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model, Object addLine) {

        if (addLine instanceof HolidayCalendarWrapper) {
            AcademicCalendarForm form = (AcademicCalendarForm) model;
            for (HolidayCalendarWrapper holidayCalendarWrapper : form.getHolidayCalendarList()) {
                String holidayCalendarId = holidayCalendarWrapper.getId();
                if (StringUtils.equals(holidayCalendarWrapper.getId(), ((HolidayCalendarWrapper) addLine).getId())) {
                    GlobalVariables.getMessageMap().putError("newCollectionLines['holidayCalendarList'].id",
                            CalendarConstants.MessageKeys.ERROR_DUPLICATE_HCAL,
                            holidayCalendarWrapper.getHolidayCalendarInfo().getName());
                    return false;
                }
            }
        } else if (addLine instanceof KeyDatesGroupWrapper) {
            AcademicCalendarForm form = (AcademicCalendarForm) model;
            form.setAddLineValid(true);
            form.setValidationJSONString("");
            KeyDatesGroupWrapper keydateGroup = (KeyDatesGroupWrapper) addLine;
            if(StringUtils.isEmpty(keydateGroup.getKeyDateGroupType())) {
                GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_GROUP_TYPE_REQUIRED);
                StringBuilder sb = new StringBuilder();
                sb.append("\"key_date_group_type\":\"Required\"");
                form.setValidationJSONString("{"+sb.toString()+"}");
                form.setAddLineValid(false);
                return false;
            }
        }
        else if (addLine instanceof KeyDateWrapper) {
            AcademicCalendarForm form = (AcademicCalendarForm) model;
            form.setAddLineValid(true);
            form.setValidationJSONString("");
            StringBuilder sb = new StringBuilder();
            KeyDateWrapper keydate = (KeyDateWrapper)addLine;
            boolean isValid = true;

            //identify termWrapper for keydates
            String selectedCollectionPath = form.getActionParamaterValue("selectedCollectionPath");
            int selectedTermWrapperIndex = Integer.parseInt(selectedCollectionPath.substring(selectedCollectionPath.indexOf("[")+1, selectedCollectionPath.indexOf("]")));
            AcademicTermWrapper termWrapper = form.getTermWrapperList().get(selectedTermWrapperIndex);

            // The Key Date Type should not be null
            if(StringUtils.isEmpty(keydate.getKeyDateType())) {
                GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_TYPE_REQUIRED);
                sb.append("\"key_date_type\":\"Required\"");
                isValid = false;
            }

            // Start Date should not be null
            if(keydate.getStartDate()==null){
                if(termWrapper.isSubTerm()){
                    GlobalVariables.getMessageMap().putWarningForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_START_DATE_REQUIRED, keydate.getKeyDateNameUI());
                }else{
                    GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_START_DATE_REQUIRED, keydate.getKeyDateNameUI());
                    isValid = false;
                    if(!StringUtils.isEmpty(sb.toString())){
                        sb.append(",");
                    }
                    sb.append("\"key_date_start_date\":\"Required\"");
                }
            }

            // If Date Range is checked
            if(keydate.isDateRange()){
                // End date should not be null
                if(keydate.getEndDate()==null){
                    if(termWrapper.isSubTerm()){
                        GlobalVariables.getMessageMap().putWarningForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_END_DATE_REQUIRED,keydate.getKeyDateNameUI());
                    }else{
                        GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_END_DATE_REQUIRED,keydate.getKeyDateNameUI());
                        isValid = false;
                        if(!StringUtils.isEmpty(sb.toString())){
                            sb.append(",");
                        }
                        sb.append("\"key_date_end_date\":\"Required\"");
                    }
                }else{
                    // The start date should come before the end date
                    if (!CommonUtils.isValidDateRange(keydate.getStartDate(),keydate.getEndDate())){
                        GlobalVariables.getMessageMap().putWarningForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,keydate.getKeyDateNameUI(),CommonUtils.formatDate(keydate.getStartDate()),CommonUtils.formatDate(keydate.getEndDate()));
                    }
                }
            }

            // Key Dates start and end dates should be within the start and end dates of the term
            if (!CommonUtils.isDateWithinRange(termWrapper.getStartDate(),termWrapper.getEndDate(),keydate.getStartDate()) ||
                    !CommonUtils.isDateWithinRange(termWrapper.getStartDate(),termWrapper.getEndDate(),keydate.getEndDate())){
                GlobalVariables.getMessageMap().putWarningForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_INVALID_DATERANGE_KEYDATE,keydate.getKeyDateNameUI(),termWrapper.getName());
            }

            // If All Day is not checked, Times should not be null.
            if(!keydate.isAllDay()){
                if(StringUtils.isEmpty(keydate.getStartTime())){
                    GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_START_TIME_REQUIRED,keydate.getKeyDateNameUI());
                    isValid = false;
                    if(!StringUtils.isEmpty(sb.toString())){
                        sb.append(",");
                    }
                    sb.append("\"key_date_start_time\":\"Required\"");
                }else{
                    if(StringUtils.isEmpty(keydate.getStartTimeAmPm())){
                        GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_TIME_START_AMPM_REQUIRED,keydate.getKeyDateNameUI());
                        isValid = false;
                        if(!StringUtils.isEmpty(sb.toString())){
                            sb.append(",");
                        }
                        sb.append("\"key_date_start_time_ampm\":\"Required\"");
                    }
                }
                if(StringUtils.isEmpty(keydate.getEndTime())){
                    GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_KEY_DATE_END_TIME_REQUIRED,keydate.getKeyDateNameUI());
                    isValid = false;
                    if(!StringUtils.isEmpty(sb.toString())){
                        sb.append(",");
                    }
                    sb.append("\"key_date_end_time\":\"Required\"");
                }else{
                    if(StringUtils.isEmpty(keydate.getEndTimeAmPm())){
                        GlobalVariables.getMessageMap().putErrorForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_TIME_END_AMPM_REQUIRED,keydate.getKeyDateNameUI());
                        isValid = false;
                        if(!StringUtils.isEmpty(sb.toString())){
                            sb.append(",");
                        }
                        sb.append("\"key_date_end_time_ampm\":\"Required\"");
                    }
                }
            }

            if(!isValid){
                form.setValidationJSONString("{"+sb.toString()+"}");
                form.setAddLineValid(false);
                return false;
            }
            if (!isValidTimeSetWrapper(keydate, keydate.getKeyDateNameUI(), collectionGroup.getAddLineBindingInfo().getBindingPath())) {
                return false;
            }
        }
        else if (addLine instanceof AcalEventWrapper) {
            AcalEventWrapper eventWrapper = (AcalEventWrapper)addLine;
            if (!isValidTimeSetWrapper(eventWrapper, eventWrapper.getEventTypeName(),
                    "newCollectionLines['events']")) {
                return false;
            }
        }
        else if(addLine instanceof AcademicTermWrapper) {
            //if tries to add a Subterm, the parent term has to exist in the Form
            AcademicTermWrapper term = (AcademicTermWrapper) addLine;
            AcademicCalendarForm acalForm = (AcademicCalendarForm) model;
            if (term.getParentTerm() != null &&
                    !StringUtils.isBlank(term.getParentTerm())){

                AcademicTermWrapper parentTerm=null;
                for (AcademicTermWrapper termWrapper : acalForm.getTermWrapperList()){
                    String termType = termWrapper.getTermType();
                    if (StringUtils.isBlank(termType)){
                        termType = termWrapper.getTermInfo().getTypeKey();
                    }
                    if (term.getParentTerm().equals(termType)){
                        parentTerm =termWrapper;
                        break;
                    }
                }

                if(parentTerm==null){
                    return false;
                }

                if (!CommonUtils.isDateWithinRange(parentTerm.getStartDate(),parentTerm.getEndDate(),term.getStartDate()) ||
                        !CommonUtils.isDateWithinRange(parentTerm.getStartDate(),parentTerm.getEndDate(),term.getEndDate())){
                    GlobalVariables.getMessageMap().putWarningForSectionId(collectionGroup.getId(), CalendarConstants.MessageKeys.ERROR_TERM_NOT_IN_TERM_RANGE,term.getName(),parentTerm.getName());
                }
            }

            return true;
        }
        return super.performAddLineValidation(view, collectionGroup, model, addLine);
    }

    /**
     * This method is being called by KRAD to populate keydate types drop down. There would be no reference
     * for this method in the code as it has it's reference at the AcademicTermPage.xml page
     *
     * @param field
     * @param acalForm
     */
    @SuppressWarnings("unused")
    public void populateKeyDateTypes(InputField field, AcademicCalendarForm acalForm) {

        boolean isAddLine = BooleanUtils.toBoolean((Boolean)field.getContext().get(UifConstants.ContextVariableNames.IS_ADD_LINE));
        if (!isAddLine) {
            return;
        }

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", "Select Keydate Type"));

        CollectionGroup collectionGroup = (CollectionGroup)field.getContext().get(UifConstants.ContextVariableNames.PARENT);
        KeyDatesGroupWrapper groupWrapper = ObjectPropertyUtils.getPropertyValue(acalForm,collectionGroup.getBindingInfo().getBindByNamePrefix());

        if (StringUtils.isNotBlank(groupWrapper.getKeyDateGroupType())){
            try {
                List<TypeInfo> types = getTypeService().getTypesForGroupType(groupWrapper.getKeyDateGroupType(),createContextInfo());
                for (TypeInfo type : types) {
                    if (!groupWrapper.isKeyDateExists(type.getKey())){
                        keyValues.add(new ConcreteKeyValue(type.getKey(), type.getName()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        ((SelectControl) field.getControl()).setOptions(keyValues);

    }

    /**
     * This method is being called by KRAD to populate keydate group types drop down. There would be no reference
     * for this method in the code as it has it's reference at the AcademicTermPage.xml page
     *
     * @param field
     * @param acalForm
     */
    @SuppressWarnings("unused")
    public void populateKeyDateGroupTypes(InputField field, AcademicCalendarForm acalForm) {

        boolean isAddLine = BooleanUtils.toBoolean((Boolean)field.getContext().get(UifConstants.ContextVariableNames.IS_ADD_LINE));
        if (!isAddLine) {
            return;
        }

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", "Select Keydate Group Type"));

        CollectionGroup collectionGroup = (CollectionGroup)field.getContext().get(UifConstants.ContextVariableNames.COLLECTION_GROUP);
        AcademicTermWrapper termWrapper = ObjectPropertyUtils.getPropertyValue(acalForm,collectionGroup.getBindingInfo().getBindByNamePrefix());

        try {
            List<TypeInfo> keyDateGroupTypes = getAcalService().getKeyDateTypesForTermType(termWrapper.getTermType(),createContextInfo());
            for (TypeInfo keyDateGroupType : keyDateGroupTypes) {
                if (!termWrapper.isKeyDateGroupExists(keyDateGroupType.getKey())){
                    keyValues.add(new ConcreteKeyValue(keyDateGroupType.getKey(),keyDateGroupType.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ((SelectControl) field.getControl()).setOptions(keyValues);

    }

    /**
     * This method is called during calendar save. As there is inconsistency between ui and the services handling
     * the allday and daterange, this method is like an adapter to convert the ui data to the data needed by services.
     *
     * In Services, it handles date range as point in time
     * More info at https://wiki.kuali.org/display/STUDENT/Storing+and+Querying+Milestone+Dates
     *
     * @param acalForm
     */
    public void populateAcademicCalendarDefaults(AcademicCalendarForm acalForm){

        for (AcalEventWrapper eventWrapper : acalForm.getEvents()) {
            eventWrapper.getAcalEventInfo().setStartDate(getStartDateWithUpdatedTime(eventWrapper,false));
            setEventEndDate(eventWrapper);
        }

        for (AcademicTermWrapper academicTermWrapper : acalForm.getTermWrapperList()) {
            for (KeyDatesGroupWrapper keyDatesGroupWrapper : academicTermWrapper.getKeyDatesGroupWrappers()){
                for(KeyDateWrapper keyDateWrapper : keyDatesGroupWrapper.getKeydates()){
                    keyDateWrapper.getKeyDateInfo().setStartDate(getStartDateWithUpdatedTime(keyDateWrapper,false));
                    setKeyDateEndDate(keyDateWrapper);
                }
            }
        }
    }

    /**
     * Validates Academic Calendar
     *
     * @param acalForm
     */
    public void validateAcademicCalendar(AcademicCalendarForm acalForm){

        AcademicCalendarInfo acal = acalForm.getAcademicCalendarInfo();

        //Validate Acal Name for duplication
        if (!isValidAcalName(acalForm.getAcademicCalendarInfo())){
            GlobalVariables.getMessageMap().putError("academicCalendarInfo.name", CalendarConstants.MessageKeys.ERROR_DUPLICATE_NAME);
        }

        if (!CommonUtils.isValidDateRange(acal.getStartDate(),acal.getEndDate())){
            GlobalVariables.getMessageMap().putErrorForSectionId("KS-AcademicCalendar-MetaSection", CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,"Calendar",CommonUtils.formatDate(acal.getStartDate()),CommonUtils.formatDate(acal.getEndDate()));
        }

        //Validate Events
        for (AcalEventWrapper eventWrapper : acalForm.getEvents()) {
            if (!CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),eventWrapper.getStartDate()) ||
                !CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),eventWrapper.getEndDate())){
                GlobalVariables.getMessageMap().putWarningForSectionId("acal-info-event", CalendarConstants.MessageKeys.ERROR_DATE_NOT_IN_ACAL_RANGE,eventWrapper.getEventTypeName());
            }
            if (eventWrapper.isDateRange() && !CommonUtils.isValidDateRange(eventWrapper.getStartDate(),eventWrapper.getEndDate())){
                GlobalVariables.getMessageMap().putWarningForSectionId("acal-info-event", CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,eventWrapper.getEventTypeName(),CommonUtils.formatDate(eventWrapper.getStartDate()),CommonUtils.formatDate(eventWrapper.getEndDate()));
            }
        }

        //Validate Holiday Calendar are in the date range of the Academic Calendar
        for (HolidayCalendarWrapper holidayCalendarWrapper : acalForm.getHolidayCalendarList())  {
            if (!CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),holidayCalendarWrapper.getHolidayCalendarInfo().getStartDate()) ||
                    !CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),holidayCalendarWrapper.getHolidayCalendarInfo().getEndDate())){
                GlobalVariables.getMessageMap().putWarning(KRADConstants.GLOBAL_MESSAGES, CalendarConstants.MessageKeys.ERROR_DATE_NOT_IN_ACAL_RANGE,"Added Holiday Calendar: " + holidayCalendarWrapper.getHolidayCalendarInfo().getName());
            }
        }
        //Validate Terms and keydates
        for (int index=0; index < acalForm.getTermWrapperList().size(); index++) {
            validateTerm(acalForm.getTermWrapperList(),index,acal);
        }

    }

    /**
     * Make sure the user entered Acal name doesnt duplicate with the existing ones
     *
     * @param acal
     * @return
     */
    private boolean isValidAcalName(AcademicCalendarInfo acal){

        QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
        List<Predicate> pList = new ArrayList<Predicate>();

        Predicate p = equal("atpType",AcademicCalendarServiceConstants.ACADEMIC_CALENDAR_TYPE_KEY);
        pList.add(p);

        p = equalIgnoreCase("name", acal.getName());
        pList.add(p);

        Predicate[] preds = new Predicate[pList.size()];
        pList.toArray(preds);
        qBuilder.setPredicates(and(preds));

        try {
            List<AcademicCalendarInfo> acals = getAcalService().searchForAcademicCalendars(qBuilder.build(),createContextInfo());
            boolean valid = acals.isEmpty();
            //Make sure it's not the same Acal which is being edited by the user
            if (!valid && StringUtils.isNotBlank(acal.getId())){
                for (AcademicCalendarInfo academicCalendarInfo : acals) {
                    if (!StringUtils.equals(academicCalendarInfo.getId(),acal.getId())){
                        valid = false;
                        break;
                    }
                    valid = true;
                }
            }

            return valid;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // NOTE: edits here should not be needed if KRAD validation is working properly...
    private boolean isValidTimeSetWrapper(TimeSetWrapper wrapper, String wrapperName, String lineName) {
        boolean isValid = true;

        // KRAD 2.2.0-M1 can handle endDate, but acal not currently using it because of addLine bug
        if (wrapper.isDateRange() && (null == wrapper.getEndDate())) {
            GlobalVariables.getMessageMap().putError(lineName+".endDate",
                    CalendarConstants.MessageKeys.ERROR_DATE_END_REQUIRED, wrapperName);
            isValid = false;
        }

        if (!wrapper.isAllDay()) { // time fields are enabled and can be filled in
            if (!StringUtils.isEmpty(wrapper.getStartTime()) && StringUtils.isEmpty(wrapper.getStartTimeAmPm())) {
                GlobalVariables.getMessageMap().putError(lineName+".startTimeAmPm",
                        CalendarConstants.MessageKeys.ERROR_TIME_START_AMPM_REQUIRED, wrapperName);
                isValid = false;
            }
            if (!StringUtils.isEmpty(wrapper.getEndTime()) && StringUtils.isEmpty(wrapper.getEndTimeAmPm())) {
                GlobalVariables.getMessageMap().putError(lineName+".endTimeAmPm",
                        CalendarConstants.MessageKeys.ERROR_TIME_END_AMPM_REQUIRED, wrapperName);
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Validates the term at the given index
     *
     * @param termWrapper list of terms in an academic calendar
     * @param termToValidateIndex index of the term to be validated
     * @param acal ACal dto needed to compare the start and end date
     */
    public void validateTerm(List<AcademicTermWrapper> termWrapper,int termToValidateIndex,AcademicCalendarInfo acal) {

        AcademicTermWrapper termWrapperToValidate = termWrapper.get(termToValidateIndex);

        int index2 = 0;
        //Validate duplicate term name
        for (AcademicTermWrapper wrapper : termWrapper) {
            index2++;
            if (wrapper != termWrapperToValidate){
                if (StringUtils.equalsIgnoreCase(wrapper.getName(),termWrapperToValidate.getName())){
                    GlobalVariables.getMessageMap().putErrorForSectionId("acal-term", CalendarConstants.MessageKeys.ERROR_DUPLICATE_TERM_NAME,""+ NumberUtils.min(new int[]{termToValidateIndex,index2}),""+NumberUtils.max(new int[]{termToValidateIndex,index2}));
                }
            }
        }

        if (!CommonUtils.isValidDateRange(termWrapperToValidate.getStartDate(),termWrapperToValidate.getEndDate())){
            GlobalVariables.getMessageMap().putErrorForSectionId("acal-term", CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,termWrapperToValidate.getName(),CommonUtils.formatDate(termWrapperToValidate.getStartDate()),CommonUtils.formatDate(termWrapperToValidate.getEndDate()));
        }

        if (!CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),termWrapperToValidate.getStartDate()) ||
            !CommonUtils.isDateWithinRange(acal.getStartDate(),acal.getEndDate(),termWrapperToValidate.getEndDate())){
            GlobalVariables.getMessageMap().putWarningForSectionId("acal-term", CalendarConstants.MessageKeys.ERROR_TERM_NOT_IN_ACAL_RANGE,termWrapperToValidate.getName());
        }
        if(termWrapperToValidate.isSubTerm()){
            if(termWrapperToValidate.getParentTermInfo()!= null){
                if (!CommonUtils.isDateWithinRange(termWrapperToValidate.getParentTermInfo().getStartDate(),termWrapperToValidate.getParentTermInfo().getEndDate(),termWrapperToValidate.getStartDate()) ||
                        !CommonUtils.isDateWithinRange(termWrapperToValidate.getParentTermInfo().getStartDate(),termWrapperToValidate.getParentTermInfo().getEndDate(),termWrapperToValidate.getEndDate())){
                    GlobalVariables.getMessageMap().putWarningForSectionId("acal-term", CalendarConstants.MessageKeys.ERROR_TERM_NOT_IN_TERM_RANGE,termWrapperToValidate.getName(),termWrapperToValidate.getParentTermInfo().getName());
                }
            }else{
                // Find term manually if calendar hasn't already been saved.
                AcademicTermWrapper parentTerm=null;
                for (AcademicTermWrapper term :termWrapper){
                    String termType = term.getTermType();
                    if (StringUtils.isBlank(termType)){
                        termType = term.getTermInfo().getTypeKey();
                    }
                    if (termWrapperToValidate.getParentTerm().equals(termType)){
                        parentTerm =term;
                        break;
                    }
                }

                if (!CommonUtils.isDateWithinRange(parentTerm.getStartDate(),parentTerm.getEndDate(),termWrapperToValidate.getStartDate()) ||
                        !CommonUtils.isDateWithinRange(parentTerm.getStartDate(),parentTerm.getEndDate(),termWrapperToValidate.getEndDate())){
                    GlobalVariables.getMessageMap().putWarningForSectionId("acal-term", CalendarConstants.MessageKeys.ERROR_TERM_NOT_IN_TERM_RANGE,termWrapperToValidate.getName(),parentTerm.getName());
                }
            }
        }

        for (KeyDatesGroupWrapper keyDatesGroupWrapper : termWrapperToValidate.getKeyDatesGroupWrappers()){
            for(KeyDateWrapper keyDateWrapper : keyDatesGroupWrapper.getKeydates()){

                // Start Date should not be null
                if(keyDateWrapper.getStartDate()==null){
                    GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_KEY_DATE_START_DATE_REQUIRED, keyDateWrapper.getKeyDateNameUI());
                }

                // If Date Range is checked
                if(keyDateWrapper.isDateRange()){
                    // End date should not be null
                    if(keyDateWrapper.getEndDate()==null){
                        GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_DATE_END_REQUIRED,keyDateWrapper.getKeyDateNameUI());
                    }else{
                        // The start date should come before the end date
                        if (!CommonUtils.isValidDateRange(keyDateWrapper.getStartDate(),keyDateWrapper.getEndDate())){
                            GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,keyDateWrapper.getKeyDateNameUI(),CommonUtils.formatDate(keyDateWrapper.getStartDate()),CommonUtils.formatDate(keyDateWrapper.getEndDate()));
                        }
                    }
                }

                // If All Day is not checked
                if(!keyDateWrapper.isAllDay()){
                    // Start time should not be null
                    if(StringUtils.isEmpty(keyDateWrapper.getStartTime())){
                        GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_KEY_DATE_START_DATE_REQUIRED,keyDateWrapper.getKeyDateNameUI());
                    }else{
                        // If start date is entered Am or Pm should be selected
                        if(StringUtils.isEmpty(keyDateWrapper.getStartTimeAmPm())){
                            GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_TIME_START_AMPM_REQUIRED,keyDateWrapper.getKeyDateNameUI());
                        }
                    }
                    // End time should not be null
                    if(StringUtils.isEmpty(keyDateWrapper.getEndTime())){
                        GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_KEY_DATE_END_DATE_REQUIRED,keyDateWrapper.getKeyDateNameUI());
                    }else{
                        // If end date is entered Am or Pm should be selected
                        if(StringUtils.isEmpty(keyDateWrapper.getEndTimeAmPm())){
                            GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_TIME_END_AMPM_REQUIRED,keyDateWrapper.getKeyDateNameUI());
                        }
                    }
                }

                // Start and End Dates of the key date entry should be within the start and end dates of the term.
                if (!CommonUtils.isDateWithinRange(termWrapperToValidate.getStartDate(),termWrapperToValidate.getEndDate(),keyDateWrapper.getStartDate()) ||
                        !CommonUtils.isDateWithinRange(termWrapperToValidate.getStartDate(),termWrapperToValidate.getEndDate(),keyDateWrapper.getEndDate())){
                    GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydatesgroup_line"+termToValidateIndex, CalendarConstants.MessageKeys.ERROR_INVALID_DATERANGE_KEYDATE,keyDateWrapper.getKeyDateNameUI(),termWrapperToValidate.getName());
                }
            }
        }

    }

    /**
     * Calculates and populates the instructional days for a term
     *
     * @param termWrapper
     * @throws Exception
     */
    public void populateInstructionalDays(AcademicTermWrapper termWrapper) {
        if (termWrapper.getKeyDatesGroupWrappers() != null){
            for (KeyDatesGroupWrapper keyDatesGroupWrapper : termWrapper.getKeyDatesGroupWrappers()) {
                 if (keyDatesGroupWrapper.getKeydates() != null){
                     for (KeyDateWrapper keydate : keyDatesGroupWrapper.getKeydates()) {
                         if (StringUtils.equals(keydate.getKeyDateType(),AtpServiceConstants.MILESTONE_INSTRUCTIONAL_PERIOD_TYPE_KEY) &&
                             termWrapper.getTermInfo() != null && StringUtils.isNotBlank(termWrapper.getTermInfo().getId())){
                             try{
                                 int instructionalDays = getAcalService().getInstructionalDaysForTerm(termWrapper.getTermInfo().getId(),createContextInfo());
                                 termWrapper.setInstructionalDays(instructionalDays);
                             }catch(Exception e){  // Calculating instructional days should not block the normal operation
                                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_ERRORS, CalendarConstants.MessageKeys.ERROR_CALCULATING_INSTRUCTIONAL_DAYS,termWrapper.getTermNameForUI(),e.getMessage());
                             }
                             break;
                         }
                     }
                 }

            }
        }
    }

    protected Date getStartDateWithUpdatedTime(TimeSetWrapper timeSetWrapper,boolean isSaveAction){
        //If start time not blank, set that with the date. If it's empty, just update with default
        if (!timeSetWrapper.isAllDay()){
            if (StringUtils.isNotBlank(timeSetWrapper.getStartTime())){
                String startTime = timeSetWrapper.getStartTime();
                String startTimeApPm = timeSetWrapper.getStartTimeAmPm();
                //On save to DB, have to replace 12AM to 00AM insead of DB considers as 12PM
                if (isSaveAction && StringUtils.startsWith(startTime,"12:") && StringUtils.equalsIgnoreCase(startTimeApPm,"am")){
                    startTime = StringUtils.replace(startTime,"12:","00:");
                }
                return CommonUtils.getDateWithTime(timeSetWrapper.getStartDate(),startTime,startTimeApPm);
            }else{
                timeSetWrapper.setStartTime("12:00");
                timeSetWrapper.setStartTimeAmPm("AM");
                return CommonUtils.getDateWithTime(timeSetWrapper.getStartDate(),
                                           timeSetWrapper.getStartTime(),timeSetWrapper.getStartTimeAmPm());
            }
        }else{
            return timeSetWrapper.getStartDate();
        }

    }

    protected void setEventEndDate(AcalEventWrapper eventWrapper) {
        if (eventWrapper.isAllDay()) {
            eventWrapper.getAcalEventInfo().setIsDateRange(eventWrapper.isDateRange());
        }
        else {
            // dateRange in db is true if end date OR end time != start date/time
            eventWrapper.getAcalEventInfo().setIsDateRange(true);
        }
        Date endDateToInfo = timeSetWrapperEndDate(eventWrapper);
        eventWrapper.getAcalEventInfo().setEndDate(endDateToInfo);
    }

    protected void setKeyDateEndDate(KeyDateWrapper keyDateWrapper) {
        if (keyDateWrapper.isAllDay()) {
            keyDateWrapper.getKeyDateInfo().setIsDateRange(keyDateWrapper.isDateRange());
        }
        else {
            // dateRange in db is true if end date OR end time != start date/time
            keyDateWrapper.getKeyDateInfo().setIsDateRange(true);
        }
        Date endDateToInfo = timeSetWrapperEndDate(keyDateWrapper);
        keyDateWrapper.getKeyDateInfo().setEndDate(endDateToInfo);
    }

    protected Date timeSetWrapperEndDate(TimeSetWrapper timeSetWrapper) {
        Date endDateToInfo;

        if (timeSetWrapper.isAllDay()) {
            if (timeSetWrapper.isDateRange()) {
                //just clearing out any time already set in end date
                endDateToInfo = CommonUtils.getDateWithTime(timeSetWrapper.getEndDate(),"00:00",StringUtils.EMPTY);
            }
            else {
                endDateToInfo = null;
                timeSetWrapper.setEndDate(null);
            }

            // set the UI time & am/pm fields to null in case they just had values:
            timeSetWrapper.setStartTime(null);
            timeSetWrapper.setStartTimeAmPm(null);
            timeSetWrapper.setEndTime(null);
            timeSetWrapper.setEndTimeAmPm(null);
        }
        else {
            String endTime = timeSetWrapper.getEndTime();
            String endTimeAmPm = timeSetWrapper.getEndTimeAmPm();
            Date endDate = timeSetWrapper.getEndDate();

            //If it's not date range, then set
            if (!timeSetWrapper.isDateRange()){
                endDate = timeSetWrapper.getStartDate();
                timeSetWrapper.setEndDate(null);
            }

            if (StringUtils.isBlank(endTime)){
                endTime = CalendarConstants.DEFAULT_END_TIME;
                endTimeAmPm = "PM";
                timeSetWrapper.setEndTime(endTime);
                timeSetWrapper.setEndTimeAmPm(endTimeAmPm);
            }

            endDateToInfo = CommonUtils.getDateWithTime(endDate,endTime,endTimeAmPm);
        }

        return endDateToInfo;
    }

    /**
     * Process before adding a term, key date group, holiday calendar or event
     *
     * @param view
     * @param collectionGroup
     * @param model
     * @param addLine
     */
    protected void processBeforeAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {

        if (addLine instanceof AcademicTermWrapper){
            AcademicTermWrapper newLine = (AcademicTermWrapper)addLine;
            AcademicCalendarForm acalForm = (AcademicCalendarForm) model;
            //need to handle Term vs subTerm in different way
            try {
                TypeInfo termType = getAcalService().getTermType(newLine.getTermType(),createContextInfo());
                if (StringUtils.isBlank(newLine.getParentTerm())){ //try to add a term
                    newLine.setTermNameForUI(termType.getName());
                    newLine.setName(termType.getName() + " " + DateFormatters.DEFULT_YEAR_FORMATTER.format(newLine.getStartDate()));
                    newLine.setTypeInfo(termType);
                    newLine.setSubTerm(false);

                }
                else {//try to add a subterm
                    if(isParentTermExisting(newLine.getParentTerm(), acalForm.getTermWrapperList(),null)) {
                        newLine.setTermNameForUI(termType.getName());
                        newLine.setName(termType.getName() + " " + DateFormatters.DEFULT_YEAR_FORMATTER.format(newLine.getStartDate()));
                        newLine.setTypeInfo(termType);
                        newLine.setSubTerm(true);
                        AcademicTermWrapper parentTermWrapper = getParentTermInForm(newLine.getParentTerm(), acalForm.getTermWrapperList());
                        if(parentTermWrapper != null){
                            populateParentTermToSubterm(parentTermWrapper, newLine);
                        }
                        parentTermWrapper.setHasSubterm(true);
                        parentTermWrapper.getSubterms().add(newLine);
                    }
                    //otherwise, let performAddLineValidation to handle and post validation error
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (addLine instanceof KeyDatesGroupWrapper){
            KeyDatesGroupWrapper group = (KeyDatesGroupWrapper)addLine;
            if(StringUtils.isNotEmpty(group.getKeyDateGroupType())) {
                try {
                    TypeInfo termType = getTypeInfo(group.getKeyDateGroupType());
                    group.setKeyDateGroupNameUI(termType.getName());
                    group.setTypeInfo(termType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }else if (addLine instanceof HolidayCalendarInfo) {
            HolidayCalendarInfo inputLine = (HolidayCalendarInfo)addLine;
            try {
                System.out.println("HC id =" +inputLine.getId());

                HolidayCalendarInfo exists = getAcalService().getHolidayCalendar(inputLine.getId(), createContextInfo());

                inputLine.setName(exists.getName());
                inputLine.setId(exists.getId());
                inputLine.setTypeKey(exists.getTypeKey());
                inputLine.setAdminOrgId(exists.getAdminOrgId());
                inputLine.setStartDate(exists.getStartDate());
                inputLine.setEndDate(exists.getEndDate());
            }catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if (addLine instanceof HolidayCalendarWrapper){
            HolidayCalendarWrapper inputLine = (HolidayCalendarWrapper)addLine;
            List<HolidayWrapper> holidays = new ArrayList<HolidayWrapper>();
            try {
                String holidayCalendarId = inputLine.getId();
                if (!StringUtils.isEmpty(holidayCalendarId)) {
                    HolidayCalendarInfo hcInfo = getAcalService().getHolidayCalendar(inputLine.getId(), createContextInfo());
                    inputLine.setHolidayCalendarInfo(hcInfo);
                    inputLine.setAdminOrgName(CommonUtils.getAdminOrgNameById(hcInfo.getAdminOrgId()));
                    StateInfo hcState = getAcalService().getHolidayCalendarState(hcInfo.getStateKey(), createContextInfo());
                    inputLine.setStateName(hcState.getName());
                    List<HolidayInfo> holidayInfoList = getAcalService().getHolidaysForHolidayCalendar(hcInfo.getId(), createContextInfo());
                    for(HolidayInfo holidayInfo : holidayInfoList){
                        HolidayWrapper holiday = new HolidayWrapper(holidayInfo);
                        TypeInfo typeInfo = getAcalService().getHolidayType(holidayInfo.getTypeKey(), createContextInfo());
                        holiday.setTypeName(typeInfo.getName());
                        holidays.add(holiday);
                    }
                    inputLine.setHolidays(holidays);
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }else if (addLine instanceof AcalEventWrapper){
            AcalEventWrapper acalEventWrapper = (AcalEventWrapper)addLine;
            try {
                TypeInfo type = getTypeService().getType(acalEventWrapper.getEventTypeKey(), createContextInfo());
                acalEventWrapper.setEventTypeName(type.getName());
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (!CommonUtils.isValidDateRange(acalEventWrapper.getStartDate(),acalEventWrapper.getEndDate())){
               GlobalVariables.getMessageMap().putWarningForSectionId("acal-info-event",CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,acalEventWrapper.getEventTypeName(),CommonUtils.formatDate(acalEventWrapper.getStartDate()),CommonUtils.formatDate(acalEventWrapper.getEndDate()));
            }
        }else if (addLine instanceof KeyDateWrapper){
            KeyDateWrapper keydate = (KeyDateWrapper)addLine;
            try {
                if(StringUtils.isNotEmpty(keydate.getKeyDateType())) {
                    TypeInfo type = getTypeService().getType(keydate.getKeyDateType(),createContextInfo());
                    keydate.setKeyDateNameUI(type.getName());
                    keydate.setTypeInfo(type);
                    if (!CommonUtils.isValidDateRange(keydate.getStartDate(),keydate.getEndDate())){
                        GlobalVariables.getMessageMap().putWarningForSectionId("acal-term-keydates", CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,keydate.getKeyDateNameUI(),CommonUtils.formatDate(keydate.getStartDate()),CommonUtils.formatDate(keydate.getEndDate()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (addLine instanceof HolidayWrapper){
            HolidayWrapper holiday = (HolidayWrapper)addLine;
            try {
                holiday.setTypeName(getTypeInfo(holiday.getTypeKey()).getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (!CommonUtils.isValidDateRange(holiday.getStartDate(),holiday.getEndDate())){
                GlobalVariables.getMessageMap().putWarningForSectionId("KS-HolidayCalendar-HolidaySection", CalendarConstants.MessageKeys.ERROR_INVALID_DATE_RANGE,holiday.getTypeName(),CommonUtils.formatDate(holiday.getStartDate()),CommonUtils.formatDate(holiday.getEndDate()));
            }
        } else {
            super.processBeforeAddLine(view, collectionGroup, model, addLine);
        }
    }

    private boolean isParentTermExisting(String parentTermType, List<AcademicTermWrapper> termWrapperList, String lineName) {
       for (AcademicTermWrapper termWrapper : termWrapperList){
           String termType = termWrapper.getTermType();
           if (StringUtils.isBlank(termType)){
               termType = termWrapper.getTermInfo().getTypeKey();
           }
           if (parentTermType.equals(termType)){
               return true;
           }
       }
       if (lineName != null){
            GlobalVariables.getMessageMap().putErrorForSectionId(lineName,
               CalendarConstants.MessageKeys.ERROR_NO_PARENT_TERM_FOR_SUBTERM);
       }
       return false;
    }

    private AcademicTermWrapper getParentTermInForm(String parentTermType, List<AcademicTermWrapper> termWrapperList){
        for (AcademicTermWrapper termWrapper : termWrapperList){
            String termType = termWrapper.getTermType();
            if (StringUtils.isBlank(termType)){
                termType = termWrapper.getTermInfo().getTypeKey();
            }
            if (parentTermType.equals(termType)){
                return termWrapper;
            }
        }
        return null;
    }

    private void populateParentTermToSubterm(AcademicTermWrapper parentTermWrapper, AcademicTermWrapper newLine){
        
        List<KeyDatesGroupWrapper> newKeyDatesGroupWrappers = new ArrayList<KeyDatesGroupWrapper>();
        for(KeyDatesGroupWrapper keyDatesGroupWrapper : parentTermWrapper.getKeyDatesGroupWrappers()){
            KeyDatesGroupWrapper newKeyDatesGroup = 
                    new KeyDatesGroupWrapper(keyDatesGroupWrapper.getKeyDateGroupType(),
                                             keyDatesGroupWrapper.getKeyDateGroupNameUI());
            List<KeyDateWrapper> newKeyDates = newKeyDatesGroup.getKeydates();
            for(KeyDateWrapper keyDateWrapper: keyDatesGroupWrapper.getKeydates()){
                KeyDateWrapper newKeyDateWrapper = new KeyDateWrapper();
                newKeyDateWrapper.setKeyDateType(keyDateWrapper.getKeyDateType());
                newKeyDateWrapper.setKeyDateNameUI(keyDateWrapper.getKeyDateNameUI());
                newKeyDateWrapper.setAllDay(keyDateWrapper.isAllDay());
                newKeyDateWrapper.setDateRange(keyDateWrapper.isDateRange());
                newKeyDates.add(newKeyDateWrapper);
            }
            newKeyDatesGroupWrappers.add(newKeyDatesGroup);
        }
        newLine.setKeyDatesGroupWrappers(newKeyDatesGroupWrappers);
    }

    public AcademicCalendarService getAcalService() {
           if(acalService == null) {
             acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE, AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.acalService;
    }

    public TypeService getTypeService() {
        if(typeService == null) {
             typeService = (TypeService) GlobalResourceLoader.getService(new QName(TypeServiceConstants.NAMESPACE, TypeServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.typeService;
    }

    public AtpService getAtpService() {
        if(atpService == null) {
            atpService = (AtpService) GlobalResourceLoader.getService(new QName(AtpServiceConstants.NAMESPACE, AtpServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.atpService;
    }
    public TermCodeGenerator getTermCodeGenerator() {
        if(termCodeGenerator==null){
            //TODO: Change this to get term code generator from the service calls instead of directly (KSENROLL-7233).
            termCodeGenerator = new TermCodeGeneratorImpl(); //(TermCodeGenerator) GlobalResourceLoader.getService(new QName("http://student.kuali.org/wsdl/termcodegen","termCodeGenerator"));
        }
        return termCodeGenerator;
    }

    public void setTermCodeGenerator(TermCodeGenerator termCodeGenerator) {
        this.termCodeGenerator = termCodeGenerator;
    }

    protected String getAdminOrgNameById(String id){
        String adminOrgName = null;
        Map<String, String> allAcalOrgs = new HashMap<String, String>();
        allAcalOrgs.put("102", "Registrar's Office");
        allAcalOrgs.put("34", "Medical School");

        if(allAcalOrgs.containsKey(id)){
            adminOrgName = allAcalOrgs.get(id);
        }

        return adminOrgName;
    }

    /**
     * Generated a preview of the term code using the start date and type
     * Called by and AttributeQuery
     *
     * @param startDate - Start date of the term in either MM/dd/yyyy or MM-dd-yyyy format
     * @param typeKey - The type of term
     * @return The term code wrapped in a blank academic term wrapper
     */
    public AcademicTermWrapper termInfoAjaxQuery(String startDate, String typeKey){
        AcademicTermWrapper temp = new AcademicTermWrapper();
        String termCode;
        try{
            // Parse the date string
            Date date;
            try{
                // Date format MM/dd/yyyy
                date = DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.parse(startDate);
            }catch(IllegalArgumentException e){
                // Date format MM-dd-yyyy
                date = DateFormatters.DEFAULT_DATE_FORMATTER.parse(startDate);
            }

            // Find what the created term code would be from the term code generator
            TermInfo term = new TermInfo();
            term.setTypeKey(typeKey);
            term.setStartDate(date);
            termCode = this.getTermCodeGenerator().generateTermCode(term);

        }catch (Exception e){
            // If code can not be determined from start date and term type key return empty code
            LOG.error("Unable to find term code using start date = " + startDate +" and type key = "+ typeKey);
            termCode="";
        }

        // Set term info code to the found code, wrap it, and return
        temp.getTermInfo().setCode(termCode);
        return temp;
    }
}