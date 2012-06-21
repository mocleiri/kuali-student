package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import static com.sigmasys.kuali.ksa.service.LocalizationService.ImportType;


@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class LocalizationServiceTest extends AbstractServiceTest {


    @Autowired
    private LocalizationService localizationService;

    @Test
    public void importResources() {

        String xliff = CommonUtils.getResourceAsString("xliff/us-jp-xliff.xml");

        localizationService.importResources(xliff, ImportType.FULL);

        // TODO: use assertions to check the result

    }

    // TODO: implement more tests


}
