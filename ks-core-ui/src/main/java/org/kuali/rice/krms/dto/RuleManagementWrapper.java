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
package org.kuali.rice.krms.dto;

import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.tree.node.CompareTreeNode;
import org.kuali.rice.krms.tree.node.RuleEditorTreeNode;
import org.kuali.rice.krms.tree.node.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Student Team
 */
public class RuleManagementWrapper extends UifFormBase {

    private String refObjectId;
    private String namespace;
    private String refDiscriminatorType;

    private List<AgendaEditor> agendas;
    private RuleEditor ruleEditor;
    private AgendaEditor agendaEditor;

    private Tree<CompareTreeNode, String> compareTree;

    private String compareLightBoxHeader;

    public String getRefObjectId() {
        return refObjectId;
    }

    public void setRefObjectId(String refObjectId) {
        this.refObjectId = refObjectId;
    }

    public List<AgendaEditor> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<AgendaEditor> agendas) {
        this.agendas = agendas;
    }

    public Tree<CompareTreeNode, String> getCompareTree() {
        return compareTree;
    }

    public void setCompareTree(Tree<CompareTreeNode, String> compareTree) {
        this.compareTree = compareTree;
    }

    public RuleEditor getRuleEditor() {
        return ruleEditor;
    }

    public void setRuleEditor(RuleEditor ruleEditor) {
        this.ruleEditor = ruleEditor;
    }

    public AgendaEditor getAgendaEditor() {
        return agendaEditor;
    }

    public void setAgendaEditor(AgendaEditor agendaEditor) {
        this.agendaEditor = agendaEditor;
    }

    public Tree<RuleEditorTreeNode, String> getEditTree() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getEditTree();
        } else {
            return null;
        }
    }

    public Tree<TreeNode, String> getPreviewTree() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getPreviewTree();
        } else {
            return null;
        }
    }

    public Tree<TreeNode, String> getViewTree() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getViewTree();
        } else {
            return null;
        }
    }

    public void setActiveSelections(List<String> activeSelections) {
        this.getRuleEditor().setActiveSelections(activeSelections);
    }

    public List<String> getActiveSelections(){
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getActiveSelections();
        } else {
            return null;
        }
    }

    public String getSelectedKey() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getSelectedKey();
        } else {
            return null;
        }
    }

    public void setSelectedKey(String selectedKey) {
        this.getRuleEditor().setSelectedKey(selectedKey);
    }

    public String getCutKey() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getCutKey();
        } else {
            return null;
        }
    }

    public void setCutKey(String cutKey) {
        this.getRuleEditor().setCutKey(cutKey);
    }

    public String getCopyKey() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getCopyKey();
        } else {
            return null;
        }
    }

    public void setCopyKey(String copyKey) {
        this.getRuleEditor().setCopyKey(copyKey);
    }

    public String getLogicArea() {
        if(this.getRuleEditor()!=null){
            return this.getRuleEditor().getLogicArea();
        } else {
            return null;
        }
    }

    public void setLogicArea(String logicArea) {
        this.getRuleEditor().setLogicArea(logicArea);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getRefDiscriminatorType() {
        return refDiscriminatorType;
    }

    public void setRefDiscriminatorType(String refDiscriminatorType) {
        this.refDiscriminatorType = refDiscriminatorType;
    }

    public String getCompareLightBoxHeader(){
        return compareLightBoxHeader;
    }

    public void setCompareLightBoxHeader( String compareLightBoxHeader){
        this.compareLightBoxHeader = compareLightBoxHeader;
    }
}
