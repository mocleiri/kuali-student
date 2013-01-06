package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class AtpServiceTest extends AbstractServiceTest {

    @Autowired
    private AtpService atpService;


    @Test
    public void getAtpsByDates() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date startDate = dateFormat.parse("01/01/1970");
        Date endDate = dateFormat.parse("01/01/2020");

        List<AtpInfo> atpInfos = atpService.getAtpsByDates(startDate, endDate, new ContextInfo());

        Assert.notNull(atpInfos);
        Assert.notEmpty(atpInfos);
    }

}
