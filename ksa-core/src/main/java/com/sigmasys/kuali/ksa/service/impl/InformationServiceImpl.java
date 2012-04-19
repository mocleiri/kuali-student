package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * Information service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("informationService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class InformationServiceImpl extends GenericPersistenceService implements InformationService {

    /**
     * Returns Information by ID
     *
     * @param id Information ID
     * @return Information instance
     */
    @Override
    public Information getInformation(Long id) {
        return getEntity(id, Information.class);
    }

    /**
     * Returns Memo by ID
     *
     * @param id Memo ID
     * @return Memo instance
     */
    @Override
    public Memo getMemo(Long id) {
        return getEntity(id, Memo.class);
    }


    /**
     * Returns all Information instances sorted by ID in the descendant order
     *
     * @return List of Information instances
     */
    @Override
    public List<Information> getInformations() {
        return getEntities(Information.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all Memo entities sorted by ID in the descendant order
     *
     * @return List of memos
     */
    @Override
    public List<Memo> getMemos() {
        return getEntities(Memo.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all Alert entities by Account ID
     *
     * @param userId Account ID
     * @return List of memos
     */
    @Override
    public List<Alert> getAlerts(String userId) {
        Query query = em.createQuery("select a from Alert a where a.account.id = :userId order by a.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Returns all Flag entities by Account ID
     *
     * @param userId Account ID
     * @return List of flags
     */
    public List<Flag> getFlags(String userId) {
        Query query = em.createQuery("select f from Flag f where f.account.id = :userId order by f.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Returns all Memo entities by Account ID
     *
     * @param userId Account ID
     * @return List of memos
     */
    @Override
    public List<Memo> getMemos(String userId) {
        Query query = em.createQuery("select m from Memo m where m.account.id = :userId order by m.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Persists Information in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param information Information instance
     * @return Information ID
     */
    @Override
    public Long persistInformation(Information information) {
        return persistEntity(information);
    }

    /**
     * Removes Information from the database.
     *
     * @param id Information ID
     * @return true if Information entity has been deleted
     */
    public boolean deleteInformation(Long id) {
        return deleteEntity(id, Information.class);
    }

}
