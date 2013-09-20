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

package org.kuali.student.lum.lu.ui.course.keyvalues;

import static org.kuali.student.logging.FormattedLogger.error;
import static org.kuali.student.logging.FormattedLogger.info;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.student.cm.course.service.impl.LookupableConstants;
import org.kuali.student.r1.core.subjectcode.service.SubjectCodeService;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultCellInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.lum.course.dto.CourseInfo;

/**
 * Return all organizations by a specific subject code.
 *
 * @author OpenCollab/rSmart KRAD CM Conversion Alliance!
 */
public class OrgsBySubjectCodeValuesFinder extends UifKeyValuesFinderBase {

	private SubjectCodeService subjectCodeService;

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> departments = new ArrayList<KeyValue>();
        
        final MaintenanceDocumentForm form = (MaintenanceDocumentForm) model;

        final SearchRequestInfo searchRequest = new SearchRequestInfo();
        searchRequest.setSearchKey("subjectCode.search.orgsForSubjectCode");

        final CourseInfo course = (CourseInfo) form.getDocument().getNewMaintainableObject().getDataObject();
        searchRequest.addParam("subjectCode.queryParam.code", course.getSubjectArea());

        try {
        	for (final SearchResultRowInfo result 
                     : getSubjectCodeService().search(searchRequest, ContextUtils.getContextInfo()).getRows()) {
                String subjectCodeId = "";
                String subjectCodeShortName = "";
                String subjectCodeOptionalLongName = "";
                String subjectCodeType = "";
                
                for (final SearchResultCellInfo resultCell : result.getCells()) {
                    if ("subjectCode.resultColumn.orgId".equals(resultCell.getKey())) {
                        subjectCodeId = resultCell.getValue();
                    } else if ("subjectCode.resultColumn.orgShortName".equals(resultCell.getKey())) {
                        subjectCodeShortName = resultCell.getValue();
                    } else if ("subjectCode.resultColumn.orgLongName".equals(resultCell.getKey())) {
                    	subjectCodeOptionalLongName = resultCell.getValue();
                    } else if ("subjectCode.resultColumn.orgType".equals(resultCell.getKey())) {
                    	subjectCodeType = resultCell.getValue();
                    }
                }
                departments.add(new ConcreteKeyValue(subjectCodeId, subjectCodeOptionalLongName));
            }
            info("Returning %s", departments);

        	return departments;
        } catch (Exception e) {
        	error("Error building KeyValues List %s", e);
            throw new RuntimeException(e);
        }
    }

	protected SubjectCodeService getSubjectCodeService() {
		if (subjectCodeService == null) {
			subjectCodeService = GlobalResourceLoader.getService(new QName(LookupableConstants.NAMESPACE_SUBJECTCODE, SubjectCodeService.class.getSimpleName()));
		}
		return subjectCodeService;
	}	

}
