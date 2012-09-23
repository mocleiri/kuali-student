package com.sigmasys.kuali.ksa.service.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.KeyPairType;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;
import com.sigmasys.kuali.ksa.service.FeeAssessmentService;
import com.sigmasys.kuali.ksa.service.support.FeeBase;

@Service("feeAssessmentService")
@Transactional(readOnly=true)
@SuppressWarnings("unchecked")
public class FeeAssessmentServiceImpl extends GenericPersistenceService implements FeeAssessmentService {
		

	/**
	 * Returns an account's Set of student data in form of KeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its student data.
	 * @return Account's student data.
	 */
	@Override
	public List<KeyPair> getStudentData(String accountId) {
		return findKeyPairs(accountId, KeyPair.class, KeyPairType.KEY_PAIR);
	}

	/**
	 * Returns an account's Set of learning period data in form of PeriodKeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its learning period data.
	 * @return Account's learning period data.
	 */
	@Override
	public List<PeriodKeyPair> getLearningPeriodData(String accountId) {
		return findKeyPairs(accountId, PeriodKeyPair.class, KeyPairType.PERIOD_KEY_PAIR);
	}

	/**
	 * Returns an account's study data in form of PeriodKeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its study data.
	 * @return Account's study data.
	 */
	@Override
	public List<LearningUnit> getStudy(String accountId) {
		// Find associated LearningUnits using a query by a foreign key in the LearningUnit table:
		String sql = "select * from kssa_lu lu where lu.acnt_id_fk = :accountId";
		Query query = em.createNativeQuery(sql, LearningUnit.class).setParameter("accountId", accountId);
		List<LearningUnit> result = query.getResultList();
		
		return result;
	}

	/**
	 * Returns a {@link FeeBase} object containing all information necessary for a 
	 * fee assessment process for an account. 
	 * 
	 * @param accountId Id of an account for which to retrieve its fee assessment data. 
	 * @return FeeBase An object containing all account's fee assessment data. 
	 */
	@Override
	public FeeBase getFeeBase(String accountId) {
		// Retrieve the Account and other data:
		Account account = getEntity(accountId, Account.class);
		List<KeyPair> studentData = getStudentData(accountId);
		List<PeriodKeyPair> periodData = getLearningPeriodData(accountId);
		List<LearningUnit> study = getStudy(accountId);
		
		// Build the resulting object:
		FeeBase feeBase = new FeeBase();

		feeBase.setAccount(account);
		feeBase.setStudentData(studentData);
		feeBase.setPeriodData(periodData);
		feeBase.setStudy(study);
		
		return feeBase;
	}
	
	/**
	 * Loads either of the KeyPair type or sub-type object associated with an account with the given ID.
	 * 
	 * @param accountId Id of an account for which to load pairs.
	 * @param resultClass Class of an object to load.
	 * @return Associated objects.
	 */
	private <T extends KeyPair> List<T> findKeyPairs(String accountId, Class<T> resultClass, KeyPairType keyPairType) {
		// Since associations between Account and KeyPair/PeriodKeyPair are not defined
		// neither in Account nor KeyPair/PeriodKeyPair, we have to load them using 
		// the physical association table join.
		String sql = "select kp.* from kssa_kypr kp, kssa_acnt_kypr akp where kp.id = akp.kypr_id_fk and akp.acnt_id_fk = :accountId and kp.type = :keypairType";
		Query query = em.createNativeQuery(sql, resultClass).setParameter("accountId", accountId).setParameter("keypairType", keyPairType.getCode());
		List<T> result = query.getResultList();

		return result;
	}
}
