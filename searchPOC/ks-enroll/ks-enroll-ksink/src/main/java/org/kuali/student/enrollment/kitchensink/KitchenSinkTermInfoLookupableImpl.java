/**
 * Copyright 2012 The Kuali Foundation Licensed under the
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
 * Created by bobhurt on 10/29/12
 */
package org.kuali.student.enrollment.kitchensink;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.enrollment.class2.acal.util.CalendarSearchViewHelperUtil;
import org.kuali.student.enrollment.common.util.ContextBuilder;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * This class replaces org.kuali.student.enrollment.class2.acal.service.AcademicTermLookupableImpl
 * which was removed after being used by ks-ksink as a convenient example.
 *
 * @author Kuali Student Team
 */
public class KitchenSinkTermInfoLookupableImpl extends LookupableImpl {

    private transient AcademicCalendarService academicCalendarService;
    private ContextInfo contextInfo;

    @Override
    protected List<?> getSearchResults(LookupForm lookupForm, Map<String, String> fieldValues, boolean unbounded) {
        List<TermInfo> rList = null;
        String name = fieldValues.get("code");
        String year = fieldValues.get("startDate");

        try {
            rList = CalendarSearchViewHelperUtil.searchForTerms(name, year, getContextInfo(), getAcademicCalendarService());
        }
        catch (Exception ex){
            throw new RuntimeException("Error in AcademicTermLookupableImpl searching for term. name[" + name +"] year["+year +"]", ex);
        }

        return rList;
    }

    protected AcademicCalendarService getAcademicCalendarService() {
        if (academicCalendarService == null) {
            academicCalendarService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE, AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.academicCalendarService;
    }

    public ContextInfo getContextInfo() {
        if (null == contextInfo) {
            contextInfo = ContextBuilder.loadContextInfo();
        }
        return contextInfo;
    }

}
