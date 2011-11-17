package org.kuali.maven.common;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for working with URL's related to Maven web site generation
 */
public class UrlBuilder {
    private static final Logger logger = LoggerFactory.getLogger(UrlBuilder.class);

    /**
     * Return true if the group id of the project positively matches the organizationGroupId. Return false if it does
     * not, or if we can't make a comparison
     */
    protected boolean isTargetGroupId(MavenProject project, String organizationGroupId) {
        // Return false if we can't do a meaningful comparison
        if (project == null || organizationGroupId == null) {
            return false;
        }

        // Check that it matches our target groupId
        return organizationGroupId.equals(project.getGroupId());
    }

    /**
     * Return the portion of the group id that is not redundant with the hostname the site is deployed to. Given a
     * project with a groupId of "org.kuali.maven.plugins" and an organizationGroupId of "org.kuali" return
     * "maven.plugins"
     */
    protected String getTrimmedGroupId(MavenProject project, String organizationGroupId) {
        String groupId = project.getGroupId();
        if (StringUtils.isEmpty(organizationGroupId)) {
            return groupId;
        }

        if (!groupId.startsWith(organizationGroupId)) {
            logger.warn("Group Id: '" + groupId + "' does not start with '" + organizationGroupId + "'");
            return groupId;
        } else {
            String trimmed = StringUtils.replace(groupId, organizationGroupId, "");
            if (trimmed.startsWith(".")) {
                trimmed = trimmed.substring(1);
            }
            return trimmed;
        }
    }

    /**
     * Replace any dots with backslashes for the groupId passed in.
     */
    protected String getGroupIdPath(String groupId) {
        return groupId.replace(".", "/");
    }

    /**
     * Return true if the artifact id should be appended to the url for site publication.
     *
     * This method returns false when generating the html web site for the top level pom for a top level Kuali project.
     *
     * We want the url to be site.kuali.org/rice not site.kuali.org/rice/rice
     */
    protected boolean isAppendArtifactId(MavenProject project, String trimmedGroupId) {
        // Always append the artifactId for single module projects
        if (isEmpty(project.getModules())) {
            return true;
        }

        // Always append the artifactId if it is different than the final
        // portion of the groupId
        if (!trimmedGroupId.endsWith(project.getArtifactId())) {
            return true;
        }

        /**
         * We have a multi-module build where the artifactId for the top level project is the same as the final portion
         * of the groupId.<br>
         * eg org.kuali.rice:rice Return false here so the public url is:<br>
         * http://site.kuali.org/rice/1.0.3<br>
         * instead of<br>
         * http://site.kuali.org/rice/rice/1.0.3<br>
         */
        return false;
    }

    /**
     * Return the portion to the right of the hostname for the published Maven web site
     */
    public String getSitePath(MavenProject project, String organizationGroupId) {
        String trimmedGroupId = getTrimmedGroupId(project, organizationGroupId);
        StringBuilder sb = new StringBuilder(trimmedGroupId);
        if (isAppendArtifactId(project, trimmedGroupId)) {
            if (sb.length() > 0) {
                sb.append("/");
            }
            sb.append(project.getArtifactId());
        }
        sb.append("/");
        sb.append(project.getVersion());
        sb.append("/");
        return sb.toString();
    }

    /**
     * Return true if the Collection passed in is null or size zero
     */
    protected boolean isEmpty(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    protected String getBaseUrl(String baseUrl, MavenProject project, SiteContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        if (!baseUrl.endsWith("/")) {
            sb.append("/");
        }
        sb.append(getSitePath(project, context.getOrganizationGroupId()));
        sb.append("/");
        sb.append(project.getVersion());
        sb.append("/");
        return sb.toString();
    }

    protected String getBaseUrl(String protocol, String hostname, MavenProject project, SiteContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol);
        sb.append("://");
        sb.append(hostname);
        sb.append("/");
        sb.append(getSitePath(project, context.getOrganizationGroupId()));
        return sb.toString();
    }

    public boolean isBaseCase(MavenProject project, String organizationGroupId) {
        // If this is the organizations groupId return true
        // This happens when using Maven to generate a site about the Kuali
        // parent pom itself
        if (isTargetGroupId(project, organizationGroupId)) {
            return true;
        }

        // Otherwise, inspect the parent project
        MavenProject parent = project.getParent();

        // If there is no parent, return true
        if (parent == null) {
            return true;
        }

        // If the parent groupId is the organizations groupId return true
        if (isTargetGroupId(parent, organizationGroupId)) {
            return true;
        }

        // False otherwise
        return false;
    }

    public String getPublicUrl(MavenProject project, SiteContext context) {
        String organizationGroupId = context.getOrganizationGroupId();
        if (isBaseCase(project, organizationGroupId)) {
            return getBaseUrl(context.getPublicUrlProtocol(), context.getHostname(), project, context);
        } else {
            return getPublicUrl(project.getParent(), context) + project.getArtifactId() + "/";
        }
    }

    public String getPublishUrlBase(MavenProject project, SiteContext context) {
        String baseUrl1 = System.getProperty("site.publish.base");
        String baseUrl2 = project.getProperties().getProperty("site.publish.base");
        if (StringUtils.isEmpty(baseUrl1) && StringUtils.isEmpty(baseUrl2)) {
            logger.warn("site.publish.base not set");
        }
        return StringUtils.isEmpty(baseUrl1) ? baseUrl2 : baseUrl1;
    }

    public boolean isSnapshot(MavenProject project) {
        String version = project.getVersion();
        int pos = version.toUpperCase().indexOf("SNAPSHOT");
        boolean isSnapshot = pos != -1;
        return isSnapshot;
    }

    public MavenProject getMavenProject(String groupId, String artifactId, String packaging) {
        MavenProject project = new MavenProject();
        project.setGroupId(groupId);
        project.setArtifactId(artifactId);
        project.setPackaging(packaging);
        return project;
    }

    public String getDownloadUrl(MavenProject project, SiteContext context) {
        String prefix = context.getDownloadPrefix();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (!prefix.endsWith("/")) {
            sb.append("/");
        }
        if (isSnapshot(project)) {
            sb.append(context.getDownloadSnapshotPrefix());
        } else {
            sb.append(context.getDownloadReleasePrefix());
        }
        sb.append("/");
        String groupId = project.getGroupId();
        sb.append(groupId.replace('.', '/'));
        sb.append("/");
        sb.append(project.getArtifactId());
        sb.append("/");
        sb.append(project.getVersion());
        sb.append("/");
        return sb.toString();
    }

}
