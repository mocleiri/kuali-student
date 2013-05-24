package org.kuali.student.sonar.database.plugin;

import org.kuali.student.sonar.database.plugin.ui.ExampleFooter;
import org.kuali.student.sonar.database.plugin.ui.ExampleRubyWidget;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point for all extensions
 */
@Properties({
        @Property(
                key = DatabaseIntegrityPlugin.MPX_DATASOURCE_URL,
                name = "Impex Datasource URL",
                description = "A datasource to populate with impex data",
                defaultValue = "ERROR - property MUST be set"),
        @Property(
                key = DatabaseIntegrityPlugin.MPX_DATASOURCE_USERNAME,
                name = "Impex Datasource DBA Username",
                description = "A datasource to populate with impex data",
                defaultValue = "master"),
        @Property(
                key = DatabaseIntegrityPlugin.MPX_DATASOURCE_PASSWORD,
                name = "Impex Datasource DBA Password",
                description = "A datasource to populate with impex data",
                defaultValue = "password")

})
public final class DatabaseIntegrityPlugin extends SonarPlugin {

    public static final String    MPX_DATASOURCE_URL = "sonar.ks.db.integrity.datasource.url",
            MPX_DATASOURCE_USERNAME = "sonar.ks.db.integrity.datasource.username",
            MPX_DATASOURCE_PASSWORD = "sonar.ks.db.integrity.datasource.password";

    // This is where you're going to declare all your Sonar extensions
    public List getExtensions() {
        return Arrays.asList(
                // Rules
                DatabseIntegrityRulesRepository.class,

                // Batch
                DatabaseIntegritySensor.class,

                // UI
                ExampleFooter.class, ExampleRubyWidget.class);
    }
}
