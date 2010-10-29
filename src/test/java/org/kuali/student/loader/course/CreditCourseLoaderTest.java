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

import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.lum.course.service.CourseService;
import static org.junit.Assert.*;

/**
 *
 * @author nwright
 */
public class CreditCourseLoaderTest
{

 private static CourseService courseService;

 public CreditCourseLoaderTest ()
 {
 }

 @BeforeClass
 public static void setUpClass ()
   throws Exception
 {
  CourseServiceFactory factory = new CourseServiceFactory ();
  factory.setHostUrl (CourseServiceFactory.LOCAL_HOST_EMBEDDED_URL);
  courseService = factory.getCourseService ();
 }

 @AfterClass
 public static void tearDownClass ()
   throws Exception
 {
 }

 @Before
 public void setUp ()
 {
 }

 @After
 public void tearDown ()
 {
 }

 /**
  * Test of load method, of class OrgLoader.
  */
 @Test
 public void testLoadCreditCourses ()
   throws Exception
 {
  System.out.println (new Date () + " load credit courses");

  CreditCourseLoader ccLoader = new CreditCourseLoader ();
  ccLoader.setCourseService (courseService);
  CreditCourseInputModel ccModel = CreditCourseInputModelFactoryTest.getInstance ().
    getModel ();

  System.out.println (new Date () + " getting credit courses...");
  List<CreditCourse> creditCourses = ccModel.getCreditCourses ();
//  List<CreditCourse> list = creditCourses.subList (400, 500);
  List<CreditCourse> list = creditCourses;
  System.out.println (new Date () + " loading " + list.size ()
                      + " credit courses");

  ccLoader.setInputDataSource (list);
//  ccLoader.setInputDataSource (creditCourses);
  List<CreditCourseLoadResult> results = ccLoader.update ();

  // output good results
  for (CreditCourseLoadResult result : results)
  {
   switch (result.getStatus ())
   {
    case CREATED:
    case COURSE_VARIATION_PROCESSED_WITH_MAIN_COURSE:
     System.out.println (result.getCourseInfo ().getCode ()
                         + " " + result.getStatus () + " id = "
                         + result.getCourseInfo ().getId ());
   }
  }

  // output summary
  for (CreditCourseLoadResult result : results)
  {
   result.getStatus ().increment ();
  }
  System.out.println (list.size () + " credit courses");
  for (CreditCourseLoadResult.Status status :
       CreditCourseLoadResult.Status.values ())
  {
   System.out.println ("Total with status of " + status + " = "
                       + status.getCount ());
  }

  // output errors
  for (CreditCourseLoadResult result : results)
  {
   switch (result.getStatus ())
   {
    case CREATED:
    case COURSE_VARIATION_PROCESSED_WITH_MAIN_COURSE:
     break;
    default:
     System.out.println (result);
   }
  }
  if (CreditCourseLoadResult.Status.VALIDATION_ERROR.getCount () > 0)
  {
   throw new RuntimeException (CreditCourseLoadResult.Status.VALIDATION_ERROR.getCount ()
                               + " records failed to load");
  }
  if (CreditCourseLoadResult.Status.EXCEPTION.getCount () > 0)
  {
   throw new RuntimeException (CreditCourseLoadResult.Status.EXCEPTION.getCount ()
                               + " records failed to load");
  }
 }
}
