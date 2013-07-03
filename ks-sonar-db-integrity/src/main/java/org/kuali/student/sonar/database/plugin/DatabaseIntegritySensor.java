package org.kuali.student.sonar.database.plugin;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import org.apache.commons.configuration.Configuration;
import org.kuali.common.impex.model.compare.ForeignKeyDifference;
import org.kuali.common.impex.model.compare.SchemaCompareResult;
import org.kuali.common.impex.model.compare.SequenceDifference;
import org.kuali.common.impex.model.compare.TableDifference;
import org.kuali.common.impex.model.compare.ViewDifference;
import org.kuali.common.impex.model.util.CompareUtils;
import org.kuali.student.sonar.database.exception.FKConstraintException;
import org.kuali.student.sonar.database.exception.InvalidConstraintException;
import org.kuali.student.sonar.database.utility.ForeignKeyValidationContext;
import org.kuali.student.sonar.database.utility.FKConstraintReport;
import org.kuali.student.sonar.database.utility.FKConstraintValidator;
import org.kuali.student.sonar.database.utility.IntegrityUtils;
import org.kuali.student.sonar.database.utility.SchemaEqualityValidationContext;
import org.kuali.student.sonar.database.utility.SchemaEqualityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;


public class DatabaseIntegritySensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseIntegritySensor.class);
    protected static final String TABLE_KEY_PREFIX = "schemaCompare.table.";
    protected static final String VIEW_KEY_PREFIX = "schemaCompare.view.";
    protected static final String SEQUENCE_KEY_PREFIX = "schemaCompare.sequence.";
    protected static final String FOREIGNKEY_KEY_PREFIX = "schemaCompare.foreignKey.";

    protected static final String KUALI_STUDENT_PROJECT_KEY_PREFIX = "org.kuali.student:student";

    protected Configuration configuration;
    protected RulesProfile rulesProfile;
    protected RuleFinder ruleFinder;

    protected ForeignKeyValidationContext foreignKeyValidationContext;
    protected SchemaEqualityValidationContext schemaEqualityValidationContext;

    public DatabaseIntegritySensor(RuleFinder ruleFinder, RulesProfile rulesProfile, Configuration configuration) {
        this.configuration = configuration;
        this.ruleFinder = ruleFinder;
        this.rulesProfile = rulesProfile;

        init();
    }

    protected void init() {
        try {
            foreignKeyValidationContext = IntegrityUtils.buildForeignKeyValidationContext(configuration);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize context for DatabaseIntegritySensor", e);
        }

        schemaEqualityValidationContext = IntegrityUtils.buildSchemaEqualityValidationContext(configuration);
    }

    public void analyse(Project project, SensorContext sensorContext) {
        if(!foreignKeyValidationContext.getSkip()) {
            findForeignKeyViolations(sensorContext);
        }

        if (!schemaEqualityValidationContext.getSkip()) {
            findSchemaCompareViolations(sensorContext);
        }
    }

    protected void findForeignKeyViolations(SensorContext sensorContext) {
        FKConstraintValidator validator = new FKConstraintValidator();
        validator.setContext(foreignKeyValidationContext);

        FKConstraintReport report = null;

        try {
            report = validator.runFKSQL(Thread.currentThread().getContextClassLoader());
        } catch (SQLException sqle) {
            LOG.error("Error running validator " + sqle.getMessage());
            sqle.printStackTrace();
        } finally {
            try {
                validator.revert();
            } catch (InvalidConstraintException ice) {
                LOG.error("Missing field from constraint " + ice.getMessage(), ice);
                ice.printStackTrace();
            } catch (SQLException sqle) {
                LOG.error("Error reverting FK Constraints " + sqle.getMessage(), sqle);
                sqle.printStackTrace();
            } finally {
                try {
                    validator.closeConn();
                } catch (SQLException sqle) {
                    LOG.error("Error reverting FK Constraints " + sqle.getMessage(), sqle);
                    sqle.printStackTrace();
                }
            }
        }

        if (report != null) {
            File sqlFileResource = new File(foreignKeyValidationContext.getQueryFilePath(), foreignKeyValidationContext.getQueryFileName());

            for (FKConstraintException exception : report.getFieldMappingIssues()) {
                saveConstraintViolation(DatabseIntegrityRulesRepository.FIELD_MAPPING_RULE_KEY,
                        exception, sensorContext, sqlFileResource);
            }

            for (FKConstraintException exception : report.getTableMappingIssues()) {
                saveConstraintViolation(DatabseIntegrityRulesRepository.TABLE_MAPPING_RULE_KEY,
                        exception, sensorContext, sqlFileResource);
            }

            for (FKConstraintException exception : report.getColumnTypeIncompatabilityIssues()) {
                saveConstraintViolation(DatabseIntegrityRulesRepository.COLUMN_TYPE_RULE_KEY,
                        exception, sensorContext, sqlFileResource);
            }

            for (FKConstraintException exception : report.getOrphanedDataIssues()) {
                saveConstraintViolation(DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY,
                        exception, sensorContext, sqlFileResource);
            }

            for (FKConstraintException exception : report.getOtherIssues()) {
                saveConstraintViolation(DatabseIntegrityRulesRepository.CONSTRAINT_MAPPING_RULE_KEY,
                        exception, sensorContext, sqlFileResource);
            }
        }
    }

    protected void findSchemaCompareViolations(SensorContext sensorContext) {
        SchemaEqualityValidator schemaEqualityValidator = new SchemaEqualityValidator();

        SchemaCompareResult schemaCompareResult = null;
        SchemaCompareResult constraintCompareResult = null;
        try {
            schemaCompareResult = schemaEqualityValidator.compareSchemas(schemaEqualityValidationContext);
            constraintCompareResult = schemaEqualityValidator.compareConstraints(schemaEqualityValidationContext);
        } catch (JAXBException e) {
            LOG.error("Could not load schema for comparison: " + e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("Could not load schema for comparison: " + e.getMessage(), e);
        }

        if(schemaCompareResult != null) {
            File xmlSchemaResource = new File(schemaEqualityValidationContext.getAppPath(), schemaEqualityValidationContext.getAppSchemaFilename());
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
            File xmlConstraintResource = new File(schemaEqualityValidationContext.getAppPath(), schemaEqualityValidationContext.getAppConstraintsFilename());
            String keyPrefix = FOREIGNKEY_KEY_PREFIX;
            for (ForeignKeyDifference f : constraintCompareResult.getForeignKeyDifferences()) {
                saveViolation(keyPrefix + f.getType(), CompareUtils.foreignKeyDifferenceToString(f), sensorContext, xmlConstraintResource);
            }
        }
    }

    protected void saveConstraintViolation(String ruleKey, FKConstraintException fkce, SensorContext sensorContext, Resource resource) {
        LOG.debug(fkce.getMessage());
        saveViolation(ruleKey,fkce.getMessage(),sensorContext,resource);
    }

    protected void saveViolation(String ruleKey, String message, SensorContext sensorContext, Resource resource) {
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
        // return false (that the plugin should not execute for the project) if both contexts are set to skip
        return !(schemaEqualityValidationContext.getSkip() && foreignKeyValidationContext.getSkip());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}