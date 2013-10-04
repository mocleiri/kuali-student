/**
 * Copyright 2005-2013 The Kuali Foundation Licensed under the
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
 *
 */
package org.kuali.student.cm.course.service.impl;

import static org.kuali.student.logging.FormattedLogger.info;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.student.cm.course.form.OrganizationInfoWrapper;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.core.organization.service.OrganizationService;
import org.kuali.student.r2.core.search.dto.SearchParamInfo;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultCellInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.core.search.service.SearchService;


/**
 * Lookupable service class for {@link OrganizationInfoWrapper} advanced search
 * 
 * @author OpenCollab/rSmart KRAD CM Conversion Alliance!
 */
public class OrganizationInfoLookupableImpl extends LookupableImpl {

	private static final long serialVersionUID = -3027283578926320100L;
	
	private SearchService searchService;
	private OrganizationService organizationService;


	@Override
	protected List<?> getSearchResults(final LookupForm form,
                                       final Map<String, String> searchCriteria, 
                                       final boolean unbounded) {
		final List<OrganizationInfoWrapper> retval = new LinkedList<OrganizationInfoWrapper>();
		
		final List<SearchParamInfo> queryParamValueList = new LinkedList<SearchParamInfo>();
        final String organizationName = searchCriteria.get("organizationName");
        final String shortName        = searchCriteria.get("abbreviation");
        
        if (StringUtils.isNotBlank(organizationName)) {
            final SearchParamInfo displayNameParam = new SearchParamInfo();
            displayNameParam.setKey("org.queryParam.orgOptionalLongName");
            displayNameParam.getValues().add(organizationName);
            queryParamValueList.add(displayNameParam);
        }

        if (StringUtils.isNotBlank(shortName)) {
            final SearchParamInfo shortNameParam = new SearchParamInfo();
            shortNameParam.setKey("org.queryParam.orgOptionalShortName");
            shortNameParam.getValues().add(shortName);
            queryParamValueList.add(shortNameParam);
        }

        info("Searching for %s", queryParamValueList);

        final SearchRequestInfo searchRequest = new SearchRequestInfo();
        searchRequest.setSearchKey("org.search.generic");
        searchRequest.setParams(queryParamValueList);
        searchRequest.setStartAt(0);
        searchRequest.setNeededTotalResults(false);
        searchRequest.setSortColumn("org.resultColumn.orgOptionalId");
        
        SearchResultInfo searchResult = null;
        try {
        	searchResult = getOrganizationService().search(searchRequest, ContextUtils.getContextInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}

        for (final SearchResultRowInfo result : searchResult.getRows()) {
            final List<SearchResultCellInfo> cells = result.getCells();
            final OrganizationInfoWrapper cluOrgInfoDisplay = new OrganizationInfoWrapper();
            for (final SearchResultCellInfo cell : cells) {
                info("Got key %s", cell.getKey());
                info("Got key %s", cell.getValue());
                
                if ("org.resultColumn.orgId".equals(cell.getKey())) {
                    cluOrgInfoDisplay.setId(cell.getValue());
                } 
                else if ("org.resultColumn.orgOptionalLongName".equals(cell.getKey())) {
                    cluOrgInfoDisplay.setOrganizationName(cell.getValue());
                } 
            }
            retval.add(cluOrgInfoDisplay);
        }
        
		return retval;
	}
	
	protected SearchService getSearchService() {
		if (searchService == null) {
			searchService = GlobalResourceLoader.getService(new QName(LookupableConstants.NAMESPACE_PERSONSEACH, LookupableConstants.PERSONSEACH_SERVICE_NAME_LOCAL_PART));
		}
		return searchService;
	}

	protected OrganizationService getOrganizationService() {
		if (organizationService == null) {
	        organizationService = (OrganizationService) GlobalResourceLoader
                .getService(new QName("http://student.kuali.org/wsdl/organization","OrganizationService"));
		}
		return organizationService;
	}

}
