package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.model.rule.Rule;
import com.sigmasys.kuali.ksa.model.rule.RuleSet;

/**
 * This utility can be used to build a String representation of a Drools-specific rule or rule set content.
 */
public class DroolsRuleBuilder {

    private DroolsRuleBuilder() {
    }

    public static String toString(Rule rule) {
        StringBuilder builder = new StringBuilder("rule ");
        // Adding the rule name
        builder.append("\"").append(rule.getName()).append("\"\n");
        // Adding the rule priority
        if (rule.getPriority() != null) {
            builder.append("salience ").append(rule.getPriority()).append("\n");
        }
        // Adding the rule header
        if (rule.getHeader() != null) {
            builder.append(rule.getHeader()).append("\n");
        }
        // Adding LHS
        builder.append("when\n    ").append(rule.getLhs()).append("\n");
        // Adding RHS
        builder.append("then\n    ").append(rule.getRhs()).append("\n");
        // Adding the rule "end"
        builder.append("end\n");
        return builder.toString();
    }

    public static String toString(RuleSet ruleSet) {
        // TODO:
        return "";
    }


}
