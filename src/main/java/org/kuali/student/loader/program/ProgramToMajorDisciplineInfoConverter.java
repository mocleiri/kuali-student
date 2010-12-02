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

import org.kuali.student.core.atp.service.AtpService;
import java.util.List;


import org.kuali.student.loader.util.RichTextInfoHelper;
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
  info.setId (prog.getId ());
  info.setType (prog.getType ());
//  AdminOrgInfo adminOrgInfo = new AdminOrgInfoHelper ().get (ADMINISTRATION_ADMIN_ORG_TYPE, prog.getAdministeringOrgName ());
//  if (adminOrgInfo != null)
//  {
//   info.getUnitsContentOwner ().add (adminOrgInfo.getId ());
//  }
  info.setDescr (new RichTextInfoHelper ().getFromPlain (prog.getDescr ()));
  info.setCode (prog.getCode ());
  info.setTranscriptTitle (prog.getTranscriptTitle ());
  info.setShortTitle (prog.getShortTitle ());
  info.setLongTitle (prog.getLongTitle ());
//  info.getTermsOffered ().addAll (convertOfferedAtpTypes (prog.getTermsOffered ()));
//  List<String> campuses = Arrays.asList (prog.getCampusLocation ().split (" "));
//  campuses.add ("NO"); // north
//  info.setCampusLocations (campuses);

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
}
