package com.sigmasys.kuali.ksa.service;


import java.io.Reader;

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
     * @param reader java.io.Reader of the resources to be imported
     * @param importType Import type
     */
    void importResources(Reader reader, ImportType importType);

}
