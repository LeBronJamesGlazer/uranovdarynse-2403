package org.example.algos;

import org.example.metrics.Metrics;
import org.example.utils.util;

import java.util.Random;

public class QuickSort {

    private final Metrics metrics;
    private final Random rand = new Random();

    public QuickSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public void sort(int[] array) {
        util.checkArray(array); // guard clause
        metrics.start();
        quickSort(array, 0, array.length - 1, 1);
        metrics.stop();
    }

    private void quickSort(int[] array, int low, int high, int depth) {
        metrics.updateDepth(depth);

        if (low >= high) return;

        // Randomized pivot
        int pivotIndex = low + rand.nextInt(high - low + 1);
        util.swap(array, pivotIndex, high);
        metrics.incAllocations();

        int pivotFinal = util.partition(array, low, high, metrics);

        // Smaller-first recursion
        if (pivotFinal - low < high - pivotFinal) {
            quickSort(array, low, pivotFinal - 1, depth + 1);
            quickSort(array, pivotFinal + 1, high, depth + 1);
        } else {
            quickSort(array, pivotFinal + 1, high, depth + 1);
            quickSort(array, low, pivotFinal - 1, depth + 1);
        }
    }
}