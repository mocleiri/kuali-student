/**
 * Copyright 2013 The Kuali Foundation Licensed under the Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */

package org.kuali.student.r2.core.class1.search;


import org.apache.commons.lang.StringUtils;
import org.kuali.student.r1.core.organizationsearch.service.impl.OrganizationSearch;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.common.util.date.DateFormatters;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.organization.constants.OrganizationServiceConstants;
import org.kuali.student.r2.core.search.dto.SearchParamInfo;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.core.search.service.SearchManager;
import org.kuali.student.r2.core.search.service.SearchService;
import org.kuali.student.r2.core.search.util.SearchRequestHelper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizationSearchServiceImpl implements SearchService {

    public static final TypeInfo ORG_TYPE_SEARCH_TYPE;
    public static final TypeInfo ORG_PERSON_RELATION_TYPE_SEARCH_TYPE;

    public static final String ORG_TYPE_SEARCH_KEY = "org.search.orgTypes";
    public static final String ORG_PERSON_RELATION_TYPE_SEARCH_KEY = "org.search.orgPersonRelationTypes";

    private SearchManager searchManager;
    private Map<String, OrganizationSearch> searchOperations;
    @Resource
    private TypeService typeService;

    static {
        TypeInfo info = new TypeInfo();
        info.setKey(ORG_TYPE_SEARCH_KEY);
        info.setName("Org Type Search");
        info.setDescr(new RichTextHelper().fromPlain("Return search results for Org Types"));

        try {
            info.setEffectiveDate(DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.parse("01/01/2012"));
        } catch ( IllegalArgumentException ex) {
            throw new RuntimeException("failed to parse date", ex);
        }

        ORG_TYPE_SEARCH_TYPE = info;

        info = new TypeInfo();
        info.setKey(ORG_PERSON_RELATION_TYPE_SEARCH_KEY);
        info.setName("Org Person Relation Type Search");
        info.setDescr(new RichTextHelper().fromPlain("Return search results for Org Person Relation Types"));

        try {
            info.setEffectiveDate(DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER.parse("01/01/2012"));
        } catch ( IllegalArgumentException ex) {
            throw new RuntimeException("failed to parse date", ex);
        }
        ORG_PERSON_RELATION_TYPE_SEARCH_TYPE = info;
    }

    public static final class SearchParameters {
       public static final String ORG_OPTIONAL_ID = "org.queryParam.orgOptionalId";
    }

    public static final class SearchResultColumns {
        public static final String ORG_PERSON_RELATION_TYPE_ID = "org.resultColumn.orgPersonRelationType";
        public static final String ORG_PERSON_RELATION_TYPE_NAME = "org.resultColumn.orgPersonRelationTypeName";
        public static final String ORG_TYPE_ID = "org.resultColumn.orgType";
        public static final String ORG_TYPE_NAME = "org.resultColumn.orgTypeName";
    }

    public SearchManager getSearchManager() {
        return searchManager;
    }

    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    public Map<String, OrganizationSearch> getSearchOperations() {
        return searchOperations;
    }

    public void setSearchOperations(Map<String, OrganizationSearch> searchOperations) {
        this.searchOperations = searchOperations;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @Override
    public SearchResultInfo search(SearchRequestInfo searchRequest, ContextInfo contextInfo) throws MissingParameterException, InvalidParameterException, OperationFailedException, PermissionDeniedException {
        checkForMissingParameter(searchRequest, "searchRequest");

        if(searchRequest.getSearchKey().equals(ORG_TYPE_SEARCH_KEY)) {
            return searchForOrgTypes(searchRequest, contextInfo);
        } else if(searchRequest.getSearchKey().equals(ORG_PERSON_RELATION_TYPE_SEARCH_KEY)) {
            return searchForOrgPersonRelationTypes(searchRequest, contextInfo);
        }

        // Look for a Organization Search instance.
        if (searchOperations != null) {
            OrganizationSearch search = searchOperations.get(searchRequest.getSearchKey());
            if (search != null) {
                return search.search(searchRequest);
            }
        }

        return searchManager.search(searchRequest, contextInfo);
    }

    @Override
    public TypeInfo getSearchType(String searchTypeKey, ContextInfo contextInfo)
            throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException {
        checkForMissingParameter(searchTypeKey, "searchTypeKey");

        if(ORG_TYPE_SEARCH_KEY.equals(searchTypeKey)) {
            return ORG_TYPE_SEARCH_TYPE;
        } else if(ORG_PERSON_RELATION_TYPE_SEARCH_KEY.equals(searchTypeKey)) {
            return ORG_PERSON_RELATION_TYPE_SEARCH_TYPE;
        }

        return searchManager.getSearchType(searchTypeKey, contextInfo);
    }

    @Override
    public List<TypeInfo> getSearchTypes(ContextInfo contextInfo)
            throws OperationFailedException, InvalidParameterException, MissingParameterException {
        List<TypeInfo> list = new ArrayList<TypeInfo>();
        list.add(ORG_TYPE_SEARCH_TYPE);
        list.add(ORG_PERSON_RELATION_TYPE_SEARCH_TYPE);

        List<TypeInfo> searchManagerList = searchManager.getSearchTypes(contextInfo);
        if(searchManagerList != null && searchManagerList.size() > 0) {
            list.addAll(searchManagerList);
        }

        return list;
    }

    /**
     * Check for missing parameter and throw localized exception if missing
     *
     * @param param
     * @param paramName
     *            name
     * @throws org.kuali.student.r2.common.exceptions.MissingParameterException
     */
    private void checkForMissingParameter(Object param, String paramName) throws MissingParameterException {
        if (param == null) {
            throw new MissingParameterException(paramName + " can not be null");
        }
    }

    private SearchResultInfo searchForOrgTypes(SearchRequestInfo searchRequest, ContextInfo contextInfo) throws OperationFailedException, MissingParameterException, PermissionDeniedException, InvalidParameterException {
        SearchResultInfo results = new SearchResultInfo();
        List<TypeInfo> types = null;
        List<SearchParamInfo> params = searchRequest.getParams();
        SearchRequestHelper requestHelper = new SearchRequestHelper(searchRequest);
        String id = requestHelper.getParamAsString(SearchParameters.ORG_OPTIONAL_ID);

        if(!StringUtils.isEmpty(id)) {
            try {
                TypeInfo type = typeService.getType(id, contextInfo);
                types = new ArrayList<TypeInfo>();
                types.add(type);
            } catch (DoesNotExistException e) {
                throw new OperationFailedException("Unable to pull any type using type key " + id, e);
            }
        } else {
            try {
                types = typeService.getTypesByRefObjectUri(OrganizationServiceConstants.REF_OBJECT_URI_ORG, contextInfo);
            } catch (DoesNotExistException e) {
                throw new OperationFailedException("Unable to pull any types for ref object URI " + OrganizationServiceConstants.REF_OBJECT_URI_ORG, e);
            }
        }

        for(TypeInfo type : types) {
            SearchResultRowInfo row = new SearchResultRowInfo();
            row.addCell(SearchResultColumns.ORG_TYPE_ID, type.getKey());
            row.addCell(SearchResultColumns.ORG_TYPE_NAME, type.getName());

            results.getRows().add(row);
        }

        return results;
    }

    private SearchResultInfo searchForOrgPersonRelationTypes(SearchRequestInfo searchRequest, ContextInfo contextInfo) throws MissingParameterException, InvalidParameterException, OperationFailedException, PermissionDeniedException {
        SearchResultInfo results = new SearchResultInfo();
        List<TypeInfo> types = null;
        List<SearchParamInfo> params = searchRequest.getParams();
        SearchRequestHelper requestHelper = new SearchRequestHelper(searchRequest);
        String id = requestHelper.getParamAsString(SearchParameters.ORG_OPTIONAL_ID);

        if(!StringUtils.isEmpty(id)) {
            try {
                TypeInfo type = typeService.getType(id, contextInfo);
                types = new ArrayList<TypeInfo>();
                types.add(type);
            } catch (DoesNotExistException e) {
                throw new OperationFailedException("Unable to pull any type using type key " + id, e);
            }
        } else {
            try {
                types = typeService.getTypesByRefObjectUri(OrganizationServiceConstants.REF_OBJECT_URI_ORG_PERSON_RELATION, contextInfo);
            } catch (DoesNotExistException e) {
                throw new OperationFailedException("Unable to pull any types for ref object URI " + OrganizationServiceConstants.REF_OBJECT_URI_ORG_PERSON_RELATION, e);
            }
        }

        for(TypeInfo type : types) {
            SearchResultRowInfo row = new SearchResultRowInfo();
            row.addCell(SearchResultColumns.ORG_PERSON_RELATION_TYPE_ID, type.getKey());
            row.addCell(SearchResultColumns.ORG_PERSON_RELATION_TYPE_NAME, type.getName());

            results.getRows().add(row);
        }

        return results;
    }

}
