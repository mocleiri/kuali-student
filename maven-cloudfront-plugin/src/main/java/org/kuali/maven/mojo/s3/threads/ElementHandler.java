package org.kuali.maven.mojo.s3.threads;

public interface ElementHandler<T> {
    void handleElement(ListIteratorContext<T> context, int index, T element);
}
