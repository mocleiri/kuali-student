package org.kuali.common.threads;

import java.util.List;


public class ThreadHandlerFactory {

    public <T> ThreadHandler getThreadHandler(int threadCount, List<T> list, ElementHandler<T> elementHandler) {
        int elementCount = list.size();
        int actualThreadCount = threadCount > elementCount ? elementCount : threadCount;
        int elementsPerThread = getElementsPerThread(actualThreadCount, list.size());
        ThreadHandler handler = new ThreadHandler();
        handler.setThreadCount(threadCount);
        handler.setElementsPerThread(elementsPerThread);
        ProgressTracker tracker = new PercentCompleteTracker();
        tracker.setTotal(list.size());
        handler.setTracker(tracker);
        ThreadGroup group = new ThreadGroup("List Iterator Threads");
        group.setDaemon(true);
        handler.setGroup(group);
        Thread[] threads = getThreads(handler, list, elementHandler);
        handler.setThreads(threads);
        return handler;
    }

    protected int getElementsPerThread(int threads, int elements) {
        int requestsPerThread = elements / threads;
        while (requestsPerThread * threads < elements) {
            requestsPerThread++;
        }
        return requestsPerThread;
    }

    protected <T> Thread[] getThreads(ThreadHandler threadHandler, List<T> list, ElementHandler<T> elementHandler) {
        Thread[] threads = new Thread[threadHandler.getThreadCount()];
        for (int i = 0; i < threads.length; i++) {
            int offset = i * threadHandler.getElementsPerThread();
            int length = threadHandler.getElementsPerThread();
            if (offset + length > list.size()) {
                length = list.size() - offset;
            }
            ListIteratorContext<T> context = new ListIteratorContext<T>();
            context.setList(list);
            context.setTracker(threadHandler.getTracker());
            context.setOffset(offset);
            context.setLength(length);
            context.setThreadHandler(threadHandler);
            context.setElementHandler(elementHandler);
            int id = i + 1;
            context.setId(id);
            ListIteratorThread<T> thread = new ListIteratorThread<T>();
            thread.setContext(context);

            threads[i] = new Thread(threadHandler.getGroup(), thread, "ListIterator-" + id);
            threads[i].setUncaughtExceptionHandler(threadHandler);
            threads[i].setDaemon(true);
        }
        return threads;
    }

}
