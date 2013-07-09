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

import java.io.FileNotFoundException;
import java.io.FileReader;

import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.SourceType;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.student.pmd.custom.FindStringBuilderUsageOutSideOfALoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team (ks.collab@kuali.org)
 *
 * 
 */
public class TestFindStringBuilderOutsideOfALoop extends AbstractPMDTest {
	private static final Logger log = LoggerFactory
			.getLogger(TestFindStringBuilderOutsideOfALoop.class);

	/**
	 * 
	 */
	public TestFindStringBuilderOutsideOfALoop() {
	}
	
	@Test
	@Ignore
	// TODO KSENROLL-7674 need to finish implementing the custom PMD rule
	public void testRule() throws FileNotFoundException, PMDException {
		
		FindStringBuilderUsageOutSideOfALoop rule = new FindStringBuilderUsageOutSideOfALoop();
		
		super.processPMDRule(rule, new FileReader("src/test/resources/StringBuilderLoopUsage.java"), SourceType.JAVA_16);
	}
}
