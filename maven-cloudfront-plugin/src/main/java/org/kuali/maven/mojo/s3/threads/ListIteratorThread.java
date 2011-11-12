package org.kuali.maven.mojo.s3.threads;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListIteratorThread<T> implements Runnable {
    final Logger logger = LoggerFactory.getLogger(ListIteratorThread.class);

    ListIteratorContext<T> context;

    @Override
    public void run() {
        logger.debug("Starting - " + context.getId());
        int offset = context.getOffset();
        int length = context.getLength();
        List<T> list = context.getList();
        ListElementHandler<T> handler = context.getListHandler();
        for (int i = offset; i < offset + length; i++) {
            if (context.getThreadHandler().isStopThreads()) {
                break;
            }
            T element = list.get(i);
            handler.handleElement(element);
            context.getTracker().increment();
        }
        logger.debug("Stopping - " + context.getId());
    }

    public ListIteratorContext<T> getContext() {
        return context;
    }

    public void setContext(ListIteratorContext<T> context) {
        this.context = context;
    }

}
