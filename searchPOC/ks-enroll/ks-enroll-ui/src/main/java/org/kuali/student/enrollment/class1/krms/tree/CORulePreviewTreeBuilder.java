package org.kuali.student.enrollment.class1.krms.tree;

import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.rice.krms.api.repository.rule.RuleDefinitionContract;
import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.dto.RuleEditor;
import org.kuali.rice.krms.tree.RulePreviewTreeBuilder;
import org.kuali.rice.krms.tree.RuleViewTreeBuilder;
import org.kuali.student.enrollment.class1.krms.dto.CluInformation;
import org.kuali.student.enrollment.class1.krms.dto.EnrolPropositionEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SW
 * Date: 2013/02/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CORulePreviewTreeBuilder extends RulePreviewTreeBuilder {

    private static final long serialVersionUID = 1L;

    @Override
    public List<String> getListItems(PropositionEditor propositionEditor) {
        if (propositionEditor instanceof EnrolPropositionEditor) {
            EnrolPropositionEditor enrolProp = (EnrolPropositionEditor) propositionEditor;
            List<String> listItems = new ArrayList<String>();
            if (enrolProp.getCluSet() != null) {
                if (enrolProp.getCluSet().getClus() != null) {
                    for (CluInformation clu : enrolProp.getCluSet().getClus()) {
                        String description = clu.getCode() + " " + clu.getTitle() + " " + clu.getCredits();
                        listItems.add(description);
                    }
                }
            }
            return listItems;
        }
        return null;
    }
}
