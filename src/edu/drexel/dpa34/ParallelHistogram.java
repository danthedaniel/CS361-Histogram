package edu.drexel.dpa34;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParallelHistogram implements Histogram {
    private ArrayList<Integer> dataSet;
    private int numThreads;

    private class HistogramThread implements Runnable {
        public HistogramThread() {

        }

        public void run() {

        }
    }

    /**
     * Generates Histograms with the help of parallel processing.
     *
     * @param data ArrayList of Integers that will become a Histogram
     * @param numThreads The number of threads used in the aggregation process.
     */
    public ParallelHistogram(ArrayList<Integer> data, int numThreads) {
        this.dataSet = data;
        this.numThreads = numThreads;
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
        ArrayList<Integer> histogram = new ArrayList();
        ArrayList<Thread> threads = new ArrayList();

        IntStream.range(0, this.numThreads).forEach(i -> {
            Thread thread = new Thread(new HistogramThread());
            thread.start();
            threads.add(thread);
        });
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
            }
        });

        return histogram;
    }
}
