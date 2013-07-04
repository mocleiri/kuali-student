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
package org.kuali.student.xpath;

import java.io.FileNotFoundException;
import java.io.FileReader;


import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.SourceType;
import net.sourceforge.pmd.dfa.report.ReportTree;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuali Student Team 
 *
 */
public class TestXPathRules  extends AbstractXPathTest {
    private static final Logger log = LoggerFactory
            .getLogger(TestXPathRules.class);

    
    /**
     * 
     */
    public TestXPathRules() {
        super();
    }
    
    @Test
    public void testContainsStringBufferXPath() throws FileNotFoundException, PMDException {
           Report report = super.processXPath("//PrimaryExpression/PrimaryPrefix/AllocationExpression/ClassOrInterfaceType[@Image='StringBuffer']", "StringBuffer is not a preferred type", new FileReader("src/test/resources/ContainsStringBuffer.java"), SourceType.JAVA_16);
            
           ReportTree vt = report.getViolationTree();
           
           Assert.assertEquals(2, vt.size());
           
        
    }
   
}
