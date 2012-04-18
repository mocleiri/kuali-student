package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.Information;
import com.sigmasys.kuali.ksa.model.Memo;

import java.util.List;

/**
 * Information Service
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface InformationService {

    /**
     * Returns Information by ID
     *
     * @param id Information ID
     * @return Information instance
     */
    Information getInformation(Long id);

    /**
     * Returns Memo by ID
     *
     * @param id Memo ID
     * @return Memo instance
     */
    Memo getMemo(Long id);

    /**
     * Returns all Information entities sorted by ID in the descendant order
     *
     * @return List of Information instances
     */
    List<Information> getInformations();

    /**
     * Returns all Memo entities sorted by ID in the descendant order
     *
     * @return List of memos
     */
    List<Memo> getMemos();

    /**
     * Persists the memo in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param memo Memo instance
     * @return Memo ID
     */
    Long persistMemo(Memo memo);

    /**
     * Removes the memo from the database.
     *
     * @param id Memo ID
     * @return true if the Memo entity has been deleted
     */
    boolean deleteMemo(Long id);

}
