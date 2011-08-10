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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.student.common.exceptions.DataValidationErrorException;
import org.kuali.student.common.exceptions.DoesNotExistException;
import org.kuali.student.common.validation.dto.ValidationResultInfo;
import org.kuali.student.lum.program.dto.CoreProgramInfo;
import org.kuali.student.lum.program.dto.CredentialProgramInfo;
import org.kuali.student.lum.program.dto.MajorDisciplineInfo;
import org.kuali.student.lum.program.service.ProgramService;

/**
 *
 * @author nwright
 */
public class ProgramLoader
{

 private ProgramService programService;
 private Map<String, Object> helperService = new HashMap<String, Object>();

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
  List<ProgramLoadResult> results = new ProgramsToMajorDisciplineInfosConverter (inputDataSource, helperService).convert ();
  for (ProgramLoadResult result : results)
  {
   if (result.getStatus () != null)
   {
    continue;
   }
   
   if (result.getMajorDisciplineInfo () != null){
	   if(processMajor(result)) continue;
   }
   
   if(result.getCoreProgramInfo() != null){
	   if(processCore(result)) continue;
   }
   
   if(result.getCredentialProgramInfo() != null){
	   if(processBacc(result)) continue;
   }
   
/*   MajorDisciplineInfo info = result.getMajorDisciplineInfo ();
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
    System.out.println ("state before create=" + info.getState ());
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
   }*/
  }
  return results;
 }
 private boolean processMajor(ProgramLoadResult result){	 
	 boolean skip = false;
	   MajorDisciplineInfo info = result.getMajorDisciplineInfo ();
	   if(info != null){
		   if (info.getId () != null)
		   {
		    try
		    {
		     MajorDisciplineInfo oldInfo =
		                         programService.getMajorDiscipline (info.getId ());
		     result.setStatus (ProgramLoadResult.Status.NOT_LOADED_ALREADY_EXISTS);
		     skip = true;
		    }
		    catch (DoesNotExistException ex)
		    {
		     // ok we will create
		    }
		    catch (Exception ex)
		    {
		     result.setException (ex);
		     result.setStatus (ProgramLoadResult.Status.EXCEPTION);
		     skip = true;
		    }
		   }
	   
		   try
		   {
		    System.out.println ("state before create=" + info.getState ());
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
	   
	   return skip;
 }
	 
 private boolean processCore(ProgramLoadResult result){
	 boolean skip = false;
	 
	 CoreProgramInfo info = result.getCoreProgramInfo();
	 if(info != null){
		 if (info.getId () != null){
		    try
		    {
		    	CoreProgramInfo oldInfo = programService.getCoreProgram(info.getId ());
	    		result.setStatus (ProgramLoadResult.Status.NOT_LOADED_ALREADY_EXISTS);
	    		return true;
		    }
		    catch (DoesNotExistException ex)
		    {
		     // ok we will create
		    }
		    catch (Exception ex)
		    {
		     result.setException (ex);
		     result.setStatus (ProgramLoadResult.Status.EXCEPTION);
		     return true;
		    }
		   }	

		   try
		   {
		    System.out.println ("state before create=" + info.getState ());
		    CoreProgramInfo createdInfo = programService.createCoreProgram(info);
		    result.setCoreProgramInfo (createdInfo);
		    result.setStatus (ProgramLoadResult.Status.CORE_PROGRAM_CREATED);
		   }catch (DataValidationErrorException ex){
			    List<ValidationResultInfo> vris = null;
			    try
			    {
			     vris = programService.validateCoreProgram("SYSTEM", info);
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
		   }catch (RuntimeException ex){
			    throw new RuntimeException (ex);
		   }catch (Exception ex){
			    result.setStatus (ProgramLoadResult.Status.EXCEPTION);
			    result.setException (ex);
		   }
		 
	 }
	 
	 return skip;
 }
 
 private boolean processBacc(ProgramLoadResult result){
	 boolean skip = false;
	 
	 CredentialProgramInfo info = result.getCredentialProgramInfo();
	 if(info != null){
		 if (info.getId () != null){
		    try
		    {
		    	CredentialProgramInfo oldInfo = programService.getCredentialProgram(info.getId ());
	    		result.setStatus (ProgramLoadResult.Status.NOT_LOADED_ALREADY_EXISTS);
	    		return true;
		    }
		    catch (DoesNotExistException ex)
		    {
		     // ok we will create
		    }
		    catch (Exception ex)
		    {
		     result.setException (ex);
		     result.setStatus (ProgramLoadResult.Status.EXCEPTION);
		     return true;
		    }
		   }	

		   try
		   {
		    System.out.println ("state before create=" + info.getState ());
		    CredentialProgramInfo createdInfo = programService.createCredentialProgram(info);
		    result.setCredentialProgramInfo (createdInfo);
		    result.setStatus (ProgramLoadResult.Status.CREDENTIAL_PROGRAM_CREATED);
		   }catch (DataValidationErrorException ex){
			    List<ValidationResultInfo> vris = null;
			    try
			    {
			     vris = programService.validateCredentialProgram("SYSTEM", info);
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
		   }catch (RuntimeException ex){
			    throw new RuntimeException (ex);
		   }catch (Exception ex){
			    result.setStatus (ProgramLoadResult.Status.EXCEPTION);
			    result.setException (ex);
		   }
		 
	 }	 
	 return skip;
 }
 public void setHelperService(Map<String, Object> helperService) {
		this.helperService = helperService;
	}

	public Map<String, Object> getHelperService() {
		return helperService;
	}
}
