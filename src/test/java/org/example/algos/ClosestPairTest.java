package org.example.algos;

import org.example.metrics.CSVWriter;
import org.example.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClosestPairTest {

    @Test
    void testTwoPoints() throws Exception {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4)
        };
        Metrics m = new Metrics("Closest2");
        ClosestPair cp = new ClosestPair(m);
        ClosestPair.Result r = cp.findClosest(pts);
        System.out.println(m);

        assertEquals(5.0, r.dist, 1e-9);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(m);
        }
    }

    @Test
    void testSquare() throws Exception {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(0, 1),
                new ClosestPair.Point(1, 0),
                new ClosestPair.Point(1, 1)
        };
        Metrics m = new Metrics("ClosestSquare");
        ClosestPair cp = new ClosestPair(m);
        ClosestPair.Result r = cp.findClosest(pts);
        System.out.println(m);

        assertEquals(1.0, r.dist, 1e-9);

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(m);
        }
    }

    @Test
    void testRandom100() throws Exception {
        int n = 100;
        Random rnd = new Random(42);
        ClosestPair.Point[] pts = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new ClosestPair.Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
        }

        Metrics m = new Metrics("ClosestRand100");
        ClosestPair cp = new ClosestPair(m);
        ClosestPair.Result r = cp.findClosest(pts);
        System.out.println(m);

        assertTrue(r.dist >= 0 && Double.isFinite(r.dist));

        try (CSVWriter csv = new CSVWriter("metrics.csv")) {
            csv.write(m);
        }
    }
}