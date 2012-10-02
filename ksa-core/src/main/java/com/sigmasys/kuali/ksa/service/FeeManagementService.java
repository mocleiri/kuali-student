package com.sigmasys.kuali.ksa.service;

import java.math.BigDecimal;
import java.util.Date;
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
public interface FeeManagementService {

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
	BigDecimal calculateFees(FeeBase feeBase, LearningPeriod period);
	
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
	BigDecimal calcluateChargeByCreditToMax(int numOfCredits, BigDecimal amountPerCredit, BigDecimal maxAmount);
	
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
	PeriodKeyPair createKeyPair(FeeBase feeBase, String name, String value, LearningPeriod period);
	
	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>LearningUnit</code>
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @return The newly created <code>KeyPair</code>.
	 */
	KeyPair createKeyPair(LearningUnit learningUnit, String name, String value);
	
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
	 */
	void removeKeyPair(FeeBase feeBase, String name);
	
	/**
	 * Removes a <code>KeyPair</code> with the specified name from a LearningUnit</code>.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name <code>KeyPair</code> name.
	 */
	void removeKeyPair(LearningUnit learningUnit, String name);
	
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
	void updateKeyPair(FeeBase feeBase, String name, String newValue, LearningPeriod newPeriod);
	
	/**
	 * Updates the <code>KeyPair</code> with the specified name with a new value.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param newValue The new <code>KeyPair</code> value.
	 */
	void updateKeyPair(LearningUnit learningUnit, String name, String newValue);
	
	/**
	 * Checks if the given FeeBase contains a KeyPair or its subtype with the given name.
	 * 
	 * @param feeBase A FeeBase to check.
	 * @param name Name of a KeyPair to locate within the FeeBase.
	 * @return <code>true</code> if a KeyPair or its subtype with the given name exists within the specified FeeBase.
	 * 	Returns <code>false</code> otherwise.
	 */
	boolean containsKeyPair(FeeBase feeBase, String name);
	
	/**
	 * Checks if the given LearningUnit contains a KeyPair or its subtype with the given name.
	 * 
	 * @param learningUnit A LearningUnit to check.
	 * @param name Name of a KeyPair to locate within the LearningUnit.
	 * @return <code>true</code> if a KeyPair with the given name exists within the specified LearningUnit.
	 * 	Returns <code>false</code> otherwise.
	 */
	boolean containsKeyPair(LearningUnit learningUnit, String name);
	
	/**
	 * Saves a <code>LearningUnit</code>. This method is helpful when making modifications to a <code>LearningUnit</code>, such as setting new Status,
	 * changing details, such as Campus, Add Date, Drop Date, etc.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> to be updated. 
	 * @returns The entity that was attached to the persistent context.
	 */
	void saveLearningUnit(LearningUnit learningUnit);
	
	/**
	 * Returns the period that the rules are currently working on.
	 * 
	 * @return The period that the rules are currently working on.
	 */
	LearningPeriod getCurrentPeriod();
	
	/**
	 * Finds all <code>LearningPeriod</code>s that fall into the specified date range.
	 * 
	 * @param dateFrom Beginning of the search date range.
	 * @param dateTo End of the search date range.
	 * @return All <code>LearningPeriod</code> objects that fall into the given date range.
	 */
	List<LearningPeriod> findLearningPeriods(Date dateFrom, Date dateTo);
}
