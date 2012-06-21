package com.sigmasys.kuali.ksa.service.impl;


import com.sigmasys.kuali.ksa.model.LocalizedString;
import com.sigmasys.kuali.ksa.model.LocalizedStringId;
import com.sigmasys.kuali.ksa.service.LocalizationService;
import com.sigmasys.kuali.ksa.service.xliff.TransUnit;
import com.sigmasys.kuali.ksa.service.xliff.Xliff;
import com.sigmasys.kuali.ksa.service.xliff.XliffParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Localization Service
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("localizationService")
@Transactional(readOnly = true)
public class LocalizationServiceImpl extends GenericPersistenceService implements LocalizationService {

    private static final Log logger = LogFactory.getLog(LocalizationServiceImpl.class);

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

        String sourceLocale = xliff.getSourceLocale();
        String targetLocale = xliff.getTargetLocale();

        Map<String, TransUnit> transUnits = xliff.getTransUnits();
        if (transUnits != null) {
            for (TransUnit transUnit : transUnits.values()) {
                persistTransUnit(transUnit, sourceLocale, importType, true);
                persistTransUnit(transUnit, targetLocale, importType, false);
            }
        }

    }

    private void persistTransUnit(TransUnit transUnit, String locale, ImportType importType, boolean isSource) {

        LocalizedStringId id = new LocalizedStringId(transUnit.getId(), locale);
        LocalizedString localizedString = getEntity(id, LocalizedString.class);
        boolean isOverridden = (localizedString != null);

        if (localizedString != null) {
            if (ImportType.NEW_ONLY == importType) {
                logger.info("The following localized string is ignored because it already exists and " +
                        "the import type is set to 'NEW_ONLY': " + localizedString);
                return;
            } else if (ImportType.FULL_NO_OVERRIDE == importType) {
                Boolean canBeOverridden = localizedString.getOverridden();
                if (canBeOverridden != null && !canBeOverridden) {
                    logger.info("The following localized string is ignored because it already exists, " +
                            "cannot be overridden and the import type is set to 'FULL_NO_OVERRIDE': " +
                            localizedString);
                    return;
                }
            }
        } else {
            localizedString = new LocalizedString();
            localizedString.setId(id);
        }

        localizedString.setValue(isSource ? transUnit.getSource() : transUnit.getTarget());
        localizedString.setMaxLength(transUnit.getMaxBytes());
        localizedString.setOverridden(isOverridden);
        persistEntity(localizedString);
    }

}
