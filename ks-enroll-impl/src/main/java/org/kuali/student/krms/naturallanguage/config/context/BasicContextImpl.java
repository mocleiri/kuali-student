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

import org.kuali.student.krms.naturallanguage.config.context.util.NLHelper;
import org.kuali.student.r2.common.exceptions.OperationFailedException;

import java.util.Map;
import org.kuali.rice.krms.impl.repository.language.AbstractContext;

public class BasicContextImpl extends AbstractContext {
	/**
	 * <code>NLHelper</code> token (key) references a static natural language
	 * helper class for use in templates.
	 * e.g. '$NLHelper.getProperGrammer($intValue, "course", "courses")'
	 */
	public final static String NL_HELPER_TOKEN = "NLHelper";

    public final static String FREE_TEXT_TOKEN = "freeText";

	/**
	 * Constructor.
	 */
	public BasicContextImpl() {
	}
	
    /**
     * Creates the context map (template data) for the requirement component.
     * Also, adds the field token map to the context map.
     *
     * @param parameters
     * @param contextInfo
     * @throws org.kuali.student.r2.common.exceptions.OperationFailedException Creating context map fails
     */
    @Override
    public Map<String, Object> createContextMap(Map<String, Object> parameters) {
    	Map<String, Object> contextMap = super.createContextMap(parameters);
        contextMap.put(NL_HELPER_TOKEN, NLHelper.class);

        String key = "kuali.term.parameter.type.free.text";
        if(parameters.containsKey(key)) {
            contextMap.put(FREE_TEXT_TOKEN, parameters.get(key));
        }

        return contextMap;
    }
}
