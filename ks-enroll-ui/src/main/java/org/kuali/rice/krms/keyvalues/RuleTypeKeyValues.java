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
package org.kuali.rice.krms.keyvalues;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.student.enrollment.class2.courseoffering.service.decorators.PermissionServiceConstants;
import org.kuali.student.r2.common.util.constants.KSKRMSServiceConstants;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Student Team
 */
public class RuleTypeKeyValues implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient RuleManagementService ruleManagementService;

    public Map<String,Map<String, String>> getKeyValues(List<KeyValue> agendaTypeIds) {

        Map<String, Map<String, String>> keyValueList = new HashMap<String, Map<String, String>>();
        Map<String, String> keyValuesRule = new HashMap<String, String>();
        Map<String, String> keyValuesInstruction = new HashMap<String, String>();
        NaturalLanguageUsage usageType = this.getRuleManagementService().getNaturalLanguageUsageByNameAndNamespace(KSKRMSServiceConstants.KRMS_NL_TYPE_DESCRIPTION, PermissionServiceConstants.KS_SYS_NAMESPACE);
        NaturalLanguageUsage usageInstruction = this.getRuleManagementService().getNaturalLanguageUsageByNameAndNamespace(KSKRMSServiceConstants.KRMS_NL_TYPE_INSTRUCTION, PermissionServiceConstants.KS_SYS_NAMESPACE);
         //Use Type Type Relation to get Rule Types
        try {
            for(KeyValue agendaTypeId : agendaTypeIds) {
                List<TypeTypeRelation> typeTypeRelationList = this.getKrmsTypeRepositoryService().findTypeTypeRelationsByFromType(agendaTypeId.getKey());
                for (TypeTypeRelation typeTypeRelation : typeTypeRelationList) {
                    NaturalLanguageTemplate templateRule = null;
                    NaturalLanguageTemplate templateInstruction = null;
                    try{
                        templateRule = this.getRuleManagementService().findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId("en", typeTypeRelation.getToTypeId(), usageType.getId());
                        templateInstruction = this.getRuleManagementService().findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId("en", typeTypeRelation.getToTypeId(), usageInstruction.getId());
                    }catch (IndexOutOfBoundsException e){
                        //Ignore, rice error in NaturalLanguageTemplateBoServiceImpl line l
                    }

                    if (templateRule != null){
                        //Use the template in the dropdown
                        keyValuesRule.put(typeTypeRelation.getToTypeId(), templateRule.getTemplate());
                    } else {
                        //If no template exist, display the type name
                        keyValuesRule.put(typeTypeRelation.getToTypeId(), getKrmsTypeRepositoryService().getTypeById(typeTypeRelation.getToTypeId()).getName());
                    }

                    if (templateInstruction != null && templateRule != null) {
                        keyValuesInstruction.put(templateRule.getTemplate(), templateInstruction.getTemplate());
                    } else {
                        keyValuesInstruction.put(typeTypeRelation.getToTypeId(), getKrmsTypeRepositoryService().getTypeById(typeTypeRelation.getToTypeId()).getName());
                    }
                }
            }
        } catch (Exception ex) {}
        keyValueList.put("ruleTypes", keyValuesRule);
        keyValueList.put("ruleInstructions", keyValuesInstruction);
        return keyValueList;
    }

    private KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
    }

    public RuleManagementService getRuleManagementService() {
        if (ruleManagementService == null) {
            ruleManagementService = (RuleManagementService) GlobalResourceLoader.getService(new QName(KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0, "ruleManagementService"));
        }
        return ruleManagementService;
    }
}
