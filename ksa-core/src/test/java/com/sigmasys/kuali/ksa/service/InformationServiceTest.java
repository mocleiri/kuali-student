package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.model.Information;
import com.sigmasys.kuali.ksa.model.Memo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class InformationServiceTest extends AbstractServiceTest {

    @Autowired
    private InformationService informationService;

    @Test
    public void getInformation() throws Exception {

        Information information = informationService.getInformation(1L);

        Assert.notNull(information);
        Assert.isTrue(information.getId() == 1L);

    }

    @Test
    public void getInformations() throws Exception {

        List<Information> informations = informationService.getInformations();

        Assert.notNull(informations);
        Assert.isTrue(!informations.isEmpty());

        for (Information information : informations) {
            Assert.notNull(information);
            Assert.notNull(information.getId());
        }

    }

    @Test
    public void updateMemo() throws Exception {

        Memo memo = informationService.getMemo(1L);

        Assert.notNull(memo);
        Assert.isTrue(memo.getId() == 1L);

        memo.setText("Blah Blah Blah");

        informationService.persistInformation(memo);

        memo = informationService.getMemo(1L);

        Assert.notNull(memo);
        Assert.isTrue(memo.getText().equals("Blah Blah Blah"));

    }

    @Test
    public void createMemo() throws Exception {

        Memo memo = new Memo();
        memo.setText("Memo text");
        memo.setCreatorId("admin");
        memo.setCreationDate(new Date());
        memo.setEffectiveDate(new Date());

        Long id = informationService.persistInformation(memo);

        Assert.notNull(id);

        memo = informationService.getMemo(id);

        Assert.notNull(memo);
        Assert.isTrue(memo.getId().equals(id));
        Assert.isTrue(memo.getText().equals("Memo text"));

    }

    @Test
    public void deleteMemo() throws Exception {

        Memo memo = new Memo();
        memo.setText("Memo text");
        memo.setCreatorId("admin");
        memo.setCreationDate(new Date());
        memo.setEffectiveDate(new Date());

        Long id = informationService.persistInformation(memo);

        Assert.notNull(id);

        memo = informationService.getMemo(id);

        Assert.notNull(memo);
        Assert.isTrue(memo.getId().equals(id));
        Assert.isTrue(memo.getText().equals("Memo text"));

        boolean isDeleted = informationService.deleteInformation(memo.getId());

        Assert.isTrue(isDeleted);

    }

}
