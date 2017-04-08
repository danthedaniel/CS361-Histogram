package edu.drexel.dpa34;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.concurrent.locks.ReentrantLock;

public class HistogramThread implements Runnable {
    private final int startIndex;
    private final int numBuckets;
    private final int dataMin;
    private final int dataMax;
    private final int endIndex;
    private final ArrayList<Integer> dataSet;
    private ArrayList<Integer> histogram;
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
     * Create a new ArrayList for aggregating the values assigned to the thread.
     *
     * @return The current thread's results
     */
    private ArrayList<Integer> aggregateData() {
        // Initialize the thread's histogram
        ArrayList<Integer> workerHistogram = new ArrayList<>();
        IntStream.range(0, this.numBuckets).forEach(i -> workerHistogram.add(0));

        double bucketWidth = (this.dataMax - this.dataMin) / (double) this.numBuckets;

        // Aggregate the data
        this.dataSet.subList(this.startIndex, this.endIndex).forEach(val -> {
            int index = Math.min(indexForValue(val, bucketWidth), this.numBuckets - 1);
            workerHistogram.set(index, workerHistogram.get(index) + 1);
        });

        return workerHistogram;
    }

    /**
     * Add the worker totals to the main thread's totals while under a lock.
     *
     * @param workerHistogram The current thread's results
     */
    private void addToMainThread(ArrayList<Integer> workerHistogram) {
        this.lock.lock();
        try {
            IntStream.range(0, this.numBuckets).forEach(i -> {
                int newValue = this.histogram.get(i) + workerHistogram.get(i);
                this.histogram.set(i, newValue);
            });
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
