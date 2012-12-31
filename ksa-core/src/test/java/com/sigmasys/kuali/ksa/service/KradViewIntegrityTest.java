package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.config.KsaConfigurer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.service.impl.ViewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class KradViewIntegrityTest extends AbstractServiceTest {

    public static final String KSA_VIEW_FOLDER = "com/sigmasys/kuali/ksa/krad";

    @Autowired
    private KsaConfigurer ksaConfigurer;

    @Test
    public void parseViews() throws Exception {

        List<String> locations = ksaConfigurer.getDataDictionaryPackages();

        Assert.notNull(locations);
        Assert.notEmpty(locations);

        DataDictionaryService dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();

        Assert.notNull(dataDictionaryService);

        dataDictionaryService.addDataDictionaryLocations(locations);

        ViewServiceImpl viewService = (ViewServiceImpl) KRADServiceLocatorWeb.getViewService();

        Assert.notNull(viewService);

        viewService.setDataDictionaryService(dataDictionaryService);

        DataDictionary dataDictionary = dataDictionaryService.getDataDictionary();

        Assert.notNull(dataDictionary);

        List<String> configFileLocations = dataDictionary.getConfigFileLocations();

        logger.debug("Config File Locations: \n" + configFileLocations);

        boolean viewsExist = false;
        for (String location : configFileLocations) {
            if (location.contains(KSA_VIEW_FOLDER)) {
                viewsExist = true;
                break;
            }
        }

        if (!viewsExist) {
            throw new AssertionError("KSA KRAD view definitions located in '" + KSA_VIEW_FOLDER +
                    "' folder have not been loaded ");
        }

        dataDictionary.parseDataDictionaryConfigurationFiles(false);
    }

}