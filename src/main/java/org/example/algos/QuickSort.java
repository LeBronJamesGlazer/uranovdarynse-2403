package org.example.algos;

import org.example.metrics.Metrics;

import java.util.Random;

public class QuickSort {

    private final Metrics metrics;
    private final Random rand = new Random();

    public QuickSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;
        metrics.start();
        quickSort(array, 0, array.length - 1, 1);
        metrics.stop();
    }

    private void quickSort(int[] array, int low, int high, int depth) {
        metrics.updateDepth(depth);

        if (low >= high) return;

        // Randomized pivot
        int pivotIndex = low + rand.nextInt(high - low + 1);
        swap(array, pivotIndex, high);
        metrics.incAllocations();

        int pivotFinal = partition(array, low, high);

        // Smaller-first recursion
        if (pivotFinal - low < high - pivotFinal) {
            quickSort(array, low, pivotFinal - 1, depth + 1);
            quickSort(array, pivotFinal + 1, high, depth + 1);
        } else {
            quickSort(array, pivotFinal + 1, high, depth + 1);
            quickSort(array, low, pivotFinal - 1, depth + 1);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incCounter(); // counting comparisons
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        if (i == j) return;
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
        metrics.incAllocations();
    }
}