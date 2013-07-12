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

import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.builder.ComponentBuilder;
import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.dto.PropositionParameterEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.dto.TermEditor;
import org.kuali.rice.krms.dto.TermParameterEditor;
import org.kuali.rice.krms.tree.RuleCompareTreeBuilder;
import org.kuali.rice.krms.tree.RulePreviewTreeBuilder;
import org.kuali.rice.krms.tree.RuleViewTreeBuilder;
import org.kuali.rice.krms.tree.node.CompareTreeNode;
import org.kuali.rice.krms.util.PropositionTreeUtil;
import org.kuali.student.enrollment.class1.krms.builder.MultiCourseComponentBuilder;
import org.kuali.student.enrollment.class1.krms.builder.ProgramComponentBuilder;
import org.kuali.student.enrollment.class1.krms.dto.EnrolPropositionEditor;
import org.kuali.student.enrollment.class1.krms.tree.CORuleCompareTreeBuilder;
import org.kuali.student.enrollment.class1.krms.tree.EnrolRulePreviewTreeBuilder;
import org.kuali.student.enrollment.class1.krms.tree.EnrolRuleViewTreeBuilder;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.r2.common.util.ContextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kuali Student Team
 */
public class CORuleViewHelperServiceImpl extends EnrolRuleViewHelperServiceImpl {

    private RulePreviewTreeBuilder previewTreeBuilder;
    private RuleViewTreeBuilder viewTreeBuilder;
    private RuleCompareTreeBuilder compareTreeBuilder;

    /**
     *
     * @return
     */
    @Override
    public Class<? extends PropositionEditor> getPropositionEditorClass() {
        return EnrolPropositionEditor.class;
    }

    /**
     * Builds compare tree for the compare CO and CLU lightbox links.
     *
     * @param original
     * @param refObjectId
     * @return
     * @throws Exception
     */
    @Override
    public Tree<CompareTreeNode, String> buildCompareTree(RuleEditor original, String refObjectId) throws Exception {

        //Set the original nl if not already exists.
        if (original.getProposition()!=null){
            PropositionEditor originalRoot = original.getPropositionEditor();
            if (!originalRoot.getNaturalLanguage().containsKey(this.getEditTreeBuilder().getNaturalLanguageUsageKey())) {
                this.getNaturalLanguageHelper().setNaturalLanguageTreeForUsage(originalRoot, this.getEditTreeBuilder().getNaturalLanguageUsageKey());
            }
        }

        //Build the Tree
        RuleEditor compareEditor = original.getParent();
        if((compareEditor!=null)&&(compareEditor.getProposition()!=null)){
            this.getNaturalLanguageHelper().setNaturalLanguageTreeForUsage(compareEditor.getPropositionEditor(), this.getEditTreeBuilder().getNaturalLanguageUsageKey());
        }
        Tree<CompareTreeNode, String> compareTree = this.getCompareTreeBuilder().buildTree(original, compareEditor);

        return compareTree;
    }

    /**
     * Initializes the proposition, populating the type and terms.
     *
     * @param propositionEditor
     */
    protected void initPropositionEditor(PropositionEditor propositionEditor) {
        if (PropositionType.SIMPLE.getCode().equalsIgnoreCase(propositionEditor.getPropositionTypeCode())) {

            if (propositionEditor.getType() == null) {
                KrmsTypeDefinition type = this.getKrmsTypeRepositoryService().getTypeById(propositionEditor.getTypeId());
                propositionEditor.setType(type.getName());
            }

            ComponentBuilder builder = this.getTemplateRegistry().getComponentBuilderForType(propositionEditor.getType());
            if (builder != null) {
                Map<String, String> termParameters = this.getTermParameters(propositionEditor);
                builder.resolveTermParameters(propositionEditor, termParameters);
            }
        } else {
            for (PropositionEditor child : propositionEditor.getCompoundEditors()) {
                initPropositionEditor(child);
            }

        }
    }

    /**
     * Create TermEditor from the TermDefinition objects to be used in the ui and return a map of
     * the key and values of the term parameters.
     *
     * @param proposition
     * @return
     */
    protected Map<String, String> getTermParameters(PropositionEditor proposition) {

        Map<String, String> termParameters = new HashMap<String, String>();
        if (proposition.getTerm() == null) {
            PropositionParameterEditor termParameter = PropositionTreeUtil.getTermParameter(proposition.getParameters());
            if (termParameter != null) {
                String termId = termParameter.getValue();
                TermDefinition termDefinition = this.getTermRepositoryService().getTerm(termId);
                proposition.setTerm(new TermEditor(termDefinition));
            } else {
                return termParameters;
            }
        }

        for (TermParameterEditor parameter : proposition.getTerm().getEditorParameters()) {
            termParameters.put(parameter.getName(), parameter.getValue());
        }

        return termParameters;
    }

    @Override
    public RulePreviewTreeBuilder getPreviewTreeBuilder() {
        if (previewTreeBuilder == null) {
            previewTreeBuilder = new EnrolRulePreviewTreeBuilder();
            previewTreeBuilder.setNlHelper(this.getNaturalLanguageHelper());
        }
        return previewTreeBuilder;
    }

    protected RuleViewTreeBuilder getViewTreeBuilder() {
        if (viewTreeBuilder == null) {
            viewTreeBuilder = new EnrolRuleViewTreeBuilder();
            viewTreeBuilder.setNlHelper(this.getNaturalLanguageHelper());
        }
        return viewTreeBuilder;
    }

    protected RuleCompareTreeBuilder getCompareTreeBuilder() {
        if (compareTreeBuilder == null) {
            compareTreeBuilder = new CORuleCompareTreeBuilder();
            compareTreeBuilder.setNlHelper(this.getNaturalLanguageHelper());
        }
        return compareTreeBuilder;
    }

}
