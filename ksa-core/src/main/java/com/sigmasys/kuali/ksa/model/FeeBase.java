package com.sigmasys.kuali.ksa.model;

import java.io.Serializable;
import java.util.List;

import com.sigmasys.kuali.ksa.service.FeeManagementService;

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
 * does not exist. Instead use an instance of {@link FeeManagementService} 
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
	private List<KeyPair> studentData;
	
	/**
	 * Period data in form of PeriodKeyPair objects.
	 */
	private List<PeriodKeyPair> periodData;
	
	/**
	 * All courses taken that are associated with the given account.
	 */
	private List<LearningUnit> study;

	
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

	public List<KeyPair> getStudentData() {
		return studentData;
	}

	public void setStudentData(List<KeyPair> studentData) {
		this.studentData = studentData;
	}

	public List<PeriodKeyPair> getPeriodData() {
		return periodData;
	}

	public void setPeriodData(List<PeriodKeyPair> periodData) {
		this.periodData = periodData;
	}

	public List<LearningUnit> getStudy() {
		return study;
	}

	public void setStudy(List<LearningUnit> study) {
		this.study = study;
	}
}
