package edu.drexel.dpa34;

import java.util.concurrent.locks.ReentrantLock;

public class HistogramThread implements Runnable {
    private final int startIndex;
    private final int numBuckets;
    private final int dataMin;
    private final int dataMax;
    private final int endIndex;
    private final int[] dataSet;
    private int[] histogram;
    private ReentrantLock lock;

    /**
     * A single worker thread for performing the mapping function of the
     * ParallelHistogram class.
     *
     * @param parent The invoking object
     * @param startIndex The worker's designated starting point
     * @param endIndex The worker's designated ending point
     */
    HistogramThread(ParallelHistogram parent, int startIndex, int endIndex) {
        this.dataSet    = parent.dataSet;
        this.dataMin    = parent.dataMin;
        this.dataMax    = parent.dataMax;
        this.lock       = parent.lock;
        this.histogram  = parent.histogram;
        this.numBuckets = parent.numBuckets;
        this.startIndex = startIndex;
        this.endIndex   = endIndex;
    }

    /**
     * This method is invoked when the object is started as a thread
     */
    public void run() {
        addToMainThread(aggregateData());
    }

    /**
     * Create a new Array for aggregating the values assigned to the thread.
     *
     * @return The current thread's results
     */
    private int[] aggregateData() {
        // Initialize the thread's histogram
        int[] workerHistogram = new int[this.numBuckets];
        for (int i = 0; i < numBuckets; i++)
            workerHistogram[i] = 0;

        double bucketWidth = (this.dataMax - this.dataMin) / (double) this.numBuckets;

        // Aggregate the data
        for (int i = this.startIndex; i < this.endIndex; i++) {
            Integer val = this.dataSet[i];
            int index = Math.min(indexForValue(val, bucketWidth), this.numBuckets - 1);
            workerHistogram[index]++;
        }

        return workerHistogram;
    }

    /**
     * Add the worker totals to the main thread's totals while under a lock.
     *
     * @param workerHistogram The current thread's results
     */
    private void addToMainThread(int[] workerHistogram) {
        this.lock.lock();
        try {
            for (int i = 0; i < this.numBuckets; i++)
                this.histogram[i] += workerHistogram[i];
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Calculate where an element belongs in the histogram.
     *
     * @param value An element from this.dataSet
     * @param bucketWidth The width of each bucket
     * @return The appropriate index in the histogram
     */
    private int indexForValue(int value, double bucketWidth) {
        return (int) Math.floor((value - this.dataMin) / bucketWidth);
    }
}
