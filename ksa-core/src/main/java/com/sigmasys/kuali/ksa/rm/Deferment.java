package com.sigmasys.kuali.ksa.rm;

public class Deferment extends Credit {

	/*
	 * All deferments are set with an expiration date. If the date passes, then the deferment is expired, and the payment application system will
	 * remove the allocation of the deferment, and the charge it is applied to will become due.
	 */
	Date expirationDate;
	
	/*
	 * A deferment is always issued against a debit. This value shows the system which debit has been deferred. Once a deferment is expired, this value remains to show 
	 * the original status and intention of the deferment.
	 */
	String deferredTransactionId;
	/* a deferment is the only type of transaction whose amount can be altered. For audit purposes, the original value of the deferment is set permanently in this attribute,
	 * even if the deferment is reduced or expired, the original value will be accessible here.
	 */
	BigDecimal originalDefermentAmount;
	
	/*
	 * A deferment may be expired automatically (when the date of the deferment passes) or be expired manually but the system, either through user intervention, or by 
	 * a payment being received on the account that removes the need for the deferment. If, for example, an account is paid in full, the deferment would have to be expired, otherwise
	 * a credit balance would technically occur on the account.
	 */
	public void expire(){
		
	
	}
	
	/*
	 * This returns the expiration status and is true if the deferment has expired.
	 */
	public boolean getExpirationStatus(){
		
		
	
	}
	/*
	 * A deferment may be reduced or set to zero after expiration. Often, the value of a deferment may not exceed the debit balance on the account to prevent a
	 * credit balance being available for refund on the strenght of a deferment. A deferment may not be increased. Should such a situation arise, the deferment would need to 
	 * be expired, and a new deferment issued.
	 */
	public void reduceDeferment(BigDecimal newAmount){
		
	}
}

