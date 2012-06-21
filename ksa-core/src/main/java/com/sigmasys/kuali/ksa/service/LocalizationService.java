package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.LocalizedString;

import java.util.Map;

/**
 * Localization Service
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface LocalizationService {

    /**
     * Import type of locale-aware resources
     */
    public static enum ImportType {
        FULL,
        FULL_NO_OVERRIDE,
        NEW_ONLY
    }

    /**
     * Imports the locale-aware resources specified by java.io.Reader for the given import type.
     *
     * @param content    the content of the resources to be imported
     * @param importType Import type
     */
    void importResources(String content, ImportType importType);

    /**
     * Returns all the localized strings for the given locale
     *
     * @param locale the locale string, i.e. "en_US", "fr_FR"
     * @return  a map of localized strings
     */
    Map<String, LocalizedString> getLocalizedStrings(String locale);

}
