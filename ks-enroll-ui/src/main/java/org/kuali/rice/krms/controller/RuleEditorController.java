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
package org.kuali.rice.krms.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.dto.AgendaEditor;
import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.dto.RuleManagementWrapper;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.service.RuleViewHelperService;
import org.kuali.rice.krms.util.AgendaUtilities;
import org.kuali.rice.krms.tree.node.SimplePropositionEditNode;
import org.kuali.rice.krms.tree.node.SimplePropositionNode;
import org.kuali.rice.krms.tree.node.RuleEditorTreeNode;
import org.kuali.rice.krms.util.KRMSConstants;
import org.kuali.rice.krms.util.PropositionTreeUtil;
import org.kuali.rice.krms.util.RuleLogicExpressionParser;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.class1.krms.util.KSKRMSConstants;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for the KS KRMS page.
 *
 * @author Kuali Student Team
 */
public class RuleEditorController extends MaintenanceDocumentController {

    /**
     * Setups a new <code>MaintenanceDocumentView</code> with the edit maintenance
     * action
     */
    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_EDIT)
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);

        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper(document);

        return getUIFModelAndView(form);
    }

    /**
     * Method used to invoke the CO inquiry view from Manage Course Offering screen while search input is Course Offering
     * Code (04a screen)
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=goToRuleView")
    public ModelAndView goToRuleView(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                     @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        //Clear the client state on new edit rule.
        form.getClientStateForSyncing().clear();

        RuleEditor ruleEditor = AgendaUtilities.retrieveSelectedRuleEditor((MaintenanceDocumentForm) form);
        this.getViewHelper(form).refreshInitTrees(ruleEditor);

        if (!form.getActionParameters().containsKey(UifParameters.NAVIGATE_TO_PAGE_ID)) {
            form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, KRMSConstants.KRMS_RULE_MAINTENANCE_PAGE_ID);
        }
        return super.navigate(form, result, request, response);
    }

    /**
     * Deletes selected rule from agenda on Manage Course Requistes page
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=deleteRule")
    public ModelAndView deleteRule(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                   @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper(document);
        String ruleKey = AgendaUtilities.getRuleKey(document);

        AgendaEditor agenda = AgendaUtilities.getSelectedAgendaEditor(ruleWrapper, ruleKey);
        if (agenda != null) {
            RuleEditor ruleEditor = agenda.getRuleEditors().get(ruleKey);

            //Only add rules to delete list that are already persisted.
            if (ruleEditor.getId() != null) {
                agenda.getDeletedRules().add(ruleEditor);
            }

            RuleEditor dummyRule = new RuleEditor(ruleEditor.getKey(), true, ruleEditor.getRuleTypeInfo());
            dummyRule.setParent(ruleEditor.getParent());
            agenda.getRuleEditors().put(ruleEditor.getKey(), dummyRule);
        }

        return getUIFModelAndView(document);
    }

    /**
     * Navigates to rule maintenance page with rule type to initialize adding of new rule.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=addRule")
    public ModelAndView addRule(@ModelAttribute("KualiForm") UifFormBase form, @SuppressWarnings("unused") BindingResult result,
                                @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        //Clear the client state on new edit rule.
        form.getClientStateForSyncing().clear();

        RuleEditor ruleEditor = AgendaUtilities.retrieveSelectedRuleEditor((MaintenanceDocumentForm) form);

        this.getViewHelper(form).refreshInitTrees(ruleEditor);

        if (!form.getActionParameters().containsKey(UifParameters.NAVIGATE_TO_PAGE_ID)) {
            form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, "KRMS-RuleMaintenance-Page");
        }
        return super.navigate(form, result, request, response);
    }

    /**
     * Call the super method to avoid the agenda tree being reloaded from the db.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=ajaxRefresh")
    public ModelAndView ajaxRefresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return getUIFModelAndView(form);
    }

    /**
     * Retrieves selected rule editor from data object.
     *
     * @param form
     * @return the current rule editor
     */
    protected RuleEditor getRuleEditor(UifFormBase form) {
        if (form instanceof MaintenanceDocumentForm) {
            MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
            Object dataObject = maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();

            if (dataObject instanceof RuleEditor) {
                return (RuleEditor) dataObject;
            } else if (dataObject instanceof RuleManagementWrapper) {
                RuleManagementWrapper wrapper = (RuleManagementWrapper) dataObject;
                return wrapper.getRuleEditor();
            }
        }

        return null;
    }

    //
    // Rule Editor Controller methods
    //

    /**
     * Method to copy rule.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=copyRule")
    public ModelAndView copyRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        //TODO: Copy rule to a different Co or AO

        return super.refresh(form, result, request, response);
    }

    /**
     * This method starts an edit on proposition.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=goToEditProposition")
    public ModelAndView goToEditProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {

        RuleViewHelperService viewHelper = this.getViewHelper(form);
        RuleEditor ruleEditor = getRuleEditor(form);

        PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
        PropositionEditor proposition = PropositionTreeUtil.getProposition(ruleEditor);
        proposition.setEditMode(true);

        if (!PropositionType.COMPOUND.getCode().equalsIgnoreCase(proposition.getPropositionTypeCode())) {

            String propositionTypeId = proposition.getTypeId();
            if (propositionTypeId == null) {
                proposition.setType(null);
            } else {

                KrmsTypeDefinition type = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().getTypeById(propositionTypeId);
                if (type != null) {
                    proposition.setType(type.getName());
                }
            }

        }

        //refresh the tree
        viewHelper.refreshInitTrees(ruleEditor);

        return getUIFModelAndView(form);
    }

    /**
     * Method to initialize the adding of proposition to rule.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=addProposition")
    public ModelAndView addProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedPropKey = ruleEditor.getSelectedKey();

        // find parent
        Node<RuleEditorTreeNode, String> root = ruleEditor.getEditTree().getRootElement();
        Node<RuleEditorTreeNode, String> parent = PropositionTreeUtil.findParentPropositionNode(root, selectedPropKey);

        //PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor);
        RuleViewHelperService viewHelper = this.getViewHelper(form);

        //Special case when only one proposition in tree or no proposition selected
        if (selectedPropKey.isEmpty() && parent == null && root.getChildren().size() > 0) {
            //Special case when now proposition selected and more than one proposition in tree
            if (root.getChildren().get(root.getChildren().size() - 1).getNodeType().contains("compoundNode")) {
                parent = root.getChildren().get(root.getChildren().size() - 1);
                selectedPropKey = parent.getChildren().get(parent.getChildren().size() - 1).getData().getProposition().getKey();
            } //Special case when one proposition in tree and no proposition selected
            else {
                parent = root;
                selectedPropKey = root.getChildren().get(root.getChildren().size() - 1).getData().getProposition().getKey();
            }
        } //If root compound proposition selected
        else if (parent != null) {
            if (parent.getNodeType().equals("treeRoot") && !parent.getChildren().get(parent.getChildren().size() - 1).getNodeType().contains("simple")) {
                parent = root.getChildren().get(root.getChildren().size() - 1);
                selectedPropKey = parent.getChildren().get(parent.getChildren().size() - 1).getData().getProposition().getKey();
            }
        }

        // add new child at appropriate spot
        if (parent != null) {
            List<Node<RuleEditorTreeNode, String>> children = parent.getChildren();
            for (int index = 0; index < children.size(); index++) {
                Node<RuleEditorTreeNode, String> child = children.get(index);

                // if our selected node is a simple proposition, add a new one after
                if (propKeyMatches(child, selectedPropKey)) {
                    // handle special case of adding to a lone simple proposition.
                    // in this case, we need to change the root level proposition to a compound proposition
                    // move the existing simple proposition as the first compound component,
                    // then add a new blank simple prop as the second compound component.
                    PropositionEditor blank = null;
                    if (parent.equals(root) && (isSimpleNode(child.getNodeType()))) {

                        // create a new compound proposition
                        blank = viewHelper.createCompoundPropositionBoStub(child.getData().getProposition(), true);
                        // don't set compound.setEditMode(true) as the Simple Prop in the compound prop is the only prop in edit mode
                        ruleEditor.setProposition(blank);
                    }
                    // handle regular case of adding a simple prop to an existing compound prop
                    else if (!parent.equals(root)) {

                        // build new Blank Proposition
                        blank = viewHelper.createSimplePropositionBoStub(child.getData().getProposition());
                        //add it to the parent
                        PropositionEditor parentProp = parent.getData().getProposition();
                        parentProp.getCompoundEditors().add(((index / 2) + 1), blank);
                    } else {
                        return getUIFModelAndView(form);
                    }
                    this.getViewHelper(form).refreshInitTrees(ruleEditor);
                    if (blank != null) {
                        ruleEditor.setSelectedKey(blank.getKey());
                    } else {
                        ruleEditor.setSelectedKey(null);
                    }
                    break;
                }
            }
        } else {
            // special case, if root has no children, add a new simple proposition
            // todo: how to add compound proposition. - just add another to the firs simple
            if (root.getChildren().isEmpty()) {
                PropositionEditor blank = viewHelper.createSimplePropositionBoStub(null);
                blank.setRuleId(ruleEditor.getId());
                ruleEditor.setPropId(blank.getId());
                ruleEditor.setProposition(blank);
            }
            this.getViewHelper(form).refreshInitTrees(ruleEditor);
        }
        return getUIFModelAndView(form);
    }

    /**
     * Returns <code>true</code> if proposition key matches tree node key
     *
     * @param node
     * @param propKey
     * @return if proposition key matches tree node key; <code>false</code> otherwise
     */
    private boolean propKeyMatches(Node<RuleEditorTreeNode, String> node, String propKey) {
        if (propKey != null && node != null && node.getData() != null && propKey.equalsIgnoreCase(node.getData().getProposition().getKey())) {
            return true;
        }
        return false;
    }

    /**
     * This method return the index of the position of the child that matches the id
     *
     * @param parent
     * @param propKey
     * @return index if found, -1 if not found
     */
    private int findChildIndex(Node<RuleEditorTreeNode, String> parent, String propKey) {
        int index;
        List<Node<RuleEditorTreeNode, String>> children = parent.getChildren();
        for (index = 0; index < children.size(); index++) {
            Node<RuleEditorTreeNode, String> child = children.get(index);
            // if our selected node is a simple proposition, add a new one after
            if (propKeyMatches(child, propKey)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Moves proposition up in tree structure.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=movePropositionUp")
    public ModelAndView movePropositionUp(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        moveSelectedProposition(form, true);

        return getUIFModelAndView(form);
    }

    /**
     * Moves proposition down in tree structure.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=movePropositionDown")
    public ModelAndView movePropositionDown(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        moveSelectedProposition(form, false);

        return getUIFModelAndView(form);
    }

    /**
     * Moves proposition up or down.
     *
     * Rough algorithm for moving a node up.
     *
     * find the following:
     *   node := the selected node
     *   parent := the selected node's parent, its containing node (via when true or when false relationship)
     *   parentsOlderCousin := the parent's level-order predecessor (sibling or cousin)
     *
     * @param form
     * @param up   whether the desired move is in an up direction
     * @throws Exception
     */
    private void moveSelectedProposition(UifFormBase form, boolean up) throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedPropKey = ruleEditor.getSelectedKey();

        // find parent
        Node<RuleEditorTreeNode, String> parent = PropositionTreeUtil.findParentPropositionNode(ruleEditor.getEditTree().getRootElement(), selectedPropKey);

        // add new child at appropriate spot
        if (parent != null) {
            List<Node<RuleEditorTreeNode, String>> children = parent.getChildren();
            for (int index = 0; index < children.size(); index++) {
                Node<RuleEditorTreeNode, String> child = children.get(index);
                // if our selected node is a simple proposition, add a new one after
                if (propKeyMatches(child, selectedPropKey) &&
                        (isSimpleNode(child.getNodeType()) ||
                                (RuleEditorTreeNode.COMPOUND_NODE_TYPE.equalsIgnoreCase(child.getNodeType())) ||
                                (child.getNodeType().contains(RuleEditorTreeNode.FIRST_IN_GROUP)) ||
                                (child.getNodeType().contains(RuleEditorTreeNode.LAST_IN_GROUP)))) {

                    //remove it from its current spot
                    PropositionEditor parentProp = parent.getData().getProposition();
                    PropositionEditor workingProp = null;
                    if (index != 0 && up) {
                        workingProp = parentProp.getCompoundEditors().remove(index / 2);
                    } else if (!up && index != (children.size() - 1)) {
                        workingProp = parentProp.getCompoundEditors().remove(index / 2);
                    }
                    if ((index > 0) && up) {
                        parentProp.getCompoundEditors().add((index / 2) - 1, workingProp);
                    } else if ((index < (children.size() - 1) && !up)) {
                        parentProp.getCompoundEditors().add((index / 2) + 1, workingProp);
                    }
                    // redisplay the tree (editMode = true)
                    this.getViewHelper(form).refreshInitTrees(ruleEditor);
                    break;
                }
            }
        }
        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

    }

    /**
     * Returns <code>true</code> if node is of type simple
     *
     * @param nodeType
     * @return if node is of type simple; <code>false</code> otherwise
     */
    public boolean isSimpleNode(String nodeType) {
        if (nodeType.contains(SimplePropositionNode.NODE_TYPE) ||
                SimplePropositionEditNode.NODE_TYPE.equalsIgnoreCase(nodeType)) {
            return true;
        }
        return false;
    }

    /**
     * Moves proposition left in tree structure.
     *
     * Rough algorithm for moving a node up.
     *
     * find the following:
     *   node := the selected node
     *   parent := the selected node's parent, its containing node (via when true or when false relationship)
     *   parentsOlderCousin := the parent's level-order predecessor (sibling or cousin)
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=movePropositionLeft")
    public ModelAndView movePropositionLeft(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedpropKey = ruleEditor.getSelectedKey();

        // find agendaEditor.getAgendaItemLine().getRule().getPropositionTree().getRootElement()parent
        Node<RuleEditorTreeNode, String> root = ruleEditor.getEditTree().getRootElement();
        Node<RuleEditorTreeNode, String> parent = PropositionTreeUtil.findParentPropositionNode(root, selectedpropKey);
        if ((parent != null) && (!parent.getNodeType().contains(RuleEditorTreeNode.ROOT_TYPE))) {
            Node<RuleEditorTreeNode, String> granny = PropositionTreeUtil.findParentPropositionNode(root, parent.getData().getProposition().getKey());
            if (!granny.equals(root)) {
                int oldIndex = findChildIndex(parent, selectedpropKey);
                int newIndex = findChildIndex(granny, parent.getData().getProposition().getKey());
                if (oldIndex >= 0 && newIndex >= 0) {
                    PropositionEditor prop = parent.getData().getProposition().getCompoundEditors().remove(oldIndex / 2);
                    if ((parent.getChildren().size() == 1) || (parent.getChildren().size() == 3)) {
                        PropositionTreeUtil.removeCompoundProp(ruleEditor.getPropositionEditor());
                    }
                    if (granny.getData().getProposition().getCompoundEditors().isEmpty()) {
                        granny.getData().getProposition().getCompoundEditors().add(newIndex, prop);
                    } else {
                        granny.getData().getProposition().getCompoundEditors().add((newIndex / 2) + 1, prop);
                    }
                    this.getViewHelper(form).refreshInitTrees(ruleEditor);
                }
            }

        }
        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);
        return getUIFModelAndView(form);
    }

    /**
     * Move proposition right in tree structure.
     *
     * Rough algorithm for moving a node Right
     * if the selected node is above a compound proposition, move it into the compound proposition as the first child
     * if the node is above a simple proposition, do nothing.
     * find the following:
     *   node := the selected node
     *   parent := the selected node's parent, its containing node
     *   nextSibling := the node after the selected node
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=movePropositionRight")
    public ModelAndView movePropositionRight(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedpropKey = ruleEditor.getSelectedKey();

        // find parent
        Node<RuleEditorTreeNode, String> parent = PropositionTreeUtil.findParentPropositionNode(
                ruleEditor.getEditTree().getRootElement(), selectedpropKey);
        if (parent != null) {
            int index = findChildIndex(parent, selectedpropKey);
            // if we are the last child, do nothing, otherwise
            if (index >= 0 && index + 1 < parent.getChildren().size()) {
                Node<RuleEditorTreeNode, String> nextSibling = parent.getChildren().get(index + 2);
                // if selected node above a compound node, move it into it as first child
                if (nextSibling.getNodeType().contains(RuleEditorTreeNode.COMPOUND_NODE_TYPE)) {
                    // remove selected node from it's current spot
                    PropositionEditor prop = parent.getData().getProposition().getCompoundEditors().remove(index / 2);
                    // add it to it's siblings children
                    nextSibling.getData().getProposition().getCompoundEditors().add(0, prop);
                }
                //Remove single parents and refresh the tree.
                PropositionTreeUtil.removeCompoundProp(parent.getData().getProposition());
                this.getViewHelper(form).refreshInitTrees(ruleEditor);
            }
        }
        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);
        return getUIFModelAndView(form);
    }

    /**
     * Introduces a new compound proposition between the selected proposition and its parent.
     * Additionally, it puts a new blank simple proposition underneath the compound proposition
     * as a sibling to the selected proposition.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=togglePropositionSimpleCompound")
    public ModelAndView togglePropositionSimpleCompound(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                        HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedPropKey = ruleEditor.getSelectedKey();

        PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
        RuleViewHelperService viewHelper = this.getViewHelper(form);

        if (!StringUtils.isBlank(selectedPropKey)) {
            // find parent
            Node<RuleEditorTreeNode, String> parent = PropositionTreeUtil.findParentPropositionNode(
                    ruleEditor.getEditTree().getRootElement(), selectedPropKey);
            if (parent != null) {

                int index = findChildIndex(parent, selectedPropKey);
                PropositionEditor propBo = parent.getChildren().get(index).getData().getProposition();

                // create a new compound proposition
                PropositionEditor compound = viewHelper.createCompoundPropositionBoStub(propBo, true);

                if (parent.getData() == null) { // SPECIAL CASE: this is the only proposition in the tree
                    ruleEditor.setProposition(compound);
                } else {
                    PropositionEditor parentBo = parent.getData().getProposition();
                    List<PropositionEditor> siblings = parentBo.getCompoundEditors();

                    int propIndex = -1;
                    for (int i = 0; i < siblings.size(); i++) {
                        if (propBo.getKey().equals(siblings.get(i).getKey())) {
                            propIndex = i;
                            break;
                        }
                    }

                    parentBo.getCompoundEditors().set(propIndex, compound);
                    compound.getCompoundEditors().get(1).setEditMode(true);
                }

                viewHelper.refreshInitTrees(ruleEditor);
                ruleEditor.setSelectedKey(compound.getCompoundEditors().get(1).getKey());

            }
        }

        return getUIFModelAndView(form);
    }

    /**
     * Paste proposition in selected position in tree structure.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=pasteProposition")
    public ModelAndView pasteProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        boolean cutAction = true;
        RuleEditor ruleEditor = getRuleEditor(form);
        RuleViewHelperService viewHelper = this.getViewHelper(form);

        // get selected id
        String selectedPropKey = ruleEditor.getSelectedKey();
        if (StringUtils.isBlank(selectedPropKey)) {
            return getUIFModelAndView(form);
        }

        // get the id to move
        String movePropKey = ruleEditor.getCutKey();
        if (StringUtils.isBlank(movePropKey)) {
            movePropKey = ruleEditor.getCopyKey();
            cutAction = false;
        }

        // check if selected and move is not the same
        if (StringUtils.isNotBlank(movePropKey)) {

            // Special case when the user copy the the only proposition in tree.
            if (selectedPropKey.equals(ruleEditor.getPropositionEditor().getKey())){
                PropositionEditor newParent = viewHelper.createCompoundPropositionBoStub(ruleEditor.getPropositionEditor(), false);
                PropositionEditor workingProp = viewHelper.copyProposition(ruleEditor.getPropositionEditor());

                // add to new
                addProposition(selectedPropKey, newParent, workingProp);
                ruleEditor.setProposition(newParent);

            } else {

                Node<RuleEditorTreeNode, String> root = ruleEditor.getEditTree().getRootElement();
                PropositionEditor newParent = getNewParent(viewHelper, ruleEditor, selectedPropKey, root);
                PropositionEditor oldParent = PropositionTreeUtil.findParentPropositionNode(root, movePropKey).getData().getProposition();
                PropositionEditor workingProp = null;

                // cut or copy from old
                if (oldParent != null) {
                    List<PropositionEditor> children = oldParent.getCompoundEditors();
                    for (int index = 0; index < children.size(); index++) {
                        if (movePropKey.equalsIgnoreCase(children.get(index).getKey())) {
                            if (cutAction) {
                                workingProp = oldParent.getCompoundEditors().remove(index);
                            } else {
                                workingProp = viewHelper.copyProposition(oldParent.getCompoundEditors().get(index));
                            }
                            break;
                        }
                    }
                }

                // add to new
                addProposition(selectedPropKey, newParent, workingProp);
            }

            //Refresh the tree.
            PropositionTreeUtil.removeCompoundProp(ruleEditor);
            viewHelper.refreshInitTrees(ruleEditor);
        }

        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

        // call the super method to avoid the agenda tree being reloaded from the db
        return getUIFModelAndView(form);
    }

    /**
     * Returns new parent for selected proposition in tree structure.     *
     *
     * @param viewHelper
     * @param ruleEditor
     * @param selectedpropKey
     * @param root
     * @return
     */
    private PropositionEditor getNewParent(RuleViewHelperService viewHelper, RuleEditor ruleEditor, String selectedpropKey, Node<RuleEditorTreeNode, String> root) {
        Node<RuleEditorTreeNode, String> parentNode = PropositionTreeUtil.findParentPropositionNode(root, selectedpropKey);
        PropositionEditor newParent;
        if (parentNode.equals(root)) {
            // special case build new top level compound proposition,
            // add existing as first child then paste cut node as 2nd child
            newParent = viewHelper.createCompoundPropositionBoStub(
                    root.getChildren().get(0).getData().getProposition(), false);
            ruleEditor.setProposition(newParent);
        } else {
            newParent = parentNode.getData().getProposition();
        }
        return newParent;
    }

    /**
     * Adds proposition at selected position.
     *
     * @param selectedpropKey
     * @param newParent
     * @param workingProp
     */
    private void addProposition(String selectedpropKey, PropositionEditor newParent, PropositionEditor workingProp) {
        // add to new
        if (newParent != null && workingProp != null) {
            List<PropositionEditor> children = newParent.getCompoundEditors();
            for (int index = 0; index < children.size(); index++) {
                if (selectedpropKey.equalsIgnoreCase(children.get(index).getKey())) {
                    children.add(index + 1, workingProp);
                    break;
                }
            }
        }
    }

    /**
     * Removes proposition.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=deleteProposition")
    public ModelAndView deleteProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedpropKey = ruleEditor.getSelectedKey();
        Node<RuleEditorTreeNode, String> root = ruleEditor.getEditTree().getRootElement();

        Node<RuleEditorTreeNode, String> parentNode = PropositionTreeUtil.findParentPropositionNode(root, selectedpropKey);

        // what if it is the root?
        if (parentNode != null && parentNode.getData() != null) { // it is not the root as there is a parent w/ a prop
            PropositionEditor parent = parentNode.getData().getProposition();
            if (parent != null) {
                List<PropositionEditor> children = (List<PropositionEditor>) parent.getCompoundComponents();
                for (int index = 0; index < children.size(); index++) {
                    if (selectedpropKey.equalsIgnoreCase(children.get(index).getKey())) {
                        parent.getCompoundComponents().remove(index);
                        break;
                    }
                }
            }
            PropositionTreeUtil.removeCompoundProp(ruleEditor);
        } else { // no parent, it is the root
            ruleEditor.reset();
        }

        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

        this.getViewHelper(form).refreshInitTrees(ruleEditor);
        return getUIFModelAndView(form);
    }

    /**
     * Updates compound operator in tree structure.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=updateCompoundOperator")
    public ModelAndView updateCompoundOperator(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        String selectedpropKey = ruleEditor.getSelectedKey();
        Node<RuleEditorTreeNode, String> parentNode = PropositionTreeUtil.findParentPropositionNode(ruleEditor.getEditTree().getRootElement(), selectedpropKey);
        PropositionEditor parent = parentNode.getData().getProposition();

        PropositionEditor proposition = PropositionTreeUtil.findProposition(parentNode, selectedpropKey);
        PropositionTreeUtil.setTypeForCompoundOpCode(parent, proposition.getCompoundOpCode());

        RuleViewHelperService viewHelper = this.getViewHelper(form);
        viewHelper.resetDescription(parent);
        viewHelper.refreshInitTrees(ruleEditor);

        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

        return getUIFModelAndView(form);
    }

    /**
     * Updates rule with new or changed propositions.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=updateProposition")
    public ModelAndView updateProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        RuleEditor ruleEditor = getRuleEditor(form);

        if (ruleEditor.getProposition() != null) {
            PropositionTreeUtil.resetNewProp(ruleEditor.getPropositionEditor());
        }

        //Reset the description on current selected proposition
        PropositionEditor proposition = PropositionTreeUtil.getProposition(ruleEditor);
        if (proposition != null) {

            //Validate the proposition and return if has errors.
            this.getViewHelper(form).validateProposition(proposition);
            if (!GlobalVariables.getMessageMap().getErrorMessages().isEmpty()) {
                return getUIFModelAndView(form);
            }

            //Reset the description and natural language for the proposition.
            this.getViewHelper(form).resetDescription(proposition);
            if (!GlobalVariables.getMessageMap().getErrorMessages().isEmpty()) {
                return getUIFModelAndView(form);
            }

            //Check if the proposition that was edited is the root proposition and replace.
            if (ruleEditor.getPropositionEditor().getKey().equals(ruleEditor.getSelectedKey())) {
                ruleEditor.setProposition(proposition);
            } else {
                //Replace old proposition if not the root proposition.
                this.setUpdatedProposition(ruleEditor.getPropositionEditor(), proposition);
            }

        }

        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

        //Remove the edit mode
        PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
        this.getViewHelper(form).refreshInitTrees(ruleEditor);

        return getUIFModelAndView(form);
    }

    /**
     * Replace old proposition with the updated proposition once the user clicked "Update Preview". We keep the
     * old proposition for when the user want to cancel the editing of this proposition.
     * <p/>
     * Recursively walk through the proposition tree and search for the proposition with the same key, if found
     * replace it at the same index.
     *
     * @param proposition
     * @param updatedProposition
     */
    private void setUpdatedProposition(PropositionEditor proposition, PropositionEditor updatedProposition) {

        if (proposition.getCompoundEditors() != null) {
            for (int i = 0; i < proposition.getCompoundEditors().size(); i++) {
                PropositionEditor childProp = proposition.getCompoundEditors().get(i);
                if (childProp.getKey().equals(updatedProposition.getKey())) {
                    proposition.getCompoundEditors().set(i, updatedProposition);
                } else {
                    setUpdatedProposition(childProp, updatedProposition);
                }
            }
        }
    }

    private void compareRulePropositions(MaintenanceDocumentForm form, RuleEditor ruleEditor) throws Exception {

        RuleManagementWrapper ruleWrapper = (RuleManagementWrapper) form.getDocument().getNewMaintainableObject().getDataObject();

        //Compare CO to CLU and display info message
        if (ruleEditor.getProposition() != null) {
            if (!this.getViewHelper(form).compareRules(ruleWrapper.getRuleEditor())) {
                GlobalVariables.getMessageMap().putInfoForSectionId(KRMSConstants.KRMS_RULE_TREE_GROUP_ID, "info.krms.tree.rule.changed");
            } else if (GlobalVariables.getMessageMap().containsMessageKey(KRMSConstants.KRMS_RULE_TREE_GROUP_ID)) {
                GlobalVariables.getMessageMap().removeAllInfoMessagesForProperty(KRMSConstants.KRMS_RULE_TREE_GROUP_ID);
            }
        }
    }

    /**
     * Updates rule and redirects to agenda maintenance page.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=updateRule")
    public ModelAndView updateRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        if (!(ruleEditor.getProposition() == null && ruleEditor.getPropId() == null)) {
            ruleEditor.setDummy(false);
            PropositionTreeUtil.resetNewProp(ruleEditor.getPropositionEditor());

            if (PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor())) {
                PropositionEditor proposition = PropositionTreeUtil.getProposition(ruleEditor);
                this.getViewHelper(form).resetDescription(proposition);
            }
        }
        this.getViewHelper(form).refreshViewTree(ruleEditor);

        //Replace edited rule with existing rule.
        RuleManagementWrapper ruleWrapper = AgendaUtilities.getRuleWrapper((MaintenanceDocumentForm) form);
        AgendaEditor agendaEditor = AgendaUtilities.getSelectedAgendaEditor(ruleWrapper, ruleEditor.getKey());
        agendaEditor.getRuleEditors().put(ruleEditor.getKey(), ruleEditor);

        if (!form.getActionParameters().containsKey(UifParameters.NAVIGATE_TO_PAGE_ID)) {
            form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, "KRMS-AgendaMaintenance-Page");
        }
        return super.navigate(form, result, request, response);
    }

    /**
     * Updates view with changed logic expressions.
     * Also does validation and displays necessary messages on view.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=updatePreview")
    public ModelAndView updatePreview(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        RuleEditor ruleEditor = getRuleEditor(form);
        parseRuleExpression(ruleEditor);

        //Clear new collection lines to remove new collection add line only for edit tree
        if (form.getNewCollectionLines().size() != 1) {
            List<String> keys = new ArrayList<String>(form.getNewCollectionLines().keySet());
            for (String key : keys) {
                if (key.contains(PropositionTreeUtil.EDIT_TREE_NEW_COLLECTION_LINE)) {
                    form.getNewCollectionLines().remove(key);
                }
            }
        }

        this.getViewHelper(form).refreshInitTrees(ruleEditor);
        return getUIFModelAndView(form);
    }

    /**
     * Validation for logic expression.
     *
     * @param ruleEditor
     */
    private void parseRuleExpression(RuleEditor ruleEditor) {
        RuleLogicExpressionParser ruleLogicExpressionParser = new RuleLogicExpressionParser();
        ruleLogicExpressionParser.setExpression(ruleEditor.getLogicArea());
        List<String> propsAlpha = this.getPropositionKeys(new ArrayList<String>(), ruleEditor.getPropositionEditor());

        //validate the expression
        List<String> errorMessages = new ArrayList<String>();
        boolean validExpression = ruleLogicExpressionParser.validateExpression(errorMessages, propsAlpha);

        //show errors and don't change anything else
        if (!validExpression) {
            for (int i = 0; i < errorMessages.size(); i++) {
                GlobalVariables.getMessageMap().putError("document.newMaintainableObject.dataObject.logicArea", errorMessages.get(i));
            }
            // reload page1
            return;
        }

        ruleEditor.setProposition(ruleLogicExpressionParser.parseExpressionIntoRule(ruleEditor));
    }

    /**
     * Returns list of proposition keys.
     *
     * @param propositionKeys
     * @param propositionEditor
     * @return
     */
    private List<String> getPropositionKeys(List<String> propositionKeys, PropositionEditor propositionEditor) {
        propositionKeys.add(propositionEditor.getKey());
        if (propositionEditor.getCompoundComponents() != null) {
            for (PropositionEditor child : propositionEditor.getCompoundEditors()) {
                this.getPropositionKeys(propositionKeys, child);
            }
        }
        return propositionKeys;
    }

    /**
     * Reverts rule to previous state and refreshes view.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=cancelEditProposition")
    public ModelAndView cancelEditProposition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        PropositionEditor root = ruleEditor.getPropositionEditor();

        //If first root and not yet updated, clear rule root
        if (root.isNewProp() && root.isEditMode()) {
            ruleEditor.reset();
            form.getView().setOnDocumentReadyScript("loadControlsInit();");
        } else {
            PropositionTreeUtil.cancelNewProp(root);
            PropositionTreeUtil.removeCompoundProp(ruleEditor);

            ruleEditor.setSelectedKey(StringUtils.EMPTY);
            PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());
        }

        //Clear new collection lines to remove new collection add line only for edit tree
        if (form.getNewCollectionLines().size() != 1) {
            List<String> keys = new ArrayList<String>(form.getNewCollectionLines().keySet());
            for (String key : keys) {
                if (key.contains(PropositionTreeUtil.EDIT_TREE_NEW_COLLECTION_LINE)) {
                    form.getNewCollectionLines().remove(key);
                }
            }
        }

        this.getViewHelper(form).refreshInitTrees(ruleEditor);

        //Compare rule with parent rule.
        compareRulePropositions((MaintenanceDocumentForm) form, ruleEditor);

        return getUIFModelAndView(form);
    }

    /**
     * Reverts rule to previous state and navigates to agenda maintenance page.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=cancelEditRule")
    public ModelAndView cancelEditRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        RuleEditor ruleEditor = getRuleEditor(form);
        PropositionEditor proposition = ruleEditor.getPropositionEditor();

        //Reset the editing tree.
        if (proposition != null) {
            PropositionTreeUtil.cancelNewProp(proposition);
        }
        PropositionTreeUtil.resetEditModeOnPropositionTree(ruleEditor.getPropositionEditor());

        if (!form.getActionParameters().containsKey(UifParameters.NAVIGATE_TO_PAGE_ID)) {
            form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, "KRMS-AgendaMaintenance-Page");
        }
        return super.navigate(form, result, request, response);
    }

    /**
     * Updates proposition type and reloads view.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=updatePropositionType")
    public ModelAndView updatePropositionType(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        PropositionEditor proposition = PropositionTreeUtil.getProposition(this.getRuleEditor(form));
        proposition.clear();
        this.getViewHelper(form).configurePropositionForType(proposition);

        return getUIFModelAndView(form);
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form     - test form
     * @param result   - Spring form binding result
     * @param request  - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=compareRules")
    public ModelAndView compareRules(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm document = (MaintenanceDocumentForm) form;
        Object dataObject = document.getDocument().getNewMaintainableObject().getDataObject();
        if (dataObject instanceof RuleManagementWrapper) {
            RuleManagementWrapper ruleWrapper = (RuleManagementWrapper) dataObject;
            String ruleId = document.getActionParamaterValue("ruleKey");
            RuleEditor ruleEditor = null;
            if ((ruleId != null) && (StringUtils.isNotBlank(ruleId))) {
                //Get a specific ruleEditor based on the ruleId.
                ruleEditor = AgendaUtilities.getSelectedRuleEditor(ruleWrapper, ruleId);
            } else {
                //Get the current editing ruleEditor.
                ruleEditor = ruleWrapper.getRuleEditor();
            }

            //Build the compare rule tree
            ruleWrapper.setCompareTree(this.getViewHelper(form).buildCompareTree(ruleEditor, ruleWrapper.getRefObjectId()));
            ruleWrapper.setCompareLightBoxHeader(ruleEditor.getRuleTypeInfo().getDescription());
        }

        // redirect back to client to display lightbox
        return showDialog("compareRuleLightBox", form, request, response);
    }

    /**
     * Returns form's view helper serivce.
     *
     * @param form
     * @return
     */
    protected RuleViewHelperService getViewHelper(UifFormBase form) {
        return (RuleViewHelperService) KSControllerHelper.getViewHelperService(form);
    }

    /**
     * Retrieves selected proposition key and initializes edit on propostion.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=getSelectedKey")
    public ModelAndView getSelectedKey(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Clear the current states of the tabs to open the first tab again with the edit tree.
        Map<String, String> states = (Map<String, String>) form.getClientStateForSyncing().get(KRMSConstants.KRMS_RULE_TABS_ID);
        states.put(KRMSConstants.KRMS_PARM_ACTIVE_TAB, KRMSConstants.KRMS_RULE_EDITWITHOBJECT_ID);

        //Set the selected rule statement key.
        String selectedKey = request.getParameter(KRMSConstants.KRMS_PARM_SELECTED_KEY);
        getRuleEditor(form).setSelectedKey(selectedKey);

        return this.goToEditProposition(form, result, request, response);
    }

}
