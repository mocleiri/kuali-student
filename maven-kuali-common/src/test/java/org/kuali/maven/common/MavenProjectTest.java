package org.kuali.maven.common;

import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class MavenProjectTest extends MavenProject {
    List<String> modules;

    public MavenProjectTest() {
        super();
    }

    public MavenProjectTest(Model model) {
        super(model);
    }

    @Override
    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

}
