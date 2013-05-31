package org.kuali.student.sonar.database.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kuali.student.sonar.database.exception.MissingFieldException;
import org.kuali.student.sonar.database.utility.FKConstraintReport;
import org.kuali.student.sonar.database.utility.FKConstraintValidator;
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
    private FKConstraintValidator validator;

    public DatabaseIntegritySensor(RuleFinder ruleFinder, RulesProfile rulesProfile, Configuration configuration) {
        this.configuration = configuration;
        this.ruleFinder = ruleFinder;
        this.rulesProfile = rulesProfile;

        validator = new FKConstraintValidator();

        validator.setDbDriver("oracle.jdbc.driver.OracleDriver");
        validator.setDbUrl("jdbc:oracle:thin:@localhost:1521:xe");
        validator.setDbUser("KSAPP");
        validator.setDbPassword("KSAPP");

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
        FKConstraintReport report = null;

        try {
            report = validator.runFKSQL(Thread.currentThread().getContextClassLoader());
        } catch (SQLException sqle) {
            LOG.error("Error running validator " + sqle.getMessage());
            sqle.printStackTrace();
        } finally {
            try {
                validator.revert();
            } catch (MissingFieldException mfe) {
                LOG.error("Missing field from constraint " + mfe.getMessage());
                mfe.printStackTrace();
            } catch (SQLException sqle) {
                LOG.error("Error reverting FK Constraints " + sqle.getMessage());
                sqle.printStackTrace();
            } finally {
                try {
                    validator.closeConn();
                } catch (SQLException sqle) {
                    LOG.error("Error reverting FK Constraints " + sqle.getMessage());
                    sqle.printStackTrace();
                }
            }
        }

        if (report != null) {
            for (ForeignKeyConstraint constraint : report.getFieldMappingIssues()) {
                LOG.debug("FIELD MAPPING ISSUE: Field does not exists (" +
                        constraint.foreignTable + "." +
                        constraint.foreignColumn + ")");
                saveViolation(DatabseIntegrityRulesRepository.FIELD_MAPPING_RULE_KEY,
                        constraint,
                        sensorContext);
            }

            for (ForeignKeyConstraint constraint : report.getTableMappingIssues()) {
                LOG.debug("TABLE MAPPING ISSUE: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.TABLE_MAPPING_RULE_KEY,
                        constraint,
                        sensorContext);
            }

            for (ForeignKeyConstraint constraint : report.getColumnTypeIncompatabilityIssues()) {
                LOG.debug("COLUMN TYPE INCOMPATIBILITY ISSUE: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.COLUMN_TYPE_RULE_KEY,
                        constraint,
                        sensorContext);
            }

            for (ForeignKeyConstraint constraint : report.getOrphanedDataIssues()) {
                LOG.debug("PARENT KEY MISSING: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY,
                        constraint,
                        sensorContext);
            }

            for (ForeignKeyConstraint constraint : report.getOtherIssues()) {
                LOG.debug("UNHANDLED CONSTRAINT MAPPING ISSUE: " + constraint.toString() + " (see log for more details)");
                saveViolation(DatabseIntegrityRulesRepository.CONSTRAINT_MAPPING_RULE_KEY,
                        constraint,
                        sensorContext);
            }
        }
    }

    private void saveViolation(String tableMappingRuleKey, ForeignKeyConstraint constraint, SensorContext sensorContext) {
        LOG.info("SAVING VIOLATION WITH KEY " + tableMappingRuleKey);
        Violation violation = Violation.create(
                ruleFinder.findByKey(
                        DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                        tableMappingRuleKey
                ),
                new org.sonar.api.resources.File(FKConstraintValidator.FK_QUERY_PATH, validator.getMissing_FK_query_sql_filename()));
        violation.setMessage(constraint.toString());
        sensorContext.saveViolation(violation);
    }

    protected void analyzeConstraint(ForeignKeyConstraint fkConstraint,SensorContext sensorContext) {


        Violation violation = Violation.create(
                ruleFinder.findByKey(
                        DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                        DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY),
                fkConstraint);

        sensorContext.saveViolation(violation);



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