package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.Url;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.LocalizedString;

import javax.jws.WebService;
import java.util.HashMap;

/**
 * Localization Service
 * <p/>
 *
 * @author Michael Ivanov
 */
@Url(LocalizationService.SERVICE_URL)
@WebService(serviceName = LocalizationService.SERVICE_NAME, portName = LocalizationService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
public interface LocalizationService {

    public static final String SERVICE_URL = "localization.webservice";
    public static final String SERVICE_NAME = "LocalizationService";
    public static final String PORT_NAME = SERVICE_NAME + "Port";

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
    HashMap<String, LocalizedString> getLocalizedStrings(String locale);

}
