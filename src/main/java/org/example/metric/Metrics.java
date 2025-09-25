package org.example.metric;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
    private String algorithmName = "unknown";

    private final AtomicLong counter = new AtomicLong(0);        // number of operations
    private final AtomicLong allocations = new AtomicLong(0);    // number of allocations
    private final AtomicLong maxDepth = new AtomicLong(0);       // max recursion depth
    private long startTime;
    private long runTime;

    private final Map<String, AtomicLong> custom =
            Collections.synchronizedMap(new LinkedHashMap<>());

    public Metrics(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void start() {
        reset();
        startTime = System.nanoTime();  // start measuring runtime
    }

    public void stop() {
        runTime = System.nanoTime() - startTime;  // calculate runtime
    }

    public void reset() {
        counter.set(0);
        allocations.set(0);
        maxDepth.set(0);
        runTime = 0;
        custom.clear();
    }

    public void incCounter() { counter.incrementAndGet(); }
    public void incAllocations() { allocations.incrementAndGet(); }

    public void updateDepth(long depth) {
        maxDepth.accumulateAndGet(depth, Math::max);  // keep max depth
    }

    public void addCustom(String key, long delta) {
        custom.computeIfAbsent(key, k -> new AtomicLong(0)).addAndGet(delta);
    }

    public String getAlgorithmName() { return algorithmName; }
    public long getCounter() { return counter.get(); }
    public long getAllocations() { return allocations.get(); }
    public long getMaxDepth() { return maxDepth.get(); }
    public long getRunTime() { return runTime; }

    public Map<String, Long> getCustom() {
        Map<String, Long> result = new LinkedHashMap<>();
        custom.forEach((k,v) -> result.put(k, v.get()));
        return result;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "algorithm='" + algorithmName + '\'' +
                ", counter=" + getCounter() +
                ", allocations=" + getAllocations() +
                ", maxDepth=" + getMaxDepth() +
                ", runTime(ns)=" + getRunTime() +
                ", custom=" + getCustom() +
                '}';
    }
}