package edu.drexel.dpa34;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class SerialHistogram implements Histogram {
    /**
     * Generates Histograms without any parallelization.
     */
    SerialHistogram() {}

    /**
     * Aggregates the raw Integers in the dataSet ArrayList into an ArrayList
     * of numBuckets buckets.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return ArrayList of size numBuckets containing Integers representing frequencies
     */
    public ArrayList<Integer> generateHistogram(ArrayList<Integer> data, int numBuckets) {
        int dataMin = dataSetMin(data);
        int dataMax = dataSetMax(data);

        ArrayList<Integer> histogram = new ArrayList<>();
        IntStream.range(0, numBuckets).forEach(i -> histogram.add(0));

        double bucketWidth = (dataMax - dataMin) / (double) numBuckets;

        // Aggregate the data
        data.forEach(val -> {
            int index = Math.min(indexForValue(val, bucketWidth, dataMin), numBuckets - 1);
            histogram.set(index, histogram.get(index) + 1);
        });

        return histogram;
    }

    /**
     * Calculate where an element belongs in the histogram.
     *
     * @param value An element from the data set
     * @param bucketWidth The width of each bucket
     * @param dataMin The minimum value in the data set
     * @return The appropriate index in the histogram
     */
    private int indexForValue(int value, double bucketWidth, int dataMin) {
        return (int) Math.floor((value - dataMin) / bucketWidth);
    }

    /**
     * Find the minimum value of dataSet
     *
     * @return The minimum value
     */
    private int dataSetMin(ArrayList<Integer> dataSet) {
        int min = Integer.MAX_VALUE;

        for (int value : dataSet) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }

    /**
     * Find the maximum value of dataSet
     *
     * @return The maximum value
     */
    private int dataSetMax(ArrayList<Integer> dataSet) {
        int max = Integer.MIN_VALUE;

        for (int value : dataSet) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
