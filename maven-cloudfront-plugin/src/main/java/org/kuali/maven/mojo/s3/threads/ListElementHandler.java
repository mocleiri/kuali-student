package org.kuali.maven.mojo.s3.threads;

public interface ListElementHandler<T> {
    void handleElement(T element);
}
