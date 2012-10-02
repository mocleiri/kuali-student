package com.sigmasys.kuali.ksa.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.KeyPairType;
import com.sigmasys.kuali.ksa.model.LearningPeriod;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;
import com.sigmasys.kuali.ksa.service.FeeManagementService;
import com.sigmasys.kuali.ksa.service.support.FeeBase;

@Service("feeManagementService")
@Transactional(readOnly=true)
@SuppressWarnings("unchecked")
public class FeeManagementServiceImpl extends GenericPersistenceService implements FeeManagementService {
		
	/**
	 * The logger.
	 */
	private static final Log logger = LogFactory.getLog(FeeManagementServiceImpl.class);


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

	@Override
	public BigDecimal calculateFees(FeeBase feeBase, LearningPeriod period) {
		// TODO Auto-generated method stub
		return new BigDecimal(0);
	}
	
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
	@Override
	public BigDecimal calcluateChargeByCreditToMax(int numOfCredits, BigDecimal amountPerCredit, BigDecimal maxAmount) {
		// Validate the input:
		if ((amountPerCredit == null) || (maxAmount == null)) {
			throw new IllegalArgumentException("Amount Per Credit and Max Amount cannot be null.");
		}
		
		BigDecimal totalCharge = amountPerCredit.multiply(new BigDecimal(numOfCredits));
		
		return (maxAmount.doubleValue() < 0d) ? totalCharge : totalCharge.min(maxAmount);
	}

	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>FeeBase</code>
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @return The newly created <code>KeyPair</code>.
	 */
	@Override
	public KeyPair createKeyPair(FeeBase feeBase, String name, String value) {
		return createFeeBaseKeyPairInternal(feeBase, name, value, null, KeyPair.class);
	}

	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>FeeBase</code>
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @param period The learning period.
	 * @return The newly created <code>KeyPair</code>.
	 */
	@Override
	public PeriodKeyPair createKeyPair(FeeBase feeBase, String name, String value, LearningPeriod period) {
		return createFeeBaseKeyPairInternal(feeBase, name, value, period, PeriodKeyPair.class);
	}

	/**
	 * Creates a new <code>KeyPair</code> object for the specified <code>LearningUnit</code>
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name Name of the new <code>KeyPair</code>.
	 * @param value Value of the new <code>KeyPair</code>.
	 * @return The newly created <code>KeyPair</code>.
	 */
	@Override
	public KeyPair createKeyPair(LearningUnit learningUnit, String name, String value) {
		// Validate the input:
		validateInputParameters(learningUnit, name, value);
		validateKeyPairNameUnique(learningUnit, name);
		
		try {
			// Create and persist a new KeyPair:
			KeyPair newKeyPair = createKeyPairInternal(name, value, null, KeyPair.class);
			
			// Update the associations:
			persistEntity(learningUnit);
			
			// Flush the EM to the persistent storage:
			em.flush();
			
			// Add the new KeyPair to the LearningUnit's list of KeyPairs:
			learningUnit.getExtended().add(newKeyPair);
			
			return newKeyPair;
		} catch (Throwable t) {
			// Log error:
			logger.error("Error creating a KeyPair for a LearningPeriod.", t);
			
			// Re-throw the error:
			throw new RuntimeException("Error creating a KeyPair for a LearningPeriod.", t);
		}
	}

	/**
	 * Returns the value of a <code>KeyPair</code> with the specified name within the given <code>FeeBase</code>. 
	 * Returns <code>null</code> if there is such <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object associated with an account.
	 * @param name Name of a <code>KeyPair</code> which value to retrieve.
	 * @return The value of a <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>
	 * 	or <code>null</code> is such a name does not exist.
	 */
	@Override
	public String getKeyPairValue(FeeBase feeBase, String name) {
		// Validate the input:
		validateInputParameters(feeBase, name);
		
		// Create an execute a query:
		Query query = em.createNativeQuery("select kp.value from kssa_kypr kp, kssa_acnt_kypr akp where kp.id = akp.kypr_id_fk and akp.acnt_id_fk = :accountId and kp.name = :keypairName")
				.setParameter("accountId", feeBase.getAccount().getId()).setParameter("keypairName", name);
		List<String> result = query.getResultList();
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}

	/**
	 * Returns the value of a <code>KeyPair</code> with the specified name within the given <code>LearningUnit</code>. 
	 * Returns <code>null</code> if there is such <code>KeyPair</code> with the given name in the specified <code>FeeBase</code>.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name Name of a <code>KeyPair</code> which value to retrieve.
	 * @return The value of a <code>KeyPair</code> with the given name in the specified <code>LearningUnit</code>
	 * 	or <code>null</code> is such a name does not exist.
	 */
	@Override
	public String getKeyPairValue(LearningUnit learningUnit, String name) {
		// Validate the input:
		validateInputParameters(learningUnit, name);
		
		// Merge the entity to attach it to the persistent context if it is detached:
		if (!em.contains(learningUnit)) {
			learningUnit = em.merge(learningUnit);
		}
		
		// Iterate through the "extended" attribute of the "learningUnit"
		// to find the one that has the "name" attribute equals to the argument:
		for (KeyPair keyPair : learningUnit.getExtended()) {
			if (StringUtils.equalsIgnoreCase(keyPair.getName(), name)) {
				return keyPair.getValue();
			}
		}
		
		return null;
	}

	/**
	 * Updates the <code>KeyPair</code> with the specified name with a new value.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param newValue The new <code>KeyPair</code> value.
	 */
	@Override
	public void updateKeyPair(FeeBase feeBase, String name, String newValue) {
		// Validate the input:
		validateInputParameters(feeBase, name, newValue, null, KeyPair.class);
		
		// Update the KeyPair value:
		updateKeyPairInternal(feeBase.getStudentData(), name, newValue, null, KeyPair.class);
	}

	/**
	 * Updates the <code>PeriodKeyPair</code> with the specified name with a new value and/or a new <code>LearningPeriod</code>.
	 * If either <code>newValue</code> or <code>newPeriod</code> is <code>null</code>, it's not updated. 
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>PeriodKeyPair</code> name.
	 * @param newValue New <code>PeriodKeyPair</code> value.
	 * @param newPeriod New <code>Learning</code> period.
	 */
	@Override
	public void updateKeyPair(FeeBase feeBase, String name, String newValue, LearningPeriod newPeriod) {
		// Validate the input:
		validateInputParameters(feeBase, name, newValue, newPeriod, PeriodKeyPair.class);
		
		// Update the KeyPair value:
		updateKeyPairInternal(feeBase.getPeriodData(), name, newValue, newPeriod, PeriodKeyPair.class);
	}

	/**
	 * Updates the <code>KeyPair</code> with the specified name with a new value.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 * @param newValue The new <code>KeyPair</code> value.
	 */
	@Override
	public void updateKeyPair(LearningUnit learningUnit, String name, String newValue) {
		// Validate the input:
		validateInputParameters(learningUnit, name, newValue);
		
		// Update the KeyPair value:
		updateKeyPairInternal(learningUnit.getExtended(), name, newValue, null, KeyPair.class);
	}

	/**
	 * Removes a <code>KeyPair</code> with the specified name from a <code>FeeBase</code>.
	 * 
	 * @param feeBase A <code>FeeBase</code> object.
	 * @param name <code>KeyPair</code> name.
	 */
	@Override
	public void removeKeyPair(FeeBase feeBase, String name) {
		// Validate the input:
		validateInputParameters(feeBase, name);
		
		// Find the KeyPair to remove in "studentData":
		List<? extends KeyPair> removeFrom = feeBase.getStudentData();
		KeyPair removeKeyPair = getKeyPairInternal(removeFrom, name);
		
		// If not found in the "studentData", find in the "periodData":
		if (removeKeyPair == null) {
			removeFrom = feeBase.getPeriodData();
			removeKeyPair = getKeyPairInternal(removeFrom, name);
		}
		
		// If a KeyPair was found and removed, delete the entity and disassociate the Account and KeyPair:
		if (removeKeyPair != null) {
			try {
				// Delete the entity:
				em.remove(removeKeyPair);
				
				// Remove the association record:
				removeKeyPairAssociationRecord(feeBase.getAccount(), removeKeyPair);
				
				// Flush the EM to the persistent storage:
				em.flush();
				
				// Remove from the Collection:
				removeFrom.remove(removeKeyPair);
			} catch (Throwable t) {
				// Log error:
				logger.error("Error removing KeyPair.", t);
				
				// Re-throw the error:
				throw new RuntimeException(t);
			}
		}
	}

	/**
	 * Removes a <code>KeyPair</code> with the specified name from a LearningUnit</code>.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> object.
	 * @param name <code>KeyPair</code> name.
	 */
	@Override
	public void removeKeyPair(LearningUnit learningUnit, String name) {
		// Validate input:
		validateInputParameters(learningUnit, name);
		
		// Remove the KeyPair with the given name from the "extended" Set:
		removeKeyPairInternal(learningUnit.getExtended(), name);
		
		// Update the persistent context:
		persistEntity(learningUnit);
	}

	/**
	 * Checks if the given FeeBase contains a KeyPair or its subtype with the given name.
	 * 
	 * @param feeBase A FeeBase to check.
	 * @param name Name of a KeyPair to locate within the FeeBase.
	 * @return <code>true</code> if a KeyPair or its subtype with the given name exists within the specified FeeBase.
	 * 	Returns <code>false</code> otherwise.
	 */
	@Override
	public boolean containsKeyPair(FeeBase feeBase, String name) {
		// Validate input:
		validateInputParameters(feeBase, name);
		
		return containsKeyPairInternal(feeBase.getStudentData(), name) 
				|| containsKeyPairInternal(feeBase.getPeriodData(), name);
	}

	/**
	 * Checks if the given LearningUnit contains a KeyPair or its subtype with the given name.
	 * 
	 * @param learningUnit A LearningUnit to check.
	 * @param name Name of a KeyPair to locate within the LearningUnit.
	 * @return <code>true</code> if a KeyPair with the given name exists within the specified LearningUnit.
	 * 	Returns <code>false</code> otherwise.
	 */
	@Override
	public boolean containsKeyPair(LearningUnit learningUnit, String name) {
		// Validate input:
		validateInputParameters(learningUnit, name);
		
		return containsKeyPairInternal(learningUnit.getExtended(), name);
	}

	/**
	 * Saves a <code>LearningUnit</code>. This method is helpful when making modifications to a <code>LearningUnit</code>, such as setting new Status,
	 * changing details, such as Campus, Add Date, Drop Date, etc.
	 * 
	 * @param learningUnit A <code>LearningUnit</code> to be updated. 
	 * @returns The entity that was attached to the persistent context.
	 */
	@Override
	public void saveLearningUnit(LearningUnit learningUnit) {
		// Validate input:
		validateInputParameters(learningUnit);
		
		// Persist the entity
		persistEntity(learningUnit);
	}

	@Override
	public LearningPeriod getCurrentPeriod() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Finds all <code>LearningPeriod</code>s that fall into the specified date range.
	 * 
	 * @param dateFrom Beginning of the search date range.
	 * @param dateTo End of the search date range.
	 * @return All <code>LearningPeriod</code> objects that fall into the given date range.
	 */
	@Override
	public List<LearningPeriod> findLearningPeriods(Date dateFrom, Date dateTo) {
		// Create a Query:
		Query query = em.createQuery("select lp from LearningPeriod lp where lp.dateFrom >= :dateFrom and lp.dateTo <= :dateTo")
				.setParameter("dateFrom", dateFrom)
				.setParameter("dateTo", dateTo);
		
		return query.getResultList();
	}
	
	
	/* ***********************************************************
	 * 
	 * Utility methods.
	 * 
	 * ***********************************************************/
	
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

	/**
	 * A generic method to create a new KeyPair or its subclass for a FeeBase. 
	 * 
	 * @param feeBase A FeeBase for which to create a KeyPair.
	 * @param name The "name" property of the new object.
	 * @param value The "value" property of the new object.
	 * @param period The "period" property of the new object, only if the type contains a <code>LearningPeriod</code>.
	 * @param entityClass The type of the new object to create.
	 * @return The newly created object.
	 */
	private <T extends KeyPair> T createFeeBaseKeyPairInternal(FeeBase feeBase, String name, String value, LearningPeriod period, Class<T> entityClass) {
		// Validate the input:
		validateInputParameters(feeBase, name, value, period, entityClass);
		validateKeyPairNameUnique(feeBase, name);
		
		try {
			// Create and persist a new KeyPair:
			T newKeyPair = createKeyPairInternal(name, value, period, entityClass);
			
			// Create an association record:
			createKeyPairAssociationRecord(feeBase.getAccount(), newKeyPair);
			
			// Flush the EM to the persistent storage:
			em.flush();
			
			// Add the new KeyPair to an appropriate list in FeeBase:
			if (entityClass.equals(PeriodKeyPair.class)) {
				feeBase.getPeriodData().add(PeriodKeyPair.class.cast(newKeyPair));
			} else {
				feeBase.getStudentData().add(newKeyPair);
			}
			
			return newKeyPair;
		} catch (Throwable t) {
			// Log an error:
			logger.error("Error creating a new KeyPair object.", t);
			
			// Re-throw the error:
			throw new RuntimeException("Error creating a new KeyPair object.", t);
		}
	}
	
	/**
	 * Creates an instance of either <code>KeyPair</code> or one of its subclasses of the given type.
	 * 
	 * @param name The "name" property of the new object.
	 * @param value The "value" property of the new object.
	 * @param period The "period" property of the new object, only if the type contains a <code>LearningPeriod</code>.
	 * @param entityClass The type of the new object to create.
	 * @return The newly created object.
	 */
	private <T extends KeyPair> T createKeyPairInternal(String name, String value, LearningPeriod period, Class<T> entityClass) throws Exception {
		// Create a new instance:
		boolean isPeriodKeyPair = entityClass.equals(PeriodKeyPair.class);
		T keyPair = entityClass.newInstance();
		
		// Set common properties:
		keyPair.setName(name);
		keyPair.setValue(value);
		
		// Set PeriodKeyPair properties:
		if (isPeriodKeyPair) {
			PeriodKeyPair.class.cast(keyPair).setLearningPeriod(period);
		}
		
		// Persist the new instance:
		persistEntity(keyPair);
		
		return keyPair;
	}
	
	/**
	 * Creates an association record in the KSSA_ACNT_KYPR table.
	 * 
	 * @param account Account to associate with a KeyPair.
	 * @param keyPair KeyPair to associate with an Account.
	 * @throws Exception If there are errors creating a new association record.
	 */
	private void createKeyPairAssociationRecord(Account account, KeyPair keyPair) throws Exception {
		createKeyPairAssociationRecord(account.getId(), keyPair);
	}
	
	/**
	 * Creates an association record in the KSSA_ACNT_KYPR table.
	 * 
	 * @param accountId Account ID to associate with a KeyPair.
	 * @param keyPair KeyPair to associate with an Account ID.
	 * @throws Exception If there are errors creating a new association record.
	 */
	private void createKeyPairAssociationRecord(String accountId, KeyPair keyPair) throws Exception {
		// Create and execute an INSERT statement:
		Query query = em.createNativeQuery("insert into kssa_acnt_kypr (acnt_id_fk, kypr_id_fk) values(:accountId, :keypairId)")
				.setParameter("accountId", accountId).setParameter("keypairId", keyPair.getId());
		
		query.executeUpdate();
	}
	
	/**
	 * Removes an association record from the KSSA_ACNT_KYPR table.
	 * 
	 * @param account Account to disassociate from the given KeyPair.
	 * @param keyPair KeyPair to disassociate from the given Account.
	 * @throws Exception If a persistent storage operation error occurs.
	 */
	private void removeKeyPairAssociationRecord(Account account, KeyPair keyPair) throws Exception {
		removeKeyPairAssociationRecord(account.getId(), keyPair);
	}
	
	/**
	 * Removes an association record from the KSSA_ACNT_KYPR table.
	 * 
	 * @param account Account ID to disassociate from the given KeyPair.
	 * @param keyPair KeyPair to disassociate from the given Account ID.
	 * @throws Exception If a persistent storage operation error occurs.
	 */
	private void removeKeyPairAssociationRecord(String accountId, KeyPair keyPair) throws Exception {
		// Create an execute a DELETE statement:
		Query query = em.createNativeQuery("delete from kssa_acnt_kypr where acnt_id_fk = :accountId and kypr_id_fk = :keypairId")
				.setParameter("accountId", accountId).setParameter("keypairId", keyPair.getId());
		
		query.executeUpdate();
	}
	
	/**
	 * Updates a KeyPair with the specified name with the new "value" and optionally with the new "period" properties
	 * depending on the type of the instance to be updated. 
	 * 
	 * @param keyPairs A Collection of KeyPair or its subclasses.
	 * @param name The "name" attribute of a KeyPair to update.
	 * @param newValue The new "value" attribute.
	 * @param newPeriod The new "period" attribute for PeriodKeyPairs.
	 * @param keyPairType Type of the KeyPair.
	 */
	private <T extends KeyPair> void updateKeyPairInternal(Collection<T> keyPairs, String name, String newValue, LearningPeriod newPeriod, Class<T> keyPairType) {
		// Get the KeyPairs, find the one with the given name and set the new value:
		boolean isPeriodKeyPair = keyPairType.equals(PeriodKeyPair.class);
		
		for (T keyPair : keyPairs) {
			if (StringUtils.equalsIgnoreCase(keyPair.getName(), name)) {
				// Set the new values:
				keyPair.setValue(newValue);
				
				if (isPeriodKeyPair) {
					PeriodKeyPair.class.cast(keyPair).setLearningPeriod(newPeriod);
				}
				
				// Persist the entity:
				persistEntity(keyPair);
				
				break;
			}
		}
		
	}
	
	/**
	 * Removes a KeyPair or its subtype with the specified "name" from the given Collection.
	 * Does NOT remove from the Persistent Store.
	 * 
	 * @param keyPairs A Collection of KeyPairs to remove from.
	 * @param name The "name" property of the KeyPair to remove from the given Collection.
	 * @return The KeyPair that has been removed or <code>null</code> if no such KeyPair with 
	 * the specified "name" attribute exists in the given Collection.
	 */
	private <T extends KeyPair> T removeKeyPairInternal(Collection<T> keyPairs, String name) {
		T keyPair = getKeyPairInternal(keyPairs, name);
		
		if (keyPair != null) {
			keyPairs.remove(keyPair);
		}
		
		return keyPair;
	}
	
	/**
	 * Checks if a KeyPair with the specified name exists in the given Collection.
	 * 
	 * @param keyPairs A Collection of KeyPairs to check.
	 * @param name Name of a KeyPair to check.
	 * @return true if exists, false otherwise.
	 */
	private <T extends KeyPair> boolean containsKeyPairInternal(Collection<T> keyPairs, String name) {
		return (getKeyPairInternal(keyPairs, name) != null);
	}
	
	/**
	 * Finds a KeyPair with the specified name within the given Collection.
	 * 
	 * @param keyPairs A Collection of KeyPairs.
	 * @param name Name of a KeyPair to find.
	 * @return The KeyPair with the given name found in the given Collection or <code>null</code> 
	 * 	if a KeyPair with such a name does not exist.
	 */
	private <T extends KeyPair> T getKeyPairInternal(Collection<T> keyPairs, String name) {
		for (T keyPair : keyPairs) {
			if (StringUtils.equalsIgnoreCase(keyPair.getName(), name)) {
				return keyPair;
			}
		}
		
		return null;
	}
	
	
	/* *****************************************************************************
	 * 
	 * Parameter validation methods.
	 * 
	 * *****************************************************************************/
	
	private void validateInputParameters(FeeBase feeBase, String name, String value, LearningPeriod period, Class<? extends KeyPair> keyPairType) {
		validateInputParameters(feeBase, name);
		
		boolean isPeriodKeyPair = keyPairType.equals(PeriodKeyPair.class);
		
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("KeyPair.value property cannot be null.");
		}
		
		if (isPeriodKeyPair && (period == null)) {
			throw new IllegalArgumentException("PeriodKeyPair.period property cannot be null.");
		}
	}
	
	private void validateInputParameters(FeeBase feeBase, String name) {
		if ((feeBase == null) || (feeBase.getAccount() == null)) {
			throw new IllegalArgumentException("FeeBase.account or Account.id property cannot be null.");
		}
		
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("KeyPair.name property cannot be null.");
		}
	}
	
	private void validateInputParameters(LearningUnit learningUnit, String name, String value) {
		validateInputParameters(learningUnit, name);
		
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("KeyPair.value property cannot be null.");
		}
	}
	
	private void validateInputParameters(LearningUnit learningUnit, String name) {
		validateInputParameters(learningUnit);
		
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("KeyPair.name property cannot be null.");
		}
	}
	
	private void validateInputParameters(LearningUnit learningUnit) {
		if (learningUnit == null) {
			throw new IllegalArgumentException("LearningUnit cannot be null.");
		}
	}
	
	private void validateKeyPairNameUnique(FeeBase feeBase, String name) {
		if (containsKeyPairInternal(feeBase.getStudentData(), name)
				|| containsKeyPairInternal(feeBase.getPeriodData(), name)) {
			throw new IllegalArgumentException("Such KeyPair name [" + name + "] already exists for Account with ID [" + feeBase.getAccount().getId() + "].");
		}
	}
	
	private void validateKeyPairNameUnique(LearningUnit learningUnit, String name) {
		if (containsKeyPairInternal(learningUnit.getExtended(), name)) {
			throw new IllegalArgumentException("Such KeyPair name [" + name + "] already exists for LearningUnit with code [" + learningUnit.getUnitCode() + "].");
		}
	}
}
