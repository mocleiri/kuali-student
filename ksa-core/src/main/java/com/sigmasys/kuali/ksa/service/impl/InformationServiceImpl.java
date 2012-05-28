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
    public Long persistInformation(Information information) {
        return persistEntity(information);
    }

    /**
     * Removes Information from the database.
     *
     * @param id Information ID
     * @return true if Information entity has been deleted
     */
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
        newMemo.setExpirationDate(expirationDate != null ? expirationDate : null);

        persistEntity(newMemo);

        if (prevMemo != null) {
            prevMemo.setNextMemo(newMemo);
        }

        return newMemo;
    }

}
