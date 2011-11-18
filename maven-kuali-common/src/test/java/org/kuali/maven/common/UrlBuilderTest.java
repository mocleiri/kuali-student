package org.kuali.maven.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class UrlBuilderTest {
    UrlBuilder builder = new UrlBuilder();
    SiteContext context = new DefaultSiteContext();

    @Test
    public void test1() {
        MavenProject project = getTopLevelRice();
        show(project);
    }

    @Test
    public void test2() {
        MavenProject project = getMavenProject(getKualiParentGAV());
        show(project);
    }

    @Test
    public void test3() {
        MavenProjectTest project = getTopLevelRice();
        List<MavenProjectTest> children = project.getChildren();
        for (MavenProjectTest child : children) {
            show(child);
        }

    }

    protected void show(MavenProject project) {
        System.out.println(builder.getDownloadUrl(project, context));
        System.out.println(builder.getPublicUrl(project, context));
        System.out.println(builder.getPublishUrl(project, context));
        System.out.println();
    }

    protected GAV getKualiParentGAV() {
        return new GAV("org.kuali.pom", "kuali", "29-SNAPSHOT");
    }

    protected GAV getKualiCommonGAV() {
        return new GAV("org.kuali.pom", "kuali-common", "110-SNAPSHOT");
    }

    protected GAV getRiceGAV() {
        return new GAV("org.kuali.rice", "rice", "2.0.0-b2-SNAPSHOT");
    }

    protected List<GAV> getRiceModuleGAVs() {
        List<GAV> gavs = new ArrayList<GAV>();
        gavs.add(new GAV("org.kuali.rice", "rice-core", "2.0.0-b2-SNAPSHOT"));
        gavs.add(new GAV("org.kuali.rice", "rice-db", "2.0.0-b2-SNAPSHOT"));
        return gavs;
    }

    protected List<MavenProjectTest> getMavenProjects(List<GAV> gavs) {
        List<MavenProjectTest> projects = new ArrayList<MavenProjectTest>();
        for (GAV gav : gavs) {
            projects.add(getMavenProject(gav));
        }
        return projects;
    }

    protected MavenProject getRiceModule() {
        MavenProjectTest project = getMavenProject(getRiceGAV());
        project.setParent(getMavenProject(getKualiCommonGAV()));
        project.setModules(getModules(getRiceModuleGAVs()));
        return project;
    }

    protected MavenProjectTest getTopLevelRice() {
        MavenProjectTest project = getMavenProject(getRiceGAV());
        project.setParent(getMavenProject(getKualiCommonGAV()));
        List<MavenProjectTest> children = getMavenProjects(getRiceModuleGAVs());
        project.setChildren(children);
        project.setModules(getModules(getRiceModuleGAVs()));
        return project;
    }

    protected List<String> getModules(List<GAV> gavs) {
        List<String> modules = new ArrayList<String>();
        for (GAV gav : gavs) {
            modules.add(gav.getArtifactId());
        }
        return modules;
    }

    protected MavenProjectTest getMavenProject(String groupId, String artifactId, String version) {
        MavenProjectTest project = new MavenProjectTest();
        project.setGroupId(groupId);
        project.setArtifactId(artifactId);
        project.setVersion(version);
        return project;
    }

    protected MavenProjectTest getMavenProject(GAV gav) {
        MavenProjectTest project = new MavenProjectTest();
        project.setGroupId(gav.getGroupId());
        project.setArtifactId(gav.getArtifactId());
        project.setVersion(gav.getVersion());
        return project;
    }
}
