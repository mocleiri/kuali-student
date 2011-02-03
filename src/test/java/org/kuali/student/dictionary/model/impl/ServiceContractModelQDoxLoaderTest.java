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
package org.kuali.student.dictionary.model.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceMethodParameter;
import org.kuali.student.dictionary.model.XmlType;
import static org.junit.Assert.*;

/**
 *
 * @author nwright
 */
public class ServiceContractModelQDoxLoaderTest
{

 public ServiceContractModelQDoxLoaderTest ()
 {
 }

 @BeforeClass
 public static void setUpClass () throws Exception
 {
 }

 @AfterClass
 public static void tearDownClass () throws Exception
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

 private static final String CORE_DIRECTORY = "C:/svn/maven-dictionary-generator/trunk/src/main/java/org/kuali/student/core";

  private ServiceContractModel getModel ()
 {
  List<String> sourceDirectories = new ArrayList ();
  sourceDirectories.add (CORE_DIRECTORY);
  ServiceContractModel instance = new ServiceContractModelQDoxLoader (
    sourceDirectories);
  return instance;

 }
 private String dump (ServiceMethod method)
 {
  StringBuilder bldr = new StringBuilder ();
  bldr.append (method.getName ());
  String comma = "";
  bldr.append ("(");
  for (ServiceMethodParameter param : method.getParameters ())
  {
   bldr.append (comma);
   comma = ", ";
   bldr.append (param.getType ());
   bldr.append (" ");
   bldr.append (param.getName ());
  }
  bldr.append (")");
  return bldr.toString ();
 }

 /**
  * Test of getServiceMethods method, of class ServiceContractModelQDoxLoader.
  */
 @Test
 public void testGetServiceMethods ()
 {
  System.out.println ("getServiceMethods");
  ServiceContractModel model = getModel ();
  List<ServiceMethod> result = model.getServiceMethods ();
  System.out.println ("Number of methods=" + result.size ());
  for (ServiceMethod method: result)
  {
   System.out.println (dump (method));
  }
  if (result.size () < 10)
  {
   fail ("too few: " + result.size ());
  }
 }

 /**
  * Test of getSourceNames method, of class ServiceContractModelQDoxLoader.
  */
 @Test
 public void testGetSourceNames ()
 {
  System.out.println ("getSourceNames");
  ServiceContractModel model = getModel ();
  List<String> expResult = new ArrayList ();
  expResult.add (CORE_DIRECTORY);
  List result = model.getSourceNames ();
  assertEquals (expResult, result);
 }

 /**
  * Test of getServices method, of class ServiceContractModelQDoxLoader.
  */
 @Test
 public void testGetServices ()
 {
  System.out.println ("getServices");
  ServiceContractModel model = getModel ();
  List<Service> result = model.getServices ();
  assertEquals (2, result.size ());
  for (Service service : result)
  {
   System.out.println (service.getKey () + " " + service.getName () + " "
                       + service.getVersion () + " " + service.getStatus ()
                       + " " + service.getComments ()
                       + " " + service.getUrl ());
  }
 }

 /**
  * Test of getXmlTypes method, of class ServiceContractModelQDoxLoader.
  */
 @Test
 public void testGetXmlTypes ()
 {
  System.out.println ("getXmlTypes");
  ServiceContractModel model = getModel ();
  List<XmlType> result = model.getXmlTypes ();
  if (result.size () < 10)
  {
   fail ("too few: " + result.size ());
  }
 }

 /**
  * Test of getMessageStructures method, of class ServiceContractModelQDoxLoader.
  */
 @Test
 public void testGetMessageStructures ()
 {
  System.out.println ("getMessageStructures");
  ServiceContractModel model = getModel ();
  List<MessageStructure> result = model.getMessageStructures ();
  if (result.size () < 10)
  {
   fail ("too few: " + result.size ());
  }
 }
}
