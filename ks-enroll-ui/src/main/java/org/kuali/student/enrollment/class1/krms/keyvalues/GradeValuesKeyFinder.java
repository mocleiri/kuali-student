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
package org.kuali.student.enrollment.class1.krms.keyvalues;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.dto.RuleManagementWrapper;
import org.kuali.student.enrollment.class1.krms.dto.EnrolPropositionEditor;
import org.kuali.student.enrollment.class1.krms.dto.EnrolRuleEditor;
import org.kuali.student.enrollment.class1.krms.form.KrmsComponentsForm;
import org.kuali.rice.krms.util.PropositionTreeUtil;
import org.kuali.student.common.util.ContextBuilder;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.search.dto.SearchParamInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValueInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValuesGroupInfo;
import org.kuali.student.r2.lum.lrc.model.ResultValueEntity;
import org.kuali.student.r2.lum.lrc.model.ResultValuesGroupEntity;
import org.kuali.student.r2.lum.lrc.service.LRCService;
import org.kuali.student.r2.lum.util.constants.LrcServiceConstants;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class GradeValuesKeyFinder extends UifKeyValuesFinderBase {

    private LRCService lrcService;

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        String gradeScale = "";
        if (model instanceof KrmsComponentsForm) {
            KrmsComponentsForm krmsComponentsForm = (KrmsComponentsForm) model;
            gradeScale = krmsComponentsForm.getProposition().getGradeScale();

        } else if (model instanceof MaintenanceDocumentForm) {
            MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
            Object dataObject = maintenanceForm.getDocument().getNewMaintainableObject().getDataObject();
            if (dataObject instanceof RuleManagementWrapper) {
                RuleEditor ruleEditor = ((RuleManagementWrapper) dataObject).getRuleEditor();
                PropositionEditor propositionEditor = PropositionTreeUtil.getProposition(ruleEditor);
                if ((propositionEditor != null) && (propositionEditor instanceof EnrolPropositionEditor)) {
                    gradeScale = ((EnrolPropositionEditor) propositionEditor).getGradeScale();
                }
            }
        }

        try {

            ResultValuesGroupInfo rvgInfo = this.getLRCService().getResultValuesGroup(gradeScale, this.getContextInfo());
            List<ResultValueInfo> rvInfos = this.getLRCService().getResultValuesByKeys(rvgInfo.getResultValueKeys(), this.getContextInfo());

            for (ResultValueInfo info : rvInfos) {
                keyValues.add(new ConcreteKeyValue(info.getKey(), info.getName()));
            }

        } catch (Exception e) {
        }

        return keyValues;
    }

    private LRCService getLRCService() {
        if (lrcService == null) {
            QName qname = new QName(LrcServiceConstants.NAMESPACE, LrcServiceConstants.SERVICE_NAME_LOCAL_PART);
            lrcService = (LRCService) GlobalResourceLoader.getService(qname);
        }
        return lrcService;
    }

    private ContextInfo getContextInfo() {
        return ContextBuilder.loadContextInfo();
    }
}
