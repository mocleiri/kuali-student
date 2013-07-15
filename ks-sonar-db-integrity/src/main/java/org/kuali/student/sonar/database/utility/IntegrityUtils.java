/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.sonar.database.utility;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.kuali.common.impex.model.compare.service.impl.SchemaCompareServiceImpl;
import org.kuali.common.util.Assert;

public class IntegrityUtils {

    // foreign key validation keys
    protected static final String FOREIGN_KEY_VALIDATION_SKIP_KEY = "kuali.student.sonar.fkvalidation.skip";

    protected static final Boolean DEFAULT_FOREIGN_KEY_VALIDATION_SKIP = Boolean.TRUE;

    protected static final String FOREIGN_KEY_VALIDATION_JDBC_DRIVER_KEY = "kuali.student.sonar.fkvalidation.driver";

    protected static final String FOREIGN_KEY_VALIDATION_USERNAME_KEY = "kuali.student.sonar.fkvalidation.username";

    protected static final String FOREIGN_KEY_VALIDATION_PASSWORD_KEY = "kuali.student.sonar.fkvalidation.password";

    protected static final String FOREIGN_KEY_VALIDATION_URL_KEY = "kuali.student.sonar.fkvalidation.url";

    protected static final String FOREIGN_KEY_VALIDATION_QUERY_PATH = "kuali.student.sonar.fkvalidation.query.path";

    protected static final String FOREIGN_KEY_VALIDATION_QUERY_FILENAME = "kuali.student.sonar.fkvalidation.query.filename";

    // schema compare configuration keys
    protected static final String SCHEMA_COMPARE_VALIDATION_SKIP_KEY = "kuali.student.sonar.schemacompare.skip";

    protected static final Boolean DEFAULT_SCHEMA_COMPARE_VALIDATION_SKIP = Boolean.TRUE;
    
    protected static final String SCHEMA_COMPARE_VALIDATION_APP_NAME_KEY = "kuali.student.sonar.schemacompare.app.name";

    protected static final String SCHEMA_COMPARE_VALIDATION_APP_PATH_KEY = "kuali.student.sonar.schemacompare.app.path";

    protected static final String SCHEMA_COMPARE_VALIDATION_APP_SCHEMAFILE_KEY = "kuali.student.sonar.schemacompare.app.schemafile";

    protected static final String SCHEMA_COMPARE_VALIDATION_APP_CONSTRAINTSFILE_KEY = "kuali.student.sonar.schemacompare.app.constraintsfile";

    protected static final String SCHEMA_COMPARE_VALIDATION_DDL_NAME_KEY = "kuali.student.sonar.schemacompare.ddl.name";

    protected static final String SCHEMA_COMPARE_VALIDATION_DDL_PATH_KEY = "kuali.student.sonar.schemacompare.ddl.path";

    protected static final String SCHEMA_COMPARE_VALIDATION_DDL_SCHEMAFILE_KEY = "kuali.student.sonar.schemacompare.ddl.schemafile";

    protected static final String SCHEMA_COMPARE_VALIDATION_DDL_CONSTRAINTSFILE_KEY = "kuali.student.sonar.schemacompare.ddl.constraintsfile";

    public static ForeignKeyValidationContext buildForeignKeyValidationContext(Configuration configuration) throws SQLException {
        ForeignKeyValidationContext context = new ForeignKeyValidationContext();

        // find the property to determine if foreign key validation is skipped
        context.setSkip(configuration.getBoolean(FOREIGN_KEY_VALIDATION_SKIP_KEY, DEFAULT_FOREIGN_KEY_VALIDATION_SKIP));

        // if we are not skipping foreign key constraint analysis
        if(!context.getSkip()) {
            // set up database connection

            // get the driver class name
            String dbDriver = configuration.getString(FOREIGN_KEY_VALIDATION_JDBC_DRIVER_KEY);

            // get the login credentials for the database connection
            String dbUser = configuration.getString(FOREIGN_KEY_VALIDATION_USERNAME_KEY);
            String dbPassword = configuration.getString(FOREIGN_KEY_VALIDATION_PASSWORD_KEY);
            String dbUrl = configuration.getString(FOREIGN_KEY_VALIDATION_URL_KEY);

            // assert that each connection property is set
            Assert.notBlank(dbDriver, dbUser, dbPassword, dbUrl);

            // attempt to load the driver class to ensure it is in the classpath
            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find DB Driver Class", e);
            }

            // populate properties for creating connection to the database
            Properties props = new Properties();
            props.setProperty("user", dbUser);
            props.setProperty("password", dbPassword);


            // create a connection to the database using JDBC and set it in the context
            context.setConnection(DriverManager.getConnection(dbUrl, props));

            // populate foreign key query path and file name properties
            context.setQueryFilePath(configuration.getString(FOREIGN_KEY_VALIDATION_QUERY_PATH));
            context.setQueryFileName(configuration.getString(FOREIGN_KEY_VALIDATION_QUERY_FILENAME));
        }

        return context;
    }

    public static SchemaValidationContext buildSchemaValidationContext(Configuration configuration) {
        
        SchemaValidationContext context = new SchemaValidationContext();

        // find the property to determine if foreign key validation is skipped
        context.setSkip(configuration.getBoolean(SCHEMA_COMPARE_VALIDATION_SKIP_KEY, DEFAULT_SCHEMA_COMPARE_VALIDATION_SKIP));

        if (!context.getSkip()) {
            // set the instance of the SchemaCompare service to the default
            context.setCompareService(new SchemaCompareServiceImpl());

            // set properties for the KS App schema
            context.setAppSchemaName(configuration.getString(SCHEMA_COMPARE_VALIDATION_APP_NAME_KEY));
            context.setAppPath(configuration.getString(SCHEMA_COMPARE_VALIDATION_APP_PATH_KEY));
            context.setAppSchemaFilename(configuration.getString(SCHEMA_COMPARE_VALIDATION_APP_SCHEMAFILE_KEY));
            context.setAppConstraintsFilename(configuration.getString(SCHEMA_COMPARE_VALIDATION_APP_CONSTRAINTSFILE_KEY));

            // set properties for the KS DDL schema
            context.setDdlSchemaName(configuration.getString(SCHEMA_COMPARE_VALIDATION_DDL_NAME_KEY));
            context.setDdlPath(configuration.getString(SCHEMA_COMPARE_VALIDATION_DDL_PATH_KEY));
            context.setDdlSchemaFilename(configuration.getString(SCHEMA_COMPARE_VALIDATION_DDL_SCHEMAFILE_KEY));
            context.setDdlConstraintsFilename(configuration.getString(SCHEMA_COMPARE_VALIDATION_DDL_CONSTRAINTSFILE_KEY));
        }

        return context;
    }
}
