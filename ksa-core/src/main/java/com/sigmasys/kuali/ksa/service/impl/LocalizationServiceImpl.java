package com.sigmasys.kuali.ksa.service.impl;


import com.sigmasys.kuali.ksa.service.LanguageService;
import com.sigmasys.kuali.ksa.service.LocalizationService;
import com.sigmasys.kuali.ksa.service.xliff.XliffParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Reader;

/**
 * Localization Service
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("localizationService")
@Transactional(readOnly = true)
public class LocalizationServiceImpl implements LocalizationService {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private XliffParser xliffParser;

    /**
     * Imports the locale-aware resources specified by java.io.Reader for the given import type.
     *
     * @param reader java.io.Reader of the resources to be imported
     * @param importType Import type
     */
    @Override
    @Transactional(readOnly = false)
    public void importResources(Reader reader, ImportType importType) {
         // TODO
    }

}
