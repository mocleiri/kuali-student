/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.ui.admin.lrc;


import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.lookup.LookupForm;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.student.common.util.ContextBuilder;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValueInfo;
import org.kuali.student.r2.lum.lrc.service.LRCService;
import org.kuali.student.r2.lum.util.constants.LrcServiceConstants;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ResultValueInfoAdminLookupableImpl extends LookupableImpl
{
	private transient LRCService lRCService;
    private static final long serialVersionUID = 1L;
	@Override
	public List<ResultValueInfo> performSearch(LookupForm lookupForm, Map<String, String> searchCriteria, boolean bounded)
	{
		QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
		List<Predicate> pList = new ArrayList<Predicate>();
        //Code Changed for JIRA-8997 - SONAR Critical issues - Performance - Inefficient use of keySet iterator instead of entrySet iterator
		for(Map.Entry<String, String> entry: searchCriteria.entrySet()) {
            String fieldName = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty())
            {
                if (fieldName.equals("maxResultsToReturn"))
                {
                    qBuilder.setMaxResults (Integer.parseInt(value));
                    continue;
                }
                pList.add(PredicateFactory.equal(fieldName, value));
            }
        }
		if (!pList.isEmpty())
		{
			qBuilder.setPredicates(PredicateFactory.and(pList.toArray(new Predicate[pList.size()])));
		}
		try
		{
            return this.getLRCService().searchForResultValues(qBuilder.build(), getContextInfo());
		}
		catch (Exception ex) {
		    throw new RuntimeException(ex);
		}
	}

	public void setLRCService(LRCService lRCService)
	{
		    this.lRCService = lRCService;
	}

	public LRCService getLRCService()
	{
		if (lRCService == null)
		{
			QName qname = new QName(LrcServiceConstants.NAMESPACE,LrcServiceConstants.SERVICE_NAME_LOCAL_PART);
			lRCService = (LRCService) GlobalResourceLoader.getService(qname);
		}
		return this.lRCService;
	}

	private ContextInfo getContextInfo() {
	    return ContextBuilder.loadContextInfo();
	}
}

