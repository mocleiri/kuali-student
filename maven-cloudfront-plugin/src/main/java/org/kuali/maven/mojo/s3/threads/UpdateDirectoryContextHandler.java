package org.kuali.maven.mojo.s3.threads;

import java.io.IOException;

import org.kuali.maven.mojo.s3.UpdateDirectoryContext;
import org.kuali.maven.mojo.s3.UpdateOriginBucketMojo;

import com.amazonaws.AmazonServiceException;

public class UpdateDirectoryContextHandler implements ElementHandler<UpdateDirectoryContext> {

    UpdateOriginBucketMojo mojo;

    public UpdateDirectoryContextHandler() {
        this(null);
    }

    public UpdateDirectoryContextHandler(UpdateOriginBucketMojo mojo) {
        super();
        this.mojo = mojo;
    }

    @Override
    public void handleElement(UpdateDirectoryContext context) {
        try {
            mojo.updateDirectory(context);
        } catch (IOException e) {
            throw new AmazonServiceException("Unexpected error", e);
        }
    }

    public UpdateOriginBucketMojo getMojo() {
        return mojo;
    }

    public void setMojo(UpdateOriginBucketMojo mojo) {
        this.mojo = mojo;
    }
}
