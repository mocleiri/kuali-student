package org.kuali.maven.common;

public class DefaultSiteContext implements SiteContext {

    @Override
    public String getOrganizationGroupId() {
        return "org.kuali";
    }

    @Override
    public String getDownloadBase() {
        return "http://s3browse.springsource.com/browse/maven.kuali.org";
    }

    @Override
    public String getDownloadSnapshotPath() {
        return "snapshot";
    }

    @Override
    public String getDownloadReleasePath() {
        return "release";
    }

    @Override
    public String getDownloadExternalPath() {
        return "external";
    }

    @Override
    public String getPublicBase() {
        return "http://site.kuali.org";
    }

    @Override
    public String getPublishBase() {
        return "s3://site.origin.kuali.org";
    }

}
