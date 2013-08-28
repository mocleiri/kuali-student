package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.KeyPair;
import com.sigmasys.kuali.ksa.model.fm.*;
import com.sigmasys.kuali.ksa.service.fm.RateService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * RateService tests.
 *
 * @author Michael Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
public class RateServiceTest extends AbstractServiceTest {


    @Autowired
    private RateService rateService;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
        _createRateType("RT_2013", "RateType_2013", "2013 Rate type description");
    }

    private RateType _createRateType(String code, String name, String description) {

        RateType rateType = rateService.createRateType(code, name, description);

        Assert.notNull(rateType);
        Assert.notNull(rateType.getId());
        Assert.notNull(rateType.getCode());
        Assert.notNull(rateType.getName());
        Assert.notNull(rateType.getDescription());
        Assert.notNull(rateType.getCreatorId());
        Assert.notNull(rateType.getCreationDate());

        return rateType;
    }

    private RateCatalog _createRateCatalog(String rateCatalogCode, String... atpIds) {

        String rateTypeCode = "RT_2013";
        String transactionTypeId = "cash";
        TransactionDateType dateType = TransactionDateType.ALWAYS;

        BigDecimal minAmount = new BigDecimal(10.99);
        BigDecimal maxAmount = new BigDecimal(300000.67);

        BigDecimal minLimitAmount = new BigDecimal(2.01);
        BigDecimal maxLimitAmount = new BigDecimal(200033.01);

        //BigDecimal limitAmount = new BigDecimal(200.88);

        int minLimitUnits = 0;
        int maxLimitUnits = 10;

        List<KeyPair> keyPairs = Arrays.asList(new KeyPair("key1", "value1"));

        RateCatalog rateCatalog = rateService.createRateCatalog(rateCatalogCode, rateTypeCode, transactionTypeId,
                dateType, minAmount, maxAmount, minLimitAmount, maxLimitAmount, minLimitUnits, maxLimitUnits,
                Arrays.asList(atpIds), keyPairs, false, false, false, false, false, false);

        Assert.notNull(rateCatalog);
        Assert.notNull(rateCatalog.getId());
        Assert.notNull(rateCatalog.getCode());
        Assert.isTrue(rateCatalog.getCode().equals(rateCatalogCode));
        Assert.notNull(rateCatalog.getMinAmount());
        Assert.notNull(rateCatalog.getMaxAmount());
        Assert.notNull(rateCatalog.getMinLimitAmount());
        Assert.notNull(rateCatalog.getMaxLimitAmount());
        Assert.notNull(rateCatalog.getKeyPairs());
        Assert.notEmpty(rateCatalog.getKeyPairs());
        Assert.notNull(rateCatalog.getTransactionDateType());

        Set<String> atpIdsSet = rateService.getAtpsForRateCatalog(rateCatalog.getId());

        Assert.notNull(atpIdsSet);
        Assert.notEmpty(atpIdsSet);

        return rateCatalog;

    }

    private RateCatalog _createRateCatalog(String rateCatalogCode) {
        return _createRateCatalog(rateCatalogCode, "19871", "19872", "19873", "19874");
    }

    private Rate _createRate(String rateCode, String subCode, String rateCatalogCode, String atpId) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);

        Date transactionDate = dateFormat.parse("05/23/2012");
        Date recognitionDate = null;

        String transactionTypeId = "cash";

        String rateName = rateCode + " name";

        int minLimitUnits = 0;
        int maxLimitUnits = 10;

        TransactionDateType dateType = TransactionDateType.ALWAYS;
        BigDecimal limitAmount = new BigDecimal(7776000.1111);
        BigDecimal rateAmount = new BigDecimal(200.88);

        Rate rate = rateService.createRate(rateCode, subCode, rateName, rateCatalogCode, transactionTypeId, dateType,
                rateAmount, limitAmount, minLimitUnits, maxLimitUnits, transactionDate, recognitionDate, atpId, false);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());
        Assert.notNull(rate.getAtpId());
        Assert.notNull(rate.getCode());
        Assert.notNull(rate.getSubCode());
        Assert.notNull(rate.getRateCatalogAtp());

        RateCatalog rateCatalog = rate.getRateCatalogAtp().getRateCatalog();

        Assert.notNull(rateCatalog);
        Assert.notNull(rate.getRateCatalogAtp().getId());
        Assert.isTrue(rateCatalogCode.equals(rateCatalog.getCode()));
        Assert.isTrue(rateCatalogCode.equals(rate.getRateCatalogAtp().getId().getCode()));
        Assert.notNull(rate.getDefaultRateAmount());
        Assert.notNull(rate.getDefaultRateAmount().getAmount());
        Assert.isTrue(rate.getDefaultRateAmount().getAmount().compareTo(rateAmount) == 0);
        Assert.notNull(rate.getKeyPairs());
        Assert.notEmpty(rate.getKeyPairs());
        Assert.notNull(rate.getTransactionDateType());

        if (!rateCatalog.isRecognitionDateDefinable()) {
            Assert.isNull(rate.getRecognitionDate());
        }

        if (rateCatalog.isTransactionDateTypeFinal()) {
            Assert.isTrue(rate.getTransactionDateType() == rateCatalog.getTransactionDateType());
        }

        if (rateCatalog.isTransactionTypeFinal()) {
            Assert.isTrue(rate.getDefaultRateAmount().getTransactionTypeId().equals(rateCatalog.getTransactionTypeId()));
        }

        if (rateCatalog.getKeyPairs() != null && rateCatalog.isKeyPairFinal()) {
            Assert.isTrue(CollectionUtils.isEqualCollection(rate.getKeyPairs(), rateCatalog.getKeyPairs()));
        }

        Assert.isTrue(!rateCatalog.isLimitAmountFinal() || rateCatalog.isLimitAmount() == rate.isLimitAmount());

        return rate;

    }

    private Rate _createRate(String rateCode, String rateCatalogCode) throws Exception {
        return _createRate(rateCode, rateCode + "_subCode", rateCatalogCode, "19874");
    }

    @Test
    public void createRateType() {
        _createRateType("RT_2013_1", "RateType_2013_1", "2013 Rate type description");
    }

    @Test
    public void persistRateType() {

        RateType rateType = _createRateType("RT_2013_3", "RateType_2013_3", "2013 3 Rate type description");

        Assert.notNull(rateType);
        Assert.notNull(rateType.getId());

        Assert.isTrue("RT_2013_3".equals(rateType.getCode()));
        Assert.isTrue("RateType_2013_3".equals(rateType.getName()));

        rateType.setCode("RT_2013_4");
        rateType.setName("RateType_2013_4");

        Long rateTypeId = rateService.persistRateType(rateType);

        Assert.notNull(rateTypeId);
        Assert.isTrue(rateTypeId.equals(rateType.getId()));

        Assert.isTrue("RT_2013_4".equals(rateType.getCode()));
        Assert.isTrue("RateType_2013_4".equals(rateType.getName()));

        Assert.notNull(rateType.getLastUpdate());
        Assert.notNull(rateType.getEditorId());
        Assert.isTrue(rateType.getLastUpdate().after(rateType.getCreationDate()));

    }

    @Test
    public void deleteRateType() {

        RateType rateType = _createRateType("RT_2013_3", "RateType_2013_3", "2013 3 Rate type description");

        Assert.notNull(rateType);
        Assert.notNull(rateType.getId());

        Assert.isTrue("RT_2013_3".equals(rateType.getCode()));
        Assert.isTrue("RateType_2013_3".equals(rateType.getName()));

        rateService.deleteRateType(rateType.getId());

        Assert.isNull(rateService.getRateType(rateType.getId()));
        Assert.isNull(rateService.getRateTypeByCode(rateType.getCode()));

    }

    @Test
    public void deleteRateTypeByCode() {

        RateType rateType = _createRateType("RT_2013_3", "RateType_2013_3", "2013 3 Rate type description");

        Assert.notNull(rateType);
        Assert.notNull(rateType.getId());

        Assert.isTrue("RT_2013_3".equals(rateType.getCode()));
        Assert.isTrue("RateType_2013_3".equals(rateType.getName()));

        rateService.deleteRateTypeByCode(rateType.getCode());

        Assert.isNull(rateService.getRateType(rateType.getId()));
        Assert.isNull(rateService.getRateTypeByCode(rateType.getCode()));

    }

    @Test
    public void getRateTypesByNamePattern() {

        RateType rateType1 = _createRateType("RT-###^_111", "RateType_2013_##-456", "2013 3 Rate type description");

        RateType rateType2 = _createRateType("RT-##X_112", "RateType_2013_##-999", "2013 3 Rate type description");

        List<RateType> rateTypes = rateService.getRateTypesByNamePattern("##-");

        Assert.notNull(rateTypes);
        Assert.notEmpty(rateTypes);

        logger.debug("Number of rate types = " + rateTypes.size());

        Assert.isTrue(rateTypes.size() == 2);

        boolean rateType1Exists = false;
        boolean rateType2Exists = false;

        for (RateType rateType : rateTypes) {

            Assert.notNull(rateType);

            Assert.notNull(rateType.getId());

            Assert.notNull(rateType.getCode());

            logger.debug("RateType code = " + rateType.getCode());

            if (rateType.getId().equals(rateType1.getId())) {
                rateType1Exists = true;
            } else if (rateType.getId().equals(rateType2.getId())) {
                rateType2Exists = true;
            }
        }

        Assert.isTrue(rateType1Exists);
        Assert.isTrue(rateType2Exists);
    }

    @Test
    public void getAllRateTypes() {

        RateType rateType1 = _createRateType("RT-###^_111", "RateType_2013_##-456", "2013 3 Rate type description");

        RateType rateType2 = _createRateType("RT-##X_112", "RateType_2013_##-999", "2013 3 Rate type description");

        List<RateType> rateTypes = rateService.getAllRateTypes();

        Assert.notNull(rateTypes);
        Assert.notEmpty(rateTypes);

        logger.debug("Number of rate types = " + rateTypes.size());

        Assert.isTrue(rateTypes.size() >= 2);

        boolean rateType1Exists = false;
        boolean rateType2Exists = false;

        for (RateType rateType : rateTypes) {

            Assert.notNull(rateType);

            Assert.notNull(rateType.getId());

            Assert.notNull(rateType.getCode());

            logger.debug("RateType code = " + rateType.getCode());

            if (rateType.getId().equals(rateType1.getId())) {
                rateType1Exists = true;
            } else if (rateType.getId().equals(rateType2.getId())) {
                rateType2Exists = true;
            }
        }

        Assert.isTrue(rateType1Exists);
        Assert.isTrue(rateType2Exists);
    }

    @Test
    public void createRateCatalog() throws Exception {

        String rateCatalogCode = "RC_2013";

        _createRateCatalog(rateCatalogCode);
    }

    @Test
    public void getRateCatalogById() {

        String rateCatalogCode = "RC_2013_1";

        RateCatalog rateCatalog = _createRateCatalog(rateCatalogCode);

        Assert.notNull(rateCatalog);
        Assert.notNull(rateCatalog.getId());
        Assert.notNull(rateCatalog.getCode());
        Assert.isTrue(rateCatalogCode.equals(rateCatalog.getCode()));

        rateCatalog = rateService.getRateCatalog(rateCatalog.getId());

        Assert.notNull(rateCatalog);
        Assert.notNull(rateCatalog.getId());
        Assert.notNull(rateCatalog.getCode());
        Assert.isTrue(rateCatalogCode.equals(rateCatalog.getCode()));

    }

    @Test
    public void deleteRateCatalog() {

        String rateCatalogCode = "RC_2013_TO_DELETE";

        RateCatalog rateCatalog = _createRateCatalog(rateCatalogCode);

        Assert.notNull(rateCatalog);
        Assert.notNull(rateCatalog.getId());

        rateService.deleteRateCatalog(rateCatalog.getId());

        rateCatalog = rateService.getRateCatalog(rateCatalog.getId());

        Assert.isNull(rateCatalog);
    }

    @Test
    public void getRateCatalogsByCode() {

        String code = "RC_2013_EXT";

        RateCatalog rateCatalog1 = _createRateCatalog(code, "19871", "19872");

        Assert.notNull(rateCatalog1);
        Assert.notNull(rateCatalog1.getId());

        RateCatalog rateCatalog2 = _createRateCatalog(code, "19873", "19874");

        Assert.notNull(rateCatalog2);
        Assert.notNull(rateCatalog2.getId());

        List<RateCatalog> rateCatalogs = rateService.getRateCatalogsByCode(code);

        Assert.notNull(rateCatalogs);
        Assert.notEmpty(rateCatalogs);
        Assert.isTrue(rateCatalogs.size() == 2);

        boolean rateCatalog1Exists = false;
        boolean rateCatalog2Exists = false;

        for (RateCatalog rateCatalog : rateCatalogs) {

            Assert.notNull(rateCatalog);

            Assert.notNull(rateCatalog.getId());

            Assert.notNull(rateCatalog.getCode());

            logger.debug("RateCatalog code = " + rateCatalog.getCode() + ", ID = " + rateCatalog.getId());

            if (rateCatalog.getId().equals(rateCatalog1.getId())) {
                rateCatalog1Exists = true;
            } else if (rateCatalog.getId().equals(rateCatalog2.getId())) {
                rateCatalog2Exists = true;
            }
        }

        Assert.isTrue(rateCatalog1Exists);
        Assert.isTrue(rateCatalog2Exists);

    }

    @Test
    public void getRateCatalogsByAtpId() {

        RateCatalog rateCatalog1 = _createRateCatalog("RC_2013_EXT_1", "19871", "19872");

        Assert.notNull(rateCatalog1);
        Assert.notNull(rateCatalog1.getId());

        RateCatalog rateCatalog2 = _createRateCatalog("RC_2013_EXT_2", "19871", "19872");

        Assert.notNull(rateCatalog2);
        Assert.notNull(rateCatalog2.getId());

        List<RateCatalog> rateCatalogs = rateService.getRateCatalogsByAtpId("19871");

        Assert.notNull(rateCatalogs);
        Assert.notEmpty(rateCatalogs);
        Assert.isTrue(rateCatalogs.size() >= 2);

        boolean rateCatalog1Exists = false;
        boolean rateCatalog2Exists = false;

        for (RateCatalog rateCatalog : rateCatalogs) {

            Assert.notNull(rateCatalog);

            Assert.notNull(rateCatalog.getId());

            Assert.notNull(rateCatalog.getCode());

            logger.debug("RateCatalog code = " + rateCatalog.getCode() + ", ID = " + rateCatalog.getId());

            if (rateCatalog.getId().equals(rateCatalog1.getId())) {
                rateCatalog1Exists = true;
            } else if (rateCatalog.getId().equals(rateCatalog2.getId())) {
                rateCatalog2Exists = true;
            }
        }

        Assert.isTrue(rateCatalog1Exists);
        Assert.isTrue(rateCatalog2Exists);

    }

    @Test
    public void getAllRateCatalogs() {

        RateCatalog rateCatalog1 = _createRateCatalog("RC_2013_EXT_3", "19871", "19872");

        Assert.notNull(rateCatalog1);
        Assert.notNull(rateCatalog1.getId());

        RateCatalog rateCatalog2 = _createRateCatalog("RC_2013_EXT_4", "19871", "19872");

        Assert.notNull(rateCatalog2);
        Assert.notNull(rateCatalog2.getId());

        List<RateCatalog> rateCatalogs = rateService.getAllRateCatalogs();

        Assert.notNull(rateCatalogs);
        Assert.notEmpty(rateCatalogs);
        Assert.isTrue(rateCatalogs.size() >= 2);

        boolean rateCatalog1Exists = false;
        boolean rateCatalog2Exists = false;

        for (RateCatalog rateCatalog : rateCatalogs) {

            Assert.notNull(rateCatalog);

            Assert.notNull(rateCatalog.getId());

            Assert.notNull(rateCatalog.getCode());

            logger.debug("RateCatalog code = " + rateCatalog.getCode() + ", ID = " + rateCatalog.getId());

            if (rateCatalog.getId().equals(rateCatalog1.getId())) {
                rateCatalog1Exists = true;
            } else if (rateCatalog.getId().equals(rateCatalog2.getId())) {
                rateCatalog2Exists = true;
            }
        }

        Assert.isTrue(rateCatalog1Exists);
        Assert.isTrue(rateCatalog2Exists);

    }

    @Test
    public void createRate() throws Exception {

        String rateCatalogCode = "RC_2013";

        _createRateCatalog(rateCatalogCode);

        String rateCode = "R_2013";

        _createRate(rateCode, rateCatalogCode);
    }

    @Test
    public void deleteRate() throws Exception {

        String rateCatalogCode = "RC_2013";

        _createRateCatalog(rateCatalogCode);

        String rateCode = "R_2013_TO_DELETE";

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        rateService.deleteRate(rate.getId());

        rate = rateService.getRate(rate.getId());

        Assert.isNull(rate);
    }

    @Test
    public void getRateByCodeAndAtpId() throws Exception {

        String rateCatalogCode = "RC_2013_ATP";

        _createRateCatalog(rateCatalogCode, "19871", "19872");

        String rateCode = "R_2013_ATP";

        String subCode = "ATP_1";

        Rate rate = _createRate(rateCode, subCode, rateCatalogCode, "19872");

        Assert.notNull(rate);

        Long rateId = rate.getId();

        Assert.notNull(rateId);

        rate = rateService.getRate(rateCode, subCode, "19872");

        Assert.notNull(rate);
        Assert.notNull(rate.getId());
        Assert.isTrue(rateId.equals(rate.getId()));
        Assert.isTrue(rateCode.equals(rate.getCode()));

    }


    @Test
    public void getRatesByCode() throws Exception {

        String rateCatalogCode = "RC_2013_CODE";

        String rateCode = "R_2013_CODE";

        String subCode = "_1";

        _createRateCatalog(rateCatalogCode, "19871");
        _createRateCatalog(rateCatalogCode, "19872");

        Rate rate1 = _createRate(rateCode, subCode, rateCatalogCode, "19871");

        Assert.notNull(rate1);
        Assert.notNull(rate1.getCode());
        Assert.notNull(rate1.getAtpId());

        Rate rate2 = _createRate(rateCode, subCode, rateCatalogCode, "19872");

        Assert.notNull(rate2);
        Assert.notNull(rate2.getCode());
        Assert.notNull(rate2.getAtpId());

        List<Rate> rates = rateService.getRatesByCode(rateCode);

        Assert.notNull(rates);
        Assert.notEmpty(rates);

        Assert.isTrue(rates.size() == 2);

        boolean rate1Exists = false;
        boolean rate2Exists = false;

        for (Rate rate : rates) {

            Assert.notNull(rate.getCode());
            Assert.notNull(rate.getAtpId());

            Assert.isTrue(rate.getCode().equals(rateCode));

            logger.debug("Rate ATP ID = '" + rate.getAtpId() + "'");

            if (rate.getId().equals(rate1.getId())) {

                rate1Exists = true;

                Assert.isTrue("19871".equals(rate.getAtpId()));

            } else if (rate.getId().equals(rate2.getId())) {

                rate2Exists = true;

                Assert.isTrue("19872".equals(rate.getAtpId()));
            }
        }

        Assert.isTrue(rate1Exists);
        Assert.isTrue(rate2Exists);

    }


    @Test
    public void getRatesByCodeAndSubCode() throws Exception {

        String rateCatalogCode = "RC_2013_C1";

        String rateCode = "R_2013_C1";

        String subCode = "C1_S";

        _createRateCatalog(rateCatalogCode, "19871");
        _createRateCatalog(rateCatalogCode, "19872");

        Rate rate1 = _createRate(rateCode, subCode, rateCatalogCode, "19871");

        Assert.notNull(rate1);
        Assert.notNull(rate1.getCode());
        Assert.notNull(rate1.getAtpId());

        Rate rate2 = _createRate(rateCode, subCode, rateCatalogCode, "19872");

        Assert.notNull(rate2);
        Assert.notNull(rate2.getCode());
        Assert.notNull(rate2.getAtpId());

        List<Rate> rates = rateService.getRatesByCodeAndSubCode(rateCode, subCode);

        Assert.notNull(rates);
        Assert.notEmpty(rates);

        Assert.isTrue(rates.size() == 2);

        boolean rate1Exists = false;
        boolean rate2Exists = false;

        for (Rate rate : rates) {

            Assert.notNull(rate.getCode());
            Assert.notNull(rate.getSubCode());
            Assert.notNull(rate.getAtpId());

            Assert.isTrue(rate.getCode().equals(rateCode));
            Assert.isTrue(rate.getSubCode().equals(subCode));

            if (rate.getId().equals(rate1.getId())) {

                rate1Exists = true;

                Assert.isTrue("19871".equals(rate.getAtpId()));

            } else if (rate.getId().equals(rate2.getId())) {

                rate2Exists = true;

                Assert.isTrue("19872".equals(rate.getAtpId()));
            }
        }

        Assert.isTrue(rate1Exists);
        Assert.isTrue(rate2Exists);

    }

    @Test
    public void getRatesByAtpId() throws Exception {

        _createRateCatalog("RC_2013_ATP_1", "19874");
        _createRateCatalog("RC_2013_ATP_2", "19874");

        Rate rate1 = _createRate("R_2013_ATP_1", "RC_2013_ATP_1");

        Assert.notNull(rate1);
        Assert.notNull(rate1.getCode());
        Assert.notNull(rate1.getAtpId());

        Rate rate2 = _createRate("R_2013_ATP_2", "RC_2013_ATP_2");

        Assert.notNull(rate2);
        Assert.notNull(rate2.getCode());
        Assert.notNull(rate2.getAtpId());

        List<Rate> rates = rateService.getRatesByAtpId("19874");

        Assert.notNull(rates);
        Assert.notEmpty(rates);

        Assert.isTrue(rates.size() >= 2);

        boolean rate1Exists = false;
        boolean rate2Exists = false;

        for (Rate rate : rates) {

            Assert.notNull(rate.getCode());
            Assert.notNull(rate.getAtpId());

            Assert.isTrue(rate.getAtpId().equals("19874"));

            if (rate.getId().equals(rate1.getId())) {

                rate1Exists = true;

                Assert.isTrue("R_2013_ATP_1".equals(rate.getCode()));

            } else if (rate.getId().equals(rate2.getId())) {

                rate2Exists = true;

                Assert.isTrue("R_2013_ATP_2".equals(rate.getCode()));
            }
        }

        Assert.isTrue(rate1Exists);
        Assert.isTrue(rate2Exists);

    }


    @Test
    public void transferAtpsToRateCatalog() {

        String rateCatalogCode = "RC_2013";

        RateCatalog rateCatalog1 = _createRateCatalog(rateCatalogCode, "19871", "19872");

        Assert.notNull(rateCatalog1);

        RateCatalog rateCatalog2 = _createRateCatalog(rateCatalogCode, "19873");

        Assert.notNull(rateCatalog2);

        rateCatalog2 = rateService.transferAtpsToRateCatalog(rateCatalog2.getId(), "19871", "19872", "19874");

        Assert.notNull(rateCatalog2);

        Set<String> atpIds = rateService.getAtpsForRateCatalog(rateCatalog2.getId());

        Assert.notNull(atpIds);
        Assert.notEmpty(atpIds);

        logger.debug("Number of ATPs = " + atpIds.size());
        for (String atpId : atpIds) {
            logger.debug("ATP ID = " + atpId);
        }

        Assert.isTrue(atpIds.size() == 4);

        Assert.isTrue(atpIds.containsAll(Arrays.asList("19871", "19872", "19873", "19874")));

        atpIds = rateService.getAtpsForRateCatalog(rateCatalog1.getId());

        Assert.notNull(atpIds);

        Assert.isTrue(atpIds.size() == 0);

    }

    @Test
    public void assignAtpsToRateCatalog() {

        String rateCatalogCode = "RC_2013";

        String[] atpIds = {"19872", "19873"};

        RateCatalog rateCatalog = _createRateCatalog(rateCatalogCode, "19871");

        rateCatalog = rateService.assignAtpsToRateCatalog(rateCatalog.getId(), atpIds);

        Assert.notNull(rateCatalog);

        for (String atpId : atpIds) {

            RateCatalog rateCatalog1 = rateService.getRateCatalogByCodeAndAtpId(rateCatalogCode, atpId);

            Assert.notNull(rateCatalog1);

            Assert.isTrue(rateCatalog1.getId().equals(rateCatalog.getId()));
        }

        Set<String> atpIdSet = rateService.getAtpsForRateCatalog(rateCatalog.getId());

        Assert.notNull(atpIdSet);
        Assert.notEmpty(atpIdSet);

        Assert.isTrue(atpIdSet.contains("19871"));

        Assert.isTrue(atpIdSet.containsAll(Arrays.asList(atpIds)));

    }

    @Test
    public void addKeyPairToRate() throws Exception {

        String rateCatalogCode = "RC_2013";

        _createRateCatalog(rateCatalogCode);

        String rateCode = "R_2013";

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        rateService.addKeyPairToRate("key_45", "value_45", rate.getId());
        rate = rateService.getRate(rate.getId());

        Set<KeyPair> keyPairs = rate.getKeyPairs();

        Assert.notNull(keyPairs);
        Assert.notEmpty(keyPairs);

        Assert.isTrue(keyPairs.contains(new KeyPair("key_45", "value_45")));

    }

    @Test
    public void removeKeyPairFromRate() throws Exception {

        String rateCatalogCode = "RC_2013";

        _createRateCatalog(rateCatalogCode);

        String rateCode = "R_2013";

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        rateService.addKeyPairToRate("key_48", "value_48", rate.getId());
        rate = rateService.getRate(rate.getId());

        Set<KeyPair> keyPairs = rate.getKeyPairs();

        Assert.notNull(keyPairs);
        Assert.notEmpty(keyPairs);

        Assert.isTrue(keyPairs.contains(new KeyPair("key_48", "value_48")));

        int keyPairsSize = keyPairs.size();

        rateService.removeKeyPairFromRate("key_48", rate.getId());
        rate = rateService.getRate(rate.getId());

        keyPairs = rate.getKeyPairs();

        Assert.notNull(keyPairs);
        Assert.notEmpty(keyPairs);

        Assert.isTrue(!keyPairs.contains(new KeyPair("key_48", "value_48")));

        Assert.isTrue(keyPairsSize - keyPairs.size() == 1);

    }

    @Test
    public void addKeyPairToRateCatalog() throws Exception {

        String rateCatalogCode = "RC_2013";

        RateCatalog catalog = _createRateCatalog(rateCatalogCode);

        Assert.notNull(catalog);
        Assert.notNull(catalog.getId());

        String rateCode = "R_2013";

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        rateService.addKeyPairToRateCatalog("key_49", "value_49", catalog.getId());

        catalog = rateService.getRateCatalog(catalog.getId());
        rate = rateService.getRate(rate.getId());

        Set<KeyPair> catalogKeyPairs = catalog.getKeyPairs();

        Assert.notNull(catalogKeyPairs);
        Assert.notEmpty(catalogKeyPairs);
        Assert.isTrue(catalogKeyPairs.contains(new KeyPair("key_49", "value_49")));
        Assert.isTrue(catalogKeyPairs.contains(new KeyPair("key1", "value1")));

        Set<KeyPair> rateKeyPairs = rate.getKeyPairs();

        Assert.notNull(rateKeyPairs);
        Assert.notEmpty(rateKeyPairs);
        Assert.isTrue(rateKeyPairs.contains(new KeyPair("key_49", "value_49")));
        Assert.isTrue(rateKeyPairs.contains(new KeyPair("key1", "value1")));

        Assert.isTrue(catalogKeyPairs.size() == rateKeyPairs.size());
        Assert.isTrue(catalogKeyPairs.size() == 2);

        Assert.isTrue(CollectionUtils.isEqualCollection(catalogKeyPairs, rateKeyPairs));

    }

    @Test
    public void removeKeyPairFromRateCatalog() throws Exception {

        String rateCatalogCode = "RC_2013";

        RateCatalog catalog = _createRateCatalog(rateCatalogCode);

        Assert.notNull(catalog);
        Assert.notNull(catalog.getId());

        String rateCode = "R_2013";

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        rateService.addKeyPairToRateCatalog("key_49", "value_49", catalog.getId());

        catalog = rateService.getRateCatalog(catalog.getId());
        rate = rateService.getRate(rate.getId());

        Set<KeyPair> catalogKeyPairs = catalog.getKeyPairs();

        Assert.notNull(catalogKeyPairs);
        Assert.notEmpty(catalogKeyPairs);
        Assert.isTrue(catalogKeyPairs.contains(new KeyPair("key_49", "value_49")));
        Assert.isTrue(catalogKeyPairs.contains(new KeyPair("key1", "value1")));

        Set<KeyPair> rateKeyPairs = rate.getKeyPairs();

        Assert.notNull(rateKeyPairs);
        Assert.notEmpty(rateKeyPairs);
        Assert.isTrue(rateKeyPairs.contains(new KeyPair("key_49", "value_49")));
        Assert.isTrue(rateKeyPairs.contains(new KeyPair("key1", "value1")));

        Assert.isTrue(catalogKeyPairs.size() == rateKeyPairs.size());
        Assert.isTrue(catalogKeyPairs.size() == 2);

        Assert.isTrue(CollectionUtils.isEqualCollection(catalogKeyPairs, rateKeyPairs));

        rateService.removeKeyPairFromRateCatalog("key_49", catalog.getId());
        catalog = rateService.getRateCatalog(catalog.getId());

        catalogKeyPairs = catalog.getKeyPairs();

        Assert.notNull(catalogKeyPairs);
        Assert.notEmpty(catalogKeyPairs);
        Assert.isTrue(catalogKeyPairs.size() == 1);

        Assert.isTrue(!catalogKeyPairs.contains(new KeyPair("key_49", "value_49")));

        rate = rateService.getRate(rate.getId());

        rateKeyPairs = rate.getKeyPairs();

        Assert.notNull(rateKeyPairs);
        Assert.notEmpty(rateKeyPairs);
        Assert.isTrue(rateKeyPairs.size() == 1);

        Assert.isTrue(!catalogKeyPairs.contains(new KeyPair("key_49", "value_49")));

    }

    @Test
    public void addAmountsToRate() throws Exception {

        String rateCatalogCode = "RC_2013_AMOUNT";
        String rateCode = "R_2013_AMOUNT";

        RateCatalog rateCatalog = _createRateCatalog(rateCatalogCode);

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        RateAmount defaultRateAmount = rate.getDefaultRateAmount();

        Assert.notNull(defaultRateAmount);

        List<RateAmount> amounts = new LinkedList<RateAmount>();

        RateAmount amount1 = new RateAmount();
        amount1.setAmount(new BigDecimal(200.00));
        amount1.setUnit(125);
        amount1.setTransactionTypeId(rateCatalog.getTransactionTypeId());
        amounts.add(amount1);

        RateAmount amount2 = new RateAmount();
        amount2.setAmount(new BigDecimal(100.00));
        amount2.setUnit(275);
        amount2.setTransactionTypeId(rateCatalog.getTransactionTypeId());
        amounts.add(amount2);

        rate = rateService.addAmountsToRate(rate.getId(), amounts);

        Set<RateAmount> rateAmounts = rate.getRateAmounts();

        Assert.notNull(rateAmounts);
        Assert.notEmpty(rateAmounts);
        Assert.isTrue(rateAmounts.size() == 3);

        boolean defaultAmountExists = false;
        boolean amount1Exists = false;
        boolean amount2Exists = false;

        for (RateAmount rateAmount : rateAmounts) {

            Assert.notNull(rateAmount);
            Assert.notNull(rateAmount.getId());
            Assert.notNull(rateAmount.getRate());
            Assert.isTrue(rateAmount.getRate().getId().equals(rate.getId()));

            if (rateAmount.getId().equals(defaultRateAmount.getId())) {
                defaultAmountExists = true;
            } else if (rateAmount.getId().equals(amount1.getId())) {
                amount1Exists = true;
            } else if (rateAmount.getId().equals(amount2.getId())) {
                amount2Exists = true;
            }

        }

        Assert.isTrue(defaultAmountExists);
        Assert.isTrue(amount1Exists);
        Assert.isTrue(amount2Exists);

    }

    @Test
    public void deleteRateAmount() throws Exception {

        String rateCatalogCode = "RC_2013_AMOUNT";
        String rateCode = "R_2013_AMOUNT";

        RateCatalog rateCatalog = _createRateCatalog(rateCatalogCode);

        Rate rate = _createRate(rateCode, rateCatalogCode);

        Assert.notNull(rate);
        Assert.notNull(rate.getId());

        RateAmount defaultRateAmount = rate.getDefaultRateAmount();

        Assert.notNull(defaultRateAmount);

        List<RateAmount> amounts = new LinkedList<RateAmount>();

        RateAmount amount1 = new RateAmount();
        amount1.setAmount(new BigDecimal(200.00));
        amount1.setUnit(350);
        amount1.setTransactionTypeId(rateCatalog.getTransactionTypeId());
        amounts.add(amount1);

        RateAmount amount2 = new RateAmount();
        amount2.setAmount(new BigDecimal(100.00));
        amount2.setUnit(125);
        amount2.setTransactionTypeId(rateCatalog.getTransactionTypeId());
        amounts.add(amount2);

        rate = rateService.addAmountsToRate(rate.getId(), amounts);

        Set<RateAmount> rateAmounts = rate.getRateAmounts();

        Assert.notNull(rateAmounts);
        Assert.notEmpty(rateAmounts);
        Assert.isTrue(rateAmounts.size() == 3);

        for (RateAmount rateAmount : new HashSet<RateAmount>(rateAmounts)) {

            Assert.notNull(rateAmount);
            Assert.notNull(rateAmount.getId());
            Assert.notNull(rateAmount.getRate());
            Assert.isTrue(rateAmount.getRate().getId().equals(rate.getId()));

            if (rateAmount.getUnit() == 125) {
                rateService.deleteRateAmount(rateAmount.getId());
            }

        }

        rate = rateService.getRate(rate.getId());

        rateAmounts = rate.getRateAmounts();

        Assert.notNull(rateAmounts);
        Assert.notEmpty(rateAmounts);
        Assert.isTrue(rateAmounts.size() == 2);

        boolean defaultAmountExists = false;

        for (RateAmount rateAmount : rateAmounts) {

            Assert.notNull(rateAmount);
            Assert.notNull(rateAmount.getId());
            Assert.notNull(rateAmount.getRate());
            Assert.isTrue(rateAmount.getRate().getId().equals(rate.getId()));

            if (rateAmount.getId().equals(defaultRateAmount.getId())) {
                defaultAmountExists = true;
            }
        }

        Assert.isTrue(defaultAmountExists);

    }

    @Test
    public void getRateSubCodes() throws Exception {

        String rateCatalogCode = "RC_2013_C1_S";

        String rateCode = "R_2013_C1_S";

        String atpId = "19871";

        String subCode1 = "C2_S";
        String subCode2 = "C1_S";

        _createRateCatalog(rateCatalogCode, atpId);

        Rate rate1 = _createRate(rateCode, subCode1, rateCatalogCode, atpId);

        Assert.notNull(rate1);
        Assert.notNull(rate1.getCode());
        Assert.notNull(rate1.getSubCode());
        Assert.notNull(rate1.getAtpId());

        Rate rate2 = _createRate(rateCode, subCode2, rateCatalogCode, atpId);

        Assert.notNull(rate2);
        Assert.notNull(rate2.getCode());
        Assert.notNull(rate2.getSubCode());
        Assert.notNull(rate2.getAtpId());

        List<String> subCodes = rateService.getRateSubCodes(rateCode, atpId);

        Assert.notNull(subCodes);
        Assert.notEmpty(subCodes);
        Assert.isTrue(subCodes.size() == 2);
        Assert.notNull(subCodes.get(0));
        Assert.notNull(subCodes.get(1));
        Assert.isTrue(subCodes.get(0).equals(subCode2));
        Assert.isTrue(subCodes.get(1).equals(subCode1));

    }


}
