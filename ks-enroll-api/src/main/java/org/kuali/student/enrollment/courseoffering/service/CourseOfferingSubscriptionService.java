/*
 * Copyright 2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.kuali.student.enrollment.courseoffering.service;

import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService(name = "CourseOfferingSubscriptionService", targetNamespace = CourseOfferingSubscribeNamespaceConstants.NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface CourseOfferingSubscriptionService {
    /**
     * Subscribe a callback to listen for new CourseOfferings for any course.
     *
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes.
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToNewCourseOfferings(@WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;
    /**
     * Subscribe a callback to listen for new CourseOfferings for a given term.
     *
     * @param termId the identifier for the Term to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToNewCourseOfferingsByTerm(@WebParam(name = "termId") String termId,
                                                @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;

    /**
     * Subscribe a callback to listen for new CourseOfferings for a given course.
     *
     * @param courseId the identifier for the Course to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToNewCourseOfferingsByCourse(@WebParam(name = "courseId") String courseId,
                                                @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;

    /**
     * Subscribe a callback to listen for new CourseOfferings for a given type.
     *
     * @param courseOfferingTypeKey the identifier for the CourseOffering type to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToNewCourseOfferingsByType(@WebParam(name = "courseOfferingTypeKey") String courseOfferingTypeKey,
                                                @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;


    /**
     * Subscribe a callback to listen for CourseOfferings updated.
     *
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToUpdateCourseOfferings(@WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;

    /**
     * Subscribe a callback to listen for CourseOfferings updated.
     *
     * @param termId the identifier for the Term to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToUpdateCourseOfferingsByTerm(@WebParam(name = "termId") String termId,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;

    /**
     * Subscribe a callback to listen for CourseOfferings updated.
     *
     * @param courseId the identifier for the Course to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToUpdateCourseOfferingsByCourse(@WebParam(name = "courseId") String courseId,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;

    /**
     * Subscribe a callback to listen for CourseOfferings updated.
     *
     * @param courseOfferingTypeKey the identifier for the CourseOffering type to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToUpdateCourseOfferingsByType(@WebParam(name = "courseOfferingTypeKey") String courseOfferingTypeKey,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;


    /**
     * Subscribe a callback to listen for CourseOffering that is deleted.
     *
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToDeleteCourseOfferings(@WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;
    /**
     * Subscribe a callback to listen for CourseOffering that is deleted.
     *
     * @param termId the identifier for the Term to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToDeleteCourseOfferingsByTerm(@WebParam(name = "termId") String termId,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;
    /**
     * Subscribe a callback to listen for CourseOffering that is deleted.
     *
     * @param courseId the identifier for the Course to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToDeleteCourseOfferingsByCourse(@WebParam(name = "courseId") String courseId,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;
    /**
     * Subscribe a callback to listen for CourseOffering that is deleted.
     *
     * @param courseOfferingTypeKey the identifier for the CourseOffering type to be retrieved.
     * @param courseOfferingCallbackService callback executable code to be invoked when the change event executes
     * @param contextInfo information containing the principalId and locale information about the caller of the service operation
     * @return callback registration id that can be used to explicitly desubscribe the listener.
     * @throws InvalidParameterException contextInfo is not valid
     * @throws MissingParameterException courseRegistrationId or contextInfo is missing or null
     * @throws OperationFailedException unable to complete request
     * @throws PermissionDeniedException an authorization failure occurred
     */
    public String subscribeToDeleteCourseOfferingsByType(@WebParam(name = "courseOfferingTypeKey") String courseOfferingTypeKey,
                                                   @WebParam(name = "courseOfferingCallbackService") CourseOfferingCallbackService courseOfferingCallbackService,
                                                   @WebParam(name = "contextInfo") ContextInfo contextInfo)
            throws InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException;




}
