package com.sigmasys.kuali.ksa.rm;

/*
 *  * The Credit class is entirely abstract and exists only to differentiate the differential between credits and debits.
 *   */
public abstract class Credit extends Transaction {
	
	/** if set to true, the amount may be refunded to the student subject to any other clearing rules, for example, a check 10-day waiting period, etc. 
	 * This will be set to false in the case of tuition deposits, etc, which may not be refunded to a student, but may be allocated to charges on the account.
	 * */
	private boolean isRefundable;
	
	/* A reference to the type of credit this is. Credit types define the clearing period for a transaction, refund preferences, charges that can be paid by the credit, etc.
	 * 
	 */
	private String creditType;
	
	

}

