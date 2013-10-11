<%--
 Copyright 2007-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>

<channel:portalChannelTop channelTitle="KS KRMS Sandbox" />
<div class="body">
    <strong>Student Views</strong>
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Manage Course Offering Agendas" url="${ConfigProperties.application.url}/kr-krad/courseOfferingRules?viewTypeName=MAINTENANCE&methodToCall=maintenanceEdit&dataObjectClassName=org.kuali.student.enrollment.class1.krms.dto.CORuleManagementWrapper&viewName=COAgendaManagementView&overrideKeys=refObjectId&refObjectId=0f074dc5-91ec-4a9d-9f27-decbdbb79e8e"/></li>
        <li><portal:portalLink displayTitle="true" title="Manage Activity Offering Agendas" url="${ConfigProperties.application.url}/kr-krad/activityOfferingRules?viewTypeName=MAINTENANCE&methodToCall=maintenanceEdit&dataObjectClassName=org.kuali.student.enrollment.class1.krms.dto.AORuleManagementWrapper&viewName=AOAgendaManagementView&overrideKeys=refObjectId&refObjectId=e8639267-ae0c-40b8-ac3e-a1cc83fddec9"/></li>
        <li><portal:portalLink displayTitle="true" title="Final Exam Scheduling Matrix" url="${ConfigProperties.application.url}/kr-krad/finalExamRules?viewTypeName=MAINTENANCE&methodToCall=maintenanceEdit&dataObjectClassName=org.kuali.student.enrollment.class1.krms.dto.FERuleManagementWrapper&viewName=FEAgendaManagementView&overrideKeys=refObjectId&refObjectId=kuali.atp.type.Fall"/></li>
    </ul>
</div>
<channel:portalChannelBottom />
