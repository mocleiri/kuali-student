package com.sigmasys.kuali.ksa.service;


import javax.servlet.http.HttpServletRequest;

import com.sigmasys.kuali.ksa.util.ContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.identity.IdentityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.util.RequestUtils;


@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { ServiceTestSuite.TEST_KSA_CONTEXT })
public class KimServiceTest extends AbstractServiceTest {
	
	@Autowired
	private UserSessionManager userSessionManager;

	@Test
	public void getUserId() throws Exception {
		
		HttpServletRequest request = RequestUtils.getThreadRequest();
		
		Assert.notNull(request);
		
		String userId = userSessionManager.getUserId(request);
		
		Assert.notNull(userId);
		
		IdentityService service = ContextUtils.getBean("identityService",IdentityService.class);
		
		Principal principal = service.getPrincipal("mivanov");
		
		Assert.isNull(principal);

	}

}
