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
import java.util.List;
import org.kuali.student.loader.util.ExcelLoaderHelper;
import org.kuali.student.spreadsheet.SpreadsheetReader;
import org.kuali.student.spreadsheet.WorksheetNotFoundException;
import org.kuali.student.spreadsheet.WorksheetReader;

/**
 *
 * @author nwright
 */
public class ProgramInputModelExcelImpl implements ProgramInputModel
{

 private SpreadsheetReader reader;

 public ProgramInputModelExcelImpl (SpreadsheetReader reader)
 {
  this.reader = reader;
 }

 @Override
 public List<Program> getPrograms ()
 {
  WorksheetReader worksheetReader;
  try
  {
   worksheetReader = reader.getWorksheetReader ("Programs");
  }
  catch (WorksheetNotFoundException ex)
  {
   throw new IllegalArgumentException (ex);
  }
  ExcelLoaderHelper helper = new ExcelLoaderHelper (worksheetReader);
  helper.setMaxStringSize (500);
  List<Program> list = new ArrayList (worksheetReader.getEstimatedRows ());
  int row = 0;
  while (worksheetReader.next ())
  {
   Program prog = new Program ();
   prog.setId (helper.getFixup ("id"));
   prog.setCode (helper.getFixup ("code"));
   if (prog.getCode () == null)
   {
    continue;
   }
   prog.setType (helper.getFixup ("type"));
   prog.setVariationOf (helper.getFixup ("variationOf"));
   list.add (prog);
   row ++;
//   prog.setSubjectArea (helper.getFixup ("subjectArea"));
//   prog.setProgramNumberSuffix (helper.getFixup ("programNumberSuffix"));
//   prog.setVariation (helper.getFixup ("variation"));
//   prog.setAdministeringOrgName (helper.getFixup ("AdministeringOrgName"));
//   prog.setAdministeringOrg (helper.getFixup ("AdministeringOrgName"));
   prog.setTranscriptTitle (helper.getFixup ("TranscriptTitle"));
   prog.setShortTitle (helper.getFixup ("ShortTitle"));
   prog.setDescr (helper.getFixup ("Descr"));
//   prog.setMinCredits (helper.getFixup ("minCredits"));
//   prog.setMaxCredits (helper.getFixup ("maxCredits"));
//   prog.setRestrictions (helper.getFixup ("restrictions"));
//   prog.setPrereq (helper.getFixup ("prereq"));
//   prog.setPrereqNL (helper.getFixup ("prereqNL"));
//   prog.setCoreq (helper.getFixup ("coreq"));
//   prog.setCoreqNL (helper.getFixup ("coreqNL"));
//   prog.setEquivalencies (helper.getFixup ("equivalencies"));
//   prog.setGradingOptions (helper.getFixup ("gradingOptions"));
//   prog.setTermsOffered (helper.getFixup ("TermsOffered"));
//   prog.setRequirementsMet (helper.getFixup ("requirementsMet"));
//   prog.setLearningObjectives (helper.getFixup ("learningObjectives"));
//   prog.setFinalExam (helper.getFixup ("FinalExam"));
//   prog.setFinalExamRationale (helper.getFixup ("FinalExamRationale"));
//   prog.setStartTerm(helper.getFixup("startTerm"));
//   prog.setEndTerm(helper.getFixup("endTerm"));
//   prog.setFormatActivities(helper.getFixup("formatActivities"));
//   prog.setLecHr(helper.getFixup("lecHr"));
//   prog.setLabHr(helper.getFixup("labHr"));
  }
  return list;
 }
}
