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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kuali.student.core.atp.service.AtpService;
import org.kuali.student.core.exceptions.DataValidationErrorException;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.exceptions.InvalidParameterException;
import org.kuali.student.core.exceptions.MissingParameterException;
import org.kuali.student.core.exceptions.OperationFailedException;
import org.kuali.student.core.exceptions.PermissionDeniedException;
import org.kuali.student.core.validation.dto.ValidationResultInfo;
import org.kuali.student.lum.program.dto.MajorDisciplineInfo;
import org.kuali.student.lum.program.service.ProgramService;

/**
 *
 * @author nwright
 */
public class ProgramLoader
{

 private ProgramService programService;
 private AtpService atpService;

 public AtpService getAtpService ()
 {
  return atpService;
 }

 public void setAtpService (AtpService atpService)
 {
  this.atpService = atpService;
 }

 public ProgramService getProgramService ()
 {
  return programService;
 }

 public void setProgramService (ProgramService programService)
 {
  this.programService = programService;
 }

 public ProgramLoader ()
 {
 }
 private List<Program> inputDataSource;

 public List<Program> getInputDataSource ()
 {
  return inputDataSource;
 }

 public void setInputDataSource (List<Program> inputDataSource)
 {
  this.inputDataSource = inputDataSource;
 }

 public List<ProgramLoadResult> update ()
 {
  List<ProgramLoadResult> results = new ProgramsToMajorDisciplineInfosConverter (inputDataSource, atpService).convert ();
  for (ProgramLoadResult result : results)
  {
   if (result.getStatus () != null)
   {
    continue;
   }
   MajorDisciplineInfo info = result.getMajorDisciplineInfo ();
   if (info.getId () != null)
   {
    try
    {
     MajorDisciplineInfo oldInfo =
                         programService.getMajorDiscipline (info.getId ());
     result.setStatus (ProgramLoadResult.Status.NOT_LOADED_ALREADY_EXISTS);
     continue;
    }
    catch (DoesNotExistException ex)
    {
     // ok we will create
    }
    catch (Exception ex)
    {
     result.setException (ex);
     result.setStatus (ProgramLoadResult.Status.EXCEPTION);
     continue;
    }
   }
   try
   {
    MajorDisciplineInfo createdInfo = programService.createMajorDiscipline (info);
    result.setMajorDisciplineInfo (createdInfo);
    result.setStatus (ProgramLoadResult.Status.MAJOR_DISCIPLINE_CREATED);
   }
   catch (DataValidationErrorException ex)
   {
    List<ValidationResultInfo> vris = null;
    try
    {
     vris = programService.validateMajorDiscipline ("SYSTEM", info);
    }
    catch (Exception ex1)
    {
     throw new RuntimeException (
       "Got an exception trying to get validation errors", ex1);
    }
    DataValidationErrorException dvex = new DataValidationErrorException (
      "got validation errors", vris, ex);
    result.setStatus (ProgramLoadResult.Status.VALIDATION_ERROR);
    result.setDataValidationErrorException (dvex);
   }
   catch (RuntimeException ex)
   {
    throw new RuntimeException (ex);
   }
   catch (Exception ex)
   {
    result.setStatus (ProgramLoadResult.Status.EXCEPTION);
    result.setException (ex);
   }
  }
  return results;
 }
}
