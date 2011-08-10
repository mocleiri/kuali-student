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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.common.dto.StatusInfo;
import org.kuali.student.common.exceptions.DataValidationErrorException;
import org.kuali.student.common.exceptions.DoesNotExistException;
import org.kuali.student.common.validation.dto.ValidationResultInfo;
import org.kuali.student.loader.util.RichTextInfoHelper;
import org.kuali.student.lum.lu.dto.CluIdentifierInfo;
import org.kuali.student.lum.lu.dto.CluInfo;
import org.kuali.student.lum.lu.service.LuService;

public class LuServiceTest
{

 public LuServiceTest ()
 {
 }
 private static LuService luService;

 @Before
 public void setUp () throws Exception
 {
 }

 @BeforeClass
 public static void setUpClass () throws Exception
 {
  LuServiceFactory factory = new LuServiceFactory ();
  factory.setHostUrl (LuServiceFactory.LOCAL_HOST_EMBEDDED_URL);
  luService = factory.getLuService ();
 }

 @AfterClass
 public static void tearDownClass () throws Exception
 {
 }

 /**
  * Test of getClu method, of class LuService.
  */
 @Test
 public void testGetClu ()
 {
  System.out.println ("getClu");
  String id = "a079e6e4-9d4c-4296-8db3-5fa353fcfec9";
  CluInfo result = null;
  try
  {
   result = luService.getClu (id);
  }
  catch (DoesNotExistException ex)
  {
   fail ("should exist if impex was run properly");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
 }

 /**
  * Test of createClu method, of class LuService.
  */
 @Test
 public void testCreateClu ()
 {
  System.out.println ("createClu");
  CluInfo info = new CluInfo ();
  info.setId ("TEST-ID-XYZZY");
  info.setDescr (
    new RichTextInfoHelper ().getFromPlain ("my stupid description"));
  CluIdentifierInfo idInfo = new CluIdentifierInfo ();
  info.setOfficialIdentifier (idInfo);
  idInfo.setType (StandardizedTestToCluInfoConverter.OFFICIAL_IDENTIFIER_TYPE);
  idInfo.setState ("Active");
  idInfo.setShortName ("Active");
  idInfo.setCode ("SAT-COMPOSITE");
  idInfo.setDivision ("SAT");
  idInfo.setSuffixCode ("COMPOSITE");
  idInfo.setLevel ("UND");
  idInfo.setShortName ("SAT Composite Score");
  idInfo.setLongName ("Scholastic Aptitude TEst");
  info.setType ("kuali.lu.type.standardized.test");
  info.setState ("Active");
  info.setEffectiveDate (toDate ("2000-01-01"));
  info.setExpirationDate (null);

  CluInfo result = null;
  try
  {
   result = luService.createClu (info.getType (), info);
   assertNotNull (result);
   assertNotSame (info, result);
   assertNotNull (result.getId ());

   assertEquals (info.getOfficialIdentifier ().getDivision (),
                 result.getOfficialIdentifier ().getDivision ());
   assertEquals (info.getOfficialIdentifier ().getSuffixCode (),
                 result.getOfficialIdentifier ().getSuffixCode ());
   assertEquals (info.getOfficialIdentifier ().getCode (),
                 result.getOfficialIdentifier ().getCode ());
  }
  catch (DataValidationErrorException ex)
  {
   List<ValidationResultInfo> vris = ex.getValidationResults ();
   if (vris == null)
   {
    System.out.println (
      "got DataValidationErrorException but no validation results");
    try
    {
     vris = luService.validateClu ("SYSTEM", info);
    }
    catch (Exception ex2)
    {
     throw new RuntimeException (ex2);
    }
   }
   for (ValidationResultInfo vri : vris)
   {
    System.out.println (vri.getElement () + " " + vri.getMessage ());
   }
   fail ("got validation exception");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }

  // get
  info = result;
  try
  {
   result = luService.getClu (info.getId ());
  }
  catch (DoesNotExistException ex)
  {
   fail ("should exist");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  assertNotSame (info, result);
  assertNotNull (result.getId ());
  assertEquals (info.getOfficialIdentifier ().getDivision (),
                result.getOfficialIdentifier ().getDivision ());
  assertEquals (info.getOfficialIdentifier ().getSuffixCode (),
                result.getOfficialIdentifier ().getSuffixCode ());
  assertEquals (info.getOfficialIdentifier ().getCode (),
                result.getOfficialIdentifier ().getCode ());

  // update
  info = result;
  info.getOfficialIdentifier ().setShortName ("SAT Composite Score - Updated");
  try
  {
   result = luService.updateClu (info.getId (), info);
  }
  catch (DoesNotExistException ex)
  {
   fail ("should exist");
  }
  catch (DataValidationErrorException ex)
  {
   List<ValidationResultInfo> vris = ex.getValidationResults ();
   if (vris == null)
   {
    System.out.println (
      "got DataValidationErrorException but no validation results");
    try
    {
     vris = luService.validateClu ("SYSTEM", info);
    }
    catch (Exception ex2)
    {
     throw new RuntimeException (ex2);
    }
   }
   for (ValidationResultInfo vri : vris)
   {
    System.out.println (vri.getElement () + " " + vri.getMessage ());
   }

   fail ("got validation exception");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  assertNotSame (info, result);
  assertNotNull (result.getId ());
  assertEquals (info.getId (), result.getId ());
  assertEquals (info.getOfficialIdentifier ().getDivision (),
                result.getOfficialIdentifier ().getDivision ());
  assertEquals (info.getOfficialIdentifier ().getSuffixCode (),
                result.getOfficialIdentifier ().getSuffixCode ());
  assertEquals (info.getOfficialIdentifier ().getCode (),
                result.getOfficialIdentifier ().getCode ());


  // get
  info = result;
  try
  {
   result = luService.getClu (info.getId ());
  }
  catch (DoesNotExistException ex)
  {
   fail ("should exist");
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }
  assertNotNull (result);
  assertNotSame (info, result);
  assertNotNull (result.getId ());
  assertEquals (info.getId (), result.getId ());
  assertEquals (info.getOfficialIdentifier ().getDivision (),
                result.getOfficialIdentifier ().getDivision ());
  assertEquals (info.getOfficialIdentifier ().getSuffixCode (),
                result.getOfficialIdentifier ().getSuffixCode ());
  assertEquals (info.getOfficialIdentifier ().getCode (),
                result.getOfficialIdentifier ().getCode ());

  // delete
  info = result;
  try
  {
   StatusInfo status = luService.deleteClu (info.getId ());
   assertTrue (status.getSuccess ());
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }

  try
  {
   result = luService.getClu (info.getId ());
   fail ("should have thrown does not exist exception");
  }
  catch (DoesNotExistException ex)
  {
   // as expected
  }
  catch (Exception ex)
  {
   throw new RuntimeException (ex);
  }

 }

 private Date toDate (String str)
 {
  DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
  try
  {
   return df.parse (str);
  }
  catch (ParseException ex)
  {
   throw new RuntimeException (ex);
  }
 }
}
