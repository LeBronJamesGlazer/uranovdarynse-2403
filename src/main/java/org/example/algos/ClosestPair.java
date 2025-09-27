package org.example.algos;

import org.example.metrics.Metrics;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Divide-and-conquer Closest Pair of Points algorithm (simplified).
 */
public class ClosestPair {

    private final Metrics metrics;

    public ClosestPair(Metrics metrics) {
        this.metrics = metrics;
    }

    /** Point structure */
    public static class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    /** Result: two points + distance */
    public static class Result {
        public final Point a, b;
        public final double dist;
        public Result(Point a, Point b, double dist) {
            this.a = a; this.b = b; this.dist = dist;
        }
    }

    /** Public entry */
    public Result findClosest(Point[] pts) {
        if (pts == null || pts.length < 2)
            throw new IllegalArgumentException("Need at least 2 points");

        metrics.start();

        Point[] sorted = Arrays.copyOf(pts, pts.length);
        Arrays.sort(sorted, Comparator.comparingDouble(p -> p.x));
        metrics.incAllocations();

        Result res = closest(sorted, 0, sorted.length - 1, 1);

        metrics.stop();
        return res;
    }

    /** Recursive divide-and-conquer */
    private Result closest(Point[] pts, int left, int right, int depth) {
        metrics.updateDepth(depth);

        int n = right - left + 1;

        // base cases
        if (n == 2) {
            metrics.incCounter();
            return new Result(pts[left], pts[right], dist(pts[left], pts[right]));
        }
        if (n == 3) {
            return bruteForce(pts, left, right);
        }

        int mid = (left + right) / 2;
        Result leftRes = closest(pts, left, mid, depth + 1);
        Result rightRes = closest(pts, mid + 1, right, depth + 1);

        Result best = (leftRes.dist < rightRes.dist) ? leftRes : rightRes;
        double d = best.dist;

        // collect strip points
        double midX = pts[mid].x;
        Point[] strip = new Point[n];
        int stripSize = 0;
        for (int i = left; i <= right; i++) {
            if (Math.abs(pts[i].x - midX) < d) {
                strip[stripSize++] = pts[i];
            }
        }
        metrics.incAllocations();

        // sort strip by y
        Arrays.sort(strip, 0, stripSize, Comparator.comparingDouble(p -> p.y));
        metrics.incAllocations();

        // check neighbors in strip
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < d; j++) {
                metrics.incCounter();
                double dist = dist(strip[i], strip[j]);
                if (dist < d) {
                    d = dist;
                    best = new Result(strip[i], strip[j], d);
                }
            }
        }

        return best;
    }

    /** Brute force for 2–3 points */
    private Result bruteForce(Point[] pts, int left, int right) {
        Result best = new Result(pts[left], pts[left + 1], dist(pts[left], pts[left + 1]));
        metrics.incCounter();
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                metrics.incCounter();
                double d = dist(pts[i], pts[j]);
                if (d < best.dist) {
                    best = new Result(pts[i], pts[j], d);
                }
            }
        }
        return best;
    }

    /** Euclidean distance */
    private double dist(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}