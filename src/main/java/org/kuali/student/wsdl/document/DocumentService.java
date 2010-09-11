package org.kuali.student.wsdl.document;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.2.10
 * Wed Sep 08 11:26:25 EDT 2010
 * Generated source version: 2.2.10
 * 
 */
 
@WebService(targetNamespace = "http://student.kuali.org/wsdl/document", name = "DocumentService")
@XmlSeeAlso({org.kuali.student.wsdl.exceptions.ObjectFactory.class, ObjectFactory.class})
public interface DocumentService {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocumentsByIdList", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentsByIdList")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentsByIdListResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentsByIdListResponse")
    public java.util.List<org.kuali.student.wsdl.document.DocumentInfo> getDocumentsByIdList(
        @WebParam(name = "documentIdList", targetNamespace = "")
        java.util.List<java.lang.String> documentIdList
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "validateDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.ValidateDocument")
    @WebMethod
    @ResponseWrapper(localName = "validateDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.ValidateDocumentResponse")
    public java.util.List<org.kuali.student.wsdl.document.ValidationResultInfo> validateDocument(
        @WebParam(name = "validationType", targetNamespace = "")
        java.lang.String validationType,
        @WebParam(name = "documentInfo", targetNamespace = "")
        org.kuali.student.wsdl.document.DocumentInfo documentInfo
    ) throws MissingParameterException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocument")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentResponse")
    public org.kuali.student.wsdl.document.DocumentInfo getDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "updateDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.UpdateDocument")
    @WebMethod
    @ResponseWrapper(localName = "updateDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.UpdateDocumentResponse")
    public org.kuali.student.wsdl.document.DocumentInfo updateDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId,
        @WebParam(name = "documentInfo", targetNamespace = "")
        org.kuali.student.wsdl.document.DocumentInfo documentInfo
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, VersionMismatchException, DoesNotExistException, DataValidationErrorException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "deleteDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.DeleteDocument")
    @WebMethod
    @ResponseWrapper(localName = "deleteDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.DeleteDocumentResponse")
    public org.kuali.student.wsdl.document.StatusInfo deleteDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocumentType", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentType")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentTypeResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentTypeResponse")
    public org.kuali.student.wsdl.document.DocumentTypeInfo getDocumentType(
        @WebParam(name = "documentTypeKey", targetNamespace = "")
        java.lang.String documentTypeKey
    ) throws MissingParameterException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getCategoriesByDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetCategoriesByDocument")
    @WebMethod
    @ResponseWrapper(localName = "getCategoriesByDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetCategoriesByDocumentResponse")
    public java.util.List<org.kuali.student.wsdl.document.DocumentCategoryInfo> getCategoriesByDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "removeDocumentCategoryFromDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.RemoveDocumentCategoryFromDocument")
    @WebMethod
    @ResponseWrapper(localName = "removeDocumentCategoryFromDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.RemoveDocumentCategoryFromDocumentResponse")
    public org.kuali.student.wsdl.document.StatusInfo removeDocumentCategoryFromDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId,
        @WebParam(name = "documentCategoryKey", targetNamespace = "")
        java.lang.String documentCategoryKey
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocumentCategories", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentCategories")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentCategoriesResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentCategoriesResponse")
    public java.util.List<org.kuali.student.wsdl.document.DocumentCategoryInfo> getDocumentCategories() throws OperationFailedException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "addDocumentCategoryToDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.AddDocumentCategoryToDocument")
    @WebMethod
    @ResponseWrapper(localName = "addDocumentCategoryToDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.AddDocumentCategoryToDocumentResponse")
    public org.kuali.student.wsdl.document.StatusInfo addDocumentCategoryToDocument(
        @WebParam(name = "documentId", targetNamespace = "")
        java.lang.String documentId,
        @WebParam(name = "documentCategoryKey", targetNamespace = "")
        java.lang.String documentCategoryKey
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, VersionMismatchException, DoesNotExistException, DataValidationErrorException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "createDocument", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.CreateDocument")
    @WebMethod
    @ResponseWrapper(localName = "createDocumentResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.CreateDocumentResponse")
    public org.kuali.student.wsdl.document.DocumentInfo createDocument(
        @WebParam(name = "documentTypeKey", targetNamespace = "")
        java.lang.String documentTypeKey,
        @WebParam(name = "documentCategoryKey", targetNamespace = "")
        java.lang.String documentCategoryKey,
        @WebParam(name = "documentInfo", targetNamespace = "")
        org.kuali.student.wsdl.document.DocumentInfo documentInfo
    ) throws MissingParameterException, PermissionDeniedException, OperationFailedException, InvalidParameterException, DoesNotExistException, DataValidationErrorException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocumentTypes", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentTypes")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentTypesResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentTypesResponse")
    public java.util.List<org.kuali.student.wsdl.document.DocumentTypeInfo> getDocumentTypes() throws OperationFailedException;

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getDocumentCategory", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentCategory")
    @WebMethod
    @ResponseWrapper(localName = "getDocumentCategoryResponse", targetNamespace = "http://student.kuali.org/wsdl/document", className = "org.kuali.student.wsdl.document.GetDocumentCategoryResponse")
    public org.kuali.student.wsdl.document.DocumentCategoryInfo getDocumentCategory(
        @WebParam(name = "documentCategoryKey", targetNamespace = "")
        java.lang.String documentCategoryKey
    ) throws MissingParameterException, OperationFailedException, InvalidParameterException, DoesNotExistException;
}
