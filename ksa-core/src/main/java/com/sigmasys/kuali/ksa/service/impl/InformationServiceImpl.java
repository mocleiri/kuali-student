package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.InformationService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserSessionManager;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Information service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("informationService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class InformationServiceImpl extends GenericPersistenceService implements InformationService {


    private static final Log logger = LogFactory.getLog(InformationServiceImpl.class);


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserSessionManager userSessionManager;


    /**
     * Returns Information by ID
     *
     * @param id Information ID
     * @return Information instance
     */
    @Override
    public Information getInformation(Long id) {
        return getEntity(id, Information.class);
    }

    /**
     * Returns Memo by ID
     *
     * @param id Memo ID
     * @return Memo instance
     */
    @Override
    public Memo getMemo(Long id) {
        return getEntity(id, Memo.class);
    }


    /**
     * Returns all Information instances sorted by ID in the descendant order
     *
     * @return List of Information instances
     */
    @Override
    public List<Information> getInformations() {
        return getEntities(Information.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all Memo entities sorted by ID in the descendant order
     *
     * @return List of memos
     */
    @Override
    public List<Memo> getMemos() {
        return getEntities(Memo.class, new Pair<String, SortOrder>("id", SortOrder.DESC));
    }

    /**
     * Returns all Alert entities by Account ID
     *
     * @param userId Account ID
     * @return List of memos
     */
    @Override
    public List<Alert> getAlerts(String userId) {
        Query query = em.createQuery("select a from Alert a where a.account.id = :userId order by a.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Returns all Flag entities by Account ID
     *
     * @param userId Account ID
     * @return List of flags
     */
    @Override
    public List<Flag> getFlags(String userId) {
        Query query = em.createQuery("select f from Flag f where f.account.id = :userId order by f.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Returns all Memo entities by Account ID
     *
     * @param userId Account ID
     * @return List of memos
     */
    @Override
    public List<Memo> getMemos(String userId) {
        Query query = em.createQuery("select m from Memo m where m.account.id = :userId order by m.id desc");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Persists Information in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param information Information instance
     * @return Information ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistInformation(Information information) {
        return persistEntity(information);
    }

    /**
     * Removes Information from the database.
     *
     * @param id Information ID
     * @return true if Information entity has been deleted
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteInformation(Long id) {
        return deleteEntity(id, Information.class);
    }

    /**
     * Creates a new memo based on the given parameters
     *
     * @param transactionId  Transaction ID
     * @param memoText       Memo text
     * @param accessLevel    Access level
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @param prevMemoId     Previous Memo ID
     * @return new Memo instance
     */
    @Override
    @Transactional(readOnly = false)
    public Memo createMemo(Long transactionId, String memoText, Integer accessLevel,
                           Date effectiveDate, Date expirationDate, Long prevMemoId) {

        Transaction transaction = transactionService.getTransaction(transactionId);
        if (transaction == null) {
            String errMsg = "Transaction with ID = " + transactionId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Memo memo = createMemo(transaction.getAccount().getId(), memoText, accessLevel,
                effectiveDate, expirationDate, prevMemoId);

        memo.setTransaction(transaction);

        return memo;

    }

    /**
     * Creates a new memo based on the given parameters
     *
     * @param accountId      Account ID
     * @param memoText       Memo text
     * @param accessLevel    Access level
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @param prevMemoId     Previous Memo ID
     * @return new Memo instance
     */
    @Override
    @Transactional(readOnly = false)
    public Memo createMemo(String accountId, String memoText, Integer accessLevel,
                           Date effectiveDate, Date expirationDate, Long prevMemoId) {

        if (accountId == null) {
            String errMsg = "Account ID cannot be null";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Account account = accountService.getFullAccount(accountId);
        if (account == null) {
            String errMsg = "Account with ID = " + accountId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Memo newMemo = new Memo();
        newMemo.setText(memoText);
        newMemo.setAccount(account);
        newMemo.setAccessLevel(accessLevel);

        Memo prevMemo = null;
        if (prevMemoId != null) {
            prevMemo = getMemo(prevMemoId);
            if (prevMemo == null) {
                String errMsg = "Memo with ID = " + prevMemoId + " does not exist";
                logger.error(errMsg);
                throw new IllegalArgumentException(errMsg);
            }
            newMemo.setPreviousMemo(prevMemo);
            prevMemo.setNextMemo(newMemo);
        }

        String creatorId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
        newMemo.setCreatorId(creatorId);
        newMemo.setResponsibleEntity(creatorId);

        Date curDate = new Date();
        newMemo.setCreationDate(curDate);
        newMemo.setEffectiveDate(effectiveDate != null ? effectiveDate : curDate);
        newMemo.setExpirationDate(expirationDate);

        persistEntity(newMemo);

        if (prevMemo != null) {
            prevMemo.setNextMemo(newMemo);
        }

        return newMemo;
    }

    /**
     * Creates a new flag based on the given parameters
     *
     * @param transactionId  Transaction ID
     * @param flagTypeId     Flag Type ID
     * @param accessLevel    Access level
     * @param severity       Severity
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @return new Flag instance
     */
    @Override
    @Transactional(readOnly = false)
    public Flag createFlag(Long transactionId, Long flagTypeId, Integer accessLevel,
                           Integer severity, Date effectiveDate, Date expirationDate) {

        Transaction transaction = transactionService.getTransaction(transactionId);
        if (transaction == null) {
            String errMsg = "Transaction with ID = " + transactionId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Flag flag = createFlag(transaction.getAccount().getId(), flagTypeId, accessLevel, severity,
                effectiveDate, expirationDate);

        flag.setTransaction(transaction);

        return flag;

    }

    /**
     * Creates a new flag based on the given parameters
     *
     * @param accountId      Account ID
     * @param flagTypeId     Flag Type ID
     * @param accessLevel    Access level
     * @param severity       Severity
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @return new Flag instance
     */
    @Override
    @Transactional(readOnly = false)
    public Flag createFlag(String accountId, Long flagTypeId, Integer accessLevel,
                           Integer severity, Date effectiveDate, Date expirationDate) {

        if (accountId == null) {
            String errMsg = "Account ID cannot be null";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Account account = accountService.getFullAccount(accountId);
        if (account == null) {
            String errMsg = "Account with ID = " + accountId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        FlagType flagType = getEntity(flagTypeId, FlagType.class);
        if (flagType == null) {
            String errMsg = "FlagType with ID = " + flagTypeId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }


        Flag flag = new Flag();
        flag.setType(flagType);
        flag.setAccount(account);
        flag.setAccessLevel(accessLevel);
        flag.setSeverity(severity);

        String creatorId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
        flag.setCreatorId(creatorId);
        flag.setResponsibleEntity(creatorId);

        Date curDate = new Date();
        flag.setCreationDate(curDate);
        flag.setEffectiveDate(effectiveDate != null ? effectiveDate : curDate);
        flag.setExpirationDate(expirationDate);

        persistEntity(flag);

        return flag;
    }


    /**
     * Creates a new alert based on the given parameters
     *
     * @param transactionId  Transaction ID
     * @param alertText      Alert text
     * @param accessLevel    Access level
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @return new Alert instance
     */
    @Override
    @Transactional(readOnly = false)
    public Alert createAlert(Long transactionId, String alertText, Integer accessLevel, Date effectiveDate,
                             Date expirationDate) {

        Transaction transaction = transactionService.getTransaction(transactionId);
        if (transaction == null) {
            String errMsg = "Transaction with ID = " + transactionId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Alert alert = createAlert(transaction.getAccount().getId(), alertText, accessLevel,
                effectiveDate, expirationDate);

        alert.setTransaction(transaction);

        return alert;

    }

    /**
     * Creates a new alert based on the given parameters
     *
     * @param accountId      Account ID
     * @param alertText      Alert text
     * @param accessLevel    Access level
     * @param effectiveDate  Effective date
     * @param expirationDate Expiration date
     * @return new Alert instance
     */
    @Override
    @Transactional(readOnly = false)
    public Alert createAlert(String accountId, String alertText, Integer accessLevel, Date effectiveDate,
                             Date expirationDate) {

        if (accountId == null) {
            String errMsg = "Account ID cannot be null";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Account account = accountService.getFullAccount(accountId);
        if (account == null) {
            String errMsg = "Account with ID = " + accountId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Alert alert = new Alert();
        alert.setText(alertText);
        alert.setAccount(account);
        alert.setAccessLevel(accessLevel);

        String creatorId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
        alert.setCreatorId(creatorId);
        alert.setResponsibleEntity(creatorId);

        Date curDate = new Date();
        alert.setCreationDate(curDate);
        alert.setEffectiveDate(effectiveDate != null ? effectiveDate : curDate);
        alert.setExpirationDate(expirationDate);

        persistEntity(alert);

        return alert;
    }
}
