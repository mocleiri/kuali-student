package org.kuali.student.enrollment.class2.appointment.keyvalue;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.student.r2.core.acal.dto.KeyDateInfo;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.enrollment.class2.appointment.form.RegistrationWindowsManagementForm;
import org.kuali.student.mock.utilities.TestHelper;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.class1.type.dto.TypeTypeRelationInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.constants.AtpServiceConstants;
import org.kuali.student.r2.core.constants.TypeServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeriodForAppWindowKeyDatesFinder extends UifKeyValuesFinderBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PeriodForAppWindowKeyDatesFinder.class);

    private transient AcademicCalendarService acalService;
    private transient TypeService typeService;

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        RegistrationWindowsManagementForm form = (RegistrationWindowsManagementForm)model;

        ContextInfo context = TestHelper.getContext1();
        try {
            TermInfo term = form.getTermInfo();
            if (term.getId() != null && !term.getId().isEmpty()) {
                List<KeyDateInfo> keyDateInfoList = getAcalService().getKeyDatesForTerm(term.getId(), context);
                if (!keyDateInfoList.isEmpty())
                    keyValues.add(new ConcreteKeyValue("", "Select Period..."));
                try{
                    List<TypeTypeRelationInfo> relations = getTypeService().getTypeTypeRelationsByOwnerAndType(AtpServiceConstants.MILESTONE_REGISTRATION_PERIOD_GROUP_TYPE_KEY,"kuali.type.type.relation.type.group",context);
                    for (KeyDateInfo keyDateInfo : keyDateInfoList) {
                        for (TypeTypeRelationInfo relationInfo : relations) {
                            String relatedTypeKey = relationInfo.getRelatedTypeKey();
                            if (keyDateInfo.getTypeKey().equals(relatedTypeKey) &&
                                   (AtpServiceConstants.ATP_OFFICIAL_STATE_KEY.equals(keyDateInfo.getStateKey()) ||
                                    AtpServiceConstants.ATP_OFFICIAL_STATE_KEY.equals(keyDateInfo.getStateKey()))) {
                                keyValues.add(new ConcreteKeyValue(keyDateInfo.getId(), keyDateInfo.getName()));
                                break;
                            }
                        }
                    }

                }catch (Exception e){
                    LOG.error("Error getting periods", e);
                }

            }
        }catch (Exception e){
            LOG.error("Error getting periods", e);
        }
        return keyValues;
    }

    public AcademicCalendarService getAcalService() {
        if(acalService == null) {
            acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE, AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.acalService;
    }

    public TypeService getTypeService() {
        if(typeService == null) {
            typeService = (TypeService) GlobalResourceLoader.getService(new QName(TypeServiceConstants.NAMESPACE, TypeServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.typeService;
    }

}
