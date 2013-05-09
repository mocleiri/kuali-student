/**
 * Copyright 2010 The Kuali Foundation Licensed under the Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */

package org.kuali.student.r2.core.organization.service.impl;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.student.r2.common.criteria.CriteriaLookupService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.exceptions.VersionMismatchException;

import org.kuali.student.r2.core.organization.dao.OrgDao;
import org.kuali.student.r2.core.organization.dao.OrgHierarchyDao;
import org.kuali.student.r2.core.organization.dao.OrgOrgRelationDao;
import org.kuali.student.r2.core.organization.dao.OrgPersonRelationDao;
import org.kuali.student.r2.core.organization.dao.OrgPositionRestrictionDao;
import org.kuali.student.r2.core.organization.model.OrgEntity;
import org.kuali.student.r2.core.organization.model.OrgHierarchyEntity;
import org.kuali.student.r2.core.organization.model.OrgOrgRelationEntity;
import org.kuali.student.r2.core.organization.model.OrgPersonRelationEntity;
import org.kuali.student.r2.core.organization.model.OrgPositionRestrictionEntity;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.organization.constants.OrganizationServiceConstants;
import org.kuali.student.r2.core.organization.dto.OrgHierarchyInfo;
import org.kuali.student.r2.core.organization.dto.OrgInfo;
import org.kuali.student.r2.core.organization.dto.OrgOrgRelationInfo;
import org.kuali.student.r2.core.organization.dto.OrgPersonRelationInfo;
import org.kuali.student.r2.core.organization.dto.OrgPositionRestrictionInfo;
import org.kuali.student.r2.core.organization.dto.OrgTreeViewInfo;
import org.kuali.student.r2.core.organization.service.OrganizationService;

import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "org.kuali.student.r2.core.organization.service.OrganizationService", serviceName = "OrganizationService", portName = "OrganizationService", targetNamespace = "http://student.kuali.org/wsdl/organization")
@Transactional(readOnly = true, noRollbackFor = {DoesNotExistException.class}, rollbackFor = {Throwable.class})
public class OrganizationServiceImpl implements OrganizationService {

    private TypeService typeService;

    private OrgHierarchyDao orgHierarchyDao;
    private OrgDao orgDao;
    private OrgOrgRelationDao orgOrgRelationDao;
    private OrgPersonRelationDao orgPersonRelationDao;
    private OrgPositionRestrictionDao orgPositionRestrictionDao;

    private CriteriaLookupService orgHierarchyCriteriaLookupService;
    private CriteriaLookupService orgCriteriaLookupService;
    private CriteriaLookupService orgOrgRelationCriteriaLookupService;
    private CriteriaLookupService orgPersonRelationCriteriaLookupService;
    private CriteriaLookupService orgPositionRestrictionCriteriaLookupService;

    private final Logger logger = Logger.getLogger(OrganizationServiceImpl.class);

    public OrgHierarchyDao getOrgHierarchyDao() {
        return orgHierarchyDao;
    }

    public void setOrgHierarchyDao(OrgHierarchyDao orgHierarchyDao) {
        this.orgHierarchyDao = orgHierarchyDao;
    }

    public OrgDao getOrgDao() {
        return orgDao;
    }

    public void setOrgDao(OrgDao orgDao) {
        this.orgDao = orgDao;
    }

    public OrgOrgRelationDao getOrgOrgRelationDao() {
        return orgOrgRelationDao;
    }

    public void setOrgOrgRelationDao(OrgOrgRelationDao orgOrgRelationDao) {
        this.orgOrgRelationDao = orgOrgRelationDao;
    }

    public OrgPersonRelationDao getOrgPersonRelationDao() {
        return orgPersonRelationDao;
    }

    public void setOrgPersonRelationDao(OrgPersonRelationDao orgPresonRelationDao) {
        this.orgPersonRelationDao = orgPresonRelationDao;
    }

    public OrgPositionRestrictionDao getOrgPositionRestrictionDao() {
        return orgPositionRestrictionDao;
    }

    public void setOrgPositionRestrictionDao(OrgPositionRestrictionDao orgPositionRestrictionDao) {
        this.orgPositionRestrictionDao = orgPositionRestrictionDao;
    }

    public CriteriaLookupService getOrgHierarchyCriteriaLookupService() {
        return orgHierarchyCriteriaLookupService;
    }

    public void setOrgHierarchyCriteriaLookupService(CriteriaLookupService orgHierarchyCriteriaLookupService) {
        this.orgHierarchyCriteriaLookupService = orgHierarchyCriteriaLookupService;
    }

    public CriteriaLookupService getOrgCriteriaLookupService() {
        return orgCriteriaLookupService;
    }

    public void setOrgCriteriaLookupService(CriteriaLookupService orgCriteriaLookupService) {
        this.orgCriteriaLookupService = orgCriteriaLookupService;
    }

    public CriteriaLookupService getOrgOrgRelationCriteriaLookupService() {
        return orgOrgRelationCriteriaLookupService;
    }

    public void setOrgOrgRelationCriteriaLookupService(CriteriaLookupService orgOrgRelationCriteriaLookupService) {
        this.orgOrgRelationCriteriaLookupService = orgOrgRelationCriteriaLookupService;
    }

    public CriteriaLookupService getOrgPersonRelationCriteriaLookupService() {
        return orgPersonRelationCriteriaLookupService;
    }

    public void setOrgPersonRelationCriteriaLookupService(CriteriaLookupService orgPersonRelationCriteriaLookupService) {
        this.orgPersonRelationCriteriaLookupService = orgPersonRelationCriteriaLookupService;
    }

    public CriteriaLookupService getOrgPositionRestrictionCriteriaLookupService() {
        return orgPositionRestrictionCriteriaLookupService;
    }

    public void setOrgPositionRestrictionCriteriaLookupService(CriteriaLookupService orgPositionRestrictionCriteriaLookupService) {
        this.orgPositionRestrictionCriteriaLookupService = orgPositionRestrictionCriteriaLookupService;
    }

    @Override
    public OrgHierarchyInfo getOrgHierarchy(String orgHierarchyId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgHierarchyEntity entity = orgHierarchyDao.find(orgHierarchyId);
        if(entity == null) {
            throw new DoesNotExistException(orgHierarchyId);
        }
        return entity.toDto();
    }

    @Override
    public List<OrgHierarchyInfo> getOrgHierarchiesByIds(List<String> orgHierarchyIds, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgHierarchyEntity> entities = orgHierarchyDao.findByIds(orgHierarchyIds);
        List<OrgHierarchyInfo> result = new ArrayList<OrgHierarchyInfo>(entities.size());
        for (OrgHierarchyEntity entity : entities) {
            if (entity == null) {
                // if one of the entities from "findByIds" is returned as null, then one of the keys in the list was not found
                throw new DoesNotExistException();
            }
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> getOrgHierarchyIdsByType(String orgHierarchyTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return orgHierarchyDao.getIdsByType(orgHierarchyTypeKey);
    }

    @Override
    public List<OrgHierarchyInfo> getOrgHierarchies(ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgHierarchyEntity> entities = orgHierarchyDao.findAll();
        List<OrgHierarchyInfo> result = new ArrayList<OrgHierarchyInfo>(entities.size());
        for (OrgHierarchyEntity entity : entities) {
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> searchForOrgHierarchyIds(QueryByCriteria criteria, ContextInfo contextInfo)throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> results = new ArrayList<String>();
        GenericQueryResults<OrgHierarchyEntity> orgHierarchies = orgHierarchyCriteriaLookupService.lookup(OrgHierarchyEntity.class, criteria);
        if (null != orgHierarchies && orgHierarchies.getResults().size() > 0) {
            for (OrgHierarchyEntity orgHierarchy : orgHierarchies.getResults()) {
                results.add(orgHierarchy.getId());
            }
        }
        return results;
    }

    @Override
    public List<OrgHierarchyInfo> searchForOrgHierarchies(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgHierarchyInfo> results = new ArrayList<OrgHierarchyInfo>();
        GenericQueryResults<OrgHierarchyEntity> orgHierarchies = orgHierarchyCriteriaLookupService.lookup(OrgHierarchyEntity.class, criteria);
        if (null != orgHierarchies && orgHierarchies.getResults().size() > 0) {
            for (OrgHierarchyEntity orgHierarchy : orgHierarchies.getResults()) {
                results.add(orgHierarchy.toDto());
            }
        }
        return results;
    }

    @Override
    public List<ValidationResultInfo> validateOrgHierarchy(String validationTypeKey, String orgHierarchyTypeKey, OrgHierarchyInfo orgHierarchyInfo, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public OrgHierarchyInfo createOrgHierarchy(String orgHierarchyTypeKey, OrgHierarchyInfo orgHierarchyInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException {
        OrgHierarchyEntity entity = new OrgHierarchyEntity(orgHierarchyInfo);
        entity.setOrgHierarchyType(orgHierarchyTypeKey);
        entity.setEntityCreated(contextInfo);
        orgHierarchyDao.persist(entity);
        return entity.toDto();
    }

    @Override
    public OrgHierarchyInfo updateOrgHierarchy(String orgHierarchyId, OrgHierarchyInfo orgHierarchyInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        if(!orgHierarchyId.equals(orgHierarchyInfo.getId())) {
            throw new InvalidParameterException(orgHierarchyId + " does not match the id in the object " + orgHierarchyInfo.getId());
        }
        OrgHierarchyEntity entity = orgHierarchyDao.find(orgHierarchyId);
        if(null == entity) {
            throw new DoesNotExistException(orgHierarchyId);
        }
        entity.fromDto(orgHierarchyInfo);
        entity.setEntityUpdated(contextInfo);
        entity = orgHierarchyDao.merge(entity);
        orgHierarchyDao.getEm().flush();
        return entity.toDto();
    }

    @Override
    public StatusInfo deleteOrgHierarchy(String orgHierarchyId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgHierarchyEntity entity = orgHierarchyDao.find(orgHierarchyId);
        if (null == entity) {
            throw new DoesNotExistException(orgHierarchyId);
        }
        orgHierarchyDao.remove(entity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public List<TypeInfo> getOrgTypes(ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        try {
            return typeService.getTypesByRefObjectUri(OrganizationServiceConstants.REF_OBJECT_URI_ORG,contextInfo);
        } catch (DoesNotExistException e) {
            throw new OperationFailedException("Unable to pull any types for ref object URI " + OrganizationServiceConstants.REF_OBJECT_URI_ORG, e);
        }
    }

    @Override
    public OrgInfo getOrg(String orgId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgEntity entity = orgDao.find(orgId);
        if(entity == null) {
            throw new DoesNotExistException(orgId);
        }
        return entity.toDto();
    }

    @Override
    public List<OrgInfo> getOrgsByIds(List<String> orgIds, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgEntity> entities = orgDao.findByIds(orgIds);
        List<OrgInfo> result = new ArrayList<OrgInfo>(entities.size());
        for (OrgEntity entity : entities) {
            if (entity == null) {
                // if one of the entities from "findByIds" is returned as null, then one of the keys in the list was not found
                throw new DoesNotExistException();
            }
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> getOrgIdsByType(String orgTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return orgDao.getIdsByType(orgTypeKey);
    }

    @Override
    public List<String> searchForOrgIds(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> results = new ArrayList<String>();
        GenericQueryResults<OrgEntity> orgs = orgCriteriaLookupService.lookup(OrgEntity.class, criteria);
        if (null != orgs && orgs.getResults().size() > 0) {
            for (OrgEntity org : orgs.getResults()) {
                results.add(org.getId());
            }
        }
        return results;
    }

    @Override
    public List<OrgInfo> searchForOrgs(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgInfo> results = new ArrayList<OrgInfo>();
        GenericQueryResults<OrgEntity> orgs = orgCriteriaLookupService.lookup(OrgEntity.class, criteria);
        if (null != orgs && orgs.getResults().size() > 0) {
            for (OrgEntity org : orgs.getResults()) {
                results.add(org.toDto());
            }
        }
        return results;
    }

    @Override
    public List<ValidationResultInfo> validateOrg(String validationTypeKey, String orgTypeKey, OrgInfo orgInfo, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public OrgInfo createOrg(String orgTypeKey, OrgInfo orgInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException {
        OrgEntity entity = new OrgEntity(orgInfo);
        entity.setOrgType(orgTypeKey);
        entity.setEntityCreated(contextInfo);
        orgDao.persist(entity);
        return entity.toDto();
    }

    @Override
    public OrgInfo updateOrg(String orgId, OrgInfo orgInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        if(!orgId.equals(orgInfo.getId())) {
            throw new InvalidParameterException(orgId + " does not match the id in the object " + orgInfo.getId());
        }
        OrgEntity entity = orgDao.find(orgId);
        if(null == entity) {
            throw new DoesNotExistException(orgId);
        }
        entity.fromDto(orgInfo);
        entity.setEntityUpdated(contextInfo);
        entity = orgDao.merge(entity);
        orgDao.getEm().flush();
        return entity.toDto();
    }

    @Override
    public StatusInfo deleteOrg(String orgId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgEntity entity = orgDao.find(orgId);
        if (null == entity) {
            throw new DoesNotExistException(orgId);
        }

        orgDao.remove(entity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public List<TypeInfo> getOrgOrgRelationTypes(ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        try {
            return typeService.getTypesByRefObjectUri(OrganizationServiceConstants.REF_OBJECT_URI_ORG_ORG_RELATION,contextInfo);
        } catch (DoesNotExistException e) {
            throw new OperationFailedException("Unable to pull any types for ref object URI " + OrganizationServiceConstants.REF_OBJECT_URI_ORG_ORG_RELATION, e);
        }
    }

    @Override
    public List<TypeInfo> getOrgOrgRelationTypesForOrgType(String orgTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TypeInfo> getOrgOrgRelationTypesForOrgHierarchy(String orgHierarchyId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean hasOrgOrgRelation(String orgId, String comparisonOrgId, String orgOrgRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgOrgRelationInfo getOrgOrgRelation(String orgOrgRelationId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgOrgRelationEntity entity = orgOrgRelationDao.find(orgOrgRelationId);
        if(null == entity) {
            throw new DoesNotExistException(orgOrgRelationId);
        }
        return entity.toDto();
    }

    @Override
    public List<OrgOrgRelationInfo> getOrgOrgRelationsByIds(List<String> orgOrgRelationIds, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgOrgRelationEntity> entities = orgOrgRelationDao.findByIds(orgOrgRelationIds);
        List<OrgOrgRelationInfo> result = new ArrayList<OrgOrgRelationInfo>(entities.size());
        for (OrgOrgRelationEntity entity : entities) {
            if (entity == null) {
                // if one of the entities from "findByIds" is returned as null, then one of the keys in the list was not found
                throw new DoesNotExistException();
            }
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> getOrgOrgRelationIdsByType(String orgOrgRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return orgOrgRelationDao.getIdsByType(orgOrgRelationTypeKey);
    }

    @Override
    public List<OrgOrgRelationInfo> getOrgOrgRelationsByOrg(String orgId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgOrgRelationInfo> getOrgOrgRelationsByOrgs(String orgId, String peerOrgId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgOrgRelationInfo> getOrgOrgRelationsByTypeAndOrg(String orgId, String orgOrgRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgOrgRelationInfo> getOrgOrgRelationsByTypeAndRelatedOrg(String relatedOrgId, String orgOrgRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> searchForOrgOrgRelationIds(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> results = new ArrayList<String>();
        GenericQueryResults<OrgOrgRelationEntity> orgOrgRelations = orgOrgRelationCriteriaLookupService.lookup(OrgOrgRelationEntity.class, criteria);
        if (null != orgOrgRelations && orgOrgRelations.getResults().size() > 0) {
            for (OrgOrgRelationEntity orgOrgRelation : orgOrgRelations.getResults()) {
                results.add(orgOrgRelation.getId());
            }
        }
        return results;
    }

    @Override
    public List<OrgOrgRelationInfo> searchForOrgOrgRelations(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgOrgRelationInfo> results = new ArrayList<OrgOrgRelationInfo>();
        GenericQueryResults<OrgOrgRelationEntity> orgOrgRelations = orgOrgRelationCriteriaLookupService.lookup(OrgOrgRelationEntity.class, criteria);
        if (null != orgOrgRelations && orgOrgRelations.getResults().size() > 0) {
            for (OrgOrgRelationEntity orgOrgRelation : orgOrgRelations.getResults()) {
                results.add(orgOrgRelation.toDto());
            }
        }
        return results;
    }

    @Override
    public List<ValidationResultInfo> validateOrgOrgRelation(String validationTypeKey, String orgId, String orgPeerId, String orgOrgRelationTypeKey, OrgOrgRelationInfo orgOrgRelationInfo, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public OrgOrgRelationInfo createOrgOrgRelation(String orgId, String orgPeerId, String orgOrgRelationTypeKey, OrgOrgRelationInfo orgOrgRelationInfo, ContextInfo contextInfo) throws DoesNotExistException, DataValidationErrorException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgOrgRelationInfo updateOrgOrgRelation(String orgOrgRelationId, OrgOrgRelationInfo orgOrgRelationInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        if(!orgOrgRelationId.equals(orgOrgRelationInfo.getId())) {
            throw new InvalidParameterException(orgOrgRelationId + " does not match the id in the object " + orgOrgRelationInfo.getId());
        }
        OrgOrgRelationEntity entity = orgOrgRelationDao.find(orgOrgRelationId);
        if(null == entity) {
            throw new DoesNotExistException(orgOrgRelationId);
        }
        entity.fromDto(orgOrgRelationInfo);
        entity.setEntityUpdated(contextInfo);
        entity = orgOrgRelationDao.merge(entity);
        orgOrgRelationDao.getEm().flush();
        return entity.toDto();
    }

    @Override
    public StatusInfo deleteOrgOrgRelation(String orgOrgRelationId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgOrgRelationEntity entity = orgOrgRelationDao.find(orgOrgRelationId);
        if (null == entity) {
            throw new DoesNotExistException(orgOrgRelationId);
        }
        orgOrgRelationDao.remove(entity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public List<TypeInfo> getOrgPersonRelationTypes(ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        try {
            return typeService.getTypesByRefObjectUri(OrganizationServiceConstants.REF_OBJECT_URI_ORG_PERSON_RELATION,contextInfo);
        } catch (DoesNotExistException e) {
            throw new OperationFailedException("Unable to pull any types for ref object URI " + OrganizationServiceConstants.REF_OBJECT_URI_ORG_PERSON_RELATION, e);
        }
    }

    @Override
    public List<TypeInfo> getOrgPersonRelationTypesForOrgType(String orgTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean hasOrgPersonRelation(String orgId, String personId, String orgPersonRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgPersonRelationInfo getOrgPersonRelation(String orgPersonRelationId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgPersonRelationEntity entity = orgPersonRelationDao.find(orgPersonRelationId);
        if(null == entity) {
            throw new DoesNotExistException(orgPersonRelationId);
        }
        return entity.toDto();
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByIds(List<String> orgPersonRelationIds, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgPersonRelationEntity> entities = orgPersonRelationDao.findByIds(orgPersonRelationIds);
        List<OrgPersonRelationInfo> result = new ArrayList<OrgPersonRelationInfo>(entities.size());
        for (OrgPersonRelationEntity entity : entities) {
            if (entity == null) {
                // if one of the entities from "findByIds" is returned as null, then one of the keys in the list was not found
                throw new DoesNotExistException();
            }
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> getOrgPersonRelationIdsByType(String orgPersonRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return orgPersonRelationDao.getIdsByType(orgPersonRelationTypeKey);
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByOrg(String orgId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByTypeAndOrg(String orgPersonRelationTypeKey, String orgId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByPerson(String personId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByTypeAndPerson(String orgPersonRelationTypeKey, String personId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByOrgAndPerson(String orgId, String personId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<OrgPersonRelationInfo> getOrgPersonRelationsByTypeAndOrgAndPerson(String orgPersonRelationTypeKey, String orgId, String personId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> searchForOrgPersonRelationIds(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> results = new ArrayList<String>();
        GenericQueryResults<OrgPersonRelationEntity> orgPersonRelations = orgPersonRelationCriteriaLookupService.lookup(OrgPersonRelationEntity.class, criteria);
        if (null != orgPersonRelations && orgPersonRelations.getResults().size() > 0) {
            for (OrgPersonRelationEntity orgPersonRelation : orgPersonRelations.getResults()) {
                results.add(orgPersonRelation.getId());
            }
        }
        return results;
    }

    @Override
    public List<OrgPersonRelationInfo> searchForOrgPersonRelations(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgPersonRelationInfo> results = new ArrayList<OrgPersonRelationInfo>();
        GenericQueryResults<OrgPersonRelationEntity> orgPersonRelations = orgPersonRelationCriteriaLookupService.lookup(OrgPersonRelationEntity.class, criteria);
        if (null != orgPersonRelations && orgPersonRelations.getResults().size() > 0) {
            for (OrgPersonRelationEntity orgPersonRelation : orgPersonRelations.getResults()) {
                results.add(orgPersonRelation.toDto());
            }
        }
        return results;
    }

    @Override
    public List<ValidationResultInfo> validateOrgPersonRelation(String validationTypeKey, String orgId, String personId, String orgPersonRelationTypeKey, OrgPersonRelationInfo orgPersonRelationInfo, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public OrgPersonRelationInfo createOrgPersonRelation(String orgId, String personId, String orgPersonRelationTypeKey, OrgPersonRelationInfo orgPersonRelationInfo, ContextInfo contextInfo) throws DoesNotExistException, DataValidationErrorException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgPersonRelationInfo updateOrgPersonRelation(String orgPersonRelationId, OrgPersonRelationInfo orgPersonRelationInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        if(!orgPersonRelationId.equals(orgPersonRelationInfo.getId())) {
            throw new InvalidParameterException(orgPersonRelationId + " does not match the id in the object " + orgPersonRelationInfo.getId());
        }
        OrgPersonRelationEntity entity = orgPersonRelationDao.find(orgPersonRelationId);
        if(null == entity) {
            throw new DoesNotExistException(orgPersonRelationId);
        }
        entity.fromDto(orgPersonRelationInfo);
        entity.setEntityUpdated(contextInfo);
        entity = orgPersonRelationDao.merge(entity);
        orgPersonRelationDao.getEm().flush();
        return entity.toDto();
    }

    @Override
    public StatusInfo deleteOrgPersonRelation(String orgPersonRelationId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgPersonRelationEntity entity = orgPersonRelationDao.find(orgPersonRelationId);
        if (null == entity) {
            throw new DoesNotExistException(orgPersonRelationId);
        }
        orgPersonRelationDao.remove(entity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public OrgPositionRestrictionInfo getOrgPositionRestriction(String orgPositionRestrictionId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgPositionRestrictionEntity entity = orgPositionRestrictionDao.find(orgPositionRestrictionId);
        if(null == entity) {
            throw new DoesNotExistException(orgPositionRestrictionId);
        }
        return entity.toDto();
    }

    @Override
    public List<OrgPositionRestrictionInfo> getOrgPositionRestrictionsByIds(List<String> orgPositionRestrictionIds, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgPositionRestrictionEntity> entities = orgPositionRestrictionDao.findByIds(orgPositionRestrictionIds);
        List<OrgPositionRestrictionInfo> result = new ArrayList<OrgPositionRestrictionInfo>(entities.size());
        for (OrgPositionRestrictionEntity entity : entities) {
            if (entity == null) {
                // if one of the entities from "findByIds" is returned as null, then one of the keys in the list was not found
                throw new DoesNotExistException();
            }
            result.add(entity.toDto());
        }
        return result;
    }

    @Override
    public List<String> getOrgPositionRestrictionIdsByType(String orgPersonRelationTypeKey, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getOrgPositionRestrictionIdsByOrg(String orgId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> searchForOrgPositionRestrictionIds(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> results = new ArrayList<String>();
        GenericQueryResults<OrgPositionRestrictionEntity> orgPositionRestrictions = orgPositionRestrictionCriteriaLookupService.lookup(OrgPositionRestrictionEntity.class, criteria);
        if (null != orgPositionRestrictions && orgPositionRestrictions.getResults().size() > 0) {
            for (OrgPositionRestrictionEntity orgPositionRestriction : orgPositionRestrictions.getResults()) {
                results.add(orgPositionRestriction.getId());
            }
        }
        return results;
    }

    @Override
    public List<OrgPositionRestrictionInfo> searchForOrgPositionRestrictions(QueryByCriteria criteria, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<OrgPositionRestrictionInfo> results = new ArrayList<OrgPositionRestrictionInfo>();
        GenericQueryResults<OrgPositionRestrictionEntity> orgPositionRestrictions = orgPositionRestrictionCriteriaLookupService.lookup(OrgPositionRestrictionEntity.class, criteria);
        if (null != orgPositionRestrictions && orgPositionRestrictions.getResults().size() > 0) {
            for (OrgPositionRestrictionEntity orgPositionRestriction : orgPositionRestrictions.getResults()) {
                results.add(orgPositionRestriction.toDto());
            }
        }
        return results;
    }

    @Override
    public List<ValidationResultInfo> validateOrgPositionRestriction(String validationTypeKey, String orgId, String orgPersonRelationTypeKey, OrgPositionRestrictionInfo orgPositionRestrictionInfo, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public OrgPositionRestrictionInfo createOrgPositionRestriction(String orgId, String orgPersonRelationTypeKey, OrgPositionRestrictionInfo orgPositionRestrictionInfo, ContextInfo contextInfo) throws AlreadyExistsException, DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgPositionRestrictionInfo updateOrgPositionRestriction(String orgPositionRestrictionId, OrgPositionRestrictionInfo orgPositionRestrictionInfo, ContextInfo contextInfo) throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        if(!orgPositionRestrictionId.equals(orgPositionRestrictionInfo.getId())) {
            throw new InvalidParameterException(orgPositionRestrictionId + " does not match the id in the object " + orgPositionRestrictionInfo.getId());
        }
        OrgPositionRestrictionEntity entity = orgPositionRestrictionDao.find(orgPositionRestrictionId);
        if(null == entity) {
            throw new DoesNotExistException(orgPositionRestrictionId);
        }
        entity.fromDto(orgPositionRestrictionInfo);
        entity.setEntityUpdated(contextInfo);
        entity = orgPositionRestrictionDao.merge(entity);
        orgPositionRestrictionDao.getEm().flush();
        return entity.toDto();
    }

    @Override
    public StatusInfo deleteOrgPositionRestriction(String orgPositionRestrictionId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        OrgPositionRestrictionEntity entity = orgPositionRestrictionDao.find(orgPositionRestrictionId);
        if (null == entity) {
            throw new DoesNotExistException(orgPositionRestrictionId);
        }
        orgPositionRestrictionDao.remove(entity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public Boolean isDescendant(String orgId, String descendantOrgId, String orgHierarchyId, ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getAllDescendants(String orgId, String orgHierarchyId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getAllAncestors(String orgId, String orgHierarchyId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrgTreeViewInfo getOrgTree(String rootOrgId, String orgHierarchyId, int maxLevels, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TypeInfo> getSearchTypes(ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TypeInfo getSearchType(String searchTypeKey, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SearchResultInfo search(SearchRequestInfo searchRequestInfo, ContextInfo contextInfo) throws MissingParameterException, InvalidParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
