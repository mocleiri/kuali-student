/*
 * Copyright 2011 The Kuali Foundation
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
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceMethodParameter;
import org.kuali.student.dictionary.model.XmlType;
import org.kuali.student.dictionary.model.util.MessageStructureDumper;
import org.kuali.student.dictionary.model.validation.ServiceContractModelValidator;

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
 private static final String CORE_DIRECTORY =
                             "C:/svn/student/ks-core/ks-core-api/src/main/java";       
 private static final String COMMON_DIRECTORY =
                             "C:/svn/student/ks-common/ks-common-api/src/main/java";
 private static final String LUM_DIRECTORY =
                             "C:/svn/student/ks-lum/ks-lum-api/src/main/java";

  private static final String RICE_DIRECTORY =
 "C:/svn/rice/rice-release-1-0-2-1-br/api/src/main/java";
 private ServiceContractModel getModel ()
 {
  List<String> srcDirs = new ArrayList ();
  srcDirs.add (CORE_DIRECTORY);
//  srcDirs.add (COMMON_DIRECTORY);
//  srcDirs.add (LUM_DIRECTORY);
  ServiceContractModel instance = new ServiceContractModelQDoxLoader (srcDirs);
  instance = new ServiceContractModelCache (instance);
  validate (instance);
  return instance;
 }

  private ServiceContractModel getRiceModel ()
 {
  List<String> srcDirs = new ArrayList ();
  srcDirs.add (RICE_DIRECTORY);
  ServiceContractModel instance = new ServiceContractModelQDoxLoader (srcDirs);
  instance = new ServiceContractModelCache (instance);
  validate (instance);
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

 private void validate (ServiceContractModel model)
 {
  Collection<String> errors =
                     new ServiceContractModelValidator (model).validate ();
  if (errors.size () > 0)
  {
   StringBuffer buf = new StringBuffer ();
   buf.append (errors.size () + " errors found while validating the data.");
   int cnt = 0;
   for (String msg : errors)
   {
    cnt ++;
    buf.append ("\n");
    buf.append ("*error*" + cnt + ":" + msg);
   }

   fail (buf.toString ());
  }
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
  for (ServiceMethod method : result)
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
  for (Service service : result)
  {
   System.out.println (service.getKey () + " " + service.getName () + " "
                       + service.getVersion () + " " + service.getStatus ());
  }
  assertEquals (7, result.size ());
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
  for (XmlType xmlType : result)
  {
   System.out.println ("XmlType=" + xmlType.getName () + " " + xmlType.getPrimitive ());
  }
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
  for (MessageStructure ms : result)
  {
   if (ms.getName ().equalsIgnoreCase ("attributes"))
   {
    System.out.println ("MessageStructure=" + ms.getId () + " " + ms.getType ());
   }
  }
  if (result.size () < 10)
  {
   fail ("too few: " + result.size ());
  }
  for (MessageStructure ms : result)
  {
   new MessageStructureDumper (ms, System.out).dump ();
  }
 }
}
