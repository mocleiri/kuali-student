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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.kuali.student.core.exceptions.DataValidationErrorException;
import org.kuali.student.core.validation.dto.ValidationResultInfo;
import org.kuali.student.lum.course.dto.CourseInfo;
import org.kuali.student.lum.course.service.CourseService;

/**
 *
 * @author nwright
 */
public class CreditCourseLoader
{

 private CourseService courseService;

 public CourseService getCourseService ()
 {
  return courseService;
 }

 public void setCourseService (CourseService courseService)
 {
  this.courseService = courseService;
 }

 public CreditCourseLoader ()
 {
 }
 private Iterator<CreditCourse> inputDataSource;

 public Iterator<CreditCourse> getInputDataSource ()
 {
  return inputDataSource;
 }

 public void setInputDataSource (Iterator<CreditCourse> inputDataSource)
 {
  this.inputDataSource = inputDataSource;
 }

 public List<CreditCourseLoadResult> update ()
 {
  List<CreditCourseLoadResult> results = new ArrayList (500);
  int row = 0;
  while (inputDataSource.hasNext ())
  {
   CreditCourseLoadResult result = new CreditCourseLoadResult ();
   results.add (result);
   CreditCourse cc = inputDataSource.next ();
   row ++;
   CourseInfo info = new CreditCourseToCourseInfoConverter (cc).convert ();
   result.setRow (row);
   result.setCreditCourse (cc);
   result.setCourseInfo (info);
   try
   {
    CourseInfo createdInfo = courseService.createCourse (info);
    result.setCourseInfo (createdInfo);
    result.setSuccess (true);
   }
   catch (DataValidationErrorException ex)
   {
    List<ValidationResultInfo> vris = null;
    try
    {
     vris = courseService.validateCourse ("SYSTEM", info);
    }
    catch (Exception ex1)
    {
     throw new RuntimeException (
       "Got an exception trying to get validation errors", ex1);
    }
    DataValidationErrorException dvex = new DataValidationErrorException (
      "got validation errors", vris, ex);
    result.setSuccess (false);
    result.setDataValidationErrorException (dvex);
   }
   catch (RuntimeException ex)
   {
    throw new RuntimeException (ex);
   }
   catch (Exception ex)
   {
    throw new RuntimeException (ex);
   }
  }
  return results;
 }

 public static CreditCourseInputModelFactory getInstance (String excelFile)
 {
  Properties props = new Properties ();
  props.putAll (CreditCourseInputModelFactory.getDefaultConfig ());
  props.put (CreditCourseInputModelFactory.EXCEL_FILES_DEFAULT_DIRECTORY_KEY,
             "src/main/"
             + CreditCourseInputModelFactory.RESOURCES_DIRECTORY);
  props.put (CreditCourseInputModelFactory.SERVICE_HOST_URL,
             "src/main/"
             + CreditCourseInputModelFactory.RESOURCES_DIRECTORY);
  System.out.println ("Current Directory=" + System.getProperty ("user.dir"));
  CreditCourseInputModelFactory factory = new CreditCourseInputModelFactory ();
  factory.setConfig (props);
  return factory;
 }
}
