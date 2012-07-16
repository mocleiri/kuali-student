package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;

/**
 * User Preference Service implementation
 *
 * @author Michael Ivanov
 */
@Service("userPreferenceService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class UserPreferenceServiceImpl extends GenericPersistenceService implements UserPreferenceService {


    @Autowired
    private AccountService accountService;


    /**
     * Returns the list of user preferences for the given user ID
     *
     * @param userId User ID
     * @return a list of preferences
     */
    @Override
    public List<UserPreference> getUserPreferences(String userId) {
        Query query = em.createQuery("select p from " + UserPreference.class.getName() + " p where " +
                " p.accountId = :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Creates or updates the user preference in the database
     *
     * @param userPref instance of UserPreference
     * @return updated user preference instance
     */
    @Override
    @Transactional(readOnly = false)
    public UserPreference persistUserPreference(UserPreference userPref) {
        persistEntity(userPref);
        return userPref;
    }

    /**
     * Creates a new user preference in the database
     *
     * @return user preference instance
     */
    @Override
    @Transactional(readOnly = false)
    public UserPreference createUserPreference(String userId, String prefName, String prefValue) {
        accountService.getOrCreateAccount(userId);
        UserPreference userPref = new UserPreference(userId, prefName, prefValue);
        persistEntity(userPref);
        return userPref;
    }

}
