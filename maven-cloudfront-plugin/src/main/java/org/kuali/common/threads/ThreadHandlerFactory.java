package org.kuali.common.threads;

import java.util.List;

public class ThreadHandlerFactory {

    public <T> ThreadHandler<T> getThreadHandler(ThreadHandlerContext<T> context) {
        List<T> list = context.getList();
        int threadCount = context.getMax();
        int elementCount = list.size();
        int actualThreadCount = threadCount > elementCount ? elementCount : threadCount;
        int elementsPerThread = getElementsPerThread(actualThreadCount, list.size());
        ThreadHandler<T> handler = new ThreadHandler<T>();
        handler.setThreadCount(threadCount);
        handler.setElementsPerThread(elementsPerThread);
        ProgressNotifier<T> notifier = new ProgressNotifier<T>();
        notifier.setListener(context.getListener());
        notifier.setTotal(list.size());
        handler.setNotifier(notifier);
        ThreadGroup group = new ThreadGroup("List Iterator Threads");
        group.setDaemon(true);
        handler.setGroup(group);
        Thread[] threads = getThreads(handler, list, context.getHandler());
        handler.setThreads(threads);
        return handler;
    }

    protected int getElementsPerThread(int threads, int elements) {
        int elementsPerThread = elements / threads;
        while (elementsPerThread * threads < elements) {
            elementsPerThread++;
        }
        return elementsPerThread;
    }

    protected <T> Thread[] getThreads(ThreadHandler<T> threadHandler, List<T> list, ElementHandler<T> elementHandler) {
        Thread[] threads = new Thread[threadHandler.getThreadCount()];
        for (int i = 0; i < threads.length; i++) {
            int offset = i * threadHandler.getElementsPerThread();
            int length = threadHandler.getElementsPerThread();
            if (offset + length > list.size()) {
                length = list.size() - offset;
            }
            ListIteratorContext<T> context = new ListIteratorContext<T>();
            context.setList(list);
            context.setTracker(threadHandler.getNotifier());
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
