package com.sigmasys.kuali.ksa.service.drools;

import com.sigmasys.kuali.ksa.model.RuleSet;
import com.sigmasys.kuali.ksa.service.impl.GenericPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Drools persistence service.
 *
 * @author Michael Ivanov
 */
@Service("droolsPersistenceService")
@Transactional(readOnly = true)
public class DroolsPersistenceService extends GenericPersistenceService {

    /**
     * Persists Drools rule set specified by the given RuleSet instance
     *
     * @param rules RuleSet instance
     * @return RuleSet ID
     */
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
    public RuleSet getRules(String id) {
       return getEntity(id, RuleSet.class);
    }

    /**
     * Retrieves all existing rule sets from the persistence store.
     *
     * @return RuleSet instance
     */
    public List<RuleSet> getRules() {
        return getEntities(RuleSet.class);
    }

}
