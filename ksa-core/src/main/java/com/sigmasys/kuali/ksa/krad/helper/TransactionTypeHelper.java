package com.sigmasys.kuali.ksa.krad.helper;

import com.sigmasys.kuali.ksa.krad.form.TransactionTypeForm;
import com.sigmasys.kuali.ksa.model.Tag;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.service.ExpressionEvaluatorService;
import org.kuali.rice.krad.uif.service.ViewDictionaryService;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: tbornholtz
 * Date: 1/30/13
 * Time: 5:25 PM
 */
public class TransactionTypeHelper extends ViewHelperServiceImpl {

    protected final Log logger = LogFactory.getLog(getClass());

    private AuditableEntityService auditableEntityService;

    private TransactionTypeForm form;
    private List<Tag> searchTags;

    public List<Tag> getTagsForSuggest(String suggest) {
        searchTags = getAuditableEntityService().getAuditableEntitiesByName(suggest, Tag.class);
        return searchTags;
    }

    private AuditableEntityService getAuditableEntityService() {
        if (auditableEntityService == null) {
            auditableEntityService = ContextUtils.getBean(AuditableEntityService.class);
        }
        return auditableEntityService;
    }

    /**
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#processCollectionAddLine(org.kuali.rice.krad.uif.view.View,
     *      Object, String)
     */
    @Override
    public void processCollectionAddLine(View view, Object model, String collectionPath) {
        if(model instanceof TransactionTypeForm){
            this.form = (TransactionTypeForm)model;
        }
        super.processCollectionAddLine(view, model, collectionPath);
    }

    /**
     * Add addLine to collection while giving derived classes an opportunity to override
     * for things like sorting.
     *
     * @param collection  - the Collection to add the given addLine to
     * @param addLine     - the line to add to the given collection
     * @param insertFirst - indicates if the item should be inserted as the first item
     */
    @Override
    protected void addLine(Collection<Object> collection, Object addLine, boolean insertFirst) {
        if(addLine instanceof Tag){
            Tag added = (Tag)addLine;
            if(this.searchTags != null){
                String name = added.getName();
                for(Tag tag : searchTags){
                    if(name.equals(tag.getName())){
                        logger.info("Names " + name + " match");
                        addLine = tag;
                        break;
                    }
                }
            }
        }
        super.addLine(collection, addLine, insertFirst);

    }
}
