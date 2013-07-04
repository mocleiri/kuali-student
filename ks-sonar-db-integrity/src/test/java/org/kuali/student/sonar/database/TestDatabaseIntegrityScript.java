package org.kuali.student.sonar.database;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.student.sonar.database.exception.FKConstraintException;
import org.kuali.student.sonar.database.exception.InvalidConstraintException;
import org.kuali.student.sonar.database.plugin.DatabseIntegrityRulesRepository;
import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;
import org.kuali.student.sonar.database.utility.FKConstraintReport;
import org.kuali.student.sonar.database.utility.FKConstraintValidator;
import org.kuali.student.sonar.database.utility.ForeignKeyValidationContext;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/17/13
 * Time: 12:08 AM
 *
 * Runs Database integrity checks by searching for unconstrained FK Relationships.
 * Finds Problems with the Mappings that are returned by the search.  It then attempts
 * to create the FK Constraint.  The addConstraint method will throw exceptions if
 * there are any issues which get handled here by adding to lists of Constraint
 * Issues.  At the end of the test the report is sent to System.out
 */
public class TestDatabaseIntegrityScript {

    private FKConstraintValidator validator;

    @Before
    public void init() throws SQLException {
        validator = new FKConstraintValidator();

        ForeignKeyValidationContext context = new ForeignKeyValidationContext();
        validator.setContext(context);
        context.setSkip(false);
        context.setQueryFileName("missing_FK_query.sql");
        context.setQueryFilePath("sql/");
        
        // attempt to load the driver class to ensure it is in the classpath
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find DB Driver Class", e);
        }

        // populate properties for creating connection to the database
        Properties props = new Properties();
        props.setProperty("user", "KSBUNDLED");
        props.setProperty("password", "KSBUNDLED");


        // create a connection to the database using JDBC and set it in the context
        context.setConnection(DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", props));
    }

    @Test
    public void testFKSQL() throws SQLException, IOException {

        FKConstraintReport report = validator.runFKSQL(Thread.currentThread().getContextClassLoader());

        System.out.println("\n****    Done Adding constraints and Detecting Orphaned Data    *****\n");

        for (FKConstraintException exception : report.getFieldMappingIssues()) {
            System.out.println(exception.getMessage());
        }

        for (FKConstraintException exception : report.getTableMappingIssues()) {
            System.out.println(exception.getMessage());
        }

        for (FKConstraintException exception : report.getColumnTypeIncompatabilityIssues()) {
            System.out.println(exception.getMessage());
        }

        for (FKConstraintException exception : report.getOrphanedDataIssues()) {
            System.out.println(exception.getMessage());
        }

        for (FKConstraintException exception : report.getOtherIssues()) {
            System.out.println(exception.getMessage());
        }

        System.out.println("\nSUMMARY");
        if (report.getFieldMappingIssues().size()>0) {
            System.out.println(report.getFieldMappingIssues().size() + " Field Mapping Issues");
        }
        if (report.getTableMappingIssues().size()>0) {
            System.out.println(report.getTableMappingIssues().size() + " Table Mapping Issues");
        }
        if (report.getColumnTypeIncompatabilityIssues().size()>0) {
            System.out.println(report.getColumnTypeIncompatabilityIssues().size() + " Column Type Issues");
        }
        if (report.getOrphanedDataIssues().size()>0) {
            System.out.println(report.getOrphanedDataIssues().size() + " Orphaned Data Issues");
        }
        if (report.getOtherIssues().size()>0) {
            System.out.println(report.getOtherIssues().size() + " Other Issues");
        }
    }

    @Test
    public void testCreateViolations() {
        ForeignKeyConstraint constraint = new ForeignKeyConstraint("localTable", "localColumn", "foreignTable", "foreignColumn", "Test Constraint");
        Violation violation = Violation.create(Rule.create(
                        DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                        DatabseIntegrityRulesRepository.PARENT_KEY_MISSING_RULE_KEY,
                        "RuleTest"),
                (Resource)constraint);
        constraint = new ForeignKeyConstraint("localTable2", "localColumn2", "foreignTable2", "foreignColumn2", "Test Constraint2");
        Violation violation2 = Violation.create(Rule.create(
                DatabseIntegrityRulesRepository.REPOSITORY_KEY,
                DatabseIntegrityRulesRepository.FIELD_MAPPING_RULE_KEY,
                "RuleTest"),
                (Resource)constraint);
    }

    @After
    public void cleanup() throws InvalidConstraintException, SQLException {
        validator.revert();
    }

}