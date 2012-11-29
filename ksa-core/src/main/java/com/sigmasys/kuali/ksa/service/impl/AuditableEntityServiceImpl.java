package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * AuditableEntityService implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("auditableEntityService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AuditableEntityServiceImpl extends GenericPersistenceService implements AuditableEntityService {


    private static final Log logger = LogFactory.getLog(AuditableEntityServiceImpl.class);

    /**
     * Returns AuditableEntity by ID
     *
     * @param id         AuditableEntity ID
     * @param entityType Class instance of AuditableEntity subclass
     * @return AuditableEntity instance
     */
    @Override
    public <T extends AuditableEntity> T getAuditableEntity(Long id, Class<T> entityType) {
        return getEntity(id, entityType);
    }

    /**
     * Returns AuditableEntity by code
     *
     * @param code       AuditableEntity code
     * @param entityType Class instance of AuditableEntity subclass
     * @return AuditableEntity instance
     */
    @Override
    public <T extends AuditableEntity> T getAuditableEntity(String code, Class<T> entityType) {
        Query query = em.createQuery("select ae from " + entityType.getName() + " ae where ae.code = :code");
        query.setParameter("code", code);
        List<T> entities = query.getResultList();
        return (entities != null && !entities.isEmpty()) ? entities.get(0) : null;
    }

    /**
     * Returns all AuditableEntity instances sorted by ID in the descendant order for the given entity type
     *
     * @param entityType Class instance of AuditableEntity subclass
     * @return List of Information instances
     */
    @Override
    public <T extends AuditableEntity> List<T> getAuditableEntities(Class<T> entityType) {
        return getEntities(entityType, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Persists the AuditableEntity entity in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param entity AuditableEntity instance
     * @return Entity ID
     */
    @Override
    @Transactional(readOnly = true)
    public Long persistAuditableEntity(AuditableEntity entity) {
        return persistEntity(entity);
    }

}
