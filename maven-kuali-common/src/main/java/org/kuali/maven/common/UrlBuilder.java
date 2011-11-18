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
     * Return true if the groupId of the project positively matches the groupId passed in. False otherwise.
     */
    protected boolean isTargetGroupId(MavenProject project, String groupId) {
        // Return false if we can't do a meaningful comparison
        if (project == null || groupId == null) {
            return false;
        }

        // Check that it matches our target groupId
        return groupId.equals(project.getGroupId());
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
     * This method returns false for multi-module projects where the top level project's artifact id matches the final
     * portion of the groupId.
     *
     * For example, the Kuali Student top level pom has the groupId "org.kuali.student" and the artifactId "student". We
     * want to translate that groupId + artifactId into "site.kuali.org/student" NOT "site.kuali.org/student/student"
     *
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
     * Return the portion to the right of the hostname for the Maven web site
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
     * Return true if the Collection passed in is null or size zero, false otherwise
     */
    protected boolean isEmpty(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return a fully qualified url given a MavenProject, urlBase, and a SiteContext
     */
    protected String getSiteUrl(MavenProject project, String urlBase, SiteContext context) {
        String organizationGroupId = context.getOrganizationGroupId();
        String trimmedGroupId = getTrimmedGroupId(project, organizationGroupId);
        boolean appendArtifactId = isAppendArtifactId(project, trimmedGroupId);
        StringBuilder sb = new StringBuilder();
        sb.append(urlBase);
        if (!urlBase.endsWith("/")) {
            sb.append("/");
        }
        sb.append(getGroupIdPath(trimmedGroupId));
        if (trimmedGroupId.length() > 0) {
            sb.append("/");
        }
        if (appendArtifactId) {
            sb.append(project.getArtifactId());
            sb.append("/");
        }
        sb.append(project.getVersion());
        sb.append("/");
        return sb.toString();
    }

    /**
     * Return the fully qualified url where this MavenProject will be published.
     */
    public String getPublishUrl(MavenProject project, SiteContext context) {
        return getSiteUrl(project, context.getPublishBase(), context);
    }

    /**
     * Return the fully qualified url where this MavenProject can be publicly accessible.
     */
    public String getPublicUrl(MavenProject project, SiteContext context) {
        return getSiteUrl(project, context.getPublicBase(), context);
    }

    /**
     * Return the fully qualified URL for downloading a Kuali artifact from Kuali's Maven repo.
     */
    public String getDownloadUrl(MavenProject project, SiteContext context) {
        String baseUrl = context.getDownloadBase();
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        if (!baseUrl.endsWith("/")) {
            sb.append("/");
        }
        sb.append(getDownloadPath(project, context));
        sb.append("/");
        sb.append(getGroupIdPath(project.getGroupId()));
        sb.append("/");
        sb.append(project.getArtifactId());
        sb.append("/");
        sb.append(project.getVersion());
        sb.append("/");
        return sb.toString();
    }

    /**
     * Decide between "snapshot", "release", and "external"
     */
    protected String getDownloadPath(MavenProject project, SiteContext context) {
        // All snapshots (Kuali and non-Kuali) go into the "snapshot" folder
        if (isSnapshot(project.getVersion())) {
            return context.getDownloadSnapshotPath();
        }

        // If we get here we are dealing with a non-snapshot artifact

        // Would be good to positively identify something as a release artifact
        // instead of assuming "non-snapshot" == "release"

        if (isOrganizationProject(project, context.getOrganizationGroupId())) {
            // For Kuali projects, this is "release"
            return context.getDownloadReleasePath();
        } else {
            // For non-Kuali projects, this is "external"
            return context.getDownloadExternalPath();
        }
    }

    protected boolean isOrganizationProject(MavenProject project, String organizationGroupId) {
        return project.getGroupId().startsWith(organizationGroupId);
    }

    protected boolean isSnapshot(String version) {
        return version.toUpperCase().contains("SNAPSHOT");
    }

}
