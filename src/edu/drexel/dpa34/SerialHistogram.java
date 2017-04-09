package edu.drexel.dpa34;

public class SerialHistogram implements Histogram {
    /**
     * Generates Histograms without any multi-threading.
     */
    SerialHistogram() {}

    /**
     * Aggregates the raw Integers in the dataSet Array into an Array
     * of numBuckets buckets.
     *
     * @param numBuckets The number of buckets (bars) in the Histogram
     * @return Array of size numBuckets containing Integers representing frequencies
     */
    public int[] generateHistogram(int[] data, int numBuckets) {
        int dataMin = dataSetMin(data);
        int dataMax = dataSetMax(data);
        int[] histogram = new int[numBuckets];

        for (int i = 0; i < numBuckets; i++)
            histogram[i] = 0;

        double bucketWidth = (dataMax - dataMin) / (double) numBuckets;

        // Aggregate the data
        for (int val : data) {
            int index = Math.min(indexForValue(val, bucketWidth, dataMin), numBuckets - 1);
            histogram[index] += 1;
        }

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
    private int dataSetMin(int[] dataSet) {
        int min = Integer.MAX_VALUE;

        for (int value : dataSet) {
            if (value < min)
                min = value;
        }

        return min;
    }

    /**
     * Find the maximum value of dataSet
     *
     * @return The maximum value
     */
    private int dataSetMax(int[] dataSet) {
        int max = Integer.MIN_VALUE;

        for (int value : dataSet) {
            if (value > max)
                max = value;
        }

        return max;
    }
}
