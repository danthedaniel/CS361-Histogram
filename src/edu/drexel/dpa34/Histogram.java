package edu.drexel.dpa34;

public interface Histogram {
    /**
     * Aggregates the raw Integers in the dataSet Array into an Array of numBuckets buckets.
     *
     * @param data The original data set
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return Array of size numBuckets containing frequencies
     */
    int[] generateHistogram(int[] data, int numBuckets);
}
