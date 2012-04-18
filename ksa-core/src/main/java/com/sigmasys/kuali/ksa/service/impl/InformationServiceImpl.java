package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.InformationService;
import com.sigmasys.kuali.ksa.service.TransactionService;
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
@Service("currencyService")
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
     * Persists the memo in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param memo Memo instance
     * @return Memo ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistMemo(Memo memo) {
        return persistEntity(memo);
    }

    /**
     * Removes the memo from the database.
     *
     * @param id Memo ID
     * @return true if the Memo entity has been deleted
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteMemo(Long id) {
        return deleteEntity(id, Memo.class);
    }

}
