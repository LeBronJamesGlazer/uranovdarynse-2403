package org.example.algos;

import org.example.metrics.CSVWriter;
import org.example.metrics.Metrics;
import org.example.utils.util;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectTest {

    @Test
    void testSmallArrayAllK() throws Exception {
        int[] array = {5, 2, 8, 1, 3};
        int[] sorted = Arrays.copyOf(array, array.length);
        Arrays.sort(sorted);

        for (int k = 0; k < array.length; k++) {
            int[] copy = Arrays.copyOf(array, array.length);
            Metrics metrics = new Metrics("SelectSmall_k" + k);
            DeterministicSelect sel = new DeterministicSelect(metrics);
            int result = sel.select(copy, k);
            System.out.println("k=" + k + " -> " + result + " | " + metrics);
            assertEquals(sorted[k], result);

            try (CSVWriter csv = new CSVWriter("metrics.csv")) {
                csv.write(metrics);
            }
        }
    }

    @Test
    void testSortedArrayVariousK() throws Exception {
        int[] array = {1,2,3,4,5,6,7};
        int[] sorted = Arrays.copyOf(array, array.length);

        for (int k = 0; k < array.length; k++) {
            Metrics metrics = new Metrics("SelectSorted_k" + k);
            DeterministicSelect sel = new DeterministicSelect(metrics);
            int res = sel.select(Arrays.copyOf(array, array.length), k);
            System.out.println("sorted k=" + k + " -> " + res + " | " + metrics);
            assertEquals(sorted[k], res);

            try (CSVWriter csv = new CSVWriter("metrics.csv")) {
                csv.write(metrics);
            }
        }
    }

    @Test
    void testReverseArrayEdgeKs() throws Exception {
        int[] array = {9,8,7,6,5,4,3,2,1};
        int[] sorted = Arrays.copyOf(array, array.length);
        Arrays.sort(sorted);

        Metrics metricsMin = new Metrics("SelectReverse_k0");
        DeterministicSelect selMin = new DeterministicSelect(metricsMin);
        int min = selMin.select(Arrays.copyOf(array, array.length), 0);
        System.out.println("min -> " + min + " | " + metricsMin);
        assertEquals(sorted[0], min);
        try (CSVWriter csv = new CSVWriter("metrics.csv")) { csv.write(metricsMin); }

        Metrics metricsMax = new Metrics("SelectReverse_kLast");
        DeterministicSelect selMax = new DeterministicSelect(metricsMax);
        int max = selMax.select(Arrays.copyOf(array, array.length), array.length - 1);
        System.out.println("max -> " + max + " | " + metricsMax);
        assertEquals(sorted[array.length - 1], max);
        try (CSVWriter csv = new CSVWriter("metrics.csv")) { csv.write(metricsMax); }
    }

    @Test
    void testRandomLarge() throws Exception {
        Random rnd = new Random(123);
        int n = 1000;
        int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = rnd.nextInt(10000);

        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        // test several k's including median
        int[] ks = {0, 1, 2, n/2, n-3, n-1};
        for (int k : ks) {
            Metrics metrics = new Metrics("SelectRandom_k" + k);
            DeterministicSelect sel = new DeterministicSelect(metrics);
            int res = sel.select(Arrays.copyOf(array, array.length), k);
            System.out.println("random k=" + k + " -> " + res + " | " + metrics);
            assertEquals(expected[k], res);

            try (CSVWriter csv = new CSVWriter("metrics.csv")) { csv.write(metrics); }
        }
    }
}