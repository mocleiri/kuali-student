package com.sigmasys.kuali.ksa.service.support;


/**
 * This {@link RuntimeException} sub-class is thrown when an account validation fails. 
 * 
 * @author Sergey
 *
 */
public class NoSuchAccountException extends RuntimeException {
	private static final long serialVersionUID = -5613065416527421402L;

	/**
	 * Account ID that failed validation.
	 */
	private String accountId;
	
	
	/**
	 * Creates a new instance of <code>NoSuchAccountException</code> with a given message and Account ID that failed validation.
	 * 
	 * @param msg 			Error message.
	 * @param accountId 	ID of an Account that failed validation.
	 */
	public NoSuchAccountException(String msg, String accountId) {
		super(String.format("%s : %s", msg, accountId));
		
		this.accountId = accountId;
	}
	
	/**
	 * Creates a new instance of <code>NoSuchAccountException</code> without an Account ID that failed validation.
	 * 
	 * @param msg 			Error message.
	 * @param accountId 	ID of an Account that failed validation.
	 */
	public NoSuchAccountException(String accountId) {
		super();
		
		this.accountId = accountId;
	}

	/**
	 * Returns ID of an Account that failed validation.
	 * 
	 * @return ID of an Account that failed validation.
	 */
	public String getAccountId() {
		return accountId;
	}
}
