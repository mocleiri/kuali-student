package com.sigmasys.kuali.ksa.service.impl;


import com.sigmasys.kuali.ksa.service.LanguageService;
import com.sigmasys.kuali.ksa.service.LocalizationService;
import com.sigmasys.kuali.ksa.service.xliff.Xliff;
import com.sigmasys.kuali.ksa.service.xliff.XliffParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param content    the content of the resources to be imported
     * @param importType Import type
     */
    @Override
    @Transactional(readOnly = false)
    public void importResources(String content, ImportType importType) {
        Xliff xliff = xliffParser.parse(content);
        // TODO
    }

}
