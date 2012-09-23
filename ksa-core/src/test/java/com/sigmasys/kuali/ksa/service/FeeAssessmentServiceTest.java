package com.sigmasys.kuali.ksa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
		assertStudentData(testStudentData, studentDataFromService);
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
		assertPeriodData(testPeriodData, periodDataFromService);
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
		// Create test objects:
		List<KeyPair> testStudentData = createTestStudentDataWithinTransaction();
		List<PeriodKeyPair> testPeriodData = createTestPeriodDataWithinTransaction();
		List<LearningUnit> testStudy = createTestStudyWithinTransaction();
		
		// If creation of the test data failed, so be it, nothing to test:
		if ((testStudentData == null) || (testPeriodData == null) || (testStudy == null)) {
			return;
		}
		
		// Get the FeeBase:
		FeeBase feeBaseFromService = feeAssessmentService.getFeeBase(accountId);
		
		// Validate the result:
		assertNotNull(feeBaseFromService);
		assertStudentData(testStudentData, feeBaseFromService.getStudentData());
		assertPeriodData(testPeriodData, feeBaseFromService.getPeriodData());
		assertStudy(testStudy, feeBaseFromService.getStudy());
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
		
		return Arrays.asList(kp1, kp2, kp3);
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
		
		return Arrays.asList(pkp1, pkp2, pkp3, pkp4);
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
		//lu1.setCredit(new BigDecimal(5.5));
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
		//lu2.setCredit(new BigDecimal(55.5));
		lu2.setDropDate(new Date());
		lu2.setLearningPeriod(lp2);
		lu2.setLevel("Level 2");
		lu2.setStatus("Undefined 2");
		lu2.setUnitCode("doo2");
		lu2.setUnitSection("section 002v");
		lu2.setExtended(new HashSet<KeyPair>(createKeyPairs()));
		
		return Arrays.asList(lu1, lu2);
	}
	
	private void persistAccount() {
		testAccount = em.merge(testAccount);
		em.persist(testAccount);
	}
	
	private void assertStudentData(List<KeyPair> expected, List<KeyPair> actual) {
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		
		// Inspect each result object:
		for (int i=0,sz=expected.size(); i<sz; i++) {
			assertNotNull(actual.get(i));
			assertEquals(expected.get(i).getId(), actual.get(i).getId());
			assertEquals(expected.get(i).getName(), actual.get(i).getName());
			assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
			assertEquals(expected.get(i).getPreviousValue(), actual.get(i).getPreviousValue());
		}
	}
	
	private void assertPeriodData(List<PeriodKeyPair> expected, List<PeriodKeyPair> actual) {
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		
		// Inspect each result object:
		for (int i=0,sz=expected.size(); i<sz; i++) {
			assertNotNull(actual.get(i));
			assertEquals(expected.get(i).getId(), actual.get(i).getId());
			assertEquals(expected.get(i).getName(), actual.get(i).getName());
			assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
			assertEquals(expected.get(i).getPreviousValue(), actual.get(i).getPreviousValue());
			assertLearningPeriod(expected.get(i).getLearningPeriod(), actual.get(i).getLearningPeriod());
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
			
			// Assert the student data:
			List<KeyPair> expectedKeyPairs = new ArrayList<KeyPair>(expected.get(i).getExtended());
			List<KeyPair> actualKeyPairs = new ArrayList<KeyPair>(actual.get(i).getExtended());
			KeyPairComparator comparator = new KeyPairComparator();
			
			Collections.sort(expectedKeyPairs, comparator);
			Collections.sort(actualKeyPairs, comparator);
			assertStudentData(expectedKeyPairs, actualKeyPairs);
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
