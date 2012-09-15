package com.sigmasys.kuali.ksa.service;

import java.util.List;

import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;
import com.sigmasys.kuali.ksa.service.support.FeeBase;

/**
 * This interface represents a service object to work with Fee Assessments of ChargeableAccounts. 
 * The methods of this interface give an ability to load the necessary information to start
 * assessing courses taken on a particular account and also load account specific student and period data. 
 * This interface includes a convenience method "getFeeBase" that creates a holder class that contains 
 * all pertinent information in addition to individual data access methods. 
 * 
 * @author Sergey
 * @version 1.0
 */
public interface FeeAssessmentService {

	/**
	 * Returns an account's Set of student data in form of KeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its student data.
	 * @return Account's student data.
	 */
	List<KeyPair> getStudentData(String accountId);
	
	/**
	 * Returns an account's Set of learning period data in form of PeriodKeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its learning period data.
	 * @return Account's learning period data.
	 */
	List<PeriodKeyPair> getLearningPeriodData(String accountId);
	
	/**
	 * Returns an account's study data in form of PeriodKeyPair objects.
	 * 
	 * @param accountId Id of an account for which to get its study data.
	 * @return Account's study data.
	 */
	List<LearningUnit> getStudy(String accountId);
	
	/**
	 * Returns a {@link FeeBase} object containing all information necessary for a 
	 * fee assessment process for an account. 
	 * 
	 * @param accountId Id of an account for which to retrieve its fee assessment data. 
	 * @return FeeBase An object containing all account's fee assessment data. 
	 */
	FeeBase getFeeBase(String accountId);
}
