package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Generic JPA persistence service
 * <p/>
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("persistenceService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class GenericPersistenceService {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private UserSessionManager userSessionManager;


    /**
     * Returns Identifiable entity by ID
     *
     * @param id Entity ID
     * @return Identifiable instance
     */
    public <T extends Identifiable> T getEntity(Serializable id, Class<T> entityClass) {
        return em.find(entityClass, id);
    }


    /**
     * Returns the list of all entities for the given class
     *
     * @return List of Identifiable objects
     */
    public <T extends Identifiable> List<T> getEntities(Class<T> entityClass, Pair<String, SortOrder>... orderBy) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = criteriaBuilder.createQuery(entityClass);
        Root<T> selection = criteria.from(entityClass);
        selection.alias(entityClass.getSimpleName().toLowerCase());

        criteria.select(selection);

        Order[] orders = new Order[orderBy.length];
        int i = 0;
        for (Pair<String, SortOrder> order : orderBy) {
            Path field = selection.get(order.getA());
            SortOrder sortOrder = order.getB();
            orders[i++] = (SortOrder.ASC == sortOrder) ? criteriaBuilder.asc(field) : criteriaBuilder.desc(field);
        }

        criteria.orderBy(orders);

        Query query = em.createQuery(criteria.select(selection));

        return query.getResultList();
    }

    /**
     * Persists the entity in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param entity Identifiable instance
     * @return Entity ID
     */
    @Transactional(readOnly = false)
    public <T extends Serializable> T persistEntity(Identifiable entity) {

        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            auditableEntity.setLastUpdate(new Date());
            String userId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
            if (auditableEntity.getId() == null) {
                auditableEntity.setCreatorId(userId);
            } else {
                auditableEntity.setEditorId(userId);
            }
        }

        // Persisting the JPA entity with Entity Manager
        if (entity.getId() == null) {
            em.persist(entity);
        }

        em.flush();

        return (T) entity.getId();
    }

}
