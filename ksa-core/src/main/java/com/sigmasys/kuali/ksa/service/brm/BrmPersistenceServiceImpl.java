package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.exception.InvalidRulesException;
import com.sigmasys.kuali.ksa.model.rule.Rule;
import com.sigmasys.kuali.ksa.model.rule.RuleSet;
import com.sigmasys.kuali.ksa.service.impl.GenericPersistenceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;

/**
 * BRM (Business Rules Management)  persistence service implementation.
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
@Service("brmPersistenceService")
@Transactional(readOnly = true)
public class BrmPersistenceServiceImpl extends GenericPersistenceService implements BrmPersistenceService {

    private static final Log logger = LogFactory.getLog(BrmPersistenceServiceImpl.class);

    /**
     * Persists Drools rule set specified by the given RuleSet instance
     *
     * @param ruleSet RuleSet instance
     * @return RuleSet ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistRuleSet(RuleSet ruleSet) {
        return persistEntity(ruleSet);
    }

    /**
     * Persists a rule specified by the given Rule instance
     *
     * @param rule Rule instance
     * @return Rule ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistRule(Rule rule) {
        return persistEntity(rule);
    }

    /**
     * Retrieves a rule set from the persistence store by ID
     *
     * @param id RuleSet identifier
     * @return RuleSet instance
     */
    @Override
    public RuleSet getRuleSet(Long id) {
        return getEntity(id, RuleSet.class);
    }

    /**
     * Retrieves a rule set from the persistence store by name
     *
     * @param name RuleSet name
     * @return RuleSet instance
     */
    @Override
    public RuleSet getRuleSet(String name) {
        Query query = em.createQuery("select rs from RuleSet rs " +
                "left outer join fetch rs.type t " +
                "left outer join fetch rs.rules r " +
                "where rs.name = :name");
        query.setParameter("name", name);
        List<RuleSet> ruleSets = query.getResultList();
        return (ruleSets != null && !ruleSets.isEmpty()) ? ruleSets.get(0) : null;
    }

    /**
     * Retrieves a rule from the persistence store by the given ID
     *
     * @param id Rule identifier
     * @return Rule instance
     */
    @Override
    public Rule getRule(Long id) {
        return getEntity(id, Rule.class);
    }

    /**
     * Retrieves all existing rule sets from the persistence store.
     *
     * @return RuleSet instance
     */
    @Override
    public List<RuleSet> getRuleSets() {
        return getEntities(RuleSet.class);
    }

    /**
     * Adds rules to a rule set, throws InvalidRulesException if any rule types don't match.
     *
     * @param ruleSetId Rule Set ID
     * @param rules     array of Rule instances
     * @return the modified rule set
     */
    @Override
    @Transactional(readOnly = false)
    public RuleSet addRulesToRuleSet(Long ruleSetId, Rule... rules) {

        RuleSet ruleSet = getRuleSet(ruleSetId);
        if (ruleSet == null) {
            String errMsg = "Rule Set with ID = " + ruleSetId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Long ruleTypeId = ruleSet.getType().getId();

        if (ruleSet.getRules() == null) {
            ruleSet.setRules(new HashSet<Rule>());
        }

        for (Rule rule : rules) {
            if (!ruleTypeId.equals(rule.getType().getId())) {
                String errMsg = "Rule Set's type '" + ruleSet.getType().getName() +
                        "' does not match Rule's type '" + rule.getType().getName() + "'";
                logger.error(errMsg);
                throw new InvalidRulesException(errMsg);
            }
            persistRule(rule);
            ruleSet.getRules().add(rule);
        }

        return ruleSet;
    }

    /**
     * Removes rules from a rule set
     *
     * @param ruleSetId Rule Set ID
     * @param ruleIds   array of Rule IDs to be removed
     * @return the modified rule set
     */
    @Override
    @Transactional(readOnly = false)
    public RuleSet deleteRulesFromRuleSet(Long ruleSetId, Long... ruleIds) {

        RuleSet ruleSet = getRuleSet(ruleSetId);
        if (ruleSet == null) {
            String errMsg = "Rule Set with ID = " + ruleSetId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        List<Long> idsToDelete = Arrays.asList(ruleIds);

        Set<Rule> rules = ruleSet.getRules();

        if (rules != null) {

            Set<Rule> rulesToDelete = new HashSet<Rule>();

            for (Rule rule : rules) {
                if (idsToDelete.contains(rule.getId())) {
                    rulesToDelete.add(rule);
                }
            }

            rules.removeAll(rulesToDelete);
            ruleSet.setRules(rules);

            persistRuleSet(ruleSet);
        }

        return ruleSet;
    }

}
