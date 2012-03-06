package com.sigmasys.kuali.ksa.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { ServiceTestSuite.TEST_KSA_CONTEXT })
public class TransactionServiceTest extends AbstractServiceTest {

	@Test
	public void getTransaction() throws Exception {
		// TODO - write a transaction test #1
		Assert.notNull("transaction");
		Assert.isTrue(1 >= 0);
		Assert.isTrue("Transaction".equalsIgnoreCase("transaction"));
	}

}
