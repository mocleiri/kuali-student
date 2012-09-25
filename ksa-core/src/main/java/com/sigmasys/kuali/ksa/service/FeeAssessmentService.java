package com.sigmasys.kuali.ksa.service;

import java.util.List;

import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.LearningPeriod;
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
	
	/**
	 * Calculates fees for the given account for during the specified period.
	 * 
	 * @param feeBase FeeBase used for assessing the fees.
	 * @param period Period during which to calculate fees. 
	 * @return The total fees amount.
	 */
	double calculateFees(FeeBase feeBase, LearningPeriod period);
	
	/**
	 * Calculates the total payment amount for the given amount of credits not to exceed 
	 * the maximum amount <code>maxAmount</code>. If <code>maxAmount</code> is equal to <code>-1</code>, 
	 * there is no total amount limit.
	 * 
	 * @param numOfCredits Amount of credits.
	 * @param amountPerCredit Cost of each credit.
	 * @param maxAmount Maximum total payment cap. 
	 * @return The total payment limited by <code>maxAmount</code> or the total amount if <code>maxAmount</code> is <code>-1</code>.
	 */
	double calcluateChargeByCreditToMax(int numOfCredits, double amountPerCredit, double maxAmount);
	
	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>FeeBase</code>
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @return The newly created <code>KeyPair</code>.
	 */
	KeyPair createKeyPair(FeeBase feeBase, String name, String value);
	
	/**
	 * Creates a new <code>PeriodKeyPair</code> object for the specified <code>FeeBase</code>
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of the new <code>PeriodKeyPair</code>.
	 * @param value Value of the new <code>PeriodKeyPair</code>.
	 * @param period A <code>LearningPeriod</code> to be associated with the new <code>KeyPair</code>.
	 * @return The newly created <code>PeriodKeyPair</code>.
	 */
	PeriodKeyPair createPeriodKeyPair(FeeBase feeBase, String name, String value, LearningPeriod period);
	
	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>LearningUnit</code>
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @return The newly created <code>KeyPair</code>.
	 */
	KeyPair createLearningUnitKeyPair(LearningUnit learningUnit, String name, String value);
	
	/**
	 * Returns the value of a <code>KeyPair</code> with the specified name within the given <code>FeeBase</code>. 
	 * Returns <code>null</code> if there is such <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of a <code>KeyPair</code> which value to retrieve.
	 * @return The value of a <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>
	 * 	or <code>null</code> is such a name does not exist.
	 */
	String getKeyPairValue(FeeBase feeBase, String name);
	
	/**
	 * Returns the value of a <code>PeriodKeyPair</code> with the specified name for the specified <code>LearningPeriod</code>. 
	 * Returns <code>null</code> if there is such <code>PeriodKeyPair</code> with the given name in the specified <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of a <code>KeyPair</code> which value to retrieve.
	 * @param period Learning period.
	 * @return The value of a <code>PeriodKeyPair</code> with the given name and period in the specified <code>FeeBase</code>
	 * 	or <code>null</code> is such a name does not exist.
	 */
	String getKeyPairValue(FeeBase feeBase, String name, LearningPeriod period);
	
	/**
	 * Returns the value of a <code>KeyPair</code> with the specified name within the given <code>LearningUnit</code>. 
	 * Returns <code>null</code> if there is such <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name Name of a <code>KeyPair</code> which value to retrieve.
	 * @return The value of a <code>KeyPair</code> with the given name in the specified <code>LearningUnit</code>
	 * 	or <code>null</code> is such a name does not exist.
	 */
	String getKeyPairValue(LearningUnit learningUnit, String name);
	
	/**
	 * Removes a <code>KeyPair</code> with the specified name from a <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param value <code>KeyPair</code> value.
	 */
	void removeKeyPair(FeeBase feeBase, String name, String value);
	
	/**
	 * Removes a <code>PeriodKeyPair</code> with the specified name and period from a <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>PeriodKeyPair</code> name.
	 * @param value <code>PeriodKeyPair</code> value.
	 * @param period <code>Learning</code> period.
	 */
	void removePeriodKeyPair(FeeBase feeBase, String name, String value, LearningPeriod period);
	
	/**
	 * Removes a <code>KeyPair</code> with the specified name from a LearningUnit</code>.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param value <code>KeyPair</code> value.
	 */
	void removeLearningUnitKeyPair(LearningUnit learningUnit, String name, String value);
	
	/**
	 * Updates the <code>KeyPair</code> with the specified name with a new value.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param newValue The new <code>KeyPair</code> value.
	 */
	void updateKeyPair(FeeBase feeBase, String name, String newValue);
	
	/**
	 * Updates the <code>PeriodKeyPair</code> with the specified name with a new value and/or a new <code>LearningPeriod</code>.
	 * If either <code>newValue</code> or <code>newPeriod</code> is <code>null</code>, it's not updated. 
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>PeriodKeyPair</code> name.
	 * @param newValue New <code>PeriodKeyPair</code> value.
	 * @param newPeriod New <code>Learning</code> period.
	 */
	void updatePeriodKeyPair(FeeBase feeBase, String name, String newValue, LearningPeriod newPeriod);
	
	/**
	 * Updates the <code>KeyPair</code> with the specified name with a new value.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param newValue The new <code>KeyPair</code> value.
	 */
	void updateLearningUnitKeyPair(LearningUnit learningUnit, String name, String newValue);
	
	/**
	 * Saves a <code>LearningUnit</code>. This method is helpful when making modifications to a <code>LearningUnit</code>, such as setting new Status,
	 * changing details, such as Campus, Add Date, Drop Date, etc.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> to be updated. 
	 */
	void saveLearningUnit(LearningUnit learningUnit);
	
	/**
	 * Returns the period that the rules are currently working on.
	 * 
	 * @return The period that the rules are currently working on.
	 */
	LearningPeriod getCurrentPeriod();
}
