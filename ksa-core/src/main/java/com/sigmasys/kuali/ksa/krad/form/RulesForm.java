package com.sigmasys.kuali.ksa.krad.form;


import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This form contains the rules related entities and properties.
 *
 * @author Michael Ivanov
 */
public class RulesForm extends AbstractViewModel {

    private String ruleSetId;
    private String ruleSetBody;
    private String newRuleSetId;
    private String newRuleSetBody;
    private String editStatusMessage;
    private String addStatusMessage;

    private final RuleSetNameFinder ruleSetNameFinder = new RuleSetNameFinder();

    private final static class RuleSetNameFinder extends KeyValuesBase {

        private List<KeyValue> keyValues = Collections.emptyList();

        public void initValues(List<String> ruleSetNames) {
            keyValues = new ArrayList<KeyValue>(ruleSetNames.size());
            for (String ruleSetName : ruleSetNames) {
                keyValues.add(new ConcreteKeyValue(ruleSetName, ruleSetName));
            }
        }

        /**
         * This is an implementation of a key value finder, normally this would make a request to
         * a database to obtain the necessary values.
         *
         * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
         */
        @Override
        public List<KeyValue> getKeyValues() {
            return Collections.unmodifiableList(keyValues);
        }
    }

    public String getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(String ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public String getRuleSetBody() {
        return ruleSetBody;
    }

    public void setRuleSetBody(String ruleSetBody) {
        this.ruleSetBody = ruleSetBody;
    }

    public String getNewRuleSetId() {
        return newRuleSetId;
    }

    public void setNewRuleSetId(String newRuleSetId) {
        this.newRuleSetId = newRuleSetId;
    }

    public String getNewRuleSetBody() {
        return newRuleSetBody;
    }

    public void setNewRuleSetBody(String newRuleSetBody) {
        this.newRuleSetBody = newRuleSetBody;
    }

    public void initRuleSetNameFinder(List<String> ruleSetNames) {
        ruleSetNameFinder.initValues(ruleSetNames);
    }

    public String getEditStatusMessage() {
        return editStatusMessage;
    }

    public void setEditStatusMessage(String editStatusMessage) {
        this.editStatusMessage = editStatusMessage;
    }

    public KeyValuesFinder getRuleSetNameFinder() {
        return ruleSetNameFinder;
    }

    public String getAddStatusMessage() {
        return addStatusMessage;
    }

    public void setAddStatusMessage(String addStatusMessage) {
        this.addStatusMessage = addStatusMessage;
    }
}
