package org.kuali.maven.mojo.s3.threads;

import java.util.List;

import org.kuali.maven.mojo.s3.BucketUpdater;
import org.kuali.maven.mojo.s3.ProgressTracker;
import org.kuali.maven.mojo.s3.UpdateDirectoryContext;

public class UpdateDirectoryThreadContext {
    int id;
    List<UpdateDirectoryContext> contexts;
    int offset;
    int length;
    BucketUpdater updater;
    ThreadHandler handler;
    ProgressTracker tracker;

    public List<UpdateDirectoryContext> getContexts() {
        return contexts;
    }

    public void setContexts(List<UpdateDirectoryContext> contexts) {
        this.contexts = contexts;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public BucketUpdater getUpdater() {
        return updater;
    }

    public void setUpdater(BucketUpdater updater) {
        this.updater = updater;
    }

    public ThreadHandler getHandler() {
        return handler;
    }

    public void setHandler(ThreadHandler handler) {
        this.handler = handler;
    }

    public ProgressTracker getTracker() {
        return tracker;
    }

    public void setTracker(ProgressTracker tracker) {
        this.tracker = tracker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
