/*
 * Copyright 2009 The Kuali Foundation
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

import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author nwright
 */
public class CreditCourseLoaderFromCommandLine
{

 /**
  * @param args the command line arguments
  */
 public static void main (String[] args)
 {
  CreditCourseLoaderFromCommandLine instance =
                                    new CreditCourseLoaderFromCommandLine ();
  instance.execute (args);

 }

 private void displayVersion ()
 {
  displayVersion (System.out);
 }

 public void displayVersion (PrintStream out)
 {
  //TODO: figure out how to get the version from the Maven property
  out.println ("Kuali Student Credit Course Loader: Version 0.5");
  out.println ("                     Built on September 9, 2010");
 }

 private void displayParameters (String inFile, String outFile)
 {
  displayParameters (System.out, inFile, outFile);
 }

 public void displayParameters (PrintStream out, String inFile, String hostURL)
 {
  out.println ("Reading: " + inFile);
  out.println ("Updating: " + hostURL);
 }

 private void displayUsage ()
 {
  displayUsage (System.out);
 }

 public void displayUsage (PrintStream out)
 {
  out.println (
    "Usage: java -jar kuali-credit-course-loader.jar <inputExcel> <hostUrl>");
  out.println (
    "\t@param inputExcel the fully qualified file name for the input excel file");
  out.println ("\t@param hostUrl the fully qualified url of the serice");
  out.println (
    "ex: java -jar kuali-credit-course-loader.jar courses.xls http://localhost:9393/ks-embedded-dev");
 }

 private void execute (String[] args)
 {
  displayVersion ();
  if (args == null)
  {
   displayUsage ();
   throw new RuntimeException ("args is null");
  }
  if (args.length == 0)
  {
   displayUsage ();
   throw new RuntimeException ("no args specified");
  }
  if (args.length == 1)
  {
   displayUsage ();
   throw new RuntimeException ("no host url specified");
  }
  String in = args[0];
  String host = args[1];
  generate (in, host);
 }

 protected void generate (String inFile, String hostUrl)
 {
  displayParameters (inFile, hostUrl);
  Properties cfg = new Properties ();
  cfg.put (CreditCourseInputModelFactory.EXCEL_FILES_KEY + "1", inFile);
  CreditCourseInputModelFactory factory = new CreditCourseInputModelFactory ();
  factory.setConfig (cfg);
  CreditCourseInputModel ccModel = factory.getModel ();
  CreditCourseLoader ccLoader = new CreditCourseLoader ();
  CourseServiceFactory servFactory = new CourseServiceFactory ();
  servFactory.setHostUrl (hostUrl);
  ccLoader.setCourseService (servFactory.getCourseService ());

  System.out.println (new Date () + " getting credit courses...");
  List<CreditCourse> creditCourses = ccModel.getCreditCourses ();

  System.out.println (new Date () + " loading " + creditCourses.size ()
                      + " credit courses");
//  List<CreditCourse> list = creditCourses.subList (0, 10);
  List<CreditCourse> list = creditCourses;
  ccLoader.setInputDataSource (list);
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
    case VALIDATION_ERROR:
    case EXCEPTION:
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
