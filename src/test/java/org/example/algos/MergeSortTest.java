package org.example.algos;

import org.example.metrics.CSVWriter;
import org.example.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeSortTest {

    @Test
    void testSmallArray() throws Exception {
        int[] array = {5, 2, 4, 1, 3};
        int[] expected = {1, 2, 3, 4, 5};

        Metrics metrics = new Metrics("MergeSortSmall");
        MergeSort sorter = new MergeSort(metrics, 4); // cutoff = 4
        sorter.sort(array);
        System.out.println(metrics);

        assertArrayEquals(expected, array);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(metrics);
        }
    }

    @Test
    void testSortedArray() throws Exception {
        int[] array = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        Metrics metrics = new Metrics("MergeSortSorted");
        MergeSort sorter = new MergeSort(metrics, 4);
        sorter.sort(array);
        System.out.println(metrics);

        assertArrayEquals(expected, array);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(metrics);
        }
    }

    @Test
    void testReverseArray() throws Exception {
        int[] array = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        Metrics metrics = new Metrics("MergeSortReverse");
        MergeSort sorter = new MergeSort(metrics, 4);
        sorter.sort(array);
        System.out.println(metrics);

        assertArrayEquals(expected, array);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(metrics);
        }
    }

    @Test
    void testRandomLargeArray() throws Exception {
        Random rand = new Random();
        int size = 1000;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = rand.nextInt(10000);

        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        Metrics metrics = new Metrics("MergeSortLarge");
        MergeSort sorter = new MergeSort(metrics, 10);
        sorter.sort(array);
        System.out.println(metrics);

        assertArrayEquals(expected, array);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(metrics);
        }
    }
}