package org.kuali.maven.mojo.s3.threads;

import static org.apache.commons.lang.StringUtils.leftPad;

import java.io.IOException;
import java.util.List;

import org.kuali.maven.mojo.s3.BucketUpdater;
import org.kuali.maven.mojo.s3.UpdateDirectoryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;

public class UpdateDirectoryThread implements Runnable {
    final Logger logger = LoggerFactory.getLogger(UpdateDirectoryThread.class);

    UpdateDirectoryThreadContext context;

    public UpdateDirectoryThread() {
        this(null);
    }

    public UpdateDirectoryThread(UpdateDirectoryThreadContext context) {
        super();
        this.context = context;
    }

    @Override
    public void run() {
        logger.debug("[Thread-" + context.getId() + "] Starting");
        int offset = context.getOffset();
        int length = context.getLength();
        List<UpdateDirectoryContext> list = context.getContexts();
        BucketUpdater updater = context.getUpdater();
        for (int i = offset; i < offset + length; i++) {
            if (context.getHandler().isStopThreads()) {
                break;
            }
            UpdateDirectoryContext udc = list.get(i);
            try {
                updater.updateDirectory(udc);
            } catch (IOException e) {
                throw new AmazonServiceException("Unexpected error", e);
            }
            logger.debug(leftPad(context.getId() + "", 2, " ") + " - " + udc.getCopyToKey());
            // context.getTracker().increment();
        }
        logger.debug("Thread " + context.getId() + " stopping");
    }

    public UpdateDirectoryThreadContext getContext() {
        return context;
    }

    public void setContext(UpdateDirectoryThreadContext context) {
        this.context = context;
    }

}
