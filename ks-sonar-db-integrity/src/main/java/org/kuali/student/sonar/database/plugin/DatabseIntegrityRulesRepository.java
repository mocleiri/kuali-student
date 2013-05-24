package org.kuali.student.sonar.database.plugin;


import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabseIntegrityRulesRepository extends RuleRepository {
    // Must be the same than the PMD plugin
    public static final String REPOSITORY_KEY = "dbi";
    public static final String LANGUAGE_KEY = "java";
    public static final String PARENT_KEY_MISSING_RULE_KEY = "parent.key.missing";
    public static final String TABLE_MAPPING_RULE_KEY = "table.mapping";
    private XMLRuleParser ruleParser;

    public DatabseIntegrityRulesRepository(XMLRuleParser ruleParser) {
        super(REPOSITORY_KEY, LANGUAGE_KEY);
        this.ruleParser = ruleParser;
    }

    @Override
    public List<Rule> createRules() {
        String filename = "/databaseIntegritySonarRules.xml";
        // In this example, new rules are declared in a XML file
        InputStream input = getClass().getResourceAsStream(filename);
        if (input == null) {
            throw new RuntimeException("missing file: " + filename);
        }
        try {
            return ruleParser.parse(input);

        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}