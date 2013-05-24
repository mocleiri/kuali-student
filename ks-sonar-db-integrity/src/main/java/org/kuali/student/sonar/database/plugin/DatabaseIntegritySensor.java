package org.kuali.student.sonar.database.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.ActiveRuleParam;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;


public class DatabaseIntegritySensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseIntegritySensor.class);

    private Configuration configuration;
    private RulesProfile rulesProfile;
    private RuleFinder ruleFinder;

    public DatabaseIntegritySensor(RuleFinder ruleFinder, RulesProfile rulesProfile, Configuration configuration) {
        this.configuration = configuration;
        this.ruleFinder = ruleFinder;
        this.rulesProfile = rulesProfile;


        initializeJsLint();

    }

    private boolean isActivated(String ruleKey, List<ActiveRule> rules) {
        for (ActiveRule rule : rules) {
            if (ruleKey.equals(rule.getRuleKey())) {
                return true;
            }
        }
        return false;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        analyzeConstraint(new ForeignKeyConstraint("a", "b", "c", "d", "e"), sensorContext);
//        for (File javaScriptFile : project.getFileSystem().getSourceFiles(javascript)) {
//            try {
//                analyzeFile(javaScriptFile, project.getFileSystem(), sensorContext);
//            } catch (IOException e) {
//                LOG.error("Can not analyze the file {}", javaScriptFile.getAbsolutePath());
//            }
//        }
    }

    protected void analyzeConstraint(ForeignKeyConstraint fkConstraint,SensorContext sensorContext) {


        Violation violation = Violation.create(
                ruleFinder.findByKey(
                        DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                        DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY),
                fkConstraint);

        sensorContext.saveViolation(violation);

//        Resource resource = JavaScriptFile.fromIOFile(file, projectFileSystem.getSourceDirs());
//
//        Reader reader = null;
//        try {
//            reader = new StringReader(FileUtils.readFileToString(file, projectFileSystem.getSourceCharset().name()));
//
//            JSLintResult result = jsLint.lint(file.getPath(), reader);
//
//            // capture function count in file
//            List<JSFunction> functions = result.getFunctions();
//            sensorContext.saveMeasure(resource, CoreMetrics.FUNCTIONS, (double) functions.size());
//
//            // process issues found by JSLint
//            List<Issue> issues = result.getIssues();
//            for (Issue issue : issues) {
//
//                LOG.debug("JSLint warning message {}", issue.getRaw());
//
//                Rule rule = ruleFinder.findByKey(JavaScriptRuleRepository.REPOSITORY_KEY, jsLintRuleManager.getRuleIdByMessage(issue.getRaw()));
//
//                Violation violation = Violation.create(rule, resource);
//
//                violation.setLineId(issue.getLine());
//                violation.setMessage(issue.getReason());
//
//                sensorContext.saveViolation(violation);
//            }
//
//            // add special violation for unused names
//            List<JSIdentifier> unused = result.getUnused();
//            for (JSIdentifier unusedName : unused) {
//                Violation violation = Violation.create(
//                        ruleFinder.findByKey(JavaScriptRuleRepository.REPOSITORY_KEY, JsLintRuleManager.UNUSED_NAMES_KEY), resource);
//
//                violation.setLineId(unusedName.getLine());
//                violation.setMessage("'" + unusedName.getName() + "' is unused");
//
//                sensorContext.saveViolation(violation);
//            }
//
//        } finally {
//            IOUtils.closeQuietly(reader);
//        }

    }

    public boolean shouldExecuteOnProject(Project project) {
        //TODO: probably need to enable this
        //return project.getLanguage().equals(DatabseIntegrityRulesRepository.LANGUAGE_KEY);
        return true;
    }

    private void initializeJsLint() {
        RuleQuery query = RuleQuery.create();
        query.withRepositoryKey(DatabseIntegrityRulesRepository.REPOSITORY_KEY);

        List<ActiveRule> activeRules = this.rulesProfile.getActiveRules();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}