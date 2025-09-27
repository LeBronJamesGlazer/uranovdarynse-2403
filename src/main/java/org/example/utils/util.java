package org.example.utils;

import org.example.metrics.Metrics;

import java.util.Random;

public class util {

    private static final Random rand = new Random();

    private util() {
        // private constructor to prevent instantiation
    }

    /**
     * Swap two elements in the array.
     */
    public static void swap(int[] array, int i, int j) {
        if (array == null) throw new IllegalArgumentException("Array cannot be null");
        if (i < 0 || j < 0 || i >= array.length || j >= array.length)
            throw new IndexOutOfBoundsException("Swap indices out of bounds");
        if (i == j) return; // nothing to swap
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Shuffle the array randomly (Fisher-Yates shuffle)
     */
    public static void shuffle(int[] array) {
        if (array == null) throw new IllegalArgumentException("Array cannot be null");
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swap(array, i, j);
        }
    }

    /**
     * Partition the array for QuickSort or similar algorithms.
     * Places elements <= pivot to left, > pivot to right.
     * Returns the final pivot index.
     */
    public static int partition(int[] array, int low, int high, Metrics metrics) {
        if (array == null) throw new IllegalArgumentException("Array cannot be null");
        if (low < 0 || high >= array.length || low > high)
            throw new IllegalArgumentException("Invalid low/high indices");

        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incCounter();
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    /**
     * Guard method to ensure array is not null or empty.
     */
    public static void checkArray(int[] array) {
        if (array == null) throw new IllegalArgumentException("Array cannot be null");
        if (array.length == 0) throw new IllegalArgumentException("Array cannot be empty");
    }
}