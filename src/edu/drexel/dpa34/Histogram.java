package edu.drexel.dpa34;

import java.util.ArrayList;

public interface Histogram {
    /**
     * Aggregates the raw Integers in the dataSet ArrayList into an ArrayList of numBuckets
     * buckets.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return ArrayList of size numBuckets containing Integers representing frequencies
     */
    ArrayList<Integer> generateHistogram(int numBuckets);
}
