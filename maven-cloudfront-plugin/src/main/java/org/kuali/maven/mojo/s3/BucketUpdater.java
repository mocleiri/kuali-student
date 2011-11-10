package org.kuali.maven.mojo.s3;

import java.io.IOException;

public interface BucketUpdater {
    void updateDirectory(UpdateDirectoryContext context) throws IOException;
}
