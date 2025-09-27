package org.example.algos;

import org.example.metrics.Metrics;
import org.example.utils.util;

public class DeterministicSelect {

    private final Metrics metrics;

    public DeterministicSelect(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Deterministic select (Median of Medians, groups of 5).
     * @param array input array (mutated)
     * @param k 0-based order statistic (0..n-1)
     * @return k-th smallest element
     */
    public int select(int[] array, int k) {
        util.checkArray(array);
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k out of bounds");
        }

        metrics.start();
        int result = selectRec(array, 0, array.length - 1, k, 1);
        metrics.stop();
        return result;
    }

    private int selectRec(int[] arr, int left, int right, int k, int depth) {
        metrics.updateDepth(depth);

        // base case: small range
        if (right - left + 1 <= 5) {
            insertionSortRange(arr, left, right);
            return arr[k];
        }

        // medians of groups of 5
        int numGroups = (int) Math.ceil((right - left + 1) / 5.0);
        int[] medians = new int[numGroups];
        metrics.incAllocations();

        int idx = 0;
        for (int i = left; i <= right; i += 5) {
            int groupRight = Math.min(i + 4, right);
            insertionSortRange(arr, i, groupRight);
            int medianIndex = i + (groupRight - i) / 2;
            medians[idx++] = arr[medianIndex];
        }

        // pivot = median of medians (recursive select)
        int pivot = selectRec(medians, 0, idx - 1, idx / 2, depth + 1);

        // partition
        int pivotPos = partition(arr, left, right, pivot);

        if (k == pivotPos) return arr[pivotPos];
        else if (k < pivotPos) return selectRec(arr, left, pivotPos - 1, k, depth + 1);
        else return selectRec(arr, pivotPos + 1, right, k, depth + 1);
    }

    private int partition(int[] arr, int left, int right, int pivotValue) {
        // find pivot index
        int pivotIndex = left;
        while (pivotIndex <= right && arr[pivotIndex] != pivotValue) pivotIndex++;
        if (pivotIndex > right) pivotIndex = right;

        util.swap(arr, pivotIndex, right);
        metrics.incAllocations();

        int store = left;
        for (int i = left; i < right; i++) {
            metrics.incCounter();
            if (arr[i] < pivotValue) {
                util.swap(arr, store, i);
                metrics.incAllocations();
                store++;
            }
        }
        util.swap(arr, store, right);
        metrics.incAllocations();
        return store;
    }

    private void insertionSortRange(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
                metrics.incCounter();
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    metrics.incAllocations();
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
    }
}