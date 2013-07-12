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

import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.constants.PopulationServiceConstants;
import org.kuali.student.r2.core.krms.naturallanguage.TermParameterTypes;
import org.kuali.student.r2.core.population.dto.PopulationInfo;
import org.kuali.student.r2.core.population.service.PopulationService;

import javax.xml.namespace.QName;
import java.util.Map;


/**
 * This class creates the template context for Class Standing.
 */
public class PopulationContextImpl extends BasicContextImpl {

    private PopulationService populationService;

	public final static String CLASS_STANDING_TOKEN = "classStanding";


    public void setPopulationService(PopulationService populationService) {
        this.populationService = populationService;
    }
    private PopulationService getPopulationService() {
        if(populationService == null) {
            populationService = (PopulationService) GlobalResourceLoader.getService(new QName(PopulationServiceConstants.NAMESPACE, "PopulationService"));
        }
        return this.populationService;
    }
	private PopulationInfo getClassStanding(String classStandingId, ContextInfo context)  {
		if (classStandingId == null) {
			return null;
		}
		try {

			return  this.getPopulationService().getPopulation(classStandingId, context);
		} catch (Exception e) {
                    throw new RiceIllegalStateException (e);
		}
	}
    /**
     * Creates the context map (template data) for the requirement component.
     *
     * @param parameters
     * @throws org.kuali.student.r2.common.exceptions.OperationFailedException Creating context map fails
     */
    @Override
    public Map<String, Object> createContextMap(Map<String, Object> parameters) {
        ContextInfo contextInfo = ContextUtils.getContextInfo();
        Map<String, Object> contextMap = super.createContextMap(parameters);

        String classStandingId = (String) parameters.get(TermParameterTypes.CLASS_STANDING_KEY.getId());

        if (classStandingId != null) {
            PopulationInfo populationInfo = this.getClassStanding(classStandingId, contextInfo);
            contextMap.put(CLASS_STANDING_TOKEN, populationInfo);
        }

        return contextMap;
    }
}
