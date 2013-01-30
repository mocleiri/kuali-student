package com.sigmasys.kuali.ksa.krad.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;

import com.sigmasys.kuali.ksa.model.AuditableEntity;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;

@SuppressWarnings("all")
public class AuditableEntityKeyValuesFinder<T extends AuditableEntity> extends GenericKeyValuesFinder {
	
	private AuditableEntityService auditableEntityService;
	
	private Class<T> type;
	
	
	public AuditableEntityKeyValuesFinder(AuditableEntityService service, Class<T> type) {
		this(service, type, false);
	}

	public AuditableEntityKeyValuesFinder(AuditableEntityService service, Class<T> type, boolean blankOption) {
		this.auditableEntityService = service;
		this.type = type;
		setBlankOption(blankOption);
	}

	@Override
	protected List<KeyValue> buildKeyValues() {
		// Get all entities of the generic type:
		List<T> entities = auditableEntityService.getAuditableEntities(type);
		List<KeyValue> result = new ArrayList<KeyValue>();
		
		// Iterate through entities and add Key Value pairs:
		for (T entity : entities) {
			String key = entity.getId().toString();
			String value = StringUtils.isNotBlank(entity.getName()) ? entity.getName() : "UNDEFINED";
			ConcreteKeyValue keyValue = new ConcreteKeyValue(key, value);
			
			result.add(keyValue);
		}
		
		return result;
	}

}
