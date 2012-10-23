package com.sigmasys.bsinas.service;


import org.collegeboard.inas._2012.input.NeedAnalysisInput;
import org.collegeboard.inas._2012.output.NeedAnalysisOutput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_CONTEXT})
@Transactional
public class BsinasEngineTest extends AbstractServiceTest {

    @Autowired
    private BsinasService bsinasService;



    @Test
    public void runEngine1() throws Exception {

        NeedAnalysisInput input = new NeedAnalysisInput();
        input.setAwardYear(2012);

        NeedAnalysisOutput output = bsinasService.runEngine(input);

        Assert.notNull(output);

        logger.info("NeedAnalysisOutput XML: \n" + bsinasService.parseResponse(output));

    }

}
