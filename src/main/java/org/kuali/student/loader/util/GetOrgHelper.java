
package org.kuali.student.loader.util;

import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.soap.SOAPFaultException;
import org.kuali.student.common.exceptions.DoesNotExistException;
import org.kuali.student.common.exceptions.InvalidParameterException;
import org.kuali.student.common.exceptions.MissingParameterException;
import org.kuali.student.common.exceptions.OperationFailedException;
import org.kuali.student.common.exceptions.PermissionDeniedException;
import org.kuali.student.core.organization.dto.OrgInfo;
import org.kuali.student.core.organization.service.OrganizationService;


public class GetOrgHelper
{

 private OrganizationService orgService;

 public GetOrgHelper (OrganizationService orgService)
 {
  this.orgService = orgService;
 }

 private static Map<String, OrgInfo> cache = new HashMap (100);

 /**
  * get the OrgInfo
  * @param orgId
  * @return null if not found
  */
 public OrgInfo getOrg (String orgId)
 {	 
	 OrgInfo info = cache.get (orgId);
  if (info != null)
  {
   return info;
  }
  try
  {
   info = orgService.getOrganization(orgId);
   this.cache.put (orgId, info);
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
  catch (PermissionDeniedException ex)
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
