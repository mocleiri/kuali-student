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
package org.kuali.student.loader.util;

import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.soap.SOAPFaultException;
import org.kuali.student.core.atp.dto.AtpInfo;
import org.kuali.student.core.atp.service.AtpService;
import org.kuali.student.common.exceptions.DoesNotExistException;
import org.kuali.student.common.exceptions.InvalidParameterException;
import org.kuali.student.common.exceptions.MissingParameterException;
import org.kuali.student.common.exceptions.OperationFailedException;

/**
 *
 * @author nwright
 */
public class GetAtpHelper
{

 private AtpService atpService;

 public GetAtpHelper (AtpService atpService)
 {
  this.atpService = atpService;
 }

 private static Map<String, AtpInfo> cache = new HashMap (100);

 /**
  * get the atp
  * @param atpKey
  * @return null if not found
  */
 public AtpInfo getAtp (String atpKey)
 {
  AtpInfo info = cache.get (atpKey);
  if (info != null)
  {
   return info;
  }
  try
  {
   info = atpService.getAtp (atpKey);
   this.cache.put (atpKey, info);
   return info;
  }
  catch (DoesNotExistException ex)
  {
   return null;
  }
  catch (InvalidParameterException ex)
  {
   throw new RuntimeException (ex);
  }
  catch (MissingParameterException ex)
  {
   throw new RuntimeException (ex);
  }
  catch (OperationFailedException ex)
  {
   throw new RuntimeException (ex);
  }
  catch (SOAPFaultException ex)
  {
   //TODO: fix this once the atp impl gets fixed
   // assume this failure means not found because the impl does not throw not found as it should
   return null;
  }
 }
}
