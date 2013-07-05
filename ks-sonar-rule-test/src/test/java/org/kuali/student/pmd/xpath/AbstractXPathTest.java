/*
 * Copyright 2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.pmd.xpath;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.IRuleViolation;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceType;
import net.sourceforge.pmd.rules.XPathRule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.list.GrowthList;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuali Student Team 
 *
 */
public class AbstractXPathTest {
    private static final Logger log = LoggerFactory
            .getLogger(AbstractXPathTest.class);

    /**
     * 
     */
    public AbstractXPathTest() {
        // TODO Auto-generated constructor stub
    }
    
    protected Report processXPath (String xpath, String message, Reader targetStream, SourceType sourceType) throws PMDException {
        
        PMD pmd = new PMD();
        Rule rule = new XPathRule();
        rule.addProperty("xpath", xpath);
        rule.setMessage(message);
        RuleSet ruleSet = new RuleSet();
        ruleSet.addRule(rule);

        Report report = new Report();
        RuleContext ctx = new RuleContext();
        ctx.setReport(report);
        ctx.setSourceCodeFilename("target.dat");

        pmd.processFile(targetStream, new RuleSets(ruleSet), ctx, sourceType);
        
        return report;

    }
    
    protected void assertNoMatches (String xpath, String message, Reader targetStream, SourceType sourceType) throws PMDException {
        Report report = processXPath(xpath, message, targetStream, sourceType);
    
        Iterator<IRuleViolation> it = report.iterator();
        
        if (it.hasNext()) {
            
            // TODO log the specifics of the rule violations.
            Assert.fail("Rule Violations exist");
        }
        
    }
    protected void assertReport (Report report) {
        for (Iterator<IRuleViolation> i = report.iterator(); i.hasNext();) {
            IRuleViolation rv = i.next();
            String res = "Match at line " + rv.getBeginLine() + " column " + rv.getBeginColumn();
            if (rv.getPackageName() != null && !rv.getPackageName().equals("")) {
                res += "; package name '" + rv.getPackageName() + "'";
            }
            if (rv.getMethodName() != null && !rv.getMethodName().equals("")) {
                res += "; method name '" + rv.getMethodName() + "'";
            }
            if (rv.getVariableName() != null && !rv.getVariableName().equals("")) {
                res += "; variable name '" + rv.getVariableName() + "'";
            }
            System.out.println(res);
        }
    }
    
    
}
