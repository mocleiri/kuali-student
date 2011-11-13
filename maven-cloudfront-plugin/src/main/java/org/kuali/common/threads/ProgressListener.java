package org.kuali.common.threads;

public interface ProgressListener {
    void started(ProgressEvent event);

    void progress(ProgressEvent event);

    void completed(ProgressEvent event);

}
