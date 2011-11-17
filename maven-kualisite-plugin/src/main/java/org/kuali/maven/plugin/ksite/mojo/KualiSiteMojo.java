package org.kuali.maven.plugin.ksite.mojo;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Site;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.kuali.maven.common.SiteContext;
import org.kuali.maven.common.UrlBuilder;

/**
 * This plugin organizes/standardizes the maven site publication process for the Kuali organization
 *
 * @goal updatesiteproperties
 * @phase pre-site
 */
public class KualiSiteMojo extends AbstractMojo implements SiteContext {

    /**
     * The prefix into the bucket when downloading a snapshot version
     *
     * @parameter expression="${downloadSnapshotPrefix}" default-value="snapshot"
     */
    private String downloadSnapshotPrefix;

    /**
     * The prefix into the bucket when downloading a release version
     *
     * @parameter expression="${downloadReleasePrefix}" default-value="release"
     */
    private String downloadReleasePrefix;

    /**
     * The protocol used when publishing the web site
     *
     * @parameter expression="${publishUrlProtocol}" default-value="s3"
     */
    private String publishUrlProtocol;

    /**
     * The protocol for the public facing website
     *
     * @parameter expression="${publicUrlProtocol}" default-value="http"
     */
    private String publicUrlProtocol;

    /**
     * The prefix for the location that artifacts can be downloaded from
     *
     * @parameter expression="${downloadPrefix}"
     *            default-value="http://s3browse.springsource.com/browse/maven.kuali.org/"
     */
    private String downloadPrefix;

    /**
     * The groupId for the organization
     *
     * @parameter expression="${organizationGroupId}" default-value="org.kuali"
     */
    private String organizationGroupId;

    /**
     * The name of the AWS bucket the site gets published to
     *
     * @parameter expression="${bucket}" default-value="site.origin.kuali.org"
     * @required
     */
    private String bucket;

    /**
     * The public DNS name for the site
     *
     * @parameter expression="${hostname}" default-value="site.kuali.org"
     * @required
     */
    private String hostname;

    /**
     * The Maven project this plugin runs in.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        UrlBuilder builder = new UrlBuilder();

        // Generate our urls
        String publicUrl = builder.getPublicUrl(getProject(), this);
        String publishUrl = builder.getPublishUrl(getProject(), this);
        String downloadUrl = builder.getDownloadUrl(getProject(), this);

        // Get a reference to the relevant model objects
        MavenProject project = getProject();
        DistributionManagement dm = project.getDistributionManagement();
        Site site = dm.getSite();

        // Update the model with our generated urls as needed
        handlePublicUrl(publicUrl, project);
        handlePublishUrl(publishUrl, site);
        handleDownloadUrl(downloadUrl, dm);

    }

    protected boolean containsUnresolvedProperty(String s) {
        return s.contains("${");
    }

    /**
     * Return true if "s" is empty or is an unresolved property
     */
    protected boolean isUnresolved(String s) {
        if (StringUtils.isEmpty(s)) {
            return true;
        } else {
            return containsUnresolvedProperty(s);
        }
    }

    protected void warn(String pomString, String calculatedString, String propertyDescription) {
        getLog().warn("****************************************");
        getLog().warn(propertyDescription + " mismatch");
        getLog().warn("  Supplied Value: " + pomString);
        getLog().warn("Calculated value: " + calculatedString);
        getLog().warn("****************************************");
    }

    /**
     * Return true if the 2 urls are exactly the same or if the only thing different about them is a trailing slash
     */
    protected boolean isUrlMatch(String url1, String url2) {
        if (url1.equals(url2)) {
            return true;
        }
        if ((url1 + "/").equals(url2)) {
            return true;
        }
        if (url1.equals(url2 + "/")) {
            return true;
        }
        return false;
    }

    protected void handleDownloadUrl(String downloadUrl, DistributionManagement dm) {
        if (isUnresolved(dm.getDownloadUrl())) {
            getLog().info("Setting download url to " + downloadUrl);
            dm.setDownloadUrl(downloadUrl);
            return;
        }
        if (!isUrlMatch(downloadUrl, dm.getDownloadUrl())) {
            warn(dm.getDownloadUrl(), downloadUrl, "Download url");
        }
        getLog().info("Using download url from the POM " + dm.getDownloadUrl());
    }

    protected void handlePublishUrl(String publishUrl, Site site) {
        if (isUnresolved(site.getUrl())) {
            getLog().info("Setting site publication url to " + publishUrl);
            site.setUrl(publishUrl);
            return;
        }
        if (!isUrlMatch(publishUrl, site.getUrl())) {
            warn(site.getUrl(), publishUrl, "Site publication url");
        }
        getLog().info("Using site publication url - " + site.getUrl());
    }

    protected void handlePublicUrl(String publicUrl, MavenProject project) {
        if (isUnresolved(project.getUrl())) {
            getLog().info("Setting public url to " + publicUrl);
            project.setUrl(publicUrl);
            return;
        }
        if (!isUrlMatch(publicUrl, project.getUrl())) {
            warn(project.getUrl(), publicUrl, "Public url");
        }
        getLog().info("Using public url from the POM " + project.getUrl());
    }

    /**
     * @return the project
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * @param project
     *            the project to set
     */
    public void setProject(final MavenProject project) {
        this.project = project;
    }

    /**
     * @return the bucket
     */
    @Override
    public String getBucket() {
        return bucket;
    }

    /**
     * @param bucket
     *            the bucket to set
     */
    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    /**
     * @return the hostname
     */
    @Override
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname
     *            the hostname to set
     */
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the downloadPrefix
     */
    @Override
    public String getDownloadPrefix() {
        return downloadPrefix;
    }

    /**
     * @param downloadPrefix
     *            the downloadPrefix to set
     */
    public void setDownloadPrefix(final String downloadPrefix) {
        this.downloadPrefix = downloadPrefix;
    }

    /**
     * @return the parentGroupId
     */
    @Override
    public String getOrganizationGroupId() {
        return organizationGroupId;
    }

    /**
     * @param parentGroupId
     *            the parentGroupId to set
     */
    public void setOrganizationGroupId(final String parentGroupId) {
        this.organizationGroupId = parentGroupId;
    }

    /**
     * @return the publishUrlProtocol
     */
    @Override
    public String getPublishUrlProtocol() {
        return publishUrlProtocol;
    }

    /**
     * @param publishUrlProtocol
     *            the publishUrlProtocol to set
     */
    public void setPublishUrlProtocol(final String publishUrlProtocol) {
        this.publishUrlProtocol = publishUrlProtocol;
    }

    /**
     * @return the publicUrlProtocol
     */
    @Override
    public String getPublicUrlProtocol() {
        return publicUrlProtocol;
    }

    /**
     * @param publicUrlProtocol
     *            the publicUrlProtocol to set
     */
    public void setPublicUrlProtocol(final String publicUrlProtocol) {
        this.publicUrlProtocol = publicUrlProtocol;
    }

    /**
     * @return the downloadSnapshotPrefix
     */
    @Override
    public String getDownloadSnapshotPrefix() {
        return downloadSnapshotPrefix;
    }

    /**
     * @param downloadSnapshotPrefix
     *            the downloadSnapshotPrefix to set
     */
    public void setDownloadSnapshotPrefix(final String downloadSnapshotPrefix) {
        this.downloadSnapshotPrefix = downloadSnapshotPrefix;
    }

    /**
     * @return the downloadReleasePrefix
     */
    @Override
    public String getDownloadReleasePrefix() {
        return downloadReleasePrefix;
    }

    /**
     * @param downloadReleasePrefix
     *            the downloadReleasePrefix to set
     */
    public void setDownloadReleasePrefix(final String downloadReleasePrefix) {
        this.downloadReleasePrefix = downloadReleasePrefix;
    }

}
