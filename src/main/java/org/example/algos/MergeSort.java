package org.example.algos;

import org.example.metrics.Metrics;
import org.example.utils.util;

import java.util.Arrays;

public class MergeSort {

    private final Metrics metrics;
    private final int cutoff; // size to switch to insertion sort
    private int[] buffer;     // reuse buffer for merges

    public MergeSort(Metrics metrics, int cutoff) {
        this.metrics = metrics;
        this.cutoff = cutoff;
    }

    public void sort(int[] array) {
        util.checkArray(array); // guard clause
        buffer = new int[array.length];
        metrics.start();
        mergeSort(array, 0, array.length - 1, 1);
        metrics.stop();
    }

    private void mergeSort(int[] array, int left, int right, int depth) {
        metrics.updateDepth(depth);

        if (right - left + 1 <= cutoff) {
            insertionSort(array, left, right);
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(array, left, mid, depth + 1);
        mergeSort(array, mid + 1, right, depth + 1);
        merge(array, left, mid, right);
    }

    private void merge(int[] array, int left, int mid, int right) {
        System.arraycopy(array, left, buffer, left, right - left + 1);
        metrics.incAllocations();

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            metrics.incCounter();
            if (buffer[i] <= buffer[j]) array[k++] = buffer[i++];
            else array[k++] = buffer[j++];
        }
        while (i <= mid) array[k++] = buffer[i++];
        while (j <= right) array[k++] = buffer[j++];
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= left && array[j] > key) {
                metrics.incCounter();
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
            metrics.incAllocations();
        }
    }
}