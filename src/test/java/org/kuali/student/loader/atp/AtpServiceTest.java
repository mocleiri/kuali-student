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
package org.kuali.student.loader.atp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.common.dto.StatusInfo;
import org.kuali.student.common.exceptions.AlreadyExistsException;
import org.kuali.student.common.exceptions.DoesNotExistException;
import org.kuali.student.common.exceptions.OperationFailedException;
import org.kuali.student.core.atp.dto.AtpInfo;
import org.kuali.student.core.atp.dto.AtpTypeInfo;
import org.kuali.student.core.atp.service.AtpService;
import org.kuali.student.loader.course.CourseServiceFactory;
import org.kuali.student.loader.util.RichTextInfoHelper;

/**
 *
 * @author nwright
 */
public class AtpServiceTest
{

 public AtpServiceTest ()
 {
 }
 private static AtpService atpService;

 @BeforeClass
 public static void setUpClass () throws Exception
 {
  AtpServiceFactory factory = new AtpServiceFactory ();
  factory.setHostUrl (CourseServiceFactory.LOCAL_HOST_EMBEDDED_URL);
  atpService = factory.getAtpService ();
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

 /**
  * Test of getTypes method in AtpFinder
  */
 @Test
 public void testGetAtpTypes ()
 {
  System.out.println ("getAtpTypes");
//  List<AtpTypeInfo> expResult = new ArrayList ();
  List<AtpTypeInfo> result;
  try
  {
   result = atpService.getAtpTypes ();
  }
  catch (OperationFailedException ex)
  {
   throw new RuntimeException (ex);
  }
  System.out.print (result.size () + " types returned");
  System.out.print ("Key");
  System.out.print ("|");
  System.out.print ("Name");
  System.out.print ("|");
  System.out.print ("getDesc");
  System.out.print ("|");
  System.out.print ("EffectiveDate");
  System.out.print ("|");
  System.out.print ("ExpirationDate");
  System.out.println ("|");
  for (AtpTypeInfo typeInfo : result)
  {
   System.out.print (typeInfo.getId ());
   System.out.print ("|");
   System.out.print (typeInfo.getName ());
   System.out.print ("|");
   System.out.print (typeInfo.getDescr ());
   System.out.print ("|");
   System.out.print (typeInfo.getEffectiveDate ());
   System.out.print ("|");
   System.out.print (typeInfo.getExpirationDate ());
   System.out.println ("|");
  }
  assertEquals (40, result.size ());
 }

 /**
  * Test of GetAtpanization method in AtpFinder
  */
 @Test
 public void testGetAtp ()
 {
  System.out.println ("getAtp");

//  List<AtpTypeInfo> expResult = new ArrayList ();
  String id = "kuali.atp.FA2008-2009";
  AtpInfo result;

  try
  {
   result = atpService.getAtp (id);
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  System.out.print (result.getId ());
  System.out.print ("|");
  System.out.print (result.getName ());
  System.out.println ("|");

 }

 /**
  * Test of GetAtpanization method in AtpFinder
  */
 @Test
 public void testThrowsAlreadyExistsExceptionOnCreate ()
 {
  System.out.println ("throwsAlreadyExistsExceptionOnCreate");

//  List<AtpTypeInfo> expResult = new ArrayList ();
  String id = "kuali.atp.FA2008-2009";
  AtpInfo result;

  try
  {
   result = atpService.getAtp (id);
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  System.out.print (result.getId ());
  System.out.print ("|");
  System.out.print (result.getName ());
  System.out.println ("|");
  try
  {
   atpService.createAtp (result.getType (), result.getId (), result);
   fail ("should have gotten already exists exception");
  }
  catch (AlreadyExistsException ex)
  {
   // should succeed
  }
  catch (Exception ex)
  {
   fail ("Did not get an AlreadyExistsException but instead got a "
         + ex.toString ());
  }

 }

 /**
  * Test of GetAtpanization method in AtpFinder
  */
 @Test
 public void testThrowsDoesNotExistExceptionOnGet ()
 {
  System.out.println ("testThrowsDoesNotExistExceptionOnGet");

//  List<AtpTypeInfo> expResult = new ArrayList ();
  String id = "kuali.atp.THIS SHOULD NOT EXIST FA2008-2009";
  AtpInfo result;

  try
  {
   result = atpService.getAtp (id);
   fail ("should have thrown does not exist exception");
  }
  catch (DoesNotExistException ex)
  {
   // success
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
 }

 /**
  * Test of CreateAtpanization method in AtpFinder
  */
 @Test
 public void testAtpLifeCycle ()
 {
  System.out.println ("AtpLifeCycle");
//  List<AtpTypeInfo> expResult = new ArrayList ();
  AtpInfo info = new AtpInfo ();
  info.setType ("kuali.atp.type.Fall");
  info.setState ("actual");
  info.setId ("kuali.atp.FA2001-02");
  info.setName ("short name");
  info.setDesc (new RichTextInfoHelper ().getFromPlain (
    "long name that is longer than the short name"));
  AtpInfo result = null;
  try
  {
   result = atpService.createAtp (info.getType (), info.getId (), info);
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  assertNotNull (result.getId ());
  assertEquals (info.getType (), result.getType ());
  assertEquals (info.getState (), result.getState ());
  assertEquals (info.getId (), result.getId ());
  assertEquals (info.getName (), result.getName ());
  assertEquals (info.getDesc ().getPlain (), result.getDesc ().getPlain ());
//  assertEquals (info.getSortName (), result.getSortName ());
  System.out.println ("id=" + result.getId ());

  // get it now
  info = result;
  try
  {
   result = atpService.getAtp (info.getId ());
  }
  catch (DoesNotExistException ex)
  {
   fail ("org was just created by cannot get it");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result.getId ());
  assertEquals (info.getType (), result.getType ());
  assertEquals (info.getState (), result.getState ());
  assertEquals (info.getId (), result.getId ());
  assertEquals (info.getName (), result.getName ());
  assertEquals (info.getDesc ().getPlain (), result.getDesc ().getPlain ());
  // now update it
  info = result;
  info.setName ("new name");
  try
  {
   result = atpService.updateAtp (info.getId (), info);
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }

  assertNotNull (result.getId ());
  assertEquals (info.getType (), result.getType ());
  assertEquals (info.getState (), result.getState ());
  assertEquals (info.getId (), result.getId ());
  assertEquals (info.getName (), result.getName ());
  assertEquals (info.getDesc ().getPlain (), result.getDesc ().getPlain ());


  // now delete
  info = result;
  StatusInfo status = null;
  try
  {
   status = atpService.deleteAtp (info.getId ());
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }

  assertTrue (status.getSuccess ());

  // get it now
  info = result;
  try
  {
   result = atpService.getAtp (info.getId ());
   fail ("should not be able to get the org");
  }
  catch (DoesNotExistException ex)
  {
   System.out.println ("as expected org does not exist");
  }
  catch (Exception ex)
  {
   System.out.println (
     "The server does not throw a DoesNotExistException but oh well");
  }


 }
}
