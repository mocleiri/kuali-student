package com.sigmasys.kuali.ksa.krad.util;

import com.sigmasys.kuali.ksa.model.Tag;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: tbornholtz
 * Date: 1/30/13
 * Time: 5:25 PM
 */
public class KsaHelper  extends ViewHelperServiceImpl {

    @Autowired
    private AuditableEntityService auditableEntityService;


    public List<Tag> getTagsForSuggest(String suggest){

        return getAuditableEntityService().getAuditableEntitiesByName(suggest, Tag.class);
    }


    private AuditableEntityService getAuditableEntityService() {
        if(auditableEntityService == null) {
            auditableEntityService = ContextUtils.getBean(AuditableEntityService.class);
        }
        return auditableEntityService;
    }

}
