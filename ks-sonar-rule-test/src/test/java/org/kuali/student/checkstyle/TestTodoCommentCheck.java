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
package org.kuali.student.checkstyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * Test the TodoCommentCheck
 */
public class TestTodoCommentCheck extends AbstractCheckStyleTest {

	private static final String ORIGINAL_REGEXP = "^(?!.*(KS|ks)[a-zA-Z]{2,7}-?\\d{1,6}).*(\\W|_)(TODO|FIXME)(\\W|_)";
	
	// These are from the examples given in the sonar rule: "TODOs and FIXMEs MUST include a JIRA issue number"
	private static final String ORIGINAL_ACCEPT_EX1 = "// TODO KSENROLL-555 add the logic for widget validation";
	private static final String ORIGINAL_ACCEPT_EX2 = "// FIXME: missing logic for widget validation see KSLAB-555";
	private static final String ORIGINAL_ACCEPT_EX3 = "widgetList.get(0); //FIXME-KSCM-555 this is very fragile";
	
	private static final String ORIGINAL_MATCH[] = {ORIGINAL_ACCEPT_EX1, ORIGINAL_ACCEPT_EX2, ORIGINAL_ACCEPT_EX3};
	
	private static final String TODO_REGEXP = ".*(TODO|FIXME)\\p{Blank}*KS\\p{Alpha}*-[1-9][0-9]+.*";

	private static final String TODO_INVERSE_REGEXP = ".*(TODO|FIXME)\\p{ASCII}*[^KS\\p{Alpha}*-[1-9][0-9]+].*";
	
	private static final String TODO_STRING_KSENROLL = "// TODO KSENROLL-1234";
	private static final String TODO_STRING_KSENROLL_COLON = "// TODO: KSENROLL-1234";
	private static final String FIXME_STRING_KSENROLL = "// FIXME KSENROLL-1234";

	private static final String TODO_STRING_KSCM = "// TODO KSCM-4321";
	private static final String TODO_STRING_KSCM_COLON = "// TODO: KSCM-4321";
	private static final String FIXME_STRING_KSCM = "// FIXME KSCM-4321";
	
	private static final String TODO_STRING_MISSING_JIRA = "// TODO implement me";
	private static final String FIXME_STRING_MISSING_JIRA = "// FIXME implement me";
	
	private static String MATCHES[] = {TODO_STRING_KSCM, TODO_STRING_KSCM_COLON, FIXME_STRING_KSCM, TODO_STRING_KSENROLL, TODO_STRING_KSENROLL_COLON, FIXME_STRING_KSENROLL};
	
	/**
	 * 
	 */
	public TestTodoCommentCheck() {
	}

	@Test
	public void testStartingPointRegExps() {
		
		/*
		 * Some initial testing to develop the regular expression
		 */
		
		// find KSENROLL-1234 or KSCM-1234 in the patterns.
		Assert.assertTrue(Pattern.matches(".*KS\\p{Alpha}*-[1-9][0-9]+.*", TODO_STRING_KSENROLL));
		Assert.assertTrue(Pattern.matches(".*KS\\p{Alpha}*-[1-9][0-9]+.*", TODO_STRING_KSCM));
		
		// test the full regexp that it can find TODO <something> KSENROLL-1234 or
		// TODO <something> KSCM-1234
		Assert.assertTrue(Pattern.matches(TODO_REGEXP, TODO_STRING_KSENROLL));
		Assert.assertTrue(Pattern.matches(TODO_REGEXP, TODO_STRING_KSCM));
		
		// test how to find a TODO that is missing KS<something>-1234
		Assert.assertTrue(Pattern.matches(".*[^KS\\p{Alpha}*-[1-9][0-9]+].*", TODO_STRING_MISSING_JIRA));
		
		// test the full inverse regexp can find TODO or FIXME <something> KS<something>-1234
		Assert.assertTrue(Pattern.matches(TODO_INVERSE_REGEXP, TODO_STRING_MISSING_JIRA));
		
	}
	
	@Test
	@Ignore
	// Having problems with the original regexp not really matching anything.
	public void testOriginalRegExp() {
		
		Assert.assertTrue(Pattern.matches(ORIGINAL_REGEXP, TODO_STRING_MISSING_JIRA));

		// test original regexp against new examples
		assertMatches (ORIGINAL_REGEXP, MATCHES);
		
		// test original regexp agains the sonar rule examples
		assertMatches (ORIGINAL_REGEXP, ORIGINAL_MATCH);
	}
	
	@Test
	public void testMissingJiraRegExp () {
		
		// test new regexp against all cases
		assertMatches (TODO_INVERSE_REGEXP, MATCHES);
		
		// test new regexp against the examples from the sonar rule
		assertMatches (TODO_INVERSE_REGEXP, ORIGINAL_MATCH);
		
		
		
		
		
		
	
		
	}
	
	private void assertMatches(String regExp, String[] matches) {
		
		Set<String>failedToMatch = new HashSet<String>();
		
		for (String match : matches) {
			
			if (!Pattern.matches(regExp, match)) {
				failedToMatch.add(match);
			}
			
		}
		
		if (failedToMatch.size() > 0)
			Assert.fail(regExp + " Failed to match against " + StringUtils.join(failedToMatch, ", "));
	}


	@Test
	public void testMissingJira () throws URISyntaxException, FileNotFoundException, CheckstyleException {
		
		URL resource = getClass().getClassLoader().getResource("TodoMissingJIRA.java");
		
		File fileToProcess = new File (resource.toURI());
		
		Map<String, String> checkAttributes = new HashMap<String, String>();
		
		checkAttributes.put("format", TODO_REGEXP);
		
		int errors = super.processCheckStyle(fileToProcess, "TodoComment", checkAttributes, "todo.match");
		
		Assert.assertEquals(0, errors);
		
		checkAttributes.clear();
		checkAttributes.put("format", TODO_INVERSE_REGEXP);
		
		errors = super.processCheckStyle(fileToProcess, "TodoComment", checkAttributes, "todo.match");
		
		Assert.assertEquals(1, errors);
		
		checkAttributes.clear();
		checkAttributes.put("format", ORIGINAL_REGEXP);
		
		errors = super.processCheckStyle(fileToProcess, "TodoComment", checkAttributes, "todo.match");
		
		Assert.assertEquals(1, errors);
	}
}
