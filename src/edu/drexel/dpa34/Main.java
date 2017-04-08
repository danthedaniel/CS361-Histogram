package edu.drexel.dpa34;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Main {
    // Maximum and minimum temperatures for our data set, Celcius
    private static int tempMin = -10;
    private static int tempMax = 35;

    public static void main(String[] args) {
        int numBuckets = 10;
        ArrayList<Integer> dataSet = generateDataSet(700);
	    SerialHistogram serialHist = new SerialHistogram(dataSet);
	    ParallelHistogram parallelHist = new ParallelHistogram(dataSet, 4);

	    ArrayList<Integer> serialResults = serialHist.generateHistogram(numBuckets);
        ArrayList<Integer> parallelResults = parallelHist.generateHistogram(numBuckets);

        System.out.print("Serial Results: ");
        printHistogram(serialResults);
        System.out.print("Parallel Results: ");
        printHistogram(parallelResults);
    }

    /**
     * Creates an ArrayList of randomly selected Integers.
     *
     * @param dataSize The number of elements to generate
     * @return ArrayList of randomly selected (normally distributed) Integers of size dataSize
     */
    private static ArrayList<Integer> generateDataSet(int dataSize) {
        ArrayList<Integer> dataSet = new ArrayList();
        IntStream.range(0, dataSize).forEach(i -> dataSet.add(randInt()));
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
    private static void printHistogram(ArrayList<Integer> histogram) {
        System.out.print("[");
        for (int i = 0; i < histogram.size(); i++) {
            System.out.print(histogram.get(i).toString());

            // All elements except the last should be followed by a comma
            if (i < histogram.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println("]");
            }
        }
    }
}
