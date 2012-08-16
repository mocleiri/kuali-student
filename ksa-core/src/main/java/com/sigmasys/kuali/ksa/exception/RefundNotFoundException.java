package com.sigmasys.kuali.ksa.exception;

/**
 * This exception is thrown when a refund is not found.
 *
 * @author Michael Ivanov
 *         Date: 8/7/12
 */
public class RefundNotFoundException extends GenericException {

    public RefundNotFoundException(String message) {
        super(message);
    }
}
