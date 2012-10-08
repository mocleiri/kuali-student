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

    private static final String EMPTY_VALUE_KEY = "_emptyValueKey";

    private String ruleSetId;
    private String ruleSetBody;

    private final RuleSetNameFinder ruleSetNameFinder = new RuleSetNameFinder();

    private final static class RuleSetNameFinder extends KeyValuesBase {

        private List<KeyValue> keyValues = Collections.emptyList();

        public void initValues(List<String> ruleSetNames) {
            keyValues = new ArrayList<KeyValue>(ruleSetNames.size()+1);
            keyValues.add(new ConcreteKeyValue(EMPTY_VALUE_KEY, ""));
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

    public void initRuleSetNameFinder(List<String> ruleSetNames) {
        ruleSetNameFinder.initValues(ruleSetNames);
    }

    public KeyValuesFinder getRuleSetNameFinder() {
        return ruleSetNameFinder;
    }
}
