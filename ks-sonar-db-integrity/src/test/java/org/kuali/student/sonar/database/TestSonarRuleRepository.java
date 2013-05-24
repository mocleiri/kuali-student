package org.kuali.student.sonar.database;

import org.junit.Test;
import static org.junit.Assert.fail;
import org.kuali.student.sonar.database.plugin.DatabseIntegrityRulesRepository;
import org.sonar.api.rules.XMLRuleParser;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/24/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSonarRuleRepository {

    @Test
    public void testRuleDefs() {
        XMLRuleParser parser = new XMLRuleParser();

        DatabseIntegrityRulesRepository repo = new DatabseIntegrityRulesRepository(parser);
        repo.createRules();
    }
}
