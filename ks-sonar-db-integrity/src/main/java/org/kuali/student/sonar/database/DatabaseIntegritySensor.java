package org.kuali.student.sonar.database;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

public class DatabaseIntegritySensor implements Sensor {


    @Override
    public void analyse(Project project, SensorContext sensorContext) {

    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }
}