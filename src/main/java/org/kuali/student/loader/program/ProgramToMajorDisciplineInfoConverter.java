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

import java.util.ArrayList;
import java.util.Arrays;

import org.kuali.student.core.atp.service.AtpService;
import java.util.List;
import org.kuali.student.loader.util.AttributeInfoHelper;


import org.kuali.student.loader.util.RichTextInfoHelper;
import org.kuali.student.loader.util.TimeAmountInfoHelper;
import org.kuali.student.lum.lu.dto.CluInstructorInfo;
import org.kuali.student.lum.program.dto.MajorDisciplineInfo;

/**
 *
 * @author nwright
 */
public class ProgramToMajorDisciplineInfoConverter
{

 private Program prog;
 private AtpService atpService;

 public ProgramToMajorDisciplineInfoConverter (Program prog)
 {
  this.prog = prog;
 }

 public ProgramToMajorDisciplineInfoConverter (Program prog,
                                               AtpService thisService)
 {
  this.prog = prog;
  this.atpService = thisService;
 }
 public static final String ADMINISTRATION_ADMIN_ORG_TYPE =
                            "kuali.adminOrg.type.Administration";

 public MajorDisciplineInfo convert ()
 {
  MajorDisciplineInfo info = new MajorDisciplineInfo ();
  info.setCode (prog.getCode ());
  info.setUniversityClassification (prog.getUniversityClassification ());
  info.setType (prog.getType ());
  info.setAttributes (new AttributeInfoHelper ().setValue (
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
  info.setStartTerm (prog.getStartTerm ());
  info.setEndTerm (prog.getEndTerm ());
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
  info.setNextReviewPeriod (prog.getNextReviewPeriod ());
  info.setPublishedInstructors (toCluInstructors (
    prog.getPublishedInstructors ()));
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




  info.setDescr (new RichTextInfoHelper ().getFromPlain (prog.getDescr ()));
  info.setTranscriptTitle (prog.getTranscriptTitle ());
  info.setShortTitle (prog.getShortTitle ());
  info.setLongTitle (prog.getLongTitle ());
//  info.getTermsOffered ().addAll (convertOfferedAtpTypes (prog.getTermsOffered ()));


  // TODO: make this a lookup via the OrgService 
//  info.getUnitsContentOwner ().add (prog.getAdministeringOrg ());

//  info.setOutOfClassHours (new AmountInfoHelper ().get ("1", "kuali.atp.duration.Semester"));
//  info.setDuration (new TimeAmountInfoHelper ().get (1, "kuali.atp.duration.Semester"));
//  info.setMetaInfo (new MetaInfoHelper ().get ());

//  info.getAttributes ().put ("finalExamStatus", prog.getFinalExam ());
//  info.getAttributes ().put ("finalExamRationale", prog.getFinalExamRationale ());
//  info.setPrimaryInstructor (new CluInstructorInfo ());
//  info.getPrimaryInstructor ().setPersonInfoOverride ("Staff");
//  List<String> gradingOptions = new ArrayList ();
//  gradingOptions.add ("kuali.resultComponent.grade.letter");
//  info.setGradingOptions (gradingOptions);

  //fixed & variable credits
//  setCreditOptions(info);

  //set atpTerm related fields
//  setAtpTerms(info);

  //set formats
//  setFormats(info);

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
