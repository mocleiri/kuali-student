package org.kuali.maven.mojo.s3.threads;

import org.kuali.maven.mojo.s3.UpdateDirectoryContext;
import org.kuali.maven.mojo.s3.UpdateOriginBucketMojo;

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
    public void handleElement(ListIteratorContext<UpdateDirectoryContext> context, int index,
            UpdateDirectoryContext element) {
    }

    public UpdateOriginBucketMojo getMojo() {
        return mojo;
    }

    public void setMojo(UpdateOriginBucketMojo mojo) {
        this.mojo = mojo;
    }
}
