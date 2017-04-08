package edu.drexel.dpa34;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    // Maximum and minimum temperatures for our data set, Celsius
    private static int tempMin = -10;
    private static int tempMax = 35;

    /**
     * Threaded vs. Serial Histogram construction benchmark.
     *
     * Accepts the following command line arguments:
     *      -NUMTHREADS <number of threads>
     *      -NUMBINS    <number of histogram bins>
     *      -DATASIZE   <number of data elements>
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        // Retrieve command line arguments
        HashMap<String, Integer> argMap = parseArgs(args);
        int numThreads = argMap.getOrDefault("-NUMTHREADS", 4);
        int numBuckets = argMap.getOrDefault("-NUMBINS", 7);
        int dataSize   = argMap.getOrDefault("-DATASIZE", 100000000);

        int[] dataSet = generateDataSet(dataSize);

	    ParallelHistogram parallelHist = new ParallelHistogram(numThreads);
        benchmark("Parallel", parallelHist, dataSet, numBuckets);

        SerialHistogram serialHist = new SerialHistogram();
        benchmark("Serial", serialHist, dataSet, numBuckets);
    }

    /**
     * Parse arguments that are in the format "KEY1 <integer> KEY2 <integer> ...".
     *
     * @param args The command line argument Array
     * @return A mapping of argument names to argument values
     */
    private static HashMap<String, Integer> parseArgs(String[] args) {
        HashMap<String, Integer> argMap = new HashMap<>();

        try {
            String currentArg = args[0];

            for (String arg : args) {
                try {
                    argMap.put(currentArg, Integer.parseInt(arg));
                } catch (NumberFormatException e) {
                    currentArg = arg;
                }
            }
        } catch (IndexOutOfBoundsException e) {}

        return argMap;
    }

    /**
     * Time the execution of a Histogram implementation.
     *
     * @param name The display name of the tested Histogram implementation
     * @param histogram The Histogram implementation being tested
     * @param dataSet The input data to be aggregated
     * @param numBuckets The number of buckets in the output histogram
     */
    private static void benchmark(String name, Histogram histogram, int[] dataSet, int numBuckets) {
        long startTime = System.nanoTime();
        int[] results = histogram.generateHistogram(dataSet, numBuckets);
        long endTime = System.nanoTime();
        int total = 0;
        for (int val : results)
            total += val;

        System.out.print(name + " Results: ");
        printHistogram(results);
        System.out.println("Total: " + total);
        System.out.println(name + " ran in " + (endTime - startTime) / 1000000 + "ms");
    }

    /**
     * Creates an ArrayList of randomly selected Integers.
     *
     * @param dataSize The number of elements to generate
     * @return ArrayList of randomly selected (normally distributed) Integers of size dataSize
     */
    private static int[] generateDataSet(int dataSize) {
        int[] dataSet = new int[dataSize];

        for (int i = 0; i < dataSize; i++)
            dataSet[i] = randInt();

        return dataSet;
    }

    /**
     * Pseudo-randomly select and return an Integer between this.tempMin and this.tempMax.
     *
     * @return The randomly selected number
     */
    private static Integer randInt() {
        return ThreadLocalRandom.current().nextInt(tempMin, tempMax);
    }

    /**
     * Pretty-print a histogram.
     *
     * @param histogram The histogram to be printed
     */
    private static void printHistogram(int[] histogram) {
        System.out.print("[");
        for (int i = 0; i < histogram.length; i++) {
            System.out.print("(" + i + ": " + histogram[i] + ")");

            // All elements except the last should be followed by a comma
            if (i < histogram.length - 1) {
                System.out.print(", ");
            } else {
                System.out.println("]");
            }
        }
    }
}
