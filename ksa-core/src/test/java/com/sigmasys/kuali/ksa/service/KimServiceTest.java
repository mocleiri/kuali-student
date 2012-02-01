package com.sigmasys.kuali.ksa.service;


import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kim.service.IdentityManagementService;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.util.RequestUtils;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { ServiceTestSuite.TEST_CONTEXT })
public class KimServiceTest extends AbstractServiceTest {
	
	@Autowired
	private UserSessionManager userSessionManager;

	@Test
	public void getUserId() throws Exception {
		
		HttpServletRequest request = RequestUtils.getThreadRequest();
		
		Assert.notNull(request);
		
		String userId = userSessionManager.getUserId(request);
		
		Assert.notNull(userId);
		
		IdentityManagementService service = KIMServiceLocator.getIdentityManagementService();
		
		String principalName = service.getAuthenticatedPrincipalName(request);
		
		Assert.notNull(principalName);

		Assert.isTrue(principalName.equals(userId));
	}

}
