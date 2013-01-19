package com.sigmasys.kuali.ksa.krad.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sigmasys.kuali.ksa.model.GlOperationType;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;

@SuppressWarnings("all")
public class CreditDebitKeyValuesFinder extends GenericKeyValuesFinder {

    private List<KeyValue> keys;

    public CreditDebitKeyValuesFinder() {
        keys = new ArrayList<KeyValue>();
        if(isBlankOption()){
            keys.add(new ConcreteKeyValue("", ""));
        }
        keys.add(new ConcreteKeyValue(GlOperationType.CREDIT_CODE, GlOperationType.CREDIT_CODE.toString()));
        keys.add(new ConcreteKeyValue(GlOperationType.DEBIT_CODE, GlOperationType.DEBIT_CODE.toString()));

    }

    @Override
    public List<KeyValue> getKeyValues() {
        // Get all entities of the generic type:
        return keys;
    }

}
