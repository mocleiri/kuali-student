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

    @Override
    protected List<KeyValue> buildKeyValues() {
    	List<KeyValue> keys = new ArrayList<KeyValue>();
    	
        keys.add(new ConcreteKeyValue(GlOperationType.CREDIT_CODE, GlOperationType.CREDIT.toString()));
        keys.add(new ConcreteKeyValue(GlOperationType.DEBIT_CODE, GlOperationType.DEBIT.toString()));

        return keys;
    }

}
