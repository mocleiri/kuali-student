package org.kuali.student.krms.util;

import org.junit.Test;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.student.krms.KSKRMSUploadTestCase;

public class TestKSKRMSAgendaDataSetupUtility extends KSKRMSUploadTestCase {

    @Test
    public void testUtility() {
        System.out.println("test");
        KSKRMSAgendaDataSetupUtility utility = new KSKRMSAgendaDataSetupUtility();
        setupKRMSServices(utility);
        utility.createKSKRMSData();
    }

    private void setupKRMSServices(KSKRMSAgendaDataSetupUtility utility) {
        utility.setAgendaBoService(agendaBoService);
        utility.setContextRepository(contextRepository);
        utility.setKrmsTypeRepository(krmsTypeRepository);
        utility.setRuleBoService(ruleBoService);
        utility.setTermBoService(termBoService);
        utility.setBoService(KRADServiceLocator.getBusinessObjectService());
    }

}
