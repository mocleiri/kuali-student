package com.sigmasys.kuali.ksa.service;

import static org.springframework.util.Assert.isTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.model.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional(readOnly = false)
public class RefundServiceTest extends AbstractServiceTest {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;
    
    @Autowired
    private RefundService refundService;
    
    
    @Test
    public void testIsRefundRuleValid() throws Exception {
    	// Test null rule:
    	String refundRule = null;
    	boolean ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(ruleValid);
    	
    	// Test empty rule:
    	refundRule = "";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(ruleValid);
    	
    	// Test blank rule:
    	refundRule = "   ";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(ruleValid);
    	
    	// Test bad format rule:
    	refundRule = "1234345---";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test bad format "A" rule:
    	refundRule = "A(1234345---";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test bad format "S" rule:
    	refundRule = "S123434)5---";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "A" rule numeric parameter out of lower bound:
    	refundRule = "A(-1)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "A" rule numeric parameter out of upper bound:
    	refundRule = "A(65536)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "S" rule numeric parameter out of lower bound:
    	refundRule = "S(-100)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "S" rule numeric parameter out of upper bound:
    	refundRule = "A(65538)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "A" rule does not have an account:
    	refundRule = "A(9)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "A" rule has an blank account:
    	refundRule = "A(9)()";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test "A" rule has an non-existing account:
    	refundRule = "A(9)(dum-de-dum)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(!ruleValid);
    	
    	// Test valid "A" rule:
    	refundRule = "A(9)(admin1)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(ruleValid);
    	
    	// Test valid "S" rule:
    	refundRule = "S(9)";
    	ruleValid = refundService.isRefundRuleValid(refundRule);
    	
    	isTrue(ruleValid);
    }

}
