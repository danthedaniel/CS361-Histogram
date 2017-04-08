package edu.drexel.dpa34;

import java.util.concurrent.locks.ReentrantLock;

public class ParallelHistogram implements Histogram {
    int[] dataSet;
    int[] histogram;
    ReentrantLock lock = new ReentrantLock();
    int numBuckets;
    int dataMin;
    int dataMax;
    private int numThreads;

    /**
     * Generates Histograms with the help of parallel processing.
     *
     * @param numThreads The number of threads used in the aggregation process.
     */
    ParallelHistogram(int numThreads) {
        this.numThreads = numThreads;
    }

    /**
     * Aggregates the raw Integers in the dataSet Array into an Array
     * of numBuckets buckets. The map-reduce functionality is performed across
     * numThreads threads.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return Array of size numBuckets containing Integers representing frequencies
     */
    public int[] generateHistogram(int[] data, int numBuckets) {
        // Calculate the min and max of the data set
        this.dataSet    = data;
        this.dataMin    = dataSetMin();
        this.dataMax    = dataSetMax();
        this.numBuckets = numBuckets;
        this.histogram  = new int[numBuckets];

        // Initialize the "main-thread" histogram
        for (int i = 0; i < numBuckets; i++)
            this.histogram[i] = 0;

        Thread[] threads = spawnThreads();
        awaitThreads(threads);

        return this.histogram;
    }

    /**
     * Delegate work to numThreads threads.
     *
     * @return The working threads
     */
    private Thread[] spawnThreads() {
        int saneThreadCount = Math.min(this.numThreads, this.dataSet.length);
        Thread[] threads = new Thread[saneThreadCount];
        int workloadSize = this.dataSet.length / saneThreadCount; // The number of elements each thread will work on
        int startIndex = 0;

        // Spawn numThreads threads and add them to the threads ArrayList
        for (int i = 0; i < saneThreadCount; i++) {
            int endIndex = Math.min(startIndex + workloadSize, this.dataSet.length);

            // Make sure that the last thread gets all remaining data
            if (i == saneThreadCount - 1)
                endIndex = this.dataSet.length;

            Thread t = new Thread(new HistogramThread(this, startIndex, endIndex));
            t.start();
            threads[i] = t;
            startIndex = endIndex;
        }

        return threads;
    }

    /**
     * Join on all Threads in an ArrayList of Threads
     *
     * @param threads List of threads currently operating on the data set
     */
    private void awaitThreads(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted");
            }
        }
    }

    /**
     * Find the minimum value of this.dataSet
     *
     * @return The minimum value
     */
    private int dataSetMin() {
        int min = Integer.MAX_VALUE;

        for (int value : this.dataSet) {
           if (value < min)
               min = value;
        }

        return min;
    }

    /**
     * Find the maximum value of this.dataSet
     *
     * @return The maximum value
     */
    private int dataSetMax() {
        int max = Integer.MIN_VALUE;

        for (int value : this.dataSet) {
            if (value > max)
                max = value;
        }

        return max;
    }
}
