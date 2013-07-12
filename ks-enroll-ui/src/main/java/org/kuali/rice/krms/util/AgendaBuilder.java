/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krms.util;

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.TreeGroup;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.MessageField;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.dto.AgendaEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.service.RuleViewHelperService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Student Team
 */
public class AgendaBuilder {

    public static final String AGENDA = "_agenda";
    public static final String RULE = "_rule";

    private RuleViewHelperService viewHelperService;

    /**
     * This method dynamically build the components on the screen to render an angenda.
     *
     * @param agenda
     * @return
     */
    public Component buildAgenda(AgendaEditor agenda, int index, AgendaSection agendaSection) {
        String agendaSuffix = AGENDA + Integer.toString(index);
        Group group = ComponentUtils.copy(agendaSection.getAgendaPrototype(), agendaSuffix);
        group.setHeaderText(agenda.getAgendaTypeInfo().getDescription());

        String bindingPrefix = "agendas[" + index + "].";
        List<Component> components = new ArrayList<Component>();
        for (RuleEditor rule : agenda.getRuleEditors().values()) {
            components.add(buildRule(rule, bindingPrefix, agendaSection));
        }

        group.setItems(components);

        return group;
    }

    /**
     * This method dynamically builds a disclosure section for each rule that already exists.
     *
     * @param rule
     * @return
     */
    protected Component buildRule(RuleEditor rule, String bindingPrefix, AgendaSection agendaSection) {
        String ruleSuffix = RULE + rule.getKey();
        Group group = ComponentUtils.copy(agendaSection.getRulePrototype(), ruleSuffix);
        group.setHeaderText(rule.getRuleTypeInfo().getDescription());

        //Set the rule key on the action links.
        List<Action> actionLinks = ComponentUtils.getComponentsOfTypeDeep(group, Action.class);
        for (Action actionLink : actionLinks) {
            actionLink.getActionParameters().put("ruleKey", rule.getKey());
        }

        ComponentUtils.updateContextForLine(group, rule, 0, ruleSuffix);

        String bindingPath = bindingPrefix + "ruleEditors[" + rule.getKey() + "].";
        this.setPropertyBindingPaths(group, bindingPath);

        return group;
    }

    protected void setPropertyBindingPaths(Group group, String bindingPath) {

        List<DataField> dataFields = ComponentUtils.getComponentsOfTypeDeep(group.getItems(), DataField.class);
        for (DataField collectionField : dataFields) {
            collectionField.getBindingInfo().setBindingName(bindingPath + collectionField.getBindingInfo().getBindingName());
        }

        List<TreeGroup> treeFields = ComponentUtils.getComponentsOfTypeDeep(group.getItems(), TreeGroup.class);
        for (TreeGroup collectionField : treeFields) {
            collectionField.getBindingInfo().setBindingName(bindingPath + collectionField.getBindingInfo().getBindingName());
        }
    }

    public RuleViewHelperService getViewHelperService() {
        return viewHelperService;
    }

    public void setViewHelperService(RuleViewHelperService viewHelperService) {
        this.viewHelperService = viewHelperService;
    }
}
