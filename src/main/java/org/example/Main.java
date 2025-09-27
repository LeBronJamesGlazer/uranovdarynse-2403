package org.example;

import org.example.metrics.CSVWriter;
import org.example.metrics.Metrics;
import org.example.algos.MergeSort;
import org.example.algos.QuickSort;
import org.example.algos.ClosestPair;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java -cp target/Assignment1_Daa-1.0-SNAPSHOT.jar org.example.Main <algorithm> <n> <csvfile>");
            return;
        }

        String algo = args[0].toLowerCase();
        int n = Integer.parseInt(args[1]);
        String csvFile = args[2];

        Metrics metrics = new Metrics(algo);

        int[] arr = new int[n];
        Random rnd = new Random(42);
        for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(100000);

        switch (algo) {
            case "mergesort" -> {
                MergeSort ms = new MergeSort(metrics, 16);
                ms.sort(arr);
            }
            case "quicksort" -> {
                QuickSort qs = new QuickSort(metrics);
                qs.sort(arr);
            }
            case "closest" -> {
                ClosestPair.Point[] pts = new ClosestPair.Point[n];
                for (int i = 0; i < n; i++) {
                    pts[i] = new ClosestPair.Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
                }
                ClosestPair cp = new ClosestPair(metrics);
                cp.findClosest(pts);
            }
            default -> {
                System.err.println("Unknown algorithm: " + algo);
                return;
            }
        }

        try (CSVWriter csv = new CSVWriter(csvFile)) {
            csv.write(metrics);
        }

        System.out.println("Done. Metrics written to " + csvFile);
    }
}