package edu.drexel.dpa34;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelHistogram implements Histogram {
    public ArrayList<Integer> dataSet;
    public ArrayList<Integer> histogram = new ArrayList();
    public ReentrantLock lock = new ReentrantLock();
    private int numThreads;
    public int dataMin;
    public int dataMax;

    /**
     * Generates Histograms with the help of parallel processing.
     *
     * @param data ArrayList of Integers that will become a Histogram
     * @param numThreads The number of threads used in the aggregation process.
     */
    public ParallelHistogram(ArrayList<Integer> data, int numThreads) {
        this.dataSet    = data;
        this.numThreads = numThreads;
        this.dataMin    = dataSetMin();
        this.dataMax    = dataSetMax();
    }

    /**
     * Aggregates the raw Integers in the dataSet ArrayList into an ArrayList
     * of numBuckets buckets. The map-reduce functionality is performed across
     * numThreads threads.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return ArrayList of size numBuckets containing Integers representing frequencies
     */
    public ArrayList<Integer> generateHistogram(int numBuckets) {
        // Initialize the "main-thread" histogram
        IntStream.range(0, numBuckets).forEach(i -> this.histogram.add(0));

        ArrayList<Thread> threads = new ArrayList();
        int saneThreadCount = Math.min(this.numThreads, this.dataSet.size());
        int workloadSize = this.dataSet.size() / numBuckets; // The number of elements each thread will work on
        int startIndex = 0;

        // Spawn numThreads threads and add them to the threads ArrayList
        for (int i = 0; i < saneThreadCount; i++) {
            int endIndex = Math.min(startIndex + workloadSize, this.dataSet.size());

            // Make sure that the last thread gets all remaining data
            if (i == saneThreadCount - 1) {
                endIndex = this.dataSet.size();
            }

            Thread t = new Thread(new HistogramThread(this, startIndex, endIndex, numBuckets));
            t.start();
            threads.add(t);
        }

        // Wait for each thread to finish
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted");
            }
        });

        return this.histogram;
    }

    /**
     * Find the minimum value of this.dataSet
     *
     * @return The minimum value
     */
    private int dataSetMin() {
        int min = Integer.MAX_VALUE;

        for (int value : this.dataSet) {
           if (value < min) {
               min = value;
           }
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
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
