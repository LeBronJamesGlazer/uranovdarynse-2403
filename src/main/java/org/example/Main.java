package org.example;

import org.example.metrics.*;
import org.example.algos.*;
import org.example.utils.XLSXWriter;


import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -cp target/Assignment1_Daa-1.0-SNAPSHOT.jar org.example.Main <algorithm|all> <n> <outfile>");
            return;
        }

        String algoArg = args[0].toLowerCase();
        int n = Integer.parseInt(args[1]);
        String outFile = args.length >= 3 ? args[2] : "metrics.csv";

        // which algorithms to run
        List<String> algos = new ArrayList<>();
        if (algoArg.equals("all")) {
            algos.addAll(Arrays.asList("mergesort", "quicksort", "closest", "select"));
        } else {
            algos.addAll(Arrays.asList(algoArg.split(",")));
        }

        // Writer: CSV or XLSX depending on extension
        if (outFile.endsWith(".xlsx")) {
            try (XLSXWriter writer = new XLSXWriter(outFile)) {
                for (String algo : algos) {
                    Metrics m = runAlgo(algo, n);
                    if (m != null) writer.write(m);
                }
            }
        } else {
            try (CSVWriter writer = new CSVWriter(outFile)) {
                for (String algo : algos) {
                    Metrics m = runAlgo(algo, n);
                    if (m != null) writer.write(m);
                }
            }
        }

        System.out.println("Done. Metrics written to " + outFile);
    }

    private static Metrics runAlgo(String algo, int n) {
        Metrics metrics = new Metrics(algo);
        Random rnd = new Random(42);

        switch (algo) {
            case "mergesort" -> {
                int[] arr = rnd.ints(n, 0, 100000).toArray();
                MergeSort ms = new MergeSort(metrics, 16);
                ms.sort(arr);
            }
            case "quicksort" -> {
                int[] arr = rnd.ints(n, 0, 100000).toArray();
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
            case "select" -> {
                int[] arr = rnd.ints(n, 0, 100000).toArray();
                DeterministicSelect sel = new DeterministicSelect(metrics);
                int k = arr.length / 2;
                sel.select(arr, k);
            }
            default -> {
                System.err.println("Unknown algorithm: " + algo);
                return null;
            }
        }

        return metrics;
    }
}