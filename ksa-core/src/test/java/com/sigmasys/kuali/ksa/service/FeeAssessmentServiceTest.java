package com.sigmasys.kuali.ksa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.LearningPeriod;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;
import com.sigmasys.kuali.ksa.service.support.FeeBase;
import com.sigmasys.kuali.ksa.util.ContextUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional(readOnly = false)
public class FeeAssessmentServiceTest extends AbstractServiceTest {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

	@Autowired
	private FeeAssessmentService feeAssessmentService;
	
	@Autowired
	private AccountService accountService;
	
	// Test objects:
	private String accountId = "admin1";
	private Account testAccount;
	
	// Transaction Management objects:
	PlatformTransactionManager transactionManager;
	DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
	
	@Test
	public void testKeyPairPreviousValue() throws Exception {
		// Create a new KeyPair:
		KeyPair kp = new KeyPair();
		String name = "name";
		String value = "value";
		String newValue = "new value";
		
		kp.setName(name);
		kp.setValue(value);
		
		// Assert validity:
		assertEquals(name, kp.getName());
		assertEquals(value, kp.getValue());
		assertNull(kp.getPreviousValue());
		
		// Set a new value:
		kp.setValue(newValue);
		
		// Make more assertions:
		assertEquals(name, kp.getName());
		assertEquals(newValue, kp.getValue());
		assertEquals(value, kp.getPreviousValue());
	}
		
	@Test
	public void testGetStudentDataNoDataExists() throws Exception {
		// Get the student data:
		List<KeyPair> studentDataFromService = feeAssessmentService.getStudentData(accountId);
		
		assertNotNull(studentDataFromService);
		assertTrue(studentDataFromService.isEmpty());
	}
	
	@Test
	public void testGetStudentDataDataPersistedWithEM() throws Exception {
		// Create and persist new Student Data:
		List<KeyPair> testStudentData = createTestStudentDataWithinTransaction();
		
		// If creation of the test data failed, so be it, nothing to test:
		if (testStudentData == null) {
			return;
		}
		
		// Get student data for the test account:
		List<KeyPair> studentDataFromService = feeAssessmentService.getStudentData(accountId);
		
		// Validate the result:
		assertKeyPairs(testStudentData, studentDataFromService);
	}
	
	@Test
	public void testGetLearningPeriodDataNoDataExists() throws Exception {
		// Get the learning period data:
		List<PeriodKeyPair> periodDataFromService = feeAssessmentService.getLearningPeriodData(accountId);
		
		assertNotNull(periodDataFromService);
		assertTrue(periodDataFromService.isEmpty());
	}
	
	@Test
	public void testGetLearningPeriodDataDataPersistedWithEM() throws Exception {
		// Create and persist Period data:
		List<PeriodKeyPair> testPeriodData = createTestPeriodDataWithinTransaction();
		
		// If creation of the test data failed, so be it, nothing to test:
		if (testPeriodData == null) {
			return;
		}
		
		// Get the learning period data:
		List<PeriodKeyPair> periodDataFromService = feeAssessmentService.getLearningPeriodData(accountId);
		
		// Validate the result:
		assertKeyPairs(testPeriodData, periodDataFromService);
	}
	
	@Test
	public void testGetStudyNoDataExists() throws Exception {
		// Get the study data:
		List<LearningUnit> studyFromService = feeAssessmentService.getStudy(accountId);
		
		assertNotNull(studyFromService);
		assertTrue(studyFromService.isEmpty());
	}
	
	@Test
	public void testGetStudyDataPersistedWithEM() throws Exception {
		// Create and persist the study data:
		List<LearningUnit> testStudy = createTestStudyWithinTransaction();
		
		// If creation of the test data failed, so be it, nothing to test:
		if (testStudy == null) {
			return;
		}
	
		// Get the study data:
		List<LearningUnit> studyFromService = feeAssessmentService.getStudy(accountId);

		// Validate the result:
		assertStudy(testStudy, studyFromService);
	}
	
	@Test
	public void testGetFeeBaseNoDataExists() throws Exception {
		// Create an account:
		testAccount = accountService.getOrCreateAccount(accountId);
		
		// Get a FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the result:
		assertNotNull(feeBaseFromService);
		assertNotNull(feeBaseFromService.getStudentData());
		assertTrue(feeBaseFromService.getStudentData().isEmpty());
		assertNotNull(feeBaseFromService.getPeriodData());
		assertTrue(feeBaseFromService.getPeriodData().isEmpty());
		assertNotNull(feeBaseFromService.getStudy());
		assertTrue(feeBaseFromService.getStudy().isEmpty());
		assertNotNull(feeBaseFromService.getAccount());
		assertAccount(testAccount, feeBaseFromService.getAccount());
	}
	
	@Test
	public void testGetFeeBaseDataPersistedWithEM() throws Exception {
		// Create a test FeeBase:
		FeeBase testFeeBase = createTestFeeBase();
		
		// If creation of a new FeeBase failed, nothing to test, something just went wrong:
		if (testFeeBase == null) {
			return;
		}
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the result:
		assertFeeBase(testFeeBase, feeBaseFromService);
	}
	
	@Test
	public void testCalcluateChargeByCreditToMaxNegativeMax() throws Exception {
		int numOfCredits = 5;
		double amountPerCredit = 250.5;
		double maxAmount = -100.5;
		double charge = feeAssessmentService.calcluateChargeByCreditToMax(numOfCredits, amountPerCredit, maxAmount);
		
		assertEquals(1252.5, charge, 0.0);
	}
	
	@Test
	public void testCalcluateChargeByCreditToMaxPositiveMaxGreaterThanTotal() throws Exception {
		int numOfCredits = 5;
		double amountPerCredit = 250.5;
		double maxAmount = 2000.5;
		double charge = feeAssessmentService.calcluateChargeByCreditToMax(numOfCredits, amountPerCredit, maxAmount);
		
		assertEquals(1252.5, charge, 0.0);
	}
	
	@Test
	public void testCalcluateChargeByCreditToMaxPositiveMaxLessThanTotal() throws Exception {
		int numOfCredits = 5;
		double amountPerCredit = 250.5;
		double maxAmount = 200.5;
		double charge = feeAssessmentService.calcluateChargeByCreditToMax(numOfCredits, amountPerCredit, maxAmount);
		
		assertEquals(200.5, charge, 0.0);
	}
	
	@Test
	public void testCalcluateChargeByCreditToMaxZeroMax() throws Exception {
		int numOfCredits = 5;
		double amountPerCredit = 250.5;
		double maxAmount = 0;
		double charge = feeAssessmentService.calcluateChargeByCreditToMax(numOfCredits, amountPerCredit, maxAmount);
		
		assertEquals(0, charge, 0.0);
	}
	
	@Test
	public void testCreateKeyPairWithNewName() throws Exception {
		// Test objects:
		createTestFeeBase();
		String name = "absolutely new name";
		String value = "absolutely new value";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		KeyPair newKeyPair = feeAssessmentService.createKeyPair(feeBaseFromService, name, value);
		
		// Validate the result:
		assertNotNull(newKeyPair);
		assertEquals(name, newKeyPair.getName());
		assertEquals(value, newKeyPair.getValue());
		assertNull(newKeyPair.getPreviousValue());
		
		// Get a fresh FeeBase from Service again:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the new KeyPair is in the FeeBase now:
		assertFeeBase(feeBaseFromService, freshFeeBaseFromService);
	}
	
	@Test
	public void testCreateKeyPairWithExistingName() throws Exception {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String nameThatHasAlreadyBeenUsed = "boo";
		String value = "absolutely new value";
		
		// Make sure an exception is thrown because the name already exists:
		boolean exceptionRaised = false;
		
		try {
			// Call the service:
			feeAssessmentService.createKeyPair(testFeeBase, nameThatHasAlreadyBeenUsed, value);
			
			// We should not be here:
			assertTrue("An error should have occurred because a KeyPair name already exists, but it DID'T.", false);
		} catch (Exception e) {
			exceptionRaised = true;
		}
		
		// Assert an exception has been raised:
		assertTrue("An error should have occurred because a KeyPair name already exists, but it DID'T.", exceptionRaised);
	}
	
	@Test
	public void testCreateKeyPairWithInvalidParameters() throws Exception {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.createKeyPair(nullFeeBase, "new name", "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.createKeyPair(testFeeBase, null, "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.createKeyPair(testFeeBase, "New name", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.createKeyPair(nullFeeBase, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}

	@Test
	public void testCreatePeriodKeyPairWithNewName() throws Exception {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String name = "absolutely new name";
		String value = "absolutely new value";
		LearningPeriod period = testFeeBase.getPeriodData().get(0).getLearningPeriod();
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		PeriodKeyPair newKeyPair = feeAssessmentService.createKeyPair(feeBaseFromService, name, value, period);
		
		// Validate the result:
		assertNotNull(newKeyPair);
		assertEquals(name, newKeyPair.getName());
		assertEquals(value, newKeyPair.getValue());
		assertNull(newKeyPair.getPreviousValue());
		assertLearningPeriod(period, newKeyPair.getLearningPeriod());
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the new KeyPair is in the FeeBase now:
		assertFeeBase(feeBaseFromService, freshFeeBaseFromService);
	}
	
	@Test
	public void testCreatePeriodKeyPairWithExistingName() throws Exception {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String nameThatHasAlreadyBeenUsed = "boo1";
		String value = "absolutely new value";
		LearningPeriod period = testFeeBase.getPeriodData().get(0).getLearningPeriod();
		
		// Make sure an exception is thrown because the name already exists:
		boolean exceptionRaised = false;
		
		try {
			// Call the service:
			feeAssessmentService.createKeyPair(testFeeBase, nameThatHasAlreadyBeenUsed, value, period);
			
			// We should not be here:
			assertTrue("An error should have occurred because a PeriodKeyPair name already exists, but it DID'T.", false);
		} catch (Exception e) {
			exceptionRaised = true;
		}
		
		// Assert an exception has been raised:
		assertTrue("An error should have occurred because a PeriodKeyPair name already exists, but it DID'T.", exceptionRaised);
	}
	
	@Test
	public void testCreatePeriodKeyPairWithInvalidParameters() throws Exception {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.createKeyPair(nullFeeBase, "new name", "New value", new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.createKeyPair(testFeeBase, null, "New value", new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.createKeyPair(testFeeBase, "New name", null, new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null period:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.createKeyPair(testFeeBase, "New name", "new value", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.createKeyPair(nullFeeBase, null, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}

	@Test
	public void testCreateLearningUnitKeyPairWithNewName() throws Exception {
		// Test objects:
		createTestFeeBase();
		String name = "absolutely new name";
		String value = "absolutely new value";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit testLearningUnit = feeBaseFromService.getStudy().get(0);
		
		// Call the service:
		KeyPair newKeyPair = feeAssessmentService.createKeyPair(testLearningUnit, name, value);
		
		// Validate the result:
		assertNotNull(newKeyPair);
		assertEquals(name, newKeyPair.getName());
		assertEquals(value, newKeyPair.getValue());
		assertNull(newKeyPair.getPreviousValue());
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the new KeyPair is in the FeeBase now:
		assertFeeBase(feeBaseFromService, freshFeeBaseFromService);
	}
	
	@Test
	public void testCreateLearningUnitKeyPairWithExistingName() throws Exception {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		LearningUnit testLearningUnit = testFeeBase.getStudy().get(0);
		String nameThatHasAlreadyBeenUsed = "boo";
		String value = "absolutely new value";
		
		// Make sure an exception is thrown because the name already exists:
		boolean exceptionRaised = false;
		
		try {
			// Call the service:
			feeAssessmentService.createKeyPair(testLearningUnit, nameThatHasAlreadyBeenUsed, value);
			
			// We should not be here:
			assertTrue("An error should have occurred because a KeyPair name already exists, but it DID'T.", false);
		} catch (Exception e) {
			exceptionRaised = true;
		}
		
		// Assert an exception has been raised:
		assertTrue("An error should have occurred because a KeyPair name already exists, but it DID'T.", exceptionRaised);
	}
	
	@Test
	public void testCreateKeyLearningUnitPairWithInvalidParameters() throws Exception {
		// Null LearningUnit:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.createKeyPair(nullLearningUnit, "new name", "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			feeAssessmentService.createKeyPair(new LearningUnit(), null, "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			feeAssessmentService.createKeyPair(new LearningUnit(), "new name", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null everything:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.createKeyPair(nullLearningUnit, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testGetKeyPairValueExistingName() throws Exception {
		// Test Objects:
		createTestFeeBase();
		String existingKeyPairName = "boo";
		String existingKeyPairValue = "foo";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		String kpValueFromService = feeAssessmentService.getKeyPairValue(feeBaseFromService, existingKeyPairName);
		
		// Validate:
		assertEquals(existingKeyPairValue, kpValueFromService);
	}
	
	@Test
	public void testGetKeyPairValueNonExistingName() throws Exception {
		// Test Objects:
		createTestFeeBase();
		String nonExistingKeyPairName = "lalala-1";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		String kpValueFromService = feeAssessmentService.getKeyPairValue(feeBaseFromService, nonExistingKeyPairName);
		
		// Validate:
		assertNull(kpValueFromService);
	}
	
	@Test
	public void testGetKeyPairValueWithInvalidParameters() throws Exception {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.getKeyPairValue(nullFeeBase, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.getKeyPairValue(testFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.getKeyPairValue(nullFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testGetPeriodKeyPairValueExistingName() throws Exception {
		// Test Objects:
		createTestFeeBase();
		String existingKeyPairName = "boo1";
		String existingKeyPairValue = "foo1";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		String kpValueFromService = feeAssessmentService.getKeyPairValue(feeBaseFromService, existingKeyPairName);
		
		// Validate:
		assertEquals(existingKeyPairValue, kpValueFromService);
	}
	
	@Test
	public void testGetLearningUnitKeyPairValueExistingName() throws Exception {
		// Test Objects:
		createTestFeeBase();
		String existingKeyPairName = "boo";
		String existingKeyPairValue = "foo";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Call the service:
		String kpValueFromService = feeAssessmentService.getKeyPairValue(luFromService, existingKeyPairName);
		
		// Validate:
		assertEquals(existingKeyPairValue, kpValueFromService);
	}
	
	@Test
	public void testGetLearningUnitKeyPairValueNonExistingName() throws Exception {
		// Test Objects:
		createTestFeeBase();
		String nonExistingKeyPairName = "lalala-1";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Call the service:
		String kpValueFromService = feeAssessmentService.getKeyPairValue(luFromService, nonExistingKeyPairName);
		
		// Validate:
		assertNull(kpValueFromService);
	}
	
	@Test
	public void testGetLearningUnitKeyPairValueWithInvalidParameters() throws Exception {
		// Null LearningUnit:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.getKeyPairValue(nullLearningUnit, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			feeAssessmentService.getKeyPairValue(new LearningUnit(), null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null everything:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.getKeyPairValue(nullLearningUnit, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testRemoveKeyPairExistingName() {
		// Test objects:
		createTestFeeBase();
		String name = "boo";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		feeAssessmentService.removeKeyPair(feeBaseFromService, name);
		
		// Validate:
		boolean hasBeenRemoved = !feeAssessmentService.containsKeyPair(feeBaseFromService, name);
		
		assertTrue(hasBeenRemoved);
		
		// Select from service again:
		feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Revalidate:
		hasBeenRemoved = !feeAssessmentService.containsKeyPair(feeBaseFromService, name);
		
		assertTrue(hasBeenRemoved);
	}
	
	@Test
	public void testRemoveKeyPairNonExistingName() {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String name = "something different";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		feeAssessmentService.removeKeyPair(feeBaseFromService, name);
		
		// Validate:
		assertFeeBase(testFeeBase, feeBaseFromService);
	}
	
	@Test
	public void testRemovePeriodKeyPairExistingName() {
		// Test objects:
		createTestFeeBase();
		String name = "boo1";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		feeAssessmentService.removeKeyPair(feeBaseFromService, name);
		
		// Validate:
		boolean hasBeenRemoved = !feeAssessmentService.containsKeyPair(feeBaseFromService, name);
		
		assertTrue(hasBeenRemoved);
		
		// Select from service again:
		feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Revalidate:
		hasBeenRemoved = !feeAssessmentService.containsKeyPair(feeBaseFromService, name);
		
		assertTrue(hasBeenRemoved);
	}
	
	@Test
	public void testRemoveKeyPairWithInvalidParameters() {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.removeKeyPair(nullFeeBase, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.removeKeyPair(testFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.removeKeyPair(nullFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testRemoveLearningUnitKeyPairExistingName() {
		// Test Objects:
		createTestFeeBase();
		String existingKeyPairName = "boo";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Call the service:
		feeAssessmentService.removeKeyPair(luFromService, existingKeyPairName);
		
		// Validate:
		boolean hasBeenRemoved = !feeAssessmentService.containsKeyPair(luFromService, existingKeyPairName);
		
		assertTrue(hasBeenRemoved);
	}
	
	@Test
	public void testRemoveLearningUnitKeyPairNonExistingName() {
		// Test Objects:
		FeeBase testFeeBase = createTestFeeBase();
		String existingKeyPairName = "boo";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Call the service:
		feeAssessmentService.removeKeyPair(luFromService, existingKeyPairName);
		
		// Validate:
		assertStudy(testFeeBase.getStudy(), feeBaseFromService.getStudy());
	}

	@Test
	public void testRemoveLearningUnitKeyPairWithInvalidParameters() {
		// Null LearningUnit:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.removeKeyPair(nullLearningUnit, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			feeAssessmentService.removeKeyPair(new LearningUnit(), null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null everything:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.removeKeyPair(nullLearningUnit, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testUpdateKeyPairExistingName() {
		// Test objects:
		createTestFeeBase();
		String name = "boo";
		String currentValue = "foo";
		String newValue = "new foo";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Assert the value is still the same:
		assertEquals(currentValue, feeAssessmentService.getKeyPairValue(feeBaseFromService, name));
		
		// Call the service:
		feeAssessmentService.updateKeyPair(feeBaseFromService, name, newValue);
		
		// Validate:
		assertEquals(newValue, feeAssessmentService.getKeyPairValue(feeBaseFromService, name));
		
		// Select from service again:
		feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Re-validate:
		assertEquals(newValue, feeAssessmentService.getKeyPairValue(feeBaseFromService, name));
	}
	
	@Test
	public void testUpdateKeyPairNonExistingName() {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String nonExistingName = "boo-hoo";
		String newValue = "new foo value";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		try {
			feeAssessmentService.updateKeyPair(feeBaseFromService, nonExistingName, newValue);
		} catch (Throwable t) {
			// Make sure we never get here:
			assertTrue("An exception should not have been raised", false);
		}
		
		// Validate:
		assertFeeBase(testFeeBase, feeBaseFromService);
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the FeeBase:
		assertFeeBase(testFeeBase, freshFeeBaseFromService);
	}
	
	@Test
	public void testUpdateKeyPairWithInvalidParameters() {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.updateKeyPair(nullFeeBase, "new name", "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.updateKeyPair(testFeeBase, null, "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.updateKeyPair(testFeeBase, "New name", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.updateKeyPair(nullFeeBase, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testUpdatePeriodKeyPairExistingName() {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String name = "boo1";
		String currentValue = "foo1";
		String newValue = "new foo1";
		LearningPeriod newPeriod = new LearningPeriod();
		Calendar calFrom = Calendar.getInstance();
		Calendar calTo = Calendar.getInstance();
		int newYearFrom = 1999;
		int newYearTo = 2001;
		
		calFrom.set(Calendar.YEAR, newYearFrom);
		calTo.set(Calendar.YEAR, newYearTo);
		newPeriod.setDateFrom(calFrom.getTime());
		newPeriod.setDateTo(calTo.getTime());
		
		// Find the original PeriodKeyPair being updated:
		PeriodKeyPair originalPkp = null;
		
		for (PeriodKeyPair pkp : testFeeBase.getPeriodData()) {
			if (StringUtils.equalsIgnoreCase(pkp.getName(), name)) {
				originalPkp = pkp;
				break;
			}
		}
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Assert the value is still the same:
		assertEquals(currentValue, feeAssessmentService.getKeyPairValue(feeBaseFromService, name));
		
		// Assert original years are different from what we're going to change them to:
		Calendar calOriginalFrom = Calendar.getInstance();
		Calendar calOriginalTo = Calendar.getInstance();
		
		calOriginalFrom.setTime(originalPkp.getLearningPeriod().getDateFrom());
		calOriginalTo.setTime(originalPkp.getLearningPeriod().getDateTo());
		assertTrue(calOriginalFrom.get(Calendar.YEAR) != newYearFrom);
		assertTrue(calOriginalTo.get(Calendar.YEAR) != newYearTo);
		
		// Call the service:
		feeAssessmentService.updateKeyPair(feeBaseFromService, name, newValue, newPeriod);
		
		// Validate:
		assertEquals(newValue, feeAssessmentService.getKeyPairValue(feeBaseFromService, name));
		
		// Find the PeriodKeyPair that has been updated:
		PeriodKeyPair updatedPkp = null;
		
		for (PeriodKeyPair pkp : testFeeBase.getPeriodData()) {
			if (StringUtils.equalsIgnoreCase(pkp.getName(), name)) {
				updatedPkp = pkp;
				break;
			}
		}
		
		// Validate:
		assertTrue(DateUtils.isSameDay(calFrom.getTime(), updatedPkp.getLearningPeriod().getDateFrom()));
		assertTrue(DateUtils.isSameDay(calTo.getTime(), updatedPkp.getLearningPeriod().getDateTo()));
	}
	
	@Test
	public void testUpdatePeriodKeyPairNonExistingName() {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String nonExistingName = "boo-hoo";
		String newValue = "new foo value";
		
		// Get the FeeBase from the service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Call the service:
		try {
			feeAssessmentService.updateKeyPair(feeBaseFromService, nonExistingName, newValue, new LearningPeriod());
		} catch (Throwable t) {
			// Make sure we never get here:
			assertTrue("An exception should not have been raised", false);
		}
		
		// Validate:
		assertFeeBase(testFeeBase, feeBaseFromService);
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the FeeBase:
		assertFeeBase(testFeeBase, freshFeeBaseFromService);
	}
	
	@Test
	public void testUpdatePeriodKeyPairWithInvalidParameters() {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.updateKeyPair(nullFeeBase, "new name", "New value", new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.updateKeyPair(testFeeBase, null, "New value", new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.updateKeyPair(testFeeBase, "New name", null, new LearningPeriod());
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null period:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.updateKeyPair(testFeeBase, "New name", "new value", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.updateKeyPair(nullFeeBase, null, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testUpdateLearningUnitKeyPairExistingName() {
		// Test objects:
		createTestFeeBase();
		String name = "boo";
		String existingValue = "foo";
		String newValue = "something really new";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		Long updatedId = luFromService.getId();
		
		// Assert the value is still the same:
		assertEquals(existingValue, feeAssessmentService.getKeyPairValue(luFromService, name));
		
		// Call the service:
		feeAssessmentService.updateKeyPair(luFromService, name, newValue);
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Find the same learning unit:
		LearningUnit updatedLu = null;
		
		for (LearningUnit lu : freshFeeBaseFromService.getStudy()) {
			if (lu.getId().equals(updatedId)) {
				updatedLu = lu;
				break;
			}
		}
		
		// Validate:
		assertEquals(newValue, feeAssessmentService.getKeyPairValue(updatedLu, name));
		assertFeeBase(feeBaseFromService, freshFeeBaseFromService);
	}
	
	@Test
	public void testUpdateLearningUnitKeyPairNonExistingName() {
		// Test objects:
		FeeBase testFeeBase = createTestFeeBase();
		String name = "boo";
		String existingValue = "foo";
		String newValue = "something really new";
		
		// Get the FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Assert the value is still the same:
		assertEquals(existingValue, feeAssessmentService.getKeyPairValue(luFromService, name));
		
		// Call the service:
		try {
			feeAssessmentService.updateKeyPair(luFromService, name, newValue);
		} catch (Throwable t) {
			// Make sure no exception has been raised:
			assertTrue(false);
		}
		
		// Validate:
		assertFeeBase(testFeeBase, feeBaseFromService);
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the FeeBase:
		assertFeeBase(testFeeBase, freshFeeBaseFromService);
	}
	
	@Test
	public void testUpdateLearningUnitKeyPairWithInvalidParameters () {
		// Null LearningUnit:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.updateKeyPair(nullLearningUnit, "new name", "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			feeAssessmentService.updateKeyPair(new LearningUnit(), null, "New value");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null value:
		try {
			feeAssessmentService.updateKeyPair(new LearningUnit(), "new name", null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null everything:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.updateKeyPair(nullLearningUnit, null, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testContainsKeyPair() {
		// Test objects:
		createTestFeeBase();
		
		// Get a fresh FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Test contains an existing name:
		String existingName = "boo";
		boolean contains = feeAssessmentService.containsKeyPair(feeBaseFromService, existingName);
		
		assertTrue(contains);
		
		// Test does not contain a non-existing name:
		String nonExistingName = "trolololol";
		
		contains = feeAssessmentService.containsKeyPair(feeBaseFromService, nonExistingName);
		assertFalse(contains);
	}
	
	@Test
	public void testContainsKeyPairWithInvalidParameters() {
		// Null FeeBase:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.containsKeyPair(nullFeeBase, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			FeeBase testFeeBase = createTestFeeBase();
			feeAssessmentService.containsKeyPair(testFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null Everything:
		try {
			FeeBase nullFeeBase = null;
			feeAssessmentService.containsKeyPair(nullFeeBase, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test 
	public void testContainsPeriodKeyPair() {
		// Test objects:
		createTestFeeBase();
		
		// Get a fresh FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Test contains an existing name:
		String existingName = "boo1";
		boolean contains = feeAssessmentService.containsKeyPair(feeBaseFromService, existingName);
		
		assertTrue(contains);
	}
	
	@Test
	public void testContainsLearningUnitKeyPair() {
		// Test objects:
		createTestFeeBase();
		
		// Get a fresh FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Test contains an existing name:
		String existingName = "boo";
		boolean contains = feeAssessmentService.containsKeyPair(luFromService, existingName);
		
		assertTrue(contains);
		
		// Test does not contain a non-existing name:
		String nonExistingName = "trolololol";
		
		contains = feeAssessmentService.containsKeyPair(luFromService, nonExistingName);
		assertFalse(contains);
	}
	
	@Test
	public void testContainsLearningUnitKeyPairWithInvalidParameters() {
		// Null LearningUnit:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.containsKeyPair(nullLearningUnit, "new name");
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null name:
		try {
			feeAssessmentService.containsKeyPair(new LearningUnit(), null);
			assertTrue(false);
		} catch(Exception e) {}
		
		// Null everything:
		try {
			LearningUnit nullLearningUnit = null;
			feeAssessmentService.containsKeyPair(nullLearningUnit, null);
			assertTrue(false);
		} catch(Exception e) {}
	}
	
	@Test
	public void testSaveLearningUnit() {
		// Test objects:
		createTestFeeBase();
		Calendar calNewAddDate = Calendar.getInstance();
		int newAddDateYear = 1987;
		int newAddDateMonth = Calendar.SEPTEMBER;
		int newAddDateDay = 5;
		Calendar calNewDropDate = Calendar.getInstance();
		int newDropDateYear = 1999;
		int newDropDateMonth = Calendar.MARCH;
		int newDropDateDay = 13;
		String newCampus = "new campus, eh?";
		double newCredit = 35.5;
		String newLevel = "new level, eh?";
		String newStatus = "new status, eh?";
		String newUnitCode = "new code, eh?";
		String newUnitSection = "new section, eh?";
		LearningPeriod newLp = new LearningPeriod();
		Calendar newDateFrom = Calendar.getInstance();
		int newDateFromYear = 1986;
		int newDateFromMonth = Calendar.JULY;
		int newDateFromDay = 18;
		Calendar newDateTo = Calendar.getInstance();
		int newDateToYear = 1995;
		int newDateToMonth = Calendar.JANUARY;
		int newDateToDay = 25;
		
		calNewAddDate.set(Calendar.YEAR, newAddDateYear);
		calNewAddDate.set(Calendar.MONTH, newAddDateMonth);
		calNewAddDate.set(Calendar.DAY_OF_MONTH, newAddDateDay);

		calNewDropDate.set(Calendar.YEAR, newDropDateYear);
		calNewDropDate.set(Calendar.MONTH, newDropDateMonth);
		calNewDropDate.set(Calendar.DAY_OF_MONTH, newDropDateDay);

		newDateFrom.set(Calendar.YEAR, newDateFromYear);
		newDateFrom.set(Calendar.MONTH, newDateFromMonth);
		newDateFrom.set(Calendar.DAY_OF_MONTH, newDateFromDay);

		newDateTo.set(Calendar.YEAR, newDateToYear);
		newDateTo.set(Calendar.MONTH, newDateToMonth);
		newDateTo.set(Calendar.DAY_OF_MONTH, newDateToDay);
		
		newLp.setDateFrom(newDateFrom.getTime());
		newLp.setDateTo(newDateTo.getTime());
		
		// Get a fresh FeeBase from service:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		LearningUnit luFromService = feeBaseFromService.getStudy().get(0);
		
		// Create a few new KeyPairs:
		String newName1 = "new name 1";
		String newValue1 = "new value 1";
		KeyPair newKp1 = feeAssessmentService.createKeyPair(feeBaseFromService, newName1, newValue1);
		String newName2 = "new name 2";
		String newValue2 = "new value 2";
		KeyPair newKp2 = feeAssessmentService.createKeyPair(feeBaseFromService, newName2, newValue2);
		
		// Set the new values:
		luFromService.setAddDate(calNewAddDate.getTime());
		luFromService.setCampus(newCampus);
		luFromService.setCredit(new BigDecimal(newCredit));
		luFromService.setDropDate(calNewDropDate.getTime());
		luFromService.setLearningPeriod(newLp);
		luFromService.setLevel(newLevel);
		luFromService.setStatus(newStatus);
		luFromService.setUnitCode(newUnitCode);
		luFromService.setUnitSection(newUnitSection);
		luFromService.setExtended(new HashSet<KeyPair>(Arrays.asList(newKp1, newKp2)));
		
		// Call the service:
		feeAssessmentService.saveLearningUnit(luFromService);
		
		// Get a fresh FeeBase from service:
		FeeBase freshFeeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate:
		assertFeeBase(feeBaseFromService, freshFeeBaseFromService);
	}
	
	@Test
	public void testSaveLearningUnitNullLearningUnit() {
		try {
			feeAssessmentService.saveLearningUnit(null);
			assertTrue(false);
		} catch (Exception e) {}
	}
	
	/* *************************************
	 * 
	 * Helper methods.
	 * 
	 * *************************************/
	
	private PlatformTransactionManager getTransactionManager() {
		if (transactionManager == null) {
			transactionManager = ContextUtils.getBean("transactionManager", PlatformTransactionManager.class);
		}
		
		return transactionManager;
	}
	
	private List<KeyPair> createTestStudentDataWithinTransaction() {
		// Create and persist a test Account:
		testAccount = accountService.getOrCreateAccount(accountId);
		persistAccount();
		
		// Get the existing transaction:
		transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus transaction = getTransactionManager().getTransaction(transactionDefinition);
		
		// Create KeyPairs:
		List<KeyPair> studentData = createKeyPairs();
		
		try {
			// Persist KeyPairs:
			for (KeyPair kp : studentData) {
				em.persist(kp);
			}
			
			em.flush();
			getTransactionManager().commit(transaction);
		} catch (Throwable t) {
			getTransactionManager().rollback(transaction);
			
			return null;
		}
		
		// Insert association table records:
		for (KeyPair kp : studentData) {
			Query query = em.createNativeQuery("insert into kssa_acnt_kypr (acnt_id_fk, kypr_id_fk) values(:accountId, :keypairId)")
					.setParameter("accountId", accountId).setParameter("keypairId", kp.getId());
			
			query.executeUpdate();
		}
		
		return studentData;
	}
	
	private List<KeyPair> createKeyPairs() {
		KeyPair kp1 = new KeyPair();
		KeyPair kp2 = new KeyPair();
		KeyPair kp3 = new KeyPair();
		
		kp1.setName("boo");
		kp1.setValue("foo");
		kp2.setName("boo2");
		kp2.setValue("foo2");
		kp2.setPreviousValue("previous foo2");
		kp3.setName("snafu");
		kp3.setValue("beyond recognition");
		
		return new ArrayList<KeyPair>(Arrays.asList(kp1, kp2, kp3));
	}
	
	private List<PeriodKeyPair> createTestPeriodDataWithinTransaction() {
		// Create and persist a test Account:
		testAccount = accountService.getOrCreateAccount(accountId);
		persistAccount();
		
		// Get the existing transaction:
		transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus transaction = getTransactionManager().getTransaction(transactionDefinition);
		
		// Create PeriodKeyPairs:
		List<PeriodKeyPair> periodData = createPeriodKeyPairs();
		
		try {
			// Persist PeriodKeyPairs:
			for (PeriodKeyPair pkp : periodData) {
				em.persist(pkp);
			}
			
			em.flush();
			getTransactionManager().commit(transaction);
		} catch (Throwable t) {
			getTransactionManager().rollback(transaction);
			
			return null;
		}
		
		// Insert association table records:
		for (PeriodKeyPair pkp : periodData) {
			Query query = em.createNativeQuery("insert into kssa_acnt_kypr (acnt_id_fk, kypr_id_fk) values(:accountId, :keypairId)")
					.setParameter("accountId", accountId).setParameter("keypairId", pkp.getId());
			
			query.executeUpdate();
		}
		
		return periodData;
	}
	
	private List<PeriodKeyPair> createPeriodKeyPairs() {
		PeriodKeyPair pkp1 = new PeriodKeyPair();
		PeriodKeyPair pkp2 = new PeriodKeyPair();
		PeriodKeyPair pkp3 = new PeriodKeyPair();
		PeriodKeyPair pkp4 = new PeriodKeyPair();
		LearningPeriod lp = new LearningPeriod();
		
		pkp1.setName("boo1");
		pkp1.setValue("foo1");
		pkp1.setLearningPeriod(lp);
		pkp2.setName("boo2");
		pkp2.setValue("foo2");
		pkp2.setPreviousValue("previous foo2");
		pkp2.setLearningPeriod(lp);
		pkp3.setName("boo3");
		pkp3.setValue("foo3");
		pkp3.setLearningPeriod(lp);
		pkp4.setName("boo4");
		pkp4.setValue("foo4");
		pkp4.setPreviousValue("previous foo4");
		pkp4.setLearningPeriod(lp);
		lp.setDateFrom(new Date());
		lp.setDateTo(new Date());
		
		return new ArrayList<PeriodKeyPair>(Arrays.asList(pkp1, pkp2, pkp3, pkp4));
	}
	
	private List<LearningUnit> createTestStudyWithinTransaction() {
		// Create and persist a test Account:
		testAccount = accountService.getOrCreateAccount(accountId);
		persistAccount();
		
		// Get the existing transaction:
		transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus transaction = getTransactionManager().getTransaction(transactionDefinition);
		
		// Create LearningUnits:
		List<LearningUnit> study = createLearningUnits();
		
		try {
			// Persist PeriodKeyPairs:
			for (LearningUnit lu : study) {
				em.persist(lu);
			}
			
			em.flush();
			getTransactionManager().commit(transaction);
		} catch (Throwable t) {
			getTransactionManager().rollback(transaction);
			
			return null;
		}
		
		return study;
	}
	
	private List<LearningUnit> createLearningUnits() {
		LearningUnit lu1 = new LearningUnit();
		LearningUnit lu2 = new LearningUnit();
		LearningPeriod lp1 = new LearningPeriod();
		LearningPeriod lp2 = new LearningPeriod();
		
		// Set up learning periods:
		lp1.setDateFrom(new Date());
		lp1.setDateTo(new Date());
		lp2.setDateFrom(new Date());
		lp2.setDateTo(new Date());
		
		// Set up LearningUnit 1:
		lu1.setAccount(testAccount);
		lu1.setAccountId(accountId);
		lu1.setAddDate(new Date());
		lu1.setCampus("boogie campus");
		lu1.setCredit(new BigDecimal(5.5));
		lu1.setDropDate(new Date());
		lu1.setLearningPeriod(lp1);
		lu1.setLevel("Level 1");
		lu1.setStatus("Undefined");
		lu1.setUnitCode("doo");
		lu1.setUnitSection("section 000v");
		lu1.setExtended(new HashSet<KeyPair>(createKeyPairs()));
		
		// Set up LearningUnit 2:
		lu2.setAccount(testAccount);
		lu2.setAccountId(accountId);
		lu2.setAddDate(new Date());
		lu2.setCampus("boogie campus 2");
		lu2.setCredit(new BigDecimal(555.45));
		lu2.setDropDate(new Date());
		lu2.setLearningPeriod(lp2);
		lu2.setLevel("Level 2");
		lu2.setStatus("Undefined 2");
		lu2.setUnitCode("doo2");
		lu2.setUnitSection("section 002v");
		lu2.setExtended(new HashSet<KeyPair>(createKeyPairs()));
		
		return new ArrayList<LearningUnit>(Arrays.asList(lu1, lu2));
	}
	
	private void persistAccount() {
		testAccount = em.merge(testAccount);
		em.persist(testAccount);
	}
	
	private FeeBase createTestFeeBase() {
		// Create test objects:
		List<KeyPair> testStudentData = createTestStudentDataWithinTransaction();
		List<PeriodKeyPair> testPeriodData = createTestPeriodDataWithinTransaction();
		List<LearningUnit> testStudy = createTestStudyWithinTransaction();
		
		// If creation of the test data failed, so be it, nothing to test:
		if ((testStudentData == null) || (testPeriodData == null) || (testStudy == null)) {
			return null;
		}
		
		// Create a new FeeBase:
		FeeBase feeBase = new FeeBase();
		
		feeBase.setAccount(testAccount);
		feeBase.setStudentData(testStudentData);
		feeBase.setPeriodData(testPeriodData);
		feeBase.setStudy(testStudy);
		
		return feeBase;
	}
	
	
	/* **************************************************************
	 * 
	 * Assertion methods.
	 * 
	 * **************************************************************/
	
	private void assertFeeBase(FeeBase expected, FeeBase actual) {
		assertNotNull(actual);
		assertKeyPairs(expected.getStudentData(), actual.getStudentData());
		assertKeyPairs(expected.getPeriodData(), actual.getPeriodData());
		assertStudy(expected.getStudy(), actual.getStudy());
	}
	
	private <T extends KeyPair> void assertKeyPairs(List<T> expected, List<T> actual) {
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		
		// Pre-sort the lists by their natural IDs:
		List<KeyPair> expectedKeyPairs = new ArrayList<KeyPair>(expected);
		List<KeyPair> actualKeyPairs = new ArrayList<KeyPair>(actual);
		KeyPairComparator comparator = new KeyPairComparator();
		
		Collections.sort(expectedKeyPairs, comparator);
		Collections.sort(actualKeyPairs, comparator);
		
		// Inspect each result object:
		for (int i=0,sz=expectedKeyPairs.size(); i<sz; i++) {
			assertNotNull(actualKeyPairs.get(i));
			assertEquals(expectedKeyPairs.get(i).getId(), actualKeyPairs.get(i).getId());
			assertEquals(expectedKeyPairs.get(i).getName(), actualKeyPairs.get(i).getName());
			assertEquals(expectedKeyPairs.get(i).getValue(), actualKeyPairs.get(i).getValue());
			assertEquals(expectedKeyPairs.get(i).getPreviousValue(), actualKeyPairs.get(i).getPreviousValue());
			
			if (actual.get(i) instanceof PeriodKeyPair) {
				assertLearningPeriod(
						PeriodKeyPair.class.cast(expectedKeyPairs.get(i)).getLearningPeriod(), 
						PeriodKeyPair.class.cast(actualKeyPairs.get(i)).getLearningPeriod()
				);
			}
		}
	}
	
	private void assertStudy(List<LearningUnit> expected, List<LearningUnit> actual) {
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		
		// Inspect each result object:
		for (int i=0,sz=expected.size(); i<sz; i++) {
			assertNotNull(actual.get(i));
			assertEquals(expected.get(i).getId(), actual.get(i).getId());
			assertEquals(expected.get(i).getUnitCode(), actual.get(i).getUnitCode());
			assertEquals(expected.get(i).getUnitSection(), actual.get(i).getUnitSection());
			assertEquals(expected.get(i).getCampus(), actual.get(i).getCampus());
			assertEquals(expected.get(i).getLevel(), actual.get(i).getLevel());
			assertTrue(DateUtils.isSameDay(expected.get(i).getAddDate(), actual.get(i).getAddDate()));
			assertTrue(DateUtils.isSameDay(expected.get(i).getDropDate(), actual.get(i).getDropDate()));
			assertLearningPeriod(expected.get(i).getLearningPeriod(), actual.get(i).getLearningPeriod());
			assertEquals(expected.get(i).getCredit(), actual.get(i).getCredit());
			assertEquals(expected.get(i).getStatus(), actual.get(i).getStatus());
			assertEquals(expected.get(i).getAccountId(), actual.get(i).getAccountId());
			assertAccount(expected.get(i).getAccount(), actual.get(i).getAccount());
			assertKeyPairs(new ArrayList<KeyPair>(expected.get(i).getExtended()), new ArrayList<KeyPair>(actual.get(i).getExtended()));
		}
	}
	
	private void assertLearningPeriod(LearningPeriod expected, LearningPeriod actual) {
		if (expected != null) {
			assertNotNull(actual);
			assertEquals(expected.getId(), actual.getId());
			assertTrue(DateUtils.isSameDay(expected.getDateFrom(), actual.getDateFrom()));
			assertTrue(DateUtils.isSameDay(expected.getDateTo(), actual.getDateTo()));
		} else {
			assertNull(actual);
		}
	}
	
	private void assertAccount(Account expected, Account actual) {
		if (expected != null) {
			assertNotNull(actual);
			assertEquals(expected.getId(), actual.getId());
		} else {
			assertNull(actual);
		}
	}
	
	private static class KeyPairComparator implements Comparator<KeyPair> {
		@Override
		public int compare(KeyPair o1, KeyPair o2) {
			return o1.getId().compareTo(o2.getId());
		}
		
	}
}
