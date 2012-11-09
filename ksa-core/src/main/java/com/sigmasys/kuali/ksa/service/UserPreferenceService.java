package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.UserPreference;
import java.util.List;

/**
 * User Preference Service interface
 *
 * @author Michael Ivanov
 */
public interface UserPreferenceService  {


    /**
     * Returns the list of user preferences for the given user ID
     *
     * @param userId User ID
     * @return a list of preferences
     */
    List<UserPreference> getUserPreferences(String userId);
    
    /**
     * Returns a single user preference with the specified name for the given user ID.
     * 
     * @param userId User ID
     * @param prefName Preference name.
     * @return A single user preference with the matching name or <code>null</code> if such a preference does not exist.
     */
    UserPreference getUserPreference(String userId, String prefName);
    
    /**
     * Returns the value of a user preference with the specified name for the given user ID.
     * 
     * @param userId User ID
     * @param prefName Preference name.
     * @return The value of a user preference with the matching name or <code>null</code> if such a preference does not exist.
     */
    String getUserPreferenceValue(String userId, String prefName);

    /**
     * Creates or updates the user preference in the database
     *
     * @param userPref instance of UserPreference
     * @return updated user preference instance
     */
    UserPreference persistUserPreference(UserPreference userPref);

    /**
     * Creates a new user preference in the database
     *
     * @param prefName Preference name
     * @param prefValue Preference value
     * @return user preference instance
     */
    UserPreference createUserPreference(String userId, String prefName, String prefValue);

}
