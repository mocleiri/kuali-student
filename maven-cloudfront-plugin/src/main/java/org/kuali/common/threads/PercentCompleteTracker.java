package org.kuali.common.threads;

/**
 * Print a dot whenever there is progress of 2% or more
 */
public class PercentCompleteTracker extends ProgressNotifier {
    int percentageIncrement = 2;
    int percentCompletePrevious;

    @Override
    protected void showProgress(int count, int total) {
        int percentComplete = (count * 100) / total;
        if (enoughProgress(percentComplete)) {
            percentCompletePrevious = percentComplete;
            out.print(progressToken);
        }
    }

    protected boolean enoughProgress(int percentComplete) {
        int needed = percentCompletePrevious + percentageIncrement;
        return percentComplete >= needed;
    }

    public int getPercentageIncrement() {
        return percentageIncrement;
    }

    public void setPercentageIncrement(int percentageIncrement) {
        this.percentageIncrement = percentageIncrement;
    }

}
