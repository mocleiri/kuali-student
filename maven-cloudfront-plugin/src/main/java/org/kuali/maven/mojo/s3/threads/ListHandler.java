package org.kuali.maven.mojo.s3.threads;

public interface ListHandler<T> {
    void handle(T context);
}
