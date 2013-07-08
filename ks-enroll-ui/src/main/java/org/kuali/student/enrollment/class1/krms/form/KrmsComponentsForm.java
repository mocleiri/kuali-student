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
package org.kuali.student.enrollment.class1.krms.form;

import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krms.impl.ui.TermParameter;
import org.kuali.student.enrollment.class1.krms.dto.CluInformation;
import org.kuali.student.enrollment.class1.krms.dto.EnrolPropositionEditor;
import org.kuali.student.enrollment.class1.krms.dto.EnrolRuleEditor;
import org.kuali.rice.krms.tree.node.TreeNode;
import org.kuali.student.common.uif.form.KSUifForm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class KrmsComponentsForm extends KSUifForm {

    private EnrolPropositionEditor proposition = new EnrolPropositionEditor();

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String field5;

    private Tree<TreeNode, String> tree1;

    private List<TermParameter> list6 = new ArrayList<TermParameter>();

    private List<TermParameter> list10;

    private Date testDate;

    private EnrolRuleEditor rule = new EnrolRuleEditor();

    private String approveCourse;
    private String clueSetCourse;

    public KrmsComponentsForm(){
        list6.add(this.createTermParameter("MATH100"));
        list6.add(this.createTermParameter("BIOL100"));
        list6.add(this.createTermParameter("CCJM100"));
    }

    public String getApproveCourse() {
        return approveCourse;
    }

    public void setApproveCourse(String approveCourse) {
        this.approveCourse = approveCourse;
    }

    public String getClueSetCourse() {
        return clueSetCourse;
    }

    public void setClueSetCourse(String clueSetCourse) {
        this.clueSetCourse = clueSetCourse;
    }

    private TermParameter createTermParameter(String parm){
        TermParameter parameter = new TermParameter();
        parameter.setParameter(parm);
        return parameter;
    }

    /**
     * @return the list6
     */
    public List<TermParameter> getList6() {
        return this.list6;
    }

    /**
     * @param list6 the list6 to set
     */
    public void setList6(List<TermParameter> list6) {
        this.list6 = list6;
    }

    public EnrolPropositionEditor getProposition() {
        return proposition;
    }

    public void setProposition(EnrolPropositionEditor proposition) {
        this.proposition = proposition;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field) {
        this.field1 = field;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field) {
        this.field2 = field;
    }

    /**
     * @return the tree1
     */
    public Tree<TreeNode, String> getTree1() {
        if (tree1 == null){
            this.initTree();
        }
        return this.tree1;
    }

    /**
     * @param tree1 the tree1 to set
     */
    public void setTree1(Tree<TreeNode, String> tree1) {
        this.tree1 = tree1;
    }

    private void initTree(){

        tree1 = new Tree<TreeNode, String>();

        Node<TreeNode, String> item1 = createNewNode("Item 1", "Item 1");
        item1.addChild(createNewNode("SubItem A", "SubItem A"));
        item1.addChild(createNewNode("SubItem B", "SubItem B"));

        Node<TreeNode, String> item2 = createNewNode("Item 2", "Item 2");
        item2.addChild(createNewNode("SubItem A", "SubItem A"));
        Node<TreeNode, String> sub2B = createNewNode("SubItem B", "SubItem B");
        sub2B.addChild(createNewNode("Item B-1", "Item B-1"));
        sub2B.addChild(createNewNode("Item B-2", "Item B-2"));
        sub2B.addChild(createNewNode("Item B-3", "Item B-3"));
        item2.addChild(sub2B);
        item2.addChild(createNewNode("SubItem C", "SubItem C"));

        Node<TreeNode, String> root = createNewNode("Root", "Root");
        root.addChild(item1);
        root.addChild(item2);

        tree1.setRootElement(root);
    }

    private Node<TreeNode, String> createNewNode(String data, String label){
        TreeNode treeNode = new TreeNode(data);
        return new Node<TreeNode, String>(treeNode, label);
    }

    public EnrolRuleEditor getRule() {
        return rule;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public List<TermParameter> getList10() {
        if(this.list10==null){
            this.list10 = new ArrayList<TermParameter>();
            for(int i=0; i<=15; i++){
                TermParameter parm = new TermParameter();
                parm.setParameter("ENGL" + i);
                this.list10.add(parm);
            }
        }
        return this.list10;
    }

    public void setList10(List<TermParameter> list10) {
        this.list10 = list10;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }
}
