package com.sigmasys.kuali.ksa.service.drools;

import com.sigmasys.kuali.ksa.model.RuleSet;
import java.util.List;

/**
 * Drools persistence service.
 *
 * @author Michael Ivanov
 */
public interface DroolsPersistenceService {

    /**
     * Persists Drools rule set specified by the given RuleSet instance
     *
     * @param rules RuleSet instance
     * @return RuleSet ID
     */
    public String persistRules(RuleSet rules);

    /**
     * Retrieves a rule set from the persistence store by the given ID
     *
     * @param id RuleSet identifier
     * @return RuleSet instance
     */
    public RuleSet getRules(String id);
    /**
     * Retrieves all existing rule sets from the persistence store.
     *
     * @return RuleSet instance
     */
    public List<RuleSet> getRules();

    /**
     * Retrieves all rule set IDs sorted in alphabetical order.
     *
     * @return a list of IDs
     */
    public List<String> getRuleIds();
}
