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
package org.kuali.student.loader.program;

import org.kuali.student.loader.util.GetAtpHelper;
import java.util.ArrayList;
import java.util.Arrays;

import org.kuali.student.core.atp.service.AtpService;
import java.util.List;
import javax.xml.ws.soap.SOAPFaultException;
import org.kuali.student.core.atp.dto.AtpInfo;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.exceptions.InvalidParameterException;
import org.kuali.student.core.exceptions.MissingParameterException;
import org.kuali.student.core.exceptions.OperationFailedException;
import org.kuali.student.loader.util.AttributeInfoHelper;


import org.kuali.student.loader.util.RichTextInfoHelper;
import org.kuali.student.loader.util.TimeAmountInfoHelper;
import org.kuali.student.lum.lu.dto.CluInstructorInfo;
import org.kuali.student.lum.program.dto.ProgramVariationInfo;

/**
 *
 * @author nwright
 */
public class ProgramToProgramVariationInfoConverter
{

 private ProgramLoadResult result;
 private Program prog;
 private AtpService atpService;

 public ProgramToProgramVariationInfoConverter (ProgramLoadResult result,
                                                AtpService atpService)
 {
  this.result = result;
  this.prog = result.getProgram ();
  this.atpService = atpService;
 }
 public static final String ADMINISTRATION_ADMIN_ORG_TYPE =
                            "kuali.adminOrg.type.Administration";

 public ProgramVariationInfo convert ()
 {
  ProgramVariationInfo info = new ProgramVariationInfo ();
  info.setCode (prog.getCode ());
  info.setUniversityClassification (prog.getUniversityClassification ());
  info.setType (prog.getType ());
  info.setAttributes (
    new AttributeInfoHelper ().setValue (
    "isVariationRequired", prog.getIsVariationRequired (), info.getAttributes ()));
//  info.setOrgCoreProgram (prog.getCorePrograms ());
  // Convert this to an Id by looking it up in the rest file
//  info.setCredentialProgramId (prog.getCredentialProgram ());
  info.setId (prog.getId ());
  info.setShortTitle (prog.getShortTitle ());
  info.setLongTitle (prog.getLongTitle ());
  info.setTranscriptTitle (prog.getTranscriptTitle ());
//info.setAccreditingAgencies          (prog.getAccreditingAgencies ());
  if (prog.getCampusLocations () != null)
  {
   List<String> campuses =
                Arrays.asList (prog.getCampusLocations ().split (" "));
   info.setCampusLocations (campuses);
  }
  if (prog.getCatalogPublicationTargets () != null)
  {
   info.setCatalogPublicationTargets (Arrays.asList (prog.getCatalogPublicationTargets ().split (
     " ")));
  }
  info.setCip2000Code (formatCIP (prog.getCip2000Code ()));
  info.setCip2010Code (formatCIP (prog.getCip2010Code ()));
  info.setHegisCode (prog.getHegisCode ());
  info.setDescr (new RichTextInfoHelper ().getFromPlain (prog.getDescr ()));
  info.setCatalogDescr (new RichTextInfoHelper ().getFromPlain (
    prog.getCatalogDescr ()));
  info.setDiplomaTitle (prog.getDiplomaTitle ());
//  info.setUnitsContentOwner (prog.getUnitsContentOwner ());
//  info.setDivisionsContentOwner (prog.getDivisionsContentOwner ());
//  info.setUnitsDeployment (prog.getUnitsDeployment ());
//  info.setDivisionsDeployment (prog.getDivisionsDeployment ());
//  info.setUnitsFinancialControl (prog.getUnitsFinancialControl ());
//  info.setDivisionsFinancialControl (prog.getDivisionsFinancialControl ());
//  info.setUnitsFinancialResources (prog.getUnitsFinancialResources ());
//  info.setDivisionsFinancialResources (prog.getDivisionsFinancialResources ());
//  info.setUnitsStudentOversight (prog.getUnitsStudentOversight ());
//  info.setDivisionsStudentOversight (prog.getDivisionsStudentOversight ());
//  info.setInstitution (prog.getInstitution ());
  info.setStdDuration (new TimeAmountInfoHelper ().getWith1InTimeQuantity (
    prog.getStdDuration ()));
  info.setAttributes (
    new AttributeInfoHelper ().setValue ("durationNotes",
                                         prog.getDurationNotes (),
                                         info.getAttributes ()));
  if (prog.getStartTerm () != null)
  {
   info.setStartTerm (prog.getStartTerm ());
   AtpInfo atp = new GetAtpHelper (atpService).getAtp (info.getStartTerm ());
   if (atp == null)
   {
    result.setException (new RuntimeException ("startTerm was not found: "
                                               + prog.getStartTerm ()));
    result.setStatus (ProgramLoadResult.Status.VALIDATION_ERROR);
    return info;
   }
   info.setEffectiveDate (atp.getStartDate ());
  }
  if (prog.getEndTerm () != null)
  {
   info.setEndTerm (prog.getEndTerm ());
   AtpInfo atp = new GetAtpHelper (atpService).getAtp (info.getStartTerm ());
   if (atp == null)
   {
    result.setException (new RuntimeException ("endTerm was not found: "
                                               + prog.getEndTerm ()));
    result.setStatus (ProgramLoadResult.Status.VALIDATION_ERROR);
    return info;
   }
  }
  info.setState (prog.getState ());
  info.setAttributes (
    new AttributeInfoHelper ().setValue ("endInstAdmitTerm",
                                         prog.getEndInstAdmitTerm (),
                                         info.getAttributes ()));
  info.setEndProgramEntryTerm (prog.getEndProgramEntryTerm ());
  info.setIntensity (prog.getIntensity ());
  info.setAttributes (
    new AttributeInfoHelper ().setValue ("lastReviewDate",
                                         prog.getLastReviewDate (),
                                         info.getAttributes ()));
//  info.setNextReviewPeriod (prog.getNextReviewPeriod ());
//  info.setPublishedInstructors (toCluInstructors (
//    prog.getPublishedInstructors ()));
  info.setReferenceURL (prog.getReferenceURL ());
  if (prog.getResultOptions () != null)
  {
   info.setResultOptions (Arrays.asList (prog.getResultOptions ().split ("\n")));
  }
  info.setSelectiveEnrollmentCode (prog.getSelectiveEnrollmentCode ());
//  info.setVersionInfo (prog.getVersionInfo ());
//  info.setProgramLevel (prog.getProgramLevel ());
//  info.setProgramRequirements (prog.getProgramRequirements ());
  // TODO: load learning objectives
//  info.setLearningObjectives (prog.getLearningObjectives ());

  return info;
 }

 private List<CluInstructorInfo> toCluInstructors (String instructors)
 {
  if (instructors == null)
  {
   return null;
  }
  List<String> names = Arrays.asList (instructors.split ("\n"));
  List<CluInstructorInfo> clis = new ArrayList (names.size ());
  for (String name : names)
  {
   CluInstructorInfo cli = new CluInstructorInfo ();
   cli.setPersonInfoOverride (name);
   clis.add (cli);
  }
  return clis;
 }

 private String formatCIP (String cip)
 {
  if (cip == null)
  {
   return null;
  }
  if (cip.length () == 6)
  {
   return cip.substring (0, 2) + "." + cip.substring (2, 4) + "." + cip.substring (
     4);
  }
  if (cip.length () == 4)
  {
   return cip.substring (0, 2) + "." + cip.substring (2);
  }
  if (cip.length () == 2)
  {
   return cip;
  }
  return cip;
 }
}
