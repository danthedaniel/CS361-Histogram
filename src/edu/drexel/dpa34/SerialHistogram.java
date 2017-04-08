package edu.drexel.dpa34;

import java.util.ArrayList;

public class SerialHistogram implements Histogram {
    private ArrayList<Integer> dataSet;

    /**
     * Generates Histograms without any parallelization.
     *
     * @param data ArrayList of Integers that will become a Histogram
     */
    public SerialHistogram(ArrayList<Integer> data) {
        this.dataSet = data;
    }

    /**
     * Aggregates the raw Integers in the dataSet ArrayList into an ArrayList
     * of numBuckets buckets.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return ArrayList of size numBuckets containing Integers representing frequencies
     */
    public ArrayList<Integer> generateHistogram(int numBuckets) {
        ArrayList<Integer> histogram = new ArrayList();
        return histogram;
    }
}
