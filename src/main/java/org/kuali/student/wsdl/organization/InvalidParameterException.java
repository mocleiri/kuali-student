
package org.kuali.student.wsdl.organization;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.10
 * Wed Sep 08 11:26:35 EDT 2010
 * Generated source version: 2.2.10
 * 
 */

@WebFault(name = "InvalidParameterException", targetNamespace = "http://student.kuali.org/wsdl/organization")
public class InvalidParameterException extends Exception {
    public static final long serialVersionUID = 20100908112635L;
    
    private org.kuali.student.wsdl.exceptions.InvalidParameterException invalidParameterException;

    public InvalidParameterException() {
        super();
    }
    
    public InvalidParameterException(String message) {
        super(message);
    }
    
    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterException(String message, org.kuali.student.wsdl.exceptions.InvalidParameterException invalidParameterException) {
        super(message);
        this.invalidParameterException = invalidParameterException;
    }

    public InvalidParameterException(String message, org.kuali.student.wsdl.exceptions.InvalidParameterException invalidParameterException, Throwable cause) {
        super(message, cause);
        this.invalidParameterException = invalidParameterException;
    }

    public org.kuali.student.wsdl.exceptions.InvalidParameterException getFaultInfo() {
        return this.invalidParameterException;
    }
}
