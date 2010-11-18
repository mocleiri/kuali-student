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
package org.kuali.student.loader.standardizedtest;

import java.util.List;
import org.kuali.student.core.exceptions.DataValidationErrorException;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.exceptions.OperationFailedException;
import org.kuali.student.core.validation.dto.ValidationResultInfo;
import org.kuali.student.lum.lu.dto.CluInfo;
import org.kuali.student.lum.lu.service.LuService;

/**
 *
 * @author nwright
 */
public class StandardizedTestLoader
{

 private LuService luService;

 public LuService getLuService ()
 {
  return luService;
 }

 public void setLuService (LuService luService)
 {
  this.luService = luService;
 }

 public StandardizedTestLoader ()
 {
 }
 private List<StandardizedTest> inputDataSource;

 public List<StandardizedTest> getInputDataSource ()
 {
  return inputDataSource;
 }

 public void setInputDataSource (List<StandardizedTest> inputDataSource)
 {
  this.inputDataSource = inputDataSource;
 }

 public List<StandardizedTestLoadResult> update ()
 {
  List<StandardizedTestLoadResult> results = new StandardizedTestsToCluInfosConverter (
    inputDataSource).convert ();
  for (StandardizedTestLoadResult result : results)
  {
   if (result.getStatus () != null)
   {
    continue;
   }
   CluInfo info = result.getCluInfo ();
   CluInfo oldClu = null;
   try
   {
    oldClu = luService.getClu (info.getId ());
   }
   catch (OperationFailedException ex)
   {
    // ignore for now because this often means the id does not exist until exceptions get fixed
   }
   catch (DoesNotExistException ex)
   {
    // ignore because that is what we are trying to find out
   }
   catch (Exception ex)
   {
    result.setStatus (StandardizedTestLoadResult.Status.EXCEPTION);
    result.setException (ex);
    continue;
   }
   if (oldClu != null)
   {
    result.setStatus (
      StandardizedTestLoadResult.Status.NOT_PROCESSED_ALREADY_EXISTS);
    continue;
   }
   try
   {
    CluInfo createdInfo = luService.createClu (info.getType (), info);
    result.setCluInfo (createdInfo);
    result.setStatus (StandardizedTestLoadResult.Status.CREATED);
    continue;
   }
   catch (DataValidationErrorException ex)
   {
    List<ValidationResultInfo> vris = null;
    try
    {
     vris = luService.validateClu ("SYSTEM", info);
    }
    catch (Exception ex1)
    {
     throw new RuntimeException (
       "Got an exception trying to get validation errors", ex1);
    }
    DataValidationErrorException dvex = new DataValidationErrorException (
      "got validation errors", vris, ex);
    result.setStatus (StandardizedTestLoadResult.Status.VALIDATION_ERROR);
    result.setDataValidationErrorException (dvex);
    continue;
   }
   catch (Exception ex)
   {
    result.setStatus (StandardizedTestLoadResult.Status.EXCEPTION);
    result.setException (ex);
    continue;
   }

  }
  return results;
 }
}
