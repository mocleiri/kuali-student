package org.kuali.student.sonar.database.plugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.configuration.Configuration;
import org.kuali.common.impex.model.compare.ForeignKeyDifference;
import org.kuali.common.impex.model.compare.SchemaCompareResult;
import org.kuali.common.impex.model.compare.SequenceDifference;
import org.kuali.common.impex.model.compare.TableDifference;
import org.kuali.common.impex.model.compare.ViewDifference;
import org.kuali.common.impex.model.util.CompareUtils;
import org.kuali.student.sonar.database.exception.MissingFieldException;
import org.kuali.student.sonar.database.utility.FKConstraintReport;
import org.kuali.student.sonar.database.utility.FKConstraintValidator;
import org.kuali.student.sonar.database.utility.SchemaEqualityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;


public class DatabaseIntegritySensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseIntegritySensor.class);
    protected static final String TABLE_KEY_PREFIX = "schemaCompare.table.";
    protected static final String VIEW_KEY_PREFIX = "schemaCompare.view.";
    protected static final String SEQUENCE_KEY_PREFIX = "schemaCompare.sequence.";
    protected static final String FOREIGNKEY_KEY_PREFIX = "schemaCompare.foreignKey.";

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
            File sqlFileResource = new File(FKConstraintValidator.FK_QUERY_PATH, validator.getMissing_FK_query_sql_filename());

            for (ForeignKeyConstraint constraint : report.getFieldMappingIssues()) {
                LOG.debug("FIELD MAPPING ISSUE: Field does not exists (" +
                        constraint.foreignTable + "." +
                        constraint.foreignColumn + ")");
                saveViolation(DatabseIntegrityRulesRepository.FIELD_MAPPING_RULE_KEY,
                        constraint.toString(),
                        sensorContext, sqlFileResource);
            }

            for (ForeignKeyConstraint constraint : report.getTableMappingIssues()) {
                LOG.debug("TABLE MAPPING ISSUE: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.TABLE_MAPPING_RULE_KEY,
                        constraint.toString(),
                        sensorContext, sqlFileResource);
            }

            for (ForeignKeyConstraint constraint : report.getColumnTypeIncompatabilityIssues()) {
                LOG.debug("COLUMN TYPE INCOMPATIBILITY ISSUE: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.COLUMN_TYPE_RULE_KEY,
                        constraint.toString(),
                        sensorContext, sqlFileResource);
            }

            for (ForeignKeyConstraint constraint : report.getOrphanedDataIssues()) {
                LOG.debug("PARENT KEY MISSING: " + constraint.toString());
                saveViolation(DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY,
                        constraint.toString(),
                        sensorContext, sqlFileResource);
            }

            for (ForeignKeyConstraint constraint : report.getOtherIssues()) {
                LOG.debug("UNHANDLED CONSTRAINT MAPPING ISSUE: " + constraint.toString() + " (see log for more details)");
                saveViolation(DatabseIntegrityRulesRepository.CONSTRAINT_MAPPING_RULE_KEY,
                        constraint.toString(),
                        sensorContext, sqlFileResource);
            }
        }

        findSchemaCompareViolations(sensorContext);

    }

    protected void findSchemaCompareViolations(SensorContext sensorContext) {
        SchemaEqualityValidator schemaValidator = new SchemaEqualityValidator();
        SchemaCompareResult schemaCompareResult = null;
        SchemaCompareResult constraintCompareResult = null;
        try {
            schemaCompareResult = schemaValidator.compareSchemas();
            constraintCompareResult = schemaValidator.compareConstraints();
        } catch (JAXBException e) {
            LOG.error("Could not load schema for comparison: " + e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("Could not load schema for comparison: " + e.getMessage(), e);
        }

        if(schemaCompareResult != null) {
            File xmlSchemaResource = new File(SchemaEqualityValidator.KS_SCHEMA_PATH, SchemaEqualityValidator.KS_SCHEMA_FILENAME);
            String keyPrefix = TABLE_KEY_PREFIX;

            for (TableDifference t : schemaCompareResult.getTableDifferences()) {
                saveViolation(keyPrefix + t.getType().toString(), CompareUtils.tableDifferenceToString(t), sensorContext, xmlSchemaResource);
            }

            keyPrefix = VIEW_KEY_PREFIX;
            for (ViewDifference v : schemaCompareResult.getViewDifferences()) {
                saveViolation(keyPrefix + v.getType().toString(), CompareUtils.viewDifferenceToString(v), sensorContext, xmlSchemaResource);
            }

            keyPrefix = SEQUENCE_KEY_PREFIX;
            for (SequenceDifference s : schemaCompareResult.getSequenceDifferences()) {
                saveViolation(keyPrefix + s.getType().toString(), CompareUtils.sequenceDifferenceToString(s), sensorContext, xmlSchemaResource);
            }
        }

        if(constraintCompareResult != null) {
            File xmlSchemaResource = new File(SchemaEqualityValidator.KS_SCHEMA_PATH, SchemaEqualityValidator.KS_CONSTRAINTS_FILENAME);
            String keyPrefix = FOREIGNKEY_KEY_PREFIX;
            for (ForeignKeyDifference f : constraintCompareResult.getForeignKeyDifferences()) {
                saveViolation(keyPrefix + f.getType(), CompareUtils.foreignKeyDifferenceToString(f), sensorContext, xmlSchemaResource);
            }
        }
    }

    protected void saveViolation(String ruleKey, String message, SensorContext sensorContext, Resource resource) {
        LOG.info("SAVING VIOLATION WITH KEY " + ruleKey);
        Violation violation = Violation.create(
                ruleFinder.findByKey(
                        DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                        ruleKey
                ),
                resource);
        violation.setMessage(message);
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