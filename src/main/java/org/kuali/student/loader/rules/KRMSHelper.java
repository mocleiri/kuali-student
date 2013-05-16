/*
 * Copyright 2010 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.loader.rules;

import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.student.r2.core.statement.dto.StatementInfo;
import org.kuali.student.r2.core.statement.service.StatementService;

import java.util.List;

/**
 * @info This class helps the loader gain access to any data related to the new KRMS format of rules.
 *       And it also provides access to persist the converted data in the KRMS format.
 * @author christoff.botha
 */
public class KRMSHelper
{
    private RuleManagementService ruleManagementService;
    private TermRepositoryService termRepositoryService;
    private KrmsTypeRepositoryService krmsTypeRepositoryService;

    public AgendaDefinition createAgenda(AgendaDefinition agenda){
        return ruleManagementService.createAgenda(agenda);
    }

    public void updateAgenda(AgendaDefinition agenda) {
        ruleManagementService.updateAgenda(agenda);
    }

    public AgendaItemDefinition createAgendaItem(AgendaItemDefinition agendaItem){
        return ruleManagementService.createAgendaItem(agendaItem);
    }

    public void updateAgendaItem(AgendaItemDefinition agendaItem) {
        ruleManagementService.updateAgendaItem(agendaItem);
    }

    public RuleDefinition createRule(RuleDefinition rule) {

        return ruleManagementService.createRule(rule);
    }

    public void updateRule(RuleDefinition rule) {
        ruleManagementService.updateRule(rule);
    }

    public TermDefinition createTerm(TermDefinition term) {
        return termRepositoryService.createTerm(term);
    }

    public void updateTerm(TermDefinition term) {
        termRepositoryService.updateTerm(term);
    }

    public PropositionDefinition createProposition(PropositionDefinition prop) {
        return ruleManagementService.createProposition(prop);
    }

    public TermSpecificationDefinition getTermSpecificationByNameAndNamespace(String name, String namespaceCode) {
        return termRepositoryService.getTermSpecificationByNameAndNamespace(name,namespaceCode);
    }


    public KrmsTypeDefinition getTypeByName(String namespaceCode, String name){
        return krmsTypeRepositoryService.getTypeByName(namespaceCode,name);
    }

    public ReferenceObjectBinding createReferenceObjectBinding(ReferenceObjectBinding referenceObjectDefinition){
        return ruleManagementService.createReferenceObjectBinding(referenceObjectDefinition);
    }

    //GETTERS AND SETTERS

    public RuleManagementService getRuleManagementService() {
        return ruleManagementService;
    }

    public void setRuleManagementService(RuleManagementService ruleManagementService) {
        this.ruleManagementService = ruleManagementService;
    }

    public TermRepositoryService getTermRepositoryService() {
        return termRepositoryService;
    }

    public void setTermRepositoryService(TermRepositoryService termRepositoryService) {
        this.termRepositoryService = termRepositoryService;
    }

    public KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return krmsTypeRepositoryService;
    }

    public void setKrmsTypeRepositoryService(KrmsTypeRepositoryService krmsTypeRepositoryService) {
        this.krmsTypeRepositoryService = krmsTypeRepositoryService;
    }



}
