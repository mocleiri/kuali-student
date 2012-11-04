package com.sigmasys.kuali.ksa.exception;

/**
 * This exception is thrown when a refund is not found.
 * A <code>Refund</code> identifier of an object that was attempted to be located
 * is stored in this object optionally as well.
 * 
 *
 * @author Michael Ivanov
 * @author Sergey Godunov
 *         Date: 8/7/12
 */
public class RefundNotFoundException extends GenericException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID of a <code>Refund</code> that was looked up, but not found.
	 */
	private Long refundId;
	
    public RefundNotFoundException(Long refundId, String message) {
        super(message);
        
        this.refundId = refundId;
    }
    
    /**
     * Returns the ID of a <code>Refund</code> object that was not found.
     * @return ID of the not found <code>Refund</code>.
     */
    public Long getRefundId() {
    	return refundId;
    }
}
