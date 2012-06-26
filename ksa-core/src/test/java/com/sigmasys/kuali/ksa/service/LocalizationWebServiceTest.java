package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_WS_CONTEXT})
public class LocalizationWebServiceTest extends LocalizationServiceTest {

    @Test
    public void importResources() {
        super.importResources();
    }

    // TODO: implement more tests


}
