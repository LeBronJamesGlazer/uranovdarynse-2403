package org.example.bench;

import org.example.algos.DeterministicSelect;
import org.example.metrics.Metrics;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2)                 // number of JVM forks
@Warmup(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 8, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class SelectVsSortBench {

    @State(Scope.Thread)
    public static class RandArrayState {

        @Param({"1000", "10000", "100000"}) // sizes to benchmark; adjust as needed
        public int n;

        public int[] baseArray;
        private Random rnd;

        @Setup(Level.Trial)
        public void setup() {
            rnd = new Random(42);
            baseArray = new int[n];
            for (int i = 0; i < n; i++) baseArray[i] = rnd.nextInt();
        }

        public int[] fresh() {
            return Arrays.copyOf(baseArray, baseArray.length);
        }
    }

    @Benchmark
    public void benchDeterministicSelect(RandArrayState state, Blackhole bh) {
        int[] arr = state.fresh();
        Metrics m = new Metrics("select-jmh");
        DeterministicSelect sel = new DeterministicSelect(m);
        // median index (0-based)
        int k = arr.length / 2;
        int result = sel.select(arr, k);
        bh.consume(result);
    }

    @Benchmark
    public void benchSortThenIndex(RandArrayState state, Blackhole bh) {
        int[] arr = state.fresh();
        Arrays.sort(arr);
        int res = arr[arr.length / 2];
        bh.consume(res);
    }
}