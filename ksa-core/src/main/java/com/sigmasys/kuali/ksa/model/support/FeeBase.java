package com.sigmasys.kuali.ksa.model.support;

import java.io.Serializable;
import java.util.Set;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.LearningUnit;
import com.sigmasys.kuali.ksa.model.PeriodKeyPair;

/**
 * This class represents an implementation of a Fee Assessment unit of calculation. 
 * The current design describes FeeBase as follows,
 * 
 * The FeeBase class underlies the fee assessment system (KSA-FM) It stores the values that are
 * transmitted to KSA in order to permit it to be able to calculate the fees associated with a student. Due to
 * the nature of fee assessment, varies so widely from school to school, the FeeBase class is abstract, and
 * stores most of its values in key-pairs, allowing fee assessment to respond to almost any type of student
 * attribute.
 * 
 * NOTE: This class is a just a placeholder for data. The actual Java Persistence mapping
 * is stored in the Account class, which delegates data storage to this class. 
 * 
 * TODO: The part that says that the FeeBase class should be abstract must be further resolved with Paul.
 * 
 * @author Sergey
 *
 */
public class FeeBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Associated Account.
	 */
	private Account account;
	
	/**
	 * Non-period student data in form of KeyPair objects.
	 */
	private Set<KeyPair> studentData;
	
	/**
	 * Period data in form of PeriodKeyPair objects.
	 */
	private Set<PeriodKeyPair> periodData;
	
	/**
	 * All courses taken that are associated with the given account.
	 */
	private Set<LearningUnit> study;

	
	public FeeBase() {}
	
	public FeeBase(Account account) {
		this.account = account;
	}
	
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<KeyPair> getStudentData() {
		return studentData;
	}

	public void setStudentData(Set<KeyPair> studentData) {
		this.studentData = studentData;
	}

	public Set<PeriodKeyPair> getPeriodData() {
		return periodData;
	}

	public void setPeriodData(Set<PeriodKeyPair> periodData) {
		this.periodData = periodData;
	}

	public Set<LearningUnit> getStudy() {
		return study;
	}

	public void setStudy(Set<LearningUnit> study) {
		this.study = study;
	}
}
