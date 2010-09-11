
package org.kuali.student.wsdl.course;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.10
 * Wed Sep 08 11:26:43 EDT 2010
 * Generated source version: 2.2.10
 * 
 */

@WebFault(name = "DependentObjectsExistException", targetNamespace = "http://student.kuali.org/wsdl/course")
public class DependentObjectsExistException extends Exception {
    public static final long serialVersionUID = 20100908112643L;
    
    private org.kuali.student.wsdl.exceptions.DependentObjectsExistException dependentObjectsExistException;

    public DependentObjectsExistException() {
        super();
    }
    
    public DependentObjectsExistException(String message) {
        super(message);
    }
    
    public DependentObjectsExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependentObjectsExistException(String message, org.kuali.student.wsdl.exceptions.DependentObjectsExistException dependentObjectsExistException) {
        super(message);
        this.dependentObjectsExistException = dependentObjectsExistException;
    }

    public DependentObjectsExistException(String message, org.kuali.student.wsdl.exceptions.DependentObjectsExistException dependentObjectsExistException, Throwable cause) {
        super(message, cause);
        this.dependentObjectsExistException = dependentObjectsExistException;
    }

    public org.kuali.student.wsdl.exceptions.DependentObjectsExistException getFaultInfo() {
        return this.dependentObjectsExistException;
    }
}
