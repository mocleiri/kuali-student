package com.sigmasys.kuali.ksa.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.model.LearningPeriod;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;
import com.sigmasys.kuali.ksa.service.AccountImportService;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.FeeManagementService;
import com.sigmasys.kuali.ksa.model.FeeBase;
import com.sigmasys.kuali.ksa.transform.KeyPair;
import com.sigmasys.kuali.ksa.transform.StudentProfile;

@Service("accountImportService")
@Transactional(readOnly=false)
public class AccountImportServiceImpl implements AccountImportService {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private FeeManagementService feeManagementService;
	

	/**
	 * Performs import of a student profile. 
	 * Refer to the "Process Diagrams" design document for a detailed depiction of the process and logic.
	 * 
	 * This method assumes that un-marshalling of an XML file containing a Student Account has already been
	 * performed and the schema validation has succeeded at the time this method is being invoked. 
	 * 
	 * @param studentProfile A student's profile object. 
	 * @throws IllegalArgumentException If "studentProfile" is invalid.
	 * @throws RuntimeException If student profile import encountered an error.
	 */
	@Override
	public void importStudentProfile(StudentProfile studentProfile) {
		// Step 1: Validating the XML schema is done prior to invoking this method by the JAX-WS framework
		
		// Step 2: Validating the account
		String accountId = studentProfile.getAccountIdentifier();
		boolean accountExists = accountService.accountExists(accountId);
		
		if (!accountExists) {
			throw new UserNotFoundException("Account does not exist, ID = " + accountId);
		}
		
		// Retrieve a new FeeBase for the now validated Account with ID "accountId":
		FeeBase feeBase = feeManagementService.getFeeBase(accountId);
		
		// Step 3a: Import StudentProfile.StudentInformation
		StudentProfile.StudentInformation studentInfo = studentProfile.getStudentInformation();
		
		createKeyPair(feeBase, "citizenship", studentInfo.getCitizenship());
		createKeyPair(feeBase, "residency", studentInfo.getResidency());
		createKeyPair(feeBase, "year-of-study", studentInfo.getYearOfStudy());
		createKeyPair(feeBase, "level-of-study", ObjectUtils.toString(studentInfo.getLevelOfStudy()));
		createKeyPair(feeBase, "campus", ObjectUtils.toString(studentInfo.getCampus()));
		createKeyPair(feeBase, "full-or-part-time", studentInfo.getFullOrPartTime());
		
		// Step 3b: Import StudentProfile.StudentInformation.ProgramOfStudy
		StudentProfile.StudentInformation.ProgramOfStudy programOfStudy = studentInfo.getProgramOfStudy();
		
		createKeyPair(feeBase, "program-of-study-major", programOfStudy.getMajor());
		createKeyPair(feeBase, "program-of-study-second-major", programOfStudy.getSecondMajor());
		createKeyPair(feeBase, "program-of-study-minor", programOfStudy.getMinor());
		createKeyPair(feeBase, "program-of-study-second-minor", programOfStudy.getSecondMinor());
				
		// Step 4: Import StudentProfile.StudentInformation.keyPair (List<KeyPair>)
		for (KeyPair keyPair : studentInfo.getKeyPair()) {
			createKeyPair(feeBase, keyPair.getKeyName(), keyPair.getValue());
		}
		
		// Step 5a: Identify the LearningPeriod:
		StudentProfile.PeriodInformation periodInfo = studentProfile.getPeriodInformation();
		String periodName = ObjectUtils.toString(periodInfo.getPeriod());
		LearningPeriod period = feeManagementService.getLearningPeriod(periodName);
		
		// Importing the PeriodInformation is only possible when there is an already defined LearningPeriod:
		if (period != null) {
			// Step 5b: Import StudentProfile.PeriodInformation.keyPair (List<KeyPair>)
			if (period != null) {
				for (KeyPair periodKeyPair : periodInfo.getKeyPair()) {
					createPeriodKeyPair(feeBase, periodKeyPair.getKeyName(), periodKeyPair.getValue(), period);
				}
			}
			
			// Step 6: Import StudentProfile.PeriodInformation.Study.learningUnit (List<LearningUnit>)
			StudentProfile.PeriodInformation.Study study = periodInfo.getStudy();
	
			for (com.sigmasys.kuali.ksa.transform.LearningUnit lu : study.getLearningUnit()) {
				importLearningUnit(feeBase, lu, period);
			}
		}
	}
	
	/**
	 * Imports a {@link LearningUnit} from a {@link com.sigmasys.kuali.ksa.transform.LearningUnit} into a 
	 * <code>FeeBase</code> using the specified <code>LearningPeriod</code>.
	 * 
	 * @param feeBase 		A <code>FeeBase</code> object.
	 * @param luFromXml		A {@link com.sigmasys.kuali.ksa.transform.LearningUnit} from the student's profile object.
	 * @param period		A <code>LearningPeriod</code>.
	 */
	private void importLearningUnit(FeeBase feeBase, com.sigmasys.kuali.ksa.transform.LearningUnit luFromXml, LearningPeriod period) {
		// Try to find an existing LearningUnit:
		LearningUnit luFromFeeBase = findMatchingLearningUnit(feeBase, luFromXml);
		Date effectiveDate = (luFromXml.getEffectiveDate() != null) ? luFromXml.getEffectiveDate().toGregorianCalendar().getTime() : new Date();
		String command = luFromXml.getCommand();
		
		// If not found, create a new LearningUnit in the FeeBase:
	    if (luFromFeeBase == null) {
	    	// Create a new LearningUnit:
	    	luFromFeeBase = new LearningUnit();
	    	luFromFeeBase.setAccount(feeBase.getAccount());
	    	luFromFeeBase.setAccountId(feeBase.getAccount().getId());
	    	luFromFeeBase.setCampus(ObjectUtils.toString(luFromXml.getCampus()));
	    	luFromFeeBase.setCredit(new BigDecimal(luFromXml.getUnitCredit()));
	    	luFromFeeBase.setLearningPeriod(period);
	    	luFromFeeBase.setLevel(luFromXml.getLevel());
	    	luFromFeeBase.setUnitCode(luFromXml.getUnitCode());
	    	luFromFeeBase.setUnitSection(luFromXml.getUnitSection());
	    	feeBase.getStudy().add(luFromFeeBase);
	    	
	    	// If the command is "drop", set the "dropDate". Otherwise, set the "addDate":
		    if (StringUtils.equalsIgnoreCase("drop", command)) {
		    	luFromFeeBase.setDropDate(effectiveDate);
		    } else {
		    	luFromFeeBase.setAddDate(effectiveDate);
		    }
	    	
	    	// Persist the LearningUnit from FeeBase:
	    	feeManagementService.saveLearningUnit(luFromFeeBase);
	    	
	    	// Create KeyPairs:
	    	for (KeyPair keyPair : luFromXml.getKeyPair()) {
	    		createKeyPair(luFromFeeBase, keyPair.getKeyName(), keyPair.getValue());
	    	}
	    } else if (StringUtils.equalsIgnoreCase("drop", command)) {
	    	luFromFeeBase.setDropDate(effectiveDate);
	    	feeManagementService.saveLearningUnit(luFromFeeBase);
	    }
	}
	
	/**
	 * Attempts to find a matching <code>LearningUnit</code> within the specified <code>FeeBase</code>
	 * that matches the argument.
	 * 
	 * @param feeBase 		A <code>FeeBase</code> object.
	 * @param luToMatch 	A <code>com.sigmasys.kuali.ksa.transform.LearningUnit</code> to find a match for.
	 * @return A matching <code>LearningUnit</code> or <code>null</code> if no match is found.
	 */
	private LearningUnit findMatchingLearningUnit(FeeBase feeBase, com.sigmasys.kuali.ksa.transform.LearningUnit luToMatch) {
		LearningUnit existingLu = null;
		
		for (LearningUnit lu : feeBase.getStudy()) {
			if (StringUtils.equalsIgnoreCase(lu.getUnitCode(), luToMatch.getUnitCode())
					&& StringUtils.equalsIgnoreCase(lu.getUnitSection(), luToMatch.getUnitSection())) {
				existingLu = lu;
				
				break;
			}
		}
	
		return existingLu;
	}
	
	/**
	 * Creates a new {@link com.sigmasys.kuali.ksa.model.KeyPair} only if the <code>value</code> is not empty
	 * as checked by {@link StringUtils#isNotBlank(String)}
	 * 
	 * @param feeBase 	A <code>FeeBase</code> object.
	 * @param name 		Name of the new <code>KeyPair</code>.
	 * @param value 	Value of the new <code>KeyPair</code>
	 */
	private void createKeyPair(FeeBase feeBase, String name, String value) {
		if (StringUtils.isNotBlank(value)) {
			if (feeManagementService.containsKeyPair(feeBase, name)) {
				feeManagementService.updateKeyPair(feeBase, name, value);
			} else {
				feeManagementService.createKeyPair(feeBase, name, value);
			}
		}
	}

	/**
	 * Creates a new {@link PeriodKeyPair} only if the <code>value</code> is not empty
	 * as checked by {@link StringUtils#isNotBlank(String)} and the <code>period</code> is not <code>null</code>.
	 * 
	 * @param feeBase 	A <code>FeeBase</code> object.
	 * @param name 		Name of the new <code>PeriodKeyPair</code>.
	 * @param value 	Value of the new <code>PeriodKeyPair</code>.
	 * @param period	Period of the new <code>PeriodKeyPair</code>.
	 */
	private void createPeriodKeyPair(FeeBase feeBase, String name, String value, LearningPeriod period) {
		if (StringUtils.isNotBlank(value)) {
			if (feeManagementService.containsKeyPair(feeBase, name)) {
				feeManagementService.updateKeyPair(feeBase, name, value, period);
			} else {
				feeManagementService.createKeyPair(feeBase, name, value, period);
			}
		}
	}
	
	/**
	 * Creates a new {@link com.sigmasys.kuali.ksa.model.KeyPair} only if the <code>value</code> is not empty
	 * as checked by {@link StringUtils#isNotBlank(String)}
	 * 
	 * @param feeBase 	A <code>FeeBase</code> object.
	 * @param name 		Name of the new <code>KeyPair</code>.
	 * @param value 	Value of the new <code>KeyPair</code>
	 */
	private void createKeyPair(LearningUnit learningUnit, String name, String value) {
		if (StringUtils.isNotBlank(value)) {
			if (feeManagementService.containsKeyPair(learningUnit, name)) {
				feeManagementService.updateKeyPair(learningUnit, name, value);
			} else {
				feeManagementService.createKeyPair(learningUnit, name, value);
			}
		}
	}
}
