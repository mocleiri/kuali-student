/*
 * Copyright 2010 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.loader.course;

import java.util.ArrayList;

import org.kuali.student.core.atp.dto.AtpInfo;
import org.kuali.student.core.atp.service.AtpService;
import org.kuali.student.core.organization.dto.OrgInfo;
import org.kuali.student.core.organization.service.OrganizationService;
import org.kuali.student.loader.util.AdminOrgInfoHelper;
import org.kuali.student.loader.util.AmountInfoHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.student.loader.util.AttributeInfoHelper;
import org.kuali.student.loader.util.GetAtpHelper;

import org.kuali.student.loader.util.GetOrgHelper;
import org.kuali.student.loader.util.MetaInfoHelper;
import org.kuali.student.loader.util.RichTextInfoHelper;
import org.kuali.student.loader.util.TimeAmountInfoHelper;
import org.kuali.student.lum.course.dto.ActivityInfo;
import org.kuali.student.lum.course.dto.CourseInfo;
import org.kuali.student.lum.course.dto.FormatInfo;
import org.kuali.student.lum.lrc.dto.ResultComponentInfo;
import org.kuali.student.lum.lu.dto.AdminOrgInfo;


/**
 *
 * @author nwright
 */
public class CreditCourseToCourseInfoConverter
{


 private CreditCourseLoadResult result;
 private CreditCourse cc;
 private Map<String, Object> helperService;
 
 public CreditCourseToCourseInfoConverter (CreditCourseLoadResult result, Map<String, Object> thisServiceMap)
 {

  this.result = result;
  this.cc = result.getCreditCourse ();
  this.helperService = thisServiceMap;
 }
 
 public static final String ADMINISTRATION_ADMIN_ORG_TYPE = "kuali.adminOrg.type.Administration";
 public CourseInfo convert ()
 {
  CourseInfo info = new CourseInfo ();
  info.setId (cc.getId ());
  AdminOrgInfo adminOrgInfo = new AdminOrgInfoHelper ().get (ADMINISTRATION_ADMIN_ORG_TYPE, cc.getAdministeringOrgName ());
  if (adminOrgInfo != null)
  {
   info.getUnitsContentOwner ().add (adminOrgInfo.getId ());
  }
  info.setDescr (new RichTextInfoHelper ().getFromPlain (cc.getDescr ()));
  // TODO: figure out why the code calculation doesn't get triggered if I leave it null
  info.setCode (cc.getSubjectArea () + cc.getCourseNumberSuffix ());
  info.setSubjectArea (cc.getSubjectArea ());
  info.setCourseNumberSuffix (cc.getCourseNumberSuffix ());
  info.setLevel (cc.getCourseNumberSuffix ().substring (0, 1) + "00");
  info.setTranscriptTitle (cc.getTranscriptTitle ());
  info.setCourseTitle (cc.getCourseTitle ());
  info.getTermsOffered ().addAll (convertOfferedAtpTypes (cc.getTermsOffered ()));
  info.setType ("kuali.lu.type.CreditCourse");
  List<String> campuses = new ArrayList ();
  campuses.add ("NO"); // north
  info.setCampusLocations (campuses);
  info.setOutOfClassHours (new AmountInfoHelper ().get ("1", "kuali.atp.duration.week"));
  info.setDuration (new TimeAmountInfoHelper ().get (1, "kuali.atp.duration.Semester"));
  info.setMetaInfo (new MetaInfoHelper ().get ());
  
  info.setAttributes (new AttributeInfoHelper ().setValue (
    "finalExamStatus", cc.getFinalExam (), info.getAttributes ()));
  info.setAttributes (new AttributeInfoHelper ().setValue (
    "finalExamRationale", cc.getFinalExamRationale (), info.getAttributes ()));
  info.setAttributes (new AttributeInfoHelper ().setValue (
    "audit", cc.isAudit (), info.getAttributes ()));
   info.setAttributes (new AttributeInfoHelper ().setValue (
    "passFail", cc.isPassFail (), info.getAttributes ()));
  info.setSpecialTopicsCourse(cc.isSpecialTopics());
  info.setPrimaryInstructor (null);
  
  // set adminOrgs
  if ( ! setAdminOrgs(info))
  {
	// if failed stop processing
	  return info;
  }
  
  //set gradingOptions
  setGradingOptions(info);
 
  //fixed & variable credits
  setCreditOptions(info);
  
  //set atpTerm related fields
  if ( ! setAtpTerms(info))
  {
   // if failed stop processing
   return info;
  }
  
  //set formats
  setFormats(info);

  return info;
 }

// public static final int ID = 0;
// public static final int SHORT_NAME = 1;
// public static final int LONG_NAME = 2;
//
//
// private String findOrgId (String name)
// {
//  if (name == null)
//  {
//   return null;
//  }
//  List<QueryParamValue> values = null;
//  String type = null;
//  QueryParamValue qpv = null;
//  type = "kuali.org.search.nameContains";
//  values = new ArrayList ();
//  qpv = new QueryParamValueBean ();
//  values.add (qpv);
//  qpv.setKey ("kuali.org.queryParam.searchName");
//  qpv.setValue (name);
//  qpv = new QueryParamValueBean ();
//  values.add (qpv);
//  qpv.setKey ("kuali.org.queryParam.types");
//  qpv.setValue ("kuali.org.Department");
//  qpv = new QueryParamValueBean ();
//  values.add (qpv);
//  qpv.setKey ("kuali.org.queryParam.types");
//  qpv.setValue ("kuali.org.Office");
//  qpv = new QueryParamValueBean ();
//  values.add (qpv);
//  qpv.setKey ("kuali.org.queryParam.types");
//  qpv.setValue ("kuali.org.Section");
//  List<Result> results = null;
//  try
//  {
//   results = context.getSearchService ().searchForResults (type, values);
//  }
//  catch (Exception ex)
//  {
//   throw new IllegalArgumentException (ex);
//  }
//  if (results.size () == 0)
//  {
//   log.warning ("could not find a primary admin organization  for course "
//                                       + course.getCode () + " with org name="
//                                       + name);
//   return null;
//  }
//  if (results.size () == 1)
//  {
//   return results.get (0).getResultCells ().get (ID).getValue ();
//  }
//  StringBuilder builder = new StringBuilder ();
//  builder.append (results.size ());
//  builder.append (" organizations match the supplied criteria ");
//  builder.append (name);
//  for (Result result: results)
//  {
//   builder.append ("\n");
//   builder.append ("shortName=");
//   builder.append (result.getResultCells ().get (SHORT_NAME).getValue ());
//   builder.append (" longName=");
//   builder.append (result.getResultCells ().get (LONG_NAME).getValue ());
//  }
//  log.warning ("Skipping could not find primary admin organization for course "
//                                      + course.getCode () + " because " + builder.
//      toString ());
//  return null;
// }
 private void setGradingOptions(CourseInfo info){
	 if(cc.getGradingOptions() != null && !cc.getGradingOptions().isEmpty()){
		 List<String> gradingOptions = new ArrayList ();

		 String[] options = cc.getGradingOptions().split(" ");
		 for (int i = 0; i < options.length; i++){
			 gradingOptions.add(options[i]);
		 }	
		 
		 info.setGradingOptions (gradingOptions);
	 }
 }
 
 private void setCreditOptions(CourseInfo info){
	if(cc.getMinCredits() != null && !cc.getMinCredits().isEmpty() && cc.getMaxCredits() != null && !cc.getMaxCredits().isEmpty()){
	  List<ResultComponentInfo> creditOptions = new ArrayList<ResultComponentInfo> ();
	  ResultComponentInfo rci = new ResultComponentInfo();
	  Map<String, String> rciAttributes = new HashMap<String, String>();
	  if (cc.getMinCredits().equals(cc.getMaxCredits())){ 
		  rci.setType("kuali.resultComponentType.credit.degree.fixed");
		  rciAttributes.put("fixedCreditValue", cc.getMaxCredits());
	  }
	  else{
		  rci.setType("kuali.resultComponentType.credit.degree.range");
		  rciAttributes.put("minCreditValue", cc.getMinCredits());
		  rciAttributes.put("maxCreditValue", cc.getMaxCredits());
	  }
		  
	  rci.setAttributes(rciAttributes);
	  creditOptions.add(rci);
	  info.setCreditOptions(creditOptions);
	}	 
 }
 
 private boolean setAtpTerms(CourseInfo info){
	 if(helperService != null && !helperService.isEmpty()){
		 AtpService atpService = (AtpService)helperService.get("atp");
		 if (atpService != null){
			 if(cc.getStartTerm()!= null && !cc.getStartTerm().isEmpty()){
				 info.setStartTerm(cc.getStartTerm());

				 AtpInfo atpStart = new GetAtpHelper (atpService).getAtp(cc.getStartTerm());
			     if (atpStart == null) {
				      result.setException (new RuntimeException ("startTerm was not found: "
				                                                + cc.getStartTerm ()));
				      result.setStatus (CreditCourseLoadResult.Status.VALIDATION_ERROR);
				      return false;
			     }
				 info.setEffectiveDate(atpStart.getStartDate());
			 }
	 
			 if(cc.getEndTerm()!= null && !cc.getEndTerm().isEmpty()){
				 info.setEndTerm(cc.getEndTerm());
	
				 AtpInfo atpEnd = new GetAtpHelper (atpService).getAtp(cc.getEndTerm());
			    if (atpEnd == null){
			      result.setException (new RuntimeException ("startTerm was not found: "
			                                                + cc.getStartTerm ()));
			      result.setStatus (CreditCourseLoadResult.Status.VALIDATION_ERROR);
			      return false;
			    }
			    
				info.setExpirationDate(atpEnd.getEndDate());
				info.setState ("Retired");
				return true;
			 }
			
		 info.setState ("Active");
		 return true;
		 }
	 }
	 
	 return false;
 }

 private boolean setAdminOrgs(CourseInfo info) {
	 if(helperService != null && !helperService.isEmpty()){
		 OrganizationService orgService = (OrganizationService)helperService.get("org");
		 if (orgService != null){
			 if(cc.getAdministeringOrg() != null && !cc.getAdministeringOrg().isEmpty()){
				 OrgInfo adminOrg = new GetOrgHelper (orgService).getOrg(cc.getAdministeringOrg());
			     if (adminOrg == null) {
				      result.setException (new RuntimeException ("AdministeringOrg was not found: "
				                                                + cc.getAdministeringOrg ()));
				      result.setStatus (CreditCourseLoadResult.Status.VALIDATION_ERROR);
				      return false;
			     }
			     info.getUnitsContentOwner ().add (cc.getAdministeringOrg ());
			     return true;
			 }			 
		 }
	 }
	 
	 return false;
 }
 
 private void setFormats(CourseInfo info) {
	 List<FormatInfo> formats = new ArrayList<FormatInfo>();
	 
	 //formatActivities
	 String formatActivity = cc.getFormatActivities();
	 if(formatActivity != null && !formatActivity.isEmpty()){
		 FormatInfo formatInfo = new FormatInfo();
		 formatInfo.setType("kuali.lu.type.CreditCourseFormatShell");
		 formatInfo.setState(info.getState());
		 
		 List<ActivityInfo> activities = new ArrayList<ActivityInfo>();
		 
		 if(formatActivity.equals("Lec") || formatActivity.equals("LecLab"))
			 setActivityInfo(activities, "kuali.lu.type.activity.Lecture", cc.getLecHr(), info.getState());			 
		 
		 if(formatActivity.equals("Lab") || formatActivity.equals("LecLab"))
			 setActivityInfo(activities, "kuali.lu.type.activity.Lab", cc.getLabHr(), info.getState());
		 
		 formatInfo.setActivities(activities);
		 formats.add(formatInfo);
	 }		 
		
	 if(!formats.isEmpty())
		 info.setFormats(formats);
 }
 
 private void setActivityInfo(List<ActivityInfo> activities, String activityType, String contactHour, String state){
	 ActivityInfo activityInfo = new ActivityInfo();
	 activityInfo.setActivityType(activityType);
	 activityInfo.setState(state);
	 activityInfo.setDuration(new TimeAmountInfoHelper ().get (1, "kuali.atp.duration.Semester"));
	 activityInfo.setContactHours(new AmountInfoHelper ().get (contactHour, "kuali.atp.duration.week"));

	 activities.add(activityInfo);
 }
 
 private List<String> convertOfferedAtpTypes (String offeredAtpTypes)
 {
  if (offeredAtpTypes == null)
  {
   return null;
  }
  return Arrays.asList (offeredAtpTypes.split (" "));
 }

}
