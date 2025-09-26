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
     * @param array input array (will be mutated)
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

    /**
     * Recursive MoM select: selects k-th (0-based) element in array[left..right]
     */
    private int selectRec(int[] array, int left, int right, int k, int depth) {
        metrics.updateDepth(depth);

        // If region small, sort and return
        if (right - left + 1 <= 5) {
            insertionSortRange(array, left, right);
            return array[left + k - left]; // k is absolute index
        }

        // Step 1: compute medians of groups of 5 and store them at the start of the range
        int numGroups = (int) Math.ceil((right - left + 1) / 5.0);
        int[] medians = new int[numGroups];
        metrics.incAllocations(); // small allocation for medians array

        int idx = 0;
        for (int i = left; i <= right; i += 5) {
            int groupRight = Math.min(i + 4, right);
            insertionSortRange(array, i, groupRight);
            int medianIndex = i + (groupRight - i) / 2;
            medians[idx++] = array[medianIndex];
        }

        // Step 2: find median of medians recursively (this is the pivot)
        int medianOfMedians = selectMedianOfArray(medians);

        // Step 3: partition around pivot value and get its final position
        int pivotPos = partitionAroundPivot(array, left, right, medianOfMedians);

        // number of elements in left partition
        int leftCount = pivotPos - left;

        if (k == pivotPos) {
            return array[pivotPos];
        } else if (k < pivotPos) {
            return selectRec(array, left, pivotPos - 1, k, depth + 1);
        } else {
            return selectRec(array, pivotPos + 1, right, k, depth + 1);
        }
    }

    /**
     * Partition array[left..right] around pivotValue.
     * Returns final pivot index.
     * Uses Lomuto partitioning and updates metrics.
     */
    private int partitionAroundPivot(int[] array, int left, int right, int pivotValue) {
        // find pivot index (first occurrence)
        int pivotIndex = left;
        while (pivotIndex <= right && array[pivotIndex] != pivotValue) pivotIndex++;
        if (pivotIndex > right) {
            // pivotValue not found (shouldn't happen), pick right as pivot
            pivotIndex = right;
        }
        // move pivot to end
        util.swap(array, pivotIndex, right);
        metrics.incAllocations();

        int store = left;
        for (int i = left; i < right; i++) {
            metrics.incCounter(); // comparison
            if (array[i] < pivotValue) {
                util.swap(array, store, i);
                metrics.incAllocations();
                store++;
            }
        }
        // put pivot in its final place
        util.swap(array, store, right);
        metrics.incAllocations();
        return store;
    }

    /**
     * Return median of an int[] array by selecting its middle after sorting small array.
     * We implement a simple selection (copy + sort is fine because medians[] is small ~ n/5).
     */
    private int selectMedianOfArray(int[] arr) {
        // arr is small (n/5); sort it to get median
        insertionSort(arr);
        return arr[arr.length / 2];
    }

    /* -------------------- small helpers -------------------- */

    // insertion sort for small arrays (used for groups of <=5)
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

    // insertion sort for whole small array (medians)
    private void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
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