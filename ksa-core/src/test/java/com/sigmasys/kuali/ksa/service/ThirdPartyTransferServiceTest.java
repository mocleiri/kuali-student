package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.tp.ThirdPartyPlan;
import com.sigmasys.kuali.ksa.service.tp.ThirdPartyTransferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ThirdPartyTransferService tests.
 *
 * @author Michael Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class ThirdPartyTransferServiceTest extends AbstractServiceTest {

    protected static final String THIRD_PARTY_ACCOUNT_ID = "third_party_account";


    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private ThirdPartyTransferService thirdPartyTransferService;

    @Autowired
    private TransactionTransferService transferService;

    @Autowired
    private GeneralLedgerService glService;

    @Autowired
    private AccountService accountService;


    private Account adminAccount;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        adminAccount = accountService.getOrCreateAccount(userId);
    }

    protected ThirdPartyAccount createThirdPartyAccount() {

        // TODO: replace it with a call to createThirdPartyAccount() method of AccountService when it is ready

        OrgName orgName = new OrgName();
        orgName.setName("Org 1");
        orgName.setCreatorId("admin");
        orgName.setLastUpdate(new Date());
        orgName.setContact(adminAccount.getDefaultPersonName());

        em.persist(orgName);

        ThirdPartyAccount account = new ThirdPartyAccount();
        account.setId(THIRD_PARTY_ACCOUNT_ID);
        account.setAbleToAuthenticate(true);
        account.setCreationDate(new Date());
        account.setCreatorId("admin");
        account.setOrgName(orgName);
        account.setCreditLimit(new BigDecimal(10e6));

        em.persist(account);

        return account;
    }

    protected GeneralLedgerType createGeneralLedgerType() throws Exception {

        String GL_ACCOUNT_ID = "01-0-131120 1326";

        GeneralLedgerType glType = glService.createGeneralLedgerType("GL_TYPE1", "Test GL type1",
                "Test GL Description 1", GL_ACCOUNT_ID, GlOperationType.CREDIT);

        Assert.notNull(glType);
        Assert.notNull(glType.getId());
        Assert.notNull(glType.getCode());
        Assert.notNull(glType.getName());
        Assert.notNull(glType.getDescription());
        Assert.notNull(glType.getGlAccountId());
        Assert.notNull(glType.getGlOperationOnCharge());
        Assert.notNull(glType.getCreationDate());
        Assert.notNull(glType.getCreatorId());

        Assert.isTrue(GlOperationType.CREDIT == glType.getGlOperationOnCharge());

        return glType;
    }


    @Test
    public void createThirdPartyPlan() throws Exception {

        GeneralLedgerType glType = createGeneralLedgerType();

        TransferType transferType = transferService.createTransferType(glType.getId(), "_TT_1", "TT 1", "Transfer Type 1");

        Assert.notNull(transferType);
        Assert.notNull(transferType.getId());

        ThirdPartyAccount account = createThirdPartyAccount();

        Assert.notNull(account);
        Assert.notNull(account.getId());

        ThirdPartyPlan plan = thirdPartyTransferService.createThirdPartyPlan(
                "TPP code1",
                "TPP name 1",
                "TPP description 1",
                transferType.getId(),
                account.getId(),
                new BigDecimal(10e4),
                new Date(),
                null,
                new Date(),
                new Date(System.currentTimeMillis() + (long) 10e6),
                new Date(System.currentTimeMillis() + (long) 10e3),
                new Date(System.currentTimeMillis() + (long) 10e8));

        Assert.notNull(plan);
        Assert.notNull(plan.getId());
        Assert.notNull(plan.getThirdPartyAccount());
        Assert.notNull(plan.getTransferType());

        Assert.isTrue(plan.getThirdPartyAccount().getId().equals(THIRD_PARTY_ACCOUNT_ID));

    }


}
