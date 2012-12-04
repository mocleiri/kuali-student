package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.model.rule.Rule;
import com.sigmasys.kuali.ksa.model.rule.RuleSet;

import java.util.List;

/**
 * BRM (Business Rules Management) persistence service.
 *
 * @author Michael Ivanov
 */
public interface BrmPersistenceService {

    /**
     * Persists a rule set (knowledge base) specified by the given RuleSet instance
     *
     * @param ruleSet RuleSet instance
     * @return RuleSet ID
     */
    String persistRuleSet(RuleSet ruleSet);

    /**
     * Persists a rule specified by the given Rule instance
     *
     * @param rule Rule instance
     * @return Rule ID
     */
    String persistRule(Rule rule);

    /**
     * Retrieves a rule set (knowledge base) from the persistence store by ID
     *
     * @param id RuleSet ID
     * @return RuleSet instance
     */
    RuleSet getRuleSet(Long id);

    /**
     * Retrieves a rule set (knowledge base) from the persistence store by name
     *
     * @param name RuleSet name
     * @return RuleSet instance
     */
    RuleSet getRuleSet(String name);

    /**
     * Retrieves all existing rule sets (knowledge bases) from the persistence store.
     *
     * @return RuleSet instance
     */
    List<RuleSet> getRuleSets();

    /**
     * Retrieves a rule from the persistence store by the given ID
     *
     * @param id Rule identifier
     * @return Rule instance
     */
    Rule getRule(Long id);

    /**
     * Adds rules to a rule set, throws IllegalsRulesException if any rule is invalid.
     *
     * @param ruleSetId Rule Set ID
     * @param rules     array of Rule instances
     * @return the modified rule set
     */
    RuleSet addRulesToRuleSet(Long ruleSetId, Rule... rules);

    /**
     * Removes rules from a rule set
     *
     * @param ruleSetId Rule Set ID
     * @param ruleIds   array of Rule IDs to be removed
     * @return the modified rule set
     */
    RuleSet deleteRulesFromRuleSet(Long ruleSetId, Long... ruleIds);

}
