package org.kuali.maven.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class UrlBuilderTest {
    UrlBuilder builder = new UrlBuilder();

    @Test
    public void test1() {
        SiteContext context = new DefaultSiteContext();
        MavenProject project = getTopLevelRice();
        System.out.println(builder.getDownloadUrl(project, context));
        System.out.println(builder.getPublicUrl(project, context));
        System.out.println(builder.getPublishUrl(project, context));
    }

    @Test
    public void test2() {
        SiteContext context = new DefaultSiteContext();
        MavenProject project = getTopLevelRice();
        System.out.println(builder.getDownloadUrl(project, context));
        System.out.println(builder.getPublicUrl(project, context));
        System.out.println(builder.getPublishUrl(project, context));
    }

    protected GAV getKualiParentGAV() {
        return new GAV("org.kuali", "kuali", "29-SNAPSHOT");
    }

    protected GAV getKualiCommonGAV() {
        return new GAV("org.kuali", "kuali-common", "110-SNAPSHOT");
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

    protected MavenProject getTopLevelRice() {
        MavenProjectTest project = getMavenProject(getRiceGAV());
        project.setParent(getMavenProject(getKualiCommonGAV()));
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
