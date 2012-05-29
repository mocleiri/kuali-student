package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class InformationServiceTest extends AbstractServiceTest {

    @Autowired
    private InformationService informationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    @Test
    public void getInformations() throws Exception {

        informationService.createMemo("admin", "New memo for 1020", 0, new Date(), null, null);

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

        Memo memo = new Memo();
        memo.setText("Memo text");
        memo.setCreatorId("admin");
        memo.setCreationDate(new Date());
        memo.setEffectiveDate(new Date());

        final Long id = informationService.persistInformation(memo);

        memo = informationService.getMemo(id);

        Assert.notNull(memo);
        Assert.notNull(memo.getId());
        Assert.isTrue(memo.getId().equals(id));

        memo.setText("Blah Blah Blah");

        informationService.persistInformation(memo);

        memo = informationService.getMemo(id);

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

    @Test
    public void createMemoForTransaction() throws Exception {

        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e8));

        Assert.notNull(transaction);
        Assert.notNull(transaction.getId());

        Memo memo = informationService.createMemo(transaction.getId(), "New memo for 1020", 0, new Date(), null, null);

        Assert.notNull(memo);
        Assert.notNull(memo.getId());
        Assert.notNull(memo.getAccount());
        Assert.notNull(memo.getTransaction());

        Assert.isTrue("New memo for 1020".equals(memo.getText()));
        Assert.isTrue(TEST_USER_ID.equals(memo.getCreatorId()));
        Assert.isTrue(TEST_USER_ID.equals(memo.getResponsibleEntity()));

        Assert.isTrue(new Date().compareTo(memo.getEffectiveDate()) >= 0);
        Assert.isTrue(new Date().compareTo(memo.getCreationDate()) >= 0);

        Assert.isTrue(0 == memo.getAccessLevel());

        Assert.isNull(memo.getExpirationDate());
        Assert.isNull(memo.getPreviousMemo());

    }

    @Test
    public void createAlertForTransaction() throws Exception {

        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e8));

        Assert.notNull(transaction);
        Assert.notNull(transaction.getId());

        Alert alert = informationService.createAlert(transaction.getId(), "New alert for 1020", 0, new Date(), null);

        Assert.notNull(alert);
        Assert.notNull(alert.getId());
        Assert.notNull(alert.getAccount());
        Assert.notNull(alert.getTransaction());

        Assert.isTrue("New alert for 1020".equals(alert.getText()));
        Assert.isTrue(TEST_USER_ID.equals(alert.getCreatorId()));
        Assert.isTrue(TEST_USER_ID.equals(alert.getResponsibleEntity()));

        Assert.isTrue(new Date().compareTo(alert.getEffectiveDate()) >= 0);
        Assert.isTrue(new Date().compareTo(alert.getCreationDate()) >= 0);

        Assert.isTrue(0 == alert.getAccessLevel());

        Assert.isNull(alert.getExpirationDate());

    }

    @Test
    public void createFlagForTransaction() throws Exception {

        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e8));

        Assert.notNull(transaction);
        Assert.notNull(transaction.getId());

        Flag flag = informationService.createFlag(transaction.getId(), 1L, 1, 1, new Date(), null);

        Assert.notNull(flag);
        Assert.notNull(flag.getId());
        Assert.notNull(flag.getAccount());
        Assert.notNull(flag.getTransaction());

        Assert.isTrue(TEST_USER_ID.equals(flag.getCreatorId()));
        Assert.isTrue(TEST_USER_ID.equals(flag.getResponsibleEntity()));

        Assert.isTrue(new Date().compareTo(flag.getEffectiveDate()) >= 0);
        Assert.isTrue(new Date().compareTo(flag.getCreationDate()) >= 0);

        Assert.isTrue(1 == flag.getAccessLevel());

        Assert.isNull(flag.getExpirationDate());

    }


}
