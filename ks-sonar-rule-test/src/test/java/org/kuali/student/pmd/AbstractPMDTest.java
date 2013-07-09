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
package org.kuali.student.pmd;

import java.io.Reader;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.SourceType;

/**
 * @author Kuali Student Team 
 * 
 */
public abstract class AbstractPMDTest {

	/**
	 * 
	 */
	public AbstractPMDTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Execute the given rule on the targetStream provided.
	 * @param rule
	 * @param targetStream
	 * @param sourceType
	 * @return the report of the rule execution
	 * @throws PMDException
	 */
	protected Report processPMDRule (Rule rule, Reader targetStream, SourceType sourceType) throws PMDException {
		
		PMD pmd = new PMD();
        RuleSet ruleSet = new RuleSet();
        ruleSet.addRule(rule);

        Report report = new Report();
        RuleContext ctx = new RuleContext();
        ctx.setReport(report);
        ctx.setSourceCodeFilename("target.dat");

        pmd.processFile(targetStream, new RuleSets(ruleSet), ctx, sourceType);
        
        return report;
	}
}
