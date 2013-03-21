/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.krms.naturallanguage.config.context;

import org.kuali.rice.krms.api.repository.term.TermDefinitionContract;
import org.kuali.student.r2.core.krms.naturallanguage.TermParameterTypes;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.OperationFailedException;

import java.util.Map;


/**
 * This class creates the template context for a person.
 */
public class PersonContextImpl extends BasicContextImpl {
 
	public final static String PERSON_TOKEN = "personId";

    /**
     * Creates the context map (template data) for the requirement component.
     * 
     *
     *
     * @param term Requirement component
     * @param contextInfo
     * @throws org.kuali.student.r2.common.exceptions.OperationFailedException Creating context map fails
     */
    public Map<String, Object> createContextMap(TermDefinitionContract term, ContextInfo contextInfo) throws OperationFailedException {
        Map<String, Object> contextMap = super.createContextMap(term, contextInfo);

        String person = getTermParameterValue(term, TermParameterTypes.PERSON_KEY.getId());
        if( person != null){
            contextMap.put(PERSON_TOKEN, person);
        }

        return contextMap;
    }
}
