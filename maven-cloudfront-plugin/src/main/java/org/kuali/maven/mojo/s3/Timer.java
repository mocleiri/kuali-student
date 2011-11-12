package org.kuali.maven.mojo.s3;

public class Timer {
    int count;
    long millis;

    public synchronized void addMillis(long millis) {
        count++;
        this.millis += millis;
    }

    public synchronized long getMillis() {
        return millis;
    }

    public synchronized int getCount() {
        return count;
    }

}
