package org.kuali.student.enrollment.class2.courseoffering.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingWrapper;
import org.kuali.student.common.uif.service.KSMaintainable;

public interface ActivityOfferingMaintainable extends KSMaintainable{

    public boolean addScheduleRequestComponent(ActivityOfferingWrapper activityOfferingWrapper);

}
