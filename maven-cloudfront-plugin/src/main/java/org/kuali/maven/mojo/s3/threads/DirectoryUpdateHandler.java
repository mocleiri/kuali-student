package org.kuali.maven.mojo.s3.threads;

import java.io.IOException;

import org.kuali.maven.mojo.s3.UpdateDirectoryContext;
import org.kuali.maven.mojo.s3.UpdateOriginBucketMojo;

import com.amazonaws.AmazonServiceException;

public class DirectoryUpdateHandler implements ListHandler<UpdateDirectoryContext> {

    UpdateOriginBucketMojo mojo;

    public DirectoryUpdateHandler() {
        this(null);
    }

    public DirectoryUpdateHandler(UpdateOriginBucketMojo mojo) {
        super();
        this.mojo = mojo;
    }

    @Override
    public void handle(UpdateDirectoryContext context) {
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
