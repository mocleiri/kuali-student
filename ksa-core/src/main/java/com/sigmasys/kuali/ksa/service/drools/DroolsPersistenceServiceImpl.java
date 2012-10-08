package com.sigmasys.kuali.ksa.service.drools;

import com.sigmasys.kuali.ksa.model.RuleSet;
import com.sigmasys.kuali.ksa.service.impl.GenericPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * Drools persistence service.
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
@Service("droolsPersistenceService")
@Transactional(readOnly = true)
public class DroolsPersistenceServiceImpl extends GenericPersistenceService implements DroolsPersistenceService {

    /**
     * Persists Drools rule set specified by the given RuleSet instance
     *
     * @param rules RuleSet instance
     * @return RuleSet ID
     */
    @Override
    @Transactional(readOnly = false)
    public String persistRules(RuleSet rules) {
        return persistEntity(rules);
    }

    /**
     * Retrieves a rule set from the persistence store by the given ID
     *
     * @param id RuleSet identifier
     * @return RuleSet instance
     */
    @Override
    public RuleSet getRules(String id) {
        return getEntity(id, RuleSet.class);
    }

    /**
     * Retrieves all existing rule sets from the persistence store.
     *
     * @return RuleSet instance
     */
    @Override
    public List<RuleSet> getRules() {
        return getEntities(RuleSet.class);
    }

    /**
     * Retrieves all rule set IDs sorted in alphabetical order.
     *
     * @return a list of IDs
     */
    @Override
    public List<String> getRuleIds() {
        Query query = em.createQuery("select r.id from RuleSet r order by r.id asc");
        return query.getResultList();
    }

}
