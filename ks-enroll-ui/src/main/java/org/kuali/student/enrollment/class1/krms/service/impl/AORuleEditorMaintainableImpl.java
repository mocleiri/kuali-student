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
package org.kuali.student.enrollment.class1.krms.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeEntryDefinitionContract;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeRuleEntry;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.dto.AgendaEditor;
import org.kuali.rice.krms.dto.AgendaTypeInfo;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.dto.RuleTypeInfo;
import org.kuali.rice.krms.service.impl.RuleEditorMaintainableImpl;
import org.kuali.rice.krms.tree.RuleCompareTreeBuilder;
import org.kuali.rice.krms.tree.RuleViewTreeBuilder;
import org.kuali.rice.krms.util.AlphaIterator;
import org.kuali.rice.krms.util.NaturalLanguageHelper;
import org.kuali.student.enrollment.class1.krms.dto.AORuleManagementWrapper;
import org.kuali.student.enrollment.class1.krms.dto.EnrolAgendaEditor;
import org.kuali.student.enrollment.class1.krms.dto.EnrolRuleEditor;
import org.kuali.student.enrollment.class1.krms.tree.EnrolRuleViewTreeBuilder;
import org.kuali.student.enrollment.class2.courseoffering.dto.CourseOfferingContextBar;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingConstants;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.class2.courseoffering.util.ManageSocConstants;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.enrollment.courseofferingset.dto.SocInfo;
import org.kuali.student.enrollment.courseofferingset.service.CourseOfferingSetService;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.common.util.constants.KSKRMSServiceConstants;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.kuali.student.r2.core.class1.state.service.StateService;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.constants.StateServiceConstants;
import org.kuali.student.r2.lum.clu.service.CluService;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Overridden class to handle AO specific maintainable functionality.
 *
 * @author Kuali Student Team
 */
public class AORuleEditorMaintainableImpl extends RuleEditorMaintainableImpl {

    private transient CluService cluService;
    private transient AtpService atpService;
    private transient CourseOfferingService courseOfferingService;

    private transient AcademicCalendarService acalService;
    private transient CourseOfferingSetService socService;
    private transient StateService stateService;

    private transient RuleViewTreeBuilder viewTreeBuilder;
    private transient NaturalLanguageHelper nlHelper;
    private AlphaIterator alphaIterator = new AlphaIterator(StringUtils.EMPTY);

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {

        AORuleManagementWrapper dataObject = new AORuleManagementWrapper();

        String aoId = dataObjectKeys.get("refObjectId");
        dataObject.setRefObjectId(aoId);

        dataObject.setNamespace(KSKRMSServiceConstants.NAMESPACE_CODE);
        dataObject.setRefDiscriminatorType("kuali.lui.type.activity.offering");

        dataObject.setAgendas(this.getAgendasForRef(dataObject.getRefDiscriminatorType(), aoId));

        //Retrieve the Reg Object information
        ActivityOfferingInfo activityOffering = null;
        if (aoId != null) {
            try {
                activityOffering = this.getCourseOfferingService().getActivityOffering(aoId, ContextUtils.createDefaultContextInfo());
            } catch (Exception e) {
                throw new RuntimeException("Could not retrieve activity offering for " + aoId);
            }
        }

        //Populate Clu Identification Information
        if (activityOffering != null) {
            StringBuilder courseNameBuilder = new StringBuilder();
            courseNameBuilder.append(activityOffering.getTermCode());
            courseNameBuilder.append(" - ");
            courseNameBuilder.append(activityOffering.getCourseOfferingCode() + activityOffering.getActivityCode());
            courseNameBuilder.append(" - ");
            courseNameBuilder.append(activityOffering.getCourseOfferingTitle());

            //Set the name prefix used for agenda and rule names.
            //dataObject.setNamePrefix(atpCode.toString() + courseOffering.getCourseOfferingCode());

            //Set the description and atp used on the screen.
            dataObject.setCluDescription(courseNameBuilder.toString());

            //try {
            //    populateContextBar(dataObject, atp.getCode());
            //} catch (Exception e) {
            //    throw new RuntimeException("Could not populate context bar.");
            //}
        }

        dataObject.setCompareTree(RuleCompareTreeBuilder.initCompareTree());

        return dataObject;
    }

    @Override
    public String getViewTypeName() {
        return KSKRMSServiceConstants.AGENDA_TYPE_COURSE;
    }

    /**
     * This method was overriden from the RuleEditorMaintainableImpl to create an EnrolAgendaEditor instead of
     * an AgendaEditor.
     *
     * @param agendaId
     * @return EnrolAgendaEditor.
     */
    @Override
    protected AgendaEditor getAgendaEditor(String agendaId) {
        AgendaDefinition agenda = this.getRuleManagementService().getAgenda(agendaId);
        return new EnrolAgendaEditor(agenda);
    }

    /**
     * Retrieves all the rules from the agenda tree and create a list of ruleeditor objects.
     * <p/>
     * Also initialize the proposition editors for each rule recursively and set natural language for the view trees.
     *
     * @param agendaItem
     * @return
     */
    @Override
    protected List<RuleEditor> getRuleEditorsFromTree(AgendaItemDefinition agendaItem, boolean initProps) {

        List<RuleEditor> rules = new ArrayList<RuleEditor>();
        if (agendaItem.getRule() != null) {

            //Build the ruleEditor
            RuleEditor ruleEditor = new EnrolRuleEditor(agendaItem.getRule());

            //Initialize the Proposition tree
            if (initProps) {
                this.initPropositionEditor(ruleEditor.getPropositionEditor());
                ruleEditor.setViewTree(this.getViewTreeBuilder().buildTree(ruleEditor));
            }

            //Add rule to list on agenda
            rules.add(ruleEditor);
        }

        if (agendaItem.getWhenTrue() != null) {
            rules.addAll(getRuleEditorsFromTree(agendaItem.getWhenTrue(), initProps));
        }

        return rules;
    }

    /**
     * Return the clu id from the canonical course that is linked to the given Activity offering id.
     *
     * @param refObjectId - the Activity offering id.
     * @return
     * @throws Exception
     */
    @Override
    public List<ReferenceObjectBinding> getParentRefOjbects(String refObjectId) {
        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        // TODO: get the Activity offering  for the given refobject and find the Parent   reference
        ActivityOfferingInfo activityOfferingInfo = null;
        try {
            activityOfferingInfo = this.getCourseOfferingService().getActivityOffering(refObjectId, ContextUtils.createDefaultContextInfo());
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve activity offering for " + refObjectId);
        }
        return this.getRuleManagementService().findReferenceObjectBindingsByReferenceObject("kuali.lui.type.course.offering", activityOfferingInfo.getCourseOfferingId());
    }
    /**
     * Return the clu id from the canonical course that is linked to the given course offering id.
     *
     * @param refObjectId - the course offering id.
     * @return
     * @throws Exception
     */
    public List<ReferenceObjectBinding> getCOParentRefOjbects(String refObjectId) {
        CourseOfferingInfo courseOffering = null;
        ActivityOfferingInfo activityOfferingInfo = null;
        try {
            activityOfferingInfo = this.getCourseOfferingService().getActivityOffering(refObjectId, ContextUtils.createDefaultContextInfo());
            if(activityOfferingInfo !=null){
            courseOffering = this.getCourseOfferingService().getCourseOffering(activityOfferingInfo.getCourseOfferingId(), ContextUtils.createDefaultContextInfo());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve course offering for " + refObjectId);
        }
        return this.getRuleManagementService().findReferenceObjectBindingsByReferenceObject("kuali.lu.type.CreditCourse", courseOffering.getCourseId());
    }

    /**
     * @param form
     * @param atpCode
     * @throws Exception
     */
    public void populateContextBar(AORuleManagementWrapper form, String atpCode) throws Exception {
        String socStateKey = null;

        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(PredicateFactory.equal("atpCode", atpCode));

        QueryByCriteria criteria = qbcBuilder.build();

        List<TermInfo> terms = getAcalService().searchForTerms(criteria, createContextInfo());

        if (terms.isEmpty()) {
            GlobalVariables.getMessageMap().putError("termCode", CourseOfferingConstants.COURSEOFFERING_MSG_ERROR_NO_TERM_IS_FOUND, atpCode);
        } else if (terms.size() > 1) {
            GlobalVariables.getMessageMap().putError("termCode", CourseOfferingConstants.COURSEOFFERING_MSG_ERROR_FOUND_MORE_THAN_ONE_TERM, atpCode);
        } else {
            //Checking soc
            List<String> socIds;
            try {
                socIds = getSocService().getSocIdsByTerm(terms.get(0).getId(), createContextInfo());
            } catch (Exception e) {
                throw convertServiceExceptionsToUI(e);
            }

            if (socIds.isEmpty()) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_SOC_NOT_EXISTS);
            } else {
                socStateKey = getSocStateKey(socIds);
            }
        }

        form.setContextBar(CourseOfferingContextBar.NEW_INSTANCE(terms.get(0), socStateKey,
                getStateService(), getAcalService(), createContextInfo()));
    }

    private String getSocStateKey(List<String> socIds) throws Exception {
        if (socIds != null && !socIds.isEmpty()) {
            List<SocInfo> targetSocs = this.getSocService().getSocsByIds(socIds, createContextInfo());
            for (SocInfo soc : targetSocs) {
                if (soc.getTypeKey().equals(CourseOfferingSetServiceConstants.MAIN_SOC_TYPE_KEY)) {
                    return soc.getStateKey();
                }
            }
        }
        return null;
    }
    /**
     * Override this method to return the reference object id of the parent object.
     *
     * @param refObjectId
     * @return
     */
    @Override
    protected List<AgendaEditor> getAgendasForRef(String discriminatorType, String refObjectId) {
        // Initialize new array lists.
        List<AgendaEditor> agendas = new ArrayList<AgendaEditor>();
        List<AgendaEditor> sortedAgendas = new ArrayList<AgendaEditor>();
        List<AgendaEditor> parentAgendas = new ArrayList<AgendaEditor>();
        List<AgendaEditor> parentOfParentAgendas = new ArrayList<AgendaEditor>();

        // Get the list of existing agendas
        List<ReferenceObjectBinding> refObjectsBindings = this.getRuleManagementService().findReferenceObjectBindingsByReferenceObject(discriminatorType, refObjectId);
        for (ReferenceObjectBinding referenceObjectBinding : refObjectsBindings) {
            agendas.add(this.getAgendaEditor(referenceObjectBinding.getKrmsObjectId()));
        }
            // Get the list of parent agendas
            List<ReferenceObjectBinding> parentRefObjects = this.getParentRefOjbects(refObjectId);
            for (ReferenceObjectBinding referenceObject : parentRefObjects) {
                parentAgendas.add(this.getAgendaEditor(referenceObject.getKrmsObjectId()));
            }
            // Get the list of parent agendas
            List<ReferenceObjectBinding> parentOfParentRefObjects = this.getCOParentRefOjbects(refObjectId);
            for (ReferenceObjectBinding referenceObject : parentOfParentRefObjects) {
                parentOfParentAgendas.add(this.getAgendaEditor(referenceObject.getKrmsObjectId()));
            }


        // Lookup existing agenda by type
        for (AgendaTypeInfo agendaTypeInfo : this.getTypeRelationships()) {
            AgendaEditor agenda = null;
            for (AgendaEditor existingAgenda : agendas) {
                if (existingAgenda.getTypeId().equals(agendaTypeInfo.getId())) {
                    agenda = existingAgenda;
                    break;
                }
            }
            if (agenda == null) {
                agenda = new AgendaEditor();
                agenda.setTypeId(agendaTypeInfo.getId());
            }

            //Set the parent agenda.
            for (AgendaEditor parent : parentAgendas) {
                if (agenda.getTypeId().equals(agenda.getTypeId())) {
                    agenda.setParent(parent);
                    break;
                }
            }
            //Set the parent of parent agenda.
            for (AgendaEditor parentOfparent : parentOfParentAgendas) {
                if (agenda.getTypeId().equals(agenda.getTypeId())) {
                    agenda.getParent().setParent(parentOfparent);
                    break;
                }
            }

            agenda.setAgendaTypeInfo(agendaTypeInfo);
            agenda.setRuleEditors(this.getRulesForAgendas(agenda));
            sortedAgendas.add(agenda);
        }

        return sortedAgendas;
    }
    /**
     * Override this method to return the rules for agendas.
     *
     * @param agenda
     * @return
     */
    @Override
    public Map<String, RuleEditor> getRulesForAgendas(AgendaEditor agenda) {

        //Get all existing rules.
        List<RuleEditor> existingRules = null;
        if (agenda.getId() != null) {
            AgendaItemDefinition firstItem = this.getRuleManagementService().getAgendaItem(agenda.getFirstItemId());
            existingRules = getRuleEditorsFromTree(firstItem, true);
        }

        //Get the parent rules
        List<RuleEditor> parentRules = null;
        if (agenda.getParent() != null) {
            AgendaItemDefinition parentItem = this.getRuleManagementService().getAgendaItem(agenda.getParent().getFirstItemId());
            parentRules = getRuleEditorsFromTree(parentItem, false);
        }

        //Get the parent rules
        List<RuleEditor> parentOfParentRules = null;
        if (agenda.getParent().getParent() != null) {
            AgendaItemDefinition parentOfParentItem = this.getRuleManagementService().getAgendaItem(agenda.getParent().getParent().getFirstItemId());
            parentOfParentRules = getRuleEditorsFromTree(parentOfParentItem, false);
        }

        //Add dummy RuleEditors for empty rule types.
        Map<String, RuleEditor> ruleEditors = new LinkedHashMap<String, RuleEditor>();
        for (RuleTypeInfo ruleType : agenda.getAgendaTypeInfo().getRuleTypes()) {
            RuleEditor ruleEditor = null;

            // Add all existing rules of this type.
            if (existingRules != null) {
                for (RuleEditor rule : existingRules) {
                    if (rule.getTypeId().equals(ruleType.getId()) && (!rule.isDummy())) {
                        ruleEditor = rule;
                    }
                }
            }

            // If the ruletype does not exist, add an empty rule section
            if (ruleEditor == null) {
                ruleEditor = new RuleEditor();
                ruleEditor.setDummy(true);
                ruleEditor.setTypeId(ruleType.getId());
            }

            ruleEditor.setKey((String) alphaIterator.next());
            ruleEditor.setRuleTypeInfo(ruleType);
            ruleEditors.put(ruleEditor.getKey(), ruleEditor);

            //Set the parent agenda.
            if (parentRules != null) {
                for (RuleEditor parent : parentRules) {
                    if (ruleEditor.getTypeId().equals(parent.getTypeId())) {
                        ruleEditor.setParent(parent);
                        break;
                    }
                }
            }
            //Set the parent of a parent agenda.
            if (parentOfParentRules != null) {
                for (RuleEditor parentofparent : parentOfParentRules) {
                    if (ruleEditor.getTypeId().equals(parentofparent.getTypeId())) {
                        ruleEditor.getParent().setParent(parentofparent);
                        break;
                    }
                }
            }
        }

        return ruleEditors;
    }
    protected RuleViewTreeBuilder getViewTreeBuilder() {
        if (this.viewTreeBuilder == null) {
            viewTreeBuilder = new EnrolRuleViewTreeBuilder();
            viewTreeBuilder.setNlHelper(this.getNLHelper());
        }
        return viewTreeBuilder;
    }

    protected NaturalLanguageHelper getNLHelper() {
        if (this.nlHelper == null) {
            nlHelper = new NaturalLanguageHelper();
            nlHelper.setRuleManagementService(this.getRuleManagementService());
        }
        return nlHelper;
    }

    protected CluService getCluService() {
        if (cluService == null) {
            cluService = CourseOfferingResourceLoader.loadCluService();
        }
        return cluService;
    }

    private CourseOfferingService getCourseOfferingService() {
        if (courseOfferingService == null) {
            courseOfferingService = CourseOfferingResourceLoader.loadCourseOfferingService();
        }
        return courseOfferingService;
    }

    private AtpService getAtpService() {
        if (atpService == null) {
            atpService = CourseOfferingResourceLoader.loadAtpService();
        }
        return atpService;
    }

    private AcademicCalendarService getAcalService() {
        if (acalService == null) {
            acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE,
                    AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return acalService;
    }

    private CourseOfferingSetService getSocService() {
        // If it hasn't been set by Spring, then look it up by GlobalResourceLoader
        if (socService == null) {
            socService = (CourseOfferingSetService) GlobalResourceLoader.getService(new QName(CourseOfferingSetServiceConstants.NAMESPACE,
                    CourseOfferingSetServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return socService;
    }

    private StateService getStateService() {
        if (stateService == null) {
            stateService = GlobalResourceLoader.getService(new QName(StateServiceConstants.NAMESPACE, StateServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return stateService;
    }
}
